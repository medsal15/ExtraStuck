package com.medsal15.items.projectiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface ICreateArrow {
    AbstractArrow createArrow(@Nonnull Level level, @Nonnull ItemStack ammo, @Nonnull LivingEntity shooter,
            @Nullable ItemStack weapon);

}
