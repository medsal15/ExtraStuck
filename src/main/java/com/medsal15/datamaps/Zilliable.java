package com.medsal15.datamaps;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.registries.datamaps.DataMapType;
import net.neoforged.neoforge.registries.datamaps.RegisterDataMapTypesEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID)
public record Zilliable(Block nextStage) {
    public static final Codec<Zilliable> ZILLIABLE_CODEC = BuiltInRegistries.BLOCK.byNameCodec()
            .xmap(Zilliable::new, Zilliable::nextStage);
    public static final Codec<Zilliable> CODEC = Codec.withAlternative(
            RecordCodecBuilder.create(in -> in.group(
                    BuiltInRegistries.BLOCK.byNameCodec().fieldOf("next_color_stage")
                            .forGetter(Zilliable::nextStage))
                    .apply(in, Zilliable::new)),
            ZILLIABLE_CODEC);

    public static DataMapType<Block, Zilliable> ZILLIABLES = DataMapType
            .builder(ExtraStuck.modid("zilliables"), Registries.BLOCK, CODEC).synced(ZILLIABLE_CODEC, false).build();

    @SubscribeEvent
    public static void registerDataMapTypes(final RegisterDataMapTypesEvent event) {
        event.register(ZILLIABLES);
    }
}
