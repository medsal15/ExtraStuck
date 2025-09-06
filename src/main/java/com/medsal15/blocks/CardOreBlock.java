package com.medsal15.blocks;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.blockentities.CardOreBlockEntity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams.Builder;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class CardOreBlock extends Block implements EntityBlock {
    public CardOreBlock(Properties properties) {
        super(properties);
    }

    @Override
    @Nullable
    public BlockEntity newBlockEntity(@Nonnull BlockPos pos, @Nonnull BlockState state) {
        return new CardOreBlockEntity(pos, state);
    }

    @Override
    protected List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull Builder builder) {
        if (builder.getOptionalParameter(LootContextParams.BLOCK_ENTITY) instanceof CardOreBlockEntity ore) {
            builder = builder.withDynamicDrop(CardOreBlockEntity.ITEM_DYNAMIC, c -> c.accept(ore.getStackInSlot(0)));
        }
        return super.getDrops(state, builder);
    }
}
