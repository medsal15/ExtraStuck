package com.medsal15.client.screen.modus;

import com.medsal15.modus.PileModus;
import com.mraof.minestuck.inventory.captchalogue.Modus;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;

public class PileModusScreen extends BaseModusScreen {
    protected PileModus modus;

    public PileModusScreen(Modus modus) {
        super(modus);
        this.modus = (PileModus) modus;
        textureIndex = 0;
    }

    @Override
    public void updateContent() {
        NonNullList<ItemStack> stacks = modus.getItems();
        int width = modus.getWidth();

        this.cards.clear();

        if (width > 0) {
            this.maxWidth = Math.max(mapWidth, 10 + (width * CARD_WIDTH + (width - 1) * 5));
            this.maxHeight = Math.max(mapHeight, 10 + (width * CARD_HEIGHT + (width - 1) * 5));

            int startx = Math.max(5, (mapWidth - (width * CARD_WIDTH + (width - 1) * 5)) / 2),
                    starty = (mapHeight + ((width - 2) * (CARD_HEIGHT + 5))) / 2;
            for (int i = 0; i < stacks.size(); i++) {
                this.cards.add(new GuiCard(stacks.get(i), this, i,
                        startx + i % width * (CARD_WIDTH + 5),
                        starty - (i / width) * (CARD_HEIGHT + 5)));
            }
        }

        if (modus.getSize() > stacks.size()) {
            cards.add(new ModusSizeCard(this, modus.getSize() - stacks.size(), 10, 10));
        }

        super.updateContent();
    }

    @Override
    public void updatePosition() {
        if (this.maxWidth != Math.max(mapWidth, 10 + (width * CARD_WIDTH + (width - 1) * 5)) ||
                this.maxHeight != Math.max(mapHeight, 10 + (width * CARD_HEIGHT + (width - 1) * 5)))
            updateContent();
    }
}
