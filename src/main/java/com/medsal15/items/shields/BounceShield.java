package com.medsal15.items.shields;

import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.entity.projectile.ProjectileDeflection;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

public class BounceShield extends ESShield implements IShieldBlock {
    private ProjectileDeflection deflection;

    public BounceShield(Properties properties, ProjectileDeflection deflection) {
        super(properties);

        this.deflection = deflection;
    }

    @Override
    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        // Ensure the damage does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
            return false;

        var attacker = damageSource.getDirectEntity();
        if (attacker == null || !(attacker instanceof Projectile projectile))
            return false;

        // todo these do not work well
        // AIM_DEFLECT works as expected
        // MOMENTUM_DEFLECT does not work
        // REVERSE works too well
        // NONE works too well, but needs positive
        projectile.deflect(deflection, event.getEntity(), damageSource.getEntity(),
                event.getEntity() instanceof Player);
        return true;
    }
}
