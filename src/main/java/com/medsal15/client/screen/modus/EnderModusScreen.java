package com.medsal15.client.screen.modus;

import com.mraof.minestuck.inventory.captchalogue.Modus;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class EnderModusScreen extends BaseModusScreen {
    public EnderModusScreen(Modus modus) {
        super(modus);
        textureIndex = 5;
    }

    @Override
    public void updateContent() {
        NonNullList<ItemStack> stacks = modus.getItems();

        this.cards.clear();

        int width = 9, height = 3;

        this.maxWidth = Math.max(mapWidth, 10 + (width * CARD_WIDTH + (width - 1) * 5));
        this.maxHeight = Math.max(mapHeight, 10 + (width * CARD_HEIGHT + (height - 1) * 5));

        int startx = Math.max(5, (mapWidth - (width * CARD_WIDTH + (width - 1) * 5)) / 2);
        int starty = (mapHeight - ((height - 2) * (CARD_HEIGHT + 5))) / 2;

        for (int i = 0; i < stacks.size(); i++) {
            this.cards.add(new GuiCard(stacks.get(i), this, i,
                    startx + i % width * (CARD_WIDTH + 5),
                    starty + (i / width) * (CARD_HEIGHT + 5)));
        }

        super.updateContent();
    }

    @Override
    public void updatePosition() {
        int width = 9, height = 3;

        this.maxWidth = Math.max(mapWidth, 10 + (width * CARD_WIDTH + (width - 1) * 5));
        this.maxHeight = Math.max(mapHeight, 10 + (width * CARD_HEIGHT + (height - 1) * 5));

        int startx = Math.max(5, (mapWidth - (width * CARD_WIDTH + (width - 1) * 5)) / 2);
        int starty = (mapHeight - ((height - 2) * (CARD_HEIGHT + 5))) / 2;

        for (int i = 0; i < cards.size(); i++) {
            GuiCard card = cards.get(i);
            card.xPos = startx + i % width * (CARD_WIDTH + 5);
            card.yPos = starty + (i / width) * (CARD_HEIGHT + 5);
        }
    }
}
