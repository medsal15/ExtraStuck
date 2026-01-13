package com.medsal15.menus.slot;

import java.util.Optional;

import javax.annotation.Nonnull;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GhostItemSlot extends Slot {
    protected final int slot;

    public GhostItemSlot(Container container, int slot, int x, int y) {
        super(container, slot, x, y);
        this.slot = slot;
    }

    @Override
    public boolean isFake() {
        return true;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        return true;
    }

    @Override
    public boolean mayPickup(@Nonnull Player player) {
        // Prevents sorting mods (like Inventory Management Deluxe) from taking contents
        return false;
    }

    @Override
    public Optional<ItemStack> tryRemove(int count, int decrement, @Nonnull Player player) {
        container.setItem(slot, ItemStack.EMPTY);

        return Optional.empty();
    }

    @Override
    public ItemStack safeInsert(@Nonnull ItemStack stack, int increment) {
        container.setItem(slot, stack.copyWithCount(1));

        return stack;
    }

    @Override
    public ItemStack safeTake(int count, int decrement, @Nonnull Player player) {
        container.setItem(slot, ItemStack.EMPTY);

        return ItemStack.EMPTY;
    }
}
