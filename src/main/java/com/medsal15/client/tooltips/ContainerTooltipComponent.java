package com.medsal15.client.tooltips;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;

public class ContainerTooltipComponent implements ClientTooltipComponent {
    public static final int MARGIN_Y = 4;
    public static final int BORDER_WIDTH = 1;
    public static final ResourceLocation SLOT_SPRITE = ExtraStuck.modid("textures/gui/toolbox/slot.png");
    public static final int SLOT_SIZE_X = 18;
    public static final int SLOT_SIZE_Y = 20;

    protected final ItemContainerContents contents;

    public ContainerTooltipComponent(ItemContainerContents contents) {
        this.contents = contents;
    }

    protected int gridSizeX() {
        return Math.max(2, (int) Math.ceil(Math.sqrt(contents.getSlots() + 1)));
    }

    protected int gridSizeY() {
        return (int) Math.ceil(((double) contents.getSlots() + 1d) / (double) gridSizeX());
    }

    protected int backgroundWidth() {
        return gridSizeX() * SLOT_SIZE_X + 2;
    }

    protected int backgroundHeight() {
        return gridSizeY() * SLOT_SIZE_Y;
    }

    @Override
    public int getWidth(@Nonnull Font font) {
        return backgroundWidth();
    }

    @Override
    public int getHeight() {
        return backgroundHeight() + 4;
    }

    @Override
    public void renderImage(@Nonnull Font font, int x, int y, @Nonnull GuiGraphics guiGraphics) {
        int i = 0;

        for (int gy = 0; gy < gridSizeY(); gy++) {
            for (int gx = 0; gx < gridSizeX(); gx++) {
                int sx = x + gx * SLOT_SIZE_X + 1;
                int sy = y + gy * SLOT_SIZE_Y + 1;
                renderSlot(sx, sy, i++, guiGraphics, font);
            }
        }
    }

    protected void renderSlot(int x, int y, int index, @Nonnull GuiGraphics guiGraphics, @Nonnull Font font) {
        if (index >= Math.max(1, contents.getSlots())) {
            return;
        }
        ItemStack stack = ItemStack.EMPTY;
        if (index < contents.getSlots()) {
            stack = contents.getStackInSlot(index);
        }
        guiGraphics.blit(SLOT_SPRITE, x, y, 0, 0, SLOT_SIZE_X, SLOT_SIZE_Y, SLOT_SIZE_X, SLOT_SIZE_Y);
        guiGraphics.renderItem(stack, x + 1, y + 1);
        guiGraphics.renderItemDecorations(font, stack, x + 1, y + 1);
    }
}
