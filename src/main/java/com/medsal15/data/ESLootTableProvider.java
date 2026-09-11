package com.medsal15.data;

import static com.mraof.minestuck.data.loot_table.MSChestLootTables.locationForTerrain;

import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.CardOreBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.compat.irons_spellbooks.items.ISSESItems;
import com.medsal15.entitysubpredicates.LandFishingHookPredicate;
import com.medsal15.items.ESItems;
import com.medsal15.loot.conditions.ESTerrainCondition;
import com.medsal15.loot.conditions.ESTitlecondition;
import com.medsal15.loot.functions.TurnToCardFunction;
import com.medsal15.world.land.ESLandTypes;
import com.mraof.minestuck.data.loot_table.MSChestLootTables;
import com.mraof.minestuck.data.loot_table.MSGiftLootTables;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.loot.MSLootTables;
import com.mraof.minestuck.item.loot.functions.SetBoondollarCount;
import com.mraof.minestuck.util.MSTags;
import com.mraof.minestuck.world.lands.LandTypes;

import net.minecraft.ChatFormatting;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.data.loot.LootTableSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ProblemReporter.Collector;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext.EntityTarget;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.DynamicLoot;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.NestedLootTable;
import net.minecraft.world.level.storage.loot.entries.TagEntry;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.EnchantWithLevelsFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetComponentsFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemDamageFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.AnyOfCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemEntityPropertyCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

