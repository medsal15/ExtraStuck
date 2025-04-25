package com.medsal15.data;

import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ESBlockStateProvider extends BlockStateProvider {
    public ESBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExtraStuck.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlock(ESBlocks.CUT_GARNET.get());
        stairsBlock(ESBlocks.CUT_GARNET_STAIRS.get(), modLoc("block/cut_garnet"));
        slabBlock(ESBlocks.CUT_GARNET_SLAB.get(), modLoc("block/cut_garnet"), modLoc("block/cut_garnet"));
        wallBlock(ESBlocks.CUT_GARNET_WALL.get(), modLoc("block/cut_garnet"));
        simpleBlock(ESBlocks.GARNET_BRICKS.get());
        stairsBlock(ESBlocks.GARNET_BRICK_STAIRS.get(), modLoc("block/garnet_bricks"));
        slabBlock(ESBlocks.GARNET_BRICK_SLAB.get(), modLoc("block/garnet_bricks"), modLoc("block/garnet_bricks"));
        wallBlock(ESBlocks.GARNET_BRICK_WALL.get(), modLoc("block/garnet_bricks"));
        simpleBlock(ESBlocks.CHISELED_GARNET_BRICKS.get());

        simpleBlock(ESBlocks.CUT_RUBY.get());
        stairsBlock(ESBlocks.CUT_RUBY_STAIRS.get(), modLoc("block/cut_ruby"));
        slabBlock(ESBlocks.CUT_RUBY_SLAB.get(), modLoc("block/cut_ruby"), modLoc("block/cut_ruby"));
        wallBlock(ESBlocks.CUT_RUBY_WALL.get(), modLoc("block/cut_ruby"));
        simpleBlock(ESBlocks.RUBY_BRICKS.get());
        stairsBlock(ESBlocks.RUBY_BRICK_STAIRS.get(), modLoc("block/ruby_bricks"));
        slabBlock(ESBlocks.RUBY_BRICK_SLAB.get(), modLoc("block/ruby_bricks"), modLoc("block/ruby_bricks"));
        wallBlock(ESBlocks.RUBY_BRICK_WALL.get(), modLoc("block/ruby_bricks"));
        simpleBlock(ESBlocks.CHISELED_RUBY_BRICKS.get());

        simpleBlock(ESBlocks.COBALT_BLOCK.get());
        paneBlock(ESBlocks.COBALT_BARS, modLoc("block/cobalt_bars"), modLoc("block/cobalt_bars"));
        doorBlock(ESBlocks.COBALT_DOOR, modLoc("block/cobalt_door_bottom"), modLoc("block/cobalt_door_top"));
        trapdoorBlock(ESBlocks.COBALT_TRAPDOOR, modLoc("block/cobalt_trapdoor"), true);
        pressurePlateBlock(ESBlocks.COBALT_PRESSURE_PLATE.get(), modLoc("block/cobalt_block"));

        simpleBlock(ESBlocks.SULFUROUS_STONE.get());
        stairsBlock(ESBlocks.SULFUROUS_STONE_STAIRS.get(), modLoc("block/sulfurous_stone"));
        slabBlock(ESBlocks.SULFUROUS_STONE_SLAB.get(), modLoc("block/sulfurous_stone"),
                modLoc("block/sulfurous_stone"));
        wallBlock(ESBlocks.SULFUROUS_STONE_WALL.get(), modLoc("block/sulfurous_stone"));

        simpleBlock(ESBlocks.MARBLE.get());
        stairsBlock(ESBlocks.MARBLE_STAIRS.get(), modLoc("block/marble"));
        slabBlock(ESBlocks.MARBLE_SLAB.get(), modLoc("block/marble"), modLoc("block/marble"));
        wallBlock(ESBlocks.MARBLE_WALL.get(), modLoc("block/marble"));
        simpleBlock(ESBlocks.POLISHED_MARBLE.get());
        stairsBlock(ESBlocks.POLISHED_MARBLE_STAIRS.get(), modLoc("block/polished_marble"));
        slabBlock(ESBlocks.POLISHED_MARBLE_SLAB.get(), modLoc("block/polished_marble"),
                modLoc("block/polished_marble"));
        wallBlock(ESBlocks.POLISHED_MARBLE_WALL.get(), modLoc("block/polished_marble"));
        simpleBlock(ESBlocks.MARBLE_BRICKS.get());
        stairsBlock(ESBlocks.MARBLE_BRICK_STAIRS.get(), modLoc("block/marble_bricks"));
        slabBlock(ESBlocks.MARBLE_BRICK_SLAB.get(), modLoc("block/marble_bricks"), modLoc("block/marble_bricks"));
        wallBlock(ESBlocks.MARBLE_BRICK_WALL.get(), modLoc("block/marble_bricks"));

        simpleBlock(ESBlocks.ZILLIUM_BRICKS.get());
        stairsBlock(ESBlocks.ZILLIUM_BRICK_STAIRS.get(), modLoc("block/zillium_bricks"));
        slabBlock(ESBlocks.ZILLIUM_BRICK_SLAB.get(), modLoc("block/zillium_bricks"), modLoc("block/zillium_bricks"));
        wallBlock(ESBlocks.ZILLIUM_BRICK_WALL.get(), modLoc("block/zillium_bricks"));
    }

    // Replacement for paneBlock that marks the models as transparent
    public void paneBlock(DeferredBlock<IronBarsBlock> block, ResourceLocation pane, ResourceLocation edge) {
        var baseName = block.getId().toString();
        var post = models().panePost(baseName + "_post", pane, edge).renderType("translucent");
        var side = this.models().paneSide(baseName + "_side", pane, edge).renderType("translucent");
        var sideAlt = this.models().paneSideAlt(baseName + "_side_alt", pane, edge).renderType("translucent");
        var noSide = this.models().paneNoSide(baseName + "_noside", pane).renderType("translucent");
        var noSideAlt = this.models().paneNoSideAlt(baseName + "_noside_alt", pane).renderType("translucent");
        paneBlock(block.get(), post, side, sideAlt, noSide, noSideAlt);
    }

    // Replacement for doorBlock that marks the models as transparent
    public void doorBlock(DeferredBlock<DoorBlock> block, ResourceLocation bottom, ResourceLocation top) {
        var baseName = block.getId().toString();
        var bottomLeft = this.models().doorBottomLeft(baseName + "_bottom_left", bottom, top).renderType("translucent");
        var bottomLeftOpen = this.models().doorBottomLeftOpen(baseName + "_bottom_left_open", bottom, top)
                .renderType("translucent");
        var bottomRight = this.models().doorBottomRight(baseName + "_bottom_right", bottom, top)
                .renderType("translucent");
        var bottomRightOpen = this.models().doorBottomRightOpen(baseName + "_bottom_right_open", bottom, top)
                .renderType("translucent");
        var topLeft = this.models().doorTopLeft(baseName + "_top_left", bottom, top).renderType("translucent");
        var topLeftOpen = this.models().doorTopLeftOpen(baseName + "_top_left_open", bottom, top)
                .renderType("translucent");
        var topRight = this.models().doorTopRight(baseName + "_top_right", bottom, top).renderType("translucent");
        var topRightOpen = this.models().doorTopRightOpen(baseName + "_top_right_open", bottom, top)
                .renderType("translucent");
        doorBlock(block.get(), bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight,
                topRightOpen);
    }

    // Replacement for trapdoorBlock that marks the models as transparent
    public void trapdoorBlock(DeferredBlock<TrapDoorBlock> block, ResourceLocation texture, boolean orientable) {
        var baseName = block.getId().toString();
        var bottom = orientable
                ? this.models().trapdoorOrientableBottom(baseName + "_bottom", texture).renderType("translucent")
                : this.models().trapdoorBottom(baseName + "_bottom", texture).renderType("translucent");
        var top = orientable
                ? this.models().trapdoorOrientableTop(baseName + "_top", texture).renderType("translucent")
                : this.models().trapdoorTop(baseName + "_top", texture).renderType("translucent");
        var open = orientable
                ? this.models().trapdoorOrientableOpen(baseName + "_open", texture).renderType("translucent")
                : this.models().trapdoorOpen(baseName + "_open", texture).renderType("translucent");
        trapdoorBlock(block.get(), bottom, top, open, orientable);
    }
}
