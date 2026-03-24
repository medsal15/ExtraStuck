package com.medsal15.items.food;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class BurningFood extends Item {
    private final float seconds;

    public BurningFood(Properties properties, float seconds) {
        super(properties);
        this.seconds = seconds;
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity) {
        entity.igniteForSeconds(seconds);
        return super.finishUsingItem(stack, level, entity);
    }
}
