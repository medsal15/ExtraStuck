package com.medsal15.blocks;

import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.ESBlockEntities;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.mraof.minestuck.block.CustomVoxelShape;
import com.mraof.minestuck.block.machine.SmallMachineBlock;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PrinterBlock extends SmallMachineBlock<PrinterBlockEntity> {
    private static final CustomVoxelShape shape = new CustomVoxelShape(new double[][] {
            { 0, 0, 0, 16, 12, 16 }
    });
    private static final CustomVoxelShape dowel = new CustomVoxelShape(new double[][] {
            { 6, 12, 6, 10, 16, 10 }
    });

    private static Map<Direction, VoxelShape> doweled = null;

    public static final BooleanProperty HAS_DOWEL = BooleanProperty.create("has_dowel");

    public PrinterBlock(Properties properties) {
        super(shape.createRotatedShapes(), ESBlockEntities.PRINTER, properties);
        registerDefaultState(this.stateDefinition.any().setValue(HAS_DOWEL, false));
        doweled = shape.merge(dowel).createRotatedShapes();
    }

    @Override
    protected VoxelShape getShape(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos,
            @Nonnull CollisionContext context) {
        if (state.getValue(HAS_DOWEL)) {
            return doweled.get(state.getValue(FACING));
        } else {
            return super.getShape(state, level, pos, context);
        }
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HAS_DOWEL);
    }

    @Override
    protected boolean hasAnalogOutputSignal(@Nonnull BlockState state) {
        return true;
    }

    @Override
    protected int getAnalogOutputSignal(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos) {
        if (level.getBlockEntity(pos) instanceof PrinterBlockEntity printer)
            return printer.comparatorValue();
        return 0;
    }

    @Override
    public boolean canConnectRedstone(@Nonnull BlockState state, @Nonnull BlockGetter level, @Nonnull BlockPos pos,
            @Nullable Direction side) {
        return side != null;
    }
}
