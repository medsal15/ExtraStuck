package com.medsal15.blocks.machine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blocks.ESBlockShapes;
import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.block.machine.SmallMachineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.items.IItemHandler;

public class ChargerBlock extends SmallMachineBlock<ChargerBlockEntity> implements SimpleWaterloggedBlock {
    public static final MapCodec<ChargerBlock> CODEC = simpleCodec(ChargerBlock::new);

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public ChargerBlock(Properties properties) {
        super(ESBlockShapes.CHARGER.createRotatedShapes(), ESBlockEntities.CHARGER, properties);
        registerDefaultState(stateDefinition.any().setValue(WATERLOGGED, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    protected MapCodec<? extends Block> codec() {
        return CODEC;
    }

    @Override
    protected boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof ChargerBlockEntity charger)
            return charger.comparatorValue();
        return 0;
    }

    @Override
    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState state = super.getStateForPlacement(context);

        if (state != null) {
            Level level = context.getLevel();
            FluidState fluid = level.getFluidState(context.getClickedPos());
            state = state.setValue(WATERLOGGED, fluid.getType() == Fluids.WATER);
        }

        return state;
    }

    @Override
    protected BlockState updateShape(@Nonnull BlockState state, @Nonnull Direction direction,
            @Nonnull BlockState neighborState, @Nonnull LevelAccessor level, @Nonnull BlockPos pos,
            @Nonnull BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }
        return super.updateShape(state, direction, neighborState, level, pos, neighborPos);
    }

    @Override
    public FluidState getFluidState(@Nonnull BlockState state) {
        return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    @Override
    protected ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level,
            @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand,
            @Nonnull BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof ChargerBlockEntity charger
                && hitResult.getDirection() == Direction.UP) {
            IItemHandler handler = charger.getItemHandler(Direction.UP);
            ItemStack charging = handler.getStackInSlot(ChargerBlockEntity.SLOT_IN);
            @SuppressWarnings("null")
            IEnergyStorage energy = Capabilities.EnergyStorage.ITEM.getCapability(stack, null);

            if (charging.isEmpty() && energy != null) {
                ItemStack remainder = handler.insertItem(ChargerBlockEntity.SLOT_IN, stack, false);
                if (!ItemStack.matches(stack, remainder)) {
                    player.setItemInHand(hand, remainder);
                    return ItemInteractionResult.CONSUME;
                }
            } else if (!charging.isEmpty() && stack.isEmpty() && player.isCrouching()) {
                charging = handler.extractItem(ChargerBlockEntity.SLOT_IN, 1, false);
                player.setItemInHand(hand, charging);
                return ItemInteractionResult.CONSUME;
            }
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
