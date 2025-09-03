package com.medsal15.modus;

import com.mraof.minestuck.inventory.captchalogue.CaptchaDeckHandler;
import com.mraof.minestuck.inventory.captchalogue.ModusType;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.fml.LogicalSide;

public class VoidModus extends BaseModus {
    public VoidModus(ModusType<? extends VoidModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public boolean putItemStack(ServerPlayer player, ItemStack item) {
        if (size <= list.size() || item.isEmpty())
            return false;

        boolean added = false;
        for (ItemStack stack : list) {
            if (ItemStack.isSameItemSameComponents(stack, item)) {
                int count = Math.min(stack.getMaxStackSize(), stack.getCount() + item.getCount());
                stack.setCount(count);
                added = true;
            }
        }
        if (!added) {
            list.add(item);
        }
        markDirty();

        // Delete anyway
        return true;
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int id, boolean asCard) {
        if (asCard || id == CaptchaDeckHandler.EMPTY_SYLLADEX || id == CaptchaDeckHandler.EMPTY_CARD) {
            return super.getItem(player, id, asCard);
        }

        if (list.isEmpty())
            return ItemStack.EMPTY;

        if (id < 0 || id >= list.size())
            return ItemStack.EMPTY;

        ItemStack item = list.get(id);
        player.getInventory().clearOrCountMatchingItems(stack -> ItemStack.isSameItemSameComponents(stack, item), 9999,
                player.getInventory());

        return ItemStack.EMPTY;
    }
}
