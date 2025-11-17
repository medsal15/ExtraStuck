package com.medsal15.client.screen.modus;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;

import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public abstract class BaseModusScreen extends SylladexScreen {
    protected static final ResourceLocation CARDS_TEXTURES = ExtraStuck.modid("textures/gui/cards.png");

    protected Modus modus;

    public BaseModusScreen(Modus modus) {
        this.modus = modus;
    }

    @Override
    public ResourceLocation getCardTexture(@Nonnull GuiCard card) {
        return CARDS_TEXTURES;
    }

    @Override
    public int getCardTextureX(@Nonnull GuiCard card) {
        return (textureIndex % 12) * CARD_WIDTH;
    }

    @Override
    public int getCardTextureY(@Nonnull GuiCard card) {
        return ((int) Math.floor(textureIndex / 12)) * CARD_HEIGHT;
    }

    protected void squareContents(int width) {
        NonNullList<ItemStack> stacks = modus.getItems();

        this.cards.clear();

        this.maxWidth = Math.max(mapWidth, 10 + (width * CARD_WIDTH + (width - 1) * 5));
        this.maxHeight = Math.max(mapHeight, 10 + (width * CARD_HEIGHT + (width - 1) * 5));

        if (width > 0) {
            int startx = Math.max(5, (mapWidth - (width * CARD_WIDTH + (width - 1) * 5)) / 2),
                    starty = (mapHeight + ((width - 2) * (CARD_HEIGHT + 5))) / 2 + 15;
            for (int i = 0; i < stacks.size(); i++) {
                this.cards.add(new GuiCard(stacks.get(i), this, i,
                        startx + i % width * (CARD_WIDTH + 5),
                        starty - (i / width) * (CARD_HEIGHT + 5)));
            }
        }
    }
}
