package com.medsal15.items.weaponeffects;

import java.util.Optional;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import com.medsal15.config.ConfigCommon;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.components.SteamFuelComponent;
import com.mraof.minestuck.client.util.MagicEffect;
import com.mraof.minestuck.item.weapon.MagicRangedRightClickEffect;
import com.mraof.minestuck.player.PlayerData;
import com.mraof.minestuck.util.MSAttachments;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.util.FakePlayer;

public final class ESRightClickEffects {
    public static final InteractionResultHolder<ItemStack> steamWeapon(Level level, Player player,
            InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Toggle
        if (player.isShiftKeyDown()) {
            SteamFuelComponent fuel = stack.getOrDefault(ESDataComponents.STEAM_FUEL, new SteamFuelComponent(0, false));
            if (fuel.burning()) {
                stack.set(ESDataComponents.STEAM_FUEL, fuel.extinguish());
                player.playSound(SoundEvents.FIRE_EXTINGUISH);
                return InteractionResultHolder.success(stack);
            }
            int fuel_needed = ConfigCommon.STEAM_FUEL_CONSUME.get();
            if (fuel.fuel() >= fuel_needed) {
                stack.set(ESDataComponents.STEAM_FUEL, fuel.toggle(true));
                player.playSound(SoundEvents.FLINTANDSTEEL_USE);
                return InteractionResultHolder.success(stack);
            }
        }

        return InteractionResultHolder.pass(stack);
    }

    public static final MagicRangedRightClickEffect CASH_MAGIC = new CashMagicRangedEffect(64, 4, null, null, 1F,
            MagicEffect.RangedType.GREEN);

    private static class CashMagicRangedEffect extends MagicRangedRightClickEffect {
        public CashMagicRangedEffect(int distance, int damage, Supplier<MobEffectInstance> effect,
                Supplier<SoundEvent> sound, float pitch, @Nullable MagicEffect.RangedType type) {
            super(distance, damage, effect, sound, pitch, type);
        }

        @Override
        protected float calculateDamage(ServerPlayer player, LivingEntity target, int damage) {
            if (!(player instanceof FakePlayer)) {
                Optional<PlayerData> odata = PlayerData.get(player);
                if (odata.isPresent()) {
                    PlayerData data = odata.get();
                    long boondollars = data.getData(MSAttachments.BOONDOLLARS);
                    if (boondollars > 0)
                        return (float) damage + (float) Math.log10(boondollars);
                }
            }
            return (float) damage;
        }
    }
}
