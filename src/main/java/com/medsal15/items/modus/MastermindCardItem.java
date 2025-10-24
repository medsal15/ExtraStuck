package com.medsal15.items.modus;

import javax.annotation.Nonnull;

import com.medsal15.items.ESDataComponents;
import com.medsal15.menus.MastermindCardMenu;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

public class MastermindCardItem extends Item {
    public MastermindCardItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND)
            return InteractionResultHolder.pass(stack);
        if (!stack.has(DataComponents.CONTAINER)) {
            stack.shrink(1);
            return InteractionResultHolder.consume(stack);
        }
        if (!stack.has(ESDataComponents.DIFFICULTY) || !stack.has(ESDataComponents.MASTERMIND_CODE)) {
            ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
            if (contents != null) {
                for (ItemStack item : contents.nonEmptyItemsCopy()) {
                    if (!player.getInventory().add(item)) {
                        player.drop(item, false);
                    }
                }
            }
            stack.shrink(1);
            return InteractionResultHolder.consume(stack);
        }

        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.openMenu(new SimpleMenuProvider(
                    (windowId, playInventory, play) -> new MastermindCardMenu(windowId, stack),
                    getName(stack)));
        }

        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide);
    }

    public static int generateCode(int difficulty, RandomSource random) {
        int max = (int) Math.pow(difficulty, 4);
        return random.nextInt(max);
    }

    public static int[] breakCode(int code, int difficulty) {
        int[] broken = new int[4];
        for (int i = 0; i < 4; i++) {
            broken[i] = code % difficulty;
            code /= difficulty;
        }
        return broken;
    }

    public static int[] breakCode(ItemStack stack) {
        if (!stack.has(ESDataComponents.DIFFICULTY) || !stack.has(ESDataComponents.MASTERMIND_CODE))
            return new int[4];
        int code = stack.get(ESDataComponents.MASTERMIND_CODE);
        int difficulty = stack.get(ESDataComponents.DIFFICULTY);
        return breakCode(code, difficulty);
    }

    public static int makeCode(int[] parts, int difficulty) {
        int code = 0;
        for (int i = 3; i >= 0; i--) {
            code *= difficulty;
            code += parts[i];
        }
        return code;
    }

    public static int makeCode(byte[] parts, int difficulty) {
        int[] iparts = new int[4];
        for (int i = 0; i < 4; i++) {
            iparts[i] = parts[i];
        }
        return makeCode(iparts, difficulty);
    }
}
