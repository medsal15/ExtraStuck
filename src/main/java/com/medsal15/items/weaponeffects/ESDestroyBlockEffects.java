package com.medsal15.items.weaponeffects;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public final class ESDestroyBlockEffects {
    public static void buffOnDoorBreak(ItemStack stack, Level level, BlockState state, BlockPos pos,
            LivingEntity entity) {
        if (state.is(BlockTags.DOORS)) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100));
            entity.addEffect(new MobEffectInstance(MobEffects.DIG_SPEED, 100));
        }
    }
}
