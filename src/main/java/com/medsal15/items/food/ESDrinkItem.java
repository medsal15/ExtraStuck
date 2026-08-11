package com.medsal15.items.food;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;

public class ESDrinkItem extends ESFoodItem {
    public ESDrinkItem(Properties properties) {
        super(properties);
    }

    public UseAnim getUseAnimation(@Nonnull ItemStack stack) {
        return UseAnim.DRINK;
    }
}
