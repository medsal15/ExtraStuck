package com.medsal15.structures.processors;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESProcessors {
    public static final DeferredRegister<StructureProcessorType<?>> PROCESSORS = DeferredRegister
            .create(Registries.STRUCTURE_PROCESSOR, ExtraStuck.MODID);

    public static final Supplier<StructureProcessorType<PlaceConsortProcessor>> PLACE_CONSORT = PROCESSORS
            .register("place_consort", () -> () -> PlaceConsortProcessor.CODEC);
}
