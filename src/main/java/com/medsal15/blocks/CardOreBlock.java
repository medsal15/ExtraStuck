package com.medsal15.blocks;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.CardOreBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class CardOreBlock extends Block implements EntityBlock {
    public CardOreBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new CardOreBlockEntity(pos, state);
    }

    // TODO this doesnt seem to be the right way, see CruxiteDowelBlock
    @Override
    protected void onRemove(@Nonnull BlockState state, @Nonnull Level level, @Nonnull BlockPos pos,
            @Nonnull BlockState newState, boolean movedByPiston) {
        if (state.getBlock() != newState.getBlock()) {
            if (level.getBlockEntity(pos) instanceof CardOreBlockEntity blockEntity) {
                ItemStack stack = blockEntity.getStackInSlot(0);
                if (!stack.isEmpty()) {
                    ItemEntity entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), stack);
                    level.addFreshEntity(entity);
                }
            }
        }

        super.onRemove(state, level, pos, newState, movedByPiston);
    }
}
