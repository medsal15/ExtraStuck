package com.medsal15.blocks.food;

import javax.annotation.Nonnull;

import com.medsal15.items.ESItems;
import com.mojang.serialization.MapCodec;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractCauldronBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class DivineTemptationBlock extends AbstractCauldronBlock {
    public static final MapCodec<DivineTemptationBlock> CODEC = simpleCodec(DivineTemptationBlock::new);

    public static final IntegerProperty SERVINGS = IntegerProperty.create("servings", 0, 3);

    public DivineTemptationBlock(Properties properties) {
        super(properties, CauldronInteraction.EMPTY);

        registerDefaultState(stateDefinition.any().setValue(SERVINGS, 3));
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull Builder<Block, BlockState> builder) {
        builder.add(SERVINGS);
    }

    @Override
    public boolean isFull(@Nonnull BlockState state) {
        return false;
    }

    @Override
    protected MapCodec<? extends AbstractCauldronBlock> codec() {
        return CODEC;
    }

    // Manually reimplement FeastBlock
    @Override
    protected ItemInteractionResult useItemOn(@Nonnull ItemStack stack, @Nonnull BlockState state, @Nonnull Level level,
            @Nonnull BlockPos pos, @Nonnull Player player, @Nonnull InteractionHand hand,
            @Nonnull BlockHitResult hitResult) {
        if (stack.is(Items.BOWL) && state.getValue(SERVINGS) >= 1) {
            // Manually do it, less pretty than FD, but still functional
            int servings = state.getValue(SERVINGS);
            BlockState newState;
            if (servings == 1) {
                newState = Blocks.CAULDRON.defaultBlockState();
            } else {
                newState = state.setValue(SERVINGS, servings - 1);
            }

            level.setBlock(pos, newState, 3);
            stack.shrink(servings);
            if (!player.getInventory().add(ESItems.DIVINE_TEMPTATION.toStack())) {
                player.drop(ESItems.DIVINE_TEMPTATION.toStack(), false);
            }
            return ItemInteractionResult.SUCCESS;
        }

        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }
}
