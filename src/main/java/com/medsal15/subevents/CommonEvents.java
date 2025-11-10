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

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
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
}
