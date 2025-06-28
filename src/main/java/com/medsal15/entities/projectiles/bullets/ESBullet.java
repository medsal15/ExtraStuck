package com.medsal15.entities.projectiles.bullets;

import javax.annotation.Nonnull;

import com.medsal15.items.ESDataComponents;
import com.medsal15.items.projectiles.IAsProjectile;
import com.medsal15.items.projectiles.ICreateArrow;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class ESBullet extends AbstractArrow {
    public ESBullet(EntityType<? extends ESBullet> type, Level level) {
        super(type, level);
        this.setNoGravity(true);
        // TODO use setSoundEvent to use a custom hit sound
    }

    public ESBullet(EntityType<? extends ESBullet> type, Level level, ItemStack pickup, LivingEntity shooter,
            ItemStack weapon) {
        super(type, shooter, level, pickup, weapon);

        this.setNoGravity(true);
        if (pickup.has(ESDataComponents.AMMO_DAMAGE)) {
            setBaseDamage((double) pickup.get(ESDataComponents.AMMO_DAMAGE));
        }
    }

    public ESBullet(EntityType<? extends ESBullet> type, Level level, double x, double y, double z, ItemStack pickup,
            ItemStack weapon) {
        super(type, x, y, z, level, pickup, weapon);

        this.setNoGravity(true);
        if (pickup.has(ESDataComponents.AMMO_DAMAGE)) {
            setBaseDamage((double) pickup.get(ESDataComponents.AMMO_DAMAGE));
        }
    }

    public static ICreateArrow createArrow(EntityType<? extends ESBullet> type) {
        return (Level level, ItemStack ammo, LivingEntity shooter, ItemStack weapon) -> new ESBullet(type, level, ammo,
                shooter, weapon);
    }

    public static IAsProjectile asProjectile(EntityType<? extends ESBullet> type) {
        return (Level level, double x, double y, double z, ItemStack stack, ItemStack weapon) -> new ESBullet(type,
                level, x, y, z, stack, weapon);
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        double damage = getBaseDamage();
        setBaseDamage(damage / this.getDeltaMovement().length());
        super.onHitEntity(result);
        setBaseDamage(damage);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ItemStack.EMPTY;
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        this.discard();
    }

    @Override
    protected SoundEvent getDefaultHitGroundSoundEvent() {
        return SoundEvents.METAL_PRESSURE_PLATE_CLICK_ON;
    }
}
