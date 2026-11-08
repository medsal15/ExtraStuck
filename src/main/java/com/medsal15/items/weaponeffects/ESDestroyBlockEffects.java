package com.medsal15.items.weaponeffects;

import com.mraof.minestuck.item.weapon.OnHitEffect;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
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

    public static void dropSelf(ItemStack stack, Level level, BlockState state, BlockPos pos, LivingEntity entity) {
        if (!entity.getCommandSenderWorld().isClientSide && entity.getRandom().nextFloat() < .25) {
            ItemEntity item = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack.copy());
            item.getItem().setCount(1);
            item.setPickUpDelay(40);
            entity.level().addFreshEntity(item);
            stack.shrink(1);
            entity.sendSystemMessage(
                    Component.translatable(item.getItem().getDescriptionId() + "." + OnHitEffect.SORD_DROP_MESSAGE));
        }
    }
}
