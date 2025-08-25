package com.medsal15.interpreters;

import java.util.List;

import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.DefaultInterpreter;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.RecipeInterpreter;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;

/**
 * Catch-all meant for recipes that have only 1 output, even if they're marked
 * special
 */
public enum SpecialInterpreter implements RecipeInterpreter {
    INSTANCE;

    public static final MapCodec<SpecialInterpreter> CODEC = MapCodec.unit(INSTANCE);

    @Override
    public MapCodec<? extends RecipeInterpreter> codec() {
        return CODEC;
    }

    @Override
    public List<Item> getOutputItems(Recipe<?> recipe) {
        return DefaultInterpreter.INSTANCE.getOutputItems(recipe);
    }

    @SuppressWarnings("null")
    @Override
    public MutableGristSet generateCost(Recipe<?> recipe, Item output, GeneratorCallback callback) {
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
}
