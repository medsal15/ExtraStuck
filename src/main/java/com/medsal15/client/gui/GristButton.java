package com.medsal15.client.gui;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class GristButton extends ExtendedButton {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/mastermind.png");
    private static final int GRIST_TEXTURE_X = 196;
    private static final int GRIST_TEXTURE_Y = 0;
    private static final int GRIST_WIDTH = 20;
    private static final int GRIST_HEIGHT = 20;
    private static final int GRIST_LOCK_X = 235;
    private static final int GRIST_LOCK_Y = 52;

    private final int grist;
    private final int difficulty;

    public GristButton(int x, int y, OnPress handler, int grist, int difficulty) {
        super(x, y, GRIST_WIDTH, GRIST_HEIGHT, null, handler);
        this.grist = grist;
        this.difficulty = difficulty;
    }

    @Override
    public void renderWidget(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int from_x = GRIST_TEXTURE_X + (grist % 3) * GRIST_WIDTH;
        int from_y = GRIST_TEXTURE_Y + (grist / 3) * GRIST_HEIGHT;

        guiGraphics.blit(BACKGROUND_TEXTURE, this.getX(), this.getY(), from_x, from_y, GRIST_WIDTH, GRIST_HEIGHT);
        if (grist >= difficulty) {
            guiGraphics.blit(BACKGROUND_TEXTURE, this.getX(), this.getY(), GRIST_LOCK_X, GRIST_LOCK_Y, GRIST_WIDTH,
                    GRIST_HEIGHT);
        }
    }
}
