package com.medsal15.blocks.machine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.StorageBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities.ItemHandler;
import net.neoforged.neoforge.items.IItemHandler;

public class CardStorageBlock extends BaseEntityBlock {
    public static final MapCodec<CardStorageBlock> CODEC = simpleCodec(CardStorageBlock::new);
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public CardStorageBlock(Properties properties) {
        super(properties);
        registerDefaultState(stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(@Nonnull BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection().getOpposite());
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            @SuppressWarnings("null")
            IItemHandler itemHandler = (IItemHandler) level.getCapability(ItemHandler.BLOCK, pos, state,
                    (BlockEntity) null, (Direction) null);
            if (itemHandler != null) {
                for (int i = 0; i < itemHandler.getSlots(); ++i) {
                    Containers.dropItemStack(level, (double) pos.getX(), (double) pos.getY(), (double) pos.getZ(),
                            itemHandler.getStackInSlot(i));
                }
            }

            super.onRemove(state, level, pos, newState, movedByPiston);
        }
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new StorageBlockEntity.Card(pos, state);
    }

    @Override
    protected boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof StorageBlockEntity storage) {
            return storage.getAnalogOutputSignal();
        }
        return 0;
    }

    @Override
    protected InteractionResult useWithoutItem(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull Player player, @Nonnull BlockHitResult hitResult) {
        if (player instanceof ServerPlayer serverPlayer) {
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null && canOpen(state, level, pos) && blockEntity instanceof MenuProvider provider) {
                level.playSound(null, pos, SoundEvents.BARREL_OPEN, SoundSource.BLOCKS, .5f,
                        level.random.nextFloat() * 0.1F + 0.9F);
                serverPlayer.openMenu(provider);
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    protected RenderShape getRenderShape(@Nonnull BlockState state) {
        return RenderShape.MODEL;
    }

    private boolean canOpen(BlockState state, Level level, BlockPos pos) {
        BlockPos frontPos = pos.relative(state.getValue(FACING));
        VoxelShape frontShape = level.getBlockState(frontPos).getCollisionShape(level, frontPos);
        return frontShape.isEmpty();
    }
}
