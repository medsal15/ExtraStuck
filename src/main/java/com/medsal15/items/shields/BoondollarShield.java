package com.medsal15.items.shields;

import com.mraof.minestuck.player.PlayerBoondollars;
import com.mraof.minestuck.player.PlayerData;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;

/**
 * Special class for the Captiashield
 *
 * Consumes boondollars instead of damage when possible
 */
public class BoondollarShield extends ESShield implements IShieldBlock {
    public BoondollarShield(Properties properties) {
        super(properties);
    }

    @Override
    public boolean onShieldBlock(LivingShieldBlockEvent event) {
        var user = event.getEntity();
        // Only players get boondollars
        if (!(user instanceof ServerPlayer player))
            return false;

        // Ensure the damage does not bypass shields
        var damageSource = event.getDamageSource();
        if (damageSource.is(DamageTypeTags.BYPASSES_SHIELD))
            return false;

        var oPlayerData = PlayerData.get(player);
        if (!oPlayerData.isPresent())
            return false;
        var playerData = oPlayerData.get();
        if (!PlayerBoondollars.tryTakeBoondollars(playerData, (long) event.getBlockedDamage()))
            return false;

        event.setShieldDamage(0);
        return true;
    }
}
