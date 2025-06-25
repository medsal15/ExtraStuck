package com.medsal15.items.projectiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface IAsProjectile {
    AbstractArrow asProjectile(@Nonnull Level level, double x, double y, double z, @Nonnull ItemStack stack,
            @Nullable ItemStack weapon);
}
