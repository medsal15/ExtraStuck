package com.medsal15.items;

import javax.annotation.Nonnull;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;

public class GiftItem extends Item {
    public GiftItem(Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        if (level instanceof ServerLevel serverLevel && stack.has(ESDataComponents.GIFT_TABLE)) {
            var tableId = stack.get(ESDataComponents.GIFT_TABLE);
            var server = level.getServer();
            if (server != null) {
                var table = server.reloadableRegistries().getLootTable(tableId);
                if (table != null) {
                    var builder = new LootParams.Builder(serverLevel).withLuck(player.getLuck());
                    var params = builder.create(LootContextParamSet.builder().build());
                    var rewards = table.getRandomItems(params);
                    var x = player.getX();
                    var y = player.getY();
                    var z = player.getZ();
                    for (var reward : rewards) {
                        ItemEntity itemEntity = new ItemEntity(level, x, y, z, reward);
                        level.addFreshEntity(itemEntity);
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
