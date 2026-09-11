package com.medsal15.client.screen.machine;

import java.text.NumberFormat;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.menus.VendingMachineMenu;
import com.medsal15.network.ESPackets.VendingMachineSetCost;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.client.gui.MachineScreen;
import com.mraof.minestuck.client.util.GuiUtil;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.neoforge.network.PacketDistributor;

public final class VendingMachineScreen {
    public static final String COST_TEXT = "extrastuck.vending_machine.cost";
    public static final String STORAGE_TITLE = "extrastuck.vending_machine.storage_title";
    public static final String STORAGE_TEXT = "extrastuck.vending_machine.storage";

    public static class Sell extends MachineScreen<VendingMachineMenu.Sell> {
        private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck
                .modid("textures/gui/vending_machine/sell.png");

        public Sell(VendingMachineMenu.Sell menu, Inventory inventory, Component title) {
            super(menu, inventory, title);
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

            Component text;
            if (menu.getCost() <= 0) {
                text = Component.translatable(GuiUtil.FREE);
            } else {
                text = Component.translatable(COST_TEXT, NumberFormat.getInstance().format(menu.getCost()));
            }
            guiGraphics.drawString(font, text, leftPos + 87 - font.width(text) / 2, topPos + 59, 0x828280, false);
        }
    }

    public static class Storage extends MachineScreen<VendingMachineMenu.Storage> {
        private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck
                .modid("textures/gui/vending_machine/store.png");

        public static int STORAGE_X = 20;
        public static int STORAGE_Y = 20;
        public static int STORAGE_WIDTH = 40;

        private EditBox priceBox;
        private boolean setCost = false;

        public Storage(VendingMachineMenu.Storage menu, Inventory inventory, Component title) {
            super(menu, inventory, title);
        }

        @Override
        protected void init() {
            super.init();

            priceBox = new EditBox(font, leftPos + 95, topPos + 52, 56, 19, Component.empty());
            priceBox.setFilter(s -> {
                if (s.length() == 0)
                    return true;

                int price;
                try {
                    price = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    return false;
                }
                return price >= 0;
            });
            priceBox.setResponder(s -> {
                int price = 0;
                if (s.length() > 0) {
                    try {
                        price = Integer.parseInt(s);
                    } catch (NumberFormatException e) {
                        return;
                    }
                    if (price < 0)
                        return;
                }
                menu.setCost(price);
                PacketDistributor.sendToServer(new VendingMachineSetCost(price), new CustomPacketPayload[0]);
            });
            addRenderableWidget(priceBox);
        }

        @Override
        public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
            if (!setCost) {
                priceBox.setValue(String.valueOf(menu.getCost()));
                setCost = true;
            }

            super.render(guiGraphics, mouseX, mouseY, partialTick);

            renderTooltip(guiGraphics, mouseX, mouseY);
        }

        @Override
        protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
            RenderSystem.setShaderColor(1, 1, 1, 1);

            guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

            if (menu.getMax() > 0) {
                int y = STORAGE_Y;
                guiGraphics.drawString(font, Component.translatable(STORAGE_TITLE), leftPos + STORAGE_X, topPos + y,
                        0x828280, false);
                y += font.lineHeight + 2;
                guiGraphics.drawWordWrap(font,
                        Component.translatable(STORAGE_TEXT, NumberFormat.getInstance().format(menu.getAmount()),
                                NumberFormat.getInstance().format(menu.getMax())),
                        leftPos + STORAGE_X, topPos + y, STORAGE_WIDTH, 0x828280);
            }
        }
    }
}
