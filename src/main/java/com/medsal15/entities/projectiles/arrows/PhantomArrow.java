package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.EventHooks;

public class PhantomArrow extends AbstractArrow {
    protected int life = 0;

    public PhantomArrow(EntityType<? extends PhantomArrow> entityType, Level level) {
        super(entityType, level);
        noPhysics = true;
        setNoGravity(true);
    }

    public PhantomArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.PHANTOM_ARROW.get(), shooter, level, pickup, weapon);
        noPhysics = true;
        setNoGravity(true);
    }

    public PhantomArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.PHANTOM_ARROW.get(), x, y, z, level, pickup, weapon);
        noPhysics = true;
        setNoGravity(true);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ESItems.BLANK_ARROW.toStack();
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putShort("arrow_life", (short) life);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        life = compound.getShort("arrow_life");
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 delta = getDeltaMovement();
        Vec3 position = position();
        Vec3 next = position.add(delta);
        EntityHitResult hitResult = findHitEntity(position, next);
        if (hitResult != null && hitResult.getType() != HitResult.Type.MISS) {
            Entity hit = hitResult.getEntity();
            Entity owner = getOwner();
            if (hit instanceof Player playerHit && owner instanceof Player playerOwner
                    && !playerOwner.canHarmPlayer(playerHit)) {
                hitResult = null;
            }

            if (hitResult != null && hitResult.getType() != HitResult.Type.MISS
                    && !EventHooks.onProjectileImpact(this, hitResult)) {
                hitTargetOrDeflectSelf(hitResult);
                hasImpulse = true;
            }
        }

        life++;
        if (life >= 100)
            discard();
    }

    @Override
    protected void checkInsideBlocks() {
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
    }
}
