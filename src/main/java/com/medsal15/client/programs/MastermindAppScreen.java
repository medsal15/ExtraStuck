package com.medsal15.client.programs;

import java.util.List;

import com.medsal15.ExtraStuck;
import com.medsal15.client.screen.computer.MastermindDecodeScreen;
import com.medsal15.client.screen.computer.MastermindEncodeScreen;
import com.mraof.minestuck.blockentity.ComputerBlockEntity;
import com.mraof.minestuck.client.gui.computer.ButtonListHelper;
import com.mraof.minestuck.client.gui.computer.ProgramGui;
import com.mraof.minestuck.client.gui.computer.ThemedScreen;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.computer.ProgramType.EmptyData;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class MastermindAppScreen implements ProgramGui<ProgramType.EmptyData> {
    public static final String NAME = ExtraStuck.MODID + ".program_screen.mastermind";
    public static final String DECODER = ExtraStuck.MODID + ".program_screen.mastermind.decorder";
    public static final String RECODER = ExtraStuck.MODID + ".program_screen.mastermind.recorder";

    private final ButtonListHelper buttonListHelper = new ButtonListHelper();

    @Override
    public void onInit(ThemedScreen screen) {
        buttonListHelper.init(screen);
    }

    @Override
    public void onUpdate(ThemedScreen screen, EmptyData data) {
        buttonListHelper.updateButtons(List.of(
                new ButtonListHelper.ButtonData(Component.translatable(DECODER), () -> openDecoder(screen.computer)),
                new ButtonListHelper.ButtonData(Component.translatable(RECODER), () -> openRecoder(screen.computer))));
    }

    @Override
    public void render(GuiGraphics graphics, ThemedScreen screen) {
        ProgramGui.drawHeaderMessage(Component.translatable(NAME), graphics, screen);
    }

    private static void openDecoder(ComputerBlockEntity computer) {
        Minecraft.getInstance().setScreen(new MastermindDecodeScreen(computer));
    }

    private static void openRecoder(ComputerBlockEntity computer) {
        Minecraft.getInstance().setScreen(new MastermindEncodeScreen(computer));
    }
}
