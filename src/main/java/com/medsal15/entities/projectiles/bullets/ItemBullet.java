package com.medsal15.entities.projectiles.bullets;

import com.medsal15.entities.ESEntities;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ItemSupplier;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ItemBullet extends ESBullet implements ItemSupplier {
    public ItemBullet(EntityType<? extends ItemBullet> type, Level level) {
        super(type, level);
    }

    public ItemBullet(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.ITEM_BULLET.get(), level, pickup, shooter, weapon);
    }

    @Override
    public ItemStack getItem() {
        return getPickupItem();
    }
}
