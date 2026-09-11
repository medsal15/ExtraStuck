package com.medsal15.client.screen.modus;

import com.medsal15.config.ConfigServer;
import com.medsal15.data.ESLangProvider;
import com.medsal15.modus.MastermindModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.CaptchaDeckPackets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

public class MastermindModusScreen extends BaseModusScreen {
    protected Button plus, minus, set;
    protected final MastermindModus modus;
    int difficulty;
    protected boolean couldChange = false;

    public MastermindModusScreen(int windowId, Inventory inventory, Modus modus) {
        super(windowId, inventory, modus);
        textureIndex = 6;
        this.modus = (MastermindModus) modus;
        difficulty = this.modus.getDifficulty();
    }

    @Override
    public void init() {
        super.init();

        couldChange = ConfigServer.MASTERMIND_CHANGE_SYLLADEX.getAsBoolean();

        plus = new ExtendedButton(xOffset + BUTTON_X_OFFSET, yOffset + BUTTON_Y_OFFSET + BUTTON_HEIGHT * 3 + 4,
                BUTTON_WIDTH / 6, BUTTON_HEIGHT, Component.literal("+"),
                button -> alterDifficulty(difficulty + 1));
        set = new ExtendedButton(xOffset + BUTTON_X_OFFSET + BUTTON_WIDTH / 6,
                yOffset + BUTTON_Y_OFFSET + BUTTON_HEIGHT * 3 + 4, BUTTON_WIDTH * 2 / 3, BUTTON_HEIGHT,
                Component.empty(), button -> updateDifficulty());
        minus = new ExtendedButton(xOffset + BUTTON_X_OFFSET + BUTTON_WIDTH * 5 / 6,
                yOffset + BUTTON_Y_OFFSET + BUTTON_HEIGHT * 3 + 4, BUTTON_WIDTH / 6, BUTTON_HEIGHT,
                Component.literal("-"), button -> alterDifficulty(difficulty - 1));

        set.setMessage(Component.translatable(ESLangProvider.MASTERMIND_DIFFICULTY_SET, difficulty));
        plus.visible = couldChange;
        set.visible = couldChange;
        minus.visible = couldChange;

        addRenderableWidget(plus);
        addRenderableWidget(set);
        addRenderableWidget(minus);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int xcor, int ycor, float f) {
        super.render(guiGraphics, xcor, ycor, f);

        guiGraphics.drawCenteredString(font,
                Component.translatable(ESLangProvider.MASTERMIND_DIFFICULTY, modus.getDifficulty()),
                xOffset + BUTTON_X_OFFSET + BUTTON_WIDTH / 2, yOffset + BUTTON_Y_OFFSET + BUTTON_HEIGHT * 2 + 8,
                0xFFFFFF);

        boolean canChange = ConfigServer.MASTERMIND_CHANGE_SYLLADEX.getAsBoolean();
        if (canChange != couldChange) {
            plus.visible = canChange;
            set.visible = canChange;
            minus.visible = canChange;

            couldChange = canChange;
        }
    }

    @Override
    public void updateContent() {
        NonNullList<ItemStack> stacks = modus.getItems();
        this.cards.clear();
        this.maxWidth = Math.max(mapWidth, 10 + (stacks.size() * CARD_WIDTH + (stacks.size() - 1) * 5));
        this.maxHeight = mapHeight;
        super.updateContent();
        int start = Math.max(5, (mapWidth - (stacks.size() * CARD_WIDTH + (stacks.size() - 1) * 5)) / 2);

        for (int i = 0; i < stacks.size(); i++)
            this.cards.add(
                    new GuiCard(stacks.get(i), this, i, start + i * (CARD_WIDTH + 5), (mapHeight - CARD_HEIGHT) / 2));
    }

    @Override
    public void updatePosition() {
        this.maxWidth = Math.max(mapWidth, 10 + (cards.size() * CARD_WIDTH + (cards.size() - 1) * 5));
        this.maxHeight = mapHeight;
        int start = Math.max(5, (mapWidth - (cards.size() * CARD_WIDTH + (cards.size() - 1) * 5)) / 2);
        for (int i = 0; i < cards.size(); i++) {
            GuiCard card = cards.get(i);
            card.xPos = start + i * (CARD_WIDTH + 5);
            card.yPos = (mapHeight - CARD_HEIGHT) / 2;
        }
    }

    protected void alterDifficulty(int difficulty) {
        this.difficulty = Math.clamp(difficulty, 1, 6);
        set.setMessage(Component.translatable(ESLangProvider.MASTERMIND_DIFFICULTY_SET, this.difficulty));
    }

    protected void updateDifficulty() {
        modus.setDifficulty(difficulty);
        PacketDistributor
                .sendToServer(new CaptchaDeckPackets.SetModusParameter(MastermindModus.DIFFICULTY, difficulty));
    }
}
