package com.medsal15.data;

import java.util.function.Supplier;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageType;
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
    public static final String GIFT_PROTECTION_GIFT_KEY = ESItems.GIFT_OF_PROTECTION.get().getDescriptionId()
            + ".gift_drop";

    public static final String GOLDEN_PAN_HIT = "sound." + ExtraStuck.MODID + ".golden_pan_hit";
    public static final String GUN_CONTENT_KEY = ExtraStuck.MODID + ".gun_content";
    public static final String GUN_EMPTY_KEY = ExtraStuck.MODID + ".gun_empty";
    public static final String ALT_GUN_EMPTY_KEY = ExtraStuck.MODID + ".office_key.empty";
    public static final String ALT_GUN_HEAVY_KEY = ExtraStuck.MODID + ".office_key.heavy";

    public static final String ENERGY_STORAGE_KEY = ExtraStuck.MODID + ".energy_storage";

    public static final String INNATE_ENCHANT_KEY = ExtraStuck.MODID + ".innate_enchant";
    public static final String INNATE_ENCHANTS_KEY = ExtraStuck.MODID + ".innate_enchants";

    @Override
    protected void addTranslations() {
        add("itemGroup.extrastuck", "ExtraStuck");

        add(SHIELD_DAMAGE_KEY, "Deals %s damage to melee attackers");
        add(SHIELD_EFFECT_KEY, "Applies %s (%s) to melee attackers");
        add(SHIELD_SELF_EFFECT_KEY, "Applies %s (%s) when attacked");
        add(ENERGY_STORAGE_KEY, "%s / %s FE");
        add(INNATE_ENCHANT_KEY, "+1 level to %2$s");
        add(INNATE_ENCHANTS_KEY, "+%1$s levels to %2$s");
        add(GUN_CONTENT_KEY, "Loaded with %1$s %2$s");
        add(GUN_EMPTY_KEY, "Unloaded");
        add(ALT_GUN_EMPTY_KEY, "It feels strangely hollow...");
        add(ALT_GUN_HEAVY_KEY, "It feels weirdly heavy...");
        add(GOLDEN_PAN_HIT, "Golden Pan Strike");

        add("patchouli.extrastuck.title", "ExtraStuck Guide");
        add("patchouli.extrastuck.landing", "Unofficial ExtraStuck Walkthrough (100%% official)");

        addShields();
        addArrows();
        addWeapons();
        addBlocks();
        addTags();

        addItem(ESItems.GIFT, "Gift");
        addItemTooltip(ESItems.GIFT, "\"For you\"");

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
        addItem(ESItems.SILVER_BAT, "Silver Bat");
        addItemTooltip(ESItems.SILVER_BAT, "Purify all your enemies");
        addItem(ESItems.GOLDEN_PAN, "Golden Pan");
        addItemTooltip(ESItems.GOLDEN_PAN, "A valuable frying pan, extremely rarely found in lootbo- gifts.");
        addItem(ESItems.KEY_OF_TRIALS, "Key of Trials");
        addItem(ESItems.KEY_OF_OMINOUS_TRIALS, "Key of Ominous Trials");
        addItemTooltip(ESItems.KEY_OF_OMINOUS_TRIALS, "Reward for challenging dangerous foes");
        addItem(ESItems.OFFICE_KEY, "Office Key");

        addItem(ESItems.HANDGUN, "Handgun");
        addItem(ESItems.HANDGUN_BULLET, "Handgun Bullet");
        addEntityType(ESEntities.HANDGUN_BULLET, "Handgun Bullet");
        addItem(ESItems.HEAVY_HANDGUN_BULLET, "Heavy Handgun Bullet");
        addEntityType(ESEntities.HEAVY_HANDGUN_BULLET, "Heavy Handgun Bullet");
        addEntityType(ESEntities.ITEM_BULLET, "Item Bullet");
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

    private void addTags() {
        add(ESItemTags.AMMO, "Ammunition");
        add(ESItemTags.AMMO_HANDGUN, "Handgun Ammunition");
    }

    protected void addItemTooltip(Supplier<? extends Item> key, String text) {
        add(((Item) (key.get())).getDescriptionId() + ".tooltip", text);
    }

    protected void addBlockTooltip(Supplier<? extends Block> key, String text) {
        add(((Block) (key.get())).getDescriptionId() + ".tooltip", text);
    }

    protected void addDeathMessages(ResourceKey<DamageType> damage, String generic, String namedItem) {
        add("death.attack." + damage.location().toString(), generic);
        add("death.attack." + damage.location().toString() + ".item", namedItem);
    }
}
