package com.medsal15.client.screen.modus;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.medsal15.modus.CraftingModus;
import com.medsal15.modus.CraftingModus.CraftingModusRecipe;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuOpen;
import com.mraof.minestuck.client.gui.captchalouge.SylladexScreen;
import com.mraof.minestuck.inventory.captchalogue.Modus;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.core.NonNullList;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.network.protocol.game.ServerboundContainerClosePacket;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingInput;
import net.neoforged.neoforge.client.gui.widget.ExtendedButton;
import net.neoforged.neoforge.network.PacketDistributor;

public class CraftingModusScreen extends BaseModusScreen {
    private final CraftingModus modus;

    protected Button addRecipeButton;

    public CraftingModusScreen(int windowId, Inventory inventory, Modus modus) {
        super(windowId, inventory, modus);
        this.modus = (CraftingModus) modus;
        textureIndex = 11;
    }

    @SuppressWarnings("null")
    @Override
    public void init() {
        super.init();

        addRecipeButton = new ExtendedButton(xOffset + BUTTON_X_OFFSET,
                yOffset + BUTTON_Y_OFFSET + BUTTON_HEIGHT * 2 + 6,
                BUTTON_WIDTH, BUTTON_HEIGHT, Component.translatable(ESLangProvider.CRAFTING_MODUS_ADD_RECIPE),
                button -> {
                    minecraft.player.connection
                            .send(new ServerboundContainerClosePacket(minecraft.player.containerMenu.containerId));
                    PacketDistributor.sendToServer(CraftingModusRecipeMenuOpen.INSTANCE, new CustomPacketPayload[0]);
                });
        addRenderableWidget(addRecipeButton);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        addRecipeButton.active = modus.remainingCards() >= 10;

        super.render(guiGraphics, mouseX, mouseY, partialTick);
    }

    @Override
    public void updateContent() {
        NonNullList<CraftingModusRecipe> recipes = modus.getRecipes();
        this.maxWidth = Math.max(mapWidth, 10 + (recipes.size() * CARD_WIDTH * 5 + (recipes.size() - 1) * 5));
        this.maxHeight = mapHeight;

        cards.clear();
        int start = Math.max(5, (mapWidth - (recipes.size() * CARD_WIDTH * 5 + (recipes.size() - 1) * 5)) / 2);
        for (int i = 0; i < recipes.size(); i++) {
            int x = start + i * 5 * CARD_WIDTH + 5;
            int y = (mapHeight - CARD_HEIGHT) / 3;
            cards.addAll(
                    getRecipeCards(recipes.get(i), i, x, y));
        }

        if (modus.remainingCards() > 0) {
            cards.add(new ModusSizeCard(this, modus.remainingCards(), 10, 10));
        }
    }

    @Override
    public void updatePosition() {
        updateContent();
    }

    protected List<GuiCard> getRecipeCards(CraftingModusRecipe recipe, int index, int x, int y) {
        List<GuiCard> cards = new ArrayList<>();

        CraftingInput ingredients = recipe.ingredients;
        for (int row = 0; row < 3; row++)
            for (int col = 0; col < 3; col++) {
                int xPos = x + row * CARD_WIDTH;
                int yPos = y + col * CARD_HEIGHT;
                int i = row + col * 3;
                ItemStack item = ItemStack.EMPTY;
                if (row < ingredients.width() && col < ingredients.height())
                    item = ingredients.getItem(row, col);
                cards.add(new CraftingCard(item, this, index, xPos, yPos, i));
            }

        cards.add(new GuiCard(recipe.result, this, index, x + 3 * CARD_WIDTH, y + CARD_HEIGHT));

        return cards;
    }

    @Override
    public int getCardTextureX(@Nonnull GuiCard card) {
        if (card instanceof CraftingCard craftingCard) {
            return craftingCard.position % 3 * CARD_WIDTH;
        }
        if (card instanceof ModusSizeCard) {
            return 3 * CARD_WIDTH;
        }
        return super.getCardTextureX(card);
    }

    @Override
    public int getCardTextureY(@Nonnull GuiCard card) {
        if (card instanceof CraftingCard craftingCard) {
            return (Math.floorDiv(craftingCard.position, 3) + 1) * CARD_HEIGHT;
        }
        if (card instanceof ModusSizeCard) {
            return 1 * CARD_HEIGHT;
        }
        return super.getCardTextureY(card);
    }

    public class CraftingCard extends GuiCard {
        /** 0-8 for crafting grid */
        private final int position;

        public CraftingCard(ItemStack item, SylladexScreen gui, int index, int x, int y, int position) {
            super(item, gui, index, x, y);

            this.position = position;
        }
    }
}
