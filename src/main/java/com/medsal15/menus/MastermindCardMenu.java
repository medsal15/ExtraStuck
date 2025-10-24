package com.medsal15.menus;

import static com.mraof.minestuck.client.gui.MSScreenFactories.closeDialogueScreen;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.items.ESDataComponents;
import com.medsal15.items.modus.MastermindCardItem;
import com.medsal15.network.ESPackets.MastermindAddAttempt;
import com.medsal15.network.ESPackets.MastermindDestroy;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.network.PacketDistributor;

public class MastermindCardMenu extends AbstractContainerMenu {
    private final ItemStack card;
    private final int[] input = new int[4];

    private int slot = 0;

    public MastermindCardMenu(int windowId, ItemStack card) {
        super(ESMenuTypes.MASTERMIND_CARD.get(), windowId);

        this.card = card;

        addSlot(new Slot(new SimpleContainer(card), 0, 0, 0));
    }

    public MastermindCardMenu(int windowId, Inventory inventory) {
        this(windowId, ItemStack.EMPTY);
    }

    @Override
    public boolean stillValid(@Nonnull Player playerIn) {
        ItemStack main = playerIn.getMainHandItem();
        ItemStack off = playerIn.getOffhandItem();
        return !main.isEmpty() && main == card || !off.isEmpty() && off == card;
    }

    @Override
    public ItemStack quickMoveStack(@Nonnull Player player, int index) {
        return ItemStack.EMPTY;
    }

    public ItemStack getCard() {
        return slots.get(0).getItem();
    }

    public int getDifficulty() {
        return getCard().getOrDefault(ESDataComponents.DIFFICULTY, 1);
    }

    public List<Integer> getAttempts() {
        return getCard().getOrDefault(ESDataComponents.ATTEMPTS, new ArrayList<>());
    }

    public int getAttemptsCount() {
        return getAttempts().size();
    }

    public int[] getAttempt(int index) {
        ItemStack card = getCard();
        List<Integer> attempts = card.getOrDefault(ESDataComponents.ATTEMPTS, List.of());
        int difficulty = card.getOrDefault(ESDataComponents.DIFFICULTY, 1);
        if (index >= attempts.size()) {
            return new int[4];
        }
        int attempt = attempts.get(index);
        return MastermindCardItem.breakCode(attempt, difficulty);
    }

    /**
     * @returns A pair of ints containing, in order, the amount of correct
     *          placements and the amount of misplacements
     */
    public int[] getHint(int index) {
        final int difficulty = getDifficulty();

        int correct = 0;
        int misplaced = 0;

        int[] solution = MastermindCardItem.breakCode(getCard());
        int[] attempt = getAttempt(index);

        int[] sum_solution = new int[difficulty];
        int[] sum_attempt = new int[difficulty];

        for (int i = 0; i < 4; i++) {
            sum_solution[solution[i]]++;
            sum_attempt[attempt[i]]++;

            if (solution[i] == attempt[i])
                correct++;
        }
        for (int i = 0; i < difficulty; i++) {
            misplaced += Math.min(sum_attempt[i], sum_solution[i]);
        }

        return new int[] { correct, misplaced };
    }

    public int[] getCurrent() {
        return input;
    }

    public int getCurrentSlot() {
        return slot;
    }

    public void addAttempt(int grist) {
        input[slot] = grist;
        slot++;
        if (slot >= 4) {
            if (checkAttempt()) {
                PacketDistributor.sendToServer(new MastermindDestroy(true), new CustomPacketPayload[0]);
                closeDialogueScreen();
            } else {
                slot = 0;
                List<Integer> attempts = getAttempts();
                if (attempts.size() >= 5) {
                    // Failure!
                    PacketDistributor.sendToServer(new MastermindDestroy(false), new CustomPacketPayload[0]);
                    closeDialogueScreen();
                } else {
                    int code = MastermindCardItem.makeCode(input, getDifficulty());
                    PacketDistributor.sendToServer(new MastermindAddAttempt(code), new CustomPacketPayload[0]);
                    attempts.add(code);
                    getCard().set(ESDataComponents.ATTEMPTS, attempts);
                }
            }
        }
    }

    private boolean checkAttempt() {
        int[] solution = MastermindCardItem.breakCode(getCard());
        for (int i = 0; i < 4; i++) {
            if (solution[i] != input[i])
                return false;
        }

        return true;
    }
}
