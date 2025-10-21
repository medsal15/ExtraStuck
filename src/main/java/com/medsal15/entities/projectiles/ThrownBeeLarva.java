package com.medsal15.entities.projectiles;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class ThrownBeeLarva extends ThrowableItemProjectile {
    private static final EntityDimensions ZERO_SIZED_DIMENSIONS = EntityDimensions.fixed(0.0F, 0.0F);

    public ThrownBeeLarva(EntityType<? extends ThrownBeeLarva> type, Level level) {
        super(type, level);
    }

    public ThrownBeeLarva(Level level, LivingEntity shooter) {
        super(ESEntities.THROWN_BEE_LARVA.get(), shooter, level);
    }

    public ThrownBeeLarva(Level level, double x, double y, double z) {
        super(ESEntities.THROWN_BEE_LARVA.get(), x, y, z, level);
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
    protected void onHitEntity(@Nonnull EntityHitResult result) {
        super.onHitEntity(result);
        result.getEntity().hurt(this.damageSources().thrown(this, this.getOwner()), 0.0F);
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);

        if (!level().isClientSide) {
            if (random.nextInt(16) == 0) {
                Bee bee = EntityType.BEE.create(level());

                if (bee != null) {
                    bee.setAge(-24000);
                    bee.moveTo(getX(), getY(), getZ(), getYRot(), 0F);
                    if (bee.fudgePositionAfterSizeChange(ZERO_SIZED_DIMENSIONS)) {
                        level().addFreshEntity(bee);
                    }
                }
            }

            level().broadcastEntityEvent(this, (byte) 3);
            discard();
        }
    }

    @Override
    protected Item getDefaultItem() {
        return ESItems.BEE_LARVA.get();
    }
}
