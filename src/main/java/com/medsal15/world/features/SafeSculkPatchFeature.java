package com.medsal15.world.features;

import java.util.Objects;
import java.util.stream.Stream;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SculkBehaviour;
import net.minecraft.world.level.block.SculkSpreader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.SculkPatchConfiguration;

public class SafeSculkPatchFeature extends Feature<SculkPatchConfiguration> {
    public SafeSculkPatchFeature(Codec<SculkPatchConfiguration> codec) {
        super(codec);
    }

    public boolean place(@Nonnull FeaturePlaceContext<SculkPatchConfiguration> context) {
        WorldGenLevel level = context.level();
        BlockPos pos = context.origin();
        if (!canSpreadFrom(level, pos)) {
            return false;
        }

        SculkPatchConfiguration config = context.config();
        RandomSource random = context.random();
        SculkSpreader spread = SculkSpreader.createWorldGenSpreader();
        int rounds = config.spreadRounds() + config.growthRounds();

        for (int i = 0; i < rounds; ++i) {
            for (int charges = 0; charges < config.chargeCount(); ++charges) {
                spread.addCursors(pos, config.amountPerCharge());
            }

            boolean flag = i < config.spreadRounds();

            for (int l = 0; l < config.spreadAttempts(); ++l) {
                spread.updateCursors(level, pos, random, flag);
            }

            spread.clear();
        }

        BlockPos below = pos.below();
        if (random.nextFloat() <= config.catalystChance()
                && level.getBlockState(below).isCollisionShapeFullBlock(level, below)) {
            level.setBlock(pos, Blocks.SCULK_CATALYST.defaultBlockState(), 3);
        }

        int rareGrowths = config.extraRareGrowths().sample(random);

        for (int i = 0; i < rareGrowths; ++i) {
            BlockPos targetPos = pos.offset(random.nextInt(5) - 2, 0, random.nextInt(5) - 2);
            if (level.getBlockState(targetPos).isAir() && level.getBlockState(targetPos.below())
                    .isFaceSturdy(level, targetPos.below(), Direction.UP)) {
                setBlock(level, targetPos, Blocks.SCULK_SHRIEKER.defaultBlockState());
            }
        }

        return true;
    }

    private boolean canSpreadFrom(LevelAccessor level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.getBlock() instanceof SculkBehaviour) {
            return true;
        } else {
            boolean valid;
            if (state.isAir() || state.is(Blocks.WATER) && state.getFluidState().isSource()) {
                Stream<Direction> var4 = Direction.stream();
                Objects.requireNonNull(pos);
                valid = var4.map(pos::relative).anyMatch(
                        (p_225245_) -> level.getBlockState(p_225245_).isCollisionShapeFullBlock(level, p_225245_));
            } else {
                valid = false;
            }

            return valid;
        }
    }
}
