package com.medsal15.modus;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.inventory.PlayerEnderChestContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

/**
 * FIXME after modifying an ender chest manually, items only update on
 * insert/extract
 *
 * Listeners do not work, as they're only added on the server side, but needed
 * on the client side
 */
public class EnderModus extends Modus {
    /** Cards available */
    public static final int SIZE = 27;
    protected NonNullList<ItemStack> list;

    // Client side
    protected boolean changed;
    protected NonNullList<ItemStack> items;

    public EnderModus(ModusType<? extends EnderModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void readFromNBT(CompoundTag nbt, Provider provider) {
        list = NonNullList.withSize(SIZE, ItemStack.EMPTY);

        for (int i = 0; i < SIZE; i++)
            if (nbt.contains("item" + i, Tag.TAG_COMPOUND)) {
                list.set(i, ItemStack.parse(provider, nbt.getCompound("item" + i)).orElseThrow());
            }
        if (side == LogicalSide.CLIENT) {
            if (items == null)
                items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
            changed = true;
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        for (int i = 0; i < list.size() && i < SIZE; i++) {
            ItemStack stack = list.get(i);
            if (!stack.isEmpty()) {
                nbt.put("item" + i, stack.save(provider));
            }
        }
        return nbt;
    }

    private void setList(SimpleContainer container) {
        list = NonNullList.copyOf(container.getItems());
        changed = true;
        markDirty();
    }

    @Override
    public void initModus(ItemStack item, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        PlayerEnderChestContainer inventory = player.getEnderChestInventory();
        setList(inventory);

        if (side == LogicalSide.CLIENT) {
            items = NonNullList.withSize(SIZE, ItemStack.EMPTY);
            changed = true;
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        if (side == LogicalSide.SERVER) {
            // Used only when replacing the modus
            return NonNullList.create();
        }

        if (changed) {
            fillList(items);
        }

        return items;
    }

    protected void fillList(NonNullList<ItemStack> items) {
        items.clear();
        for (int i = 0; i < list.size() && i < SIZE; i++) {
            items.set(i, list.get(i));
        }
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int slot, boolean asCard) {
        if (slot == CaptchaDeckHandler.EMPTY_CARD || slot == CaptchaDeckHandler.EMPTY_SYLLADEX || list.isEmpty())
            return ItemStack.EMPTY;

        PlayerEnderChestContainer inventory = player.getEnderChestInventory();
        ItemStack taken = inventory.removeItem(slot, Item.ABSOLUTE_MAX_STACK_SIZE);
        setList(inventory);
        markDirty();
        return taken;
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack item) {
        PlayerEnderChestContainer inventory = player.getEnderChestInventory();
        ItemStack leftover = inventory.addItem(item);
        setList(inventory);
        if (!leftover.isEmpty()) {
            CaptchaDeckHandler.launchItem(player, leftover);
            changed = true;
        }
        return !leftover.equals(item);
    }

    @Override
    public boolean increaseSize(ServerPlayer player) {
        return false;
    }

    @Override
    public boolean canSwitchFrom(Modus modus) {
        return false;
    }

    @Override
    public int getSize() {
        return SIZE;
    }
}
