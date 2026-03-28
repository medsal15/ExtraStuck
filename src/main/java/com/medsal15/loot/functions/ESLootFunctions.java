package com.medsal15.loot.functions;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESLootFunctions {
    public static final DeferredRegister<LootItemFunctionType<?>> FUNCTIONS = DeferredRegister
            .create(Registries.LOOT_FUNCTION_TYPE, ExtraStuck.MODID);

    public static final Supplier<LootItemFunctionType<TurnToCardFunction>> TURN_TO_CARD = FUNCTIONS
            .register("turn_to_card", () -> new LootItemFunctionType<>(TurnToCardFunction.CODEC));
}
