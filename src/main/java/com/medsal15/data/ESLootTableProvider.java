package com.medsal15.data;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.CardOreBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.compat.irons_spellbooks.items.ESISSItems;
import com.medsal15.items.ESItems;
import com.medsal15.loot_functions.TurnToCardFunction;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;

import net.minecraft.ChatFormatting;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ESLootTableProvider extends LootTableProvider {
    public ESLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BlockSubProvider::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(TableSubProvider::new, LootContextParamSets.CHEST)), lookupProvider);
    }

    public static class TableSubProvider implements LootTableSubProvider {
        public TableSubProvider(HolderLookup.Provider provider) {
        }

        public static ResourceKey<LootTable> GIFT_LOOT_TABLE = key("gameplay/gift");
        public static ResourceKey<LootTable> SPAM_LOOT_TABLE = key("gameplay/spam");
        public static ResourceKey<LootTable> INJECT_PUSB = key("chests/inject/unique_spellbook");

        public static ResourceKey<LootTable> INJECT_TERRAIN_END = key("chests/inject/medium_end");
        public static ResourceKey<LootTable> INJECT_TERRAIN_HEAT = key("chests/inject/medium_heat");
        public static ResourceKey<LootTable> INJECT_TERRAIN_FROST = key("chests/inject/medium_frost");
        public static ResourceKey<LootTable> INJECT_TERRAIN_RAIN = key("chests/inject/medium_rain");
        public static ResourceKey<LootTable> INJECT_TERRAIN_ROCK = key("chests/inject/medium_rock");

        public static ResourceKey<LootTable> INJECT_TITLE_THUNDER = key("chests/inject/medium_thunder");
        public static ResourceKey<LootTable> INJECT_TITLE_CAKE = key("chests/inject/medium_cake");

        @Override
        public void generate(@Nonnull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            // #region Land Terrains
            consumer.accept(INJECT_TERRAIN_END,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.END_ARROW).setQuality(1).apply(rangeAmount(0, 8)))));
            consumer.accept(INJECT_TERRAIN_HEAT,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.NETHER_ARROW).setQuality(1).apply(rangeAmount(0, 8)))));
            consumer.accept(INJECT_TERRAIN_FROST,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.FLAME_ARROW).setQuality(1).apply(rangeAmount(0, 8))
                                    .setWeight(4))
                            .add(LootItem.lootTableItem(ESItems.FLAME_SHIELD).setQuality(2)
                                    .apply(rangeDamage(.75f, 1)))));
            consumer.accept(INJECT_TERRAIN_RAIN,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.PRISMARINE_ARROW).setQuality(1)
                                    .apply(rangeAmount(0, 8)))));
            consumer.accept(INJECT_TERRAIN_ROCK,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.PROJECDRILL).setQuality(2).apply(rangeAmount(0, 8)))));
            // #endregion Land Terrains

            // #region Land Titles
            consumer.accept(INJECT_TITLE_THUNDER,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.LIGHTNING_ARROW).setQuality(1)
                                    .apply(rangeAmount(0, 8)))));
            consumer.accept(INJECT_TITLE_CAKE,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ESItems.LEMON_CAKE).setQuality(1))
                            .add(EmptyLootItem.emptyItem().setWeight(12))));
            // #endregion Land Titles

            consumer.accept(INJECT_PUSB,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ESISSItems.PERFECTLY_UNIQUE_SPELLBOOK)
                                    .apply(TurnToCardFunction.builder(true)))
                            .add(EmptyLootItem.emptyItem().setWeight(99))));

            // Gift
            consumer.accept(GIFT_LOOT_TABLE, giftLootTable());
            consumer.accept(SPAM_LOOT_TABLE, spamLootTable());
        }

        private static LootTable.Builder giftLootTable() {
            return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
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
                            .apply(rangeAmount(1, 8)))
                    .add(LootItem.lootTableItem(Items.REDSTONE).setQuality(2).setWeight(5)
                            .apply(rangeAmount(1, 8)))
                    .add(TagEntry.expandTag(MSTags.Items.FAYGO).setWeight(6))
                    .add(TagEntry.expandTag(MSTags.Items.GRIST_CANDY).setQuality(1).setWeight(3))
                    .add(LootItem.lootTableItem(ESItems.GOLDEN_PAN).setQuality(5)));
        }

        private static LootTable.Builder spamLootTable() {
            return LootTable.lootTable().withPool(LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(Items.PAPER).apply(SetComponentsFunction.setComponent(
                            DataComponents.ITEM_NAME, Component.translatable(ESLangProvider.SPAM_TITLE_1)))
                            .apply(SetComponentsFunction.setComponent(DataComponents.LORE,
                                    new ItemLore(List.of(Component.translatable(ESLangProvider.SPAM_DESC_1))))))
                    .add(LootItem.lootTableItem(Items.PAPER).apply(SetComponentsFunction.setComponent(
                            DataComponents.ITEM_NAME, Component.translatable(ESLangProvider.SPAM_TITLE_2)))
                            .apply(SetComponentsFunction.setComponent(DataComponents.LORE,
                                    new ItemLore(List.of(Component.translatable(ESLangProvider.SPAM_DESC_2))))))
                    .add(LootItem.lootTableItem(Items.PAPER).apply(SetComponentsFunction.setComponent(
                            DataComponents.ITEM_NAME, Component.translatable(ESLangProvider.SPAM_TITLE_3)))
                            .apply(SetComponentsFunction.setComponent(DataComponents.LORE,
                                    new ItemLore(List.of(Component.translatable(ESLangProvider.SPAM_DESC_3)
                                            .withStyle(ChatFormatting.GRAY)))))));
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

    public static class BlockSubProvider extends BlockLootSubProvider {
        public BlockSubProvider(HolderLookup.Provider lookupProvider) {
            super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ESBlocks.BLOCKS.getEntries()
                    .stream()
                    // Cast to Block here, otherwise it will be a ? extends Block and Java will
                    // complain.
                    .map(e -> (Block) e.value())
                    .toList();
        }

        @Override
        protected void generate() {
            dropSelf(ESBlocks.CUT_GARNET.get());
            dropSelf(ESBlocks.CUT_GARNET_STAIRS.get());
            add(ESBlocks.CUT_GARNET_SLAB.get(), createSlabItemTable(ESBlocks.CUT_GARNET_SLAB.get()));
            dropSelf(ESBlocks.CUT_GARNET_WALL.get());
            dropSelf(ESBlocks.GARNET_BRICKS.get());
            dropSelf(ESBlocks.GARNET_BRICK_STAIRS.get());
            add(ESBlocks.GARNET_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.GARNET_BRICK_SLAB.get()));
            dropSelf(ESBlocks.GARNET_BRICK_WALL.get());
            dropSelf(ESBlocks.CHISELED_GARNET_BRICKS.get());

            dropSelf(ESBlocks.CUT_RUBY.get());
            dropSelf(ESBlocks.CUT_RUBY_STAIRS.get());
            add(ESBlocks.CUT_RUBY_SLAB.get(), createSlabItemTable(ESBlocks.CUT_RUBY_SLAB.get()));
            dropSelf(ESBlocks.CUT_RUBY_WALL.get());
            dropSelf(ESBlocks.RUBY_BRICKS.get());
            dropSelf(ESBlocks.RUBY_BRICK_STAIRS.get());
            add(ESBlocks.RUBY_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.RUBY_BRICK_SLAB.get()));
            dropSelf(ESBlocks.RUBY_BRICK_WALL.get());
            dropSelf(ESBlocks.CHISELED_RUBY_BRICKS.get());

            dropSelf(ESBlocks.COBALT_BLOCK.get());
            dropSelf(ESBlocks.COBALT_BARS.get());
            add(ESBlocks.COBALT_DOOR.get(), this::createDoorTable);
            dropSelf(ESBlocks.COBALT_TRAPDOOR.get());
            dropSelf(ESBlocks.COBALT_PRESSURE_PLATE.get());

            dropSelf(ESBlocks.SULFUROUS_STONE.get());
            dropSelf(ESBlocks.SULFUROUS_STONE_STAIRS.get());
            add(ESBlocks.SULFUROUS_STONE_SLAB.get(), createSlabItemTable(ESBlocks.SULFUROUS_STONE_SLAB.get()));
            dropSelf(ESBlocks.SULFUROUS_STONE_WALL.get());

            dropSelf(ESBlocks.MARBLE.get());
            dropSelf(ESBlocks.MARBLE_STAIRS.get());
            add(ESBlocks.MARBLE_SLAB.get(), createSlabItemTable(ESBlocks.MARBLE_SLAB.get()));
            dropSelf(ESBlocks.MARBLE_WALL.get());
            dropSelf(ESBlocks.POLISHED_MARBLE.get());
            dropSelf(ESBlocks.POLISHED_MARBLE_STAIRS.get());
            add(ESBlocks.POLISHED_MARBLE_SLAB.get(), createSlabItemTable(ESBlocks.POLISHED_MARBLE_SLAB.get()));
            dropSelf(ESBlocks.POLISHED_MARBLE_WALL.get());
            dropSelf(ESBlocks.MARBLE_BRICKS.get());
            dropSelf(ESBlocks.MARBLE_BRICK_STAIRS.get());
            add(ESBlocks.MARBLE_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.MARBLE_BRICK_SLAB.get()));
            dropSelf(ESBlocks.MARBLE_BRICK_WALL.get());

            dropSelf(ESBlocks.ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.ZILLIUM_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.ZILLIUM_BRICK_WALL.get());

            add(ESBlocks.PIZZA.get(), noDrop());
            dropOther(ESBlocks.DIVINE_TEMPTATION_BLOCK.get(), Items.CAULDRON);
            dropOther(ESBlocks.MORTAL_TEMPTATION_BLOCK.get(), Items.CAULDRON);
            add(ESBlocks.LEMON_CAKE.get(), noDrop());

            add(ESBlocks.CARD_ORE.get(), this::droppingWithOreItem);

            dropSelf(ESBlocks.PRINTER.get());
            dropSelf(ESBlocks.DISPRINTER.get());
            dropSelf(ESBlocks.CHARGER.get());
            dropSelf(ESBlocks.REACTOR.get());
            dropSelf(ESBlocks.URANIUM_BLASTER.get());
            dropSelf(ESBlocks.DOWEL_STORAGE.get());
            dropSelf(ESBlocks.CARD_STORAGE.get());

            dropSelf(ESBlocks.NORMAL_CAT_PLUSH.get());
        }

        private LootTable.Builder droppingWithOreItem(Block block) {
            return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(DynamicLoot.dynamicEntry(CardOreBlockEntity.ITEM_DYNAMIC))));
        }
    }
}
