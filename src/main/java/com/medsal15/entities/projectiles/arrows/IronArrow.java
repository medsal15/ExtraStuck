package com.medsal15.entities.projectiles.arrows;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IronArrow extends AbstractArrow {
    public IronArrow(EntityType<? extends IronArrow> entityType, Level level) {
        super(entityType, level);
        setBaseDamage(3D);
    }

    public IronArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.IRON_ARROW.get(), shooter, level, pickup, weapon);
        setBaseDamage(3D);
    }

    public IronArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.IRON_ARROW.get(), x, y, z, level, pickup, weapon);
        setBaseDamage(3D);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.IRON_ARROW.get());
    }
}
