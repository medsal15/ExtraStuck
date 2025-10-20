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
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.fml.ModList;

/**
 * Generic interpreter for create recipes that works for most of them
 *
 * Accepts an optional option that disables it
 *
 * @see {ItemApplicationInterpreter} for item application (e.g. deployers), as
 *      it supports keepHeldItem
 * @see {SpecialInterpreter} for mechanical crafting
 * @see {SequencedInterpreter} for sequenced assembly
 */
public record CreateBasicInterpreter(GristSet.Immutable processCost, String option) implements RecipeInterpreter {
    public static final MapCodec<CreateBasicInterpreter> CODEC = RecordCodecBuilder
            .mapCodec(
                    inst -> inst
                            .group(GristSet.Codecs.MAP_CODEC.optionalFieldOf("process_cost", GristSet.EMPTY)
                                    .forGetter(CreateBasicInterpreter::processCost),
                                    Codec.STRING.optionalFieldOf("option", "")
                                            .forGetter(CreateBasicInterpreter::option))
                            .apply(inst, CreateBasicInterpreter::new));

    @Override
    public MapCodec<? extends RecipeInterpreter> codec() {
        return CODEC;
    }

    @Override
    public List<Item> getOutputItems(Recipe<?> recipe) {
        return DefaultInterpreter.INSTANCE.getOutputItems(recipe);
    }

    @Override
    public GristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return null;
        if (!ModList.get().isLoaded("create")
                || !(recipe instanceof ProcessingRecipe))
            return null;

        ProcessingRecipe<?, ?> proc = (ProcessingRecipe<?, ?>) recipe;

        // No support for fluid outputs
        if (proc.getFluidIngredients().size() > 0 || proc.getFluidResults().size() > 0) {
            return null;
        }

        int count = 0;
        for (ItemStack stack : proc.getRollableResultsAsItemStacks()) {
            // Also no support for multiple output items
            if (stack.getItem() != output) {
                return null;
            }
            count += stack.getCount();
        }

        MutableGristSet totalCost = MutableGristSet.newDefault();
        totalCost.add(processCost);
        for (Ingredient ingredient : proc.getIngredients()) {
            GristSet cost = callback.lookupCostFor(ingredient);
            if (cost == null)
                return null;
            else
                totalCost.add(cost);
        }

        totalCost.scale(1F / count, false);

        return totalCost;
    }

    @Override
    public void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return;
        if (!ModList.get().isLoaded("create") || !(recipe instanceof ProcessingRecipe))
            return;

        ProcessingRecipe<?, ?> proc = (ProcessingRecipe<?, ?>) recipe;

        if (proc.getFluidIngredients().size() > 0 || proc.getFluidResults().size() > 0)
            return;

        for (Ingredient ingredient : proc.getIngredients()) {
            tracker.report(ingredient);
        }
    }
}
