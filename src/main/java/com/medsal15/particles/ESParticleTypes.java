package com.medsal15.particles;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;

import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESParticleTypes {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister
            .create(BuiltInRegistries.PARTICLE_TYPE, ExtraStuck.MODID);

    public static final Supplier<SimpleParticleType> URANIUM_BLAST = PARTICLE_TYPES.register(
            "uranium_blast", () -> new SimpleParticleType(false));
}
