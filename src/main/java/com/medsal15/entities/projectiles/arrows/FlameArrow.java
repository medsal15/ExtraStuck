package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.FireBlock;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public class FlameArrow extends AbstractArrow {
    public FlameArrow(EntityType<? extends FlameArrow> entityType, Level level) {
        super(entityType, level);
        setBaseDamage(1);
    }

    public FlameArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.FLAME_ARROW.get(), shooter, level, pickup, weapon);
        setBaseDamage(1);
    }

    public FlameArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.FLAME_ARROW.get(), x, y, z, level, pickup, weapon);
        setBaseDamage(1);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.FLAME_ARROW.get());
    }

    @Override
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);
        Entity entity = result.getEntity();
        if (!(entity instanceof LivingEntity living))
            return;
        int duration = 200;
        if (this.isCritArrow())
            duration *= 2;
        living.setRemainingFireTicks(duration);
    }

    @Override
    protected void onHitBlock(@Nonnull BlockHitResult result) {
        super.onHitBlock(result);

        if (result.getType() == HitResult.Type.MISS)
            return;

        if (level() instanceof ServerLevel) {
            BlockPos target;

            switch (result.getDirection()) {
                case Direction.UP:
                    target = result.getBlockPos().above();
                    break;
                case Direction.DOWN:
                    target = result.getBlockPos().below();
                    break;
                case Direction.EAST:
                    target = result.getBlockPos().east();
                    break;
                case Direction.WEST:
                    target = result.getBlockPos().west();
                    break;
                case Direction.NORTH:
                    target = result.getBlockPos().north();
                    break;
                case Direction.SOUTH:
                    target = result.getBlockPos().south();
                    break;
                default:
                    return;
            }
            if (FireBlock.canBePlacedAt(level(), target, getDirection())) {
                level().setBlock(target, BaseFireBlock.getState(level(), target), 11);
            }
        }
    }
}
