package com.medsal15.loot_modifiers;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.MapCodec;

import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class ESLootModifiers {
    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLM_SERIALIZERS = DeferredRegister
            .create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, ExtraStuck.MODID);

    public static final Supplier<MapCodec<ESTitleLootModifier>> TITLE_LOOT_MODIFIER = GLM_SERIALIZERS
            .register("title_loot_modifier", () -> ESTitleLootModifier.CODEC);
    public static final Supplier<MapCodec<ESTerrainLootModifier>> TERRAIN_LOOT_MODIFIER = GLM_SERIALIZERS
            .register("terrain_loot_modifier", () -> ESTerrainLootModifier.CODEC);
    public static final Supplier<MapCodec<ESLandLootModifier>> LAND_LOOT_MODIFIER = GLM_SERIALIZERS
            .register("land_loot_modifier", () -> ESLandLootModifier.CODEC);
}
