package com.medsal15.compat.irons_spellbooks.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.MissingModItem;
import com.medsal15.items.melee.SburbDBWeapon;
import com.medsal15.items.weaponeffects.ESRightClickEffects;
import com.mraof.minestuck.item.MSItemProperties;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
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
    }

    public static Collection<DeferredItem<Item>> getStaves() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(CURSED_CAT_STAFF);
        list.add(BLESSED_CAT_STAFF);

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
}
