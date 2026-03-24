package com.medsal15.entitysubpredicates;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.entities.LandFishingHook;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.advancements.critereon.EntitySubPredicate;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public record LandFishingHookPredicate(Optional<Boolean> inOpenFluid) implements EntitySubPredicate {
    public static final LandFishingHookPredicate ANY = new LandFishingHookPredicate(Optional.empty());
    public static final MapCodec<LandFishingHookPredicate> CODEC = RecordCodecBuilder.mapCodec(p_337364_ -> p_337364_
            .group(Codec.BOOL.optionalFieldOf("in_open_fluids").forGetter(LandFishingHookPredicate::inOpenFluid))
            .apply(p_337364_, LandFishingHookPredicate::new));

    public static LandFishingHookPredicate inOpenFluids(boolean inOpenFluids) {
        return new LandFishingHookPredicate(Optional.of(inOpenFluids));
    }

    @Override
    public MapCodec<? extends EntitySubPredicate> codec() {
        return CODEC;
    }

    @Override
    public boolean matches(@Nonnull Entity entity, @Nonnull ServerLevel level, @Nullable Vec3 position) {
        if (inOpenFluid.isEmpty())
            return true;
        return entity instanceof LandFishingHook hook ? inOpenFluid.get() == hook.isOpenFluid() : false;
    }
}
