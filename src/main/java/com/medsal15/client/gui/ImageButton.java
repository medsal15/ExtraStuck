package com.medsal15.client.gui;

import javax.annotation.Nonnull;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ImageButton extends Button {
    private final ResourceLocation texture;
    private final int texture_x, texture_y;

    public ImageButton(int x, int y, int width, int height, Button.OnPress onPress, ResourceLocation texture, int texX,
            int texY) {
        super(x, y, width, height, Component.empty(), onPress, DEFAULT_NARRATION);

        this.texture = texture;
        this.texture_x = texX;
        this.texture_y = texY;
    }

    @Override
    protected void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.blit(texture, getX(), getY(), texture_x, texture_y, getWidth(), getHeight());
    }
}
