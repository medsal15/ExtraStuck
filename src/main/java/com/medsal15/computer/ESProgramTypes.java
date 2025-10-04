package com.medsal15.computer;

import com.medsal15.ExtraStuck;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.computer.ProgramType;

import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESProgramTypes {
    public static final DeferredRegister<ProgramType<?>> PROGRAM_TYPES = DeferredRegister
            .create(Minestuck.id("program_type"), ExtraStuck.MODID);

    public static final ProgramType.EventHandler EMPTY = new ProgramType.EventHandler() {
    };

    public static final DeferredHolder<ProgramType<?>, ProgramType<ProgramType.EmptyData>> MASTERMIND_CODEBREAKER = PROGRAM_TYPES
            .register(
                    "mastermind_codebreaker",
                    () -> new ProgramType<>(EMPTY, ignored -> ProgramType.EmptyData.INSTANCE));
}
