package com.medsal15.client.screen.machine;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.config.ConfigCommon;
import com.medsal15.menus.BlasterMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MachineScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class BlasterScreen extends MachineScreen<BlasterMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/blaster.png");
    private static final ResourceLocation FUEL_BAR_TEXTURE = Minestuck.id("textures/gui/progress/uranium_level.png");
    private static final int FUEL_BAR_X = 40;
    private static final int FUEL_BAR_Y = 23;
    private static final int FUEL_BAR_WIDTH = 35;
    private static final int FUEL_BAR_HEIGHT = 39;

    public BlasterScreen(BlasterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int fuel_fill = getScaledValue(menu.getFuel(), ConfigCommon.BLASTER_URANIUM_STORAGE.get(), FUEL_BAR_HEIGHT);
        guiGraphics.blit(FUEL_BAR_TEXTURE, leftPos + FUEL_BAR_X, topPos + FUEL_BAR_Y + FUEL_BAR_HEIGHT - fuel_fill, 0,
                FUEL_BAR_HEIGHT - fuel_fill, FUEL_BAR_WIDTH, fuel_fill, FUEL_BAR_WIDTH, FUEL_BAR_HEIGHT);
    }
}
