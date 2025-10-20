package com.medsal15.interpreters.create;

import java.util.List;

import com.medsal15.config.ConfigCommon;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.DefaultInterpreter;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.RecipeInterpreter;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;
import com.mraof.minestuck.api.alchemy.recipe.generator.LookupTracker;
import com.simibubi.create.content.kinetics.deployer.ItemApplicationRecipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.fml.ModList;

public record ItemApplicationInterpreter(String option) implements RecipeInterpreter {
    public static final MapCodec<ItemApplicationInterpreter> CODEC = RecordCodecBuilder
            .mapCodec(
                    inst -> inst
                            .group(Codec.STRING.optionalFieldOf("option", "")
                                    .forGetter(ItemApplicationInterpreter::option))
                            .apply(inst, ItemApplicationInterpreter::new));

    @Override
    public MapCodec<? extends RecipeInterpreter> codec() {
        return CODEC;
    }

    @Override
    public List<Item> getOutputItems(Recipe<?> recipe) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return List.of();
        return DefaultInterpreter.INSTANCE.getOutputItems(recipe);
    }

    @Override
    public GristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return null;
        if (!ModList.get().isLoaded("create") || !(recipe instanceof ItemApplicationRecipe))
            return null;

        ItemApplicationRecipe apply = (ItemApplicationRecipe) recipe;

        int count = 0;
        for (ItemStack stack : apply.getRollableResultsAsItemStacks()) {
            // No support for multiple output types
            if (stack.getItem() != output) {
                return null;
            }
            count += stack.getCount();
        }

        MutableGristSet totalCost = MutableGristSet.newDefault();
        GristSet baseCost = callback.lookupCostFor(apply.getProcessedItem());
        if (baseCost == null) {
            return null;
        } else {
            totalCost.add(baseCost);
        }
        if (!apply.shouldKeepHeldItem()) {
            GristSet heldCost = callback.lookupCostFor(apply.getRequiredHeldItem());
            if (heldCost == null) {
                return null;
            } else {
                totalCost.add(heldCost);
            }
        }

        totalCost.scale(1F / count, false);

        return totalCost;
    }

    @Override
    public void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return;
        if (!ModList.get().isLoaded("create") || !(recipe instanceof ItemApplicationRecipe))
            return;

        ItemApplicationRecipe apply = (ItemApplicationRecipe) recipe;

        tracker.report(apply.getProcessedItem());

        if (!apply.shouldKeepHeldItem())
            tracker.report(apply.getRequiredHeldItem());
    }
}
