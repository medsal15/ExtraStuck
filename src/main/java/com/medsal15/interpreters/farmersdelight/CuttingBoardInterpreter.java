package com.medsal15.interpreters.farmersdelight;

import java.util.List;

import com.medsal15.config.ConfigCommon;
import com.medsal15.data.ESItemTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.DefaultInterpreter;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.RecipeInterpreter;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.generator.GeneratorCallback;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.neoforged.fml.ModList;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;

public record CuttingBoardInterpreter(String option) implements RecipeInterpreter {
    public static final MapCodec<CuttingBoardInterpreter> CODEC = RecordCodecBuilder
            .mapCodec(
                    inst -> inst
                            .group(Codec.STRING.optionalFieldOf("option", "")
                                    .forGetter(CuttingBoardInterpreter::option))
                            .apply(inst, CuttingBoardInterpreter::new));

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
        if (!ConfigCommon.configEnabled(option) || !ModList.get().isLoaded("farmersdelight")
                || !(recipe instanceof CuttingBoardRecipe cut))
            return null;

        int count = 0;
        for (ItemStack stack : cut.getResults()) {
            if (stack.getItem() != output) {
                if (stack.is(ESItemTags.IGNORE_BYPRODUCT_CUTTING))
                    continue;
                return null;
            }
            count += stack.getCount();
        }

        MutableGristSet totalCost = MutableGristSet.newDefault();
        for (Ingredient ingredient : cut.getIngredients()) {
            GristSet cost = callback.lookupCostFor(ingredient);
            if (cost == null)
                return null;
            totalCost.add(cost);
        }

        return totalCost.scale(1F / count, false);
    }
}
