package com.medsal15.client.screen.modus;

import com.mraof.minestuck.inventory.captchalogue.Modus;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class FortuneModusScreen extends BaseModusScreen {
    private Modus modus;

    public FortuneModusScreen(Modus modus) {
        super();
        this.modus = modus;
        textureIndex = 1;
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
}
