package com.medsal15.loot.conditions;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESLootConditions {
    public static final DeferredRegister<LootItemConditionType> LOOT_CONDITIONS = DeferredRegister
            .create(Registries.LOOT_CONDITION_TYPE, ExtraStuck.MODID);

    public static final Supplier<LootItemConditionType> LAND_TERRAIN = LOOT_CONDITIONS.register("land_terrain",
            () -> new LootItemConditionType(ESTerrainCondition.CODEC));
    public static final Supplier<LootItemConditionType> LAND_TITLE = LOOT_CONDITIONS.register("land_title",
            () -> new LootItemConditionType(ESTitlecondition.CODEC));
}
