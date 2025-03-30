package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class GlassArrow extends AbstractArrow {
    public GlassArrow(EntityType<? extends GlassArrow> entityType, Level level) {
        super(entityType, level);
        setBaseDamage(5);
    }

    public GlassArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.GLASS_ARROW.get(), shooter, level, pickup, weapon);
        setBaseDamage(5);
    }

    public GlassArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.GLASS_ARROW.get(), x, y, z, level, pickup, weapon);
        setBaseDamage(5);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.GLASS_ARROW.get());
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        this.discard();
    }
}
