package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ESItems;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipeBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;

public class ESRecipeProvider extends RecipeProvider {
    public ESRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput output) {
        RecipeOutput msOutput = output.withConditions(new ICondition[] { new ModLoadedCondition("minestuck") });

        CombinationRecipeBuilder.of(ESItems.WOODEN_SHIELD)
                .input(Items.SHIELD).and().input(ItemTags.PLANKS)
                .build(msOutput);

        GristCostRecipeBuilder.of(ESItems.WOODEN_SHIELD)
                .grist(GristTypes.BUILD, 6)
                .build(msOutput);
    }
}
