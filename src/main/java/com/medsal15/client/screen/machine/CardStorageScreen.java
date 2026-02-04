package com.medsal15.client.screen.machine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.menus.StorageBlockMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.CardStoredItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class CardStorageScreen extends AbstractContainerScreen<StorageBlockMenu.Card> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/storage.png");

    public CardStorageScreen(StorageBlockMenu.Card menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageHeight = 222;
        imageWidth = 176;
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    protected void init() {
        leftPos = (width - imageWidth) / 2;
        topPos = (height - imageHeight) / 2;
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    protected void renderSlotContents(@Nonnull GuiGraphics guiGraphics, @Nonnull ItemStack itemstack,
            @Nonnull Slot slot, @Nullable String countString) {
        ItemStack stack = itemstack.copy();

        if (itemstack.is(MSItems.CAPTCHA_CARD) && slot.index < 54) {
            if (itemstack.has(MSItemComponents.ENCODED_ITEM)) {
                stack = new ItemStack(itemstack.get(MSItemComponents.ENCODED_ITEM).item(), itemstack.getCount());

            } else if (itemstack.has(MSItemComponents.CARD_STORED_ITEM)) {
                CardStoredItemComponent storedItemComponent = itemstack.get(MSItemComponents.CARD_STORED_ITEM);
                stack = storedItemComponent.storedStack().copyWithCount(itemstack.getCount());

            }
        }

        super.renderSlotContents(guiGraphics, stack, slot, countString);
    }
}
