package com.medsal15.modus;

import java.util.Iterator;

import com.mraof.minestuck.MinestuckConfig;
import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

public class BaseModus extends Modus {
    /** Cards available */
    protected int size;
    protected NonNullList<ItemStack> list;

    // Client side
    protected boolean changed;
    protected NonNullList<ItemStack> items;

    public BaseModus(ModusType<? extends BaseModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void readFromNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        size = nbt.getInt("size");
        list = NonNullList.create();

        for (int i = 0; i < size; i++)
            if (nbt.contains("item" + i, Tag.TAG_COMPOUND))
                list.add(ItemStack.parse(provider, nbt.getCompound("item" + i)).orElseThrow());
            else
                break;
        if (side == LogicalSide.CLIENT) {
            if (items == null)
                items = NonNullList.create();
            changed = true;
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt, HolderLookup.Provider provider) {
        nbt.putInt("size", size);
        Iterator<ItemStack> iter = list.iterator();
        for (int i = 0; i < list.size(); i++) {
            ItemStack stack = iter.next();
            nbt.put("item" + i, stack.save(provider));
        }
        return nbt;
    }

    @Override
    public void initModus(ItemStack item, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        this.size = size;
        list = NonNullList.create();

        if (side == LogicalSide.CLIENT) {
            items = NonNullList.create();
            changed = true;
        }
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        if (side == LogicalSide.SERVER) {
            // Used only when replacing the modus
            NonNullList<ItemStack> items = NonNullList.create();
            fillList(items);
            return items;
        }

        if (changed) {
            fillList(items);
        }

        return items;
    }

    protected void fillList(NonNullList<ItemStack> items) {
        items.clear();
        for (ItemStack stack : list) {
            items.add(stack);
        }
        while (items.size() < size)
            items.add(ItemStack.EMPTY);
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard) {
        if (id == CaptchaDeckHandler.EMPTY_CARD) {
            if (list.size() < size) {
                size--;
                markDirty();
                return MSItems.CAPTCHA_CARD.toStack();
            } else {
                return ItemStack.EMPTY;
            }
        }

        if (list.isEmpty())
            return ItemStack.EMPTY;

        if (id == CaptchaDeckHandler.EMPTY_SYLLADEX) {
            for (ItemStack item : list) {
                CaptchaDeckHandler.launchAnyItem(player, item);
            }
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if (id < 0 || id >= list.size()) {
            return ItemStack.EMPTY;
        }

        ItemStack item = list.remove(id);
        markDirty();

        if (asCard) {
            size--;
            markDirty();
            item = CaptchaCardItem.createCardWithItem(item, player.server);
        }

        return item;
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack item) {
        if (size <= list.size() || item.isEmpty())
            return false;

        boolean added = false;
        if (!added && list.size() < size) {
            list.add(item);
            markDirty();
            added = true;
        }

        return added;
    }

    @Override
    public boolean increaseSize(ServerPlayer player) {
        if (MinestuckConfig.SERVER.modusMaxSize.get() > 0 && size >= MinestuckConfig.SERVER.modusMaxSize.get())
            return false;

        size++;
        markDirty();

        return true;
    }

    @Override
    public boolean canSwitchFrom(Modus modus) {
        return false;
    }

    @Override
    public int getSize() {
        return size;
    }
}
