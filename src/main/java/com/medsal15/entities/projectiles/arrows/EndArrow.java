package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class EndArrow extends AbstractArrow {
    public EndArrow(EntityType<? extends EndArrow> entityType, Level level) {
        super(entityType, level);
    }

    public EndArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.END_ARROW.get(), shooter, level, pickup, weapon);
    }

    public EndArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.END_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.END_ARROW.get());
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        var entity = result.getEntity();
        var baseDamage = getBaseDamage();
        if (!entity.onGround()) {
            setBaseDamage(baseDamage * 1.5);
        }

        super.onHitEntity(result);

        setBaseDamage(baseDamage);
    }
}
