package com.medsal15.loot.conditions;

import com.medsal15.loot.modifiers.ESLandLootModifier;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ESTerrainCondition(TerrainLandType terrain) implements LootItemCondition {
    public static MapCodec<ESTerrainCondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            TerrainLandType.CODEC.fieldOf("terrain").forGetter(ESTerrainCondition::terrain))
            .apply(inst, ESTerrainCondition::new));

    @Override
    public boolean test(LootContext context) {
        return terrain == ESLandLootModifier.getTerrain(context);
    }

    @Override
    public LootItemConditionType getType() {
        return ESLootConditions.LAND_TERRAIN.get();
    }
}
