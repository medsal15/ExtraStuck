package com.medsal15.datamaps;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.datamaps.DataMapType;

public record ReactorFuel(int duration, ItemStack result) {
    public static final Codec<ReactorFuel> CODEC = RecordCodecBuilder
            .create(inst -> inst.group(Codec.INT.fieldOf("duration").forGetter(ReactorFuel::duration),
                    ItemStack.CODEC.optionalFieldOf("result", ItemStack.EMPTY).forGetter(ReactorFuel::result))
                    .apply(inst, ReactorFuel::new));

    public static final DataMapType<Item, ReactorFuel> REACTOR_MAP = DataMapType
            .builder(ExtraStuck.modid("reactor_fuel"), Registries.ITEM, CODEC).synced(CODEC, false).build();
}
