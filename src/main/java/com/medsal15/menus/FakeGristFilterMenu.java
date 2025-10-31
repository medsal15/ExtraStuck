package com.medsal15.menus;

import javax.annotation.Nonnull;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class FakeGristFilterMenu extends AbstractContainerMenu {
    public FakeGristFilterMenu(int windowId, Inventory inventory) {
        super(ESMenuTypes.GRIST_FILTER.get(), windowId);
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return false;
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        return ItemStack.EMPTY;
    }
}
