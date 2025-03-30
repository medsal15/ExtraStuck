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

public class LightningArrow extends AbstractArrow {
    private boolean landed;

    public LightningArrow(EntityType<? extends LightningArrow> entityType, Level level) {
        super(entityType, level);
    }

    public LightningArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.LIGHTNING_ARROW.get(), shooter, level, pickup, weapon);
    }

    public LightningArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.LIGHTNING_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.LIGHTNING_ARROW.get());
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);
        if (!landed) {
            var bolt = EntityType.LIGHTNING_BOLT.create(level());
            if (bolt != null) {
                bolt.moveTo(this.getX(), this.getY(), this.getZ());
                level().addFreshEntity(bolt);
            }
            landed = true;
        }
    }
}
