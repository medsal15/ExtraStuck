package com.medsal15.client.gui;

import com.medsal15.menus.ChargerMenu;
import com.medsal15.network.ESPackets.ToggleMode;
import com.mraof.minestuck.client.gui.GoButton;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public class SwitchButton extends GoButton {
    public static final String CHARGE = "-->";
    public static final String DRAIN = "<--";

    private final ChargerMenu menu;

    public SwitchButton(int x, int y, int width, int height, ChargerMenu menu) {
        super(x, y, width, height, menu, true);

        this.menu = menu;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        if (this.active && this.visible) {
            if (this.isValidClickButton(mouseKey)) {
                boolean flag = this.clicked(mouseX, mouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    onToggleClick();
                    return true;
                }
            }

            return false;
        } else {
            return false;
        }
    }

    @Override
    public Component getMessage() {
        return Component.literal(menu.getMode() ? CHARGE : DRAIN);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        onToggleClick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.active || !this.visible || keyCode != 257 && keyCode != 335) {
            return false;
        } else {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            if (Screen.hasShiftDown()) {
                onToggleClick();
            }

            return true;
        }
    }

    private void onToggleClick() {
        if (menu.getMode()) {
            PacketDistributor.sendToServer(new ToggleMode(false), new CustomPacketPayload[0]);
        } else {
            PacketDistributor.sendToServer(new ToggleMode(true), new CustomPacketPayload[0]);
        }
    }
}
