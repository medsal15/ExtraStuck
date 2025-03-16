package com.medsal15.items.shields;

import com.medsal15.ExtraStuck;
import com.mraof.minestuck.entity.underling.UnderlingEntity;

import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public class CandyShield extends ThornShield {
    public CandyShield(Properties properties, float damage) {
        super(properties, damage);
    }

    @Override
    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        if (!ExtraStuck.isMinestuckLoaded)
            return false;

        // Ensure the damage is melee and does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
            return false;

        // Ensure the attacker exists and is an underling
        var attacker = damageSource.getEntity();
        if (attacker == null)
            return false;

        if (attacker instanceof UnderlingEntity underling)
            underling.dropCandy = true;

        return super.onShieldBlock(event);
    }
}
