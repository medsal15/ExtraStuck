package com.medsal15.blockentities.handlers;

import java.util.function.BiPredicate;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.neoforge.items.ItemStackHandler;

public class BEStackHandler extends ItemStackHandler {
    private final BiPredicate<Integer, ItemStack> isValidPredicate;
    private final BlockEntity blockEntity;

    public BEStackHandler(int size, BiPredicate<Integer, ItemStack> isValidPredicate, BlockEntity self) {
        super(size);

        this.isValidPredicate = isValidPredicate;
        this.blockEntity = self;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return isValidPredicate.test(slot, stack);
    }

    @Override
    protected void onContentsChanged(int slot) {
        blockEntity.setChanged();
    }
}
