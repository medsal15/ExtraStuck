package com.medsal15.items.shields;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public class HaltShield extends ESShield implements IShieldBlock {
    public HaltShield(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        // Ensure the damage is melee and does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
            return false;

        // Ensure the attacker exists and can be damaged
        var attacker = damageSource.getDirectEntity();
        if (attacker == null || !(attacker instanceof LivingEntity target))
            return false;

        var entity = event.getEntity();
        var rot = entity.getYRot();
        target.knockback(1, Math.sin(rot / 180 * Math.PI), -Math.cos(rot / 180 * Math.PI));
        target.setYRot((target.getYRot() + 180) % 180);

        return true;
    }
}
