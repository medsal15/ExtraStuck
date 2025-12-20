package com.medsal15.modus;

import java.util.List;

import com.medsal15.config.ConfigServer;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.modus.MastermindCardItem;
import com.mraof.minestuck.inventory.captchalogue.ModusType;

import net.minecraft.core.NonNullList;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.fml.LogicalSide;

public class MastermindModus extends BaseModus {
    private int difficulty = 0;

    public MastermindModus(ModusType<? extends MastermindModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public void initModus(ItemStack item, ServerPlayer player, NonNullList<ItemStack> prev, int size) {
        super.initModus(item, player, prev, size);

        if (item.has(ESDataComponents.DIFFICULTY)) {
            this.difficulty = item.get(ESDataComponents.DIFFICULTY);
        }
    }

    @Override
    public CompoundTag writeToNBT(CompoundTag nbt, Provider provider) {
        nbt.putInt("difficulty", difficulty);

        return super.writeToNBT(nbt, provider);
    }

    @Override
    public void readFromNBT(CompoundTag nbt, Provider provider) {
        super.readFromNBT(nbt, provider);

        difficulty = nbt.getInt("difficulty");
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int slot, boolean asCard) {
        ItemStack item = super.getItem(player, slot, asCard);
        int difficulty;
        if (ConfigServer.MASTERMIND_HARDER.getAsBoolean() && item.has(ESDataComponents.DIFFICULTY)) {
            // Imagine captchaloguing a locked card
            difficulty = Math.min(item.get(ESDataComponents.DIFFICULTY) + 1, 6);
        } else if (this.difficulty > 0 && this.difficulty <= 6 && ConfigServer.MASTERMIND_CHANGE.getAsBoolean()) {
            difficulty = this.difficulty;
        } else {
            difficulty = ConfigServer.MASTERMIND_DIFFICULTY.get();
        }

        int code = MastermindCardItem.generateCode(difficulty, player.getRandom());

        ItemStack lock = ESItems.MASTERMIND_CARD.toStack();
        lock.set(ESDataComponents.DIFFICULTY, difficulty);
        lock.set(ESDataComponents.MASTERMIND_CODE, code);
        lock.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(item)));

        return lock;
    }
}
