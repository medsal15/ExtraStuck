package com.medsal15.data;

import java.util.function.Supplier;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.ChargerBlockEntity;
import com.medsal15.blockentities.PrinterBlockEntity;
import com.medsal15.blockentities.ReactorBlockEntity;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.client.gui.LoopButton;
import com.medsal15.client.programs.MastermindAppScreen;
import com.medsal15.client.screen.computer.MastermindDecodeScreen;
import com.medsal15.client.screen.computer.MastermindEncodeScreen;
import com.medsal15.computer.ESProgramTypes;
import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;
import com.medsal15.mobeffects.ESMobEffects;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.computer.ProgramType;
import com.mraof.minestuck.computer.ProgramTypes;

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

    public static final String TOKEN_TETRAHEDRON_TOKEN_KEY = ESItems.TOKEN_TETRAHEDRON.get().getDescriptionId()
            + ".token_drop";

    public static final String GOLDEN_PAN_HIT = "sound." + ExtraStuck.MODID + ".golden_pan_hit";
    public static final String GUN_CONTENT_KEY = ExtraStuck.MODID + ".gun_content";
    public static final String GUN_EMPTY_KEY = ExtraStuck.MODID + ".gun_empty";
    public static final String ALT_GUN_EMPTY_KEY = ExtraStuck.MODID + ".office_key.empty";
    public static final String ALT_GUN_HEAVY_KEY = ExtraStuck.MODID + ".office_key.heavy";
    public static final String RADBOW_CHARGE = ExtraStuck.MODID + ".radbow.charge";
    public static final String GRIST_DETECTOR_LOCATED = ExtraStuck.MODID + ".grist_detector.located";
    public static final String GRIST_DETECTOR_MODE = ExtraStuck.MODID + ".grist_detector.mode";
    public static final String GRIST_DETECTOR_ANY = ExtraStuck.MODID + ".grist_detector.any";
    public static final String GRIST_DETECTOR_COMMON = ExtraStuck.MODID + ".grist_detector.common";
    public static final String GRIST_DETECTOR_UNCOMMON = ExtraStuck.MODID + ".grist_detector.uncommon";
    public static final String MASTERMIND_GRIST_BASE = ExtraStuck.MODID + ".mastermind.grist.";

    public static final String ENERGY_STORAGE_KEY = ExtraStuck.MODID + ".energy_storage";
    public static final String FLUID_STORAGE_KEY = ExtraStuck.MODID + ".fluid_storage";
    public static final String BOONDOLLAR_VALUE_KEY = ExtraStuck.MODID + ".boondollar_value";

    public static final String INNATE_ENCHANT_KEY = ExtraStuck.MODID + ".innate_enchant";
    public static final String INNATE_ENCHANTS_KEY = ExtraStuck.MODID + ".innate_enchants";

    @Override
    protected void addTranslations() {
        add("itemGroup.extrastuck", "ExtraStuck");

        add(SHIELD_DAMAGE_KEY, "Deals %1$s damage to melee attackers");
        add(SHIELD_EFFECT_KEY, "Applies %1$s (%2$s) to melee attackers");
        add(SHIELD_SELF_EFFECT_KEY, "Applies %1$s (%2$s) when attacked");
        add(ENERGY_STORAGE_KEY, "%1$s / %2$s FE");
        add(FLUID_STORAGE_KEY, "%1$s mB / %2$s mB %3$s");
        add(BOONDOLLAR_VALUE_KEY, "Value: %1$s ฿");
        add(INNATE_ENCHANT_KEY, "+1 level to %2$s");
        add(INNATE_ENCHANTS_KEY, "+%1$s levels to %2$s");
        add(GUN_CONTENT_KEY, "Loaded with %1$s %2$s");
        add(GUN_EMPTY_KEY, "Unloaded");
        add(ALT_GUN_EMPTY_KEY, "It feels strangely hollow...");
        add(ALT_GUN_HEAVY_KEY, "It feels weirdly heavy...");
        add(GOLDEN_PAN_HIT, "Golden Pan Strike");
        add(RADBOW_CHARGE, "Loaded with %1$s charges");

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

        addEffect(ESMobEffects.TIME_STOP, "Time Stop");
        addEffectDescription(ESMobEffects.TIME_STOP, "Prevents most movement");
        addEffect(ESMobEffects.BEE_ANGRY, "Anger Bees");
        addEffectDescription(ESMobEffects.BEE_ANGRY,
                "Anger all bees to the target with a 6 blocks radius, increased by 3 per level");

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

        addDeathMessages(ESDamageTypes.CAPTAIN_JUSTICE_PROJECTILE, "%1$s was shot by %2$s",
                "%1$s was shot by %2$s with %3$s");
        addDeathMessages(ESDamageTypes.THORN_SHIELD, "%1$s struck %2$s's shield too hard",
                "%1$s struck %2$s's %3$s too hard");
    }

    private void addShields() {
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
        addItem(ESItems.GEM_BREAKER, "Gem Breaker");
        addItem(ESItems.BELL_HAMMER, "Bell Hammer");
        addItemTooltip(ESItems.BELL_HAMMER, "It rings with every strike");
        addItem(ESItems.BLIND_HAMMER, "Blind Hammer");
        addItemTooltip(ESItems.BLIND_HAMMER, "With you in the dark");

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

        addItem(ESItems.SILVER_BAT, "Silver Bat");
        addItemTooltip(ESItems.SILVER_BAT, "Purify all your enemies");
        addItem(ESItems.GOLDEN_PAN, "Golden Pan");
        addItemTooltip(ESItems.GOLDEN_PAN, "A valuable frying pan, extremely rarely found in lootbo- gifts.");
        addItem(ESItems.ROLLING_PIN, "Rolling Pin");

        addItem(ESItems.KEY_OF_TRIALS, "Key of Trials");
        addItem(ESItems.KEY_OF_OMINOUS_TRIALS, "Key of Ominous Trials");
        addItemTooltip(ESItems.KEY_OF_OMINOUS_TRIALS, "Reward for challenging dangerous foes");
        addItem(ESItems.OFFICE_KEY, "Office Key");

        addItem(ESItems.BAGUETTE_MAGIQUE, "Baguette Magique");
        addItemTooltip(ESItems.BAGUETTE_MAGIQUE, "Un morceau de pain utilisé par les magiciens");

        addItem(ESItems.BROOM, "Broom");
        addItemTooltip(ESItems.BROOM, "Sweep sweep sweep");

        addItem(ESItems.MAGNEFORK, "Magnefork");
        addItem(ESItems.OVERCHARGED_MAGNEFORK, "Overcharged Magnefork");
        addItemTooltip(ESItems.OVERCHARGED_MAGNEFORK, "Batteries sold separately");
        addItem(ESItems.UNDERCHARGED_MAGNEFORK, "Undercharged Magnefork");

        addItem(ESItems.YELLOWCAKESAW, "YellowcakeSaw");
        addItem(ESItems.YELLOWCAKESAW_LIPSTICK, "Glowing Lipstick");
        addItemTooltip(ESItems.YELLOWCAKESAW_LIPSTICK, "Contraband");

        addItem(ESItems.RADBOW, "Radbow");
        addItemTooltip(ESItems.RADBOW, "Silent, deadly, and quite radioactive!");
        addEntityType(ESEntities.URANIUM_ROD, "Uranium Rod");
        addItem(ESItems.INCOMPLETE_MECHANICAL_RADBOW, "Incomplete Mechanical Radbow");
        addItemTooltip(ESItems.INCOMPLETE_MECHANICAL_RADBOW, "You can tell this is a great idea");
        addItem(ESItems.MECHANICAL_RADBOW, "Mechanical Radbow");
        addItemTooltip(ESItems.MECHANICAL_RADBOW, "An engineer's weapon of choice");

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

        addItem(ESItems.DARK_KNIGHT_HELMET, "Dark Knight Helmet");
        addItemTooltip(ESItems.DARK_KNIGHT_HELMET, "You can't see out of it");
        addItem(ESItems.DARK_KNIGHT_CHESTPLATE, "Dark Knight Chestplate");
        addItem(ESItems.DARK_KNIGHT_LEGGINGS, "Dark Knight Leggings");
        addItem(ESItems.DARK_KNIGHT_BOOTS, "Dark Knight Boots");
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

        addItem(ESItems.ORE_MODUS_CARD, "Ore Modus");
        addItemTooltip(ESItems.ORE_MODUS_CARD, "Perfect for mining lovers");
        addItemBookDescription(ESItems.ORE_MODUS_CARD,
                "The Ore Modus is perfect if you have a lot of pickaxes, or want a cheap building material."
                        + " Getting an item from it will give a Card Ore, which must be broken to get it back."
                        + " (You can even use your bare hands!)");

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

        addItem(ESItems.FORTUNE_COOKIE, "Fortune Cookie");
        addItemTooltip(ESItems.FORTUNE_COOKIE, "What's inside?");
        addBlock(ESBlocks.CARD_ORE, "Card Ore");
        addBlockBookDescription(ESBlocks.CARD_ORE,
                "Card ore is only obtainable from an Ore Modus. Breaking it will drop whatever is stored inside. Despite its name, it can be mined by hand.");
        addItem(ESItems.MASTERMIND_CARD, "Mastermind Card");
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
    }

    private void addBlockEntities() {
        // Printer
        addBlock(ESBlocks.PRINTER, "Printer");
        add(PrinterBlockEntity.TITLE, "Printer");
        add(LoopButton.LOOP, "START");

        // Charger
        addBlock(ESBlocks.CHARGER, "Charger");
        add(ChargerBlockEntity.TITLE, "Charger");

        // Reactor
        addBlock(ESBlocks.REACTOR, "Nuclear Reactor");
        addBlockTooltip(ESBlocks.REACTOR,
                "You don't think it's a good idea to contain so much power in such a small machine");
        add(ReactorBlockEntity.TITLE, "Nuclear Reactor");
        add(ESFluidTags.REACTOR_FLUIDS, "Reactor Coolants");
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

        addItem(ESItems.DESERT_JUICE, "Desert Juice");
    }

    private void addTags() {
        add(ESTags.Items.AMMO, "Ammunition");
        add(ESTags.Items.AMMO_HANDGUN, "Handgun Ammunition");
        add(ESTags.Items.SHOW_VALUE, "Armors Displaying Value");
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

    private void addDeathMessages(ResourceKey<DamageType> damage, String generic, String namedItem) {
        add("death.attack." + damage.location().toString(), generic);
        add("death.attack." + damage.location().toString() + ".item", namedItem);
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
            // TODO update when PR https://github.com/lunar-sway/minestuck/pull/700 is
            // merged
            String key = "minestuck.program." + location.getPath();
            add(key, text);
        }
    }
}
