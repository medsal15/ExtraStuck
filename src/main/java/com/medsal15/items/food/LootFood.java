package com.medsal15.items.food;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.medsal15.items.components.ESDataComponents;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class LootFood extends Item {
    @Nullable
    private final Component message;

    public LootFood(Properties properties) {
        super(properties);
        message = null;
    }

    public LootFood(Properties properties, Component message) {
        super(properties);
        this.message = message;
    }

    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull Level level,
            @Nonnull LivingEntity livingEntity) {
        if (level instanceof ServerLevel serverLevel && stack.has(ESDataComponents.GIFT_TABLE)
                && livingEntity instanceof ServerPlayer player) {
            ResourceKey<LootTable> tableId = stack.get(ESDataComponents.GIFT_TABLE);
            MinecraftServer server = level.getServer();
            if (server != null) {
                LootTable table = server.reloadableRegistries().getLootTable(tableId);
                if (table != null) {
                    LootParams.Builder builder = new LootParams.Builder(serverLevel).withLuck(player.getLuck());
                    LootParams params = builder.create(LootContextParamSet.builder().build());
                    ObjectArrayList<ItemStack> rewards = table.getRandomItems(params);
                    if (message != null)
                        player.sendSystemMessage(message);
                    for (ItemStack reward : rewards) {
                        if (!player.getInventory().add(reward)) {
                            player.drop(reward, false);
                        }
                    }
                }
            }
        }
        return super.finishUsingItem(stack, level, livingEntity);
    }
}
