package com.medsal15.interpreters.create;

import java.util.List;

import com.mojang.serialization.MapCodec;
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

public enum ItemApplicationInterpreter implements RecipeInterpreter {
    INSTANCE;

    public static final MapCodec<ItemApplicationInterpreter> CODEC = MapCodec.unit(INSTANCE);

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
        DefaultInterpreter.INSTANCE.reportPreliminaryLookups(recipe, tracker);
    }
}
