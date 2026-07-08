package com.medsal15.blocks.zillium;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.medsal15.datamaps.Zilliable;
import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.state.BlockState;

public interface WeatheringZillium extends ChangeOverTimeBlock<WeatheringZillium.ZilliumColors> {
    @Override
    default float getChanceModifier() {
        return getAge().chanceModifier;
    }

    @Override
    default void changeOverTime(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos,
            @Nonnull RandomSource random) {
        float chance = 0.05688889F;
        if (getAge() == ZilliumColors.SECONDARY)
            chance *= 10f;

        if (random.nextFloat() < chance) {
            getNextState(state, level, pos, random)
                    .ifPresent(blockState -> level.setBlockAndUpdate(pos, blockState));
        }
    }

    @Override
    default Optional<BlockState> getNextState(@Nonnull BlockState state, @Nonnull ServerLevel level,
            @Nonnull BlockPos pos, @Nonnull RandomSource random) {
        int age = this.getAge().ordinal();
        int nextage = (age + 1) % ZilliumColors.values().length;
        int prevage = (age - 1 + ZilliumColors.values().length) % ZilliumColors.values().length;

        int next = 0, prev = 0;

        for (BlockPos blockPos : BlockPos.withinManhattan(pos, 4, 4, 4)) {
            int dist = blockPos.distManhattan(pos);
            if (dist > 4)
                break;

            if (!blockPos.equals(pos)
                    && level.getBlockState(blockPos).getBlock() instanceof ChangeOverTimeBlock<?> changeBlock) {
                Enum<?> other = changeBlock.getAge();
                if (this.getAge().getClass() == other.getClass()) {
                    int oage = other.ordinal();

                    if (oage == nextage)
                        next++;
                    else if (oage == prevage)
                        prev++;
                }
            }
        }

        float basechance = (float) (next + 1) / (float) (next + prev + 1);
        float chance = basechance * basechance * getChanceModifier();
        return random.nextFloat() < chance ? getNext(state) : Optional.empty();
    }

    @Override
    default Optional<BlockState> getNext(@Nonnull BlockState state) {
        Zilliable zilliable = state.getBlockHolder().getData(Zilliable.ZILLIABLES);
        if (zilliable == null)
            return Optional.empty();
        return Optional.of(zilliable.nextStage().withPropertiesOf(state));
    }

    public static enum ZilliumColors implements StringRepresentable {
        GREEN("green", 1),
        BLUE("blue", 1),
        PINK("pink", 1),
        SECONDARY("secondary", 10);

        public static final Codec<ZilliumColors> CODEC = StringRepresentable.fromEnum(ZilliumColors::values);
        private final String name;
        private final float chanceModifier;

        private ZilliumColors(String name, float chanceModifier) {
            this.name = name;
            this.chanceModifier = chanceModifier;
        }

        @Override
        public String getSerializedName() {
            return name;
        }
    }
}
