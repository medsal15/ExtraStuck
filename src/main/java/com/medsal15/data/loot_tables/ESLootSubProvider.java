package com.medsal15.data.loot_tables;

import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTable.Builder;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ESLootSubProvider implements LootTableSubProvider {
    public ESLootSubProvider(HolderLookup.Provider provider) {
    }

    public static ResourceKey<LootTable> GIFT_LOOT_TABLE = key("gameplay/gift");

    @Override
    public void generate(@Nonnull BiConsumer<ResourceKey<LootTable>, Builder> consumer) {
        // #region Land Terrains
        consumer.accept(key("chests/inject/medium_end"),
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ESItems.END_ARROW).setQuality(1).apply(rangeAmount(0, 8)))));
        consumer.accept(key("chests/inject/medium_heat"),
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ESItems.NETHER_ARROW).setQuality(1).apply(rangeAmount(0, 8)))));
        consumer.accept(key("chests/inject/medium_frost"),
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ESItems.FLAME_ARROW).setQuality(1).apply(rangeAmount(0, 8))
                                .setWeight(4))
                        .add(LootItem.lootTableItem(ESItems.FLAME_SHIELD).setQuality(2).apply(rangeDamage(.75f, 1)))));
        consumer.accept(key("chests/inject/medium_rain"),
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ESItems.PRISMARINE_ARROW).setQuality(1).apply(rangeAmount(0, 8)))));
        consumer.accept(key("chests/inject/medium_rock"),
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ESItems.PROJECDRILL).setQuality(2).apply(rangeAmount(0, 8)))));
        // #endregion Land Terrains

        // #region Land Titles
        consumer.accept(key("chests/inject/medium_thunder"),
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                        .add(LootItem.lootTableItem(ESItems.LIGHTNING_ARROW).setQuality(1).apply(rangeAmount(0, 8)))));
        // #endregion Land Titles

        // Gift
        consumer.accept(GIFT_LOOT_TABLE,
                LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                        .add(LootItem.lootTableItem(Items.PAPER).setQuality(-1).setWeight(20))
                        .add(LootItem.lootTableItem(Items.STRING).setQuality(-1).setWeight(15))
                        .add(LootItem.lootTableItem(MSItems.GAMEBRO_MAGAZINE).setWeight(10))
                        .add(LootItem.lootTableItem(MSItems.GAMEGRL_MAGAZINE).setWeight(10))
                        .add(LootItem.lootTableItem(MSItems.ICE_SHARD).setQuality(-1).setWeight(10))
                        .add(LootItem.lootTableItem(MSItems.SURPRISE_EMBRYO).setWeight(5))
                        .add(LootItem.lootTableItem(MSItems.STRAWBERRY).setQuality(1).setWeight(10))
                        .add(LootItem.lootTableItem(Items.MELON).setQuality(1).setWeight(10))
                        .add(LootItem.lootTableItem(Items.PUMPKIN).setQuality(1).setWeight(10))
                        .add(LootItem.lootTableItem(Items.GOLD_INGOT).setQuality(2).setWeight(5)
                                .apply(rangeAmount(1, 85)))
                        .add(LootItem.lootTableItem(Items.REDSTONE).setQuality(2).setWeight(5).apply(rangeAmount(1, 8)))
                        .add(TagEntry.expandTag(MSTags.Items.FAYGO).setWeight(6))
                        .add(TagEntry.expandTag(MSTags.Items.GRIST_CANDY).setQuality(1).setWeight(3))
                        .add(LootItem.lootTableItem(ESItems.GOLDEN_PAN).setQuality(5))));
    }

    public static ResourceKey<LootTable> key(String path) {
        return ResourceKey.create(Registries.LOOT_TABLE, ExtraStuck.modid(path));
    }

    public static LootItemFunction.Builder rangeAmount(float min, float max) {
        return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
    }

    public static LootItemFunction.Builder rangeDamage(float min, float max) {
        return SetItemDamageFunction.setDamage(UniformGenerator.between(min, max));
    }
}
