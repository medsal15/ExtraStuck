package com.medsal15.compat.irons_spellbooks.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.MissingModItem;
import com.medsal15.compat.irons_spellbooks.ISSAttributes;
import com.medsal15.items.ESItemTiers;
import com.medsal15.items.melee.SburbDBWeapon;
import com.medsal15.items.shields.ESShield;
import com.medsal15.items.weaponeffects.ESRightClickEffects;
import com.mraof.minestuck.item.MSItemProperties;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.weapon.ItemRightClickEffect;
import com.mraof.minestuck.item.weapon.OnHitEffect;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESISSMissingItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    // #region Misc
    public static final DeferredItem<Item> SBURBDB = ITEMS.register("sburbdb",
            () -> new SburbDBWeapon(new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -3F).set(MSItemTypes.MISC_TOOL),
                    new MSItemProperties().durability(120)));

    public static final DeferredItem<Item> LEADER_SWORD = ITEMS.register("leader_sword",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -2.4f).set(MSItemTypes.SWORD_TOOL).efficiency(15f)
                            .set(ESRightClickEffects.healNearby(5)),
                    new MSItemProperties().durability(1500).rarity(Rarity.UNCOMMON)));

    public static final DeferredItem<Item> CAST_GOLD_SHIELD = ITEMS.register("cast_gold_shield",
            () -> new ESShield(new Item.Properties().durability(1200), ESISSMissingItems.PROSPITIAN_WAND,
                    ESShield.IBlock::shootFireball));

    public static final DeferredItem<Item> AMETHYST_BACKSTABBER = ITEMS.register("amethyst_backstabber",
            () -> new WeaponItem(
                    new WeaponItem.Builder(ESItemTiers.AMETHYST_TIER, 1, -2F).set(MSItemTypes.KNIFE_TOOL)
                            .add(OnHitEffect.backstab(7))
                            .set(ItemRightClickEffect.switchTo(ESISSMissingItems.DERSITE_WAND)),
                    new MSItemProperties().durability(1200)));

    public static final DeferredItem<Item> LICH_CROWN = ITEMS.register("lich_crown",
            () -> new LichCrownArmor(ArmorMaterials.IRON, ArmorItem.Type.HELMET, new Item.Properties()
                    .stacksTo(1)
                    .durability(ArmorItem.Type.HELMET.getDurability(23))
                    .attributes(ISSAttributes.lichCrown())));
    public static final DeferredItem<Item> NETHER_LICH_CROWN = ITEMS.register("nether_lich_crown",
            () -> new NetherLichCrownArmor(ArmorMaterials.NETHERITE, ArmorItem.Type.HELMET,
                    new Item.Properties().stacksTo(1)
                            .durability(ArmorItem.Type.HELMET.getDurability(32))
                            .attributes(ISSAttributes.netheriteLichCrown())));

    public static final DeferredItem<Item> COSMIC_PLAGUE_HELMET = ITEMS.register("cosmic_plague_helmet",
            () -> new CosmicPlagueArmor(ArmorItem.Type.HELMET, new Item.Properties().stacksTo(1)
                    .durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredItem<Item> COSMIC_PLAGUE_CHESTPLATE = ITEMS.register("cosmic_plague_chestplate",
            () -> new CosmicPlagueArmor(ArmorItem.Type.CHESTPLATE, new Item.Properties().stacksTo(1)
                    .durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredItem<Item> COSMIC_PLAGUE_LEGGINGS = ITEMS.register("cosmic_plague_leggings",
            () -> new CosmicPlagueArmor(ArmorItem.Type.LEGGINGS, new Item.Properties().stacksTo(1)
                    .durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredItem<Item> COSMIC_PLAGUE_BOOTS = ITEMS.register("cosmic_plague_boots",
            () -> new CosmicPlagueArmor(ArmorItem.Type.BOOTS, new Item.Properties().stacksTo(1)
                    .durability(ArmorItem.Type.BOOTS.getDurability(37))));
    // #endregion Misc

    // #region Staves
    // Staves are technically wands, but slower and bigger
    public static final DeferredItem<Item> CURSED_CAT_STAFF = ITEMS.register("cursed_cat_staff",
            () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -3F).efficiency(1F)
                    .set(MSItemTypes.WAND_TOOL).set(ESRightClickEffects.CURSED_STAFF_MAGIC),
                    new Item.Properties()));
    public static final DeferredItem<Item> BLESSED_CAT_STAFF = ITEMS.register("blessed_cat_staff",
            () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -3F).efficiency(1F)
                    .set(MSItemTypes.WAND_TOOL).set(ESRightClickEffects.BLESSED_STAFF_MAGIC),
                    new Item.Properties()));
    public static final DeferredItem<Item> BRANCH_OF_YGGDRASIL = ITEMS.register("branch_of_yggdrasil",
            () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 5, -2.5F).efficiency(1F)
                    .set(MSItemTypes.WAND_TOOL).set(ESRightClickEffects.BRANCH_OF_YGGDRASIL_MAGIC),
                    new Item.Properties()));
    public static final DeferredItem<Item> STAFF_OF_YGGDRASIL = ITEMS.register("staff_of_yggdrasil",
            () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.WELSH_TIER, 6, -2.5F).efficiency(1F)
                    .set(MSItemTypes.WAND_TOOL).set(ESRightClickEffects.STAFF_OF_YGGDRASIL_MAGIC),
                    new Item.Properties()));
    public static final DeferredItem<Item> PROSPITIAN_WAND = ITEMS.register("prospitian_wand",
            () -> new WeaponItem(new WeaponItem.Builder(Tiers.GOLD, 6, -3F).efficiency(1F)
                    .set(MSItemTypes.WAND_TOOL).set(ESRightClickEffects::prospitianWand),
                    new MSItemProperties().durability(1200)));
    public static final DeferredItem<Item> DERSITE_WAND = ITEMS.register("dersite_wand",
            () -> new WeaponItem(new WeaponItem.Builder(ESItemTiers.AMETHYST_TIER, 4, -3F).efficiency(1F)
                    .set(MSItemTypes.WAND_TOOL).set(ESRightClickEffects::dersiteWand),
                    new MSItemProperties().durability(1200)));
    // #endregion Staves

    // These are not referenced anywhere
    static {
        ITEMS.register("grimoire",
                () -> new MissingModItem(new Item.Properties().stacksTo(1),
                        "Iron's Spells & Spellbooks",
                        "irons_spellbooks"));
        ITEMS.register("gemini_spellbook_red",
                () -> new MissingModItem(new Item.Properties().stacksTo(1),
                        "Iron's Spells & Spellbooks",
                        "irons_spellbooks"));
        ITEMS.register("gemini_spellbook_blue",
                () -> new MissingModItem(new Item.Properties().stacksTo(1),
                        "Iron's Spells & Spellbooks",
                        "irons_spellbooks"));
        ITEMS.register("perfectly_unique_spellbook",
                () -> new MissingModItem(new Item.Properties().stacksTo(1),
                        "Iron's Spells & Spellbooks",
                        "irons_spellbooks"));
        ITEMS.register("mage_guy",
                () -> new MissingModItem(new Item.Properties().stacksTo(1),
                        "Iron's Spells & Spellbooks",
                        "irons_spellbooks"));
    }

    public static Collection<DeferredItem<Item>> getStaves() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(CURSED_CAT_STAFF);
        list.add(BLESSED_CAT_STAFF);
        list.add(BRANCH_OF_YGGDRASIL);
        list.add(STAFF_OF_YGGDRASIL);
        list.add(PROSPITIAN_WAND);
        list.add(DERSITE_WAND);

        return list;
    }

    public static Collection<DeferredItem<Item>> getMisc() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(SBURBDB);

        return list;
    }

    public static Collection<DeferredItem<Item>> getSwords() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(LEADER_SWORD);

        return list;
    }

    public static Collection<DeferredItem<Item>> getKnives() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(AMETHYST_BACKSTABBER);

        return list;
    }

    public static Collection<DeferredItem<Item>> getShields() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(CAST_GOLD_SHIELD);

        return list;
    }

    public static Collection<DeferredItem<Item>> getArmor() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(LICH_CROWN);
        list.add(NETHER_LICH_CROWN);

        list.add(COSMIC_PLAGUE_HELMET);
        list.add(COSMIC_PLAGUE_CHESTPLATE);
        list.add(COSMIC_PLAGUE_LEGGINGS);
        list.add(COSMIC_PLAGUE_BOOTS);

        return list;
    }

    public static Collection<DeferredItem<Item>> getHelmets() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(LICH_CROWN);
        list.add(NETHER_LICH_CROWN);
        list.add(COSMIC_PLAGUE_HELMET);

        return list;
    }

    public static Collection<DeferredItem<Item>> getChesplates() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(COSMIC_PLAGUE_CHESTPLATE);

        return list;
    }

    public static Collection<DeferredItem<Item>> getLeggings() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(COSMIC_PLAGUE_LEGGINGS);

        return list;
    }

    public static Collection<DeferredItem<Item>> getBoots() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(COSMIC_PLAGUE_BOOTS);

        return list;
    }
}
