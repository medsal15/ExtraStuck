package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;

public class MiningArrow extends AbstractArrow {
    private boolean mined;

    public MiningArrow(EntityType<? extends MiningArrow> entityType, Level level) {
        super(entityType, level);
        setBaseDamage(3D);
    }

    public MiningArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.MINING_ARROW.get(), shooter, level, pickup, weapon);
        setBaseDamage(3D);
    }

    public MiningArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.MINING_ARROW.get(), x, y, z, level, pickup, weapon);
        setBaseDamage(3D);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.PROJECDRILL.get());
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        super.onHitBlock(result);

        if (result.getType() == HitResult.Type.MISS || mined)
            return;

        if (level() instanceof ServerLevel) {
            mined = true;
            var pos = result.getBlockPos();
            if (!level().getBlockState(pos).is(BlockTags.INCORRECT_FOR_IRON_TOOL)) {
                level().destroyBlock(pos, true, getOwner());
            }
        }
    }
}
