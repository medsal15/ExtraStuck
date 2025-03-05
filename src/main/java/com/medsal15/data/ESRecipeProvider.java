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
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
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

                GristCostRecipeBuilder.of(ESItems.FLAME_SHIELD)
                                .grist(GristTypes.TAR, 34).grist(GristTypes.SULFUR, 13)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.HALT_SHIELD)
                                .input(ESItems.FLAME_SHIELD).or().input(ItemTags.WOODEN_DOORS)
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.HALT_SHIELD)
                                .grist(GristTypes.TAR, 187).grist(GristTypes.DIAMOND, 60)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.IRON_SHIELD)
                                .input(Items.SHIELD).and().input(Tags.Items.INGOTS_IRON)
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.IRON_SHIELD)
                                .grist(GristTypes.RUST, 63).grist(GristTypes.MERCURY, 17)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.GOLD_SHIELD)
                                .input(ESItems.IRON_SHIELD).and().input(Tags.Items.INGOTS_GOLD)
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.GOLD_SHIELD)
                                .grist(GristTypes.GOLD, 253).grist(GristTypes.CAULK, 153)
                                .build(msOutput);

                CombinationRecipeBuilder.of(ESItems.DIAMOND_SHIELD)
                                .input(ESItems.GOLD_SHIELD).or().input(Tags.Items.GEMS_DIAMOND)
                                .build(msOutput);
                GristCostRecipeBuilder.of(ESItems.DIAMOND_SHIELD)
                                .grist(GristTypes.DIAMOND, 512).grist(GristTypes.AMETHYST, 256)
                                .build(msOutput);

                SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                                Ingredient.of(ESItems.DIAMOND_SHIELD), Ingredient.of(Items.NETHERITE_INGOT),
                                RecipeCategory.COMBAT,
                                ESItems.NETHERITE_SHIELD.get())
                                .unlocks("netherite_shield", has(ESItems.DIAMOND_SHIELD))
                                .save(output, ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID,
                                                "smithing/netherite_shield"));
        }
}
