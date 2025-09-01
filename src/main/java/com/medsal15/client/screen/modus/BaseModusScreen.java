package com.medsal15.client.screen.modus;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;

import net.minecraft.resources.ResourceLocation;

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
}
