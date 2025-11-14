package com.medsal15.items.food;

import javax.annotation.Nonnull;

import com.mraof.minestuck.item.foods.DrinkableItem;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Level.ExplosionInteraction;

public class RocketJump extends DrinkableItem {
    public RocketJump(Properties properties) {
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
