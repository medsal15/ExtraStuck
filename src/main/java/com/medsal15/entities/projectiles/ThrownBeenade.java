package com.medsal15.entities.projectiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.throwables.BeenadeItem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.NeutralMob;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class ThrownBeenade extends ThrowableItemProjectile {
    private static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(ThrownBeenade.class,
            EntityDataSerializers.ITEM_STACK);

    public ThrownBeenade(EntityType<? extends ThrownBeenade> type, Level level) {
        super(type, level);
    }

    public ThrownBeenade(Level level, LivingEntity shooter) {
        super(ESEntities.THROWN_BEENADE.get(), shooter, level);
    }

    public ThrownBeenade(Level level, double x, double y, double z) {
        super(ESEntities.THROWN_BEENADE.get(), x, y, z, level);
    }

    @Override
    public void setItem(@Nonnull ItemStack stack) {
        super.setItem(stack);
        entityData.set(STACK, stack);
    }

    @Override
    public ItemStack getItem() {
        return entityData.get(STACK);
    }

    @Override
    protected void defineSynchedData(@Nonnull Builder builder) {
        super.defineSynchedData(builder);
        builder.define(STACK, ItemStack.EMPTY);
    }

    @Override
    protected Item getDefaultItem() {
        return ESItems.BEENADE.get();
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 3) {
            double d0 = 0.08;

            for (int i = 0; i < 8; i++) {
                this.level()
                        .addParticle(
                                new ItemParticleOption(ParticleTypes.ITEM, this.getItem()),
                                this.getX(),
                                this.getY(),
                                this.getZ(),
                                ((double) this.random.nextFloat() - 0.5) * d0,
                                ((double) this.random.nextFloat() - 0.5) * d0,
                                ((double) this.random.nextFloat() - 0.5) * d0);
            }
        }
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);

        if (!level().isClientSide) {
            ItemStack stack = getItem();
            if (BeenadeItem.hasEntity(stack)) {
                LivingEntity entity = getEntity(stack, level());
                if (entity != null) {
                    entity.setPos(result.getLocation());
                    level().addFreshEntity(entity);

                    if (entity instanceof NeutralMob mob) {
                        Entity target;
                        if (result.getType() == HitResult.Type.ENTITY) {
                            EntityHitResult entityHitResult = (EntityHitResult) result;
                            target = entityHitResult.getEntity();
                        } else {
                            Vec3 location = result.getLocation();
                            AABB box = new AABB(this.blockPosition()).inflate(5);
                            target = level().getNearestEntity(LivingEntity.class, TargetingConditions.forCombat(),
                                    entity, location.x, location.y, location.z, box);
                        }

                        if (target != null) {
                            entity.fudgePositionAfterSizeChange(target.getDimensions(target.getPose()));
                            mob.startPersistentAngerTimer();
                            mob.setPersistentAngerTarget(target.getUUID());
                        }
                    }
                }
            }

            level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
    }

    @Nullable
    public static LivingEntity getEntity(ItemStack stack, Level level) {
        if (!stack.has(ESDataComponents.ENTITY_TYPE))
            return null;

        EntityType<?> entityType = EntityType.byString(stack.get(ESDataComponents.ENTITY_TYPE)).orElse(null);
        if (entityType == null)
            return null;

        Entity entity = entityType.create(level);
        if (!(entity instanceof LivingEntity))
            return null;

        CompoundTag entityData = new CompoundTag();
        if (stack.has(DataComponents.ENTITY_DATA)) {
            CustomData customData = stack.get(DataComponents.ENTITY_DATA);
            if (customData != null) {
                entityData = customData.copyTag();
            }
        }
        entity.load(entityData);

        return (LivingEntity) entity;
    }
}
