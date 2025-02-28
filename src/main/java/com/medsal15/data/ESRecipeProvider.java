package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ESItems;
import com.medsal15.ExtraStuck;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipeBuilder;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
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
                                .input(Tags.Items.RODS_WOODEN).and().input(ItemTags.WOODEN_PRESSURE_PLATES)
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.WOODEN_SHIELD)
                                .grist(GristTypes.BUILD, 6)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.THORN_SHIELD)
                                .input(ESItems.WOODEN_SHIELD).and().input(Items.ROSE_BUSH)
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.THORN_SHIELD)
                                .grist(GristTypes.BUILD, 24).grist(GristTypes.GARNET, 6).grist(GristTypes.IODINE, 8)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.FLAME_SHIELD)
                                .input(ESItems.WOODEN_SHIELD).and()
                                .input(Ingredient.of(Items.FLINT_AND_STEEL, Items.FIRE_CHARGE))
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.FLAME_SHIELD)
                                .grist(GristTypes.TAR, 73).grist(GristTypes.GOLD, 13)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.WITHERED_SHIELD)
                                .input(ESItems.THORN_SHIELD).or().input(Items.WITHER_ROSE)
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.WITHERED_SHIELD)
                                .grist(GristTypes.URANIUM, 25).grist(GristTypes.TAR, 140).grist(GristTypes.CAULK, 236)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.GLASS_SHIELD)
                                .input(Items.SHIELD).or().input(Ingredient.of(Tags.Items.GLASS_BLOCKS_COLORLESS))
                                .build(msOutput);
                CombinationRecipeBuilder.of(ESItems.GLASS_SHIELD)
                                .input(Items.SHIELD).or().input(Ingredient.of(Tags.Items.GLASS_PANES_COLORLESS))
                                .build(msOutput, ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID,
                                                ESItems.GLASS_SHIELD.getId().getPath() + "_alt"));
                GristCostRecipeBuilder.of(ESItems.GLASS_SHIELD)
                                .grist(GristTypes.BUILD, 10).grist(GristTypes.QUARTZ, 1)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.REINFORCED_GLASS_SHIELD)
                                .input(ESItems.GLASS_SHIELD).and().input(Items.IRON_BARS)
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.REINFORCED_GLASS_SHIELD)
                                .grist(GristTypes.BUILD, 10).grist(GristTypes.QUARTZ, 15).grist(GristTypes.RUST, 5)
                                .build(msOutput);
        }
}
