package com.medsal15.compat.create.menus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.components.GristFilterEntry.ComparingMode;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.simibubi.create.content.logistics.filter.AbstractFilterScreen;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class GristFilterScreen extends AbstractFilterScreen<GristFilterMenu> {
    public static final String ADD_DESC = "gui." + ExtraStuck.MODID + ".grist_filter.add";
    public static final String GRIST_TYPE = "gui." + ExtraStuck.MODID + ".grist_filter.grist_type";
    public static final String MODE = "gui." + ExtraStuck.MODID + ".grist_filter.mode";
    public static final String LIST_EMPTY = "gui." + ExtraStuck.MODID + ".grist_filter.list_empty";
    public static final String LIST_SELECTED = "gui." + ExtraStuck.MODID + ".grist_filter.list_selected";

    private final List<GristType> grists;
    private final ComparingMode[] modes = ComparingMode.values();
    private final List<Component> selectedGrists = new ArrayList<>();

    private IconButton add;
    private Label gristLabel;
    private SelectionScrollInput gristSelector;
    private Label modeLabel;
    private SelectionScrollInput modeSelector;
    private EditBox amountInput;

    public GristFilterScreen(GristFilterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title, AllGuiTextures.ATTRIBUTE_FILTER);

        grists = GristTypes.REGISTRY.stream().toList();
    }

    public AbstractContainerScreen<? extends AbstractContainerMenu> wrap() {
        return this;
    }

    @Override
    protected void init() {
        super.init();

        int x = leftPos;
        int y = topPos;

        addRenderableWidget(add = new IconButton(x + 200, y + 26, AllIcons.I_ADD));
        add.withCallback(() -> {
            handleAddFilter();
        });
        add.setToolTip(Component.translatable(ADD_DESC));
        add.active = true;

        handleIndicators();

        gristLabel = new Label(x + 43, y + 31, Component.translatable(GRIST_TYPE)).colored(0xDEE6F3).withShadow();
        gristSelector = new SelectionScrollInput(x + 39, y + 26, 76, 18);
        gristSelector.forOptions(
                grists.stream().<Component>map(type -> Component.translatable(type.getTranslationKey())).toList());
        gristSelector.removeCallback();

        modeLabel = new Label(x + 125, y + 31, Component.translatable(MODE)).colored(0xDEE6F3).withShadow();
        modeSelector = new SelectionScrollInput(x + 121, y + 26, 18, 18);
        modeSelector.forOptions(
                Arrays.stream(modes).<Component>map(mode -> Component.translatable(mode.symbol())).toList());
        modeSelector.removeCallback();

        addRenderableWidget(gristLabel);
        addRenderableWidget(gristSelector);
        addRenderableWidget(modeLabel);
        addRenderableWidget(modeSelector);

        amountInput = new EditBox(font, x + 150, y + 31, 39, 9, Component.empty());
        amountInput.setValue("0");
        amountInput.setFilter(str -> {
            try {
                Integer.parseInt(str);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        });
        addRenderableOnly(amountInput);

        selectedGrists.clear();
        selectedGrists.add(Component.translatable(menu.entries.isEmpty() ? LIST_EMPTY : LIST_SELECTED));
    }

    @Override
    protected boolean isButtonEnabled(IconButton button) {
        return true;
    }

    @Override
    protected void contentsCleared() {
        selectedGrists.clear();
        selectedGrists.add(Component.translatable(LIST_EMPTY));
    }

    @Override
    protected void renderForeground(@Nonnull GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        ItemStack stack = menu.ghostInventory.getStackInSlot(0);
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate(0, 0, 150);
        graphics.renderItemDecorations(font, stack, leftPos + 16, topPos + 62,
                String.valueOf(selectedGrists.size() - 1));
        matrixStack.popPose();

        super.renderForeground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics graphics, int mouseX, int mouseY) {
        Slot hovered = hoveredSlot;
        if (this.menu.getCarried().isEmpty() && hovered != null && hovered.hasItem()) {
            if (hovered.index == 37) {
                graphics.renderComponentTooltip(font, selectedGrists, mouseX, mouseY);
                return;
            }
            graphics.renderTooltip(font, hovered.getItem(), mouseX, mouseY);
        }
        super.renderTooltip(graphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTicks, int mouseX, int mouseY) {
        // TODO override background image
    }

    private void handleAddFilter() {
        int gristIndex = gristSelector.getState();
        if (gristIndex >= grists.size())
            return;

        int modeIndex = modeSelector.getState();
        if (modeIndex >= modes.length)
            return;

        GristType grist = grists.get(gristIndex);
        ComparingMode mode = modes[modeIndex];
        int amount = Integer.parseInt(amountInput.getValue());

        ExtraStuck.LOGGER.info("grist {} mode {} amount {}", grist, mode, amount);

        // TODO actually add
    }
}
