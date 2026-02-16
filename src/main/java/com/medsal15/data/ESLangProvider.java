package com.medsal15.data;

import java.util.function.Supplier;

import com.medsal15.ESDamageTypes;
import com.medsal15.ESKindAbstratus;
import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.BlasterBlockEntity;
import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.blockentities.ReactorBlockEntity;
import com.medsal15.blockentities.StorageBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.client.gui.LoopButton;
import com.medsal15.client.programs.MastermindAppScreen;
import com.medsal15.client.screen.computer.MastermindDecodeScreen;
import com.medsal15.client.screen.computer.MastermindEncodeScreen;
import com.medsal15.compat.irons_spellbooks.items.ESISSItems;
import com.medsal15.computer.ESProgramTypes;
import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;
import com.medsal15.mobeffects.ESMobEffects;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.computer.ProgramTypes;
import com.mraof.minestuck.entity.MSAttributes;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class ESLangProvider extends LanguageProvider {
    public ESLangProvider(PackOutput output) {
        super(output, ExtraStuck.MODID, "en_us");
    }

    public static final String SHIELD_DAMAGE_KEY = ExtraStuck.MODID + ".shield_damage";
    public static final String SHIELD_EFFECT_KEY = ExtraStuck.MODID + ".shield_effect";
    public static final String SHIELD_SELF_EFFECT_KEY = ExtraStuck.MODID + ".shield_self_effect";
    public static final String SLIED_DROP_KEY = ESItems.SLIED.get().getDescriptionId() + ".drop";
    public static final String DONE_DROP_KEY = ESItems.DONE.get().getDescriptionId() + ".drop_message";
    public static final String GIFT_PROTECTION_GIFT_KEY = ESItems.GIFT_OF_PROTECTION.get().getDescriptionId()
            + ".gift_drop";
    public static final String JACKPOT_JACKPOT_KEY = ESItems.JACKPOT.get().getDescriptionId() + ".jackpot";
    public static final String JACKPOT_ROLL_KEY = ESItems.JACKPOT.get().getDescriptionId() + ".roll";
    public static final String JACKPOT_ROLLED_BASE = ESItems.JACKPOT.get().getDescriptionId() + ".rolled.";
    public static final String JACKPOT_ROLLED_0 = ESItems.JACKPOT.get().getDescriptionId() + ".rolled.0";
    public static final String JACKPOT_ROLLED_1 = ESItems.JACKPOT.get().getDescriptionId() + ".rolled.1";
    public static final String JACKPOT_ROLLED_2 = ESItems.JACKPOT.get().getDescriptionId() + ".rolled.2";
    public static final String JACKPOT_ROLLED_3 = ESItems.JACKPOT.get().getDescriptionId() + ".rolled.3";
    public static final String JACKPOT_ROLLED_4 = ESItems.JACKPOT.get().getDescriptionId() + ".rolled.4";
    public static final String JACKPOT_ROLLED_5 = ESItems.JACKPOT.get().getDescriptionId() + ".rolled.5";
    public static final String JACKPOT_ROLLED_6 = ESItems.JACKPOT.get().getDescriptionId() + ".rolled.6";
    public static final String TOKEN_TETRAHEDRON_TOKEN_KEY = ESItems.TOKEN_TETRAHEDRON.get().getDescriptionId()
            + ".token_drop";
    public static final String SBURBDB_SECONDARIES_KEY = ExtraStuck.MODID + ".sburbdb.secondaries";

    public static final String GOLDEN_PAN_HIT = "sound." + ExtraStuck.MODID + ".golden_pan_hit";
    public static final String GUN_CONTENT_KEY = ExtraStuck.MODID + ".gun_content";
    public static final String GUN_EMPTY_KEY = ExtraStuck.MODID + ".gun_empty";
    public static final String ALT_GUN_EMPTY_KEY = ExtraStuck.MODID + ".office_key.empty";
    public static final String ALT_GUN_HEAVY_KEY = ExtraStuck.MODID + ".office_key.heavy";
    public static final String RADBOW_CHARGE = ExtraStuck.MODID + ".radbow.charge";
    public static final String BEENADE_LOADED = ExtraStuck.MODID + ".beenade.loaded";
    public static final String STEAM_WEAPON_FUEL = ExtraStuck.MODID + ".steam_weapon.fuel";
    public static final String STEAM_WEAPON_LIT = ExtraStuck.MODID + ".steam_weapon.lit";
    public static final String STEAM_WEAPON_UNLIT = ExtraStuck.MODID + ".steam_weapon.unlit";
    public static final String HOME_DONUT_NO_TP = ExtraStuck.MODID + ".home_done.no_tp";
    public static final String SPAM_FOOD = ExtraStuck.MODID + ".spam.food";
    public static final String SPAM_TITLE_1 = ESItems.SPAM.get().getDescriptionId() + ".title.1";
    public static final String SPAM_TITLE_2 = ESItems.SPAM.get().getDescriptionId() + ".title.2";
    public static final String SPAM_TITLE_3 = ESItems.SPAM.get().getDescriptionId() + ".title.3";
    public static final String SPAM_DESC_1 = ESItems.SPAM.get().getDescriptionId() + ".desc.1";
    public static final String SPAM_DESC_2 = ESItems.SPAM.get().getDescriptionId() + ".desc.2";
    public static final String SPAM_DESC_3 = ESItems.SPAM.get().getDescriptionId() + ".desc.3";

    public static final String VISION_HINT_ONE = ExtraStuck.MODID + ".vision.hint_curios";
    public static final String VISION_HINT_MANY = ExtraStuck.MODID + ".vision.hint_nocurios";
    public static final String VISION_TUNE = ExtraStuck.MODID + ".vision.tune";
    public static final String VISION_TUNE_FAIL = ExtraStuck.MODID + ".vision.tune_fail";
    public static final String VISION_TUNE_POWERLESS = ExtraStuck.MODID + ".vision.tune_powerless";

    public static final String GRIST_DETECTOR_LOCATED = ExtraStuck.MODID + ".grist_detector.located";
    public static final String GRIST_DETECTOR_MODE = ExtraStuck.MODID + ".grist_detector.mode";
    public static final String GRIST_DETECTOR_ANY = ExtraStuck.MODID + ".grist_detector.any";
    public static final String GRIST_DETECTOR_COMMON = ExtraStuck.MODID + ".grist_detector.common";
    public static final String GRIST_DETECTOR_UNCOMMON = ExtraStuck.MODID + ".grist_detector.uncommon";
    public static final String MASTERMIND_GRIST_BASE = ExtraStuck.MODID + ".mastermind.grist.";
    public static final String FURNACE_MODUS_FUEL = ESItems.FURNACE_MODUS_CARD.get().getDescriptionId() + ".fuel";
    public static final String COMPACT_MODUS_STRICT_ON = ESItems.COMPACT_MODUS_CARD.get().getDescriptionId()
            + ".strict.on";
    public static final String COMPACT_MODUS_STRICT_OFF = ESItems.COMPACT_MODUS_CARD.get().getDescriptionId()
            + ".strict.off";
    public static final String CRAFTING_MODUS_ADD_RECIPE = ESItems.CRAFTING_MODUS_CARD.get().getDescriptionId()
            + ".add_recipe";

    public static final String ENERGY_STORAGE_KEY = ExtraStuck.MODID + ".energy_storage";
    public static final String FLUID_STORAGE_KEY = ExtraStuck.MODID + ".fluid_storage";
    public static final String BOONDOLLAR_VALUE_KEY = ExtraStuck.MODID + ".boondollar_value";
    public static final String BOONDOLLAR_RANGE_KEY = ExtraStuck.MODID + ".boondollar_range";

    public static final String INNATE_ENCHANT_KEY = ExtraStuck.MODID + ".innate_enchant";
    public static final String INNATE_ENCHANTS_KEY = ExtraStuck.MODID + ".innate_enchants";

    public static final String MISSING_MOD_KEY = ExtraStuck.MODID + ".missing_mod";
    public static final String MISSING_MOD_KEY_ADVANCED = ExtraStuck.MODID + ".missing_mod_advanced";

    @Override
    protected void addTranslations() {
        add("itemGroup.extrastuck", "ExtraStuck");

        add(ENERGY_STORAGE_KEY, "%1$s / %2$s FE");
        add(FLUID_STORAGE_KEY, "%1$s mB / %2$s mB %3$s");
        add(BOONDOLLAR_VALUE_KEY, "Value: %s ฿");
        add(BOONDOLLAR_RANGE_KEY, "Value: %1$s-%2$s ฿");
        add(MISSING_MOD_KEY, "Requires %s loaded to be useful");
        add(MISSING_MOD_KEY_ADVANCED, "Requires %1$s [%2$s] loaded to be useful");

        add("patchouli.extrastuck.title", "ExtraStuck Guide");
        add("patchouli.extrastuck.landing", "Unofficial ExtraStuck Walkthrough (100%% official)");

        addShields();
        addArrows();
        addWeapons();
        addArmors();
        addModuses();
        addBlocks();
        addComputerPrograms();
        addBlockEntities();
        addTools();
        addFood();
        addTags();
        addMobEffects();

        addItem(ESItems.GIFT, "Gift");
        addItemTooltip(ESItems.GIFT, "\"For you\"");
        addItemBookDescription(ESItems.GIFT,
                "Often found under trees in december, this cardboard box wrapped in colorful paper tends to contain something you desire.");
        addItem(ESItems.ANTI_DIE, "Anti Die");
        addItem(ESItems.LUCK_TOKEN, "Luck Token");
        addItemTooltip(ESItems.LUCK_TOKEN, "Just holding this makes you feel lucky!");
        addItem(ESItems.EMPTY_ENERGY_CORE, "Empty Energy Core");
        addItemTooltip(ESItems.EMPTY_ENERGY_CORE, "You forgot the uranium");
        addBlock(ESBlocks.NORMAL_CAT_PLUSH, "Normal Cat Plush");
        addBlockTooltip(ESBlocks.NORMAL_CAT_PLUSH, "In what world is that normal?");
        addItem(ESItems.MASTERMIND_DISK, "Mastermind Codebreaker Disk");
        addItemTooltip(ESItems.MASTERMIND_DISK, "Screw that. Puzzles suck.");
        addItem(ESItems.BEE_LARVA, "Bee Larva");
        addItemTooltip(ESItems.BEE_LARVA, "D'aww, it's a baybee");
        addEntityType(ESEntities.THROWN_BEE_LARVA, "Thrown Bee Larva");
        addItem(ESItems.BOONDOLLARS_FOR_IDIOTS, "Boondollars for Idiots");
        addItemTooltip(ESItems.BOONDOLLARS_FOR_IDIOTS,
                "Contains all you need to know to become the richest player in the session");
        addItem(ESItems.COSMIC_PLAGUE_SPORE, "Cosmic Plague Spore");
        addItemTooltip(ESItems.COSMIC_PLAGUE_SPORE, "Extremely toxic in airless environements");

        add("strife." + ESKindAbstratus.DICE, "Dicekind");
        add("strife." + ESKindAbstratus.SHIELD, "Shieldkind");

        addPlayerDeathMessages(ESDamageTypes.CAPTAIN_JUSTICE_PROJECTILE, "%1$s was shot by %2$s",
                "%1$s was shot by %2$s with %3$s");
        addPlayerDeathMessages(ESDamageTypes.THORN_SHIELD, "%1$s struck %2$s's shield too hard",
                "%1$s struck %2$s's %3$s too hard");
        addEffectDeathMessages(ESDamageTypes.COSMIC_PLAGUE, "%1$s succumbed to the cosmic plague",
                "%1$s succumbed to the cosmic plague while fighting %2$s");

        add(ESAdvancementsProvider.title(ESAdvancementsProvider.EXTRA_MODI), "An Extra Set");
        add(ESAdvancementsProvider.desc(ESAdvancementsProvider.EXTRA_MODI), "Obtain every Extrastuck modi");
        add(ESAdvancementsProvider.title(ESAdvancementsProvider.VISIONARY), "Visionary");
        add(ESAdvancementsProvider.desc(ESAdvancementsProvider.VISIONARY),
                "Obtain all 14 visions! You're surrounded by friends");

        // Band-aids for Minestuck's missing translation
        add(MSAttributes.UNDERLING_DAMAGE_MODIFIER.value().getDescriptionId(), "Damage Against Underlings");
        add(MSAttributes.UNDERLING_PROTECTION_MODIFIER.value().getDescriptionId(), "Damage From Underlings");
    }

    private void addShields() {
        add(SHIELD_DAMAGE_KEY, "Deals %1$s damage to melee attackers");
        add(SHIELD_EFFECT_KEY, "Applies %1$s (%2$s) to melee attackers");
        add(SHIELD_SELF_EFFECT_KEY, "Applies %1$s (%2$s) when attacked");

        addItem(ESItems.WOODEN_SHIELD, "Wooden Shield");
        addItemTooltip(ESItems.WOODEN_SHIELD, "This cheap shield is 100% fire-ready");
        addItem(ESItems.FLAME_SHIELD, "Flame Shield");
        addItemTooltip(ESItems.FLAME_SHIELD, "Uh oh, it seems your wooden shield is burning!");
        addItem(ESItems.HALT_SHIELD, "Halt Shield");
        addItemTooltip(ESItems.HALT_SHIELD, "Forces your enemies to turn back");
        addItem(ESItems.LIGHT_SHIELD, "Light Shield");
        addItem(ESItems.NON_CONTACT_CONTRACT, "Non-Contact Contract");
        addItemTooltip(ESItems.NON_CONTACT_CONTRACT, "This piece of paper has the ability to prevent contact with you");
        addItem(ESItems.GIFT_OF_PROTECTION, "Gift of Protection");
        add(GIFT_PROTECTION_GIFT_KEY, "You found a gift!");
        addItem(ESItems.SLIED, "slied");
        addItemTooltip(ESItems.SLIED, "Can this even block damage?");
        add(SLIED_DROP_KEY, "Expectedly, it could not");
        addItem(ESItems.RIOT_SHIELD, "Riot Shield");
        addItem(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE, "Captain Justice's Shield Throwable");
        addItemTooltip(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE, "Straight from a comic book and ready to throw down!");
        addItem(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD, "Captain Justice's Throwable Shield");
        addItemTooltip(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD, "Straight from a comic book and ready to protect!");
        addEntityType(ESEntities.CAPTAIN_JUSTICE_SHIELD, "Captain Justice Shield");
        addItem(ESItems.CAPITASHIELD, "Capitashield");
        addItemTooltip(ESItems.CAPITASHIELD, "Protects you, at a cost");
        addItem(ESItems.IRON_SHIELD, "Iron Shield");
        addItem(ESItems.SPIKES_ON_A_SLAB, "Spikes on a Slab");
        addItem(ESItems.JAWBITER, "Jawbiter");
        addItem(ESItems.ELDRITCH_SHIELD, "Eldritch Shield");
        addItem(ESItems.GOLD_SHIELD, "Gold Shield");
        addItem(ESItems.FLUX_SHIELD, "Flux Shield");
        addItem(ESItems.DIAMOND_SHIELD, "Diamond Shield");
        addItem(ESItems.NETHERITE_SHIELD, "Netherite Shield");
        addItem(ESItems.GARNET_SHIELD, "Garnet Shield");
        addItem(ESItems.POGO_SHIELD, "Pogo Shield");
        addItemTooltip(ESItems.POGO_SHIELD, "The shield that bounces back!");
        addItem(ESItems.RETURN_TO_SENDER, "Return to Sender");
        addItemTooltip(ESItems.RETURN_TO_SENDER, "The shield that shoots back!");
        addItem(ESISSItems.CAST_GOLD_SHIELD, "Cast Gold Shield");
        addItemTooltip(ESISSItems.CAST_GOLD_SHIELD, "Using Prospit's core has strange effects");
    }

    private void addArrows() {
        addItem(ESItems.NETHER_ARROW, "Nether Arrow");
        addItemTooltip(ESItems.NETHER_ARROW, "Likes the heat");
        addEntityType(ESEntities.NETHER_ARROW, "Nether Arrow");
        addItem(ESItems.FLAME_ARROW, "Flame Arrow");
        addItemTooltip(ESItems.FLAME_ARROW, "Extra hot");
        addEntityType(ESEntities.FLAME_ARROW, "Flame Arrow");
        addItem(ESItems.CARDBOARD_ARROW, "Cardboard Arrow");
        addEntityType(ESEntities.CARDBOARD_ARROW, "Cardboard Arrow");
        addItem(ESItems.MISSED_YOU, "missed you");
        addEntityType(ESEntities.MISSED_ARROW, "Missed Arrow");
        addItem(ESItems.SWEET_TOOTH, "Sweet Tooth");
        addItemTooltip(ESItems.SWEET_TOOTH, "Delicious and dangerous!");
        addEntityType(ESEntities.CANDY_ARROW, "Sweet Tooth");
        addItem(ESItems.LIGHTNING_ARROW, "Lightning Bolt");
        addEntityType(ESEntities.LIGHTNING_ARROW, "Lighting Arrow");
        addItem(ESItems.EXPLOSIVE_ARROW, "Explosive Arrow");
        addEntityType(ESEntities.EXPLOSIVE_ARROW, "Explosive Arrow");
        addItem(ESItems.IRON_ARROW, "Iron Arrow");
        addEntityType(ESEntities.IRON_ARROW, "Iron Arrow");
        addItem(ESItems.QUARTZ_ARROW, "Quartz Arrow");
        addEntityType(ESEntities.QUARTZ_ARROW, "Quartz Arrow");
        addItem(ESItems.PRISMARINE_ARROW, "Prismarine Arrow");
        addItemTooltip(ESItems.PRISMARINE_ARROW, "Best swimmer in class");
        addEntityType(ESEntities.PRISMARINE_ARROW, "Prismarine Arrow");
        addItem(ESItems.GLASS_ARROW, "Glass Arrow");
        addItemTooltip(ESItems.GLASS_ARROW, "Fragile");
        addEntityType(ESEntities.GLASS_ARROW, "Glass Arrow");
        addItem(ESItems.AMETHYST_ARROW, "Amethyst Arrow");
        addEntityType(ESEntities.AMETHYST_ARROW, "Amethyst Arrow");
        addItem(ESItems.PROJECDRILL, "Projecdrill");
        addEntityType(ESEntities.MINING_ARROW, "Mining Arrow");
        addItem(ESItems.CRUSADER_CROSSBOLT, "Crusader's Crossbolt");
        addItemTooltip(ESItems.CRUSADER_CROSSBOLT, "After nine years in development, " +
                "hopefully it will have been worth the wait...");
        addEntityType(ESEntities.HEALING_ARROW, "Healing Arrow");
        addItem(ESItems.END_ARROW, "End Arrow");
        addItemTooltip(ESItems.END_ARROW, "Hates flying");
        addEntityType(ESEntities.END_ARROW, "End Arrow");
        addItem(ESItems.TELERROW, "Telerrow");
        addItemTooltip(ESItems.TELERROW, "Now you're over there");
        addEntityType(ESEntities.TELEPORT_ARROW, "Teleport Arrow");
        addItem(ESItems.DRAGON_ARROW, "Dragon Arrow");
        addEntityType(ESEntities.DRAGON_ARROW, "Dragon Arrow");
    }

    private void addWeapons() {
        add(INNATE_ENCHANT_KEY, "+1 level to %2$s");
        add(INNATE_ENCHANTS_KEY, "+%1$s levels to %2$s");

        add(ALT_GUN_EMPTY_KEY, "It feels strangely hollow...");
        add(ALT_GUN_HEAVY_KEY, "It feels weirdly heavy...");

        add(STEAM_WEAPON_FUEL, "Holds %1$s fuel (%2$s)");
        add(STEAM_WEAPON_LIT, "Lit");
        add(STEAM_WEAPON_UNLIT, "Exinguished");

        // Hammers
        addItem(ESItems.GEM_BREAKER, "Gem Breaker");
        addItem(ESItems.BELL_HAMMER, "Bell Hammer");
        addItemTooltip(ESItems.BELL_HAMMER, "It rings with every strike");
        addItem(ESItems.BLIND_HAMMER, "Blind Hammer");
        addItemTooltip(ESItems.BLIND_HAMMER, "With you in the dark");
        addItem(ESItems.STEAM_HAMMER, "Steam-Powered Warhammer");
        addItemTooltip(ESItems.STEAM_HAMMER, "Stoke the Forge");
        // Dice
        addItem(ESItems.GOLD_COIN, "Gold Coin");
        addItem(ESItems.STICKY_DIE, "Sticky Die");
        addItem(ESItems.TOKEN_TETRAHEDRON, "Token Tetrahedron");
        addItemTooltip(ESItems.TOKEN_TETRAHEDRON, "Jackpot");
        add(TOKEN_TETRAHEDRON_TOKEN_KEY, "The enemy droped a token!");
        addItem(ESItems.D_ICE, "D-Ice");
        addItemTooltip(ESItems.D_ICE, "Only for the coolest rollers");
        addItem(ESItems.SLICE_AND_DICE, "Slice and Dice");
        addItem(ESItems.DONE, "D1");
        addItemExtra(ESItems.DONE, "drop_message", "You dropped down your d1");
        addItem(ESItems.D10, "D10");
        addItemTooltip(ESItems.D10, "The power of the World, in a die");
        addItem(ESItems.RAINBOW_D7, "Rainbow D7");
        addItem(ESItems.D8_NIGHT, "D8 Night");
        addItem(ESItems.CAN_DIE, "Can-Die");
        addItem(ESItems.INFINI_DIE, "Infini-Die");
        addItemTooltip(ESItems.INFINI_DIE, "Theorically infinite damage output");
        addItem(ESItems.JACKPOT, "Jackpot");
        addItemTooltip(ESItems.JACKPOT, "Let's go gambling!");
        add(JACKPOT_JACKPOT_KEY, "Rolled 3 sevens and got a reward!");
        add(JACKPOT_ROLL_KEY, "Last roll: %1$s %2$s %3$s");
        add(JACKPOT_ROLLED_0, "Bell");
        add(JACKPOT_ROLLED_1, "Horseshoe");
        add(JACKPOT_ROLLED_2, "Club");
        add(JACKPOT_ROLLED_3, "Spade");
        add(JACKPOT_ROLLED_4, "Heart");
        add(JACKPOT_ROLLED_5, "Diamond");
        add(JACKPOT_ROLLED_6, "Seven");
        // Clubs
        addItem(ESItems.SILVER_BAT, "Silver Bat");
        addItemTooltip(ESItems.SILVER_BAT, "Purify all your enemies");
        addItem(ESItems.GOLDEN_PAN, "Golden Pan");
        addItemTooltip(ESItems.GOLDEN_PAN, "A valuable frying pan, extremely rarely found in lootbo- gifts.");
        add(GOLDEN_PAN_HIT, "Golden Pan Strike");
        addItem(ESItems.ROLLING_PIN, "Rolling Pin");
        addItem(ESItems.DESTRUCTION_BAT, "Destruction's Bat");
        addItemTooltip(ESItems.DESTRUCTION_BAT, "The bigger they are, the harder they fall");
        addItem(ESItems.DEATH_MACE, "Death Mace");
        addItem(ESItems.WITHERED_MACE, "Withered Mace");
        addItem(ESItems.D8TH_M8CE, "D8th M8ce");
        // Keys
        addItem(ESItems.KEY_OF_TRIALS, "Key of Trials");
        addItem(ESItems.KEY_OF_OMINOUS_TRIALS, "Key of Ominous Trials");
        addItemTooltip(ESItems.KEY_OF_OMINOUS_TRIALS, "Reward for challenging dangerous foes");
        addItem(ESItems.OFFICE_KEY, "Office Key");
        addItem(ESItems.ANCIENT_VAULT_OPENER, "Ancient Vault Opener");
        addItem(ESItems.VAULT_MELTER, "Vault Melter");
        addItemTooltip(ESItems.VAULT_MELTER, "No vault can stand the heat. What is the point?");
        // Wands
        addItem(ESItems.BAGUETTE_MAGIQUE, "Baguette Magique");
        addItemTooltip(ESItems.BAGUETTE_MAGIQUE, "Un morceau de pain utilisé par les magiciens");
        addItem(ESItems.MONEY_MAGIC, "Money Magic");
        addItemTooltip(ESItems.MONEY_MAGIC, "Empowered by your bank account");
        // Canes
        addItem(ESItems.BROOM, "Broom");
        addItemTooltip(ESItems.BROOM, "Sweep sweep sweep");
        addItem(ESItems.IRON_CROWBAR, "Iron Crowbar");
        addItemTooltip(ESItems.IRON_CROWBAR, "A normal crowbar that pries things open");
        // Forks
        addItem(ESItems.MAGNEFORK, "Magnefork");
        addItem(ESItems.OVERCHARGED_MAGNEFORK, "Overcharged Magnefork");
        addItemTooltip(ESItems.OVERCHARGED_MAGNEFORK, "Batteries sold separately");
        addItem(ESItems.UNDERCHARGED_MAGNEFORK, "Undercharged Magnefork");
        // Chainsaws & Lipsticks
        addItem(ESItems.YELLOWCAKESAW, "YellowcakeSaw");
        addItem(ESItems.YELLOWCAKESAW_LIPSTICK, "Glowing Lipstick");
        addItemTooltip(ESItems.YELLOWCAKESAW_LIPSTICK, "Contraband");
        // Batons
        addItem(ESItems.THE_STING, "The Sting");
        addItem(ESItems.STOCKS_UPTICKER, "Stocks Upticker");
        addItemTooltip(ESItems.STOCKS_UPTICKER, "Looks like your investments are finally paying off!");
        // Swords
        addItem(ESItems.SUN_REAVER, "Sun Reaver");
        addItemTooltip(ESItems.SUN_REAVER, "To kill a god...");
        addItem(ESISSItems.LEADER_SWORD, "Leader's Sword");
        addItemTooltip(ESISSItems.LEADER_SWORD, "Your reflection judges you");
        // Sickles
        addItem(ESItems.PIRATE_HOOK, "Pirate Hook");
        addItemTooltip(ESItems.PIRATE_HOOK, "Yarr! Thy booty is mine!");
        addItem(ESItems.NEW_MOON, "New Moon");
        addItemTooltip(ESItems.NEW_MOON, "More strength...");
        addItem(ESItems.BLIGHT, "Blight");
        addItem(ESItems.END_OF_CIVILIZATION, "End of Civilization");
        addItemTooltip(ESItems.END_OF_CIVILIZATION, "Are you sure this is a good idea?");
        // Scythes
        addItem(ESItems.DEBT_REAPER, "Debt Reaper");
        addItemTooltip(ESItems.DEBT_REAPER, "You can't escape taxes");
        addItem(ESItems.LEAFBURNER, "Leafburner");
        // Fans
        addItem(ESItems.NONE_OF_YOUR_BUSINESS, "None of Your Business");
        addItemTooltip(ESItems.NONE_OF_YOUR_BUSINESS, "Privacy first");
        // Lances
        addItem(ESItems.INVESTLANCE, "Investlance");
        addItemTooltip(ESItems.INVESTLANCE, "Crash the stocks (through your enemies)");
        // Claws
        addItem(ESItems.CASHGRABBERS, "Cashgrabbers");
        addItemTooltip(ESItems.CASHGRABBERS, "Claw machines are not liable for whatever you use the claws for");
        addItem(ESItems.CASHGRABBERS_SHEATHED, "Sheathed Cashgrabbers");
        // Knives
        addItem(ESISSItems.AMETHYST_BACKSTABBER, "Amethyst Backstabber");
        addItemTooltip(ESISSItems.AMETHYST_BACKSTABBER, "Intruder Alert! A Dersite Spy is in the base!");
        // Crossbows
        addItem(ESItems.RADBOW, "Radbow");
        addItemTooltip(ESItems.RADBOW, "Silent, deadly, and quite radioactive!");
        add(RADBOW_CHARGE, "Loaded with %1$s charges");
        addEntityType(ESEntities.URANIUM_ROD, "Uranium Rod");
        addItem(ESItems.INCOMPLETE_MECHANICAL_RADBOW, "Incomplete Mechanical Radbow");
        addItemTooltip(ESItems.INCOMPLETE_MECHANICAL_RADBOW, "You can tell this is a great idea");
        addItem(ESItems.MECHANICAL_RADBOW, "Mechanical Radbow");
        addItemTooltip(ESItems.MECHANICAL_RADBOW, "An engineer's weapon of choice");
        // Bows
        addItem(ESItems.BOWWOB, "BowwoB");
        // Throwables
        addItem(ESItems.BEENADE, "Beenade");
        add(BEENADE_LOADED, "It buzzes softly");
        addEntityType(ESEntities.THROWN_BEENADE, "Thrown Beenade");
        addItem(ESItems.YIN_YANG_ORB, "Yin-Yang Orb");
        addItemTooltip(ESItems.YIN_YANG_ORB, "Only the true bloodline can use its full power");
        addItem(ESItems.LEMONNADE, "LemonNade");
        addItemTooltip(ESItems.LEMONNADE, "This lemon burns houses down");
        addEntityType(ESEntities.LEMONNADE, "LemonNade");
        // Staves
        addItem(ESISSItems.CURSED_CAT_STAFF, "Cursed Cat Staff");
        addItemTooltip(ESISSItems.CURSED_CAT_STAFF, "You can hear strange whispers when holding it...");
        addItem(ESISSItems.BLESSED_CAT_STAFF, "Blessed Cat Staff");
        addItemTooltip(ESISSItems.BLESSED_CAT_STAFF, "A powerful staff for a dangerous wanderer");
        addItem(ESISSItems.BRANCH_OF_YGGDRASIL, "Branch of Yggdrasil");
        addItemTooltip(ESISSItems.BRANCH_OF_YGGDRASIL, "Snapped straight off the World Tree");
        addItem(ESISSItems.STAFF_OF_YGGDRASIL, "Staff of Yggdrasil");
        addItem(ESISSItems.PROSPITIAN_WAND, "Prospitian Wand");
        addItemTooltip(ESISSItems.PROSPITIAN_WAND, "Big wand commonly used by Prospitians for magic");
        addItem(ESISSItems.DERSITE_WAND, "Dersite Wand");
        addItemTooltip(ESISSItems.DERSITE_WAND, "Years of misuse by Dersites have bent it");
        // Spellbooks
        addItem(ESISSItems.GRIMOIRE, "Grimoire for Manipulating the Physically Dubious");
        addItemTooltip(ESISSItems.GRIMOIRE, "The volume number is unreadable");
        addItem(ESISSItems.GEMINI_SPELLBOOK_RED, "Gemini Spell Book");
        addItem(ESISSItems.GEMINI_SPELLBOOK_BLUE, "Gemini Spell Book");
        addItem(ESISSItems.MAGE_GUY, "Mage Guy");
        addItemTooltip(ESISSItems.MAGE_GUY, "Instead of magic tricks, this one contains spells!");
        addItem(ESISSItems.PERFECTLY_UNIQUE_SPELLBOOK, "Perfectly Unique Spell Book");
        addItem(ESISSItems.SBURBDB, "SburbDB");
        addItemTooltip(ESISSItems.SBURBDB, "Database of everything in Sburb (book edition)");
        add(SBURBDB_SECONDARIES_KEY, "Can also drop %s");
        // Other Ranged
        add(GUN_CONTENT_KEY, "Loaded with %1$s %2$s");
        add(GUN_EMPTY_KEY, "Unloaded");
        addItem(ESItems.HANDGUN, "Handgun");
        addItem(ESItems.HANDGUN_BULLET, "Handgun Bullet");
        addEntityType(ESEntities.HANDGUN_BULLET, "Handgun Bullet");
        addItem(ESItems.HEAVY_HANDGUN_BULLET, "Heavy Handgun Bullet");
        addEntityType(ESEntities.HEAVY_HANDGUN_BULLET, "Heavy Handgun Bullet");
        addEntityType(ESEntities.ITEM_BULLET, "Item Bullet");
    }

    private void addArmors() {
        addItem(ESItems.CHEF_HAT, "Chef Hat");
        addItem(ESItems.CHEF_APRON, "Chef Apron");

        addItem(ESItems.HEAVY_BOOTS, "Heavy Boots");
        addItem(ESItems.PROPELLER_HAT, "Propeller Hat");
        addItem(ESItems.SALESMAN_GOGGLES, "Salesman Goggles");
        addItemTooltip(ESItems.SALESMAN_GOGGLES, "How valuable is it?");
        addItemBookDescription(ESItems.SALESMAN_GOGGLES,
                "ARE YOU SO BORED THAT YOU [[Browse catalogue]]?"
                        + " THEN FEEL FREE TO USE THESE FOR THE BEST [[Specil Deal]]!"
                        + " ALL YOU HAVE TO DO IS [[Hyperlink Blocked]]");
        addItem(ESItems.SALESWOMAN_GLASSES, "Saleswoman Glasses");
        addItemTooltip(ESItems.SALESWOMAN_GLASSES, "A \"reasonable\" price");
        addItemBookDescription(ESItems.SALESWOMAN_GLASSES,
                "Welcome, feel free to browse my wares."
                        + " Lucky you! I just so happen to have lucked upon quite the elusive treasure."
                        + " As for the price... How about... just a million boondollars?");

        addItem(ESItems.DARK_KNIGHT_HELMET, "Dark Knight Helmet");
        addItemTooltip(ESItems.DARK_KNIGHT_HELMET, "You can't see out of it");
        addItem(ESItems.DARK_KNIGHT_CHESTPLATE, "Dark Knight Chestplate");
        addItem(ESItems.DARK_KNIGHT_LEGGINGS, "Dark Knight Leggings");
        addItem(ESItems.DARK_KNIGHT_BOOTS, "Dark Knight Boots");

        addItem(ESItems.CACTUS_HELMET, "Cactus Helmet");
        addItem(ESItems.CACTUS_CHESTPLATE, "Cactus Chestplate");
        addItem(ESItems.CACTUS_LEGGINGS, "Cactus Leggings");
        addItem(ESItems.CACTUS_BOOTS, "Cactus Boots");

        addItem(ESISSItems.LICH_CROWN, "Lich Crown");
        addItem(ESISSItems.NETHER_LICH_CROWN, "Crown of the Netherlich");

        addItem(ESISSItems.COSMIC_PLAGUE_HELMET, "Cosmic Plague Helmet");
        addItem(ESISSItems.COSMIC_PLAGUE_CHESTPLATE, "Cosmic Plague Chestplate");
        addItem(ESISSItems.COSMIC_PLAGUE_LEGGINGS, "Cosmic Plague Leggings");
        addItem(ESISSItems.COSMIC_PLAGUE_BOOTS, "Cosmic Plague Boots");
    }

    private void addModuses() {
        addItem(ESItems.PILE_MODUS_CARD, "Pile Modus");
        addItemBookDescription(ESItems.PILE_MODUS_CARD,
                "The Pile Modus is a square modus."
                        + " Unlike other moduses, cards will try to arrange themselves in a square."
                        + " Taking an item will drop all those above. Hopefully, you're not near lava!");

        addItem(ESItems.FORTUNE_MODUS_CARD, "Fortune Modus");
        addItemBookDescription(ESItems.FORTUNE_MODUS_CARD,
                "The Fortune Modus gives fortune cookies."
                        + " Just eat them to get your items back."
                        + " Please do not put the fortune cookies back in the fortune modus, otherwise you'll have to eat them twice.");
        addItem(ESItems.FORTUNE_COOKIE, "Fortune Cookie");
        addItemTooltip(ESItems.FORTUNE_COOKIE, "What's inside?");

        addItem(ESItems.ORE_MODUS_CARD, "Ore Modus");
        addItemTooltip(ESItems.ORE_MODUS_CARD, "Perfect for mining lovers");
        addItemBookDescription(ESItems.ORE_MODUS_CARD,
                "The Ore Modus is perfect if you have a lot of pickaxes, or want a cheap building material."
                        + " Getting an item from it will give a Card Ore, which must be broken to get it back."
                        + " (You can even use your bare hands!)");
        addBlock(ESBlocks.CARD_ORE, "Card Ore");
        addBlockBookDescription(ESBlocks.CARD_ORE,
                "Card ore is only obtainable from an Ore Modus. Breaking it will drop whatever is stored inside. Despite its name, it can be mined by hand.");

        addItem(ESItems.ARCHEOLOGY_MODUS_CARD, "Archeology Modus");
        addItemBookDescription(ESItems.ARCHEOLOGY_MODUS_CARD,
                "The Archeology Modus is aimed at those who wish minecraft had more things to brush than a few select locations."
                        + " Retrieved items will be stored in suspicious sand or suspicious gravel, requiring a brush to obtain."
                        + " Just, don't forget to put them on a block.");

        addItem(ESItems.VOID_MODUS_CARD, "Void Modus");
        addItemTooltip(ESItems.VOID_MODUS_CARD, "Too Many Items");
        addItemBookDescription(ESItems.VOID_MODUS_CARD,
                "The Void Modus is a modus whose purpose is to delete items."
                        + " Instead of giving items back, they will be removed from your inventory."
                        + " It will also destroy any overflow. Primarily aimed at those who do not use their moduses as storage and often have a cluttered inventory.");

        addItem(ESItems.ENDER_MODUS_CARD, "Ender Modus");
        addItemTooltip(ESItems.ENDER_MODUS_CARD, "Portable Version");
        addItemBookDescription(ESItems.ENDER_MODUS_CARD,
                "The Ender Modus is a modus with direct access to your ender chest."
                        + " This means it's limited to 27 cards. Unfortunately, it also cannot get items as cards.");

        addItem(ESItems.MASTERMIND_MODUS_CARD, "Mastermind Modus");
        addItemTooltip(ESItems.MASTERMIND_MODUS_CARD, "Also known as bulls and cows");
        addItemBookDescription(ESItems.MASTERMIND_MODUS_CARD,
                "The Mastermind Modus is a simple and basic modus."
                        + " Retrieving items will instead lock them in a special card. Failing will destroy the item (and card)."
                        + " Good luck figuring out the combination!");
        add(MASTERMIND_GRIST_BASE + 0, "Blue Chalk");
        add(MASTERMIND_GRIST_BASE + 1, "Crimsite");
        add(MASTERMIND_GRIST_BASE + 2, "Slurry");
        add(MASTERMIND_GRIST_BASE + 3, "§knull");
        add(MASTERMIND_GRIST_BASE + 4, "Citrine");
        add(MASTERMIND_GRIST_BASE + 5, "Antibuild");
        addItem(ESItems.MASTERMIND_CARD, "Mastermind Card");

        addItem(ESItems.FURNACE_MODUS_CARD, "Furnace Modus");
        addItemTooltip(ESItems.FURNACE_MODUS_CARD, "Portable smelter");
        addItemBookDescription(ESItems.FURNACE_MODUS_CARD, "The Furnace Modus requires fuel to work."
                + " Extracting an item requires as much fuel as smelting it (or a second of fuel if it does not have a smelting recipe)."
                + " It will also smelt the item in question if possible.");
        add(FURNACE_MODUS_FUEL, "Can smelt (estimated) %s items");

        addItem(ESItems.COMPACT_MODUS_CARD, "Compact Modus");
        addItemBookDescription(ESItems.COMPACT_MODUS_CARD, "The Compact Modus displays fewer cards."
                + " Every item is kept in a compact pile that can be scrolled between its different variants."
                + " Only applies if the item ids are the same. Strict mode extends this to components.");
        add(COMPACT_MODUS_STRICT_ON, "Strict Mode Enabled");
        add(COMPACT_MODUS_STRICT_OFF, "Strict Mode Disabled");

        addItem(ESItems.CRAFTING_MODUS_CARD, "Crafting Modus");
        addItemTooltip(ESItems.CRAFTING_MODUS_CARD, "Portable workbench");
        addItemBookDescription(ESItems.CRAFTING_MODUS_CARD, "The Crafting Modus does not store items, but recipes."
                + " Ingredients will be pulled straight from your inventory.");
        add(CRAFTING_MODUS_ADD_RECIPE, "Add Recipe");
    }

    private void addTools() {
        addItem(ESItems.OLD_BRUSH, "Old Brush");
        addItem(ESItems.MAGNET, "Magnet");
        addItemTooltip(ESItems.MAGNET, "How do they work?");
        addItem(ESItems.FIELD_CHARGER, "Field Charger");
        addItemTooltip(ESItems.FIELD_CHARGER, "Don't lick it");
        addItem(ESItems.GRIST_DETECTOR, "Grist Detector");
        add(GRIST_DETECTOR_LOCATED, "Located %1$s");
        add(GRIST_DETECTOR_MODE, "Currently showing %1$s grist");
        add(GRIST_DETECTOR_ANY, "any");
        add(GRIST_DETECTOR_COMMON, "common");
        add(GRIST_DETECTOR_UNCOMMON, "uncommon");
        // Shovels
        addItem(ESItems.GOLD_DIGGER, "Gold Digger");
        addItemTooltip(ESItems.GOLD_DIGGER, "There's money everywhere!");
        // Visions
        add(VISION_HINT_ONE, "Wearing more than 1 vision at once will cause interferences between them");
        add(VISION_HINT_MANY, "Wearing more than %s visions at once will cause interferences between them");
        add(VISION_TUNE, "You resonate with the Vision and change it");
        add(VISION_TUNE_FAIL, "You try to resonate with the Vision but only dull it");
        add(VISION_TUNE_POWERLESS, "You try to resonate with the Vision but without enough power nothing happens");
        addItem(ESItems.VISION_BLANK, "Blank Vision");
        addItemTooltip(ESItems.VISION_BLANK, "It resonates with you");
        addItem(ESItems.VISION_DULL, "Dull Vision");
        addItemTooltip(ESItems.VISION_DULL, "It is no longer resonating");
        addItem(ESItems.VISION_SPACE, "Kosmos Vision");
        addItem(ESItems.VISION_TIME, "Khronos Vision");
        addItem(ESItems.VISION_MIND, "Menos Vision");
        addItem(ESItems.VISION_HEART, "Kardia Vision");
        addItem(ESItems.VISION_HOPE, "Elpis Vision");
        addItem(ESItems.VISION_RAGE, "Lussa Vision");
        addItem(ESItems.VISION_BREATH, "Pnoe Vision");
        addItem(ESItems.VISION_BLOOD, "Haima Vision");
        addItem(ESItems.VISION_LIFE, "Zoe Vision");
        addItem(ESItems.VISION_DOOM, "Moira Vision");
        addItem(ESItems.VISION_LIGHT, "Phoster Vision");
        addItem(ESItems.VISION_VOID, "Adeios Vision");
    }

    private void addBlockEntities() {
        // Printer
        addBlock(ESBlocks.PRINTER, "Printer");
        add(PrinterBlockEntity.TITLE, "Printer");
        add(LoopButton.LOOP, "START");

        // Disrinter
        addBlock(ESBlocks.DISPRINTER, "Disprinter");
        addBlockTooltip(ESBlocks.DISPRINTER, "Is this really an upgrade?");
        add(PrinterBlockEntity.TITLE_DISPRINTER, "Disprinter");

        // Charger
        addBlock(ESBlocks.CHARGER, "Charger");
        add(ChargerBlockEntity.TITLE, "Charger");

        // Reactor
        addBlock(ESBlocks.REACTOR, "Nuclear Reactor");
        addBlockTooltip(ESBlocks.REACTOR,
                "You don't think it's a good idea to contain so much power in such a small machine");
        add(ReactorBlockEntity.TITLE, "Nuclear Reactor");
        add(ESTags.Fluids.REACTOR_FLUIDS, "Reactor Coolants");

        // Uranium Blaster
        addBlock(ESBlocks.URANIUM_BLASTER, "Uranium Blaster");
        add(BlasterBlockEntity.TITLE, "Uranium Blaster");

        // Storage Blocks
        addBlock(ESBlocks.DOWEL_STORAGE, "Dowel Drive");
        add(StorageBlockEntity.Dowel.TITLE, "Dowel Drive");
        addBlock(ESBlocks.CARD_STORAGE, "Card Cabinet");
        add(StorageBlockEntity.Card.TITLE, "Card Cabinet");
    }

    private void addComputerPrograms() {
        addProgram(ESProgramTypes.MASTERMIND_CODEBREAKER, "Mastermind Codebreaker");

        add(MastermindAppScreen.NAME, "Mastermind Utilities");
        add(MastermindAppScreen.DECODER, "Codebreaker");
        add(MastermindAppScreen.RECODER, "Codemaker");

        add(MastermindDecodeScreen.NAME, "Mastermind Codebreaker");
        add(MastermindDecodeScreen.DECODE, "Codebreak");
        add(MastermindDecodeScreen.RESULT, "Card code:");
        add(MastermindDecodeScreen.FAILURE, "Failed to decode, are you holding a locked mastermind card?");

        add(MastermindEncodeScreen.NAME, "Mastermind Codemaker");
        add(MastermindEncodeScreen.ENCODE, "Encode");
        add(MastermindEncodeScreen.DIFFICULTY, "New difficulty: %s");
        add(MastermindEncodeScreen.RESULT, "Enjoy your easier (or harder) puzzle!");
        add(MastermindEncodeScreen.FAILURE, "Failed to encode, are you holding a locked mastermind card?");
    }

    private void addBlocks() {
        addBlock(ESBlocks.CUT_GARNET, "Cut Garnet");
        addBlock(ESBlocks.CUT_GARNET_STAIRS, "Cut Garnet Stairs");
        addBlock(ESBlocks.CUT_GARNET_SLAB, "Cut Garnet Slab");
        addBlock(ESBlocks.CUT_GARNET_WALL, "Cut Garnet Wall");
        addBlock(ESBlocks.GARNET_BRICKS, "Garnet Bricks");
        addBlock(ESBlocks.GARNET_BRICK_STAIRS, "Garnet Brick Stairs");
        addBlock(ESBlocks.GARNET_BRICK_SLAB, "Garnet Brick Slab");
        addBlock(ESBlocks.GARNET_BRICK_WALL, "Garnet Brick Wall");
        addBlock(ESBlocks.CHISELED_GARNET_BRICKS, "Chiseled Garnet Bricks");

        addBlock(ESBlocks.CUT_RUBY, "Cut Ruby");
        addBlock(ESBlocks.CUT_RUBY_STAIRS, "Cut Ruby Stairs");
        addBlock(ESBlocks.CUT_RUBY_SLAB, "Cut Ruby Slab");
        addBlock(ESBlocks.CUT_RUBY_WALL, "Cut Ruby Wall");
        addBlock(ESBlocks.RUBY_BRICKS, "Ruby Bricks");
        addBlock(ESBlocks.RUBY_BRICK_STAIRS, "Ruby Brick Stairs");
        addBlock(ESBlocks.RUBY_BRICK_SLAB, "Ruby Brick Slab");
        addBlock(ESBlocks.RUBY_BRICK_WALL, "Ruby Brick Wall");
        addBlock(ESBlocks.CHISELED_RUBY_BRICKS, "Chiseled Ruby Bricks");

        addBlock(ESBlocks.COBALT_BLOCK, "Cobalt Block");
        addBlock(ESBlocks.COBALT_BARS, "Cobalt Bars");
        addBlock(ESBlocks.COBALT_DOOR, "Cobalt Door");
        addBlock(ESBlocks.COBALT_TRAPDOOR, "Cobalt Trapdoor");
        addBlock(ESBlocks.COBALT_PRESSURE_PLATE, "Cobalt Pressure Plate");

        addBlock(ESBlocks.SULFUROUS_STONE, "Sulfurous Stone");
        addBlock(ESBlocks.SULFUROUS_STONE_STAIRS, "Sulfurous Stone Stairs");
        addBlock(ESBlocks.SULFUROUS_STONE_SLAB, "Sulfurous Stone Slab");
        addBlock(ESBlocks.SULFUROUS_STONE_WALL, "Sulfurous Stone Wall");

        addBlock(ESBlocks.MARBLE, "Marble");
        addBlock(ESBlocks.MARBLE_STAIRS, "Marble Stairs");
        addBlock(ESBlocks.MARBLE_SLAB, "Marble Slab");
        addBlock(ESBlocks.MARBLE_WALL, "Marble Wall");
        addBlock(ESBlocks.POLISHED_MARBLE, "Polished Marble");
        addBlock(ESBlocks.POLISHED_MARBLE_STAIRS, "Polished Marble Stairs");
        addBlock(ESBlocks.POLISHED_MARBLE_SLAB, "Polished Marble Slab");
        addBlock(ESBlocks.POLISHED_MARBLE_WALL, "Polished Marble Wall");
        addBlock(ESBlocks.MARBLE_BRICKS, "Marble Bricks");
        addBlock(ESBlocks.MARBLE_BRICK_STAIRS, "Marble Brick Stairs");
        addBlock(ESBlocks.MARBLE_BRICK_SLAB, "Marble Brick Slab");
        addBlock(ESBlocks.MARBLE_BRICK_WALL, "Marble Brick Wall");

        addBlock(ESBlocks.ZILLIUM_BRICKS, "Zillium Bricks");
        addBlock(ESBlocks.ZILLIUM_BRICK_STAIRS, "Zillium Brick Stairs");
        addBlock(ESBlocks.ZILLIUM_BRICK_SLAB, "Zillium Brick Slab");
        addBlock(ESBlocks.ZILLIUM_BRICK_WALL, "Zillium Brick Wall");
    }

    private void addFood() {
        addBlock(ESBlocks.PIZZA, "Pizza");
        addItem(ESItems.PIZZA_SLICE, "Pizza Slice");
        addItem(ESItems.SUSHROOM_STEW, "Sushroom Stew");
        addItemTooltip(ESItems.SUSHROOM_STEW, "Is this stewmate trustworthy?");
        addItem(ESItems.RADBURGER, "Big Rad");
        addBlock(ESBlocks.DIVINE_TEMPTATION_BLOCK, "Divine Temptation");
        addBlockTooltip(ESBlocks.DIVINE_TEMPTATION_BLOCK, "You're gonna need a bowl for that");
        addItem(ESItems.DIVINE_TEMPTATION, "Bowl of Divine Temptation");
        addItem(ESItems.YELLOWCAKE_SLICE, "Slice of Yellowcake");
        addItemTooltip(ESItems.YELLOWCAKE_SLICE, "It's cake, right?");
        addItem(ESItems.COOKED_BEE_LARVA, "Cooked Bee Larva");
        addItemTooltip(ESItems.COOKED_BEE_LARVA, "D:");
        addItem(ESItems.APPLE_CAKE_SLICE, "Slice of Apple Cake");
        addItem(ESItems.BLUE_CAKE_SLICE, "Slice of Blue Cake");
        addItem(ESItems.COLD_CAKE_SLICE, "Slice of Really Cold Cake");
        addItem(ESItems.RED_CAKE_SLICE, "Slice of Red Cake");
        addItem(ESItems.HOT_CAKE_SLICE, "Slice of Really Hot Cake");
        addItem(ESItems.REVERSE_CAKE_SLICE, "Slice of Reverse Cake");
        addItem(ESItems.FUCHSIA_CAKE_SLICE, "Slice of Fuchsia Cake");
        addItem(ESItems.NEGATIVE_CAKE_SLICE, "Slice of Negative Cake");
        addItem(ESItems.CARROT_CAKE_SLICE, "Slice of Carrot Cake");
        addItem(ESItems.CHOCOLATEY_CAKE_SLICE, "Slice of Chocolatey Cake");
        addItem(ESItems.MOON_CAKE_SLICE, "Slice of Moon Cake");
        addBlock(ESBlocks.MORTAL_TEMPTATION_BLOCK, "Mortal Temptation");
        addBlockTooltip(ESBlocks.MORTAL_TEMPTATION_BLOCK, "You're gonna need a bowl for that");
        addItem(ESItems.MORTAL_TEMPTATION, "Bowl of Mortal Temptation");
        addItemTooltip(ESItems.MORTAL_TEMPTATION, "Even consorts love it");
        addItem(ESItems.CANDY_CRUNCH, "Bowl of Candy Crunch");
        addItemTooltip(ESItems.CANDY_CRUNCH, "So much sugar can't be good for you");
        addItem(ESItems.HOME_DONUT, "Donut");
        addItemTooltip(ESItems.HOME_DONUT, "Smells like home");
        add(HOME_DONUT_NO_TP, "The donut could not find the way back home.");
        addItem(ESItems.SOUR_BOMB_CANDY, "Sour Bomb Candy");
        addItem(ESItems.COSMIC_SPOREO, "Cosmic Sporeo");
        addItemTooltip(ESItems.COSMIC_SPOREO, "A delicious quasar, now rid of all the toxins");

        addItem(ESItems.DESERT_JUICE, "Desert Juice");
        addItem(ESItems.ROCKET_JUMP, "Rocket Jump");
        addItemTooltip(ESItems.ROCKET_JUMP, "Screamin' Eagles!");
        addItem(ESItems.SPAM, "Spam");
        add(SPAM_FOOD, "Looks like there's something else inside");
        add(SPAM_TITLE_1, "Big Sale! Everything 90%* off!");
        add(SPAM_DESC_1, "We are currently having a Big Sale in all of our shops! Come now and don't miss out!");
        add(SPAM_TITLE_2, "Now's your chance to save a lot of money");
        add(SPAM_DESC_2, "All of our affiliate shops have everything on sale (up to) 75% off!");
        add(SPAM_TITLE_3, "Stained Paper");
        add(SPAM_DESC_3, "You can't make out the text, but you can tell there's a phone number.");
    }

    private void addMobEffects() {
        addEffect(ESMobEffects.TIME_STOP, "Time Stop");
        addEffectDescription(ESMobEffects.TIME_STOP, "Prevents most movement");
        addEffect(ESMobEffects.BEE_ANGRY, "Anger Bees");
        addEffectDescription(ESMobEffects.BEE_ANGRY,
                "Anger all bees to the target with a 6 blocks radius, increased by 3 per level");
        addEffect(ESMobEffects.COSMIC_PLAGUE, "Cosmic Plague");
        addEffectDescription(ESMobEffects.COSMIC_PLAGUE,
                "Deals 2 damage every 2 seconds.\nEvery level halves the time between damage ticks.\nSpreads to nearby entities with one less level");
    }

    private void addTags() {
        add(ESTags.Items.AMMO, "Ammunition");
        add(ESTags.Items.AMMO_HANDGUN, "Handgun Ammunition");
        add(ESTags.Items.SHOW_VALUE, "Armors Displaying Value");
        add(ESTags.Items.IGNORE_BYPRODUCT_CUTTING, "Ignored Cutting Byproducts");
        add(ESTags.Items.DROPS_BOONDOLLARS, "Drops Boondollars on Kill");
        add(ESTags.Items.COSMIC_PLAGUE_ARMOR, "Cosmic Plague Armor");
        add(ESTags.Items.VISION, "Visions");
        add(ESTags.Items.ACTIVE_VISION, "Activated Visions");

        add(ESTags.Blocks.PRYABLE, "Pryable with the Iron Crowbar");

        add(ESTags.EntityTypes.BEENADE_ACCEPTS, "Accepted by Beenades");
        add(ESTags.EntityTypes.COSMIC_PLAGUE_IMMUNE, "Immune to Cosmic Plague");

        add(ESTags.MobEffects.COSMIC_PLAGUE_IMMUNITY, "Prevented with cosmic plague armor");
        add(ESTags.MobEffects.COSMIC_PLAGUE_PARTIAL_IMMUNITY, "Sometimes prevented with cosmic plague armor");

        add(ESTags.DimensionTypes.COSMIC_DIMENSION_TYPES, "Dimensions triggering the Cosmic Plague Spore effect");
    }

    private void addItemTooltip(Supplier<? extends Item> key, String text) {
        addItemExtra(key, "tooltip", text);
    }

    private void addItemExtra(Supplier<? extends Item> key, String extra, String text) {
        add(((Item) (key.get())).getDescriptionId() + "." + extra, text);
    }

    private void addBlockTooltip(Supplier<? extends Block> key, String text) {
        add(((Block) (key.get())).getDescriptionId() + ".tooltip", text);
    }

    private void addPlayerDeathMessages(ResourceKey<DamageType> damage, String generic, String namedItem) {
        add("death.attack." + damage.location().toString(), generic);
        add("death.attack." + damage.location().toString() + ".item", namedItem);
    }

    private void addEffectDeathMessages(ResourceKey<DamageType> damage, String generic, String player) {
        add("death.attack." + damage.location().toString(), generic);
        add("death.attack." + damage.location().toString() + ".player", player);
    }

    private void addItemBookDescription(Supplier<? extends Item> item, String text) {
        addBookDescription(BuiltInRegistries.ITEM.getKey(item.get()), text);
    }

    private void addBlockBookDescription(Supplier<? extends Block> block, String text) {
        addBookDescription(BuiltInRegistries.BLOCK.getKey(block.get()), text);
    }

    private void addBookDescription(ResourceLocation key, String text) {
        add(key.toString() + ".book_desc", text);
    }

    private void addEffectDescription(Supplier<? extends MobEffect> effect, String desc) {
        add(((MobEffect) effect.get()).getDescriptionId() + ".description", desc);
    }

    private void addProgram(Holder<ProgramType<?>> program, String text) {
        ResourceLocation location = ProgramTypes.REGISTRY.getKey(program.value());
        if (location != null) {
            String key = location.getNamespace() + ".program." + location.getPath();
            add(key, text);
        }
    }
}
