package com.medsal15.menus;

import java.util.function.Predicate;

import javax.annotation.Nonnull;

import com.medsal15.blocks.ESBlocks;
import com.mraof.minestuck.inventory.ContainerHelper;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public abstract class StorageBlockMenu extends AbstractContainerMenu {
    protected final ContainerLevelAccess access;
    private final Predicate<ItemStack> isValidItem;

    public StorageBlockMenu(MenuType<? extends StorageBlockMenu> type, int windowId, ContainerLevelAccess access,
            Inventory playerInventory,
            IItemHandler inventory, Predicate<ItemStack> validPredicate) {
        super(type, windowId);
        this.access = access;
        this.isValidItem = validPredicate;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new SlotItemHandler(inventory, col + row * 9, 8 + 18 * col, 18 + 18 * row));
            }
        }
        ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 140, playerInventory);
    }

    protected abstract Block validBlock();

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return stillValid(access, player, validBlock());
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack held = slot.getItem();

            if (!isValidItem.test(held))
                return ItemStack.EMPTY;

            stack = held.copy();

            if (index < 54) {
                if (!moveItemStackTo(held, 54, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(held, 0, 54, false)) {
                return ItemStack.EMPTY;
            }

            if (held.isEmpty()) {
                slot.setByPlayer(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return stack;
    }

    public static class Dowel extends StorageBlockMenu {
        public Dowel(int windowId, ContainerLevelAccess access, Inventory playerInventory,
                IItemHandler inventory, Predicate<ItemStack> validPredicate) {
            super(ESMenuTypes.DOWEL_STORAGE.get(), windowId, access, playerInventory, inventory, validPredicate);
        }

        public Dowel(int windowId, Inventory inventory, FriendlyByteBuf buffer) {
            this(windowId, ContainerLevelAccess.NULL, inventory, new ItemStackHandler(54), s -> false);
        }

        @Override
        protected Block validBlock() {
            return ESBlocks.DOWEL_STORAGE.get();
        }
    }

    public static class Card extends StorageBlockMenu {
        public Card(int windowId, ContainerLevelAccess access, Inventory playerInventory,
                IItemHandler inventory, Predicate<ItemStack> validPredicate) {
            super(ESMenuTypes.CARD_STORAGE.get(), windowId, access, playerInventory, inventory, validPredicate);
        }

        public Card(int windowId, Inventory inventory, FriendlyByteBuf buffer) {
            this(windowId, ContainerLevelAccess.NULL, inventory, new ItemStackHandler(54), s -> false);
        }

        @Override
        protected Block validBlock() {
            return ESBlocks.CARD_STORAGE.get();
        }
    }
}
