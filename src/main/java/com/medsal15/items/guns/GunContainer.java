package com.medsal15.items.guns;

import javax.annotation.Nonnull;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.items.ItemStackHandler;

public class GunContainer extends ItemStackHandler {
    /** Accepted ammunition */
    public static interface Filter {
        public boolean accepts(ItemStack stack);
    }

    /** Gun */
    private ItemStack stack;

    public GunContainer(@Nonnull ItemStack stack) {
        super();
        this.stack = stack;
        setStacks(stack);
    }

    public GunContainer(int size, @Nonnull ItemStack stack) {
        super(size);
        this.stack = stack;
        setStacks(stack);
    }

    private void setStacks(@Nonnull ItemStack stack) {
        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        if (contents != null) {
            setStackInSlot(0, contents.copyOne());
        }
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        boolean valid = super.isItemValid(slot, stack);

        if (this.stack.getItem() instanceof ESGun gun) {
            return valid && gun.accepts(stack);
        }

        return valid;
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        int limit = super.getStackLimit(slot, stack);

        if (this.stack.getItem() instanceof ESGun gun) {
            limit = Math.min(limit, gun.getMaxBullets());
        }

        return limit;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        stack.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(stacks));
    }
}
