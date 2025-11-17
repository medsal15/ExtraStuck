package com.medsal15.client.screen.modus;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.medsal15.modus.FurnaceModus;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class FurnaceModusScreen extends BaseModusScreen {
    protected FurnaceModus modus;

    public FurnaceModusScreen(Modus modus) {
        super(modus);
        this.modus = (FurnaceModus) modus;
        textureIndex = 7;
    }

    @Override
    public void updateContent() {
        squareContents(modus.getWidth());

        cards.add(new FuelCard(this, modus.getFuel(), 10, 10));

        super.updateContent();
    }

    @Override
    public void updatePosition() {
        if (this.maxWidth != Math.max(mapWidth, 10 + (width * CARD_WIDTH + (width - 1) * 5)) ||
                this.maxHeight != Math.max(mapHeight, 10 + (width * CARD_HEIGHT + (width - 1) * 5)))
            updateContent();
    }

    @Override
    public int getCardTextureX(@Nonnull GuiCard card) {
        if (card instanceof FuelCard)
            return ((textureIndex + 1) % 12) * CARD_WIDTH;
        return super.getCardTextureX(card);
    }

    @Override
    public int getCardTextureY(@Nonnull GuiCard card) {
        if (card instanceof FuelCard)
            return ((int) Math.floor((textureIndex + 1) / 12)) * CARD_HEIGHT;
        return super.getCardTextureY(card);
    }

    public class FuelCard extends GuiCard {
        public int fuel;

        public FuelCard(SylladexScreen gui, int fuel, int xPos, int yPos) {
            this.gui = gui;
            this.index = -1;
            this.fuel = fuel;
            this.xPos = xPos;
            this.yPos = yPos;
        }

        @Override
        protected void drawTooltip(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
            guiGraphics.renderTooltip(font, Component.translatable(ESLangProvider.FURNACE_MODUS_FUEL, fuel), mouseX,
                    mouseY);
        }

        @Override
        protected void drawItem(@Nonnull GuiGraphics graphics) {
            String fuelAmount = String.valueOf(fuel);
            int x = this.xPos - mapX + 11 - font.width(fuelAmount);
            int y = this.yPos - mapY + 13;
            if (x >= mapWidth || y >= mapHeight || x + font.width(fuelAmount) < 0 || y + font.lineHeight < 0)
                return;

            RenderSystem.setShaderColor(1, 1, 1, 1);
            RenderSystem.disableDepthTest();
            RenderSystem.disableBlend();
            graphics.drawString(font, fuelAmount, x, y, 0xC6C6C6);
            RenderSystem.enableDepthTest();
            RenderSystem.enableBlend();
        }

        @Override
        public void onClick(int mouseButton) {
        }
    }
}
