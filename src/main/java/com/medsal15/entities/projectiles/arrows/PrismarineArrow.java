package com.medsal15.entities.projectiles.arrows;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PrismarineArrow extends AbstractArrow {
    public PrismarineArrow(EntityType<? extends PrismarineArrow> entityType, Level level) {
        super(entityType, level);
    }

    public PrismarineArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.PRISMARINE_ARROW.get(), shooter, level, pickup, weapon);
    }

    public PrismarineArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.PRISMARINE_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.PRISMARINE_ARROW.get());
    }

    @Override
    protected float getWaterInertia() {
        return .99F;
    }
}
