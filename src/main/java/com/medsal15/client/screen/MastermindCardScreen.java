package com.medsal15.client.screen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.client.gui.GristButton;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.ESItems;
import com.medsal15.items.modus.MastermindCardItem;
import com.medsal15.network.ESPackets.MastermindAddAttempt;
import com.medsal15.network.ESPackets.MastermindDestroy;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class MastermindCardScreen extends Screen {
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

    private final ItemStack card;
    private final Player player;
    private final int difficulty;
    private final int[] input = new int[4];
    private final int[] correct;
    private final int[] count;

    private int leftPos;
    private int topPos;
    private int slot = 0;

    public MastermindCardScreen(ItemStack card, Player player) {
        super(getTitle(card));
        this.card = card.copy();
        this.player = player;
        difficulty = card.get(ESDataComponents.DIFFICULTY);
        correct = MastermindCardItem.breakCode(card);
        count = new int[difficulty];
        for (int i = 0; i < correct.length; i++) {
            count[correct[i]]++;
        }
    }

    @Override
    protected void init() {
        this.leftPos = (this.width - BACKGROUND_WIDTH) / 2;
        this.topPos = (this.height - BACKGROUND_HEIGHT) / 2;

        for (int i = 0; i < 6; i++) {
            int x = (i % 3) * GRIST_WIDTH + GRIST_RENDER_X + leftPos;
            int y = (i / 3) * GRIST_HEIGHT + GRIST_RENDER_Y + topPos;
            final int grist = i;
            addRenderableWidget(new GristButton(x, y, (b) -> {
                addAttempt(grist);
            }, i, difficulty));
        }
    }

    @Override
    public void renderBackground(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        renderTransparentBackground(guiGraphics);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        // Render input
        renderAttempt(graphics, input, ATTEMPT_ROWS, slot);

        if (card.has(ESDataComponents.ATTEMPTS)) {
            List<Integer> attempts = card.get(ESDataComponents.ATTEMPTS);
            int rows = ATTEMPT_ROWS;
            for (int i = 0; i < rows && i < attempts.size(); i++) {
                int[] code = MastermindCardItem.breakCode(attempts.get(i), difficulty);
                renderAttempt(graphics, code, rows - i - 1);
            }
        }
    }

    /**
     * @param row Row, from the bottom
     */
    private void renderAttempt(@Nonnull GuiGraphics graphics, int[] code, int row, int max, boolean hints) {
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

        if (hints) {
            int right = 0;
            int misplaced = 0;
            int[] attempt = new int[difficulty];
            for (int i = 0; i < max; i++) {
                if (code[i] == this.correct[i])
                    right++;
                attempt[code[i]]++;
            }
            for (int i = 0; i < difficulty; i++) {
                misplaced += Math.min(count[i], attempt[i]);
            }

            for (int i = 0; i < max; i++) {
                int from_x;
                if (i < right) {
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

    private void renderAttempt(@Nonnull GuiGraphics graphics, int[] code, int row, int max) {
        renderAttempt(graphics, code, row, max, false);
    }

    private void renderAttempt(@Nonnull GuiGraphics graphics, int[] code, int row) {
        renderAttempt(graphics, code, row, 4, true);
    }

    private void addAttempt(int grist) {
        if (grist < difficulty && slot < 4) {
            input[slot] = grist;
            slot++;

            if (slot == 4) {
                checkAttempt();
            }
        }
    }

    private void checkAttempt() {
        for (int i = 0; i < 4; i++) {
            if (correct[i] != input[i]) {
                failAttempt();
                return;
            }
        }
        successAttempt();
    }

    private void failAttempt() {
        slot = 0;
        List<Integer> attempts = card.getOrDefault(ESDataComponents.ATTEMPTS, new ArrayList<>());
        if (attempts.size() + 1 > ATTEMPT_ROWS) {
            // Failure! Destroy the whole stack and close the screen
            PacketDistributor.sendToServer(new MastermindDestroy(false), new CustomPacketPayload[0]);
            if (minecraft != null) {
                minecraft.setScreen(null);
            }
        } else {
            int code = MastermindCardItem.makeCode(input, difficulty);
            PacketDistributor.sendToServer(new MastermindAddAttempt(code), new CustomPacketPayload[0]);
            attempts.add(code);
            card.set(ESDataComponents.ATTEMPTS, attempts);
        }
    }

    private void successAttempt() {
        PacketDistributor.sendToServer(new MastermindDestroy(true), new CustomPacketPayload[0]);
        if (minecraft != null) {
            minecraft.setScreen(null);
        }
    }

    @Override
    public void tick() {
        // For some reason, the player lost the card
        if ((card.isEmpty()
                || player.getItemInHand(InteractionHand.MAIN_HAND).getItem() != ESItems.MASTERMIND_CARD.get())
                && minecraft != null) {
            minecraft.setScreen(null);
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    private static Component getTitle(ItemStack stack) {
        return stack.getDisplayName();
    }
}
