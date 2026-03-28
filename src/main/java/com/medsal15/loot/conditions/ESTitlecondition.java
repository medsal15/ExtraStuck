package com.medsal15.loot.conditions;

import com.medsal15.loot.modifiers.ESLandLootModifier;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.lands.title.TitleLandType;

import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

public record ESTitlecondition(TitleLandType title) implements LootItemCondition {
    public static MapCodec<ESTitlecondition> CODEC = RecordCodecBuilder.mapCodec(inst -> inst.group(
            TitleLandType.CODEC.fieldOf("title").forGetter(ESTitlecondition::title))
            .apply(inst, ESTitlecondition::new));

    @Override
    public boolean test(LootContext context) {
        return title == ESLandLootModifier.getTitle(context);
    }

    @Override
    public LootItemConditionType getType() {
        return ESLootConditions.LAND_TITLE.get();
    }
}
