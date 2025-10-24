package com.medsal15.client.screen;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.client.gui.GristButton;
import com.medsal15.menus.MastermindCardMenu;
import com.mojang.blaze3d.systems.RenderSystem;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MastermindCardScreen extends AbstractContainerScreen<MastermindCardMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/mastermind.png");
    private static final int BACKGROUND_WIDTH = 185;
    private static final int BACKGROUND_HEIGHT = 235;

    private static final int GRIST_TEXTURE_X = 196;
    private static final int GRIST_TEXTURE_Y = 0;
    private static final int GRIST_RENDER_X = 25;
    private static final int GRIST_RENDER_Y = 32;
    private static final int GRIST_WIDTH = 20;
    private static final int GRIST_HEIGHT = 20;

    private static final int ATTEMPT_AREA_X = 25;
    private static final int ATTEMPT_AREA_Y = 79;
    private static final int ATTEMPT_ROWS = 5;

    private static final int HINT_AREA_X = 115;
    private static final int HINT_TEXTURE_X = 235;
    private static final int HINT_TEXTURE_Y = 42;
    private static final int HINT_SIZE = 10;

    public MastermindCardScreen(MastermindCardMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = BACKGROUND_WIDTH;
        imageHeight = BACKGROUND_HEIGHT;
    }

    @Override
    protected void init() {
        super.init();

        for (int i = 0; i < 6; i++) {
            int x = (i % 3) * GRIST_WIDTH + GRIST_RENDER_X + leftPos;
            int y = (i / 3) * GRIST_HEIGHT + GRIST_RENDER_Y + topPos;
            final int grist = i;
            addRenderableWidget(new GristButton(x, y, (b) -> {
                menu.addAttempt(grist);
            }, i));
        }

    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(guiGraphics, mouseX, mouseY, partialTick);

        int difficulty = menu.getDifficulty();

        for (Renderable renderable : this.renderables) {
            if (renderable instanceof GristButton button) {
                button.renderWidget(guiGraphics, mouseX, mouseY, partialTick, difficulty);
            } else {
                renderable.render(guiGraphics, mouseX, mouseY, partialTick);
            }
        }

        renderAttempt(guiGraphics, ATTEMPT_ROWS, menu.getCurrent(), menu.getCurrentSlot());

        for (int i = 0; i < menu.getAttemptsCount(); i++) {
            int[] attempt = menu.getAttempt(i);
            renderAttempt(guiGraphics, ATTEMPT_ROWS - i - 1, attempt);
            int[] hints = menu.getHint(i);
            renderHints(guiGraphics, ATTEMPT_ROWS - i - 1, hints[0], hints[1]);
        }
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    /**
     * @param row Row, from the bottom
     */
    private void renderAttempt(@Nonnull GuiGraphics graphics, int row, int[] code, int max) {
        if (max > code.length)
            max = code.length;
        for (int i = 0; i < max; i++) {
            int grist = code[i];
            int draw_x = i * GRIST_WIDTH + leftPos + ATTEMPT_AREA_X;
            int draw_y = row * GRIST_HEIGHT + topPos + ATTEMPT_AREA_Y;
            int from_x = GRIST_TEXTURE_X + (grist % 3) * GRIST_WIDTH;
            int from_y = GRIST_TEXTURE_Y + (grist / 3) * GRIST_HEIGHT;

            graphics.blit(BACKGROUND_TEXTURE, draw_x, draw_y, from_x, from_y, GRIST_WIDTH, GRIST_HEIGHT);
        }
    }

    private void renderAttempt(@Nonnull GuiGraphics graphics, int row, int[] code) {
        renderAttempt(graphics, row, code, 4);
    }

    /**
     * @param row       Row, from the top
     * @param correct   Correct entries in the correct slots
     * @param msiplaced Correct entries in the wrong slots (>= correct)
     */
    private void renderHints(@Nonnull GuiGraphics graphics, int row, int correct, int misplaced) {
        for (int i = 0; i < 4; i++) {
            int from_x;
            if (i < correct) {
                from_x = HINT_TEXTURE_X;
            } else if (i < misplaced) {
                from_x = HINT_TEXTURE_X + HINT_SIZE;
            } else {
                break;
            }

            int draw_x = (i % 2) * HINT_SIZE + leftPos + HINT_AREA_X;
            int draw_y = (i / 2) * HINT_SIZE + topPos + ATTEMPT_AREA_Y + row * GRIST_HEIGHT;

            graphics.blit(BACKGROUND_TEXTURE, draw_x, draw_y, from_x, HINT_TEXTURE_Y, HINT_SIZE, HINT_SIZE);
        }
    }
}
