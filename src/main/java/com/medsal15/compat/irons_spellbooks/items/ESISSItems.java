package com.medsal15.compat.irons_spellbooks.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;
import com.mraof.minestuck.item.MSItemProperties;
import com.mraof.minestuck.item.MSItemTypes;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.item.weapons.StaffTier;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESISSItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    // #region Staves
    public static final DeferredItem<Item> CURSED_CAT_STAFF = ITEMS.register("cursed_cat_staff",
            () -> new StaffItem(ItemPropertiesHelper.equipment(1)
                    .attributes(ExtendedSwordItem.createAttributes(new StaffTier(7, -3F,
                            new AttributeContainer(AttributeRegistry.MANA_REGEN, .15, Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, .1,
                                    Operation.ADD_MULTIPLIED_BASE))))));
    public static final DeferredItem<Item> BLESSED_CAT_STAFF = ITEMS.register("blessed_cat_staff",
            () -> new StaffItem(ItemPropertiesHelper.equipment(1)
                    .attributes(ExtendedSwordItem.createAttributes(new StaffTier(7, -3F,
                            new AttributeContainer(AttributeRegistry.MANA_REGEN, .15, Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, .15,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, .15,
                                    Operation.ADD_MULTIPLIED_BASE))))));
    public static final DeferredItem<Item> BRANCH_OF_YGGDRASIL = ITEMS.register("branch_of_yggdrasil",
            () -> new StaffItem(ItemPropertiesHelper.equipment(1)
                    .attributes(ExtendedSwordItem.createAttributes(new StaffTier(8, -2.5F,
                            new AttributeContainer(AttributeRegistry.MANA_REGEN, .25, Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION, .15,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.CASTING_MOVESPEED, .15,
                                    Operation.ADD_MULTIPLIED_BASE))))));
    public static final DeferredItem<Item> STAFF_OF_YGGDRASIL = ITEMS.register("staff_of_yggdrasil",
            () -> new StaffItem(ItemPropertiesHelper.equipment(1)
                    .attributes(ExtendedSwordItem.createAttributes(new StaffTier(8, -2.5F,
                            new AttributeContainer(AttributeRegistry.MANA_REGEN, .25, Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION, .15,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.CASTING_MOVESPEED, .15,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE))))));
    // #endregion Staves

    // #region Spellbooks
    public static final DeferredItem<Item> GRIMOIRE = ITEMS.register("grimoire", () -> new SpellBook(8)
            .withSpellbookAttributes(new AttributeContainer(AttributeRegistry.MAX_MANA, 266, Operation.ADD_VALUE),
                    new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, .1666,
                            Operation.ADD_MULTIPLIED_BASE)));
    public static final DeferredItem<Item> GEMINI_SPELLBOOK_RED = ITEMS.register("gemini_spellbook_red",
            () -> new DoubleSpellbook(6, ESISSItems.GEMINI_SPELLBOOK_BLUE)
                    .withSpellbookAttributes(
                            new AttributeContainer(AttributeRegistry.MAX_MANA, 300, Operation.ADD_VALUE)));
    public static final DeferredItem<Item> GEMINI_SPELLBOOK_BLUE = ITEMS.register("gemini_spellbook_blue",
            () -> new DoubleSpellbook(6, ESISSItems.GEMINI_SPELLBOOK_RED)
                    .withSpellbookAttributes(
                            new AttributeContainer(AttributeRegistry.MAX_MANA, 300, Operation.ADD_VALUE)));
    // TODO mage guy
    public static final DeferredItem<Item> PERFECTLY_UNIQUE_SPELLBOOK = ITEMS.register("perfectly_unique_spellbook",
            PerfectlyUniqueSpellbook::new);
    public static final DeferredItem<Item> SBURBDB = ITEMS.register("sburbdb", () -> new SburbDBSpellbook(4));
    // #endregion Spellbooks

    public static final DeferredItem<Item> LEADER_SWORD = ITEMS.register("leader_sword",
            () -> new MagicSwordItem(MSItemTypes.REGI_TIER, new MSItemProperties().durability(1500)
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.ATTACK_DAMAGE,
                                    new AttributeModifier(SwordItem.BASE_ATTACK_DAMAGE_ID, 7, Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .add(Attributes.ATTACK_SPEED,
                                    new AttributeModifier(SwordItem.BASE_ATTACK_SPEED_ID, -2.4f, Operation.ADD_VALUE),
                                    EquipmentSlotGroup.MAINHAND)
                            .add(AttributeRegistry.HOLY_SPELL_POWER,
                                    new AttributeModifier(ExtraStuck.modid("leader_sword_holy"), .1f,
                                            Operation.ADD_MULTIPLIED_BASE),
                                    EquipmentSlotGroup.MAINHAND)
                            .build())
                    .rarity(Rarity.UNCOMMON),
                    new SpellDataRegistryHolder[] {
                            new SpellDataRegistryHolder(SpellRegistry.HEALING_CIRCLE_SPELL, 1),
                    }));

    public static Collection<DeferredItem<Item>> getSpellbooks() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(GRIMOIRE);
        list.add(GEMINI_SPELLBOOK_BLUE);
        list.add(GEMINI_SPELLBOOK_RED);
        list.add(PERFECTLY_UNIQUE_SPELLBOOK);
        list.add(SBURBDB);

        return list;
    }

    public static Collection<DeferredItem<Item>> getStaves() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(CURSED_CAT_STAFF);
        list.add(BLESSED_CAT_STAFF);
        list.add(BRANCH_OF_YGGDRASIL);
        list.add(STAFF_OF_YGGDRASIL);

        return list;
    }

    public static Collection<DeferredItem<Item>> getSwords() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(LEADER_SWORD);

        return list;
    }
}
