package com.medsal15.items.shields;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Shield that applies an effect to melee attackers
 */
public class FlameShield extends ESShield implements IShieldBlock {
    /**
     * Duration in ticks
     */
    public int duration;

    public FlameShield(Properties properties, int duration) {
        super(properties);
        this.duration = duration;
    }

    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        if (this.duration <= 0)
            return false;

        // Ensure the damage is melee and does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
            return false;

        // Ensure the attacker exists and can be damaged
        var attacker = damageSource.getDirectEntity();
        if (attacker == null || !(attacker instanceof LivingEntity target))
            return false;

        target.setRemainingFireTicks(this.duration);
        return true;
    }
}
