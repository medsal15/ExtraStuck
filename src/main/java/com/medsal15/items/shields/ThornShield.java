package com.medsal15.items.shields;

import com.medsal15.ESDamageTypes;
import com.medsal15.items.ESDataComponents;

import net.minecraft.core.registries.Registries;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Shield that deals damage to melee attackers
 */
public class ThornShield extends ESShield implements IShieldBlock {
    /**
     * Damage dealt
     */
    @Deprecated
    public float damage;

    @Deprecated
    public ThornShield(Properties properties, float damage) {
        super(properties);
        this.damage = damage;
    }

    public ThornShield(Properties properties) {
        super(properties);
    }

    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        var useItem = event.getEntity().getUseItem();
        if (!useItem.has(ESDataComponents.SHIELD_DAMAGE))
            return false;

        var damage = useItem.get(ESDataComponents.SHIELD_DAMAGE);
        if (damage <= 0)
            return false;

        // Ensure the damage is melee and does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD) || !damageSource.isDirect())
            return false;

        // Ensure the attacker exists and can be damaged
        var attacker = damageSource.getDirectEntity();
        if (attacker == null || !(attacker instanceof LivingEntity livingEntity))
            return false;

        // Hurt them
        // This will crash at some point due to a null or whatever, no clue when or why
        var level = event.getEntity().level();
        var type = level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE)
                .getHolderOrThrow(ESDamageTypes.THORN_SHIELD);
        var retSource = new DamageSource(type, event.getEntity());
        livingEntity.hurt(retSource, damage);
        return true;
    }
}
