package com.medsal15.items;

import javax.annotation.Nonnull;

import com.medsal15.items.components.ESDataComponents;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class GiftItem extends Item {
    public GiftItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel && stack.has(ESDataComponents.GIFT_TABLE)) {
            ResourceKey<LootTable> tableId = stack.get(ESDataComponents.GIFT_TABLE);
            MinecraftServer server = level.getServer();
            if (server != null) {
                LootTable table = server.reloadableRegistries().getLootTable(tableId);
                if (table != null) {
                    LootParams.Builder builder = new LootParams.Builder(serverLevel).withLuck(player.getLuck());
                    LootParams params = builder.create(LootContextParamSet.builder().build());
                    ObjectArrayList<ItemStack> rewards = table.getRandomItems(params);
                    for (ItemStack reward : rewards) {
                        if (!player.getInventory().add(reward)) {
                            player.drop(reward, false);
                        }
                    }
                    if (!player.isCreative())
                        stack.shrink(1);
                    return InteractionResultHolder.consume(stack);
                }
            }
        }

        return super.use(level, player, hand);
    }
}
