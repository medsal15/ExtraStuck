package com.medsal15.menus;

import javax.annotation.Nonnull;

import com.medsal15.blocks.ESBlocks;
import com.mraof.minestuck.inventory.ContainerHelper;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.SlotItemHandler;

public class DowelStorageMenu extends AbstractContainerMenu {
    protected final ContainerLevelAccess access;

    public DowelStorageMenu(int windowId, ContainerLevelAccess access, Inventory playerInventory,
            IItemHandler inventory) {
        super(ESMenuTypes.DOWEL_STORAGE.get(), windowId);
        this.access = access;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new SlotItemHandler(inventory, col + row * 9, 8 + 18 * col, 18 + 18 * row));
            }
        }
        ContainerHelper.addPlayerInventorySlots(this::addSlot, 8, 140, playerInventory);
    }

    public DowelStorageMenu(int windowId, Inventory inventory, FriendlyByteBuf buffer) {
        this(windowId, ContainerLevelAccess.NULL, inventory, new ItemStackHandler(54));
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return stillValid(access, player, ESBlocks.DOWEL_STORAGE.get());
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        ItemStack stack = ItemStack.EMPTY;
        Slot slot = slots.get(index);

        if (slot != null && slot.hasItem()) {
            ItemStack held = slot.getItem();

            if (!held.is(MSItems.CRUXITE_DOWEL))
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
}
