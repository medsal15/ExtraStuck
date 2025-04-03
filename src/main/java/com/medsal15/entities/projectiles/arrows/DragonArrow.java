package com.medsal15.entities.projectiles.arrows;

import javax.annotation.Nonnull;

import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AreaEffectCloud;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.HitResult;

public class DragonArrow extends AbstractArrow {
    public DragonArrow(EntityType<? extends DragonArrow> entityType, Level level) {
        super(entityType, level);
    }

    public DragonArrow(Level level, ItemStack pickup, LivingEntity shooter, ItemStack weapon) {
        super(ESEntities.DRAGON_ARROW.get(), shooter, level, pickup, weapon);
    }

    public DragonArrow(Level level, double x, double y, double z, ItemStack pickup, ItemStack weapon) {
        super(ESEntities.DRAGON_ARROW.get(), x, y, z, level, pickup, weapon);
    }

    @Override
    protected ItemStack getDefaultPickupItem() {
        return new ItemStack(ESItems.DRAGON_ARROW.get());
    }

    @Override
    protected void onHit(@Nonnull HitResult result) {
        super.onHit(result);

        // Me? Copy DragonFireball$onHit? Yup
        if (!level().isClientSide) {
            var area = new AreaEffectCloud(level(), getX(), getY(), getZ());
            if (getOwner() instanceof LivingEntity owner) {
                area.setOwner(owner);
            }

            area.setParticle(ParticleTypes.DRAGON_BREATH);
            area.setRadius(2.5F);
            area.setDuration(100);
            area.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1));

            this.level().addFreshEntity(area);
            this.discard();
        }
    }
}
