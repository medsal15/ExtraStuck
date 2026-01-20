package com.medsal15.compat.irons_spellbooks.subevents;

import com.medsal15.config.ConfigServer;
import com.mraof.minestuck.effects.MSEffects;

import io.redspace.ironsspellbooks.api.events.SpellPreCastEvent;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;

public final class ISSCommonEvents {
    /**
     * Prevents spell casting with Creative Shock
     */
    @SubscribeEvent
    public static void onSpellPreCast(final SpellPreCastEvent event) {
        Player player = event.getEntity();

        if (player.hasEffect(MSEffects.CREATIVE_SHOCK)) {
            @SuppressWarnings("null")
            int amplifier = player.getEffect(MSEffects.CREATIVE_SHOCK).getAmplifier();
            int min = (player.isCreative() ? ConfigServer.ISS_CREATIVE_SHOCK_CREATIVE.get()
                    : ConfigServer.ISS_CREATIVE_SHOCK_SURVIVAL.get()) - 1;
            if (amplifier >= min)
                event.setCanceled(true);
        }
    }
}
