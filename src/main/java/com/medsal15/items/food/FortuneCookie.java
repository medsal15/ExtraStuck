package com.medsal15.items.food;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;

import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.level.Level;

public class FortuneCookie extends Item {
    public FortuneCookie(Properties properties) {
        super(properties);
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level,
            @Nonnull LivingEntity entity) {
        if (stack.has(DataComponents.CONTAINER)) {
            ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
            if (contents != null) {
                ExtraStuck.LOGGER.info("contents {}", contents.nonEmptyItems());
                if (entity instanceof Player player) {
                    for (ItemStack item : contents.nonEmptyItems()) {
                        if (!player.getInventory().add(item)) {
                            player.drop(item, false);
                        }
                    }
                } else {
                    double x = entity.getX();
                    double y = entity.getY();
                    double z = entity.getZ();
                    for (ItemStack item : contents.nonEmptyItems()) {
                        ItemEntity itemEntity = new ItemEntity(level, x, y, z, item);
                        level.addFreshEntity(itemEntity);
                    }
                }
            }
        }
        // Override eating, as it would otherwise prevent the last fortune cookie from
        // giving an item
        FoodProperties foodProperties = stack.getFoodProperties(entity);
        if (foodProperties != null) {
            entity.eat(level, stack, foodProperties);
        }
        return stack;
    }
}
