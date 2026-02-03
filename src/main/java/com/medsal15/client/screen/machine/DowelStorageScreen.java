package com.medsal15.client.screen.machine;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.menus.DowelStorageMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.util.ColorHandler;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

//TODO test if this works
public class DowelStorageScreen extends AbstractContainerScreen<DowelStorageMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/storage.png");

    public DowelStorageScreen(DowelStorageMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        imageHeight = 222;
        imageWidth = 176;
        inventoryLabelY = imageHeight - 94;
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - this.imageWidth) / 2;
        this.topPos = (this.height - this.imageHeight) / 2;
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
        if (itemstack.has(MSItemComponents.ENCODED_ITEM) && itemstack.is(MSItems.CRUXITE_DOWEL) && slot.index < 54) {
            stack = new ItemStack(itemstack.get(MSItemComponents.ENCODED_ITEM).item(), itemstack.getCount());
        }
        super.renderSlotContents(guiGraphics, stack, slot, countString);
    }

    @Override
    public int getSlotColor(int index) {
        if (index < 54) {
            Slot slot = menu.getSlot(index);
            if (slot.hasItem() && slot.getItem().is(MSItems.CRUXITE_DOWEL)) {
                // Copy dowel's color with 50% transparency
                return ColorHandler.getColorFromStack(slot.getItem()) | 0x3f000000;
            }
        }
        return super.getSlotColor(index);
    }
}
