package com.medsal15.client.screen.modus;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.data.ESLangProvider;
import com.medsal15.modus.CompactModus;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;
import com.mraof.minestuck.network.CaptchaDeckPackets;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

public class CompactModusScreen extends BaseModusScreen {
    private CompactModus modus;

    protected Button modeButton;

    public CompactModusScreen(int windowId, Inventory inventory, Modus modus) {
        super(windowId, inventory, modus);
        this.modus = (CompactModus) modus;
        textureIndex = 9;
    }

    @Override
    public void init() {
        super.init();

        modeButton = new ExtendedButton(xOffset + BUTTON_X_OFFSET, yOffset + BUTTON_Y_OFFSET + BUTTON_HEIGHT * 2 + 6,
                BUTTON_WIDTH, BUTTON_HEIGHT, Component.empty(), button -> changeSetting());
        addRenderableWidget(modeButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        modeButton.setMessage(Component.translatable(
                modus.isStrict() ? ESLangProvider.COMPACT_MODUS_STRICT_ON : ESLangProvider.COMPACT_MODUS_STRICT_OFF));

        super.render(guiGraphics, mouseX, mouseY, partialTick);

        /*
         * if (isMouseInContainer(mouseX, mouseY)) {
         * ArrayList<GuiCard> visibleCards = new ArrayList<>();
         * for (GuiCard card : cards)
         * if (card.xPos + CARD_WIDTH > mapX && card.xPos < mapX + mapWidth
         * && card.yPos + CARD_HEIGHT > mapY && card.yPos < mapY + mapHeight)
         * visibleCards.add(card);
         *
         * int xOffset = (width - GUI_WIDTH) / 2;
         * int yOffset = (height - GUI_HEIGHT) / 2;
         *
         * int translX = (int) ((mouseX - xOffset - X_OFFSET) * scroll);
         * int translY = (int) ((mouseY - yOffset - Y_OFFSET) * scroll);
         *
         * // Draw topmost card's tooltip
         * for (GuiCard card : visibleCards)
         * if (translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
         * translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY &&
         * card instanceof PileCard pileCard && !pileCard.background) {
         * pileCard.realDrawTooltip(guiGraphics, mouseX, mouseY);
         * break;
         * }
         * }
         */
    }

    @Override
    @Nullable
    protected GuiCard getCardAt(double xcor, double ycor, @Nonnull List<GuiCard> visibleCards) {
        if (isMouseInContainer(xcor, ycor)) {
            int translX = (int) ((xcor - xOffset - X_OFFSET) * scroll);
            int translY = (int) ((ycor - yOffset - Y_OFFSET) * scroll);

            for (GuiCard card : visibleCards)
                if (translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
                        translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY &&
                        card instanceof PileCard pileCard && !pileCard.background) {
                    return pileCard;
                }
        }
        return super.getCardAt(xcor, ycor, visibleCards);
    }

    @Override
    public void updateContent() {
        NonNullList<ItemStack> stacks = modus.getItems();
        List<List<Integer>> groups = modus.getGroups();

        cards.clear();
        int columns = (groups.size() + 1) / 3;
        maxWidth = Math.max(mapWidth, 10 + (columns * CARD_WIDTH + (columns - 1) * 5));
        maxHeight = mapHeight;

        int start = Math.max(5 + CARD_WIDTH * 2, (mapWidth - (columns * CARD_WIDTH + (columns - 1) * 5)) / 3);
        for (int i = 0; i < groups.size(); i++) {
            var group = groups.get(i);
            for (int j = 0; j < group.size(); j++) {
                cards.addFirst(new PileCard(
                        stacks.get(group.get(j)),
                        this,
                        group.get(j),
                        start + i / 3 * (CARD_WIDTH + 5) - j,
                        (mapHeight - 3 * CARD_HEIGHT - 5) / 3 + (i % 3) * (CARD_HEIGHT + 5) + 15 - j,
                        j != 0,
                        i));
            }
        }

        if (modus.getSize() > modus.groupedCount()) {
            cards.add(new ModusSizeCard(this, modus.getSize() - modus.groupedCount(), 10, 10));
        }

        super.updateContent();
    }

    @Override
    public void updatePosition() {
        updateContent();
    }

    private void changeSetting() {
        modus.switchStrictness();
        PacketDistributor.sendToServer(
                new CaptchaDeckPackets.SetModusParameter(CompactModus.STRICTNESS_MODE, modus.isStrict() ? 1 : 0),
                new CustomPacketPayload[0]);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scrollX, double scrollY) {
        if (isMouseInContainer(mouseX, mouseY) && scrollY != 0) {
            int translX = (int) ((mouseX - xOffset - X_OFFSET) * scroll);
            int translY = (int) ((mouseY - yOffset - Y_OFFSET) * scroll);

            for (GuiCard gcard : cards) {
                // Check for mouse to be in a card
                if (translX < gcard.xPos + 2 - mapX || translX >= gcard.xPos + 18 - mapX ||
                        translY < gcard.yPos + 7 - mapY || translY >= gcard.yPos + 23 - mapY)
                    continue;
                // Check for PileCard
                if (!(gcard instanceof PileCard card))
                    continue;

                PacketDistributor.sendToServer(
                        new CaptchaDeckPackets.SetModusParameter(
                                scrollY > 0 ? CompactModus.SHIFT_INDEX_UP : CompactModus.SHIFT_INDEX_DOWN, card.pile),
                        new CustomPacketPayload[0]);
                return true;
            }
        }

        return super.mouseScrolled(mouseX, mouseY, scrollX, scrollY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        if (isMouseInContainer(mouseX, mouseY)) {
            int translX = (int) ((mouseX - xOffset - X_OFFSET) * scroll);
            int translY = (int) ((mouseY - yOffset - Y_OFFSET) * scroll);
            for (GuiCard card : this.cards) {
                if (card instanceof PileCard pileCard && pileCard.background)
                    continue;
                if (translX >= card.xPos + 2 - mapX && translX < card.xPos + 18 - mapX &&
                        translY >= card.yPos + 7 - mapY && translY < card.yPos + 23 - mapY) {
                    card.onClick(mouseButton);
                    return true;
                }
            }
        }

        return super.mouseClicked(mouseX, mouseY, mouseButton);

    }

    @Override
    public int getCardTextureX(@Nonnull GuiCard card) {
        if (card instanceof PileCard pileCard && pileCard.background)
            return ((textureIndex + 1) % 12) * CARD_WIDTH;
        return super.getCardTextureX(card);
    }

    @Override
    public int getCardTextureY(@Nonnull GuiCard card) {
        if (card instanceof PileCard pileCard && pileCard.background)
            return ((int) Math.floor((textureIndex + 1) / 12)) * CARD_HEIGHT;
        return super.getCardTextureY(card);
    }

    public class PileCard extends GuiCard {
        private final boolean background;
        private final int pile;

        public PileCard(ItemStack item, SylladexScreen gui, int index, int x, int y, boolean background, int pile) {
            super(item, gui, index, x, y);
            this.background = background;
            this.pile = pile;
        }

        @Override
        public void drawItem(@Nonnull GuiGraphics graphics) {
            if (!background)
                super.drawItem(graphics);
        }
    }
}
