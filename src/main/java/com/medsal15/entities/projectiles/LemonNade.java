package com.medsal15.entities.projectiles;

import javax.annotation.Nonnull;

import com.medsal15.items.ESItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class LemonNade extends ThrowableItemProjectile {
    public LemonNade(EntityType<? extends LemonNade> type, Level level) {
        super(type, level);
    }

    public LemonNade(EntityType<? extends LemonNade> type, double x, double y, double z, Level level) {
        super(type, x, y, z, level);
    }

    public LemonNade(EntityType<? extends LemonNade> type, LivingEntity thrower, Level level) {
        super(type, thrower, level);
    }

    @Override
    protected Item getDefaultItem() {
        return ESItems.LEMONNADE.get();
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        if (!level().isClientSide) {
            level().explode(null, result.getLocation().x, result.getLocation().y, result.getLocation().z, 4F,
                    Level.ExplosionInteraction.NONE);
        }
        discard();
    }
}
