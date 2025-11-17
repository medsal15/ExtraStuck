package com.medsal15.modus;

import java.util.ArrayList;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;
import com.mraof.minestuck.item.CaptchaCardItem;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerPlayer;
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

        ArrayList<ItemStack> pile = removePile(id);
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

    protected ArrayList<ItemStack> removePile(int id) {
        ArrayList<ItemStack> items = new ArrayList<>();
        ArrayList<Integer> ids = new ArrayList<>();

        // I know there's a better way to do this, but I can't figure it out
        int cols = getWidth();
        for (int i = id; i < list.size(); i += cols) {
            ids.add(i);
        }
        for (int i : ids.reversed()) {
            items.add(list.remove(i));
        }

        return items;
    }
}
