package com.medsal15.items.food;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level.ExplosionInteraction;
import net.minecraft.world.level.Level;

public class ExplosiveFood extends Item {
    private final float radius;

    public ExplosiveFood(Properties properties) {
        super(properties);
        radius = .1f;
    }

    public ExplosiveFood(Properties properties, float radius) {
        super(properties);
        this.radius = radius;
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level,
            @Nonnull LivingEntity livingEntity) {
        level.explode(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(), radius,
                ExplosionInteraction.NONE);
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
