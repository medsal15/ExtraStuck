package com.medsal15.data;

import static com.medsal15.ExtraStuck.modid;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.irons_spellbooks.items.ESISSItems;
import com.medsal15.conditions.ConfigCondition;
import com.medsal15.items.ESItems;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.mraof.minestuck.api.alchemy.recipe.SourceGristCostBuilder;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipeBuilder;
import com.mraof.minestuck.data.recipe.IrradiatingRecipeBuilder;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.deployer.DeployerApplicationRecipe;
import com.simibubi.create.content.kinetics.press.PressingRecipe;
import com.simibubi.create.content.processing.sequenced.SequencedAssemblyRecipeBuilder;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.common.conditions.ModLoadedCondition;
import net.neoforged.neoforge.common.conditions.NotCondition;
import net.neoforged.neoforge.common.conditions.TagEmptyCondition;
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

public final class ESRecipeProvider extends RecipeProvider {
    private static final ICondition CREATE_LOADED = new ModLoadedCondition("create");
    private static final ICondition FARMERSDELIGHT_LOADED = new ModLoadedCondition("farmersdelight");
    private static final ICondition ISS_LOADED = new ModLoadedCondition("irons_spellbooks");

    public ESRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(@Nonnull RecipeOutput output) {
        shieldRecipes(output);
        weaponRecipes(output);
        arrowRecipes(output);
        ammoRecipes(output);
        armorRecipes(output);
        modusRecipes(output);
        toolRecipes(output);
        foodRecipes(output);
        drinkRecipes(output);
        blockRecipes(output);

        issRecipes(output);

        CombinationRecipeBuilder.of(ESItems.GIFT)
                .input(Items.PAPER).and().input(MSItems.SURPRISE_EMBRYO)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GIFT)
                .grist(GristTypes.GARNET, 2).grist(GristTypes.GOLD, 12)
                .build(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ESItems.EMPTY_ENERGY_CORE.toStack())
                .pattern("G G")
                .pattern("PGP")
                .pattern("G G")
                .define('G', MSItems.RAW_CRUXITE)
                .define('P', Tags.Items.GLASS_PANES_COLORLESS)
                .unlockedBy("has_raw_cruxite", has(MSItems.RAW_CRUXITE))
                .save(output, modid("shaped/empty_energy_core"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, MSItems.ENERGY_CORE.toStack())
                .requires(ESItems.EMPTY_ENERGY_CORE)
                .requires(MSItems.RAW_URANIUM, 4)
                .unlockedBy("has_empty_energy_core", has(ESItems.EMPTY_ENERGY_CORE))
                .save(output, modid("shapeless/fill_energy_core"));

        CombinationRecipeBuilder.of(ESItems.BEE_LARVA)
                .input(Items.EGG).and().input(Items.BEE_NEST)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BEE_LARVA)
                .grist(GristTypes.AMBER, 4).grist(GristTypes.GOLD, 1).grist(GristTypes.TAR, 1)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .input(ESItems.SALESWOMAN_GLASSES).and().input(ESItems.SALESMAN_GOGGLES)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .grist(GristTypes.GOLD, 110).grist(GristTypes.DIAMOND, 10)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.COSMIC_PLAGUE_SPORE)
                .input(MSItems.FUNGAL_SPORE).or().input(Items.DRAGON_HEAD)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.COSMIC_PLAGUE_SPORE)
                .grist(GristTypes.SHALE, 66).grist(GristTypes.URANIUM, 23).grist(GristTypes.AMETHYST, 11)
                .build(output);
    }

    private void shieldRecipes(@Nonnull RecipeOutput output) {
        CombinationRecipeBuilder.of(ESItems.WOODEN_SHIELD)
                .input(Tags.Items.RODS_WOODEN).and().input(ItemTags.WOODEN_PRESSURE_PLATES)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.WOODEN_SHIELD)
                .grist(GristTypes.BUILD, 26)
                .build(output);

        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(ESItems.WOODEN_SHIELD), RecipeCategory.COMBAT,
                ESItems.FLAME_SHIELD, 0, 600)
                .unlockedBy("has_wooden_shield", has(ESItems.WOODEN_SHIELD))
                .save(output, modid("campfire/flame_shield"));
        GristCostRecipeBuilder.of(ESItems.FLAME_SHIELD)
                .grist(GristTypes.TAR, 13).grist(GristTypes.SULFUR, 8)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.HALT_SHIELD)
                .input(ESItems.FLAME_SHIELD).or().input(ItemTags.WOODEN_DOORS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.HALT_SHIELD)
                .grist(GristTypes.TAR, 187).grist(GristTypes.DIAMOND, 60)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.LIGHT_SHIELD)
                .input(ESItems.FLAME_SHIELD).and().input(Items.BEACON)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.LIGHT_SHIELD)
                .grist(GristTypes.URANIUM, 444).grist(GristTypes.QUARTZ, 345)
                .grist(GristTypes.DIAMOND, 246)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.NON_CONTACT_CONTRACT)
                .input(ESItems.WOODEN_SHIELD).and().input(Items.PAPER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.NON_CONTACT_CONTRACT)
                .grist(GristTypes.CHALK, 202).grist(GristTypes.TAR, 20)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.SLIED)
                .input(ESItems.NON_CONTACT_CONTRACT).and().input(MSItems.SBAHJ_POSTER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SLIED)
                .grist(GristTypes.ARTIFACT, -5)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.RIOT_SHIELD)
                .input(ESItems.NON_CONTACT_CONTRACT).or()
                .input(Ingredient.of(MSItems.ROCKEFELLERS_WALKING_BLADECANE,
                        MSItems.ROCKEFELLERS_WALKING_BLADECANE_SHEATHED))
                .build(output);
        GristCostRecipeBuilder.of(ESItems.RIOT_SHIELD)
                .grist(GristTypes.TAR, 197).grist(GristTypes.RUST, 228)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD)
                .input(ESItems.RIOT_SHIELD).and().input(Items.TRIDENT)
                .build(output);
        GristCostRecipeBuilder
                .of(Ingredient.of(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD,
                        ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE))
                .grist(GristTypes.COBALT, 827).grist(GristTypes.BUILD, 256).grist(GristTypes.GOLD, 433)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CAPITASHIELD)
                .input(ESItems.GOLD_SHIELD).and().input(ESItems.NON_CONTACT_CONTRACT)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CAPITASHIELD)
                .grist(GristTypes.GOLD, 567).grist(GristTypes.DIAMOND, 246)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.IRON_SHIELD)
                .input(Items.SHIELD).and().input(Tags.Items.INGOTS_IRON)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.IRON_SHIELD)
                .grist(GristTypes.RUST, 63).grist(GristTypes.MERCURY, 17)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.SPIKES_ON_A_SLAB)
                .input(ESItems.IRON_SHIELD).or().input(MSItems.SPIKES)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SPIKES_ON_A_SLAB)
                .grist(GristTypes.RUST, 122).grist(GristTypes.GARNET, 21)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.JAWBITER)
                .input(ESItems.SPIKES_ON_A_SLAB).and().input(MSItems.CANDY_CORN)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.JAWBITER)
                .grist(GristTypes.MARBLE, 83).grist(GristTypes.IODINE, 52)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.ELDRITCH_SHIELD)
                .input(ESItems.JAWBITER).or().input(MSItems.GRIMOIRE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.ELDRITCH_SHIELD)
                .grist(GristTypes.CHALK, 1666).grist(GristTypes.GARNET, 999).grist(GristTypes.TAR, 777)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.GOLD_SHIELD)
                .input(ESItems.IRON_SHIELD).and().input(Tags.Items.INGOTS_GOLD)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GOLD_SHIELD)
                .grist(GristTypes.GOLD, 253).grist(GristTypes.CAULK, 153)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.FLUX_SHIELD)
                .input(ESItems.GOLD_SHIELD).or().input(Tags.Items.STORAGE_BLOCKS_REDSTONE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.FLUX_SHIELD)
                .grist(GristTypes.GOLD, 593).grist(GristTypes.GARNET, 231)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DIAMOND_SHIELD)
                .input(ESItems.GOLD_SHIELD).or().input(Tags.Items.GEMS_DIAMOND)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DIAMOND_SHIELD)
                .grist(GristTypes.DIAMOND, 768).grist(GristTypes.AMETHYST, 256)
                .build(output);

        SmithingTransformRecipeBuilder.smithing(Ingredient.of(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE),
                Ingredient.of(ESItems.DIAMOND_SHIELD), Ingredient.of(Items.NETHERITE_INGOT),
                RecipeCategory.COMBAT,
                ESItems.NETHERITE_SHIELD.get())
                .unlocks("netherite_shield", has(ESItems.DIAMOND_SHIELD))
                .save(output, modid("smithing/netherite_shield"));

        CombinationRecipeBuilder.of(ESItems.GARNET_SHIELD)
                .input(ESItems.DIAMOND_SHIELD).and().input(MSItems.FROG)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GARNET_SHIELD)
                .grist(GristTypes.GARNET, 1500).grist(GristTypes.DIAMOND, 1000)
                .grist(GristTypes.RUBY, 444)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.POGO_SHIELD)
                .input(ESItems.IRON_SHIELD).and().input(Items.SLIME_BALL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.POGO_SHIELD)
                .grist(GristTypes.RUST, 125).grist(GristTypes.SHALE, 55)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.RETURN_TO_SENDER)
                .input(ESItems.POGO_SHIELD).or().input(MSItems.MAILBOX)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.RETURN_TO_SENDER)
                .grist(GristTypes.MARBLE, 868).grist(GristTypes.CAULK, 133)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.GIFT_OF_PROTECTION)
                .input(ESItems.GIFT).or().input(ESItems.NON_CONTACT_CONTRACT)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GIFT_OF_PROTECTION)
                .grist(GristTypes.AMBER, 1225).grist(GristTypes.IODINE, 606)
                .build(output);

        CombinationRecipeBuilder.of(ESISSItems.CAST_GOLD_SHIELD)
                .input(ESItems.GOLD_SHIELD).and().input(MSItems.CAST_IRON)
                .build(output);
        GristCostRecipeBuilder.of(ESISSItems.CAST_GOLD_SHIELD)
                .grist(GristTypes.GOLD, 512).grist(GristTypes.RUST, 201).grist(GristTypes.TAR, 1115)
                .build(output);
        GristCostRecipeBuilder.of(ESISSItems.PROSPITIAN_WAND)
                .grist(GristTypes.GOLD, 512).grist(GristTypes.RUST, 201).grist(GristTypes.TAR, 1115)
                .build(output);
        // Add swapping for ISS
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ESISSItems.CAST_GOLD_SHIELD.toStack())
                .requires(ESISSItems.PROSPITIAN_WAND)
                .unlockedBy("has_prospitian_wand", has(ESISSItems.PROSPITIAN_WAND))
                .save(output.withConditions(ISS_LOADED), modid("shapeless/prospitian_wand_swap"));
    }

    private void weaponRecipes(@Nonnull RecipeOutput output) {
        final RecipeOutput issOutput = output.withConditions(ISS_LOADED);

        // #region Hammers
        CombinationRecipeBuilder.of(ESItems.GEM_BREAKER)
                .input(MSItems.REGI_HAMMER).or().input(Items.EMERALD_BLOCK)
                .build(output, modid("gem_breaker_emerald"));
        CombinationRecipeBuilder.of(ESItems.GEM_BREAKER)
                .input(MSItems.REGI_HAMMER).or().input(Items.DIAMOND_BLOCK)
                .build(output, modid("gem_breaker_diamond"));
        GristCostRecipeBuilder.of(ESItems.GEM_BREAKER)
                .grist(GristTypes.AMETHYST, 63).grist(GristTypes.RUBY, 63).grist(GristTypes.MARBLE, 127)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.BELL_HAMMER)
                .input(MSItems.LUCERNE_HAMMER).and().input(Items.BELL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BELL_HAMMER)
                .grist(GristTypes.GOLD, 444).grist(GristTypes.MERCURY, 150).grist(GristTypes.RUST, 300)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.BLIND_HAMMER)
                .input(ESItems.BELL_HAMMER).or().input(Items.SCULK_SHRIEKER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BLIND_HAMMER)
                .grist(GristTypes.TAR, 3444).grist(GristTypes.DIAMOND, 1250).grist(GristTypes.RUST, 900)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.STEAM_HAMMER)
                .input(MSItems.BLACKSMITH_HAMMER).and().input(Items.FURNACE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.STEAM_HAMMER)
                .grist(GristTypes.SHALE, 64).grist(GristTypes.TAR, 50).grist(GristTypes.BUILD, 160)
                .build(output);

        CombinationRecipeBuilder.of(MSItems.QUENCH_CRUSHER)
                .input(MSItems.COPSE_CRUSHER).or().input(ESItems.DESERT_JUICE)
                .build(output, modid("quench_crusher"));
        // #endregion Hammers

        // #region Dice
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ESItems.GOLD_COIN.toStack())
                .pattern(" N ")
                .pattern("NIN")
                .pattern(" N ")
                .define('N', Items.GOLD_NUGGET)
                .define('I', Items.GOLD_INGOT)
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(output, modid("shaped/gold_coin"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ESItems.GOLD_COIN), RecipeCategory.MISC,
                new ItemStack(Items.GOLD_NUGGET), 0.1F, 200)
                .unlockedBy("has_gold_coin", has(ESItems.GOLD_COIN))
                .save(output, modid("smelting/gold_coin"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ESItems.GOLD_COIN), RecipeCategory.MISC,
                new ItemStack(Items.GOLD_NUGGET), 0.1F, 100)
                .unlockedBy("has_gold_coin", has(ESItems.GOLD_COIN))
                .save(output, modid("blasting/gold_coin"));

        CombinationRecipeBuilder.of(ESItems.STICKY_DIE)
                .input(MSItems.DICE).or().input(Items.SLIME_BALL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.STICKY_DIE)
                .grist(GristTypes.CAULK, 33).grist(GristTypes.TAR, 3)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.ANTI_DIE)
                .input(ESItems.STICKY_DIE).and().input(MSItems.MIRROR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.ANTI_DIE)
                .grist(GristTypes.URANIUM, 166).grist(GristTypes.MERCURY, 333)
                .grist(GristTypes.AMETHYST, 139)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.TOKEN_TETRAHEDRON)
                .input(ESItems.GOLD_COIN).and().input(Items.EMERALD)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.TOKEN_TETRAHEDRON)
                .grist(GristTypes.RUBY, 244).grist(GristTypes.DIAMOND, 244).grist(GristTypes.GOLD, 444)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.D_ICE)
                .input(ESItems.STICKY_DIE).and()
                .input(Ingredient.of(Items.ICE, Items.PACKED_ICE, Items.BLUE_ICE, MSItems.ICE_SHARD))
                .build(output);
        GristCostRecipeBuilder.of(ESItems.D_ICE)
                .grist(GristTypes.COBALT, 66).grist(GristTypes.BUILD, 55)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.SLICE_AND_DICE)
                .input(MSItems.DAGGER).or().input(ESItems.GOLD_COIN)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SLICE_AND_DICE)
                .grist(GristTypes.GOLD, 66).grist(GristTypes.CHALK, 33)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DONE)
                .input(MSItems.SBAHJ_POSTER).or().input(ESItems.D_ICE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DONE)
                .grist(GristTypes.ARTIFACT, -6)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.D10)
                .input(ESItems.TOKEN_TETRAHEDRON).and().input(Items.CLOCK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.D10)
                .grist(GristTypes.DIAMOND, 1331).grist(GristTypes.GOLD, 10)
                .grist(GristTypes.GARNET, 999)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.RAINBOW_D7)
                .input(MSItems.DICE).and().input(MSItems.WATER_COLORS_BUCKET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.RAINBOW_D7)
                .grist(GristTypes.GOLD, 777).grist(GristTypes.DIAMOND, 777)
                .grist(GristTypes.COBALT, 777).grist(GristTypes.URANIUM, 77)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.D8_NIGHT)
                .input(ESItems.RAINBOW_D7).or().input(MSItems.NIGHTSTICK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.D8_NIGHT)
                .grist(GristTypes.COBALT, 1888).grist(GristTypes.DIAMOND, 888)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CAN_DIE)
                .input(MSItems.DICE).or().input(MSItems.CANDY_CORN)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CAN_DIE)
                .grist(GristTypes.MARBLE, 12).grist(GristTypes.CHALK, 36)
                .grist(GristTypes.AMBER, 12)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.JACKPOT)
                .input(ESItems.TOKEN_TETRAHEDRON).or().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.JACKPOT)
                .grist(GristTypes.GOLD, 777).grist(GristTypes.DIAMOND, 333)
                .grist(GristTypes.GARNET, 333).grist(GristTypes.AMETHYST, 333)
                .build(output);
        // #endregion Dice

        // #region Clubs
        CombinationRecipeBuilder.of(ESItems.SILVER_BAT)
                .input(MSItems.METAL_BAT).and().input(Items.GLASS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SILVER_BAT)
                .grist(GristTypes.MERCURY, 35).grist(GristTypes.RUST, 70)
                .build(output);

        GristCostRecipeBuilder.of(ESItems.GOLDEN_PAN)
                .grist(GristTypes.GOLD, 250).grist(GristTypes.RUST, 44)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.ROLLING_PIN)
                .input(MSItems.DEUCE_CLUB).and().input(ESItems.PIZZA)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.ROLLING_PIN)
                .grist(GristTypes.BUILD, 12)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DESTRUCTION_BAT)
                .input(ESItems.SILVER_BAT).or().input(Items.NETHERITE_SCRAP)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DESTRUCTION_BAT)
                .grist(GristTypes.TAR, 164).grist(GristTypes.AMBER, 62)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DEATH_MACE)
                .input(MSItems.RUBIKS_MACE).or().input(Items.SKELETON_SKULL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DEATH_MACE)
                .grist(GristTypes.CHALK, 242).grist(GristTypes.MARBLE, 64).grist(GristTypes.CAULK, 9)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.WITHERED_MACE)
                .input(ESItems.DEATH_MACE).and().input(Items.WITHER_SKELETON_SKULL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.WITHERED_MACE)
                .grist(GristTypes.TAR, 472).grist(GristTypes.DIAMOND, 64).grist(GristTypes.URANIUM, 50)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.D8TH_M8CE)
                .input(ESItems.D8_NIGHT).and().input(ESItems.DEATH_MACE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.D8TH_M8CE)
                .grist(GristTypes.COBALT, 888).grist(GristTypes.DIAMOND, 128).grist(GristTypes.MARBLE, 343)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.BIG_CLUB)
                .input(MSItems.DEUCE_CLUB).or().input(Items.SPYGLASS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BIG_CLUB)
                .grist(GristTypes.BUILD, 30).grist(GristTypes.GOLD, 1)
                .build(output);
        // #endregion Clubs

        // #region Keys
        CombinationRecipeBuilder.of(ESItems.KEY_OF_TRIALS)
                .input(MSItems.HOUSE_KEY).and().input(Items.TRIAL_KEY)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.KEY_OF_TRIALS)
                .grist(GristTypes.SHALE, 13).grist(GristTypes.RUST, 7)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.KEY_OF_OMINOUS_TRIALS)
                .input(ESItems.KEY_OF_TRIALS).and().input(Items.OMINOUS_TRIAL_KEY)
                .build(output);
        CombinationRecipeBuilder.of(ESItems.KEY_OF_OMINOUS_TRIALS)
                .input(ESItems.KEY_OF_TRIALS).and().input(Items.OMINOUS_BOTTLE)
                .build(output, ExtraStuck.modid("key_of_ominous_trials_alt"));
        GristCostRecipeBuilder.of(ESItems.KEY_OF_OMINOUS_TRIALS)
                .grist(GristTypes.GARNET, 6).grist(GristTypes.SHALE, 43)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.OFFICE_KEY)
                .input(MSItems.HOUSE_KEY).or().input(Items.IRON_DOOR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.OFFICE_KEY)
                .grist(GristTypes.MARBLE, 33).grist(GristTypes.TAR, 12)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.ANCIENT_VAULT_OPENER)
                .input(ESItems.KEY_OF_TRIALS).or().input(ItemRegistry.DECREPIT_KEY.get())
                .build(output.withConditions(ISS_LOADED), ExtraStuck.modid("ancient_vault_opener_irons_spellbooks"));
        CombinationRecipeBuilder.of(ESItems.ANCIENT_VAULT_OPENER)
                .input(ESItems.KEY_OF_TRIALS).or().input(Items.NETHERITE_SCRAP)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESItems.ANCIENT_VAULT_OPENER)
                .grist(GristTypes.RUST, 36).grist(GristTypes.TAR, 29)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.VAULT_MELTER)
                .input(ESItems.ANCIENT_VAULT_OPENER).and().input(Items.BLAZE_ROD)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESItems.VAULT_MELTER)
                .grist(GristTypes.RUST, 50).grist(GristTypes.TAR, 50).grist(GristTypes.QUARTZ, 20)
                .grist(GristTypes.GOLD, 150).grist(GristTypes.AMBER, 250)
                .build(output.withConditions(not(ISS_LOADED)));
        SmithingTransformRecipeBuilder.smithing(Ingredient.of(ItemRegistry.TIMELESS_SLURRY.get()),
                Ingredient.of(ESItems.ANCIENT_VAULT_OPENER.get()), Ingredient.of(ItemRegistry.PYRIUM_INGOT.get()),
                RecipeCategory.COMBAT, ESItems.VAULT_MELTER.get())
                .unlocks("vault_melter", has(ESItems.ANCIENT_VAULT_OPENER))
                .save(output.withConditions(ISS_LOADED), modid("smithing/vault_melter"));
        // #endregion Keys

        // #region Wands
        CombinationRecipeBuilder.of(ESItems.BAGUETTE_MAGIQUE)
                .input(MSItems.STALE_BAGUETTE).and().input(MSItems.WAND)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BAGUETTE_MAGIQUE)
                .grist(GristTypes.AMBER, 12).grist(GristTypes.IODINE, 18).grist(GristTypes.SULFUR, 6)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.MONEY_MAGIC)
                .input(MSItems.NEEDLE_WAND).or().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.MONEY_MAGIC)
                .grist(GristTypes.RUBY, 120).grist(GristTypes.GOLD, 730).grist(GristTypes.COBALT, 1250)
                .build(output);
        // #endregion Wands

        // #region Canes
        CombinationRecipeBuilder.of(ESItems.BROOM)
                .input(Items.BRUSH).and().input(Items.HAY_BLOCK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BROOM)
                .grist(GristTypes.CHALK, 39).grist(GristTypes.IODINE, 50).grist(GristTypes.SHALE, 27)
                .build(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ESItems.IRON_CROWBAR)
                .pattern(" IB")
                .pattern(" B ")
                .pattern("BN ")
                .define('I', Tags.Items.INGOTS_IRON)
                .define('B', Items.IRON_BARS)
                .define('N', Tags.Items.NUGGETS_IRON)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(output, modid("shaped/iron_crowbar"));
        // #endregion Canes

        // #region Forks
        CombinationRecipeBuilder.of(ESItems.MAGNEFORK)
                .input(MSItems.TUNING_FORK).and().input(ESItems.MAGNET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.MAGNEFORK)
                .grist(GristTypes.RUST, 40).grist(GristTypes.GARNET, 5).grist(GristTypes.COBALT, 5)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.OVERCHARGED_MAGNEFORK)
                .input(ESItems.MAGNEFORK).or().input(MSItems.BATTERY)
                .build(output);
        CombinationRecipeBuilder.of(ESItems.OVERCHARGED_MAGNEFORK)
                .input(ESItems.MAGNET).and().input(MSItems.EDISONS_FURY)
                .build(output, ExtraStuck.modid("overcharged_magnefork_alt"));
        GristCostRecipeBuilder.of(ESItems.OVERCHARGED_MAGNEFORK)
                .grist(GristTypes.GOLD, 450).grist(GristTypes.URANIUM, 75)
                .grist(GristTypes.GARNET, 20).grist(GristTypes.COBALT, 20)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.UNDERCHARGED_MAGNEFORK)
                .grist(GristTypes.GOLD, 450).grist(GristTypes.URANIUM, 75)
                .grist(GristTypes.GARNET, 20).grist(GristTypes.COBALT, 20)
                .build(output);
        // #endregion Forks

        // #region Chainsaws
        CombinationRecipeBuilder.of(ESItems.YELLOWCAKESAW)
                .input(MSItems.CAKESAW).and().input(ESItems.YELLOWCAKE_SLICE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.YELLOWCAKESAW)
                .grist(GristTypes.SULFUR, 90).grist(GristTypes.MERCURY, 36).grist(GristTypes.URANIUM, 12)
                .build(output);
        SourceGristCostBuilder.of(ESItems.YELLOWCAKESAW_LIPSTICK)
                .source(ESItems.YELLOWCAKESAW.get())
                .build(output);
        // #endregion Chainsaws

        // #region Batons
        CombinationRecipeBuilder.of(ESItems.THE_STING)
                .input(MSItems.CONDUCTORS_BATON).and().input(ESItems.BEE_LARVA)
                .build(output);
        CombinationRecipeBuilder.of(ESItems.THE_STING)
                .input(MSItems.CONDUCTORS_BATON).and().input(Items.BEEHIVE)
                .build(output, modid("the_sting_hive"));
        GristCostRecipeBuilder.of(ESItems.THE_STING)
                .grist(GristTypes.GOLD, 60).grist(GristTypes.TAR, 40)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.STOCKS_UPTICKER)
                .input(MSItems.URANIUM_BATON).and().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.STOCKS_UPTICKER)
                .grist(GristTypes.GOLD, 750).grist(GristTypes.GARNET, 400).grist(GristTypes.URANIUM, 25)
                .build(output);
        // #endregion Batons

        // #region Swords
        CombinationRecipeBuilder.of(ESItems.SUN_REAVER)
                .input(MSItems.REGISWORD).or().input(Items.DAYLIGHT_DETECTOR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SUN_REAVER)
                .grist(GristTypes.TAR, 400).grist(GristTypes.SHALE, 158).grist(GristTypes.COBALT, 914)
                .build(output);

        CombinationRecipeBuilder.of(ESISSItems.LEADER_SWORD)
                .input(MSItems.ANGEL_APOCALYPSE).and().input(ItemRegistry.DIVINE_PEARL.get())
                .build(issOutput, modid("leader_sword_iss"));
        CombinationRecipeBuilder.of(ESISSItems.LEADER_SWORD)
                .input(MSItems.ANGEL_APOCALYPSE).and().input(MSItems.MIRROR)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.LEADER_SWORD)
                .grist(GristTypes.RUBY, 88).grist(GristTypes.URANIUM, 150).grist(GristTypes.AMBER, 1511)
                .build(output);
        // #endregion Swords

        // #region Sickles
        CombinationRecipeBuilder.of(ESItems.NEW_MOON)
                .input(MSItems.REGISICKLE).or().input(MSItems.OIL_BUCKET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.NEW_MOON)
                .grist(GristTypes.CAULK, 400).grist(GristTypes.TAR, 158).grist(GristTypes.SHALE, 914)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.PIRATE_HOOK)
                .input(MSItems.HERETICUS_AURURM).and().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.PIRATE_HOOK)
                .grist(GristTypes.GOLD, 700).grist(GristTypes.TAR, 400).grist(GristTypes.COBALT, 1300)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.BLIGHT)
                .input(MSItems.THORNY_SUBJECT).or().input(Items.DEAD_BUSH)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BLIGHT)
                .grist(GristTypes.SULFUR, 111).grist(GristTypes.AMBER, 41).grist(GristTypes.BUILD, 63)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.END_OF_CIVILIZATION)
                .input(ESItems.BLIGHT).and().input(ESItems.COSMIC_PLAGUE_SPORE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.END_OF_CIVILIZATION)
                .grist(GristTypes.AMETHYST, 3929).grist(GristTypes.DIAMOND, 1352).grist(GristTypes.URANIUM, 420)
                .build(output);
        // #endregion Sickles

        // #region Scythes
        CombinationRecipeBuilder.of(ESItems.DEBT_REAPER)
                .input(MSItems.PROSPECTING_PICKSCYTHE).or().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DEBT_REAPER)
                .grist(GristTypes.GOLD, 600).grist(GristTypes.AMBER, 1400).grist(GristTypes.DIAMOND, 250)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.LEAFBURNER)
                .input(MSItems.SUNRAY_HARVESTER).or().input(Items.SMOKER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.LEAFBURNER)
                .grist(GristTypes.AMBER, 767).grist(GristTypes.TAR, 500).grist(GristTypes.BUILD, 750)
                .build(output);
        // #endregion Scythes

        // #region Fans
        CombinationRecipeBuilder.of(ESItems.NONE_OF_YOUR_BUSINESS)
                .input(MSItems.FIRESTARTER).and().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.NONE_OF_YOUR_BUSINESS)
                .grist(GristTypes.AMETHYST, 440).grist(GristTypes.SHALE, 880).grist(GristTypes.DIAMOND, 220)
                .build(output);
        // #endregion Fans

        // #region Lances
        CombinationRecipeBuilder.of(ESItems.INVESTLANCE)
                .input(MSItems.JOUSTING_LANCE).and().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.INVESTLANCE)
                .grist(GristTypes.GARNET, 678).grist(GristTypes.COBALT, 654).grist(GristTypes.GOLD, 720)
                .build(output);
        // #endregion Lances

        // #region Claws
        CombinationRecipeBuilder.of(ESItems.CASHGRABBERS)
                .input(MSItems.CAT_CLAWS_DRAWN).or().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CASHGRABBERS)
                .grist(GristTypes.GOLD, 804).grist(GristTypes.DIAMOND, 450).grist(GristTypes.RUBY, 222)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CASHGRABBERS_SHEATHED)
                .grist(GristTypes.GOLD, 804).grist(GristTypes.DIAMOND, 450).grist(GristTypes.RUBY, 222)
                .build(output);
        // #endregion Claws

        // #region Knives
        CombinationRecipeBuilder.of(ESISSItems.AMETHYST_BACKSTABBER)
                .input(MSItems.SHADOWRAZOR).and().input(Items.AMETHYST_BLOCK)
                .build(output);
        GristCostRecipeBuilder.of(ESISSItems.AMETHYST_BACKSTABBER)
                .grist(GristTypes.AMETHYST, 264).grist(GristTypes.SHALE, 512).grist(GristTypes.TAR, 1128)
                .build(output);
        GristCostRecipeBuilder.of(ESISSItems.DERSITE_WAND)
                .grist(GristTypes.AMETHYST, 264).grist(GristTypes.SHALE, 512).grist(GristTypes.TAR, 1128)
                .build(output);
        // Add swapping for ISS
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ESISSItems.AMETHYST_BACKSTABBER.toStack())
                .requires(ESISSItems.DERSITE_WAND)
                .unlockedBy("has_dersite_wand", has(ESISSItems.DERSITE_WAND))
                .save(output.withConditions(ISS_LOADED), modid("shapeless/dersite_wand_swap"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.COMBAT, ESISSItems.DERSITE_WAND.toStack())
                .requires(ESISSItems.AMETHYST_BACKSTABBER)
                .unlockedBy("has_dersite_wand", has(ESISSItems.AMETHYST_BACKSTABBER))
                .save(output.withConditions(ISS_LOADED), modid("shapeless/amethyst_backstabber_swap"));
        // #endregion Knives

        // #region Crossbows
        CombinationRecipeBuilder.of(ESItems.RADBOW)
                .input(Items.CROSSBOW).and().input(MSItems.URANIUM_POWERED_STICK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.RADBOW)
                .grist(GristTypes.MERCURY, 26).grist(GristTypes.URANIUM, 1).grist(GristTypes.CAULK, 9)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.MECHANICAL_RADBOW)
                .input(ESItems.RADBOW).and().input(MSItems.COMPUTER_PARTS)
                .build(output.withConditions(not(CREATE_LOADED)));
        GristCostRecipeBuilder.of(ESItems.MECHANICAL_RADBOW)
                .grist(GristTypes.MERCURY, 36).grist(GristTypes.URANIUM, 2).grist(GristTypes.CAULK, 15)
                .grist(GristTypes.BUILD, 50).grist(GristTypes.RUST, 25).grist(GristTypes.GOLD, 5)
                .build(output.withConditions(not(CREATE_LOADED)));

        new SequencedAssemblyRecipeBuilder(ExtraStuck.modid("mechanical_radbow"))
                .require(ESItems.RADBOW)
                .transitionTo(ESItems.INCOMPLETE_MECHANICAL_RADBOW)
                .addOutput(ESItems.MECHANICAL_RADBOW, 1)
                .loops(2)
                .addStep(DeployerApplicationRecipe::new, sub -> sub.require(AllBlocks.LARGE_COGWHEEL))
                .addStep(DeployerApplicationRecipe::new, sub -> sub.require(MSItems.COMPUTER_PARTS))
                .addStep(DeployerApplicationRecipe::new, sub -> sub.require(ESTags.Items.BRASS_NUGGETS))
                .addStep(PressingRecipe::new, sub -> sub)
                .build(output.withConditions(new ModLoadedCondition("create")));
        // #endregion Crossbows

        // #region Bows
        CombinationRecipeBuilder.of(ESItems.BOWWOB)
                .input(Items.BOW).and().input(MSItems.MIRROR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BOWWOB)
                .grist(GristTypes.BUILD, 30).grist(GristTypes.MERCURY, 12)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.RAINBOW_BOW)
                .input(Items.BOW).and().input(MSItems.WATER_COLORS_BUCKET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.RAINBOW_BOW)
                .grist(GristTypes.BUILD, 78)
                .grist(GristTypes.SULFUR, 24).grist(GristTypes.MARBLE, 24).grist(GristTypes.SHALE, 24)
                .grist(GristTypes.RUBY, 24)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.MAKE_IT_RAIN)
                .input(ESItems.RAINBOW_BOW).and().input(Items.BEACON)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.MAKE_IT_RAIN)
                .grist(GristTypes.COBALT, 15625).grist(GristTypes.CHALK, 625).grist(GristTypes.DIAMOND, 3125)
                .build(output);
        // #endregion Bows

        // #region Throwables
        CombinationRecipeBuilder.of(ESItems.BEENADE)
                .input(MSItems.BARBASOL_BOMB).and().input(Items.BEEHIVE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BEENADE)
                .grist(GristTypes.GOLD, 10).grist(GristTypes.AMBER, 10).grist(GristTypes.TAR, 10)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.LEMONNADE)
                .input(MSItems.BARBASOL_BOMB).or().input(Items.YELLOW_DYE)
                .build(output);
        CombinationRecipeBuilder.of(ESItems.LEMONNADE)
                .input(ESTags.Items.LEMON_FRUITS).and().input(Items.GUNPOWDER)
                .build(output.withConditions(not(new TagEmptyCondition(ESTags.Items.LEMON_FRUITS))),
                        modid("lemonnade_lemon_fruits"));
        CombinationRecipeBuilder.of(ESItems.LEMONNADE)
                .input(ESTags.Items.LEMON_CROPS).and().input(Items.GUNPOWDER)
                .build(output.withConditions(not(new TagEmptyCondition(ESTags.Items.LEMON_CROPS))),
                        modid("lemonnade_lemon_crops"));
        GristCostRecipeBuilder.of(ESItems.LEMONNADE)
                .grist(GristTypes.AMBER, 40).grist(GristTypes.CHALK, 23).grist(GristTypes.GOLD, 9)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.YIN_YANG_ORB)
                .input(MSItems.SORCERERS_PINBALL).and().input(MSItems.KATANA)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.YIN_YANG_ORB)
                .grist(GristTypes.TAR, 46).grist(GristTypes.CHALK, 46).grist(GristTypes.GARNET, 8)
                .build(output);
        // #endregion Throwables

        // #region Staves
        CombinationRecipeBuilder.of(ESISSItems.CURSED_CAT_STAFF)
                .input(ItemRegistry.BLOOD_STAFF.get()).and().input(ESItems.NORMAL_CAT_PLUSH)
                .build(issOutput, ExtraStuck.modid("cursed_cat_staff_from_blood_staff"));
        CombinationRecipeBuilder.of(ESISSItems.CURSED_CAT_STAFF)
                .input(MSItems.POOL_CUE_WAND).and().input(ESItems.NORMAL_CAT_PLUSH)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.CURSED_CAT_STAFF)
                .grist(GristTypes.TAR, 1000).grist(GristTypes.DIAMOND, 500).grist(GristTypes.COBALT, 900)
                .build(output);

        CombinationRecipeBuilder.of(ESISSItems.BLESSED_CAT_STAFF)
                .input(ItemRegistry.BLOOD_STAFF.get()).or().input(ESItems.NORMAL_CAT_PLUSH)
                .build(issOutput, ExtraStuck.modid("blessed_cat_staff_from_blood_staff"));
        CombinationRecipeBuilder.of(ESISSItems.BLESSED_CAT_STAFF)
                .input(MSItems.WAND).or().input(ESItems.NORMAL_CAT_PLUSH)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.BLESSED_CAT_STAFF)
                .grist(GristTypes.MARBLE, 1200).grist(GristTypes.GOLD, 250).grist(GristTypes.CHALK, 800)
                .build(output);

        CombinationRecipeBuilder.of(ESISSItems.BRANCH_OF_YGGDRASIL)
                .input(ItemRegistry.ARTIFICER_STAFF.get()).and().input(MSItems.CUEBALL)
                .build(issOutput, ExtraStuck.modid("branch_of_yggdrasil_iss"));
        CombinationRecipeBuilder.of(ESISSItems.BRANCH_OF_YGGDRASIL)
                .input(MSItems.THORN_OF_OGLOGOTH).and().input(MSItems.CUEBALL)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.BRANCH_OF_YGGDRASIL)
                .grist(GristTypes.BUILD, 5983).grist(GristTypes.MARBLE, 3723).grist(GristTypes.QUARTZ, 2700)
                .build(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ESISSItems.STAFF_OF_YGGDRASIL.toStack())
                .pattern("fGi")
                .pattern("lBe")
                .pattern("bGn")
                .define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .define('B', ESISSItems.BRANCH_OF_YGGDRASIL)
                .define('f', ItemRegistry.FIRE_RUNE.get())
                .define('i', ItemRegistry.ICE_RUNE.get())
                .define('l', ItemRegistry.LIGHTNING_RUNE.get())
                .define('e', ItemRegistry.ENDER_RUNE.get())
                .define('b', ItemRegistry.BLOOD_RUNE.get())
                .define('n', ItemRegistry.NATURE_RUNE.get())
                .unlockedBy("has_yggdrasil_branch", has(ESISSItems.BRANCH_OF_YGGDRASIL))
                .save(issOutput, modid("shaped/staff_of_yggdrasil_iss"));
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ESISSItems.STAFF_OF_YGGDRASIL.toStack())
                .pattern("lGe")
                .pattern("fBn")
                .pattern("bGi")
                .define('G', Tags.Items.STORAGE_BLOCKS_GOLD)
                .define('B', ESISSItems.BRANCH_OF_YGGDRASIL)
                .define('f', Items.BLAZE_ROD)
                .define('i', Items.BLUE_ICE)
                .define('l', Items.CREEPER_HEAD)
                .define('e', Items.DRAGON_HEAD)
                .define('b', MSItems.COAGULATED_BLOOD)
                .define('n', Items.SPIDER_EYE)
                .unlockedBy("has_yggdrasil_branch", has(ESISSItems.BRANCH_OF_YGGDRASIL))
                .save(output.withConditions(not(ISS_LOADED)), modid("shaped/staff_of_yggdrasil"));
        // #endregion Staves

        // #region Spellbooks
        CombinationRecipeBuilder.of(ESISSItems.GRIMOIRE)
                .input(MSItems.GRIMOIRE).and().input(ItemRegistry.GOLD_SPELL_BOOK.get())
                .build(issOutput);
        GristCostRecipeBuilder.of(ESISSItems.GRIMOIRE)
                .grist(GristTypes.MARBLE, 66).grist(GristTypes.AMETHYST, 666).grist(GristTypes.GARNET, 133)
                .build(issOutput);

        CombinationRecipeBuilder.of(ESISSItems.GEMINI_SPELLBOOK_BLUE)
                .input(ItemRegistry.DIAMOND_SPELL_BOOK.get()).or().input(MSItems.MIRROR)
                .build(issOutput);
        GristCostRecipeBuilder.of(ESISSItems.GEMINI_SPELLBOOK_BLUE)
                .grist(GristTypes.BUILD, 160).grist(GristTypes.COBALT, 200).grist(GristTypes.GARNET, 100)
                .grist(GristTypes.DIAMOND, 32).grist(GristTypes.AMBER, 64)
                .build(issOutput);
        GristCostRecipeBuilder.of(ESISSItems.GEMINI_SPELLBOOK_RED)
                .grist(GristTypes.BUILD, 160).grist(GristTypes.COBALT, 200).grist(GristTypes.GARNET, 100)
                .grist(GristTypes.DIAMOND, 32).grist(GristTypes.AMBER, 64)
                .build(issOutput);

        CombinationRecipeBuilder.of(ESISSItems.MAGE_GUY)
                .input(ItemRegistry.IRON_SPELL_BOOK.get()).and().input(MSItems.WISEGUY)
                .build(issOutput);
        GristCostRecipeBuilder.of(ESISSItems.MAGE_GUY)
                .grist(GristTypes.SULFUR, 12).grist(GristTypes.CHALK, 18).grist(GristTypes.IODINE, 12)
                .grist(GristTypes.SHALE, 21)
                .build(issOutput);

        GristCostRecipeBuilder.of(ESISSItems.PERFECTLY_UNIQUE_SPELLBOOK)
                .grist(GristTypes.BUILD, 93475).grist(GristTypes.URANIUM, 31).grist(GristTypes.AMETHYST, 866578)
                .grist(GristTypes.ZILLIUM, 3).grist(GristTypes.CAULK, 30)
                .build(issOutput);

        CombinationRecipeBuilder.of(ESISSItems.SBURBDB)
                .input(ItemRegistry.IRON_SPELL_BOOK.get()).and().input(MSItems.COMPLETED_SBURB_CODE)
                .build(issOutput, modid("sburbdb_iss"));
        CombinationRecipeBuilder.of(ESISSItems.SBURBDB)
                .input(Items.SPYGLASS).and().input(MSItems.COMPLETED_SBURB_CODE)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.SBURBDB)
                .grist(GristTypes.BUILD, 160).grist(GristTypes.MERCURY, 20)
                .grist(GristTypes.CAULK, 8).grist(GristTypes.SHALE, 8).grist(GristTypes.RUST, 8)
                .grist(GristTypes.URANIUM, 8)
                .build(output);
        // #endregion Spellbooks

        // #region Cards
        CombinationRecipeBuilder.of(ESItems.TWO_OF_HEARTS)
                .input(MSItems.ACE_OF_HEARTS).and().input(MSItems.MIRROR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.TWO_OF_HEARTS)
                .grist(GristTypes.MARBLE, 1080).grist(GristTypes.RUBY, 125).grist(GristTypes.DIAMOND, 15)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.TWO_OF_DIAMONDS)
                .input(MSItems.ACE_OF_DIAMONDS).and().input(MSItems.MIRROR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.TWO_OF_DIAMONDS)
                .grist(GristTypes.MARBLE, 1080).grist(GristTypes.DIAMOND, 125).grist(GristTypes.GOLD, 15)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.TWO_OF_SPADES)
                .input(MSItems.ACE_OF_SPADES).and().input(MSItems.MIRROR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.TWO_OF_SPADES)
                .grist(GristTypes.MARBLE, 1080).grist(GristTypes.SULFUR, 125).grist(GristTypes.DIAMOND, 15)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.TWO_OF_CLUBS)
                .input(MSItems.ACE_OF_CLUBS).and().input(MSItems.MIRROR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.TWO_OF_CLUBS)
                .grist(GristTypes.MARBLE, 1080).grist(GristTypes.DIAMOND, 125).grist(GristTypes.RUST, 15)
                .build(output);
        // #endregion Cards

        GristCostRecipeBuilder.of(ESItems.HANDGUN)
                .grist(GristTypes.MARBLE, 33).grist(GristTypes.TAR, 12)
                .build(output);
    }

    private void arrowRecipes(@Nonnull RecipeOutput output) {
        CombinationRecipeBuilder.of(ESItems.NETHER_ARROW)
                .input(Items.ARROW).and().input(Items.NETHERRACK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.NETHER_ARROW)
                .grist(GristTypes.SULFUR, 12).grist(GristTypes.RUST, 8)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.FLAME_ARROW)
                .input(ESItems.NETHER_ARROW).and()
                .input(Ingredient.of(Items.BLAZE_ROD, Items.BLAZE_POWDER))
                .build(output);
        GristCostRecipeBuilder.of(ESItems.FLAME_ARROW)
                .grist(GristTypes.SULFUR, 8).grist(GristTypes.TAR, 4)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CARDBOARD_ARROW)
                .input(MSItems.CARDBOARD_TUBE).and().input(Items.FLINT)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CARDBOARD_ARROW)
                .grist(GristTypes.BUILD, 1)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.MISSED_YOU)
                .input(ESItems.CARDBOARD_ARROW).or().input(MSItems.SBAHJ_POSTER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.MISSED_YOU)
                .grist(GristTypes.ARTIFACT, 1)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.SWEET_TOOTH)
                .input(Ingredient.of(ESItems.CARDBOARD_ARROW, Items.BONE)).and()
                .input(MSItems.CANDY_CORN)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SWEET_TOOTH)
                .grist(GristTypes.SHALE, 1).grist(GristTypes.IODINE, 6)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.LIGHTNING_ARROW)
                .input(Items.LIGHTNING_ROD).or().input(MSItems.BATTERY)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.LIGHTNING_ARROW)
                .grist(GristTypes.GOLD, 25).grist(GristTypes.RUST, 25)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.EXPLOSIVE_ARROW)
                .input(ESItems.FLAME_ARROW).or().input(ESItems.LIGHTNING_ARROW)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.EXPLOSIVE_ARROW)
                .grist(GristTypes.SULFUR, 21).grist(GristTypes.TAR, 7).grist(GristTypes.CHALK, 12)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.IRON_ARROW)
                .input(Items.ARROW).and().input(Tags.Items.INGOTS_IRON)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.IRON_ARROW)
                .grist(GristTypes.BUILD, 2).grist(GristTypes.RUST, 4)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.QUARTZ_ARROW)
                .input(ESItems.IRON_ARROW).or().input(Tags.Items.GEMS_QUARTZ)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.QUARTZ_ARROW)
                .grist(GristTypes.QUARTZ, 12).grist(GristTypes.BUILD, 4)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.PRISMARINE_ARROW)
                .input(ESItems.QUARTZ_ARROW).and().input(Items.PRISMARINE_SHARD)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.PRISMARINE_ARROW)
                .grist(GristTypes.DIAMOND, 1).grist(GristTypes.COBALT, 7).grist(GristTypes.BUILD, 1)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.GLASS_ARROW)
                .input(ESItems.QUARTZ_ARROW).and().input(Tags.Items.GLASS_BLOCKS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GLASS_ARROW)
                .grist(GristTypes.QUARTZ, 25).grist(GristTypes.MARBLE, 5)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.AMETHYST_ARROW)
                .input(ESItems.GLASS_ARROW).or().input(Tags.Items.GEMS_AMETHYST)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.AMETHYST_ARROW)
                .grist(GristTypes.DIAMOND, 4).grist(GristTypes.AMETHYST, 19)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.PROJECDRILL)
                .input(ESItems.IRON_ARROW).and().input(Items.IRON_PICKAXE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.PROJECDRILL)
                .grist(GristTypes.RUST, 32).grist(GristTypes.MERCURY, 8)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CRUSADER_CROSSBOLT)
                .input(Items.ARROW).and()
                .input(Ingredient.of(Items.GLISTERING_MELON_SLICE, Items.GOLDEN_CARROT))
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CRUSADER_CROSSBOLT)
                .grist(GristTypes.GOLD, 9).grist(GristTypes.GARNET, 5)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.END_ARROW)
                .input(Items.ARROW).or().input(Items.END_STONE)
                .build(output);
        CombinationRecipeBuilder.of(ESItems.END_ARROW)
                .input(Items.END_ROD).and().input(Items.FLINT)
                .build(output, modid("end_arrow_2"));
        GristCostRecipeBuilder.of(ESItems.END_ARROW)
                .grist(GristTypes.URANIUM, 3).grist(GristTypes.CAULK, 7)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.TELERROW)
                .input(ESItems.END_ARROW).or().input(Items.ENDER_PEARL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.TELERROW)
                .grist(GristTypes.URANIUM, 23).grist(GristTypes.MERCURY, 12)
                .grist(GristTypes.DIAMOND, 8)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DRAGON_ARROW)
                .input(ESItems.TELERROW).and().input(Items.DRAGON_BREATH)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DRAGON_ARROW)
                .grist(GristTypes.URANIUM, 15).grist(GristTypes.RUBY, 4)
                .build(output);
    }

    private void ammoRecipes(@Nonnull RecipeOutput output) {
        CombinationRecipeBuilder.of(ESItems.HANDGUN_BULLET)
                .input(Items.GUNPOWDER).and().input(Items.IRON_NUGGET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.HANDGUN_BULLET)
                .grist(GristTypes.CHALK, 3).grist(GristTypes.TAR, 1)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.HEAVY_HANDGUN_BULLET)
                .input(ESItems.HANDGUN_BULLET).and().input(Items.IRON_INGOT)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.HEAVY_HANDGUN_BULLET)
                .grist(GristTypes.RUST, 5).grist(GristTypes.TAR, 2)
                .build(output);
    }

    private void armorRecipes(@Nonnull RecipeOutput output) {
        CombinationRecipeBuilder.of(ESItems.CHEF_HAT)
                .input(ESItems.PIZZA).or().input(Items.LEATHER_HELMET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CHEF_HAT)
                .grist(GristTypes.CHALK, 22)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CHEF_APRON)
                .input(ESItems.PIZZA).or().input(Items.LEATHER_CHESTPLATE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CHEF_APRON)
                .grist(GristTypes.CHALK, 32)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.HEAVY_BOOTS)
                .input(Items.IRON_BOOTS).and().input(Items.ANVIL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.HEAVY_BOOTS)
                .grist(GristTypes.RUST, 54).grist(GristTypes.TAR, 27)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.PROPELLER_HAT)
                .input(Items.IRON_HELMET).and().input(Items.FEATHER)
                .build(output);
        CombinationRecipeBuilder.of(ESItems.PROPELLER_HAT)
                .input(Items.IRON_HELMET).and().input(Items.WIND_CHARGE)
                .build(output, modid("propeller_hat_alt"));
        GristCostRecipeBuilder.of(ESItems.PROPELLER_HAT)
                .grist(GristTypes.RUST, 90).grist(GristTypes.GARNET, 17)
                .grist(GristTypes.AMBER, 17).grist(GristTypes.COBALT, 17)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.SALESMAN_GOGGLES)
                .input(Items.YELLOW_STAINED_GLASS_PANE).and().input(Items.MAGENTA_STAINED_GLASS_PANE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SALESMAN_GOGGLES)
                .grist(GristTypes.AMETHYST, 19).grist(GristTypes.GOLD, 19).grist(GristTypes.ARTIFACT, 97)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.SALESWOMAN_GLASSES)
                .input(Items.RED_STAINED_GLASS_PANE).and().input(Items.GOLD_NUGGET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SALESWOMAN_GLASSES)
                .grist(GristTypes.GARNET, 20).grist(GristTypes.GOLD, 30).grist(GristTypes.DIAMOND, 10)
                .build(output);

        // #region Dark Knight
        CombinationRecipeBuilder.of(ESItems.DARK_KNIGHT_HELMET)
                .input(Items.NETHERITE_HELMET).and().input(Items.TINTED_GLASS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DARK_KNIGHT_HELMET)
                .grist(GristTypes.TAR, 7300).grist(GristTypes.AMETHYST, 3210)
                .grist(GristTypes.SHALE, 4400)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DARK_KNIGHT_CHESTPLATE)
                .input(Items.NETHERITE_CHESTPLATE).and().input(Items.TINTED_GLASS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DARK_KNIGHT_CHESTPLATE)
                .grist(GristTypes.TAR, 7300).grist(GristTypes.AMETHYST, 3210)
                .grist(GristTypes.SHALE, 6400)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DARK_KNIGHT_LEGGINGS)
                .input(Items.NETHERITE_LEGGINGS).and().input(Items.TINTED_GLASS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DARK_KNIGHT_LEGGINGS)
                .grist(GristTypes.TAR, 7300).grist(GristTypes.AMETHYST, 3210)
                .grist(GristTypes.SHALE, 6000)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DARK_KNIGHT_BOOTS)
                .input(Items.NETHERITE_BOOTS).and().input(Items.TINTED_GLASS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DARK_KNIGHT_BOOTS)
                .grist(GristTypes.TAR, 7300).grist(GristTypes.AMETHYST, 3210)
                .grist(GristTypes.SHALE, 5200)
                .build(output);
        // #endregion Dark Knight

        // #region Cactus Armor
        CombinationRecipeBuilder.of(ESItems.CACTUS_HELMET)
                .input(Items.LEATHER_HELMET).or().input(Items.CACTUS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CACTUS_HELMET)
                .grist(GristTypes.AMBER, 25).grist(GristTypes.IODINE, 5)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CACTUS_CHESTPLATE)
                .input(Items.LEATHER_CHESTPLATE).or().input(Items.CACTUS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CACTUS_CHESTPLATE)
                .grist(GristTypes.AMBER, 50).grist(GristTypes.MARBLE, 8)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CACTUS_LEGGINGS)
                .input(Items.LEATHER_LEGGINGS).or().input(Items.CACTUS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CACTUS_LEGGINGS)
                .grist(GristTypes.AMBER, 40).grist(GristTypes.CAULK, 7)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CACTUS_BOOTS)
                .input(Items.LEATHER_BOOTS).or().input(Items.CACTUS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CACTUS_BOOTS)
                .grist(GristTypes.AMBER, 20).grist(GristTypes.SULFUR, 4)
                .build(output);
        // #endregion Cactus Armor

        CombinationRecipeBuilder.of(ESISSItems.LICH_CROWN)
                .input(ItemRegistry.TARNISHED_CROWN.get()).and().input(MSItems.GARNET_TWIX)
                .build(output.withConditions(ISS_LOADED), modid("lich_crown_irons_spellbooks"));
        CombinationRecipeBuilder.of(ESISSItems.LICH_CROWN)
                .input(Items.IRON_HELMET).and().input(MSItems.GARNET_TWIX)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.LICH_CROWN)
                .grist(GristTypes.GARNET, 18).grist(GristTypes.MERCURY, 420).grist(GristTypes.CAULK, 249)
                .build(output);

        // #region Cosmic Plague Armor
        CombinationRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_HELMET)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(ItemRegistry.NETHERITE_MAGE_HELMET.get())
                .build(output.withConditions(ISS_LOADED), modid("cosmic_plague_helmet_irons_spellbooks"));
        CombinationRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_HELMET)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(Items.NETHERITE_HELMET)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_HELMET)
                .grist(GristTypes.SHALE, 1250).grist(GristTypes.AMETHYST, 150 * 7).grist(GristTypes.DIAMOND, 250)
                .build(output);

        CombinationRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_CHESTPLATE)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(ItemRegistry.NETHERITE_MAGE_CHESTPLATE.get())
                .build(output.withConditions(ISS_LOADED), modid("cosmic_plague_chestplate_irons_spellbooks"));
        CombinationRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_CHESTPLATE)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(Items.NETHERITE_CHESTPLATE)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_CHESTPLATE)
                .grist(GristTypes.SHALE, 1250).grist(GristTypes.AMETHYST, 150 * 10).grist(GristTypes.URANIUM, 250)
                .build(output);

        CombinationRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_LEGGINGS)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(ItemRegistry.NETHERITE_MAGE_LEGGINGS.get())
                .build(output.withConditions(ISS_LOADED), modid("cosmic_plague_leggings_irons_spellbooks"));
        CombinationRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_LEGGINGS)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(Items.NETHERITE_LEGGINGS)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_LEGGINGS)
                .grist(GristTypes.SHALE, 1250).grist(GristTypes.AMETHYST, 150 * 9).grist(GristTypes.MERCURY, 250)
                .build(output);

        CombinationRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_BOOTS)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(ItemRegistry.NETHERITE_MAGE_BOOTS.get())
                .build(output.withConditions(ISS_LOADED), modid("cosmic_plague_boots_irons_spellbooks"));
        CombinationRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_BOOTS)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(Items.NETHERITE_BOOTS)
                .build(output.withConditions(not(ISS_LOADED)));
        GristCostRecipeBuilder.of(ESISSItems.COSMIC_PLAGUE_BOOTS)
                .grist(GristTypes.SHALE, 1250).grist(GristTypes.AMETHYST, 150 * 6).grist(GristTypes.TAR, 250)
                .build(output);
        // #endregion Cosmic Plague Armor
    }

    private void modusRecipes(@Nonnull RecipeOutput output) {
        CombinationRecipeBuilder.of(ESItems.PILE_MODUS_CARD)
                .input(MSTags.Items.MODUS_CARD).and().input(Items.BRICK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.PILE_MODUS_CARD)
                .grist(GristTypes.BUILD, 234).grist(GristTypes.GARNET, 34)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.FORTUNE_MODUS_CARD)
                .input(MSTags.Items.MODUS_CARD).and().input(Items.COOKIE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.FORTUNE_MODUS_CARD)
                .grist(GristTypes.BUILD, 357).grist(GristTypes.IODINE, 28).grist(GristTypes.AMBER, 28)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.ORE_MODUS_CARD)
                .input(MSTags.Items.MODUS_CARD).or().input(Items.IRON_PICKAXE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.ORE_MODUS_CARD)
                .grist(GristTypes.BUILD, 320).grist(GristTypes.DIAMOND, 16)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.ARCHEOLOGY_MODUS_CARD)
                .input(MSTags.Items.MODUS_CARD).or().input(Items.BRUSH)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.ARCHEOLOGY_MODUS_CARD)
                .grist(GristTypes.BUILD, 210).grist(GristTypes.SHALE, 25)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.VOID_MODUS_CARD)
                .input(MSTags.Items.MODUS_CARD).and().input(Items.LAVA_BUCKET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.VOID_MODUS_CARD)
                .grist(GristTypes.BUILD, 410).grist(GristTypes.COBALT, 31).grist(GristTypes.TAR, 27)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.ENDER_MODUS_CARD)
                .input(MSTags.Items.MODUS_CARD).and().input(Items.ENDER_CHEST)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.ENDER_MODUS_CARD)
                .grist(GristTypes.BUILD, 480).grist(GristTypes.TAR, 1380).grist(GristTypes.COBALT, 480)
                .grist(GristTypes.URANIUM, 140)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.MASTERMIND_MODUS_CARD)
                .input(MSTags.Items.MODUS_CARD).or().input(MSItems.WATER_COLORS_BUCKET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.MASTERMIND_MODUS_CARD)
                .grist(GristTypes.BUILD, 260).grist(GristTypes.AMBER, 12).grist(GristTypes.CHALK, 12)
                .grist(GristTypes.AMETHYST, 12).grist(GristTypes.GARNET, 12)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.MASTERMIND_DISK)
                .input(MSItems.BLANK_DISK).and().input(ESItems.MASTERMIND_MODUS_CARD)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.MASTERMIND_DISK)
                .grist(GristTypes.BUILD, 52).grist(GristTypes.RUST, 13).grist(GristTypes.AMBER, 1)
                .grist(GristTypes.CHALK, 1).grist(GristTypes.AMETHYST, 1).grist(GristTypes.GARNET, 1)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.FURNACE_MODUS_CARD)
                .input(MSTags.Items.MODUS_CARD).or().input(Items.FURNACE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.FURNACE_MODUS_CARD)
                .grist(GristTypes.BUILD, 160).grist(GristTypes.TAR, 16)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.COMPACT_MODUS_CARD)
                .input(MSItems.SET_MODUS_CARD).and().input(Items.BARREL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.COMPACT_MODUS_CARD)
                .grist(GristTypes.BUILD, 1650).grist(GristTypes.QUARTZ, 149).grist(GristTypes.DIAMOND, 12)
                .build(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ESItems.CRAFTING_MODUS_CARD.toStack())
                .pattern("TTT")
                .pattern("TCT")
                .pattern("TTT")
                .define('T', Items.CRAFTING_TABLE)
                .define('C', MSItems.CAPTCHA_CARD)
                .unlockedBy("has_card", has(MSItems.CAPTCHA_CARD))
                .save(output, modid("shaped/crafting_modus_card"));
    }

    private void toolRecipes(@Nonnull RecipeOutput output) {
        CombinationRecipeBuilder.of(ESItems.OLD_BRUSH)
                .input(Items.BRUSH).and().input(Items.NETHERITE_SCRAP)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.OLD_BRUSH)
                .grist(GristTypes.SHALE, 8).grist(GristTypes.RUST, 32).grist(GristTypes.CAULK, 15)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.MAGNET)
                .input(MSItems.ITEM_MAGNET).or().input(Items.REDSTONE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.MAGNET)
                .grist(GristTypes.CAULK, 36).grist(GristTypes.GARNET, 8).grist(GristTypes.COBALT, 8)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.FIELD_CHARGER)
                .input(MSItems.BATTERY).and().input(Items.HONEYCOMB)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.FIELD_CHARGER)
                .grist(GristTypes.AMBER, 50).grist(GristTypes.GOLD, 50).grist(GristTypes.MERCURY, 23)
                .grist(GristTypes.AMETHYST, 9).grist(GristTypes.GARNET, 9)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.GRIST_DETECTOR)
                .input(Items.COMPASS).or().input(MSTags.Items.GRIST_CANDY)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GRIST_DETECTOR)
                .grist(GristTypes.QUARTZ, 4).grist(GristTypes.MERCURY, 44)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.GOLD_DIGGER)
                .input(Items.GOLDEN_SHOVEL).or().input(ESItems.BOONDOLLARS_FOR_IDIOTS)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GOLD_DIGGER)
                .grist(GristTypes.GOLD, 850).grist(GristTypes.BUILD, 1500).grist(GristTypes.AMBER, 600)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.VISION_BLANK)
                .input(MSItems.CUEBALL).and().input(Items.END_PORTAL_FRAME)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.VISION_BLANK)
                // As one could expect, these are in reference to Genshin Impact's release date
                // (28/10) and first beta test (21/6, but the US way)
                .grist(GristTypes.BUILD, 2810)
                .grist(GristTypes.URANIUM, 621)
                // And the 12 aspects of course!
                .grist(GristTypes.ZILLIUM, 12)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.GUMMY_RING)
                .input(ESTags.Items.CANDY_WEAPONS).and().input(Items.STRING)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GUMMY_RING)
                .grist(GristTypes.IODINE, 34).grist(GristTypes.AMBER, 23).grist(GristTypes.CHALK, 12)
                .grist(GristTypes.RUST, 3).grist(GristTypes.GOLD, 2).grist(GristTypes.RUBY, 1)
                .build(output);
    }

    private void foodRecipes(@Nonnull RecipeOutput output) {
        final RecipeOutput fdOutput = output.withConditions(FARMERSDELIGHT_LOADED);

        CombinationRecipeBuilder.of(ESItems.PIZZA)
                .input(Items.BREAD).or().input(Items.BEETROOT)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.PIZZA)
                .grist(GristTypes.AMBER, 6).grist(GristTypes.RUST, 1)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.PIZZA_SLICE)
                .input(ESItems.PIZZA).or().input(MSItems.DAGGER)
                .build(output);
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ESItems.PIZZA), Ingredient.of(ModTags.KNIVES),
                ESItems.PIZZA_SLICE, 3)
                .build(fdOutput, modid("pizza_slice").toString());
        GristCostRecipeBuilder.of(ESItems.PIZZA_SLICE)
                .grist(GristTypes.AMBER, 2).grist(GristTypes.RUST, 1)
                .build(output);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ESItems.SUSHROOM_STEW)
                .requires(MSItems.SUSHROOM)
                .requires(MSItems.MOREL_MUSHROOM)
                .requires(Items.BOWL)
                .unlockedBy("has_sushroom", has(MSItems.SUSHROOM))
                .save(output, modid("shapeless/sushroom_stew"));
        CookingPotRecipeBuilder.cookingPotRecipe(ESItems.SUSHROOM_STEW, 1, 200, 1)
                .addIngredient(MSItems.SUSHROOM)
                .addIngredient(MSItems.MOREL_MUSHROOM)
                .unlockedByAnyIngredient(MSItems.SUSHROOM, MSItems.MOREL_MUSHROOM)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(fdOutput, modid("sushroom_stew"));

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ESItems.RADBURGER)
                .requires(Tags.Items.FOODS_BREAD)
                .requires(MSItems.IRRADIATED_STEAK)
                .requires(CommonTags.FOODS_LEAFY_GREEN)
                .requires(CommonTags.CROPS_TOMATO)
                .requires(MSItems.ONION)
                .unlockedBy("has_irradiated_steak", has(MSItems.IRRADIATED_STEAK))
                .save(fdOutput, modid("shapeless/radburger"));
        CombinationRecipeBuilder.of(ESItems.RADBURGER)
                .input(MSItems.IRRADIATED_STEAK).and().input(Items.BREAD)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.RADBURGER)
                .grist(GristTypes.BUILD, 1).grist(GristTypes.AMBER, 5).grist(GristTypes.IODINE, 20)
                .grist(GristTypes.TAR, 2).grist(GristTypes.GARNET, 2).grist(GristTypes.URANIUM, 2)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CookingPotRecipeBuilder.cookingPotRecipe(ESItems.DIVINE_TEMPTATION_BLOCK, 1, 200, .5F, Items.CAULDRON)
                .addIngredient(ModItems.HAM.get())
                .addIngredient(ModItems.HAM.get())
                .addIngredient(MSItems.AMBER_GUMMY_WORM)
                .addIngredient(Items.GOLDEN_APPLE)
                .addIngredient(Items.BROWN_MUSHROOM)
                .addIngredient(Items.BROWN_MUSHROOM)
                .unlockedByAnyIngredient(MSItems.CLAW_SICKLE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(fdOutput, modid("divine_temptation").toString());
        CombinationRecipeBuilder.of(ESItems.DIVINE_TEMPTATION_BLOCK)
                .input(Items.CAULDRON).and().input(Items.GOLDEN_APPLE)
                .build(output);
        SourceGristCostBuilder.of(ESItems.DIVINE_TEMPTATION_BLOCK)
                .grist(GristTypes.AMBER, 5).grist(GristTypes.IODINE, 50)
                .source(Items.CAULDRON).source(Items.GOLDEN_APPLE)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        IrradiatingRecipeBuilder
                .irradiating(Ingredient.of(ModItems.CAKE_SLICE.get()), ESItems.YELLOWCAKE_SLICE, .1F, 20)
                .save(fdOutput, modid("irradiating/yellowcake_slice"));
        CombinationRecipeBuilder.of(ESItems.YELLOWCAKE_SLICE)
                .input(Items.CAKE).or().input(MSItems.RAW_URANIUM)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.YELLOWCAKE_SLICE)
                .grist(GristTypes.IODINE, 2).grist(GristTypes.AMBER, 2).grist(GristTypes.URANIUM, 1)
                .build(output);

        SimpleCookingRecipeBuilder
                .campfireCooking(Ingredient.of(ESItems.BEE_LARVA), RecipeCategory.FOOD, ESItems.COOKED_BEE_LARVA, .35F,
                        600)
                .unlockedBy("has_bee_egg", has(ESItems.BEE_LARVA))
                .save(output, modid("campfire/cooked_bee_larva"));
        SimpleCookingRecipeBuilder
                .smelting(Ingredient.of(ESItems.BEE_LARVA), RecipeCategory.FOOD, ESItems.COOKED_BEE_LARVA, .35F, 200)
                .unlockedBy("has_bee_egg", has(ESItems.BEE_LARVA))
                .save(output, modid("smelting/cooked_bee_larva"));
        SimpleCookingRecipeBuilder
                .smoking(Ingredient.of(ESItems.BEE_LARVA), RecipeCategory.FOOD, ESItems.COOKED_BEE_LARVA, .35F, 100)
                .unlockedBy("has_bee_egg", has(ESItems.BEE_LARVA))
                .save(output, modid("smoking/cooked_bee_larva"));

        // #region Cake Slice
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.APPLE_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.APPLE_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("apple_cake"));
        CombinationRecipeBuilder.of(ESItems.APPLE_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.APPLE)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.APPLE_CAKE)
                .requires(ESItems.APPLE_CAKE_SLICE, 7)
                .unlockedBy("has_apple_cake_slice", has(ESItems.APPLE_CAKE_SLICE))
                .save(output, modid("shapeless/apple_cake"));
        SourceGristCostBuilder.of(ESItems.APPLE_CAKE_SLICE)
                .source(MSItems.APPLE_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.BLUE_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.BLUE_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("blue_cake"));
        CombinationRecipeBuilder.of(ESItems.BLUE_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(MSItems.GLOWING_MUSHROOM)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.BLUE_CAKE)
                .requires(ESItems.BLUE_CAKE_SLICE, 7)
                .unlockedBy("has_blue_cake_slice", has(ESItems.BLUE_CAKE_SLICE))
                .save(output, modid("shapeless/blue_cake"));
        SourceGristCostBuilder.of(ESItems.BLUE_CAKE_SLICE)
                .source(MSItems.BLUE_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.COLD_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.COLD_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cold_cake"));
        CombinationRecipeBuilder.of(ESItems.COLD_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.ICE)
                .build(fdOutput, modid("cold_cake_slice_ice"));
        CombinationRecipeBuilder.of(ESItems.COLD_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.PACKED_ICE)
                .build(fdOutput, modid("cold_cake_slice_packed"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.COLD_CAKE)
                .requires(ESItems.COLD_CAKE_SLICE, 7)
                .unlockedBy("has_cold_cake_slice", has(ESItems.COLD_CAKE_SLICE))
                .save(output, modid("shapeless/cold_cake"));
        SourceGristCostBuilder.of(ESItems.COLD_CAKE_SLICE)
                .source(MSItems.COLD_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.RED_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.RED_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("red_cake"));
        CombinationRecipeBuilder.of(ESItems.RED_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.MELON_SLICE)
                .build(fdOutput, modid("red_cake_slice_melon"));
        CombinationRecipeBuilder.of(ESItems.RED_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.GLISTERING_MELON_SLICE)
                .build(fdOutput, modid("red_cake_slice_glistering"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.RED_CAKE)
                .requires(ESItems.RED_CAKE_SLICE, 7)
                .unlockedBy("has_red_cake_slice", has(ESItems.RED_CAKE_SLICE))
                .save(output, modid("shapeless/red_cake"));
        SourceGristCostBuilder.of(ESItems.RED_CAKE_SLICE)
                .source(MSItems.RED_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.HOT_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.HOT_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("hot_cake"));
        CombinationRecipeBuilder.of(ESItems.HOT_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.LAVA_BUCKET)
                .build(fdOutput, modid("hot_cake_slice_lava"));
        CombinationRecipeBuilder.of(ESItems.HOT_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.BLAZE_POWDER)
                .build(fdOutput, modid("hot_cake_slice_blaze"));
        CombinationRecipeBuilder.of(ESItems.HOT_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.MAGMA_BLOCK)
                .build(fdOutput, modid("hot_cake_slice_magma"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.HOT_CAKE)
                .requires(ESItems.HOT_CAKE_SLICE, 7)
                .unlockedBy("has_hot_cake_slice", has(ESItems.HOT_CAKE_SLICE))
                .save(output, modid("shapeless/hot_cake"));
        SourceGristCostBuilder.of(ESItems.HOT_CAKE_SLICE)
                .source(MSItems.HOT_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.REVERSE_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.REVERSE_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("reverse_cake"));
        CombinationRecipeBuilder.of(ESItems.REVERSE_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.GLASS_PANE)
                .build(fdOutput, modid("reverse_cake_slice_pane"));
        CombinationRecipeBuilder.of(ESItems.REVERSE_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.GLASS)
                .build(fdOutput, modid("reverse_cake_slice_glass"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.REVERSE_CAKE)
                .requires(ESItems.REVERSE_CAKE_SLICE, 7)
                .unlockedBy("has_reverse_cake_slice", has(ESItems.REVERSE_CAKE_SLICE))
                .save(output, modid("shapeless/reverse_cake"));
        SourceGristCostBuilder.of(ESItems.REVERSE_CAKE_SLICE)
                .source(MSItems.REVERSE_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.FUCHSIA_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.FUCHSIA_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("fuchsia_cake"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.FUCHSIA_CAKE)
                .requires(ESItems.FUCHSIA_CAKE_SLICE, 7)
                .unlockedBy("has_fuchsia_cake_slice", has(ESItems.FUCHSIA_CAKE_SLICE))
                .save(output, modid("shapeless/fuchsia_cake"));
        SourceGristCostBuilder.of(ESItems.FUCHSIA_CAKE_SLICE)
                .source(MSItems.FUCHSIA_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.NEGATIVE_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.NEGATIVE_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("negative_cake"));
        CombinationRecipeBuilder.of(ESItems.NEGATIVE_CAKE_SLICE)
                .input(ESItems.REVERSE_CAKE_SLICE).and().input(ESItems.FUCHSIA_CAKE_SLICE)
                .build(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.NEGATIVE_CAKE)
                .requires(ESItems.NEGATIVE_CAKE_SLICE, 7)
                .unlockedBy("has_negative_cake_slice", has(ESItems.NEGATIVE_CAKE_SLICE))
                .save(output, modid("shapeless/negative_cake"));
        SourceGristCostBuilder.of(ESItems.NEGATIVE_CAKE_SLICE)
                .source(MSItems.NEGATIVE_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.CARROT_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.CARROT_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("carrot_cake"));
        CombinationRecipeBuilder.of(ESItems.CARROT_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.CARROT)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.CARROT_CAKE)
                .requires(ESItems.CARROT_CAKE_SLICE, 7)
                .unlockedBy("has_carrot_cake_slice", has(ESItems.CARROT_CAKE_SLICE))
                .save(output, modid("shapeless/carrot_cake"));
        SourceGristCostBuilder.of(ESItems.CARROT_CAKE_SLICE)
                .source(MSItems.CARROT_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.CHOCOLATEY_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.CHOCOLATEY_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("chocolatey_cake"));
        CombinationRecipeBuilder.of(ESItems.CHOCOLATEY_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.COCOA_BEANS)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.CHOCOLATEY_CAKE)
                .requires(ESItems.CHOCOLATEY_CAKE_SLICE, 7)
                .unlockedBy("has_chocolatey_cake_slice", has(ESItems.CHOCOLATEY_CAKE_SLICE))
                .save(output, modid("shapeless/chocolatey_cake"));
        SourceGristCostBuilder.of(ESItems.CHOCOLATEY_CAKE_SLICE)
                .source(MSItems.CHOCOLATEY_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CombinationRecipeBuilder.of(ESItems.MOON_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(ItemTags.BEDS)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.MOON_CAKE)
                .requires(ESItems.MOON_CAKE_SLICE, 7)
                .unlockedBy("has_moon_cake_slice", has(ESItems.MOON_CAKE_SLICE))
                .save(output, modid("shapeless/moon_cake"));
        SourceGristCostBuilder.of(ESItems.MOON_CAKE_SLICE)
                .source(MSItems.MOON_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(ESItems.LEMON_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.LEMON_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("lemon_cake"));
        CombinationRecipeBuilder.of(ESItems.LEMON_CAKE_SLICE)
                .input(ESItems.APPLE_CAKE_SLICE).and().input(ESItems.LEMONNADE)
                .build(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ESItems.LEMON_CAKE)
                .requires(ESItems.LEMON_CAKE_SLICE, 7)
                .unlockedBy("has_lemon_cake_slice", has(ESItems.LEMON_CAKE_SLICE))
                .save(output, modid("shapeless/lemon_cake"));
        SourceGristCostBuilder.of(ESItems.LEMON_CAKE_SLICE)
                .source(ESItems.LEMON_CAKE.get())
                .multiplier(1f / 7f)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));
        // #endregion Cake Slice

        CookingPotRecipeBuilder.cookingPotRecipe(ESItems.MORTAL_TEMPTATION_BLOCK, 1, 200, .5F, Items.CAULDRON)
                .addIngredient(MSItems.CANDY_CORN)
                .addIngredient(MSItems.TUIX_BAR)
                .addIngredient(MSTags.Items.GRIST_CANDY)
                .addIngredient(MSItems.GOLDEN_GRASSHOPPER)
                .addIngredient(MSItems.MOREL_MUSHROOM)
                .addIngredient(Items.RED_MUSHROOM)
                .unlockedByAnyIngredient(MSItems.CLAW_SICKLE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .build(fdOutput, modid("mortal_temptation").toString());
        CombinationRecipeBuilder.of(ESItems.MORTAL_TEMPTATION_BLOCK)
                .input(ESItems.DIVINE_TEMPTATION_BLOCK).and().input(MSItems.GOLDEN_GRASSHOPPER)
                .build(output);
        SourceGristCostBuilder.of(ESItems.MORTAL_TEMPTATION_BLOCK)
                .grist(GristTypes.BUILD, 15)
                .source(Items.CAULDRON).source(MSItems.CANDY_CORN.get()).source(MSItems.TUIX_BAR.get())
                .source(MSItems.GOLDEN_GRASSHOPPER.get()).source(MSItems.MOREL_MUSHROOM.get())
                .source(Items.RED_MUSHROOM)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CookingPotRecipeBuilder.cookingPotRecipe(ESItems.CANDY_CRUNCH, 1, 200, .35F)
                .addIngredient(MSItems.CANDY_CORN)
                .addIngredient(MSItems.TUIX_BAR)
                .addIngredient(MSItems.SPOREO)
                .addIngredient(MSTags.Items.FAYGO)
                .unlockedByAnyIngredient(MSItems.CANDY_CORN, MSItems.TUIX_BAR, MSItems.SPOREO)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(fdOutput, modid("candy_crunch"));
        CombinationRecipeBuilder.of(ESItems.CANDY_CRUNCH)
                .input(Items.BOWL).and().input(MSTags.Items.FAYGO)
                .build(output);
        SourceGristCostBuilder.of(ESItems.CANDY_CRUNCH)
                .grist(GristTypes.BUILD, 2).grist(GristTypes.AMBER, 3)
                .source(MSItems.CANDY_CORN.get()).source(MSItems.TUIX_BAR.get()).source(MSItems.SPOREO.get())
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));

        CombinationRecipeBuilder.of(ESItems.HOME_DONUT)
                .input(Items.RESPAWN_ANCHOR).or().input(Items.BREAD)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.HOME_DONUT)
                .grist(GristTypes.IODINE, 12).grist(GristTypes.GARNET, 1).grist(GristTypes.CHALK, 5)
                .grist(GristTypes.ARTIFACT, 1)
                .build(output);

        CookingPotRecipeBuilder.cookingPotRecipe(ESItems.SOUR_BOMB_CANDY, 4, 200, .25F)
                .addIngredient(ESItems.LEMONNADE)
                .addIngredient(Items.SUGAR)
                .unlockedByAnyIngredient(ESItems.LEMONNADE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(fdOutput, modid("sour_bomb_candy"));
        CombinationRecipeBuilder.of(ESItems.SOUR_BOMB_CANDY)
                .input(ESItems.LEMONNADE).or().input(Items.SUGAR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SOUR_BOMB_CANDY)
                .grist(GristTypes.AMBER, 10).grist(GristTypes.CHALK, 6).grist(GristTypes.SULFUR, 2)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.COSMIC_SPOREO)
                .input(ESItems.COSMIC_PLAGUE_SPORE).and().input(Items.MILK_BUCKET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.COSMIC_SPOREO)
                .grist(GristTypes.SHALE, 12).grist(GristTypes.URANIUM, 1).grist(GristTypes.MERCURY, 3)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.SPAM)
                .input(MSItems.FOOD_CAN).and().input(ESItems.SALESMAN_GOGGLES)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SPAM)
                .grist(GristTypes.IODINE, 8).grist(GristTypes.RUST, 4).grist(GristTypes.CHALK, 2)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.LEMON_CAKE)
                .input(MSItems.APPLE_CAKE).and().input(ESItems.LEMONNADE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.LEMON_CAKE)
                .grist(GristTypes.AMBER, 21).grist(GristTypes.MARBLE, 11).grist(GristTypes.SULFUR, 9)
                .build(output);
    }

    private void drinkRecipes(@Nonnull RecipeOutput output) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ESItems.DESERT_JUICE)
                .requires(MSItems.DESERT_FRUIT, 4)
                .requires(Items.SUGAR)
                .requires(Items.GLASS_BOTTLE)
                .unlockedBy("has_desert_fruit", has(MSItems.DESERT_FRUIT))
                .save(output, modid("shapeless/desert_juice"));

        CookingPotRecipeBuilder.cookingPotRecipe(ESItems.ROCKET_JUMP, 1, 200, .25F)
                .addIngredient(ESItems.LEMONNADE)
                .addIngredient(Items.NETHER_WART)
                .unlockedByAnyIngredient(ESItems.LEMONNADE)
                .setRecipeBookTab(CookingPotRecipeBookTab.MEALS)
                .save(output.withConditions(FARMERSDELIGHT_LOADED), modid("rocket_jump"));
        CombinationRecipeBuilder.of(ESItems.ROCKET_JUMP)
                .input(ESItems.LEMONNADE).or().input(Items.GLASS_BOTTLE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.ROCKET_JUMP)
                .grist(GristTypes.AMBER, 60).grist(GristTypes.CHALK, 32).grist(GristTypes.SULFUR, 8)
                .grist(GristTypes.TAR, 3)
                .build(output);
    }

    private void blockRecipes(@Nonnull RecipeOutput output) {
        // #region Machines
        CombinationRecipeBuilder.of(ESItems.PRINTER)
                .input(Items.CRAFTER).and().input(MSItems.ALCHEMITER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.PRINTER)
                .grist(GristTypes.BUILD, 5000).grist(GristTypes.URANIUM, 175)
                .grist(GristTypes.GARNET, 500).grist(GristTypes.DIAMOND, 250)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DISPRINTER)
                .input(ESItems.PRINTER).or().input(Items.DISPENSER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DISPRINTER)
                .grist(GristTypes.BUILD, 5000).grist(GristTypes.URANIUM, 175)
                .grist(GristTypes.GARNET, 505).grist(GristTypes.DIAMOND, 250)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CHARGER)
                .input(MSItems.POWER_HUB).or().input(Items.HOPPER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CHARGER)
                .grist(GristTypes.BUILD, 50).grist(GristTypes.GOLD, 15).grist(GristTypes.URANIUM, 25)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.REACTOR)
                .input(ESItems.CHARGER).and().input(MSItems.URANIUM_BLOCK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.REACTOR)
                .grist(GristTypes.BUILD, 250).grist(GristTypes.COBALT, 170)
                .grist(GristTypes.URANIUM, 92)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.URANIUM_BLASTER)
                .input(Items.DISPENSER).or().input(MSItems.RAW_URANIUM)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.URANIUM_BLASTER)
                .grist(GristTypes.RUST, 150).grist(GristTypes.RUBY, 1)
                .grist(GristTypes.URANIUM, 25)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.DOWEL_STORAGE)
                .input(Items.CHEST).or().input(MSItems.CRUXITE_DOWEL)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.DOWEL_STORAGE)
                .grist(GristTypes.BUILD, 216).grist(GristTypes.COBALT, 1)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.CARD_STORAGE)
                .input(Items.BARREL).or().input(Items.PAPER)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CARD_STORAGE)
                .grist(GristTypes.BUILD, 216).grist(GristTypes.CHALK, 1)
                .build(output);
        // #endregion Machines

        // #region Garnet
        CombinationRecipeBuilder.of(ESItems.CUT_GARNET)
                .input(Items.AMETHYST_BLOCK).or().input(Tags.Items.DYES_RED)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CUT_GARNET)
                .grist(GristTypes.GARNET, 4)
                .build(output);

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_GARNET), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_GARNET_STAIRS)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modid("stonecutting/cut_garnet_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_GARNET_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.CUT_GARNET)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modid("shaped/cut_garnet_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_GARNET), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_GARNET_SLAB, 2)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modid("stonecutting/cut_garnet_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_GARNET_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.CUT_GARNET)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modid("shaped/cut_garnet_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_GARNET), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_GARNET_WALL)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modid("stonecutting/cut_garnet_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_GARNET_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.CUT_GARNET)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modid("shaped/cut_garnet_wall"));

        CombinationRecipeBuilder.of(ESItems.GARNET_BRICKS)
                .input(MSItems.CRUXITE_BRICKS).and().input(Tags.Items.DYES_RED)
                .build(output);
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_GARNET), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.GARNET_BRICKS)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modid("stonecutting/garnet_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.GARNET_BRICKS.toStack(4))
                .pattern("GG")
                .pattern("GG")
                .define('G', ESItems.CUT_GARNET)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modid("shaped/garnet_bricks"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.GARNET_BRICKS, ESItems.CUT_GARNET),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.GARNET_BRICK_STAIRS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modid("stonecutting/garnet_brick_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.GARNET_BRICK_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.GARNET_BRICKS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modid("shaped/garnet_brick_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.GARNET_BRICKS, ESItems.CUT_GARNET),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.GARNET_BRICK_SLAB, 2)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modid("stonecutting/garnet_brick_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.GARNET_BRICK_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.GARNET_BRICKS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modid("shaped/garnet_brick_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.GARNET_BRICKS, ESItems.CUT_GARNET),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.GARNET_BRICK_WALL)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modid("stonecutting/garnet_brick_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.GARNET_BRICK_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.GARNET_BRICKS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modid("shaped/garnet_brick_wall"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.GARNET_BRICKS, ESItems.CUT_GARNET),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CHISELED_GARNET_BRICKS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modid("stonecutting/chiseled_garnet_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CHISELED_GARNET_BRICKS.toStack(1))
                .pattern("G")
                .pattern("G")
                .define('G', ESItems.GARNET_BRICK_SLAB)
                .unlockedBy("has_garnet_brick_slab", has(ESItems.GARNET_BRICK_SLAB))
                .save(output, modid("shaped/chiseled_garnet_bricks"));
        // #endregion Garnet

        // #region Ruby
        CombinationRecipeBuilder.of(ESItems.CUT_RUBY)
                .input(ESItems.CUT_GARNET).and().input(Tags.Items.DUSTS_GLOWSTONE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.CUT_RUBY)
                .grist(GristTypes.RUBY, 4)
                .build(output);

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_RUBY), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_RUBY_STAIRS)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modid("stonecutting/cut_ruby_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_RUBY_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.CUT_RUBY)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modid("shaped/cut_ruby_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_RUBY), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_RUBY_SLAB, 2)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modid("stonecutting/cut_ruby_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_RUBY_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.CUT_RUBY)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modid("shaped/cut_ruby_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_RUBY), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_RUBY_WALL)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modid("stonecutting/cut_ruby_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_RUBY_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.CUT_RUBY)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modid("shaped/cut_ruby_wall"));

        CombinationRecipeBuilder.of(ESItems.RUBY_BRICKS)
                .input(ESItems.GARNET_BRICKS).and().input(Tags.Items.DUSTS_GLOWSTONE)
                .build(output);
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_RUBY), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.RUBY_BRICKS)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modid("stonecutting/ruby_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.RUBY_BRICKS.toStack(4))
                .pattern("GG")
                .pattern("GG")
                .define('G', ESItems.CUT_RUBY)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modid("shaped/ruby_bricks"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.RUBY_BRICKS, ESItems.CUT_RUBY),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.RUBY_BRICK_STAIRS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modid("stonecutting/ruby_brick_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.RUBY_BRICK_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.RUBY_BRICKS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modid("shaped/ruby_brick_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.RUBY_BRICKS, ESItems.CUT_RUBY),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.RUBY_BRICK_SLAB, 2)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modid("stonecutting/ruby_brick_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.RUBY_BRICK_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.RUBY_BRICKS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modid("shaped/ruby_brick_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.RUBY_BRICKS, ESItems.CUT_RUBY),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.RUBY_BRICK_WALL)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modid("stonecutting/ruby_brick_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.RUBY_BRICK_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.RUBY_BRICKS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modid("shaped/ruby_brick_wall"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.RUBY_BRICKS, ESItems.CUT_RUBY),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CHISELED_RUBY_BRICKS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modid("stonecutting/chiseled_ruby_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CHISELED_RUBY_BRICKS.toStack(1))
                .pattern("G")
                .pattern("G")
                .define('G', ESItems.RUBY_BRICK_SLAB)
                .unlockedBy("has_ruby_brick_slab", has(ESItems.RUBY_BRICK_SLAB))
                .save(output, modid("shaped/chiseled_ruby_bricks"));
        // #endregion Ruby

        // #region Cobalt
        CombinationRecipeBuilder.of(ESItems.COBALT_BLOCK)
                .input(Tags.Items.STORAGE_BLOCKS_IRON).and().input(Tags.Items.DYES_BLUE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.COBALT_BLOCK)
                .grist(GristTypes.COBALT, 45)
                .build(output);

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.COBALT_BLOCK), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.COBALT_BARS,
                        24)
                .unlockedBy("has_cobalt_block", has(ESItems.COBALT_BLOCK))
                .save(output, modid("stonecutting/cobalt_bars"));
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.COBALT_BLOCK), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.COBALT_DOOR,
                        4)
                .unlockedBy("has_cobalt_block", has(ESItems.COBALT_BLOCK))
                .save(output, modid("stonecutting/cobalt_door"));
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.COBALT_BLOCK), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.COBALT_TRAPDOOR,
                        2)
                .unlockedBy("has_cobalt_block", has(ESItems.COBALT_BLOCK))
                .save(output, modid("stonecutting/cobalt_trapdoor"));
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.COBALT_BLOCK), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.COBALT_PRESSURE_PLATE,
                        4)
                .unlockedBy("has_cobalt_block", has(ESItems.COBALT_BLOCK))
                .save(output, modid("stonecutting/cobalt_pressure_plate"));
        // #endregion Cobalt

        // #region Sulfur
        CombinationRecipeBuilder.of(ESItems.SULFUROUS_STONE)
                .input(Items.NETHERRACK).and().input(Tags.Items.DYES_YELLOW)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SULFUROUS_STONE)
                .grist(GristTypes.BUILD, 2).grist(GristTypes.SULFUR, 4)
                .build(output);

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.SULFUROUS_STONE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.SULFUROUS_STONE_STAIRS)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modid("stonecutting/sulfurous_stone_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.SULFUROUS_STONE_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.SULFUROUS_STONE)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modid("shaped/sulfurous_stone_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.SULFUROUS_STONE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.SULFUROUS_STONE_SLAB, 2)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modid("stonecutting/sulfurous_stone_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.SULFUROUS_STONE_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.SULFUROUS_STONE)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modid("shaped/sulfurous_stone_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.SULFUROUS_STONE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.SULFUROUS_STONE_WALL)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modid("stonecutting/sulfurous_stone_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.SULFUROUS_STONE_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.SULFUROUS_STONE)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modid("shaped/sulfurous_stone_wall"));
        // #endregion Sulfur

        // #region Marble
        CombinationRecipeBuilder.of(ESItems.MARBLE)
                .input(MSItems.CHALK).and().input(Tags.Items.DYES_PINK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.MARBLE)
                .grist(GristTypes.MARBLE, 4)
                .build(output);

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_STAIRS)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modid("stonecutting/marble_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modid("shaped/marble_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_SLAB, 2)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modid("stonecutting/marble_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modid("shaped/marble_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_WALL)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modid("stonecutting/marble_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modid("shaped/marble_wall"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.POLISHED_MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modid("stonecutting/polished_marble"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.POLISHED_MARBLE.toStack(4))
                .pattern("GG")
                .pattern("GG")
                .define('G', ESItems.MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modid("shaped/polished_marble"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.POLISHED_MARBLE_STAIRS)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modid("stonecutting/polished_marble_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.POLISHED_MARBLE_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.POLISHED_MARBLE)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modid("shaped/polished_marble_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.POLISHED_MARBLE_SLAB, 2)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modid("stonecutting/polished_marble_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.POLISHED_MARBLE_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.POLISHED_MARBLE)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modid("shaped/polished_marble_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.POLISHED_MARBLE_WALL)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modid("stonecutting/polished_marble_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.POLISHED_MARBLE_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.POLISHED_MARBLE)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modid("shaped/polished_marble_wall"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_BRICKS)
                .unlockedBy("has_marble", has(ESItems.MARBLE_BRICKS))
                .save(output, modid("stonecutting/marble_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_BRICKS.toStack(4))
                .pattern("GG")
                .pattern("GG")
                .define('G', ESItems.POLISHED_MARBLE)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modid("shaped/marble_bricks"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE,
                        ESItems.MARBLE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_BRICK_STAIRS)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modid("stonecutting/marble_bricks_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_BRICK_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.MARBLE_BRICKS)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modid("shaped/marble_bricks_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE,
                        ESItems.MARBLE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_BRICK_SLAB, 2)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modid("stonecutting/marble_bricks_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_BRICK_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.MARBLE_BRICKS)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modid("shaped/marble_bricks_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE,
                        ESItems.MARBLE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_BRICK_WALL)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modid("stonecutting/marble_bricks_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_BRICK_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.MARBLE_BRICKS)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modid("shaped/marble_bricks_wall"));
        // #endregion Marble

        // #region Zillium
        CombinationRecipeBuilder.of(ESItems.ZILLIUM_BRICKS)
                .input(Items.STONE_BRICKS).and().input(MSItems.ZILLIUM_SKITTLES)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.ZILLIUM_BRICKS)
                .grist(GristTypes.ZILLIUM, 4)
                .build(output);

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.ZILLIUM_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.ZILLIUM_BRICK_STAIRS)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modid("stonecutting/zillium_bricks_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.ZILLIUM_BRICK_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.ZILLIUM_BRICKS)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modid("shaped/zillium_bricks_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.ZILLIUM_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.ZILLIUM_BRICK_SLAB, 2)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modid("stonecutting/zillium_bricks_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.ZILLIUM_BRICK_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.ZILLIUM_BRICKS)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modid("shaped/zillium_bricks_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.ZILLIUM_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.ZILLIUM_BRICK_WALL)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modid("stonecutting/zillium_bricks_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.ZILLIUM_BRICK_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.ZILLIUM_BRICKS)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modid("shaped/zillium_bricks_wall"));
        // #endregion Zillium

        GristCostRecipeBuilder.of(ESItems.NORMAL_CAT_PLUSH)
                .grist(GristTypes.BUILD, 1).grist(GristTypes.CHALK, 3)
                .build(output);
    }

    private void issRecipes(@Nonnull RecipeOutput output) {
        final RecipeOutput gristCosts = output.withConditions(ISS_LOADED,
                new ConfigCondition("integration.irons_spellbooks.grist_costs"));
        final RecipeOutput combinations = output.withConditions(ISS_LOADED,
                new ConfigCondition("integration.irons_spellbooks.combinations"));
        final RecipeOutput lootCombinations = output.withConditions(ISS_LOADED,
                new ConfigCondition("integration.irons_spellbooks.loot_combinations"));

        final String pathPrefix = "integration/irons_spellbooks/";

        // #region Spell Books
        GristCostRecipeBuilder.of(ItemRegistry.EVOKER_SPELL_BOOK.get())
                .grist(GristTypes.TAR, 450).grist(GristTypes.GOLD, 360).grist(GristTypes.MARBLE, 360)
                .build(gristCosts, modid("evoker_spell_book").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.EVOKER_SPELL_BOOK.get())
                .input(ItemRegistry.RUINED_BOOK.get()).or().input(ItemRegistry.EVOCATION_RUNE.get())
                .build(lootCombinations, modid("evoker_spell_book").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.NECRONOMICON.get())
                .grist(GristTypes.IODINE, 600).grist(GristTypes.AMBER, 250).grist(GristTypes.GOLD, 450)
                .build(gristCosts, modid("necronomicon_spell_book").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.NECRONOMICON.get())
                .input(ItemRegistry.ROTTEN_SPELL_BOOK.get()).and().input(ItemRegistry.BLOOD_RUNE.get())
                .build(lootCombinations, modid("necronomicon_spell_book").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.ROTTEN_SPELL_BOOK.get())
                .grist(GristTypes.IODINE, 300).grist(GristTypes.AMBER, 50).grist(GristTypes.SHALE, 75)
                .build(gristCosts, modid("rotten_spell_book").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.ROTTEN_SPELL_BOOK.get())
                .input(ItemRegistry.IRON_SPELL_BOOK.get()).and().input(Items.ROTTEN_FLESH)
                .build(lootCombinations, modid("rotten_spell_book").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.BLAZE_SPELL_BOOK.get())
                .grist(GristTypes.TAR, 450).grist(GristTypes.SULFUR, 250).grist(GristTypes.URANIUM, 175)
                .build(gristCosts, modid("blaze_spell_book").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.BLAZE_SPELL_BOOK.get())
                .input(ItemRegistry.GOLD_SPELL_BOOK.get()).and().input(Items.BLAZE_ROD)
                .build(lootCombinations, modid("blaze_spell_book").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.VILLAGER_SPELL_BOOK.get())
                .grist(GristTypes.IODINE, 300).grist(GristTypes.CHALK, 300)
                .grist(GristTypes.GOLD, 150).grist(GristTypes.RUBY, 75)
                .build(gristCosts, modid("villager_spell_book").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.VILLAGER_SPELL_BOOK.get())
                .input(ItemRegistry.COPPER_SPELL_BOOK.get()).or().input(ItemRegistry.HOLY_RUNE.get())
                .build(lootCombinations, modid("villager_spell_book").withPrefix(pathPrefix));
        // #endregion Spell Books

        // #region Curios
        GristCostRecipeBuilder.of(ItemRegistry.SILVER_RING.get())
                .grist(GristTypes.MERCURY, 75).grist(GristTypes.DIAMOND, 5)
                .build(gristCosts, modid("silver_ring").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.SILVER_RING.get())
                .input(Items.ENCHANTING_TABLE).or().input(ItemRegistry.ARCANE_ESSENCE.get())
                .build(combinations, modid("silver_ring").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.TELEPORTATION_AMULET.get())
                .grist(GristTypes.RUST, 36).grist(GristTypes.DIAMOND, 1).grist(GristTypes.MERCURY, 2)
                .grist(GristTypes.URANIUM, 4).grist(GristTypes.ARTIFACT, 4)
                .build(gristCosts, modid("teleportation_amulet").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.TELEPORTATION_AMULET.get())
                .input(ItemRegistry.AMETHYST_RESONANCE_NECKLACE.get()).or().input(Items.ENDER_PEARL)
                .build(combinations, modid("teleportation_amulet").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.INVISIBILITY_RING.get())
                .grist(GristTypes.QUARTZ, 75).grist(GristTypes.MERCURY, 55).grist(GristTypes.ARTIFACT, 35)
                .build(gristCosts, modid("invisibility_ring").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.INVISIBILITY_RING.get())
                .input(ItemRegistry.VISIBILITY_RING.get()).and().input(Items.TINTED_GLASS)
                .build(combinations, modid("invisibility_ring").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.SIGNET_OF_THE_BETRAYER.get())
                .grist(GristTypes.RUST, 350).grist(GristTypes.CAULK, 290).grist(GristTypes.SHALE, 230)
                .build(gristCosts, modid("betrayer_signet").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.SIGNET_OF_THE_BETRAYER.get())
                .input(ItemRegistry.AFFINITY_RING.get()).and().input(Items.ECHO_SHARD)
                .build(lootCombinations, modid("betrayer_signet").withPrefix(pathPrefix));
        // #endregion Curios

        // #region Weapons
        GristCostRecipeBuilder.of(ItemRegistry.BLOOD_STAFF.get())
                .grist(GristTypes.GARNET, 800).grist(GristTypes.GOLD, 666).grist(GristTypes.TAR, 750)
                .build(gristCosts, modid("blood_staff").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.BLOOD_STAFF.get())
                .input(ItemRegistry.ARTIFICER_STAFF.get()).and().input(ItemRegistry.BLOOD_RUNE.get())
                .build(lootCombinations, modid("blood_staff").withPrefix(pathPrefix));

        SourceGristCostBuilder.of(ItemRegistry.LIGHTNING_ROD_STAFF.get())
                .source(Items.LIGHTNING_ROD).source(ItemRegistry.ENERGIZED_CORE.get())
                .build(gristCosts, modid("lightning_rod").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.MAGEHUNTER.get())
                .grist(GristTypes.DIAMOND, 25).grist(GristTypes.MERCURY, 75)
                .build(gristCosts, modid("magehunter").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.MAGEHUNTER.get())
                .input(Items.DIAMOND_SWORD).or().input(ItemRegistry.BLANK_RUNE.get())
                .build(lootCombinations, modid("magehunter").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.KEEPER_FLAMBERGE.get())
                .grist(GristTypes.RUST, 350).grist(GristTypes.GOLD, 175)
                .build(gristCosts, modid("keeper_flamberge").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.KEEPER_FLAMBERGE.get())
                .input(Items.NETHERITE_SWORD).and().input(ItemRegistry.CINDER_ESSENCE.get())
                .build(lootCombinations, modid("keeper_flamberge").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.DECREPIT_SCYTHE.get())
                .grist(GristTypes.RUST, 350).grist(GristTypes.GOLD, 175)
                .build(gristCosts, modid("decrepit_scythe").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.DECREPIT_SCYTHE.get())
                .input(MSItems.SUNRAY_HARVESTER).and().input(ItemRegistry.CINDER_ESSENCE.get())
                .build(lootCombinations, modid("decrepit_scythe").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.AUTOLOADER_CROSSBOW.get())
                .grist(GristTypes.BUILD, 21).grist(GristTypes.GOLD, 49).grist(GristTypes.AMBER, 70)
                .build(gristCosts, modid("autoloader_crossbow").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.AUTOLOADER_CROSSBOW.get())
                .input(Items.CROSSBOW).or().input(Items.HOPPER)
                .build(combinations, modid("autoloader_crossbow").withPrefix(pathPrefix));
        // #endregion Weapons

        // #region Materials
        CombinationRecipeBuilder.of(ItemRegistry.INK_COMMON.get())
                .input(MSItems.INK_SQUID_PRO_QUO).or().input(ItemRegistry.ARCANE_ESSENCE.get())
                .build(combinations, modid("common_ink").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.INK_COMMON.get())
                .grist(GristTypes.TAR, 10).grist(GristTypes.BUILD, 1)
                .build(gristCosts, modid("common_ink").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.INK_UNCOMMON.get())
                .grist(GristTypes.TAR, 40).grist(GristTypes.AMBER, 8).grist(GristTypes.BUILD, 1)
                .build(gristCosts, modid("uncommon_ink").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.INK_RARE.get())
                .grist(GristTypes.TAR, 150).grist(GristTypes.AMBER, 8).grist(GristTypes.COBALT, 8)
                .grist(GristTypes.BUILD, 1)
                .build(gristCosts, modid("rare_ink").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.INK_EPIC.get())
                .grist(GristTypes.TAR, 600).grist(GristTypes.AMBER, 8).grist(GristTypes.COBALT, 8)
                .grist(GristTypes.AMETHYST, 8).grist(GristTypes.BUILD, 1)
                .build(gristCosts, modid("epic_ink").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.INK_LEGENDARY.get())
                .grist(GristTypes.TAR, 2000).grist(GristTypes.AMBER, 8).grist(GristTypes.COBALT, 8)
                .grist(GristTypes.AMETHYST, 8).grist(GristTypes.GOLD, 8).grist(GristTypes.BUILD, 1)
                .build(gristCosts, modid("legendary_ink").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.LIGHTNING_BOTTLE.get())
                .input(Items.GLASS_BOTTLE).and().input(Items.LIGHTNING_ROD)
                .build(combinations, modid("lightning_bottle").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.LIGHTNING_BOTTLE.get())
                .grist(GristTypes.QUARTZ, 8).grist(GristTypes.CHALK, 4).grist(GristTypes.BUILD, 2)
                .build(gristCosts, modid("lightning_bottle").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.FROZEN_BONE_SHARD.get())
                .input(Items.BONE).or().input(Items.ICE)
                .build(lootCombinations, modid("frozen_bone").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.FROZEN_BONE_SHARD.get())
                .grist(GristTypes.CHALK, 7).grist(GristTypes.COBALT, 3)
                .build(gristCosts, modid("frozen_bone").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.BLOOD_VIAL.get())
                .grist(GristTypes.GARNET, 8).grist(GristTypes.BUILD, 1)
                .build(gristCosts, modid("blood_vial").withPrefix(pathPrefix));

        SourceGristCostBuilder.of(ItemRegistry.ICE_VENOM_VIAL.get())
                .source(ItemRegistry.ICY_FANG.get())
                .grist(GristTypes.COBALT, 1).grist(GristTypes.BUILD, 1)
                .build(gristCosts, modid("ice_venom_vial").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.HOGSKIN.get())
                .input(Items.LEATHER).or().input(Items.NETHERRACK)
                .build(lootCombinations, modid("hogskin").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.HOGSKIN.get())
                .grist(GristTypes.IODINE, 9).grist(GristTypes.SULFUR, 3)
                .build(gristCosts, modid("hogskin").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.BLOODY_VELLUM.get())
                .grist(GristTypes.IODINE, 9).grist(GristTypes.SULFUR, 3).grist(GristTypes.GARNET, 32)
                .build(gristCosts, modid("bloody_vellum").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.DRAGONSKIN.get())
                .grist(GristTypes.SHALE, 25).grist(GristTypes.MERCURY, 9)
                .build(gristCosts, modid("dragonskin").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.ARCANE_ESSENCE.get())
                .input(Items.GLOWSTONE_DUST).and().input(Items.ENCHANTING_TABLE)
                .build(combinations, modid("arcane_essence").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.ARCANE_ESSENCE.get())
                .grist(GristTypes.AMETHYST, 7).grist(GristTypes.COBALT, 3)
                .build(gristCosts, modid("arcane_essence").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.CHAINED_BOOK.get())
                .input(Items.BOOK).or().input(Items.CHAIN)
                .build(combinations, modid("chained_book").withPrefix(pathPrefix));
        SourceGristCostBuilder.of(ItemRegistry.CHAINED_BOOK.get())
                .source(Items.BOOK)
                .grist(GristTypes.RUST, 11)
                .build(gristCosts, modid("chained_book").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.RUINED_BOOK.get())
                .input(ItemRegistry.CHAINED_BOOK.get()).and().input(Items.SCULK)
                .build(combinations, modid("ruined_book").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.RUINED_BOOK.get())
                .grist(GristTypes.IODINE, 23).grist(GristTypes.CHALK, 5).grist(GristTypes.AMBER, 13)
                .grist(GristTypes.TAR, 31).grist(GristTypes.AMETHYST, 11)
                .build(gristCosts, modid("ruined_book").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.CINDER_ESSENCE.get())
                .input(ItemRegistry.ARCANE_ESSENCE.get()).and().input(Items.BLAZE_POWDER)
                .build(combinations, modid("cinder_essence").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.CINDER_ESSENCE.get())
                .grist(GristTypes.TAR, 17).grist(GristTypes.GOLD, 7)
                .build(gristCosts, modid("cinder_essence").withPrefix(pathPrefix));

        SourceGristCostBuilder.of(ItemRegistry.TIMELESS_SLURRY.get())
                .source(Items.ECHO_SHARD)
                .grist(GristTypes.COBALT, 2).grist(GristTypes.BUILD, 1).grist(GristTypes.CAULK, 4)
                .build(gristCosts, modid("timeless_slurry").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.MITHRIL_SCRAP.get())
                .input(ItemRegistry.ARCANE_ESSENCE.get()).and().input(Items.NETHERITE_SCRAP)
                .build(combinations, modid("mithril_scrap").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.MITHRIL_SCRAP.get())
                .grist(GristTypes.DIAMOND, 45).grist(GristTypes.MERCURY, 90)
                .build(gristCosts, modid("mithril_scrap").withPrefix(pathPrefix));

        SourceGristCostBuilder.of(ItemRegistry.RAW_MITHRIL.get())
                .source(ItemRegistry.MITHRIL_SCRAP.get())
                .build(gristCosts, modid("raw_mithril").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.DIVINE_SOULSHARD.get())
                .input(Items.NETHER_STAR).or().input(Items.ECHO_SHARD)
                .build(lootCombinations, modid("divine_soulshard").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.DIVINE_SOULSHARD.get())
                .grist(GristTypes.RUBY, 97).grist(GristTypes.AMETHYST, 150).grist(GristTypes.QUARTZ, 344)
                .build(gristCosts, modid("divine_soulshard").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.PYRIUM_INGOT.get())
                .input(Items.NETHERITE_INGOT).and().input(ItemRegistry.DIVINE_SOULSHARD.get())
                .build(combinations, modid("pyrium_ingot").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.PYRIUM_INGOT.get())
                .grist(GristTypes.GOLD, 150).grist(GristTypes.SULFUR, 250)
                .build(gristCosts, modid("pyrium_ingot").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.LOST_KNOWLEDGE_FRAGMENT.get())
                .input(ItemRegistry.SCROLL.get()).or().input(MSItems.GRIMOIRE)
                .build(combinations, modid("ancient_knowledge_fragment").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.LOST_KNOWLEDGE_FRAGMENT.get())
                .grist(GristTypes.MARBLE, 25).grist(GristTypes.COBALT, 80).grist(GristTypes.TAR, 50)
                .build(gristCosts, modid("ancient_knowledge_fragment").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.ICY_FANG.get())
                .input(Items.SPIDER_EYE).and().input(Items.PACKED_ICE)
                .build(lootCombinations, modid("icy_fang").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.ICY_FANG.get())
                .grist(GristTypes.CAULK, 29).grist(GristTypes.QUARTZ, 12)
                .build(gristCosts, modid("icy_fang").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.ICE_CRYSTAL.get())
                .input(Items.DIAMOND).and().input(Items.BLUE_ICE)
                .build(combinations, modid("permafrost_shard").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.ICE_CRYSTAL.get())
                .grist(GristTypes.DIAMOND, 54).grist(GristTypes.COBALT, 90)
                .build(gristCosts, modid("permafrost_shard").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.BLANK_RUNE.get())
                .input(Items.STONE_SLAB).or().input(ItemRegistry.ARCANE_ESSENCE.get())
                .build(combinations, modid("blank_rune").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.BLANK_RUNE.get())
                .grist(GristTypes.BUILD, 800).grist(GristTypes.RUST, 92).grist(GristTypes.AMETHYST, 32)
                .build(gristCosts, modid("blank_rune").withPrefix(pathPrefix));
        // #endregion Materials

        // #region Blocks
        CombinationRecipeBuilder.of(ItemRegistry.INSCRIPTION_TABLE_BLOCK_ITEM.get())
                .input(ItemRegistry.SCROLL.get()).and().input(Items.CRAFTING_TABLE)
                .build(combinations, modid("inscription_table").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.ACANE_ANVIL_BLOCK_ITEM.get())
                .input(Items.ANVIL).or().input(Items.AMETHYST_BLOCK)
                .build(combinations, modid("arcane_anvil").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.MITHRIL_ORE_BLOCK_ITEM.get())
                .input(ItemRegistry.MITHRIL_INGOT.get()).and().input(Items.STONE)
                .build(combinations, modid("mithril_ore").withPrefix(pathPrefix));
        SourceGristCostBuilder.of(ESTags.Items.ISS_MITHRIL_ORES)
                .grist(GristTypes.BUILD, 4).multiplier(3).source(ItemRegistry.RAW_MITHRIL.get())
                .build(gristCosts, modid("mithril_ore").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.ALCHEMIST_CAULDRON_BLOCK_ITEM.get())
                .input(Items.CAULDRON).or().input(ItemRegistry.ARCANE_ESSENCE.get())
                .build(combinations, modid("alchemist_cauldron").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ItemRegistry.PORTAL_FRAME_ITEM.get())
                .input(MSItems.TRANSPORTALIZER).and().input(ItemRegistry.MITHRIL_INGOT.get())
                .build(combinations, modid("portal_frame").withPrefix(pathPrefix));
        // #endregion Blocks

        // Misc
        GristCostRecipeBuilder.of(ItemRegistry.TARNISHED_CROWN.get())
                .grist(GristTypes.RUST, 299).grist(GristTypes.CAULK, 135)
                .build(gristCosts, modid("tarnished_crown").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.TARNISHED_CROWN.get())
                .input(Items.CHAINMAIL_HELMET).and().input(ItemRegistry.ARCANE_ESSENCE.get())
                .build(lootCombinations, modid("tarnished_crown").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.HITHER_THITHER_WAND.get())
                .grist(GristTypes.AMETHYST, 664).grist(GristTypes.GOLD, 200).grist(GristTypes.BUILD, 400)
                .build(gristCosts, modid("hither_thither_wand").withPrefix(pathPrefix));
        CombinationRecipeBuilder.of(ItemRegistry.HITHER_THITHER_WAND.get())
                .input(ItemRegistry.GRAYBEARD_STAFF.get()).and().input(ItemRegistry.PORTAL_FRAME_ITEM.get())
                .build(combinations, modid("hither_thither_wand").withPrefix(pathPrefix));

        GristCostRecipeBuilder.of(ItemRegistry.MUSIC_DISC_DEAD_KING_LULLABY.get())
                .grist(GristTypes.BUILD, 15).grist(GristTypes.CAULK, 8)
                .grist(GristTypes.IODINE, 5).grist(GristTypes.RUST, 5)
                .build(gristCosts, modid("music_disc_dead_king_lullaby").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.FLAME_STILL_BURNS_FRAGMENT.get())
                .grist(GristTypes.BUILD, 25).grist(GristTypes.RUBY, 4)
                .grist(GristTypes.GOLD, 8).grist(GristTypes.TAR, 15)
                .build(gristCosts, modid("disc_fragment_flame_still_burns").withPrefix(pathPrefix));
        GristCostRecipeBuilder.of(ItemRegistry.MUSIC_DISC_WHISPERS_OF_ICE.get())
                .grist(GristTypes.BUILD, 15).grist(GristTypes.CAULK, 8)
                .grist(GristTypes.CHALK, 5).grist(GristTypes.DIAMOND, 5)
                .build(gristCosts, modid("music_disc_whispers_of_ice").withPrefix(pathPrefix));

        CombinationRecipeBuilder.of(ESISSItems.CASSETTE_DEAD_KING_LULLABY)
                .input(ItemRegistry.MUSIC_DISC_DEAD_KING_LULLABY.get()).or().input(MSItems.CASSETTE_PLAYER)
                .build(output.withConditions(ISS_LOADED));
        GristCostRecipeBuilder.of(ESISSItems.CASSETTE_DEAD_KING_LULLABY)
                .grist(GristTypes.BUILD, 15).grist(GristTypes.CAULK, 8)
                .grist(GristTypes.IODINE, 5).grist(GristTypes.RUST, 5)
                .build(output.withConditions(ISS_LOADED));
        CombinationRecipeBuilder.of(ESISSItems.CASSETTE_FLAME_STILL_BURNS)
                .input(ItemRegistry.MUSIC_DISC_FLAME_STILL_BURNS.get()).or().input(MSItems.CASSETTE_PLAYER)
                .build(output.withConditions(ISS_LOADED));
        GristCostRecipeBuilder.of(ESISSItems.CASSETTE_FLAME_STILL_BURNS)
                .grist(GristTypes.BUILD, 15).grist(GristTypes.CAULK, 8)
                .grist(GristTypes.GOLD, 5).grist(GristTypes.GARNET, 5)
                .build(output.withConditions(ISS_LOADED));
        CombinationRecipeBuilder.of(ESISSItems.CASSETTE_WHISPERS_OF_ICE)
                .input(ItemRegistry.MUSIC_DISC_WHISPERS_OF_ICE.get()).or().input(MSItems.CASSETTE_PLAYER)
                .build(output.withConditions(ISS_LOADED));
        GristCostRecipeBuilder.of(ESISSItems.CASSETTE_WHISPERS_OF_ICE)
                .grist(GristTypes.BUILD, 15).grist(GristTypes.CAULK, 8)
                .grist(GristTypes.CHALK, 5).grist(GristTypes.DIAMOND, 5)
                .build(output.withConditions(ISS_LOADED));
    }

    private ICondition not(ICondition condition) {
        return new NotCondition(condition);
    }
}
