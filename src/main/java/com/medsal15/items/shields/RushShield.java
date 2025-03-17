package com.medsal15.items.shields;

import com.medsal15.ExtraStuck;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public class RushShield extends ThornShield {
    public RushShield(Properties properties, float damage) {
        super(properties, damage);
    }

    @Override
    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        if (!ExtraStuck.isMinestuckLoaded)
            return false;

        // Ensure the damage does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
            return false;

        var entity = event.getEntity();
        if (!entity.hasEffect(MobEffects.DAMAGE_BOOST)) {
            entity.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 100));
        }

        return super.onShieldBlock(event);
    }
}
