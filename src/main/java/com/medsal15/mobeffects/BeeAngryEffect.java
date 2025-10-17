package com.medsal15.mobeffects;

import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.world.effect.InstantenousMobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.phys.AABB;

public class BeeAngryEffect extends InstantenousMobEffect {
    public BeeAngryEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        if (!livingEntity.level().isClientSide) {
            AABB box = new AABB(livingEntity.blockPosition()).inflate((amplifier + 1) * 3);
            List<Entity> entities = livingEntity.level().getEntities(livingEntity, box, e -> e instanceof Bee);

            for (Entity entity : entities) {
                Bee bee = (Bee) entity;
                bee.setPersistentAngerTarget(livingEntity.getUUID());
                bee.setLastHurtByMob(livingEntity);
                bee.startPersistentAngerTimer();
            }
        }
        return true;
    }
}
