package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.entity.underling.UnderlingEntity;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;

public class CandyArrow extends AbstractArrow {
    public CandyArrow(EntityType<? extends CandyArrow> entityType, Level level) {
        super(entityType, level);
    }

    public CandyArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.CANDY_ARROW.get(), shooter, level, pickup, weapon);
    }

    public CandyArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.CANDY_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return ESItems.SWEET_TOOTH.toStack();
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        Entity entity = result.getEntity();
        if (!(entity instanceof UnderlingEntity underling))
            return;
        underling.dropCandy = true;

        super.onHitEntity(result);
    }
}
