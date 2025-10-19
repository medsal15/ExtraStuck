package com.medsal15.items.food;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class HotCakeSlice extends Item {
    public HotCakeSlice(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull LivingEntity entity) {
        entity.igniteForSeconds(4);
        return super.finishUsingItem(stack, level, entity);
    }
}
