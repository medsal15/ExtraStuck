package com.medsal15.entitysubpredicates;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.MapCodec;

import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESEntityPredicates {
    public static final DeferredRegister<MapCodec<? extends EntitySubPredicate>> ENTITY_PREDICATES = DeferredRegister
            .create(Registries.ENTITY_SUB_PREDICATE_TYPE, ExtraStuck.MODID);

    public static final DeferredHolder<MapCodec<? extends EntitySubPredicate>, MapCodec<LandFishingHookPredicate>> LAND_FISHING_HOOK = ENTITY_PREDICATES
            .register("land_fishing_hook", () -> LandFishingHookPredicate.CODEC);
}
