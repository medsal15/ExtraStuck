package com.medsal15.entities.projectiles;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class UraniumRod extends AbstractArrow {
    public UraniumRod(EntityType<? extends UraniumRod> type, Level level) {
        super(type, level);
    }

    public UraniumRod(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.URANIUM_ROD.get(), shooter, level, pickup, weapon);
    }

    public UraniumRod(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.URANIUM_ROD.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected ItemStack getPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);

        Entity entity = result.getEntity();
        if (entity instanceof LivingEntity livingEntity) {
            livingEntity.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 2));
        }
    }
}
