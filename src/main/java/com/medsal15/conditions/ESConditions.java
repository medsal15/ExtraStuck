package com.medsal15.conditions;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.Holder;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public final class ESConditions {
    public static final DeferredRegister<MapCodec<? extends ICondition>> CONDITIONS = DeferredRegister
            .create(NeoForgeRegistries.Keys.CONDITION_CODECS, ExtraStuck.MODID);

    public static final Holder<MapCodec<? extends ICondition>> CONFIG_CONDITION = CONDITIONS.register("config",
            () -> ConfigCondition.CODEC);
}
