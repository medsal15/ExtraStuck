package com.medsal15.subevents;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.blockentities.ReactorBlockEntity;
import com.medsal15.items.ESEnergyStorage;
import com.medsal15.items.ESItems;
import com.medsal15.items.guns.GunContainer;
import com.medsal15.items.shields.ESShield;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

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

        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ESBlockEntities.CHARGER.get(),
                ChargerBlockEntity::getEnergyHandler);
        event.registerBlockEntity(Capabilities.EnergyStorage.BLOCK, ESBlockEntities.REACTOR.get(),
                ReactorBlockEntity::getEnergyHandler);

        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, ESBlockEntities.REACTOR.get(),
                ReactorBlockEntity::getFluidHandler);
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
    public static void onDamage(final LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.getHealth() > event.getAmount())
            return;

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
    }

    @SubscribeEvent
    public static void onDeath(final LivingDeathEvent event) {
        dropBoondollars(event);
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
}
