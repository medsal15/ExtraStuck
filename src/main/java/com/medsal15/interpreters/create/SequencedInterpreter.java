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
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedRecipe;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.fml.ModList;

public record SequencedInterpreter(String option) implements RecipeInterpreter {
    public static final MapCodec<SequencedInterpreter> CODEC = RecordCodecBuilder
            .mapCodec(
                    inst -> inst
                            .group(Codec.STRING.optionalFieldOf("option", "")
                                    .forGetter(SequencedInterpreter::option))
                            .apply(inst, SequencedInterpreter::new));

    @Override
    public MapCodec<? extends RecipeInterpreter> codec() {
        return CODEC;
    }

    @Override
    public List<Item> getOutputItems(Recipe<?> recipe) {
        return DefaultInterpreter.INSTANCE.getOutputItems(recipe);
    }

    @Override
    public GristSet generateCost(Recipe<?> recipe, Item item, GeneratorCallback callback) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return null;
        if (!ModList.get().isLoaded("create") || !(recipe instanceof SequencedAssemblyRecipe))
            return null;

        SequencedAssemblyRecipe assembly = (SequencedAssemblyRecipe) recipe;

        MutableGristSet totalCost = MutableGristSet.newDefault();

        GristSet base = callback.lookupCostFor(assembly.getIngredient());
        if (base == null)
            return null;
        totalCost.add(base);

        int loops = assembly.getLoops();
        Item transitional = assembly.getTransitionalItem().getItem();

        for (SequencedRecipe<?> step : assembly.getSequence()) {
            Recipe<?> sub = step.getRecipe();
            GristSet cost = null;
            if (sub instanceof ItemApplicationRecipe application) {
                cost = applicationCost(application, callback);
            } else if (sub instanceof ProcessingRecipe processing) {
                if (processing.getFluidIngredients().size() > 0)
                    return null;
                cost = processingCost(processing, callback, transitional);
            }
            if (cost == null)
                return null;
            totalCost.add(cost.mutableCopy().scale(loops));
        }

        return totalCost.scale(1F / assembly.getResultItem(null).getCount(), false);
    }

    private GristSet applicationCost(ItemApplicationRecipe recipe, GeneratorCallback callback) {
        if (recipe.shouldKeepHeldItem())
            return GristSet.EMPTY;
        return callback.lookupCostFor(recipe.getRequiredHeldItem());
    }

    private GristSet processingCost(ProcessingRecipe<?, ?> recipe, GeneratorCallback callback, Item transitional) {
        MutableGristSet totalCost = MutableGristSet.newDefault();

        if (recipe.getFluidIngredients().size() > 0 || recipe.getFluidResults().size() > 0)
            return null;

        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.test(transitional.getDefaultInstance()))
                continue;

            GristSet cost = callback.lookupCostFor(ingredient);
            if (cost == null)
                return null;
            else
                totalCost.add(cost);
        }

        return totalCost;
    }

    @Override
    public void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return;
        if (!ModList.get().isLoaded("create") || !(recipe instanceof SequencedAssemblyRecipe))
            return;

        SequencedAssemblyRecipe assembly = (SequencedAssemblyRecipe) recipe;

        tracker.report(assembly.getIngredient());

        for (SequencedRecipe<?> step : assembly.getSequence()) {
            Recipe<?> sub = step.getRecipe();
            if (sub instanceof ItemApplicationRecipe application) {
                reportApplication(application, tracker);
            } else if (sub instanceof ProcessingRecipe processing) {
                if (processing.getFluidIngredients().size() > 0)
                    return;
                reportProcessing(processing, tracker, assembly.getTransitionalItem().getItem());
            }
        }
    }

    private void reportApplication(ItemApplicationRecipe recipe, LookupTracker tracker) {
        tracker.report(recipe.getProcessedItem());

        if (!recipe.shouldKeepHeldItem())
            tracker.report(recipe.getRequiredHeldItem());
    }

    private void reportProcessing(ProcessingRecipe<?, ?> recipe, LookupTracker tracker, Item transitional) {
        if (recipe.getFluidIngredients().size() > 0 || recipe.getFluidResults().size() > 0)
            return;

        for (Ingredient ingredient : recipe.getIngredients()) {
            if (ingredient.test(transitional.getDefaultInstance()))
                continue;

            tracker.report(ingredient);
        }
    }
}
