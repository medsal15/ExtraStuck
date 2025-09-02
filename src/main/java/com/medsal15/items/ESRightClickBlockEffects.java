package com.medsal15.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;

public final class ESRightClickBlockEffects {
    public static InteractionResult brush(UseOnContext context) {
        Level level = context.getLevel();
        Player player = context.getPlayer();
        BlockPos pos = context.getClickedPos();

        if (!level.isClientSide && player != null
                && level.getBlockEntity(pos) instanceof BrushableBlockEntity brushable) {
            boolean complete = brushable.brush(level.getGameTime(), player, context.getClickedFace());
            if (complete) {
                ItemStack stack = context.getItemInHand();
                EquipmentSlot slot = context.getHand() == InteractionHand.MAIN_HAND ? EquipmentSlot.MAINHAND
                        : EquipmentSlot.OFFHAND;
                stack.hurtAndBreak(1, player, slot);
            }
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }
}