public class ESLootTableProvider extends LootTableProvider {
    public ESLootTableProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(BlockSubProvider::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(TableSubProvider::new, LootContextParamSets.CHEST)),
                lookupProvider);
    }

    @Override
    protected void validate(@Nonnull WritableRegistry<LootTable> writableregistry,
            @Nonnull ValidationContext validationcontext, @Nonnull Collector problemreporter) {
        // Stupid vanilla validation not being aware of vanilla loot tables
    }

    public static class TableSubProvider implements LootTableSubProvider {
        private final HolderLookup.Provider provider;

        public TableSubProvider(HolderLookup.Provider provider) {
            this.provider = provider;
        }

        public static ResourceKey<LootTable> GIFT_LOOT_TABLE = key("gameplay/gift");
        public static ResourceKey<LootTable> SPAM_LOOT_TABLE = key("gameplay/spam");
        public static ResourceKey<LootTable> DEEPSLATE_UNREINFORCING = key("gameplay/deepslate_unreinforcing");

        public static ResourceKey<LootTable> TWO_OF_DIAMONDS = key("gameplay/two_of_diamonds");
        public static ResourceKey<LootTable> TWO_OF_SPADES = key("gameplay/two_of_spades");

        public static ResourceKey<LootTable> INJECT_PUSB = key("chests/inject/unique_spellbook");

        public static ResourceKey<LootTable> INJECT_TERRAIN_END = key("chests/inject/medium_end");
        public static ResourceKey<LootTable> INJECT_TERRAIN_HEAT = key("chests/inject/medium_heat");
        public static ResourceKey<LootTable> INJECT_TERRAIN_FROST = key("chests/inject/medium_frost");
        public static ResourceKey<LootTable> INJECT_TERRAIN_RAIN = key("chests/inject/medium_rain");
        public static ResourceKey<LootTable> INJECT_TERRAIN_ROCK = key("chests/inject/medium_rock");

        public static ResourceKey<LootTable> INJECT_TITLE_THUNDER = key("chests/inject/medium_thunder");
        public static ResourceKey<LootTable> INJECT_TITLE_CAKE = key("chests/inject/medium_cake");

        public static ResourceKey<LootTable> LAND_FISHING = key("gameplay/fishing/land");
        public static ResourceKey<LootTable> LAND_FISHING_TREASURE = key("gameplay/fishing/land/treasure");
        public static ResourceKey<LootTable> LAND_FISHING_JUNK = key("gameplay/fishing/land/junk");
        public static ResourceKey<LootTable> LAND_FISHING_FISH = key("gameplay/fishing/land/fish");

        @Override
        public void generate(@Nonnull BiConsumer<ResourceKey<LootTable>, LootTable.Builder> consumer) {
            // #region Land Terrains
            consumer.accept(INJECT_TERRAIN_END,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(UniformGenerator.between(0, 1))
                                    .add(LootItem.lootTableItem(ESItems.END_ARROW)
                                            .setQuality(1)
                                            .apply(rangeAmount(0, 8)))));
            consumer.accept(INJECT_TERRAIN_HEAT,
                    LootTable.lootTable().withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.NETHER_ARROW).setQuality(1)
                                    .apply(rangeAmount(0, 8)))));
            consumer.accept(INJECT_TERRAIN_FROST,
                    LootTable.lootTable().withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.FLAME_ARROW).setQuality(1)
                                    .apply(rangeAmount(0, 8))
                                    .setWeight(4))
                            .add(LootItem.lootTableItem(ESItems.FLAME_SHIELD).setQuality(2)
                                    .apply(rangeDamage(.75f, 1)))));
            consumer.accept(INJECT_TERRAIN_RAIN,
                    LootTable.lootTable().withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.PRISMARINE_ARROW)
                                    .setQuality(1)
                                    .apply(rangeAmount(0, 8)))));
            consumer.accept(INJECT_TERRAIN_ROCK,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool()
                                    .setRolls(UniformGenerator.between(0, 1))
                                    .add(LootItem.lootTableItem(ESItems.PROJECDRILL)
                                            .setQuality(2)
                                            .apply(rangeAmount(0, 8)))));

            // Dark
            consumer.accept(locationForTerrain(ESLandTypes.DARK, MSChestLootTables.WEAPON_ITEM_TABLE),
                    LootTable.lootTable().withPool(LootPool.lootPool().name(MSChestLootTables.ITEM_POOL)
                            .add(LootItem.lootTableItem(MSItems.DIAMOND_DAGGER).setWeight(10)
                                    .apply(rangeDamage(.25f, .5f)))
                            .add(LootItem.lootTableItem(MSItems.CLUBS_SUITARANG)
                                    .apply(rangeAmount(1, 5)))));
            consumer.accept(locationForTerrain(ESLandTypes.DARK, MSChestLootTables.SUPPLY_ITEM_TABLE),
                    LootTable.lootTable().withPool(LootPool.lootPool().name(MSChestLootTables.ITEM_POOL)
                            .add(LootItem.lootTableItem(ESItems.SHINEBREAKER)
                                    .apply(rangeDamage(.25f, .75f)).setQuality(5))
                            .add(LootItem.lootTableItem(Items.SCULK_SENSOR)
                                    .apply(rangeAmount(1, 5)).setQuality(3))));
            consumer.accept(locationForTerrain(ESLandTypes.DARK, MSChestLootTables.MISC_ITEM_TABLE),
                    LootTable.lootTable().withPool(LootPool.lootPool().name(MSChestLootTables.ITEM_POOL)
                            .add(LootItem.lootTableItem(Items.SCULK)
                                    .apply(rangeAmount(2, 10)).setWeight(2))
                            .add(LootItem.lootTableItem(Items.SCULK_VEIN)
                                    .apply(rangeAmount(2, 10)).setWeight(4))));

            consumer.accept(locationForTerrain(ESLandTypes.DARK, MSLootTables.CONSORT_GENERAL_STOCK),
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool().name(MSGiftLootTables.ITEM_POOL)
                                    .add(LootItem.lootTableItem(Items.AMETHYST_SHARD).apply(rangeAmount(4, 10))
                                            .setWeight(2))
                                    .add(LootItem.lootTableItem(Items.DISC_FRAGMENT_5))
                                    .add(LootItem.lootTableItem(Items.FEATHER).setWeight(6).apply(rangeAmount(5, 10))))
                            .withPool(LootPool.lootPool().name(MSGiftLootTables.BLOCK_POOL)
                                    .add(LootItem.lootTableItem(Items.SOUL_LANTERN).apply(rangeAmount(5, 15))
                                            .setWeight(5))
                                    .add(LootItem.lootTableItem(Items.DARK_OAK_LOG).setWeight(10)
                                            .apply(rangeAmount(8, 20)))));
            consumer.accept(locationForTerrain(ESLandTypes.DARK, MSLootTables.CONSORT_FOOD_STOCK),
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool().name(MSGiftLootTables.MAIN_POOL)
                                    .add(LootItem.lootTableItem(MSItems.JAR_OF_BUGS.get()).setWeight(5)
                                            .apply(rangeAmount(3, 10)))
                                    .add(LootItem.lootTableItem(MSItems.CONE_OF_FLIES.get()).setWeight(8)
                                            .apply(rangeAmount(3, 10)))
                                    .add(LootItem.lootTableItem(Items.GLOW_BERRIES).setWeight(8)
                                            .apply(rangeAmount(5, 15)))
                                    .add(LootItem.lootTableItem(Items.APPLE).setWeight(5).apply(rangeAmount(1, 5))))
                            .withPool(LootPool.lootPool().name(MSGiftLootTables.SPECIAL_POOL)
                                    .add(LootItem.lootTableItem(Items.GOLDEN_APPLE).apply(rangeAmount(1, 2)))));
            // #endregion Land Terrains

            // #region Land Titles
            consumer.accept(INJECT_TITLE_THUNDER,
                    LootTable.lootTable().withPool(LootPool.lootPool()
                            .setRolls(UniformGenerator.between(0, 1))
                            .add(LootItem.lootTableItem(ESItems.LIGHTNING_ARROW)
                                    .setQuality(1)
                                    .apply(rangeAmount(0, 8)))));
            consumer.accept(INJECT_TITLE_CAKE,
                    LootTable.lootTable()
                            .withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                                    .add(LootItem.lootTableItem(ESItems.LEMON_CAKE)
                                            .setQuality(1))
                                    .add(EmptyLootItem.emptyItem().setWeight(12))));
            // #endregion Land Titles

            consumer.accept(INJECT_PUSB,
                    LootTable.lootTable().withPool(LootPool.lootPool()
                            .setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(
                                    ISSESItems.PERFECTLY_UNIQUE_SPELLBOOK)
                                    .apply(TurnToCardFunction.builder(true)))
                            .add(EmptyLootItem.emptyItem().setWeight(99))));

            // Misc
            consumer.accept(GIFT_LOOT_TABLE, giftLootTable());
            consumer.accept(SPAM_LOOT_TABLE, spamLootTable());
            consumer.accept(DEEPSLATE_UNREINFORCING,
                    LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ESItems.DEEPSLATE_REINFORCEMENT))));

            // Cards
            consumer.accept(TWO_OF_DIAMONDS, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(MSItems.BOONDOLLARS)
                                    .apply(SetBoondollarCount.builder(
                                            UniformGenerator.between(2,
                                                    2000))))));
            consumer.accept(TWO_OF_SPADES, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(2))
                            .add(LootItem.lootTableItem(MSItems.BARBASOL_BOMB))
                            .add(LootItem.lootTableItem(ESItems.LEMONNADE))
                            .add(LootItem.lootTableItem(Items.TNT))));

            // Fishing
            consumer.accept(LAND_FISHING_TREASURE, fishingTreasureLootTable());
            consumer.accept(LAND_FISHING_JUNK, fishingJunkLootTable());
            consumer.accept(LAND_FISHING, LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(NestedLootTable.lootTableReference(LAND_FISHING_TREASURE)
                                    .setQuality(2).setWeight(5)
                                    .when(LootItemEntityPropertyCondition
                                            .hasProperties(
                                                    EntityTarget.THIS,
                                                    EntityPredicate.Builder
                                                            .entity()
                                                            .subPredicate(LandFishingHookPredicate
                                                                    .inOpenFluids(true)))))
                            .add(NestedLootTable.lootTableReference(LAND_FISHING_JUNK)
                                    .setQuality(-2).setWeight(10))
                            .add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH)
                                    .setQuality(-1)
                                    .setWeight(85))));
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
                    .add(LootItem.lootTableItem(Items.PAPER)
                            .apply(SetComponentsFunction.setComponent(
                                    DataComponents.ITEM_NAME,
                                    Component.translatable(
                                            ESLangProvider.SPAM_TITLE_1)))
                            .apply(SetComponentsFunction.setComponent(DataComponents.LORE,
                                    new ItemLore(List.of(Component.translatable(
                                            ESLangProvider.SPAM_DESC_1))))))
                    .add(LootItem.lootTableItem(Items.PAPER)
                            .apply(SetComponentsFunction.setComponent(
                                    DataComponents.ITEM_NAME,
                                    Component.translatable(
                                            ESLangProvider.SPAM_TITLE_2)))
                            .apply(SetComponentsFunction.setComponent(DataComponents.LORE,
                                    new ItemLore(List.of(Component.translatable(
                                            ESLangProvider.SPAM_DESC_2))))))
                    .add(LootItem.lootTableItem(Items.PAPER)
                            .apply(SetComponentsFunction.setComponent(
                                    DataComponents.ITEM_NAME,
                                    Component.translatable(
                                            ESLangProvider.SPAM_TITLE_3)))
                            .apply(SetComponentsFunction.setComponent(DataComponents.LORE,
                                    new ItemLore(List.of(Component.translatable(
                                            ESLangProvider.SPAM_DESC_3)
                                            .withStyle(ChatFormatting.GRAY)))))));
        }

        private LootTable.Builder fishingTreasureLootTable() {
            return LootTable.lootTable().withPool(
                    LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                            .add(LootItem.lootTableItem(ESItems.SOLID_FISHING_ROD)
                                    .apply(rangeDamage(0f, .25f))
                                    .apply(enchantWithLevel(30)))
                            .add(LootItem.lootTableItem(Items.BOOK)
                                    .apply(enchantWithLevel(30)))
                            .add(TagEntry.expandTag(MSTags.Items.GRIST_CANDY)
                                    .apply(rangeAmount(1, 5)))
                            .add(LootItem.lootTableItem(MSItems.GLUB_CLUB)
                                    .apply(rangeDamage(.25f, .75f)))
                            .add(LootItem.lootTableItem(MSItems.BOONDOLLARS)
                                    .apply(SetBoondollarCount.builder(
                                            UniformGenerator.between(10,
                                                    500))))
                            .add(LootItem.lootTableItem(MSItems.MUSIC_DISC_RETRO_BATTLE))
                            // funny dialogue reference
                            .add(LootItem.lootTableItem(ESItems.GUMMY_RING))
                            // Plushes
                            .add(LootItem.lootTableItem(MSItems.PLUSH_NAKAGATOR)
                                    .when(AnyOfCondition.anyOf(
                                            () -> new ESTerrainCondition(
                                                    LandTypes.END.get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.HEAT.get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.ROCK.get()))))
                            .add(LootItem.lootTableItem(MSItems.PLUSH_IGUANA)
                                    .when(AnyOfCondition.anyOf(
                                            () -> new ESTerrainCondition(
                                                    LandTypes.FLORA.get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.FOREST.get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.FROST.get()))))
                            .add(LootItem.lootTableItem(MSItems.PLUSH_SALAMANDER)
                                    .when(AnyOfCondition.anyOf(
                                            () -> new ESTerrainCondition(
                                                    LandTypes.FUNGI.get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.SHADE.get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.WOOD.get()))))
                            .add(LootItem.lootTableItem(MSItems.PLUSH_TURTLE)
                                    .when(AnyOfCondition.anyOf(
                                            () -> new ESTerrainCondition(
                                                    LandTypes.RAIN.get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.RAINBOW
                                                            .get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.SAND.get()),
                                            () -> new ESTerrainCondition(
                                                    LandTypes.SANDSTONE
                                                            .get())))));
        }

        private static LootTable.Builder fishingJunkLootTable() {
            return LootTable.lootTable().withPool(LootPool.lootPool().setRolls(ConstantValue.exactly(1))
                    .add(LootItem.lootTableItem(MSItems.STONE_TABLET).setWeight(15))
                    .add(LootItem.lootTableItem(MSItems.CRUMPLY_HAT).setWeight(10))
                    .add(LootItem.lootTableItem(MSItems.RAW_CRUXITE).apply(rangeAmount(1, 8))
                            .setWeight(10))
                    .add(LootItem.lootTableItem(MSItems.GRASSHOPPER).apply(rangeAmount(1, 5))
                            .setWeight(10))
                    .add(LootItem.lootTableItem(MSItems.CICADA).apply(rangeAmount(1, 5))
                            .setWeight(10))
                    .add(LootItem.lootTableItem(MSItems.TAB).apply(rangeAmount(1, 3)).setWeight(8))
                    .add(LootItem.lootTableItem(MSItems.SOPOR_SLIME_PIE).setWeight(5))
                    .add(LootItem.lootTableItem(Items.BAMBOO).setWeight(3))
                    .add(LootItem.lootTableItem(Items.STRING).setWeight(3))
                    .add(LootItem.lootTableItem(MSItems.BARBASOL))
                    .add(LootItem.lootTableItem(MSItems.CLOTHES_IRON))
                    .add(LootItem.lootTableItem(MSItems.INK_SQUID_PRO_QUO))
                    // Terrain-specific
                    .add(LootItem.lootTableItem(Items.OAK_PLANKS).apply(rangeAmount(1, 5))
                            .setWeight(5)
                            .when(() -> new ESTerrainCondition(LandTypes.FOREST.get())))
                    .add(LootItem.lootTableItem(Items.BIRCH_PLANKS).apply(rangeAmount(1, 5))
                            .setWeight(5)
                            .when(() -> new ESTerrainCondition(LandTypes.FOREST.get())))
                    .add(LootItem.lootTableItem(MSItems.ROCK_COOKIE)
                            .when(AnyOfCondition.anyOf(
                                    () -> new ESTerrainCondition(
                                            LandTypes.ROCK.get()),
                                    () -> new ESTerrainCondition(
                                            LandTypes.PETRIFICATION.get())))
                            .apply(rangeAmount(1, 5)).setWeight(5))
                    .add(LootItem.lootTableItem(MSItems.WOODEN_CARROT).setWeight(5)
                            .when(() -> new ESTerrainCondition(LandTypes.WOOD.get())))
                    // Title-specific
                    .add(LootItem.lootTableItem(MSItems.CAKE_MIX)
                            .when(() -> new ESTitlecondition(LandTypes.CAKE.get()))));
        }

        private static LootTable.Builder fishingFishLootTable() {
            // TODO actual land-based fishes to use here
            // Copy of vanilla loot table
            return LootTable.lootTable().withPool(
                    LootPool.lootPool().add(NestedLootTable.lootTableReference(BuiltInLootTables.FISHING_FISH)));
        }

        public static ResourceKey<LootTable> key(String path) {
            return ResourceKey.create(Registries.LOOT_TABLE, ExtraStuck.modid(path));
        }

        public static ResourceKey<LootTable> key(String modid, String path) {
            return ResourceKey.create(Registries.LOOT_TABLE, ResourceLocation.fromNamespaceAndPath(modid, path));
        }

        public static LootItemFunction.Builder rangeAmount(float min, float max) {
            return SetItemCountFunction.setCount(UniformGenerator.between(min, max));
        }

        public static LootItemFunction.Builder rangeDamage(float min, float max) {
            return SetItemDamageFunction.setDamage(UniformGenerator.between(min, max));
        }

        public LootItemFunction.Builder enchantWithLevel(int level) {
            return EnchantWithLevelsFunction.enchantWithLevels(provider, ConstantValue.exactly(level));
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
            dropSelf(ESBlocks.DEEPSLATE_PILLAR.get());
            add(ESBlocks.DEEPSLATE_CRUXITE_ORE.get(), this::cruxiteOreDrop);

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
            add(ESBlocks.SULFUROUS_STONE_SLAB.get(),
                    createSlabItemTable(ESBlocks.SULFUROUS_STONE_SLAB.get()));
            dropSelf(ESBlocks.SULFUROUS_STONE_WALL.get());

            dropSelf(ESBlocks.MARBLE.get());
            dropSelf(ESBlocks.MARBLE_STAIRS.get());
            add(ESBlocks.MARBLE_SLAB.get(), createSlabItemTable(ESBlocks.MARBLE_SLAB.get()));
            dropSelf(ESBlocks.MARBLE_WALL.get());
            dropSelf(ESBlocks.POLISHED_MARBLE.get());
            dropSelf(ESBlocks.POLISHED_MARBLE_STAIRS.get());
            add(ESBlocks.POLISHED_MARBLE_SLAB.get(),
                    createSlabItemTable(ESBlocks.POLISHED_MARBLE_SLAB.get()));
            dropSelf(ESBlocks.POLISHED_MARBLE_WALL.get());
            dropSelf(ESBlocks.MARBLE_BRICKS.get());
            dropSelf(ESBlocks.MARBLE_BRICK_STAIRS.get());
            add(ESBlocks.MARBLE_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.MARBLE_BRICK_SLAB.get()));
            dropSelf(ESBlocks.MARBLE_BRICK_WALL.get());

            dropOther(ESBlocks.ZILLIUM_BRICKS.get(), ESItems.GREEN_ZILLIUM_BRICKS);
            dropOther(ESBlocks.ZILLIUM_BRICK_STAIRS.get(), ESItems.GREEN_ZILLIUM_BRICKS);
            add(ESBlocks.ZILLIUM_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.GREEN_ZILLIUM_BRICK_SLAB.get()));
            dropOther(ESBlocks.ZILLIUM_BRICK_WALL.get(), ESItems.GREEN_ZILLIUM_BRICKS);

            dropSelf(ESBlocks.GREEN_ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.GREEN_ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.GREEN_ZILLIUM_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.GREEN_ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.GREEN_ZILLIUM_BRICK_WALL.get());
            dropSelf(ESBlocks.WAXED_GREEN_ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.WAXED_GREEN_ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.WAXED_GREEN_ZILLIUM_BRICK_SLAB.get(),
                    createSlabItemTable(ESBlocks.WAXED_GREEN_ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.WAXED_GREEN_ZILLIUM_BRICK_WALL.get());

            dropSelf(ESBlocks.BLUE_ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.BLUE_ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.BLUE_ZILLIUM_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.BLUE_ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.BLUE_ZILLIUM_BRICK_WALL.get());
            dropSelf(ESBlocks.WAXED_BLUE_ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.WAXED_BLUE_ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.WAXED_BLUE_ZILLIUM_BRICK_SLAB.get(),
                    createSlabItemTable(ESBlocks.WAXED_BLUE_ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.WAXED_BLUE_ZILLIUM_BRICK_WALL.get());

            dropSelf(ESBlocks.PINK_ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.PINK_ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.PINK_ZILLIUM_BRICK_SLAB.get(), createSlabItemTable(ESBlocks.PINK_ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.PINK_ZILLIUM_BRICK_WALL.get());
            dropSelf(ESBlocks.WAXED_PINK_ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.WAXED_PINK_ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.WAXED_PINK_ZILLIUM_BRICK_SLAB.get(),
                    createSlabItemTable(ESBlocks.WAXED_PINK_ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.WAXED_PINK_ZILLIUM_BRICK_WALL.get());

            dropSelf(ESBlocks.SECONDARY_ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.SECONDARY_ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.SECONDARY_ZILLIUM_BRICK_SLAB.get(),
                    createSlabItemTable(ESBlocks.SECONDARY_ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.SECONDARY_ZILLIUM_BRICK_WALL.get());
            dropSelf(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICKS.get());
            dropSelf(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICK_STAIRS.get());
            add(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICK_SLAB.get(),
                    createSlabItemTable(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICK_SLAB.get()));
            dropSelf(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICK_WALL.get());

            add(ESBlocks.PIZZA.get(), noDrop());
            dropOther(ESBlocks.DIVINE_TEMPTATION_BLOCK.get(), Items.CAULDRON);
            dropOther(ESBlocks.MORTAL_TEMPTATION_BLOCK.get(), Items.CAULDRON);
            add(ESBlocks.LEMON_CAKE.get(), noDrop());

            add(ESBlocks.CARD_ORE.get(), this::droppingWithOreItem);

            dropSelf(ESBlocks.PRINTER.get());
            dropSelf(ESBlocks.DISPRINTER.get());
            dropSelf(ESBlocks.CHARGER.get());
            dropSelf(ESBlocks.WIRELESS_CHARGER.get());
            dropSelf(ESBlocks.REACTOR.get());
            dropSelf(ESBlocks.URANIUM_BLASTER.get());
            dropSelf(ESBlocks.DOWEL_STORAGE.get());
            dropSelf(ESBlocks.CARD_STORAGE.get());
            dropSelf(ESBlocks.SMALL_VENDING_MACHINE.get());

            dropSelf(ESBlocks.NORMAL_CAT_PLUSH.get());
        }

        public LootTable.Builder droppingWithOreItem(Block block) {
            return LootTable.lootTable().withPool(applyExplosionCondition(block, LootPool.lootPool()
                    .setRolls(ConstantValue.exactly(1))
                    .add(DynamicLoot.dynamicEntry(CardOreBlockEntity.ITEM_DYNAMIC))));
        }

        public LootTable.Builder cruxiteOreDrop(Block block) {
            HolderLookup.RegistryLookup<Enchantment> registrylookup = this.registries
                    .lookupOrThrow(Registries.ENCHANTMENT);
            return createSilkTouchDispatchTable(block, applyExplosionDecay(block,
                    LootItem.lootTableItem(MSItems.RAW_CRUXITE.get())
                            .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                            .apply(ApplyBonusCount.addOreBonusCount(registrylookup.getOrThrow(Enchantments.FORTUNE)))));
        }
    }
}
