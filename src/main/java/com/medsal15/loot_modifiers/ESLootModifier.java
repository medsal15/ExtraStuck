package com.medsal15.loot_modifiers;

import java.util.List;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.LootModifier;

public abstract class ESLootModifier extends LootModifier {
    public ESLootModifier(LootItemCondition[] conditions) {
        super(conditions);
    }

    /**
     * Runs a table and returns its rolled contents
     *
     * @param level
     * @param loot_table ID of the loot table to run
     * @return
     */
    public static List<ItemStack> runTable(ServerLevel level, ResourceLocation loot_table) {
        ResourceKey<LootTable> key = ResourceKey.create(Registries.LOOT_TABLE, loot_table);
        LootTable table = level.getServer().reloadableRegistries().getLootTable(key);
        LootParams.Builder builder = new LootParams.Builder(level);
        LootParams params = builder.create(LootContextParamSet.builder().build());
        List<ItemStack> rewards = table.getRandomItems(params);
        return rewards;
    }
}
