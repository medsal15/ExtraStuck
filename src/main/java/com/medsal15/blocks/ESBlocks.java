package com.medsal15.blocks;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

import com.medsal15.ExtraStuck;

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
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExtraStuck.MODID);

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
                    ofFullCopy(COBALT_BLOCK.get()).isValidSpawn(Blocks::never).explosionResistance(0).noOcclusion()));
    public static final DeferredBlock<PressurePlateBlock> COBALT_PRESSURE_PLATE = BLOCKS.register(
            "cobalt_pressure_plate",
            () -> new PressurePlateBlock(BlockSetType.COPPER, ofFullCopy(COBALT_BLOCK.get())
                    .noCollission().pushReaction(PushReaction.DESTROY).explosionResistance(0)));
}
