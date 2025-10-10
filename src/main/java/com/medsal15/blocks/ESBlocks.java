package com.medsal15.blocks;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

import com.medsal15.ExtraStuck;
import com.medsal15.blocks.food.DivineTemptationBlock;
import com.medsal15.blocks.machine.ChargerBlock;
import com.medsal15.blocks.machine.PrinterBlock;
import com.medsal15.blocks.machine.ReactorBlock;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.PressurePlateBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExtraStuck.MODID);

    // #region Food
    public static final DeferredBlock<PizzaBlock> PIZZA = BLOCKS.registerBlock("pizza", PizzaBlock::new,
            Block.Properties.of().mapColor(MapColor.COLOR_YELLOW).sound(SoundType.WOOL).noCollission()
                    .noOcclusion().pushReaction(PushReaction.DESTROY));
    public static final DeferredBlock<DivineTemptationBlock> DIVINE_TEMPTATION_BLOCK = BLOCKS
            .registerBlock("divine_temptation_block", DivineTemptationBlock::new,
                    BlockBehaviour.Properties.of().mapColor(MapColor.STONE).requiresCorrectToolForDrops().strength(2.0F)
                            .noOcclusion());
    // #endregion Food

    public static final DeferredBlock<CardOreBlock> CARD_ORE = BLOCKS.registerBlock("card_ore", CardOreBlock::new,
            Block.Properties.of().mapColor(MapColor.TERRACOTTA_LIGHT_BLUE).sound(SoundType.STONE).strength(1.5F, 6.0F));

    public static final DeferredBlock<NormalCatBlock> NORMAL_CAT_PLUSH = BLOCKS.registerBlock("normal_cat_plush",
            NormalCatBlock::new,
            Block.Properties.of().mapColor(MapColor.SNOW).sound(SoundType.WOOL).strength(0.5F).noOcclusion());

    // #region Machines
    // Objectively worse than a miniature alchemiter
    public static final DeferredBlock<PrinterBlock> PRINTER = BLOCKS.registerBlock("printer", PrinterBlock::new,
            Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3F));
    public static final DeferredBlock<ChargerBlock> CHARGER = BLOCKS.registerBlock("charger", ChargerBlock::new,
            Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3F));
    // Objectively better than a charger
    public static final DeferredBlock<ReactorBlock> REACTOR = BLOCKS.registerBlock("reactor", ReactorBlock::new,
            Block.Properties.of().mapColor(MapColor.METAL).requiresCorrectToolForDrops().strength(3F));
    // #endregion Machines

    // #region Garnet
    public static final DeferredBlock<Block> CUT_GARNET = BLOCKS.registerBlock("cut_garnet", Block::new,
            Block.Properties.of().mapColor(MapColor.TERRACOTTA_RED).strength(1.5F).sound(SoundType.AMETHYST)
                    .requiresCorrectToolForDrops());
    public static final DeferredBlock<StairBlock> CUT_GARNET_STAIRS = BLOCKS.register("cut_garnet_stairs",
            () -> new StairBlock(CUT_GARNET.get().defaultBlockState(), ofFullCopy(CUT_GARNET.get())));
    public static final DeferredBlock<SlabBlock> CUT_GARNET_SLAB = BLOCKS.register("cut_garnet_slab",
            () -> new SlabBlock(ofFullCopy(CUT_GARNET.get())));
    public static final DeferredBlock<WallBlock> CUT_GARNET_WALL = BLOCKS.register("cut_garnet_wall",
            () -> new WallBlock(ofFullCopy(CUT_GARNET.get())));
    public static final DeferredBlock<Block> GARNET_BRICKS = BLOCKS.register("garnet_bricks",
            () -> new Block(ofFullCopy(CUT_GARNET.get())));
    public static final DeferredBlock<StairBlock> GARNET_BRICK_STAIRS = BLOCKS.register("garnet_brick_stairs",
            () -> new StairBlock(GARNET_BRICKS.get().defaultBlockState(), ofFullCopy(CUT_GARNET.get())));
    public static final DeferredBlock<SlabBlock> GARNET_BRICK_SLAB = BLOCKS.register("garnet_brick_slab",
            () -> new SlabBlock(ofFullCopy(CUT_GARNET.get())));
    public static final DeferredBlock<WallBlock> GARNET_BRICK_WALL = BLOCKS.register("garnet_brick_wall",
            () -> new WallBlock(ofFullCopy(CUT_GARNET.get())));
    public static final DeferredBlock<Block> CHISELED_GARNET_BRICKS = BLOCKS.register("chiseled_garnet_bricks",
            () -> new Block(ofFullCopy(CUT_GARNET.get())));
    // #endregion Garnet

    // #region Ruby
    public static final DeferredBlock<Block> CUT_RUBY = BLOCKS.registerBlock("cut_ruby", Block::new,
            Block.Properties.of().mapColor(MapColor.COLOR_RED).strength(1.5F).sound(SoundType.AMETHYST)
                    .requiresCorrectToolForDrops());
    public static final DeferredBlock<StairBlock> CUT_RUBY_STAIRS = BLOCKS.register("cut_ruby_stairs",
            () -> new StairBlock(CUT_RUBY.get().defaultBlockState(), ofFullCopy(CUT_RUBY.get())));
    public static final DeferredBlock<SlabBlock> CUT_RUBY_SLAB = BLOCKS.register("cut_ruby_slab",
            () -> new SlabBlock(ofFullCopy(CUT_RUBY.get())));
    public static final DeferredBlock<WallBlock> CUT_RUBY_WALL = BLOCKS.register("cut_ruby_wall",
            () -> new WallBlock(ofFullCopy(CUT_RUBY.get())));
    public static final DeferredBlock<Block> RUBY_BRICKS = BLOCKS.register("ruby_bricks",
            () -> new Block(ofFullCopy(CUT_RUBY.get())));
    public static final DeferredBlock<StairBlock> RUBY_BRICK_STAIRS = BLOCKS.register("ruby_brick_stairs",
            () -> new StairBlock(RUBY_BRICKS.get().defaultBlockState(), ofFullCopy(CUT_RUBY.get())));
    public static final DeferredBlock<SlabBlock> RUBY_BRICK_SLAB = BLOCKS.register("ruby_brick_slab",
            () -> new SlabBlock(ofFullCopy(CUT_RUBY.get())));
    public static final DeferredBlock<WallBlock> RUBY_BRICK_WALL = BLOCKS.register("ruby_brick_wall",
            () -> new WallBlock(ofFullCopy(CUT_RUBY.get())));
    public static final DeferredBlock<Block> CHISELED_RUBY_BRICKS = BLOCKS.register("chiseled_ruby_bricks",
            () -> new Block(ofFullCopy(CUT_RUBY.get())));
    // #endregion Ruby

    // #region Cobalt
    public static final DeferredBlock<Block> COBALT_BLOCK = BLOCKS.registerBlock("cobalt_block", Block::new,
            Block.Properties.of().mapColor(MapColor.COLOR_BLUE).strength(5F, 6F).sound(SoundType.METAL)
                    .requiresCorrectToolForDrops());
    public static final DeferredBlock<IronBarsBlock> COBALT_BARS = BLOCKS.register("cobalt_bars",
            () -> new IronBarsBlock(ofFullCopy(COBALT_BLOCK.get()).noOcclusion()));
    public static final DeferredBlock<DoorBlock> COBALT_DOOR = BLOCKS.register("cobalt_door",
            () -> new DoorBlock(BlockSetType.COPPER, ofFullCopy(COBALT_BLOCK.get()).noOcclusion()
                    .pushReaction(PushReaction.DESTROY).explosionResistance(0)));
    public static final DeferredBlock<TrapDoorBlock> COBALT_TRAPDOOR = BLOCKS.register("cobalt_trapdoor",
            () -> new TrapDoorBlock(BlockSetType.COPPER,
                    ofFullCopy(COBALT_BLOCK.get()).isValidSpawn(Blocks::never)
                            .explosionResistance(0).noOcclusion()));
    public static final DeferredBlock<PressurePlateBlock> COBALT_PRESSURE_PLATE = BLOCKS.register(
            "cobalt_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.COPPER, ofFullCopy(COBALT_BLOCK.get())
                    .noCollission().pushReaction(PushReaction.DESTROY).explosionResistance(0)));
    // #endregion Cobalt

    // #region Sulfur
    public static final DeferredBlock<Block> SULFUROUS_STONE = BLOCKS.registerBlock("sulfurous_stone", Block::new,
            Block.Properties.of().mapColor(MapColor.TERRACOTTA_YELLOW).strength(1.5F, 6.0F)
                    .requiresCorrectToolForDrops());
    public static final DeferredBlock<StairBlock> SULFUROUS_STONE_STAIRS = BLOCKS.register("sulfurous_stone_stairs",
            () -> new StairBlock(SULFUROUS_STONE.get().defaultBlockState(),
                    ofFullCopy(SULFUROUS_STONE.get())));
    public static final DeferredBlock<SlabBlock> SULFUROUS_STONE_SLAB = BLOCKS.register("sulfurous_stone_slab",
            () -> new SlabBlock(ofFullCopy(SULFUROUS_STONE.get())));
    public static final DeferredBlock<WallBlock> SULFUROUS_STONE_WALL = BLOCKS.register("sulfurous_stone_wall",
            () -> new WallBlock(ofFullCopy(SULFUROUS_STONE.get())));
    // #endregion Sulfur

    // #region Marble
    public static final DeferredBlock<Block> MARBLE = BLOCKS.registerBlock("marble", Block::new,
            Block.Properties.of().mapColor(MapColor.COLOR_PINK).strength(1.5F, 6F)
                    .requiresCorrectToolForDrops());
    public static final DeferredBlock<StairBlock> MARBLE_STAIRS = BLOCKS.register("marble_stairs",
            () -> new StairBlock(MARBLE.get().defaultBlockState(), ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<SlabBlock> MARBLE_SLAB = BLOCKS.register("marble_slab",
            () -> new SlabBlock(ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<WallBlock> MARBLE_WALL = BLOCKS.register("marble_wall",
            () -> new WallBlock(ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<Block> POLISHED_MARBLE = BLOCKS.register("polished_marble",
            () -> new Block(ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<StairBlock> POLISHED_MARBLE_STAIRS = BLOCKS.register("polished_marble_stairs",
            () -> new StairBlock(MARBLE.get().defaultBlockState(), ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<SlabBlock> POLISHED_MARBLE_SLAB = BLOCKS.register("polished_marble_slab",
            () -> new SlabBlock(ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<WallBlock> POLISHED_MARBLE_WALL = BLOCKS.register("polished_marble_wall",
            () -> new WallBlock(ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<Block> MARBLE_BRICKS = BLOCKS.register("marble_bricks",
            () -> new Block(ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<StairBlock> MARBLE_BRICK_STAIRS = BLOCKS.register("marble_brick_stairs",
            () -> new StairBlock(MARBLE.get().defaultBlockState(), ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<SlabBlock> MARBLE_BRICK_SLAB = BLOCKS.register("marble_brick_slab",
            () -> new SlabBlock(ofFullCopy(MARBLE.get())));
    public static final DeferredBlock<WallBlock> MARBLE_BRICK_WALL = BLOCKS.register("marble_brick_wall",
            () -> new WallBlock(ofFullCopy(MARBLE.get())));
    // #endregion Marble

    // #region Zillium
    public static final DeferredBlock<Block> ZILLIUM_BRICKS = BLOCKS.registerBlock("zillium_bricks", Block::new,
            Block.Properties.of().mapColor(MapColor.COLOR_LIGHT_GREEN).strength(2.5F, 10F)
                    .requiresCorrectToolForDrops());
    public static final DeferredBlock<StairBlock> ZILLIUM_BRICK_STAIRS = BLOCKS.register("zillium_brick_stairs",
            () -> new StairBlock(ZILLIUM_BRICKS.get().defaultBlockState(),
                    ofFullCopy(ZILLIUM_BRICKS.get())));
    public static final DeferredBlock<SlabBlock> ZILLIUM_BRICK_SLAB = BLOCKS.register("zillium_brick_slab",
            () -> new SlabBlock(ofFullCopy(ZILLIUM_BRICKS.get())));
    public static final DeferredBlock<WallBlock> ZILLIUM_BRICK_WALL = BLOCKS.register("zillium_brick_wall",
            () -> new WallBlock(ofFullCopy(ZILLIUM_BRICKS.get())));
    // #endregion Zillium
}
