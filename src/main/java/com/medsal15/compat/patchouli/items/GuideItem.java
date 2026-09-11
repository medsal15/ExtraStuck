package com.medsal15.compat.patchouli.items;

import com.medsal15.ExtraStuck;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.common.item.ItemModBook;
import vazkii.patchouli.common.item.PatchouliDataComponents;

public class GuideItem extends ItemModBook {
    public GuideItem() {
        super();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.has(PatchouliDataComponents.BOOK))
            stack.set(PatchouliDataComponents.BOOK, ExtraStuck.modid("extrastuck"));

        return super.use(level, player, hand);
    }
}
