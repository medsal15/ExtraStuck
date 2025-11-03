package com.medsal15.items.weaponeffects;

import com.medsal15.config.ConfigCommon;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.SteamFuelComponent;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public final class ESRightClickEffects {
    public static final InteractionResultHolder<ItemStack> steamWeapon(Level level, Player player,
            InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Toggle
        if (player.isShiftKeyDown()) {
            SteamFuelComponent fuel = stack.getOrDefault(ESDataComponents.STEAM_FUEL, new SteamFuelComponent(0, false));
            if (fuel.burning()) {
                stack.set(ESDataComponents.STEAM_FUEL, fuel.extinguish());
                return InteractionResultHolder.success(stack);
            }
            int fuel_needed = ConfigCommon.STEAM_FUEL_CONSUME.get();
            if (fuel.fuel() >= fuel_needed) {
                stack.set(ESDataComponents.STEAM_FUEL, fuel.toggle(true));
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }
}
