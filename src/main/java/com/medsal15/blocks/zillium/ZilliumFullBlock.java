package com.medsal15.blocks.zillium;

import javax.annotation.Nonnull;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ChangeOverTimeBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ZilliumFullBlock extends Block implements WeatheringZillium {
    public static final MapCodec<ZilliumFullBlock> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(WeatheringZillium.ZilliumColors.CODEC.fieldOf("color_state")
                            .forGetter(ChangeOverTimeBlock::getAge), propertiesCodec())
                    .apply(builder, ZilliumFullBlock::new));

    private final WeatheringZillium.ZilliumColors color;

    @Override
    public MapCodec<ZilliumFullBlock> codec() {
        return CODEC;
    }

    public ZilliumFullBlock(WeatheringZillium.ZilliumColors color, Properties properties) {
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
