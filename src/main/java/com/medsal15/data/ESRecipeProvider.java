package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipeBuilder;
import com.mraof.minestuck.item.MSItems;

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

        CombinationRecipeBuilder.of(ESItems.LIGHT_SHIELD)
                .input(ESItems.FLAME_SHIELD).and().input(Items.BEACON)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.LIGHT_SHIELD)
                .grist(GristTypes.URANIUM, 444).grist(GristTypes.QUARTZ, 345)
                .grist(GristTypes.DIAMOND, 246)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.NON_CONTACT_CONTRACT)
                .input(ESItems.WOODEN_SHIELD).and().input(Items.PAPER)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.NON_CONTACT_CONTRACT)
                .grist(GristTypes.CHALK, 202).grist(GristTypes.TAR, 20)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.SLIED)
                .input(ESItems.NON_CONTACT_CONTRACT).and().input(MSItems.SBAHJ_POSTER)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.SLIED)
                .grist(GristTypes.ARTIFACT, -5)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.RIOT_SHIELD)
                .input(ESItems.NON_CONTACT_CONTRACT).or()
                .input(Ingredient.of(MSItems.ROCKEFELLERS_WALKING_BLADECANE,
                        MSItems.ROCKEFELLERS_WALKING_BLADECANE_SHEATHED))
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.RIOT_SHIELD)
                .grist(GristTypes.TAR, 197).grist(GristTypes.RUST, 228)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD)
                .input(ESItems.RIOT_SHIELD).and().input(Items.TRIDENT)
                .build(msOutput);
        GristCostRecipeBuilder
                .of(Ingredient.of(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD,
                        ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE))
                .grist(GristTypes.COBALT, 827).grist(GristTypes.BUILD, 256).grist(GristTypes.GOLD, 433)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.CAPITASHIELD)
                .input(ESItems.GOLD_SHIELD).and().input(ESItems.NON_CONTACT_CONTRACT)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.CAPITASHIELD)
                .grist(GristTypes.GOLD, 567).grist(GristTypes.DIAMOND, 246)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.IRON_SHIELD)
                .input(Items.SHIELD).and().input(Tags.Items.INGOTS_IRON)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.IRON_SHIELD)
                .grist(GristTypes.RUST, 63).grist(GristTypes.MERCURY, 17)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.SPIKES_ON_A_SLAB)
                .input(ESItems.IRON_SHIELD).or().input(MSItems.SPIKES)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.SPIKES_ON_A_SLAB)
                .grist(GristTypes.RUST, 122).grist(GristTypes.GARNET, 21)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.JAWBITER)
                .input(ESItems.SPIKES_ON_A_SLAB).and().input(MSItems.CANDY_CORN)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.JAWBITER)
                .grist(GristTypes.MARBLE, 83).grist(GristTypes.IODINE, 52)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.ELDRITCH_SHIELD)
                .input(ESItems.JAWBITER).or().input(MSItems.GRIMOIRE)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.ELDRITCH_SHIELD)
                .grist(GristTypes.CHALK, 1666).grist(GristTypes.GARNET, 999).grist(GristTypes.TAR, 777)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.GOLD_SHIELD)
                .input(ESItems.IRON_SHIELD).and().input(Tags.Items.INGOTS_GOLD)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.GOLD_SHIELD)
                .grist(GristTypes.GOLD, 253).grist(GristTypes.CAULK, 153)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.FLUX_SHIELD)
                .input(ESItems.GOLD_SHIELD).or().input(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.FLUX_SHIELD)
                .grist(GristTypes.GOLD, 593).grist(GristTypes.GARNET, 231)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.DIAMOND_SHIELD)
                .input(ESItems.GOLD_SHIELD).or().input(Tags.Items.GEMS_DIAMOND)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.DIAMOND_SHIELD)
                .grist(GristTypes.DIAMOND, 768).grist(GristTypes.AMETHYST, 256)
                .build(msOutput);

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(ESItems.DIAMOND_SHIELD), Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.COMBAT,
                ESItems.NETHERITE_SHIELD.get())
                .unlocks("netherite_shield", has(ESItems.DIAMOND_SHIELD))
                .save(output, ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID,
                        "smithing/netherite_shield"));

        CombinationRecipeBuilder.of(ESItems.GARNET_SHIELD)
                .input(ESItems.DIAMOND_SHIELD).and().input(MSItems.FROG)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.GARNET_SHIELD)
                .grist(GristTypes.GARNET, 1500).grist(GristTypes.DIAMOND, 1000)
                .grist(GristTypes.RUBY, 444)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.POGO_SHIELD)
                .input(ESItems.IRON_SHIELD).and().input(Items.SLIME_BALL)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.POGO_SHIELD)
                .grist(GristTypes.RUST, 125).grist(GristTypes.SHALE, 55)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.RETURN_TO_SENDER)
                .input(ESItems.POGO_SHIELD).or().input(MSItems.MAILBOX)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.RETURN_TO_SENDER)
                .grist(GristTypes.MARBLE, 868).grist(GristTypes.CAULK, 133)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.NETHER_ARROW)
                .input(Items.ARROW).and().input(Items.NETHERRACK)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.NETHER_ARROW)
                .grist(GristTypes.SULFUR, 12).grist(GristTypes.RUST, 8)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.FLAME_ARROW)
                .input(ESItems.NETHER_ARROW).and().input(Ingredient.of(Items.BLAZE_ROD, Items.BLAZE_POWDER))
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.FLAME_ARROW)
                .grist(GristTypes.SULFUR, 8).grist(GristTypes.TAR, 4)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.CARDBOARD_ARROW)
                .input(MSItems.CARDBOARD_TUBE).and().input(Items.FLINT)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.CARDBOARD_ARROW)
                .grist(GristTypes.BUILD, 1)
                .build(msOutput);

        CombinationRecipeBuilder.of(ESItems.MISSED_YOU)
                .input(ESItems.CARDBOARD_ARROW).or().input(MSItems.SBAHJ_POSTER)
                .build(msOutput);
        GristCostRecipeBuilder.of(ESItems.MISSED_YOU)
                .grist(GristTypes.ARTIFACT, 1)
                .build(msOutput);
    }
}
