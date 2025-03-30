package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.Explosion.BlockInteraction;
import net.minecraft.world.phys.HitResult;

public class ExplosiveArrow extends AbstractArrow {
    private boolean landed;

    public ExplosiveArrow(EntityType<? extends ExplosiveArrow> entityType, Level level) {
        super(entityType, level);
    }

    public ExplosiveArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.EXPLOSIVE_ARROW.get(), shooter, level, pickup, weapon);
    }

    public ExplosiveArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.EXPLOSIVE_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.EXPLOSIVE_ARROW.get());
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        if (!landed) {
            var exp = new Explosion(level(), this, this.getX(), this.getY(), this.getZ(), 2F, wasOnFire,
                    BlockInteraction.KEEP);
            exp.explode();
            exp.finalizeExplosion(true);
            landed = true;
        }
    }
}
