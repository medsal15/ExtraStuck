package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Endermite;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class TeleportArrow extends AbstractArrow {
    private boolean hit;

    public TeleportArrow(EntityType<? extends TeleportArrow> entityType, Level level) {
        super(entityType, level);
    }

    public TeleportArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.TELEPORT_ARROW.get(), shooter, level, pickup, weapon);
    }

    public TeleportArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.TELEPORT_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ESItems.TELERROW.toStack();
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        super.onHitBlock(result);
        var owner = getOwner();
        var level = level();
        if (!hit && owner != null && canTeleport(owner, level) && level instanceof ServerLevel serverLevel) {
            owner.teleportTo(getX(), getY(), getZ());

            if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && random.nextFloat() < .05F) {
                Endermite endermite = EntityType.ENDERMITE.create(serverLevel);
                if (endermite != null) {
                    endermite.moveTo(getX(), getY(), getZ());
                    serverLevel.addFreshEntity(endermite);
                }
            }

            var type = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.FALL);
            owner.hurt(new DamageSource(type), 5F);

            hit = true;
        }
        this.discard();
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);
        var owner = getOwner();
        var target = result.getEntity();
        var level = level();
        if (!hit && owner != null && canTeleport(owner, level) && canTeleport(target, level)
                && level instanceof ServerLevel serverLevel) {
            var x = target.getX();
            var y = target.getY();
            var z = target.getZ();

            target.teleportTo(owner.getX(), owner.getY(), owner.getZ());
            owner.teleportTo(x, y, z);

            if (serverLevel.getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) && random.nextFloat() < .05F) {
                Endermite endermite = EntityType.ENDERMITE.create(serverLevel);
                if (endermite != null) {
                    endermite.moveTo(x, y, z);
                    serverLevel.addFreshEntity(endermite);
                }
            }

            var type = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                    .getHolderOrThrow(DamageTypes.FALL);
            owner.hurt(new DamageSource(type), 5F);
            target.hurt(new DamageSource(type), 5F);

            hit = true;
        }
    }

    // copied from ThrownEnderpearl
    private boolean canTeleport(Entity entity, Level level) {
        if (entity.level().dimension() == level.dimension()) {
            return !(entity instanceof LivingEntity livingentity) ? entity.isAlive()
                    : livingentity.isAlive() && !livingentity.isSleeping();
        } else {
            return entity.canUsePortal(true);
        }
    }

    @Override
    public void addAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("extrastuck:hit", hit);
    }

    @Override
    public void readAdditionalSaveData(@Nonnull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        hit = compound.getBoolean("extrastuck:hit");
    }
}
