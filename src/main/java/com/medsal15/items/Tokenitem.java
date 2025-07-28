package com.medsal15.items;

import javax.annotation.Nonnull;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class Tokenitem extends Item {
    // 30 seconds cooldown
    private static final int COOLDOWN = 30 * 20;

    public Tokenitem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel) {
            int amplifier = 0;
            int duration = 60 * 20; // 1 minute
            if (player.hasEffect(MobEffects.LUCK)) {
                MobEffectInstance luck = player.getEffect(MobEffects.LUCK);
                if (luck != null) {
                    // Cap at level 4
                    amplifier = Math.min(luck.getAmplifier() + 1, 4);
                    duration = Math.max(duration, luck.getDuration());
                }
            }
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, duration, amplifier));

            player.getCooldowns().addCooldown(stack.getItem(), COOLDOWN);
            if (!player.isCreative())
                stack.shrink(1);
            return InteractionResultHolder.consume(stack);
        }
        return super.use(level, player, hand);
    }
}
