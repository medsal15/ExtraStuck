package com.medsal15.items.food;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.Level;

public class SourBombCandy extends Item {
    public SourBombCandy(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level,
            @Nonnull LivingEntity livingEntity) {
        level.explode(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), .1F,
                ExplosionInteraction.NONE);
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
