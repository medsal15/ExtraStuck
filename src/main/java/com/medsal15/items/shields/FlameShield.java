package com.medsal15.items.shields;

import com.medsal15.items.ESDataComponents;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Shield that applies an effect to melee attackers
 */
public class FlameShield extends ESShield implements IShieldBlock {
    public FlameShield(Properties properties) {
        super(properties);
    }

    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        var useItem = event.getEntity().getUseItem();
        if (!useItem.has(ESDataComponents.BURN_DURATION.get()))
            return false;
        var duration = useItem.get(ESDataComponents.BURN_DURATION);
        if (duration <= 0)
            return false;

        // Ensure the damage is melee and does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
            return false;

        // Ensure the attacker exists and can be damaged
        var attacker = damageSource.getDirectEntity();
        if (attacker == null || !(attacker instanceof LivingEntity target))
            return false;

        target.setRemainingFireTicks(duration);
        return true;
    }
}
