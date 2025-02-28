package com.medsal15.items.shields;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;

public class GlassShield extends ESShield {
    public GlassShield(Properties properties) {
        super(properties);
    }

    @Override
    public SoundEvent getBreakingSound() {
        return SoundEvents.GLASS_BREAK;
    }
}
