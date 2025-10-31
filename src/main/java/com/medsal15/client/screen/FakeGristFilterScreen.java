package com.medsal15.client.screen;

import javax.annotation.Nonnull;

import com.medsal15.menus.FakeGristFilterMenu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class FakeGristFilterScreen extends AbstractContainerScreen<FakeGristFilterMenu> {
    public FakeGristFilterScreen(FakeGristFilterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        // TODO? render something (this should not be called anyways)
    }
}
