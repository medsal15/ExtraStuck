package com.medsal15.modus;

import java.util.ArrayList;
import java.util.List;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSSoundEvents;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

public class PileModus extends BaseModus {
    public PileModus(ModusType<? extends PileModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public NonNullList<ItemStack> getItems() {
        if (side == LogicalSide.SERVER) {
            // Used only when replacing the modus
            NonNullList<ItemStack> items = NonNullList.copyOf(list);
            return items;
        }

        if (changed) {
            items = NonNullList.copyOf(list);
        }

        return items;
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
            player.level().playSound(null, player.getX(), player.getY(), player.getZ(),
                    MSSoundEvents.EVENT_CAPTCHALOGUE_SHUFFLE.get(), SoundSource.AMBIENT, 1F, 1F);
            for (ItemStack item : list) {
                CaptchaDeckHandler.ejectAnyItem(player, item);
            }
            list.clear();
            markDirty();
            return ItemStack.EMPTY;
        }

        if (id < 0 || id >= list.size()) {
            return ItemStack.EMPTY;
        }

        List<ItemStack> pile = removePile(id);
        ItemStack item = pile.getLast();
        for (int i = 0; i < pile.size() - 1; i++)
            CaptchaDeckHandler.launchAnyItem(player, pile.get(i));
        markDirty();

        if (asCard) {
            size--;
            markDirty();
            item = CaptchaCardItem.createCardWithItem(item, player.server);
        }

        return item;
    }

    public int getWidth() {
        return (int) Math.ceil(Math.sqrt(list.size()));
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack item) {
        if (size <= list.size() || item.isEmpty())
            return false;

        boolean added = false;
        if (getWidth() > 0) {
            // Get top slots with the same item
            ArrayList<Integer> matchingSlots = new ArrayList<>();
            final int COLS = getWidth();
            for (int i = 0; i < COLS; i++) {
                List<Integer> pile = getPile(i);
                int last = pile.getLast();
                if (ItemStack.isSameItemSameComponents(list.get(last), item))
                    matchingSlots.add(last);
            }
            // Add to top slots
            for (int slot : matchingSlots) {
                ItemStack stored = list.get(slot);
                int increase = Math.min(item.getCount(), item.getMaxStackSize() - stored.getCount());
                stored.grow(increase);
                item.shrink(increase);
                added = true;
                if (item.isEmpty())
                    break;
            }
        }
        if (!item.isEmpty()) {
            added |= super.putItemStack(player, item);
        }
        if (added)
            markDirty();

        return added;
    }

    protected List<Integer> getPile(int slot) {
        ArrayList<Integer> ids = new ArrayList<>();
        // I know there's a better way to do this, but I can't figure it out
        int cols = getWidth();
        for (int i = slot; i < list.size(); i += cols) {
            ids.add(i);
        }

        return ids;
    }

    protected List<ItemStack> removePile(int id) {
        ArrayList<ItemStack> items = new ArrayList<>();
        List<Integer> ids = getPile(id);

        for (int i : ids.reversed()) {
            items.add(list.remove(i));
        }

        return items;
    }

    @Override
    protected boolean canStackExisting() {
        return false;
    }
}
