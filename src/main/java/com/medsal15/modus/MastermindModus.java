package com.medsal15.modus;

import java.util.List;

import com.medsal15.config.ConfigCommon;
import com.medsal15.items.ESItems;
import com.medsal15.items.components.ESDataComponents;
import com.medsal15.items.modus.MastermindCardItem;
import com.mraof.minestuck.inventory.captchalogue.ModusType;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.fml.LogicalSide;

public class MastermindModus extends BaseModus {
    public MastermindModus(ModusType<? extends MastermindModus> type, LogicalSide side) {
        super(type, side);
    }

    @Override
    public ItemStack getItem(ServerPlayer player, int slot, boolean asCard) {
        ItemStack item = super.getItem(player, slot, asCard);
        int difficulty;
        if (ConfigCommon.MASTERMIND_HARDER.getAsBoolean() && item.has(ESDataComponents.DIFFICULTY)) {
            // Imagine captchaloguing a locked card
            difficulty = Math.min(item.get(ESDataComponents.DIFFICULTY) + 1, 6);
        } else {
            difficulty = ConfigCommon.MASTERMIND_DIFFICULTY.get();
        }

        int code = MastermindCardItem.generateCode(difficulty, player.getRandom());

        ItemStack lock = ESItems.MASTERMIND_CARD.toStack();
        lock.set(ESDataComponents.DIFFICULTY, difficulty);
        lock.set(ESDataComponents.MASTERMIND_CODE, code);
        lock.set(DataComponents.CONTAINER, ItemContainerContents.fromItems(List.of(item)));

        return lock;
    }
}
