package com.medsal15.items.shields;

import net.minecraft.core.Holder;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Shield that applies an effect to melee attackers
 */
public class EffectShield extends ESShield implements IShieldBlock {
    /**
     * Effect to apply
     */
    public Holder<MobEffect> effect;
    /**
     * Duration in ticks
     */
    public int duration;
    /**
     * If true, applies to user instead of attacker
     */
    public boolean self;

    public EffectShield(Properties properties, Holder<MobEffect> effect, int duration, boolean self) {
        super(properties);
        this.effect = effect;
        this.duration = duration;
        this.self = self;
    }

    public EffectShield(Properties properties, Holder<MobEffect> effect, int duration) {
        super(properties);
        this.effect = effect;
        this.duration = duration;
        this.self = false;
    }

    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        if (this.duration <= 0)
            return false;

        // Ensure the damage does not bypass shields
        // and can be applied to self/melee attacker
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || (!damageSource.isDirect() && !self))
            return false;

        LivingEntity target;
        if (self) {
            target = event.getEntity();
        } else {
            // Ensure the attacker exists and can be damaged
            var attacker = damageSource.getDirectEntity();
            if (attacker == null || !(attacker instanceof LivingEntity))
                return false;
            target = (LivingEntity) attacker;
        }

        target.addEffect(new MobEffectInstance(this.effect, this.duration));
        return true;
    }
}
