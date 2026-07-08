package com.medsal15.blocks.zillium;

import javax.annotation.Nonnull;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ZilliumSlabBlock extends SlabBlock implements WeatheringZillium {
    public static final MapCodec<ZilliumSlabBlock> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(ZilliumColors.CODEC.fieldOf("color_state").forGetter(ChangeOverTimeBlock::getAge),
                            propertiesCodec())
                    .apply(builder, ZilliumSlabBlock::new));
    private final ZilliumColors color;

    @Override
    public MapCodec<ZilliumSlabBlock> codec() {
        return CODEC;
    }

    public ZilliumSlabBlock(ZilliumColors color, Properties properties) {
        super(properties);
        this.color = color;
    }

    @Override
    public ZilliumColors getAge() {
        return color;
    }

    @Override
    protected void randomTick(@Nonnull BlockState state, @Nonnull ServerLevel level, @Nonnull BlockPos pos,
            @Nonnull RandomSource random) {
        this.changeOverTime(state, level, pos, random);
    }

    @Override
    protected boolean isRandomlyTicking(@Nonnull BlockState state) {
        return true;
    }
}
