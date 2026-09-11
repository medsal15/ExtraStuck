package com.medsal15.world.features;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESFeatures {
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(BuiltInRegistries.FEATURE,
            ExtraStuck.MODID);

    public static final Supplier<Feature<SculkPatchConfiguration>> SAFE_SCULK_PATCH = FEATURES
            .register("safe_sculk_patch", () -> new SafeSculkPatchFeature(SculkPatchConfiguration.CODEC));
}
