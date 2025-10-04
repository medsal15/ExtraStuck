package com.medsal15.client.screen.computer;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.config.ConfigCommon;
import com.medsal15.items.ESItems;
import com.medsal15.items.modus.MastermindCardItem;
import com.medsal15.network.ESPackets.MastermindReset;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.computer.ThemedScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

public class MastermindEncodeScreen extends ThemedScreen {
    public static final String NAME = ExtraStuck.MODID + ".program_screen.mastermind_encode";
    public static final String ENCODE = ExtraStuck.MODID + ".program_screen.mastermind_encode.encode";
    public static final String DIFFICULTY = ExtraStuck.MODID + ".program_screen.mastermind_encode.difficulty";
    public static final String RESULT = ExtraStuck.MODID + ".program_screen.mastermind_encode.result";
    public static final String FAILURE = ExtraStuck.MODID + ".program_screen.mastermind_encode.failure";
    /** UI width - offsets */
    private static final int COMPUTER_WIDTH = 158 - 16;

    private Button decrease;
    private Button increase;
    private Button recode;

    private int difficulty;
    private boolean attempted = false;
    private boolean success = false;

    public MastermindEncodeScreen(ComputerBlockEntity computer) {
        super(computer, Component.translatable(NAME));
    }

    @Override
    public void init() {
        super.init();

        difficulty = ConfigCommon.MASTERMIND_DIFFICULTY.get();

        decrease = new ExtendedButton(xOffset + SCREEN_OFFSET_X + 8, yOffset + SCREEN_OFFSET_Y + 8, 20, 20,
                Component.literal("-"), button -> {
                    if (difficulty > 1)
                        difficulty--;
                });
        increase = new ExtendedButton(xOffset + SCREEN_OFFSET_X + COMPUTER_WIDTH - 20 + 8,
                yOffset + SCREEN_OFFSET_Y + 8, 20, 20, Component.literal("+"), button -> {
                    if (difficulty < 6)
                        difficulty++;
                });
        recode = new ExtendedButton(xOffset + SCREEN_OFFSET_X + 8, yOffset + SCREEN_OFFSET_Y + 38, COMPUTER_WIDTH, 20,
                Component.translatable(ENCODE), button -> {
                    setCode();
                });

        addRenderableWidget(decrease);
        addRenderableWidget(increase);
        addRenderableWidget(recode);
    }

    @Override
    public void render(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);

        graphics.drawWordWrap(font, Component.translatable(DIFFICULTY, difficulty), xOffset + SCREEN_OFFSET_X + 33,
                yOffset + SCREEN_OFFSET_Y + 13, COMPUTER_WIDTH - 33, 0);

        if (attempted) {
            Component text = Component.empty();
            int color = 0;
            if (!success) {
                text = Component.translatable(FAILURE);
                color = 11141120;
            } else {
                text = Component.translatable(RESULT);
            }
            graphics.drawWordWrap(font, text, xOffset + SCREEN_OFFSET_X + 8,
                    yOffset + SCREEN_OFFSET_Y + 68, COMPUTER_WIDTH, color);
        }
    }

    private void setCode() {
        attempted = true;
        success = false;

        Player player = Minecraft.getInstance().player;
        if (player == null)
            return;

        ItemStack stack = player.getItemInHand(InteractionHand.MAIN_HAND);
        if (stack.getItem() != ESItems.MASTERMIND_CARD.get())
            return;

        success = true;
        int code = MastermindCardItem.generateCode(difficulty, player.getRandom());
        PacketDistributor.sendToServer(new MastermindReset(code, difficulty), new CustomPacketPayload[0]);
    }
}
