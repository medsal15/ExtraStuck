package com.medsal15.compat.create.client.screens;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.create.client.menus.GristFilterMenu;
import com.medsal15.compat.create.items.GristFilterItem.GristFilterEntry.ComparingMode;
import com.medsal15.compat.create.network.ESCreatePackets.AddGristFilterEntry;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.simibubi.create.foundation.gui.AllIcons;
import com.simibubi.create.foundation.gui.widget.IconButton;
import com.simibubi.create.foundation.gui.widget.Label;
import com.simibubi.create.foundation.gui.widget.SelectionScrollInput;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public class GristFilterScreen extends AbstractContainerScreen<GristFilterMenu> {
    public static final String ADD_DESC = "gui." + ExtraStuck.MODID + ".grist_filter.add";
    public static final String GRIST_TYPE = "gui." + ExtraStuck.MODID + ".grist_filter.grist_type";
    public static final String MODE = "gui." + ExtraStuck.MODID + ".grist_filter.mode";
    public static final String LIST_EMPTY = "gui." + ExtraStuck.MODID + ".grist_filter.list_empty";
    public static final String LIST_SELECTED = "gui." + ExtraStuck.MODID + ".grist_filter.list_selected";
    public static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/grist_filter.png");
    public static final int BACKGROUND_WIDTH = 241, BACKGROUND_HEIGHT = 197;
    public static final int ADD_X = 200, ADD_Y = 26;
    public static final int TRASH_X = 180, TRASH_Y = 61;
    public static final int CONFIRM_X = 208, CONFIRM_Y = 61;

    private final List<GristType> grists;
    private final ComparingMode[] modes = ComparingMode.values();
    private final List<Component> selectedGrists = new ArrayList<>();

    private IconButton add, resetButton, confirmButton;
    private Label gristLabel;
    private SelectionScrollInput gristSelector;
    private Label modeLabel;
    private SelectionScrollInput modeSelector;
    private EditBox amountInput;

    public GristFilterScreen(GristFilterMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);

        grists = GristTypes.REGISTRY.stream().toList();

        imageWidth = BACKGROUND_WIDTH;
        imageHeight = BACKGROUND_HEIGHT;
        titleLabelY = 4;
        inventoryLabelX = 65;
        inventoryLabelY = 95;
    }

    @Override
    protected void init() {
        super.init();

        titleLabelX = (BACKGROUND_WIDTH - font.width(title)) / 2;

        int x = leftPos;
        int y = topPos;

        resetButton = new IconButton(x + BACKGROUND_WIDTH - 61, y + 61, AllIcons.I_TRASH);
        resetButton.withCallback(() -> {
            menu.clearContents();
            contentsCleared();
            menu.sendClearPacket();
        });
        confirmButton = new IconButton(x + BACKGROUND_WIDTH - 33, y + 61, AllIcons.I_CONFIRM);
        confirmButton.withCallback(() -> {
            Minecraft minecraft = this.minecraft;
            if (minecraft != null && minecraft.player != null)
                minecraft.player.closeContainer();
        });

        addRenderableWidget(resetButton);
        addRenderableWidget(confirmButton);

        addRenderableWidget(add = new IconButton(x + ADD_X, y + ADD_Y, AllIcons.I_ADD));
        add.withCallback(this::handleAddFilter);
        add.setToolTip(Component.translatable(ADD_DESC));
        add.active = true;

        gristLabel = new Label(x + 20, y + 31, Component.translatable(GRIST_TYPE)).colored(0xDEE6F3).withShadow();
        gristSelector = new SelectionScrollInput(x + 20, y + 26, 100, 18);
        gristSelector.forOptions(
                grists.stream().<Component>map(type -> Component.translatable(type.getTranslationKey())).toList());
        gristSelector.removeCallback();
        gristSelector.writingTo(gristLabel);

        modeLabel = new Label(x + 125, y + 31, Component.translatable(MODE)).colored(0xDEE6F3).withShadow();
        modeSelector = new SelectionScrollInput(x + 121, y + 26, 18, 18);
        modeSelector.forOptions(
                Arrays.stream(modes).<Component>map(mode -> Component.translatable(mode.symbol())).toList());
        modeSelector.removeCallback();
        modeSelector.writingTo(modeLabel);

        addRenderableOnly(gristLabel);
        addRenderableWidget(gristSelector);
        addRenderableOnly(modeLabel);
        addRenderableWidget(modeSelector);

        amountInput = new EditBox(font, x + 150, y + 31, 39, 9, Component.empty());
        amountInput.setBordered(false);
        amountInput.setFocused(false);
        amountInput.mouseClicked(0, 0, 0);
        amountInput.setMaxLength(25);
        amountInput.setValue("0");
        amountInput.setFilter(str -> {
            if (str.length() == 0)
                return true;
            try {
                int v = Integer.parseInt(str);
                return v >= 0;
            } catch (NumberFormatException e) {
                return false;
            }
        });
        addRenderableWidget(amountInput);

        selectedGrists.clear();
        selectedGrists.add(Component.translatable(menu.entries.isEmpty() ? LIST_EMPTY : LIST_SELECTED));
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);

        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, BACKGROUND_WIDTH, BACKGROUND_HEIGHT);
    }

    protected void contentsCleared() {
        selectedGrists.clear();
        selectedGrists.add(Component.translatable(LIST_EMPTY));
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

        PacketDistributor.sendToServer(new AddGristFilterEntry(grist, amount, mode));
        menu.appendEntry(grist, amount, mode);
    }
}
