package com.medsal15.items;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.Codec;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESDataComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
            .createDataComponents(Registries.DATA_COMPONENT_TYPE, ExtraStuck.MODID);
    public static final Supplier<DataComponentType<Integer>> ENERGY = DATA_COMPONENTS
            .registerComponentType(
                    "energy",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final Supplier<DataComponentType<Integer>> ENERGY_STORAGE = DATA_COMPONENTS
            .registerComponentType(
                    "energy_storage",
                    builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    // Shield specific components
    /** Flame shield burn duration in ticks */
    public static final Supplier<DataComponentType<Integer>> BURN_DURATION = DATA_COMPONENTS
            .registerComponentType(
                    "burn_duration", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    /** Flux shield RF cost per damage */
    public static final Supplier<DataComponentType<Integer>> FLUX_MULTIPLIER = DATA_COMPONENTS
            .registerComponentType(
                    "flux_multiplier", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    /** Thorn shield damage */
    public static final Supplier<DataComponentType<Float>> SHIELD_DAMAGE = DATA_COMPONENTS
            .registerComponentType(
                    "shield_damage",
                    builder -> builder.persistent(Codec.FLOAT).networkSynchronized(ByteBufCodecs.FLOAT));

}
