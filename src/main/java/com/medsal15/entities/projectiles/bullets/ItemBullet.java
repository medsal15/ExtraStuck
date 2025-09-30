package com.medsal15.entities.projectiles.bullets;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.network.syncher.SynchedEntityData.Builder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBullet extends ESBullet implements ItemSupplier {
    private static final EntityDataAccessor<ItemStack> STACK = SynchedEntityData.defineId(ItemBullet.class,
            EntityDataSerializers.ITEM_STACK);

    public ItemBullet(EntityType<? extends ItemBullet> type, Level level) {
        super(type, level);
    }

    public ItemBullet(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.ITEM_BULLET.get(), level, pickup, shooter, weapon);
        entityData.set(STACK, pickup);
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
}
