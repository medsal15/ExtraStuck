package com.medsal15.blocks;

import static net.minecraft.world.level.block.state.BlockBehaviour.Properties.ofFullCopy;

import com.medsal15.ExtraStuck;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(ExtraStuck.MODID);

    public static final DeferredBlock<Block> GARNET_BRICKS = BLOCKS.registerBlock("garnet_bricks", Block::new,
            Block.Properties.of().mapColor(MapColor.TERRACOTTA_RED).strength(1.5F).sound(SoundType.AMETHYST)
                    .requiresCorrectToolForDrops());
    public static final DeferredBlock<StairBlock> GARNET_BRICK_STAIRS = BLOCKS.register("garnet_brick_stairs",
            () -> new StairBlock(ESBlocks.GARNET_BRICKS.get().defaultBlockState(), ofFullCopy(GARNET_BRICKS.get())));
    public static final DeferredBlock<SlabBlock> GARNET_BRICK_SLAB = BLOCKS.register("garnet_brick_slab",
            () -> new SlabBlock(ofFullCopy(GARNET_BRICKS.get())));
    public static final DeferredBlock<WallBlock> GARNET_BRICK_WALL = BLOCKS.register("garnet_brick_wall",
            () -> new WallBlock(ofFullCopy(GARNET_BRICKS.get())));
}
