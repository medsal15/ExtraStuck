package com.medsal15.menus.slot;

import java.util.function.Function;

import javax.annotation.Nonnull;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class FilteredInputSlot extends SlotItemHandler {
    public Function<ItemStack, Boolean> filter;

    public FilteredInputSlot(IItemHandler inventory, int index, int x, int y, Function<ItemStack, Boolean> filter) {
        super(inventory, index, x, y);
        this.filter = filter;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return filter.apply(stack);
    }
}
