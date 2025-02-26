package com.medsal15.items.shields;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShieldItem;

/**
 * A normal shield but it cannot be repaired
 */
public class ESShield extends ShieldItem {
    public ESShield(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isValidRepairItem(@Nonnull ItemStack toRepair, @Nonnull ItemStack repair) {
        return false;
    }
}
