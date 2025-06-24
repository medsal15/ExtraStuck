package com.medsal15.items;

import javax.annotation.Nonnull;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.items.ItemStackHandler;

public class FilterContainer extends ItemStackHandler {
    public static interface Filter {
        public boolean accepts(ItemStack stack);
    }

    /** Accepted ammunition */
    private ItemStack stack;

    public FilterContainer(@Nonnull ItemStack stack) {
        super();
        this.stack = stack;
        setStacks(stack);
    }

    public FilterContainer(int size, @Nonnull ItemStack stack) {
        super(size);
        this.stack = stack;
        setStacks(stack);
    }

    private void setStacks(@Nonnull ItemStack stack) {
        var contents = stack.get(DataComponents.CONTAINER);
        if (contents != null) {
            setStackInSlot(0, contents.copyOne());
        }
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        boolean valid = super.isItemValid(slot, stack);

        if (this.stack.getItem() instanceof Filter filter) {
            return valid && filter.accepts(stack);
        }

        return valid;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(stacks));
    }
}
