package com.medsal15.blocks.machine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.BlasterBlockEntity;
import com.medsal15.blockentities.ESBlockEntities;
import com.mraof.minestuck.block.machine.SmallMachineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class BlasterBlock extends SmallMachineBlock<BlasterBlockEntity> {
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    public BlasterBlock(Properties properties) {
        super(ESBlockShapes.URANIUM_BLASTER.createRotatedShapes(), ESBlockEntities.BLASTER, properties);
        registerDefaultState(this.stateDefinition.any().setValue(ACTIVE, false));
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVE);
    }

    @Override
    public boolean canConnectRedstone(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos,
            @Nullable Direction side) {
        return side != null;
    }
}
