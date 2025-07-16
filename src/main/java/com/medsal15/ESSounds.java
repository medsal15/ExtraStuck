package com.medsal15;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister
            .create(BuiltInRegistries.SOUND_EVENT, ExtraStuck.MODID);

    public static final DeferredHolder<SoundEvent, SoundEvent> GOLDEN_PAN_HIT = SOUND_EVENTS.register("golden_pan_hit",
            () -> SoundEvent.createVariableRangeEvent(ExtraStuck.modid("golden_pan_hit")));
}
