package com.medsal15.client.screen;

import java.text.NumberFormat;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.client.gui.SwitchButton;
import com.medsal15.data.ESLangProvider;
import com.medsal15.menus.ChargerMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MachineScreen;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class ChargerScreen extends MachineScreen<ChargerMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/charger.png");
    private static final ResourceLocation CHARGE_BAR_TEXTURE = ExtraStuck.modid("textures/gui/progress/charger.png");
    private static final ResourceLocation FUEL_BAR_TEXTURE = Minestuck.id("textures/gui/progress/uranium_level.png");
    private static final int CHARGE_BAR_X = 17;
    private static final int CHARGE_BAR_Y = 20;
    private static final int CHARGE_BAR_WIDTH = 30;
    private static final int CHARGE_BAR_HEIGHT = 46;
    private static final int FUEL_BAR_X = 128;
    private static final int FUEL_BAR_Y = 24;
    private static final int FUEL_BAR_WIDTH = 35;
    private static final int FUEL_BAR_HEIGHT = 39;
    private static final int BUTTON_X = 50;
    private static final int BUTTON_Y = 25;

    public ChargerScreen(ChargerMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    protected void init() {
        super.init();

        goButton = addRenderableWidget(new SwitchButton(leftPos + BUTTON_X, topPos + BUTTON_Y, 24, 12, menu));
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int x = mouseX - leftPos;
        int y = mouseY - topPos;
        if (x >= CHARGE_BAR_X && x <= CHARGE_BAR_X + CHARGE_BAR_WIDTH && y >= CHARGE_BAR_Y
                && y <= CHARGE_BAR_Y + CHARGE_BAR_HEIGHT) {
            Component tooltip = Component.translatable(ESLangProvider.ENERGY_STORAGE_KEY,
                    NumberFormat.getInstance().format(menu.getCharge()),
                    NumberFormat.getInstance().format(ChargerBlockEntity.MAX_CHARGE));
            guiGraphics.renderTooltip(font, tooltip, mouseX, mouseY);
        }
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int charge_fill = getScaledValue(menu.getCharge(), ChargerBlockEntity.MAX_CHARGE, CHARGE_BAR_HEIGHT);
        guiGraphics.blit(CHARGE_BAR_TEXTURE, leftPos + CHARGE_BAR_X,
                topPos + CHARGE_BAR_Y + CHARGE_BAR_HEIGHT - charge_fill, 0,
                CHARGE_BAR_HEIGHT - charge_fill, CHARGE_BAR_WIDTH, charge_fill, CHARGE_BAR_WIDTH, CHARGE_BAR_HEIGHT);

        int fuel_fill = getScaledValue(menu.getFuel(), ChargerBlockEntity.MAX_FUEL, FUEL_BAR_HEIGHT);
        guiGraphics.blit(FUEL_BAR_TEXTURE, leftPos + FUEL_BAR_X, topPos + FUEL_BAR_Y + FUEL_BAR_HEIGHT - fuel_fill, 0,
                FUEL_BAR_HEIGHT - fuel_fill, FUEL_BAR_WIDTH, fuel_fill, FUEL_BAR_WIDTH, FUEL_BAR_HEIGHT);
    }
}
