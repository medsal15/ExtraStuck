package com.medsal15.data;

import static com.medsal15.ExtraStuck.modid;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
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
import vectorwing.farmersdelight.client.recipebook.CookingPotRecipeBookTab;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.tag.CommonTags;
import vectorwing.farmersdelight.common.tag.ModTags;
import vectorwing.farmersdelight.data.builder.CookingPotRecipeBuilder;
import vectorwing.farmersdelight.data.builder.CuttingBoardRecipeBuilder;

public final class ESRecipeProvider extends RecipeProvider {
    private static final ICondition CREATE_LOADED = new ModLoadedCondition("create");
    private static final ICondition FARMERSDELIGHT_LOADED = new ModLoadedCondition("farmersdelight");

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
        blockRecipes(output);

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
    }

    private void weaponRecipes(@Nonnull RecipeOutput output) {
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

        CombinationRecipeBuilder.of(MSItems.QUENCH_CRUSHER)
                .input(MSItems.COPSE_CRUSHER).or().input(ESItems.DESERT_JUICE)
                .build(output, modid("combinations/quench_crusher"));
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
        // #endregion Keys

        // #region Wands
        CombinationRecipeBuilder.of(ESItems.BAGUETTE_MAGIQUE)
                .input(MSItems.STALE_BAGUETTE).and().input(MSItems.WAND)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BAGUETTE_MAGIQUE)
                .grist(GristTypes.AMBER, 12).grist(GristTypes.IODINE, 18).grist(GristTypes.SULFUR, 6)
                .build(output);
        // #endregion Wands

        // #region Canes
        CombinationRecipeBuilder.of(ESItems.BROOM)
                .input(Items.BRUSH).and().input(Items.HAY_BLOCK)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BROOM)
                .grist(GristTypes.CHALK, 39).grist(GristTypes.IODINE, 50).grist(GristTypes.SHALE, 27)
                .build(output);
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
                .build(output, modid("combinations/the_sting_hive"));
        GristCostRecipeBuilder.of(ESItems.THE_STING)
                .grist(GristTypes.GOLD, 60).grist(GristTypes.TAR, 40)
                .build(output);
        // #endregion Batons

        // #region Swords
        CombinationRecipeBuilder.of(ESItems.SUN_REAVER)
                .input(MSItems.REGISWORD).or().input(Items.DAYLIGHT_DETECTOR)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.SUN_REAVER)
                .grist(GristTypes.TAR, 400).grist(GristTypes.SHALE, 158).grist(GristTypes.COBALT, 914)
                .build(output);
        // #endregion Swords

        // #region Sickles
        CombinationRecipeBuilder.of(ESItems.NEW_MOON)
                .input(MSItems.REGISICKLE).or().input(MSItems.OIL_BUCKET)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.NEW_MOON)
                .grist(GristTypes.CAULK, 400).grist(GristTypes.TAR, 158).grist(GristTypes.SHALE, 914)
                .build(output);
        // #endregion Sickles

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

        // #region Throwables
        CombinationRecipeBuilder.of(ESItems.BEENADE)
                .input(MSItems.BARBASOL_BOMB).and().input(Items.BEEHIVE)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.BEENADE)
                .grist(GristTypes.GOLD, 10).grist(GristTypes.AMBER, 10).grist(GristTypes.TAR, 10)
                .build(output);
        // #endregion Throwables

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
                .build(fdOutput, modid("cutting/pizza_slice").toString());
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
                .build(fdOutput, modid("cooking/sushroom_stew").toString());

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
                .build(fdOutput, modid("cooking/divine_temptation").toString());
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

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ESItems.DESERT_JUICE)
                .requires(MSItems.DESERT_FRUIT, 4)
                .requires(Items.SUGAR)
                .requires(Items.GLASS_BOTTLE)
                .unlockedBy("has_desert_fruit", has(MSItems.DESERT_FRUIT))
                .save(output, modid("shapeless/desert_juice"));

        // #region Cake Slice
        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.APPLE_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.APPLE_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/apple_cake"));
        CombinationRecipeBuilder.of(ESItems.APPLE_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.APPLE)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.APPLE_CAKE)
                .requires(ESItems.APPLE_CAKE_SLICE, 7)
                .unlockedBy("has_apple_cake_slice", has(ESItems.APPLE_CAKE_SLICE))
                .save(output, modid("shapeless/apple_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.BLUE_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.BLUE_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/blue_cake"));
        CombinationRecipeBuilder.of(ESItems.BLUE_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(MSItems.GLOWING_MUSHROOM)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.BLUE_CAKE)
                .requires(ESItems.BLUE_CAKE_SLICE, 7)
                .unlockedBy("has_blue_cake_slice", has(ESItems.BLUE_CAKE_SLICE))
                .save(output, modid("shapeless/blue_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.COLD_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.COLD_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/cold_cake"));
        CombinationRecipeBuilder.of(ESItems.COLD_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.ICE)
                .build(fdOutput, modid("combinations/cold_cake_slice_ice"));
        CombinationRecipeBuilder.of(ESItems.COLD_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.PACKED_ICE)
                .build(fdOutput, modid("combinations/cold_cake_slice_packed"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.COLD_CAKE)
                .requires(ESItems.COLD_CAKE_SLICE, 7)
                .unlockedBy("has_cold_cake_slice", has(ESItems.COLD_CAKE_SLICE))
                .save(output, modid("shapeless/cold_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.RED_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.RED_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/red_cake"));
        CombinationRecipeBuilder.of(ESItems.RED_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.MELON_SLICE)
                .build(fdOutput, modid("combinations/red_cake_slice_melon"));
        CombinationRecipeBuilder.of(ESItems.RED_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.GLISTERING_MELON_SLICE)
                .build(fdOutput, modid("combinations/red_cake_slice_glistering"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.RED_CAKE)
                .requires(ESItems.RED_CAKE_SLICE, 7)
                .unlockedBy("has_red_cake_slice", has(ESItems.RED_CAKE_SLICE))
                .save(output, modid("shapeless/red_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.HOT_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.HOT_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/hot_cake"));
        CombinationRecipeBuilder.of(ESItems.HOT_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.LAVA_BUCKET)
                .build(fdOutput, modid("combinations/hot_cake_slice_lava"));
        CombinationRecipeBuilder.of(ESItems.HOT_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.BLAZE_POWDER)
                .build(fdOutput, modid("combinations/hot_cake_slice_blaze"));
        CombinationRecipeBuilder.of(ESItems.HOT_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.MAGMA_BLOCK)
                .build(fdOutput, modid("combinations/hot_cake_slice_magma"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.HOT_CAKE)
                .requires(ESItems.HOT_CAKE_SLICE, 7)
                .unlockedBy("has_hot_cake_slice", has(ESItems.HOT_CAKE_SLICE))
                .save(output, modid("shapeless/hot_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.REVERSE_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.REVERSE_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/reverse_cake"));
        CombinationRecipeBuilder.of(ESItems.REVERSE_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.GLASS_PANE)
                .build(fdOutput, modid("combinations/reverse_cake_slice_pane"));
        CombinationRecipeBuilder.of(ESItems.REVERSE_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.GLASS)
                .build(fdOutput, modid("combinations/reverse_cake_slice_glass"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.REVERSE_CAKE)
                .requires(ESItems.REVERSE_CAKE_SLICE, 7)
                .unlockedBy("has_reverse_cake_slice", has(ESItems.REVERSE_CAKE_SLICE))
                .save(output, modid("shapeless/reverse_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.FUCHSIA_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.FUCHSIA_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/fuchsia_cake"));
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.FUCHSIA_CAKE)
                .requires(ESItems.FUCHSIA_CAKE_SLICE, 7)
                .unlockedBy("has_fuchsia_cake_slice", has(ESItems.FUCHSIA_CAKE_SLICE))
                .save(output, modid("shapeless/fuchsia_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.NEGATIVE_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.NEGATIVE_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/negative_cake"));
        CombinationRecipeBuilder.of(ESItems.FUCHSIA_CAKE_SLICE)
                .input(ESItems.REVERSE_CAKE_SLICE).and().input(ESItems.FUCHSIA_CAKE_SLICE)
                .build(output);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.NEGATIVE_CAKE)
                .requires(ESItems.NEGATIVE_CAKE_SLICE, 7)
                .unlockedBy("has_negative_cake_slice", has(ESItems.NEGATIVE_CAKE_SLICE))
                .save(output, modid("shapeless/negative_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.CARROT_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.CARROT_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/carrot_cake"));
        CombinationRecipeBuilder.of(ESItems.CARROT_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.CARROT)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.CARROT_CAKE)
                .requires(ESItems.CARROT_CAKE_SLICE, 7)
                .unlockedBy("has_carrot_cake_slice", has(ESItems.CARROT_CAKE_SLICE))
                .save(output, modid("shapeless/carrot_cake"));

        CuttingBoardRecipeBuilder.cuttingRecipe(Ingredient.of(MSItems.CHOCOLATEY_CAKE),
                Ingredient.of(CommonTags.TOOLS_KNIFE), ESItems.CHOCOLATEY_CAKE_SLICE.get(), 7)
                .build(fdOutput, modid("cutting/chocolatey_cake"));
        CombinationRecipeBuilder.of(ESItems.CHOCOLATEY_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(Items.COCOA_BEANS)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.CHOCOLATEY_CAKE)
                .requires(ESItems.CHOCOLATEY_CAKE_SLICE, 7)
                .unlockedBy("has_chocolatey_cake_slice", has(ESItems.CHOCOLATEY_CAKE_SLICE))
                .save(output, modid("shapeless/chocolatey_cake"));

        CombinationRecipeBuilder.of(ESItems.MOON_CAKE_SLICE)
                .input(ModItems.CAKE_SLICE.get()).or().input(ItemTags.BEDS)
                .build(fdOutput);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, MSItems.MOON_CAKE)
                .requires(ESItems.MOON_CAKE_SLICE, 7)
                .unlockedBy("has_moon_cake_slice", has(ESItems.MOON_CAKE_SLICE))
                .save(output, modid("shapeless/moon_cake"));
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
                .build(fdOutput, modid("cooking/mortal_temptation").toString());
        CombinationRecipeBuilder.of(ESItems.MORTAL_TEMPTATION_BLOCK)
                .input(ESItems.DIVINE_TEMPTATION_BLOCK).and().input(MSItems.GOLDEN_GRASSHOPPER)
                .build(output);
        SourceGristCostBuilder.of(ESItems.MORTAL_TEMPTATION_BLOCK)
                .grist(GristTypes.BUILD, 15)
                .source(Items.CAULDRON).source(MSItems.CANDY_CORN.get()).source(MSItems.TUIX_BAR.get())
                .source(MSItems.GOLDEN_GRASSHOPPER.get()).source(MSItems.MOREL_MUSHROOM.get())
                .source(Items.RED_MUSHROOM)
                .build(output.withConditions(not(FARMERSDELIGHT_LOADED)));
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

    private ICondition not(ICondition condition) {
        return new NotCondition(condition);
    }
}
