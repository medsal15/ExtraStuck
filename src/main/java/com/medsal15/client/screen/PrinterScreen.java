package com.medsal15.client.screen;

import java.util.Objects;
import java.util.Optional;

import javax.annotation.Nonnull;

import org.lwjgl.glfw.GLFW;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.client.gui.LoopButton;
import com.medsal15.menus.PrinterMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.client.gui.GristSelectorScreen;
import com.mraof.minestuck.client.gui.MachineScreen;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class PrinterScreen extends MachineScreen<PrinterMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/printer.png");
    private static final ResourceLocation PROGRESS_BAR_TEXTURE = ExtraStuck.modid("textures/gui/printer_progress.png");
    private static final ResourceLocation FUEL_BAR_TEXTURE = Minestuck.id("textures/gui/progress/uranium_level.png");
    private static final int PROGRESS_BAR_X = 37;
    private static final int PROGRESS_BAR_Y = 18;
    private static final int PROGRESS_BAR_WIDTH = 36;
    private static final int PROGRESS_BAR_HEIGHT = 10;
    private static final int FUEL_BAR_X = 104;
    private static final int FUEL_BAR_Y = 4;
    private static final int FUEL_BAR_WIDTH = 35;
    private static final int FUEL_BAR_HEIGHT = 39;
    private static final int BUTTON_X = 37;
    private static final int BUTTON_Y = 31;

    public PrinterScreen(PrinterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
        // Move label to not be on top of dowel slot
        titleLabelX = 37;
    }

    // AbstractContainerScreen
    @Override
    protected void init() {
        super.init();

        goButton = addRenderableWidget(new LoopButton(leftPos + BUTTON_X, topPos + BUTTON_Y, 36, 12, menu));
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderLabels(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        Minecraft mc = minecraft;
        Objects.requireNonNull(mc);
        super.renderLabels(guiGraphics, mouseX, mouseY);

        if (menu.getSlot(0).hasItem()) {
            Level l = mc.level;
            Objects.requireNonNull(l);
            ItemStack stack = EncodedItemComponent.getEncodedOrBlank(menu.getSlot(0).getItem());

            Optional<GristCostRecipe> recipe = GristCostRecipe.findRecipeForItem(stack, l);
            GristSet cost = recipe.map(rec -> rec.getGristCost(stack, menu.getWildcardType(), false)).orElse(null);
            boolean useWildcard = recipe.map(GristCostRecipe::canPickWildcard).orElse(false);

            GuiUtil.drawGristBoard(guiGraphics, cost,
                    useWildcard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER, 9, 45,
                    font);

            Component tooltip = GuiUtil.getGristboardTooltip(cost,
                    useWildcard ? GuiUtil.GristboardMode.ALCHEMITER_SELECT : GuiUtil.GristboardMode.ALCHEMITER,
                    mouseX - leftPos, mouseY - topPos, 9, 45, font);
            if (tooltip != null) {
                guiGraphics.renderTooltip(font, tooltip, mouseX - leftPos, mouseY - topPos);
            }
        }
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int bar_progress = getScaledValue(menu.getProgress(), PrinterBlockEntity.MAX_PROGRESS, PROGRESS_BAR_WIDTH);
        guiGraphics.blit(PROGRESS_BAR_TEXTURE, leftPos + PROGRESS_BAR_X, topPos + PROGRESS_BAR_Y,
                0, 0, bar_progress, PROGRESS_BAR_HEIGHT, PROGRESS_BAR_WIDTH, PROGRESS_BAR_HEIGHT);

        int fuel_fill = getScaledValue(menu.getFuel(), PrinterBlockEntity.MAX_FUEL, FUEL_BAR_HEIGHT);
        guiGraphics.blit(FUEL_BAR_TEXTURE, leftPos + FUEL_BAR_X, topPos + FUEL_BAR_Y + FUEL_BAR_HEIGHT - fuel_fill, 0,
                FUEL_BAR_HEIGHT - fuel_fill, FUEL_BAR_WIDTH, fuel_fill, FUEL_BAR_WIDTH, FUEL_BAR_HEIGHT);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        Minecraft mc = minecraft;
        Objects.requireNonNull(mc);

        boolean b = super.mouseClicked(mouseX, mouseY, button);
        if (button == GLFW.GLFW_MOUSE_BUTTON_1 && menu.getCarried().isEmpty()
                && mouseX >= leftPos + 9 && mouseX < leftPos + 167
                && mouseY >= topPos + 45 && mouseY < topPos + 70) {
            EncodedItemComponent encoded = menu.getSlot(0).getItem().get(MSItemComponents.ENCODED_ITEM);
            if (encoded != null && encoded.item() == MSItems.CAPTCHA_CARD.get()) {
                mc.pushGuiLayer(new GristSelectorScreen(this.getMenu().machinePos));
                return true;
            }
        }

        return b;
    }
}
