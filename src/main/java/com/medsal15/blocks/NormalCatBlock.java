package com.medsal15.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.block.CustomVoxelShape;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class NormalCatBlock extends HorizontalDirectionalBlock {
    public static final MapCodec<NormalCatBlock> CODEC = simpleCodec(NormalCatBlock::new);

    public NormalCatBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos,
            @Nonnull CollisionContext context) {
        return new CustomVoxelShape(new double[][] { { 3.5, 0, 3.5, 12.5, 10.5, 12.5 } }).create(Direction.NORTH);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);

        builder.add(FACING);
    }

    @Override
    protected InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
        if (level.isClientSide()) {
            return InteractionResult.SUCCESS;
        }

        level.blockEvent(pos, this, 0, 0);
        return InteractionResult.CONSUME;
    }

    @Override
    protected boolean triggerEvent(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos, int id,
            int param) {
        level.playSeededSound(null, pos.getX(), pos.getY(), pos.getZ(), SoundEvents.CAT_AMBIENT, SoundSource.BLOCKS, 1F,
                1F, level.random.nextLong());
        return true;
    }
}
