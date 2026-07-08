package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.blocks.ESBlocks;
import com.medsal15.datamaps.ReactorFuel;
import com.medsal15.datamaps.Zilliable;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.item.MSItems;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.DataMapProvider;
import net.neoforged.neoforge.registries.datamaps.builtin.FurnaceFuel;
import net.neoforged.neoforge.registries.datamaps.builtin.NeoForgeDataMaps;
import net.neoforged.neoforge.registries.datamaps.builtin.Waxable;

public final class DataMapGenerator extends DataMapProvider {
    public DataMapGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider);
    }

    @Override
    protected void gather(@Nonnull HolderLookup.Provider provider) {
        builder(NeoForgeDataMaps.FURNACE_FUELS)
                .add(ESItems.WOODEN_SHIELD, new FurnaceFuel(200), false)
                .add(ESItems.ROLLING_PIN, new FurnaceFuel(200), false);

        builder(ReactorFuel.REACTOR_MAP)
                .add(MSItems.ENERGY_CORE, new ReactorFuel(128, ESItems.EMPTY_ENERGY_CORE.toStack()), false)
                .add(MSItems.PLUSH_MUTATED_CAT, new ReactorFuel(0, ESItems.NORMAL_CAT_PLUSH.toStack()), false);

        builder(Zilliable.ZILLIABLES)
                .add(ESBlocks.GREEN_ZILLIUM_BRICKS, new Zilliable(ESBlocks.BLUE_ZILLIUM_BRICKS.get()), false)
                .add(ESBlocks.GREEN_ZILLIUM_BRICK_STAIRS, new Zilliable(ESBlocks.BLUE_ZILLIUM_BRICK_STAIRS.get()),
                        false)
                .add(ESBlocks.GREEN_ZILLIUM_BRICK_SLAB, new Zilliable(ESBlocks.BLUE_ZILLIUM_BRICK_SLAB.get()), false)
                .add(ESBlocks.GREEN_ZILLIUM_BRICK_WALL, new Zilliable(ESBlocks.BLUE_ZILLIUM_BRICK_WALL.get()), false)
                .add(ESBlocks.BLUE_ZILLIUM_BRICKS, new Zilliable(ESBlocks.PINK_ZILLIUM_BRICKS.get()), false)
                .add(ESBlocks.BLUE_ZILLIUM_BRICK_STAIRS, new Zilliable(ESBlocks.PINK_ZILLIUM_BRICK_STAIRS.get()), false)
                .add(ESBlocks.BLUE_ZILLIUM_BRICK_SLAB, new Zilliable(ESBlocks.PINK_ZILLIUM_BRICK_SLAB.get()), false)
                .add(ESBlocks.BLUE_ZILLIUM_BRICK_WALL, new Zilliable(ESBlocks.PINK_ZILLIUM_BRICK_WALL.get()), false)
                .add(ESBlocks.PINK_ZILLIUM_BRICKS, new Zilliable(ESBlocks.SECONDARY_ZILLIUM_BRICKS.get()), false)
                .add(ESBlocks.PINK_ZILLIUM_BRICK_STAIRS, new Zilliable(ESBlocks.SECONDARY_ZILLIUM_BRICK_STAIRS.get()),
                        false)
                .add(ESBlocks.PINK_ZILLIUM_BRICK_SLAB, new Zilliable(ESBlocks.SECONDARY_ZILLIUM_BRICK_SLAB.get()),
                        false)
                .add(ESBlocks.PINK_ZILLIUM_BRICK_WALL, new Zilliable(ESBlocks.SECONDARY_ZILLIUM_BRICK_WALL.get()),
                        false)
                .add(ESBlocks.SECONDARY_ZILLIUM_BRICKS, new Zilliable(ESBlocks.BLUE_ZILLIUM_BRICKS.get()), false)
                .add(ESBlocks.SECONDARY_ZILLIUM_BRICK_STAIRS, new Zilliable(ESBlocks.BLUE_ZILLIUM_BRICK_STAIRS.get()),
                        false)
                .add(ESBlocks.SECONDARY_ZILLIUM_BRICK_SLAB, new Zilliable(ESBlocks.BLUE_ZILLIUM_BRICK_SLAB.get()),
                        false)
                .add(ESBlocks.SECONDARY_ZILLIUM_BRICK_WALL, new Zilliable(ESBlocks.BLUE_ZILLIUM_BRICK_WALL.get()),
                        false);

        builder(NeoForgeDataMaps.WAXABLES)
                .add(ESBlocks.GREEN_ZILLIUM_BRICKS, new Waxable(ESBlocks.WAXED_GREEN_ZILLIUM_BRICKS.get()), false)
                .add(ESBlocks.GREEN_ZILLIUM_BRICK_STAIRS, new Waxable(ESBlocks.WAXED_GREEN_ZILLIUM_BRICK_STAIRS.get()),
                        false)
                .add(ESBlocks.GREEN_ZILLIUM_BRICK_SLAB, new Waxable(ESBlocks.WAXED_GREEN_ZILLIUM_BRICK_SLAB.get()),
                        false)
                .add(ESBlocks.GREEN_ZILLIUM_BRICK_WALL, new Waxable(ESBlocks.WAXED_GREEN_ZILLIUM_BRICK_WALL.get()),
                        false)
                .add(ESBlocks.BLUE_ZILLIUM_BRICKS, new Waxable(ESBlocks.WAXED_BLUE_ZILLIUM_BRICKS.get()), false)
                .add(ESBlocks.BLUE_ZILLIUM_BRICK_STAIRS, new Waxable(ESBlocks.WAXED_BLUE_ZILLIUM_BRICK_STAIRS.get()),
                        false)
                .add(ESBlocks.BLUE_ZILLIUM_BRICK_SLAB, new Waxable(ESBlocks.WAXED_BLUE_ZILLIUM_BRICK_SLAB.get()), false)
                .add(ESBlocks.BLUE_ZILLIUM_BRICK_WALL, new Waxable(ESBlocks.WAXED_BLUE_ZILLIUM_BRICK_WALL.get()),
                        false)
                .add(ESBlocks.PINK_ZILLIUM_BRICKS, new Waxable(ESBlocks.WAXED_PINK_ZILLIUM_BRICKS.get()), false)
                .add(ESBlocks.PINK_ZILLIUM_BRICK_STAIRS, new Waxable(ESBlocks.WAXED_PINK_ZILLIUM_BRICK_STAIRS.get()),
                        false)
                .add(ESBlocks.PINK_ZILLIUM_BRICK_SLAB, new Waxable(ESBlocks.WAXED_PINK_ZILLIUM_BRICK_SLAB.get()), false)
                .add(ESBlocks.PINK_ZILLIUM_BRICK_WALL, new Waxable(ESBlocks.WAXED_PINK_ZILLIUM_BRICK_WALL.get()),
                        false)
                .add(ESBlocks.SECONDARY_ZILLIUM_BRICKS, new Waxable(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICKS.get()),
                        false)
                .add(ESBlocks.SECONDARY_ZILLIUM_BRICK_STAIRS,
                        new Waxable(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICK_STAIRS.get()), false)
                .add(ESBlocks.SECONDARY_ZILLIUM_BRICK_SLAB,
                        new Waxable(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICK_SLAB.get()), false)
                .add(ESBlocks.SECONDARY_ZILLIUM_BRICK_WALL,
                        new Waxable(ESBlocks.WAXED_SECONDARY_ZILLIUM_BRICK_WALL.get()), false);
    }
}
