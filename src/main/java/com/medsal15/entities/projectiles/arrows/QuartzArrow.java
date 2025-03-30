package com.medsal15.entities.projectiles.arrows;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class QuartzArrow extends AbstractArrow {

    public QuartzArrow(EntityType<? extends QuartzArrow> entityType, Level level) {
        super(entityType, level);
        setBaseDamage(3D);
    }

    public QuartzArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.QUARTZ_ARROW.get(), shooter, level, pickup, weapon);
        setBaseDamage(3D);
    }

    public QuartzArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.QUARTZ_ARROW.get(), x, y, z, level, pickup, weapon);
        setBaseDamage(3D);
    }

    @Override
    public byte getPierceLevel() {
        return (byte) (super.getPierceLevel() + 1);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.QUARTZ_ARROW.get());
    }
}
