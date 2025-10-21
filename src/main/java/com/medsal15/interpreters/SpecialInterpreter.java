package com.medsal15.interpreters;

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

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Catch-all meant for recipes that have only 1 output, even if they're marked
 * special
 */
public record SpecialInterpreter(GristSet.Immutable processCost, String option) implements RecipeInterpreter {
    public static final MapCodec<SpecialInterpreter> CODEC = RecordCodecBuilder
            .mapCodec(
                    inst -> inst
                            .group(GristSet.Codecs.MAP_CODEC.optionalFieldOf("process_cost", GristSet.EMPTY)
                                    .forGetter(SpecialInterpreter::processCost),
                                    Codec.STRING.optionalFieldOf("option", "")
                                            .forGetter(SpecialInterpreter::option))
                            .apply(inst, SpecialInterpreter::new));

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

    @SuppressWarnings("null")
    @Override
    public MutableGristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return null;
        MutableGristSet totalCost = MutableGristSet.newDefault();
        for (Ingredient ingredient : recipe.getIngredients()) {
            GristSet ingredientCost = callback.lookupCostFor(ingredient);
            if (ingredientCost == null)
                return null;
            else
                totalCost.add(ingredientCost);
        }

        totalCost.scale(1F / recipe.getResultItem(null).getCount(), false);

        return totalCost;
    }

    @Override
    public void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return;

        for (Ingredient ingredient : recipe.getIngredients()) {
            tracker.report(ingredient);
        }
    }
}
