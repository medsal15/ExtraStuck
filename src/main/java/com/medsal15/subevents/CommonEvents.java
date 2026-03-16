package com.medsal15.subevents;

import java.util.List;
import java.util.Map;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.blockentities.ReactorBlockEntity;
import com.medsal15.blockentities.StorageBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.compat.ESCompatUtils;
import com.medsal15.compat.create.network.ESCreatePackets;
import com.medsal15.compat.curios.CuriosCapabilities;
import com.medsal15.compat.curios.ESCuriosEventsHandlers;
import com.medsal15.compat.curios.items.ESCuriosUtils;
import com.medsal15.data.ESLangProvider;
import com.medsal15.datamaps.ReactorFuel;
import com.medsal15.items.ESEnergyStorage;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.MoonCakeSliceColor;
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
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.weapon.MSToolType;
import com.mraof.minestuck.item.weapon.WeaponItem;
import com.mraof.minestuck.network.MSPacket;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CakeBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.OnDatapackSyncEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
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

        if (ESCompatUtils.isLoaded("curios")) {
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
        if (handleAntiDie(event))
            return;

        dropBoondollars(event);
        healAttacker(event);
        explodeDead(event);
        if (ESCompatUtils.isLoaded("curios")) {
            ESCuriosEventsHandlers.handleGummyRing(event);
        }
    }

    /**
     * Prevents death when holding an Anti Die
     * <p>
     * Heals from 1 to 6 (both inclusive) health, cancelling the event
     *
     * @return <code>true</code> if the event is cancelled
     */
    private static boolean handleAntiDie(final LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();

        InteractionHand hand;
        if (entity.getMainHandItem().is(ESItems.ANTI_DIE)) {
            hand = InteractionHand.MAIN_HAND;
        } else if (entity.getOffhandItem().is(ESItems.ANTI_DIE)) {
            hand = InteractionHand.OFF_HAND;
        } else {
            // Not holding anti die
            return false;
        }

        // Prevent death and consume anti die
        entity.level().playSeededSound(null, (int) entity.getX(), (int) entity.getY(), (int) entity.getZ(),
                SoundEvents.TOTEM_USE, SoundSource.PLAYERS, 1F, 1F, entity.level().random.nextLong());
        Minecraft minecraft = Minecraft.getInstance();
        GameRenderer renderer = minecraft.gameRenderer;
        renderer.displayItemActivation(ESItems.ANTI_DIE.toStack());
        event.setCanceled(true);
        entity.setItemInHand(hand, ItemStack.EMPTY);
        int health = entity.getRandom().nextInt(5) + 1;
        entity.setHealth(health);
        entity.sendSystemMessage(Component.translatable(ESLangProvider.ANTIDIE_HEAL, health));
        return true;
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

    /**
     * Heals 2 health to the attacker if using the Heartstabber
     */
    private static void healAttacker(final LivingDeathEvent event) {
        Entity attacker = event.getSource().getEntity();
        if (!(attacker instanceof LivingEntity livingEntity))
            return;

        ItemStack weapon = event.getSource().getWeaponItem();
        if (weapon == null || !weapon.is(ESItems.HEARTSTABBER))
            return;

        livingEntity.heal(2);
    }

    /**
     * Creates an explosion if using the Explosive Scoop
     */
    private static void explodeDead(final LivingDeathEvent event) {
        ItemStack weapon = event.getSource().getWeaponItem();
        if (weapon == null || !weapon.is(ESItems.EXPLOSIVE_SCOOP))
            return;

        Entity entity = event.getEntity();
        if (!entity.level().isClientSide) {
            Entity attacker = event.getSource().getEntity();
            entity.level().explode(attacker, entity.getX(), entity.getEyeY(), entity.getZ(), 4,
                    Level.ExplosionInteraction.NONE);
        }
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

        if (ESCompatUtils.isLoaded("create")) {
            ESCreatePackets.registerPayloadHandlers(event);
        }
    }

    public static void exec(MSPacket.PlayToServer packet, IPayloadContext context) {
        packet.execute(context, (ServerPlayer) context.player());
    }

    public static void execClient(MSPacket.PlayToClient packet, IPayloadContext context) {
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
     * Cancels application for Poison, Cosmic Plague, and Wither with valid Cosmic
     * Plague Armor
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
        if (ESCompatUtils.isLoaded("curios")) {
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
    public static void onRightClickBlock(final PlayerInteractEvent.RightClickBlock event) {
        ItemStack stack = event.getEntity().getItemInHand(event.getHand());

        boolean cake_cuttable = stack.is(ESTags.Items.FD_KNIVES);
        if (stack.getItem() instanceof WeaponItem weaponItem) {
            List<MSToolType> types = weaponItem.getToolTypes();
            if (types != null && types.contains(MSItemTypes.KNIFE_TOOL))
                cake_cuttable = true;
        }
        if (cake_cuttable)
            handleCakeCutting(event);
    }

    private static final Map<Holder<Block>, Holder<Item>> SUPPORTED_CAKES = Map.ofEntries(
            Map.entry(MSBlocks.APPLE_CAKE, ESItems.APPLE_CAKE_SLICE),
            Map.entry(MSBlocks.BLUE_CAKE, ESItems.BLUE_CAKE_SLICE),
            Map.entry(MSBlocks.COLD_CAKE, ESItems.COLD_CAKE_SLICE),
            Map.entry(MSBlocks.RED_CAKE, ESItems.RED_CAKE_SLICE),
            Map.entry(MSBlocks.HOT_CAKE, ESItems.HOT_CAKE_SLICE),
            Map.entry(MSBlocks.REVERSE_CAKE, ESItems.REVERSE_CAKE_SLICE),
            Map.entry(MSBlocks.FUCHSIA_CAKE, ESItems.FUCHSIA_CAKE_SLICE),
            Map.entry(MSBlocks.NEGATIVE_CAKE, ESItems.NEGATIVE_CAKE_SLICE),
            Map.entry(MSBlocks.CARROT_CAKE, ESItems.CARROT_CAKE_SLICE),
            Map.entry(MSBlocks.CHOCOLATEY_CAKE, ESItems.CHOCOLATEY_CAKE_SLICE),
            Map.entry(ESBlocks.LEMON_CAKE, ESItems.LEMON_CAKE_SLICE));

    /**
     * Handles cake cutting using either items tagged with farmer's delight knives
     * or minestuck weapons that are daggers
     */
    private static void handleCakeCutting(final PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        BlockPos pos = event.getPos();
        BlockState state = event.getLevel().getBlockState(pos);

        for (Map.Entry<Holder<Block>, Holder<Item>> entry : SUPPORTED_CAKES.entrySet()) {
            Holder<Block> block = entry.getKey();
            if (state.is(block)) {
                int bites = state.getValue(CakeBlock.BITES);
                if (bites < 6) {
                    level.setBlock(pos, state.setValue(CakeBlock.BITES, bites + 1), 3);
                } else {
                    level.removeBlock(pos, false);
                }

                ItemEntity itemEntity = new ItemEntity(level, pos.getX() + (bites * .1), pos.getY() + .2,
                        pos.getZ() + 0.5,
                        new ItemStack(entry.getValue()));
                itemEntity.setDeltaMovement(-.05, 0, 0);
                level.addFreshEntity(itemEntity);
                level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);

                event.setCancellationResult(InteractionResult.SUCCESS);
                event.setCanceled(true);
                break;
            }
        }

        // Special handling for moon cake, as there are 3 colors for the slices
        if (state.is(MSBlocks.MOON_CAKE)) {
            int bites = state.getValue(CakeBlock.BITES);
            ItemStack slice = ESItems.MOON_CAKE_SLICE.toStack();
            if (bites < 6) {
                level.setBlock(pos, state.setValue(CakeBlock.BITES, bites + 1), 3);
                slice.set(ESDataComponents.MOON_CAKE_SLICE_COLOR,
                        bites % 2 == 1 ? MoonCakeSliceColor.DERSE : MoonCakeSliceColor.PROSPIT);
            } else {
                level.removeBlock(pos, false);
            }

            ItemEntity itemEntity = new ItemEntity(level, pos.getX() + (bites * .1), pos.getY() + .2,
                    pos.getZ() + 0.5, slice);
            itemEntity.setDeltaMovement(-.05, 0, 0);
            level.addFreshEntity(itemEntity);
            level.playSound(null, pos, SoundEvents.WOOL_BREAK, SoundSource.PLAYERS, 0.8F, 0.8F);

            event.setCancellationResult(InteractionResult.SUCCESS);
            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onDataPackSync(final OnDatapackSyncEvent event) {
        Map<Item, CauldronInteraction> map = CauldronInteraction.WATER.map();
        for (var item : ESItems.ITEMS.getEntries()) {
            if (item.is(ItemTags.DYEABLE)) {
                map.put(item.get(), CauldronInteraction.DYED_ITEM);
            }
        }
    }
}
