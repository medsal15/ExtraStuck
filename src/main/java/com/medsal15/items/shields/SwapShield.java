package com.medsal15.items.shields;

import javax.annotation.Nonnull;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredItem;

public class SwapShield extends ESShield {
    public DeferredItem<Item> next;

    public SwapShield(Properties properties, DeferredItem<Item> next) {
        super(properties);
        this.next = next;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (player.isShiftKeyDown() && next != null) {
            ItemStack swap = new ItemStack(next.getDelegate(), stack.getCount(), stack.getComponentsPatch());

            return InteractionResultHolder.success(swap);
        }
        return super.use(level, player, hand);
    }
}
