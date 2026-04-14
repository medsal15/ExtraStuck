package com.medsal15.client.screen;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.client.gui.ImageButton;
import com.medsal15.menus.CraftingModusRecipeMenu;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuNext;
import com.medsal15.network.ESPackets.CraftingModusRecipeMenuSave;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.playerStats.PlayerStatsScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class CraftingModusRecipeScreen extends AbstractContainerScreen<CraftingModusRecipeMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck
            .modid("textures/gui/crafting_modus_recipe.png");
    private static final int BACKGROUND_WIDTH = 185;
    private static final int BACKGROUND_HEIGHT = 235;

    private static final int MINI_WIDTH = 11;
    private static final int MINI_HEIGHT = 13;
    private static final int MINI_X = 234;
    private static final int MINI_Y = 0;

    public CraftingModusRecipeScreen(CraftingModusRecipeMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        imageWidth = BACKGROUND_WIDTH;
        imageHeight = BACKGROUND_HEIGHT;
        inventoryLabelY = imageHeight - 97;
    }

    @Override
    protected void init() {
        super.init();

        addRenderableWidget(
                new ImageButton(leftPos + 100, topPos + 94, MINI_WIDTH, MINI_HEIGHT, b -> {
                    PacketDistributor.sendToServer(CraftingModusRecipeMenuSave.INSTANCE, new CustomPacketPayload[0]);
                }, BACKGROUND_TEXTURE, MINI_X, MINI_Y));
        addRenderableWidget(
                new ImageButton(leftPos + 112, topPos + 94, MINI_WIDTH, MINI_HEIGHT, b -> {
                    PlayerStatsScreen.openGui(true);
                }, BACKGROUND_TEXTURE, MINI_X, MINI_Y + MINI_HEIGHT));
        addRenderableWidget(
                new ImageButton(leftPos + 106, topPos + 41, MINI_WIDTH, MINI_HEIGHT, b -> {
                    PacketDistributor.sendToServer(CraftingModusRecipeMenuNext.INSTANCE, new CustomPacketPayload[0]);
                }, BACKGROUND_TEXTURE, MINI_X + MINI_WIDTH, MINI_Y));
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        if (menu.recipeCount() > 1) {
            String text = String.format("%s / %s", menu.recipeIndex() + 1, menu.recipeCount());
            int width = font.width(text);
            int left = leftPos + 111 - width / 2;
            guiGraphics.drawString(font, text, left, topPos + 55, 0x404040, false);
        }

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }
}
