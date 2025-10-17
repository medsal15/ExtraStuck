package com.medsal15.mobeffects;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(BuiltInRegistries.MOB_EFFECT,
            ExtraStuck.MODID);

    public static final DeferredHolder<MobEffect, TimeStopEffect> TIME_STOP = MOB_EFFECTS.register("time_stop",
            () -> new TimeStopEffect(MobEffectCategory.HARMFUL, 0xff0000));
    public static final DeferredHolder<MobEffect, BeeAngryEffect> BEE_ANGRY = MOB_EFFECTS.register("bee_angry",
            () -> new BeeAngryEffect(MobEffectCategory.HARMFUL, 0xff7700));
}
