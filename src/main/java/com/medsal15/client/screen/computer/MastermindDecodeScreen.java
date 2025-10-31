package com.medsal15.client.screen.computer;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.modus.MastermindCardItem;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.computer.ThemedScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;

public class MastermindDecodeScreen extends ThemedScreen {
    public static final String NAME = ExtraStuck.MODID + ".program_screen.mastermind_decode";
    public static final String DECODE = ExtraStuck.MODID + ".program_screen.mastermind_decode.decode";
    public static final String RESULT = ExtraStuck.MODID + ".program_screen.mastermind_decode.result";
    public static final String FAILURE = ExtraStuck.MODID + ".program_screen.mastermind_decode.failure";
    /** UI width - offsets */
    private static final int COMPUTER_WIDTH = 158 - 16;

    private static final ResourceLocation GRIST_TEXTURE = ExtraStuck.modid("textures/gui/mastermind.png");
    private static final int GRIST_TEXTURE_X = 196;
    private static final int GRIST_TEXTURE_Y = 0;
    private static final int GRIST_RENDER_X = 0;
    private static final int GRIST_RENDER_Y = 43;
    private static final int GRIST_WIDTH = 20;
    private static final int GRIST_HEIGHT = 20;

    private Button decode;

    private boolean attempted = false;
    private boolean success = false;
    private int[] code = new int[4];

    public MastermindDecodeScreen(ComputerBlockEntity computer) {
        super(computer, Component.translatable(NAME));
    }

    @Override
    public void init() {
        super.init();

        decode = new ExtendedButton(xOffset + SCREEN_OFFSET_X + 8, yOffset + SCREEN_OFFSET_Y + 8, COMPUTER_WIDTH, 20,
                Component.translatable(DECODE), button -> {
                    getCode();
                });

        addRenderableWidget(decode);
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        if (attempted) {
            if (!success) {
                graphics.drawWordWrap(font, Component.translatable(FAILURE), xOffset + SCREEN_OFFSET_X + 8,
                        yOffset + SCREEN_OFFSET_Y + 33, COMPUTER_WIDTH, 0xAA0000);
            } else {
                graphics.drawWordWrap(font, Component.translatable(RESULT), xOffset + SCREEN_OFFSET_X + 8,
                        yOffset + SCREEN_OFFSET_Y + 33, COMPUTER_WIDTH, 0);
                for (int i = 0; i < 4; i++) {
                    int grist = code[i];
                    int draw_x = xOffset + SCREEN_OFFSET_X + 8 + i * GRIST_WIDTH + GRIST_RENDER_X;
                    int draw_y = yOffset + SCREEN_OFFSET_Y + GRIST_RENDER_Y;
                    int from_x = GRIST_TEXTURE_X + (grist % 3) * GRIST_WIDTH;
                    int from_y = GRIST_TEXTURE_Y + (grist / 3) * GRIST_HEIGHT;

                    graphics.blit(GRIST_TEXTURE, draw_x, draw_y, from_x, from_y, GRIST_WIDTH, GRIST_HEIGHT);
                }
            }
        }
    }

    private void getCode() {
        attempted = true;
        success = false;
        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;
        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() != ESItems.MASTERMIND_CARD.get() || !stack.has(ESDataComponents.DIFFICULTY)
                || !stack.has(ESDataComponents.MASTERMIND_CODE))
            return;
        success = true;
        code = MastermindCardItem.breakCode(stack);
    }
}
