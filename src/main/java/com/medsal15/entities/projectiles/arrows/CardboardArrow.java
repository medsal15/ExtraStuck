package com.medsal15.entities.projectiles.arrows;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class CardboardArrow extends AbstractArrow {
    public CardboardArrow(EntityType<? extends CardboardArrow> entityType, Level level) {
        super(entityType, level);
        setBaseDamage(.5D);
    }

    public CardboardArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.CARDBOARD_ARROW.get(), shooter, level, pickup, weapon);
        setBaseDamage(.5D);
    }

    public CardboardArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.CARDBOARD_ARROW.get(), x, y, z, level, pickup, weapon);
        setBaseDamage(.5D);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.CARDBOARD_ARROW.get());
    }
}
