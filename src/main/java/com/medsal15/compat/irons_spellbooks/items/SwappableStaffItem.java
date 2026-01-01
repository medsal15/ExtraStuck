package com.medsal15.compat.irons_spellbooks.items;

import javax.annotation.Nonnull;

import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.registries.DeferredItem;

public class SwappableStaffItem extends StaffItem {
    private final DeferredItem<Item> other;

    public SwappableStaffItem(Properties properties, DeferredItem<Item> other) {
        super(properties);

        this.other = other;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        if (other != null && player.isShiftKeyDown()) {
            ItemStack stack = player.getItemInHand(hand);
            ItemStack swap = new ItemStack(other.getDelegate(), stack.getCount(), stack.getComponentsPatch());

            return InteractionResultHolder.success(swap);
        }

        return super.use(level, player, hand);
    }
}
