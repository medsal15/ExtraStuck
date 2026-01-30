package com.medsal15.mobeffects;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.ESDamageTypes;
import com.medsal15.config.ConfigServer;
import com.medsal15.utils.ESTags;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.AABB;

public class CosmicPlagueEffect extends MobEffect {
    public CosmicPlagueEffect(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int duration, int amplifier) {
        int i = 40 >> amplifier;
        return i > 0 ? duration % i == 0 : true;
    }

    @Override
    public boolean applyEffectTick(@Nonnull LivingEntity livingEntity, int amplifier) {
        if (livingEntity.getType().is(ESTags.EntityTypes.COSMIC_PLAGUE_IMMUNE))
            return false;

        livingEntity.hurt(livingEntity.damageSources().source(ESDamageTypes.COSMIC_PLAGUE), 2);

        MobEffectInstance effect = livingEntity.getEffect(ESMobEffects.COSMIC_PLAGUE);
        if (amplifier > 0 && effect != null && ConfigServer.COSMIC_PLAGUE_SPREAD.getAsBoolean()) {
            AABB box = new AABB(livingEntity.blockPosition()).inflate(ConfigServer.COSMIC_PLAGUE_RANGE.get());
            List<Entity> entities = livingEntity.level().getEntities(livingEntity, box,
                    e -> e instanceof LivingEntity living && !living.hasEffect(ESMobEffects.COSMIC_PLAGUE)
                            && !e.getType().is(ESTags.EntityTypes.COSMIC_PLAGUE_IMMUNE));
            for (Entity entity : entities) {
                if (!(entity instanceof LivingEntity living))
                    continue;
                living.addEffect(new MobEffectInstance(ESMobEffects.COSMIC_PLAGUE, effect.getDuration(),
                        effect.getAmplifier() - 1, effect.isAmbient(), effect.isVisible(), effect.showIcon()));
            }
        }

        return true;
    }
}
