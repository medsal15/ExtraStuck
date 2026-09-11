package com.medsal15.blocks.zillium;

import javax.annotation.Nonnull;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockState;

public class ZilliumWallBlock extends WallBlock implements WeatheringZillium {
    public static final MapCodec<WallBlock> CODEC = RecordCodecBuilder.mapCodec(
            builder -> builder
                    .group(ZilliumColors.CODEC.fieldOf("color_state")
                            .forGetter(wall -> wall instanceof ZilliumWallBlock zwall ? zwall.getAge()
                                    : ZilliumColors.BLUE),
                            propertiesCodec())
                    .apply(builder, ZilliumWallBlock::new));
    private final ZilliumColors color;

    @Override
    public MapCodec<WallBlock> codec() {
        return CODEC;
    }

    public ZilliumWallBlock(ZilliumColors color, Properties properties) {
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
