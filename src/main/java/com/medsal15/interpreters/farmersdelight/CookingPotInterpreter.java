package com.medsal15.interpreters.farmersdelight;

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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.fml.ModList;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public record CookingPotInterpreter(GristSet.Immutable cookingCost, String option) implements RecipeInterpreter {
    public static final MapCodec<CookingPotInterpreter> CODEC = RecordCodecBuilder
            .mapCodec(
                    inst -> inst
                            .group(GristSet.Codecs.MAP_CODEC.optionalFieldOf("cooking_cost", GristSet.EMPTY)
                                    .forGetter(CookingPotInterpreter::cookingCost),
                                    Codec.STRING.optionalFieldOf("option", "").forGetter(CookingPotInterpreter::option))
                            .apply(inst, CookingPotInterpreter::new));

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
        if (!ModList.get().isLoaded("farmersdelight") || !(recipe instanceof CookingPotRecipe cook))
            return null;

        MutableGristSet totalCost = MutableGristSet.newDefault();
        totalCost.add(cookingCost);
        for (Ingredient ingredient : cook.getIngredients()) {
            GristSet cost = callback.lookupCostFor(ingredient);
            if (cost == null)
                return null;
            totalCost.add(cost);
        }

        @SuppressWarnings("null")
        int count = cook.getResultItem(null).getCount();
        if (!cook.getContainerOverride().isEmpty()) {
            ItemStack override = cook.getContainerOverride();
            GristSet cost = callback.lookupCostFor(override);
            if (cost == null)
                return null;
            totalCost.add(cost.mutableCopy().scale(count));
        } else if (!cook.getOutputContainer().isEmpty()) {
            ItemStack container = cook.getOutputContainer();
            GristSet cost = callback.lookupCostFor(container);
            if (cost == null)
                return null;
            totalCost.add(cost.mutableCopy().scale(count));
        }

        return totalCost.scale(1F / count, false);
    }

    @Override
    public void reportPreliminaryLookups(Recipe<?> recipe, LookupTracker tracker) {
        if (option != "" && !ConfigCommon.configEnabled(option))
            return;
        if (!ModList.get().isLoaded("farmersdelight") || !(recipe instanceof CookingPotRecipe cook))
            return;

        for (Ingredient ingredient : cook.getIngredients()) {
            tracker.report(ingredient);
        }

        if (!cook.getContainerOverride().isEmpty()) {
            tracker.report(cook.getContainerOverride());
        } else if (!cook.getOutputContainer().isEmpty()) {
            tracker.report(cook.getOutputContainer());
        }
    }
}
