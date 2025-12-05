package com.medsal15.conditions;

import javax.annotation.Nonnull;

import com.medsal15.config.ConfigCommon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.neoforged.neoforge.common.conditions.ICondition;

public record ConfigCondition(String option) implements ICondition {
    public static final MapCodec<ConfigCondition> CODEC = RecordCodecBuilder
            .mapCodec(inst -> inst.group(Codec.STRING.fieldOf("option").forGetter(ConfigCondition::option)).apply(inst,
                    ConfigCondition::new));

    @Override
    public MapCodec<? extends ICondition> codec() {
        return CODEC;
    }

    @Override
    public boolean test(@Nonnull IContext context) {
        return ConfigCommon.configEnabled(option);
    }
}
