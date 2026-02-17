package com.medsal15.subevents;

import org.jetbrains.annotations.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.blockentities.ReactorBlockEntity;
import com.medsal15.blockentities.StorageBlockEntity;
import com.medsal15.compat.curios.CuriosCapabilities;
import com.medsal15.compat.curios.items.ESCuriosUtils;
import com.medsal15.datamaps.ReactorFuel;
import com.medsal15.items.ESEnergyStorage;
import com.medsal15.items.ESItems;
import com.medsal15.items.guns.GunContainer;
import com.medsal15.items.shields.ESShield;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuNext;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuOpen;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuQuit;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuSave;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuSync;
import com.medsal15.network.ESPackets.MastermindAddAttempt;
import com.medsal15.network.ESPackets.MastermindDestroy;
import com.medsal15.network.ESPackets.MastermindDifficulty;
import com.medsal15.network.ESPackets.MastermindReset;
import com.medsal15.network.ESPackets.SyncBoondollarValues;
import com.medsal15.network.ESPackets.ToggleMode;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.network.MSPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.RegisterItemDecorationsEvent;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID)
public final class CommonEvents {
    @SubscribeEvent
    public static void registerCapabilities(final RegisterCapabilitiesEvent event) {
        event.registerItem(Capabilities.EnergyStorage.ITEM,
                (stack, u) -> new ESEnergyStorage(stack),
                ESItems.FLUX_SHIELD.get(), ESItems.OVERCHARGED_MAGNEFORK.get(), ESItems.UNDERCHARGED_MAGNEFORK.get(),
                ESItems.FIELD_CHARGER.get());

        event.registerItem(Capabilities.ItemHandler.ITEM,
                // TODO move to a subclass & method to get
                (stack, u) -> new GunContainer(1, stack),
                ESItems.HANDGUN.get());

        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ESBlockEntities.PRINTER.get(),
                PrinterBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ESBlockEntities.CHARGER.get(),
                ChargerBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ESBlockEntities.REACTOR.get(),
                ReactorBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ESBlockEntities.DOWEL_STORAGE.get(),
                StorageBlockEntity::getItemHandler);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, ESBlockEntities.CARD_STORAGE.get(),
                StorageBlockEntity::getItemHandler);

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ESBlockEntities.CHARGER.get(),
                ChargerBlockEntity::getEnergyHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ESBlockEntities.REACTOR.get(),
                ReactorBlockEntity::getEnergyHandler);

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ESBlockEntities.REACTOR.get(),
                ReactorBlockEntity::getFluidHandler);

        if (ModList.get().isLoaded("curios")) {
            CuriosCapabilities.registerCuriosCapabilities(event);
        }
    }

    @SubscribeEvent
    public static void onShieldBlock(final LivingShieldBlockEvent event) {
        // Ensure we're using a mod shield
        Item item = event.getEntity().getUseItem().getItem();
        if (item instanceof ESShield shield) {
            shield.onShieldBlock(event);
            return;
        }
    }

    @SubscribeEvent
    public static void onDeath(final LivingDeathEvent event) {
        handleAntiDie(event);
        if (event.isCanceled())
            return;

        dropBoondollars(event);
    }

    /**
     * Prevents death when holding an Anti Die
     * <p>
     * Heals from 1 to 6 (both inclusive) health, cancelling the event
     */
    private static void handleAntiDie(final LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        InteractionHand hand;
        if (entity.getMainHandItem().is(ESItems.ANTI_DIE)) {
            hand = InteractionHand.MAIN_HAND;
        } else if (entity.getOffhandItem().is(ESItems.ANTI_DIE)) {
            hand = InteractionHand.OFF_HAND;
        } else {
            // Not holding anti die
            return;
        }

        // Prevent death and consume anti die
        entity.level().playSeededSound(null, (int) entity.getX(), (int) entity.getY(), (int) entity.getZ(),
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1F, 1F, entity.level().random.nextLong());
        Minecraft minecraft = Minecraft.getInstance();
        GameRenderer renderer = minecraft.gameRenderer;
        renderer.displayItemActivation(ESItems.ANTI_DIE.toStack());
        event.setCanceled(true);
        entity.setItemInHand(hand, ItemStack.EMPTY);
        entity.setHealth(entity.getRandom().nextFloat() * 5 + 1);
    }

    /**
     * Drops boondollars based on enemy health, unless it's a player
     * <p>
     * 10% + 10% * Looting level of the time
     * <p>
     * Drops between 0 and (enemy max health / 10)
     */
    private static void dropBoondollars(final LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        // Deny players and enemies with too little health
        if (entity instanceof Player || entity.getMaxHealth() < 10)
            return;

        // Check that weapon does drop boondollars
        ItemStack weapon = event.getSource().getWeaponItem();
        if (weapon == null || !weapon.is(ESTags.Items.DROPS_BOONDOLLARS))
            return;

        // Roll the first dice to check if boondollars are dropped
        Level level = entity.level();
        int looting = weapon.getEnchantmentLevel(
                level.registryAccess().lookupOrThrow(Registries.ENCHANTMENT).getOrThrow(Enchantments.LOOTING));
        float chance = .1F + ((float) looting) / 10;
        if (entity.getRandom().nextFloat() >= chance)
            return;

        // Roll the second dice to get the amount to drop
        float max = entity.getMaxHealth() / 10;
        long amount = (long) (entity.getRandom().nextFloat() * max);
        if (amount <= 0)
            return;

        // Drop boondollars where the entity died
        ItemStack boondollars = MSItems.BOONDOLLARS.toStack();
        boondollars = BoondollarsItem.setCount(boondollars, amount);
        ItemEntity drops = new ItemEntity(level, entity.getX(), entity.getY(), entity.getZ(), boondollars);
        entity.level().addFreshEntity(drops);
    }

    @SubscribeEvent
    public static void registerPayloadHandlers(final RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(ExtraStuck.MODID);

        registrar.playToServer(ToggleMode.ID, ToggleMode.STREAM_CODEC, CommonEvents::exec);
        registrar.playToServer(MastermindAddAttempt.ID, MastermindAddAttempt.STREAM_CODEC, CommonEvents::exec);
        registrar.playToServer(MastermindDestroy.ID, MastermindDestroy.STREAM_CODEC, CommonEvents::exec);
        registrar.playToServer(MastermindReset.ID, MastermindReset.STREAM_CODEC, CommonEvents::exec);
        registrar.playToServer(MastermindDifficulty.ID, MastermindDifficulty.STREAM_CODEC, CommonEvents::exec);
        registrar.playToServer(CraftingModusRecipeMenuOpen.ID, CraftingModusRecipeMenuOpen.STREAM_CODEC,
                CommonEvents::exec);
        registrar.playToServer(CraftingModusRecipeMenuNext.ID, CraftingModusRecipeMenuNext.STREAM_CODEC,
                CommonEvents::exec);
        registrar.playToServer(CraftingModusRecipeMenuSave.ID, CraftingModusRecipeMenuSave.STREAM_CODEC,
                CommonEvents::exec);

        registrar.playToClient(SyncBoondollarValues.ID, SyncBoondollarValues.STREAM_CODEC, CommonEvents::execClient);
        registrar.playToClient(CraftingModusRecipeMenuSync.ID, CraftingModusRecipeMenuSync.STREAM_CODEC,
                CommonEvents::execClient);
        registrar.playToClient(CraftingModusRecipeMenuQuit.ID, CraftingModusRecipeMenuQuit.STREAM_CODEC,
                CommonEvents::execClient);
    }

    private static void exec(MSPacket.PlayToServer packet, IPayloadContext context) {
        packet.execute(context, (ServerPlayer) context.player());
    }

    private static void execClient(MSPacket.PlayToClient packet, IPayloadContext context) {
        packet.execute(context);
    }

    @SubscribeEvent
    public static void registerDataMapTypes(final RegisterDataMapTypesEvent event) {
        event.register(ReactorFuel.REACTOR_MAP);
    }

    @SubscribeEvent
    public static void onEffectApplicable(final MobEffectEvent.Applicable event) {
        handleCosmicPlagueArmor(event);
    }

    /**
     * Rolls the CancelsEffectArmor::chanceToCancel
     */
    private static void handleCosmicPlagueArmor(final MobEffectEvent.Applicable event) {
        Entity entity = event.getEntity();
        if (!(entity instanceof LivingEntity livingEntity))
            return;

        int count = 0;
        for (ItemStack armorStack : livingEntity.getArmorSlots()) {
            if (armorStack.is(ESTags.Items.COSMIC_PLAGUE_ARMOR)) {
                count++;
            }
        }
        if (ModList.get().isLoaded("curios")) {
            count += ESCuriosUtils.countCosmicPlagueImmune(livingEntity);
        }

        if (count <= 0)
            return;

        if (event.getEffectInstance().getEffect().is(ESTags.MobEffects.COSMIC_PLAGUE_IMMUNITY)
                && (count >= 4 || entity.getRandom().nextFloat() * 4 < count)) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        } else if (event.getEffectInstance().getEffect().is(ESTags.MobEffects.COSMIC_PLAGUE_PARTIAL_IMMUNITY)
                && count >= 4
                && entity.getRandom().nextFloat() < .2) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    @SubscribeEvent
    public static void registerItemDecorations(final RegisterItemDecorationsEvent event) {
        event.register(ESItems.FLUX_SHIELD, CommonEvents::renderFEBar);
        event.register(ESItems.OVERCHARGED_MAGNEFORK, CommonEvents::renderFEBar);
        event.register(ESItems.UNDERCHARGED_MAGNEFORK, CommonEvents::renderFEBar);
        event.register(ESItems.FIELD_CHARGER, CommonEvents::renderFEBar);
    }

    private static boolean renderFEBar(GuiGraphics guiGraphics, Font font, ItemStack stack, int x, int y) {
        @SuppressWarnings("null")
        @Nullable
        IEnergyStorage energyHandler = Capabilities.EnergyStorage.ITEM.getCapability(stack, null);
        if (energyHandler != null && energyHandler.getMaxEnergyStored() > 0 && energyHandler.getEnergyStored() > 0) {
            int startx = x + 2;
            int starty = y + 11;
            // Lowers FE bar when damage is hidden
            if (!stack.isBarVisible())
                starty += 2;
            int width = Math
                    .round((float) energyHandler.getEnergyStored() * 13F / (float) energyHandler.getMaxEnergyStored());
            guiGraphics.fill(RenderType.GUI_OVERLAY, startx, starty, startx + 13, starty + 2, -16777216);
            guiGraphics.fill(RenderType.GUI_OVERLAY, startx, starty, startx + width, starty + 1, 0xFFFFFF00);
        }
        return false;
    }
}
