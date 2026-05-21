package com.medsal15.items.tools;

import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ConvertOnDestroyItem extends Item {
    private final Supplier<ItemStack> result;

    public ConvertOnDestroyItem(Properties properties, @Nonnull Supplier<ItemStack> result) {
        super(properties);
        this.result = result;
    }

    @Override
    public <T extends LivingEntity> int damageItem(@Nonnull ItemStack stack, int amount,
            @SuppressWarnings("null") @Nullable T entity, @Nonnull Consumer<Item> onBroken) {
        if (entity != null) {
            Integer damage = stack.get(DataComponents.DAMAGE);
            Integer maxDamage = stack.get(DataComponents.MAX_DAMAGE);
            if (damage != null && maxDamage != null && amount + damage >= maxDamage) {
                ItemStack newStack = result.get();
                if (entity instanceof Player player) {
                    if (!player.getInventory().add(newStack))
                        player.drop(newStack, false);
                }
            }
        }

        return amount;
    }
}
