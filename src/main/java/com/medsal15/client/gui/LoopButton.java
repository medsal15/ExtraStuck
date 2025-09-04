package com.medsal15.client.gui;

import com.mraof.minestuck.client.gui.GoButton;
import com.mraof.minestuck.inventory.MachineContainerMenu;
import com.mraof.minestuck.network.block.MachinePackets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.neoforged.neoforge.network.PacketDistributor;

public class LoopButton extends GoButton {
    public static final String LOOP = "extrastuck.button.loop";

    private final MachineContainerMenu menu;

    public LoopButton(int x, int y, int width, int height, MachineContainerMenu menu) {
        super(x, y, width, height, menu, true);

        this.menu = menu;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseKey) {
        // >:(
        if (this.active && this.visible) {
            if (this.isValidClickButton(mouseKey)) {
                boolean flag = this.clicked(mouseX, mouseY);
                if (flag) {
                    this.playDownSound(Minecraft.getInstance().getSoundManager());
                    onLoopClick();
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
        return Component.translatable(menu.isRunning() ? STOP : LOOP);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        onLoopClick();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (!this.active || !this.visible || keyCode != 257 && keyCode != 335) {
            return false;
        } else {
            this.playDownSound(Minecraft.getInstance().getSoundManager());
            if (Screen.hasShiftDown()) {
                this.onLoopClick();
            }

            return true;
        }
    }

    private void onLoopClick() {
        if (!menu.isLooping()) {
            PacketDistributor.sendToServer(new MachinePackets.SetLooping(true), new CustomPacketPayload[0]);
        } else if (menu.isRunning()) {
            PacketDistributor.sendToServer(new MachinePackets.SetRunning(false), new CustomPacketPayload[0]);
        }
    }
}
