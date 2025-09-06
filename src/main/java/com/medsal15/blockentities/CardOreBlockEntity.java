package com.medsal15.blockentities;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.items.IItemHandler;

public class CardOreBlockEntity extends BlockEntity implements IItemHandler {
    public static final ResourceLocation ITEM_DYNAMIC = ExtraStuck.modid("item");
    public static final String ITEM_STORED = "stored";

    private ItemStack stored;

    public CardOreBlockEntity(BlockPos pos, BlockState state) {
        super(ESBlockEntities.CARD_ORE.get(), pos, state);
        stored = ItemStack.EMPTY;
    }

    @Override
    protected void saveAdditional(@Nonnull CompoundTag tag, @Nonnull Provider registries) {
        super.saveAdditional(tag, registries);

        tag.put(ITEM_STORED, stored.saveOptional(registries));
    }

    @Override
    protected void loadAdditional(@Nonnull CompoundTag tag, @Nonnull Provider registries) {
        super.loadAdditional(tag, registries);

        stored = ItemStack.parseOptional(registries, tag.getCompound(ITEM_STORED));
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return stored;
    }

    @Override
    public int getSlotLimit(int slot) {
        if (!stored.isEmpty())
            return stored.getMaxStackSize();
        return Item.ABSOLUTE_MAX_STACK_SIZE;
    }

    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (ItemStack.isSameItemSameComponents(stored, stack)) {
            int sum = stack.getCount() + stored.getCount();
            int leftover = 0;
            if (sum > stored.getMaxStackSize()) {
                leftover = sum - stored.getMaxStackSize();
            }

            if (!simulate) {
                stored.setCount(Math.min(sum, stored.getMaxStackSize()));
                stack.setCount(leftover);
            }
            if (leftover == 0)
                return ItemStack.EMPTY;
            else
                return stack;
        }
        if (stored.isEmpty()) {
            if (!simulate) {
                stored = stack;
            }
            return ItemStack.EMPTY;
        }

        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        // Nuh uh
        return ItemStack.EMPTY;
    }
}
