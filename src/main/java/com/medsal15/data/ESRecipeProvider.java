package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipeBuilder;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipeBuilder;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.MSTags;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.SimpleCookingRecipeBuilder;
import net.minecraft.data.recipes.SingleItemRecipeBuilder;
import net.minecraft.data.recipes.SmithingTransformRecipeBuilder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public final class ESRecipeProvider extends RecipeProvider {
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
        blockRecipes(output);

        CombinationRecipeBuilder.of(ESItems.GIFT)
                .input(Items.PAPER).and().input(MSItems.SURPRISE_EMBRYO)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.GIFT)
                .grist(GristTypes.GARNET, 2).grist(GristTypes.GOLD, 12)
                .build(output);

        CombinationRecipeBuilder.of(ESItems.PIZZA)
                .input(Items.BREAD).or().input(Items.BEETROOT)
                .build(output);
        GristCostRecipeBuilder.of(ESItems.PIZZA)
                .grist(GristTypes.AMBER, 6).grist(GristTypes.RUST, 1)
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
                .save(output, modLoc("campfire/flame_shield"));
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
                .save(output, modLoc("smithing/netherite_shield"));

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
                .build(output, modLoc("combinations/gem_breaker_emerald"));
        CombinationRecipeBuilder.of(ESItems.GEM_BREAKER)
                .input(MSItems.REGI_HAMMER).or().input(Items.DIAMOND_BLOCK)
                .build(output, modLoc("combinations/gem_breaker_diamond"));
        GristCostRecipeBuilder.of(ESItems.GEM_BREAKER)
                .grist(GristTypes.AMETHYST, 63).grist(GristTypes.RUBY, 63).grist(GristTypes.MARBLE, 127)
                .build(output);
        // #endregion Hammers

        // #region Dice
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ESItems.GOLD_COIN.toStack())
                .pattern(" N ")
                .pattern("NIN")
                .pattern(" N ")
                .define('N', Items.GOLD_NUGGET)
                .define('I', Items.GOLD_INGOT)
                .unlockedBy("has_gold_ingot", has(Items.GOLD_INGOT))
                .save(output, modLoc("shaped/gold_coin"));
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(ESItems.GOLD_COIN), RecipeCategory.MISC,
                new ItemStack(Items.GOLD_NUGGET), 0.1F, 200)
                .unlockedBy("has_gold_coin", has(ESItems.GOLD_COIN))
                .save(output, modLoc("smelting/gold_coin"));
        SimpleCookingRecipeBuilder.blasting(Ingredient.of(ESItems.GOLD_COIN), RecipeCategory.MISC,
                new ItemStack(Items.GOLD_NUGGET), 0.1F, 100)
                .unlockedBy("has_gold_coin", has(ESItems.GOLD_COIN))
                .save(output, modLoc("blasting/gold_coin"));

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
                .grist(GristTypes.DIAMOND, 1331).grist(GristTypes.GOLD, 10).grist(GristTypes.GARNET, 999)
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
                .build(output, ExtraStuck.modid("combinations/key_of_ominous_trials_alt"));
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
                .build(output, modLoc("end_arrow_2"));
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
                .build(output, modLoc("propeller_hat_alt"));
        GristCostRecipeBuilder.of(ESItems.PROPELLER_HAT)
                .grist(GristTypes.RUST, 90).grist(GristTypes.GARNET, 17)
                .grist(GristTypes.AMBER, 17).grist(GristTypes.COBALT, 17)
                .build(output);
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
    }

    private void blockRecipes(@Nonnull RecipeOutput output) {
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
                .save(output, modLoc("stonecutting/cut_garnet_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_GARNET_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.CUT_GARNET)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modLoc("shaped/cut_garnet_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_GARNET), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_GARNET_SLAB, 2)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modLoc("stonecutting/cut_garnet_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_GARNET_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.CUT_GARNET)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modLoc("shaped/cut_garnet_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_GARNET), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_GARNET_WALL)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modLoc("stonecutting/cut_garnet_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_GARNET_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.CUT_GARNET)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modLoc("shaped/cut_garnet_wall"));

        CombinationRecipeBuilder.of(ESItems.GARNET_BRICKS)
                .input(MSItems.CRUXITE_BRICKS).and().input(Tags.Items.DYES_RED)
                .build(output);
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_GARNET), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.GARNET_BRICKS)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modLoc("stonecutting/garnet_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.GARNET_BRICKS.toStack(4))
                .pattern("GG")
                .pattern("GG")
                .define('G', ESItems.CUT_GARNET)
                .unlockedBy("has_cut_garnet", has(ESItems.CUT_GARNET))
                .save(output, modLoc("shaped/garnet_bricks"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.GARNET_BRICKS, ESItems.CUT_GARNET),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.GARNET_BRICK_STAIRS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modLoc("stonecutting/garnet_brick_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.GARNET_BRICK_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.GARNET_BRICKS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modLoc("shaped/garnet_brick_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.GARNET_BRICKS, ESItems.CUT_GARNET),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.GARNET_BRICK_SLAB, 2)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modLoc("stonecutting/garnet_brick_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.GARNET_BRICK_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.GARNET_BRICKS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modLoc("shaped/garnet_brick_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.GARNET_BRICKS, ESItems.CUT_GARNET),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.GARNET_BRICK_WALL)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modLoc("stonecutting/garnet_brick_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.GARNET_BRICK_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.GARNET_BRICKS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modLoc("shaped/garnet_brick_wall"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.GARNET_BRICKS, ESItems.CUT_GARNET),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CHISELED_GARNET_BRICKS)
                .unlockedBy("has_garnet_bricks", has(ESItems.GARNET_BRICKS))
                .save(output, modLoc("stonecutting/chiseled_garnet_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CHISELED_GARNET_BRICKS.toStack(1))
                .pattern("G")
                .pattern("G")
                .define('G', ESItems.GARNET_BRICK_SLAB)
                .unlockedBy("has_garnet_brick_slab", has(ESItems.GARNET_BRICK_SLAB))
                .save(output, modLoc("shaped/chiseled_garnet_bricks"));
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
                .save(output, modLoc("stonecutting/cut_ruby_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_RUBY_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.CUT_RUBY)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modLoc("shaped/cut_ruby_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_RUBY), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_RUBY_SLAB, 2)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modLoc("stonecutting/cut_ruby_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_RUBY_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.CUT_RUBY)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modLoc("shaped/cut_ruby_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_RUBY), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CUT_RUBY_WALL)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modLoc("stonecutting/cut_ruby_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CUT_RUBY_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.CUT_RUBY)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modLoc("shaped/cut_ruby_wall"));

        CombinationRecipeBuilder.of(ESItems.RUBY_BRICKS)
                .input(ESItems.GARNET_BRICKS).and().input(Tags.Items.DUSTS_GLOWSTONE)
                .build(output);
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.CUT_RUBY), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.RUBY_BRICKS)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modLoc("stonecutting/ruby_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.RUBY_BRICKS.toStack(4))
                .pattern("GG")
                .pattern("GG")
                .define('G', ESItems.CUT_RUBY)
                .unlockedBy("has_cut_ruby", has(ESItems.CUT_RUBY))
                .save(output, modLoc("shaped/ruby_bricks"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.RUBY_BRICKS, ESItems.CUT_RUBY),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.RUBY_BRICK_STAIRS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modLoc("stonecutting/ruby_brick_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.RUBY_BRICK_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.RUBY_BRICKS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modLoc("shaped/ruby_brick_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.RUBY_BRICKS, ESItems.CUT_RUBY),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.RUBY_BRICK_SLAB, 2)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modLoc("stonecutting/ruby_brick_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.RUBY_BRICK_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.RUBY_BRICKS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modLoc("shaped/ruby_brick_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.RUBY_BRICKS, ESItems.CUT_RUBY),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.RUBY_BRICK_WALL)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modLoc("stonecutting/ruby_brick_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.RUBY_BRICK_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.RUBY_BRICKS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modLoc("shaped/ruby_brick_wall"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.RUBY_BRICKS, ESItems.CUT_RUBY),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.CHISELED_RUBY_BRICKS)
                .unlockedBy("has_ruby_bricks", has(ESItems.RUBY_BRICKS))
                .save(output, modLoc("stonecutting/chiseled_ruby_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.CHISELED_RUBY_BRICKS.toStack(1))
                .pattern("G")
                .pattern("G")
                .define('G', ESItems.RUBY_BRICK_SLAB)
                .unlockedBy("has_ruby_brick_slab", has(ESItems.RUBY_BRICK_SLAB))
                .save(output, modLoc("shaped/chiseled_ruby_bricks"));
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
                .save(output, modLoc("stonecutting/cobalt_bars"));
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.COBALT_BLOCK), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.COBALT_DOOR,
                        4)
                .unlockedBy("has_cobalt_block", has(ESItems.COBALT_BLOCK))
                .save(output, modLoc("stonecutting/cobalt_door"));
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.COBALT_BLOCK), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.COBALT_TRAPDOOR,
                        2)
                .unlockedBy("has_cobalt_block", has(ESItems.COBALT_BLOCK))
                .save(output, modLoc("stonecutting/cobalt_trapdoor"));
        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.COBALT_BLOCK), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.COBALT_PRESSURE_PLATE,
                        4)
                .unlockedBy("has_cobalt_block", has(ESItems.COBALT_BLOCK))
                .save(output, modLoc("stonecutting/cobalt_pressure_plate"));
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
                .save(output, modLoc("stonecutting/sulfurous_stone_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.SULFUROUS_STONE_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.SULFUROUS_STONE)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modLoc("shaped/sulfurous_stone_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.SULFUROUS_STONE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.SULFUROUS_STONE_SLAB, 2)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modLoc("stonecutting/sulfurous_stone_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.SULFUROUS_STONE_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.SULFUROUS_STONE)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modLoc("shaped/sulfurous_stone_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.SULFUROUS_STONE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.SULFUROUS_STONE_WALL)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modLoc("stonecutting/sulfurous_stone_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.SULFUROUS_STONE_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.SULFUROUS_STONE)
                .unlockedBy("has_sulfurous_stone", has(ESItems.SULFUROUS_STONE))
                .save(output, modLoc("shaped/sulfurous_stone_wall"));
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
                .save(output, modLoc("stonecutting/marble_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modLoc("shaped/marble_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_SLAB, 2)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modLoc("stonecutting/marble_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modLoc("shaped/marble_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_WALL)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modLoc("stonecutting/marble_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modLoc("shaped/marble_wall"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE), RecipeCategory.BUILDING_BLOCKS,
                        ESItems.POLISHED_MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modLoc("stonecutting/polished_marble"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.POLISHED_MARBLE.toStack(4))
                .pattern("GG")
                .pattern("GG")
                .define('G', ESItems.MARBLE)
                .unlockedBy("has_marble", has(ESItems.MARBLE))
                .save(output, modLoc("shaped/polished_marble"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.POLISHED_MARBLE_STAIRS)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modLoc("stonecutting/polished_marble_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.POLISHED_MARBLE_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.POLISHED_MARBLE)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modLoc("shaped/polished_marble_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.POLISHED_MARBLE_SLAB, 2)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modLoc("stonecutting/polished_marble_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.POLISHED_MARBLE_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.POLISHED_MARBLE)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modLoc("shaped/polished_marble_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.POLISHED_MARBLE_WALL)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modLoc("stonecutting/polished_marble_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.POLISHED_MARBLE_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.POLISHED_MARBLE)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modLoc("shaped/polished_marble_wall"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_BRICKS)
                .unlockedBy("has_marble", has(ESItems.MARBLE_BRICKS))
                .save(output, modLoc("stonecutting/marble_bricks"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_BRICKS.toStack(4))
                .pattern("GG")
                .pattern("GG")
                .define('G', ESItems.POLISHED_MARBLE)
                .unlockedBy("has_polished_marble", has(ESItems.POLISHED_MARBLE))
                .save(output, modLoc("shaped/marble_bricks"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE,
                        ESItems.MARBLE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_BRICK_STAIRS)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modLoc("stonecutting/marble_bricks_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_BRICK_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.MARBLE_BRICKS)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modLoc("shaped/marble_bricks_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE,
                        ESItems.MARBLE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_BRICK_SLAB, 2)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modLoc("stonecutting/marble_bricks_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_BRICK_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.MARBLE_BRICKS)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modLoc("shaped/marble_bricks_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.MARBLE, ESItems.POLISHED_MARBLE,
                        ESItems.MARBLE_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.MARBLE_BRICK_WALL)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modLoc("stonecutting/marble_bricks_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.MARBLE_BRICK_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.MARBLE_BRICKS)
                .unlockedBy("has_marble_bricks", has(ESItems.MARBLE_BRICKS))
                .save(output, modLoc("shaped/marble_bricks_wall"));
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
                .save(output, modLoc("stonecutting/zillium_bricks_stairs"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.ZILLIUM_BRICK_STAIRS.toStack(4))
                .pattern("G  ")
                .pattern("GG ")
                .pattern("GGG")
                .define('G', ESItems.ZILLIUM_BRICKS)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modLoc("shaped/zillium_bricks_stairs"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.ZILLIUM_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.ZILLIUM_BRICK_SLAB, 2)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modLoc("stonecutting/zillium_bricks_slab"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.ZILLIUM_BRICK_SLAB.toStack(6))
                .pattern("GGG")
                .define('G', ESItems.ZILLIUM_BRICKS)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modLoc("shaped/zillium_bricks_slab"));

        SingleItemRecipeBuilder
                .stonecutting(Ingredient.of(ESItems.ZILLIUM_BRICKS),
                        RecipeCategory.BUILDING_BLOCKS,
                        ESItems.ZILLIUM_BRICK_WALL)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modLoc("stonecutting/zillium_bricks_wall"));
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ESItems.ZILLIUM_BRICK_WALL.toStack(6))
                .pattern("GGG")
                .pattern("GGG")
                .define('G', ESItems.ZILLIUM_BRICKS)
                .unlockedBy("has_zillium_bricks", has(ESItems.ZILLIUM_BRICKS))
                .save(output, modLoc("shaped/zillium_bricks_wall"));
        // #endregion Zillium
    }

    private ResourceLocation modLoc(String text) {
        return ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID, text);
    }
}
