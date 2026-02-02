package com.medsal15.compat.irons_spellbooks.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItemTiers;
import com.medsal15.items.shields.ESShield;
import com.mraof.minestuck.item.MSItemProperties;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.weapon.ItemRightClickEffect;
import com.mraof.minestuck.item.weapon.OnHitEffect;
import com.mraof.minestuck.item.weapon.WeaponItem;

import io.redspace.ironsspellbooks.api.item.weapons.ExtendedSwordItem;
import io.redspace.ironsspellbooks.api.item.weapons.MagicSwordItem;
import io.redspace.ironsspellbooks.api.magic.SpellSelectionManager;
import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.SpellData;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import io.redspace.ironsspellbooks.item.weapons.StaffItem;
import io.redspace.ironsspellbooks.item.weapons.StaffTier;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import io.redspace.ironsspellbooks.util.ItemPropertiesHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Unit;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.entity.living.LivingShieldBlockEvent;
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
    public static final DeferredItem<Item> PROSPITIAN_WAND = ITEMS.register("prospitian_wand",
            () -> new SwappableStaffItem(ItemPropertiesHelper.equipment(1)
                    .attributes(ExtendedSwordItem.createAttributes(new StaffTier(6, -3F,
                            new AttributeContainer(AttributeRegistry.CAST_TIME_REDUCTION, .15,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE)))),
                    ESISSItems.CAST_GOLD_SHIELD));
    public static final DeferredItem<Item> DERSITE_WAND = ITEMS.register("dersite_wand",
            () -> new SwappableStaffItem(ItemPropertiesHelper.equipment(1)
                    .attributes(ExtendedSwordItem.createAttributes(new StaffTier(6, -3F,
                            new AttributeContainer(AttributeRegistry.CASTING_MOVESPEED, .15,
                                    Operation.ADD_MULTIPLIED_BASE),
                            new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, .2,
                                    Operation.ADD_MULTIPLIED_BASE)))),
                    ESISSItems.AMETHYST_BACKSTABBER));
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
    public static final DeferredItem<Item> MAGE_GUY = ITEMS.register("mage_guy",
            () -> new SpellBook(6).withSpellbookAttributes(
                    new AttributeContainer(AttributeRegistry.MAX_MANA, 50, Operation.ADD_VALUE)));
    public static final DeferredItem<Item> PERFECTLY_UNIQUE_SPELLBOOK = ITEMS.register("perfectly_unique_spellbook",
            PerfectlyUniqueSpellbook::new);
    public static final DeferredItem<Item> SBURBDB = ITEMS.register("sburbdb", () -> new SburbDBSpellbook(4));
    // #endregion Spellbooks

    // #region Armory
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
    public static final DeferredItem<Item> CAST_GOLD_SHIELD = ITEMS.register("cast_gold_shield",
            () -> new ESShield(
                    new ESShield.Builder().setOther(ESISSItems.PROSPITIAN_WAND).addBlock(ESISSItems::shieldCastSpell)
                            .setRepairMaterial(stack -> stack.is(Tags.Items.INGOTS_GOLD)),
                    new Item.Properties().durability(1200).attributes(ItemAttributeModifiers.builder()
                            .add(AttributeRegistry.HOLY_SPELL_POWER,
                                    new AttributeModifier(ExtraStuck.modid("cast_gold_shield"), .2,
                                            Operation.ADD_MULTIPLIED_BASE),
                                    EquipmentSlotGroup.HAND)
                            .build())));
    public static final DeferredItem<Item> AMETHYST_BACKSTABBER = ITEMS.register("amethyst_backstabber",
            () -> new WeaponItem(
                    new WeaponItem.Builder(ESItemTiers.AMETHYST_TIER, 1, -2F).set(MSItemTypes.KNIFE_TOOL)
                            .add(OnHitEffect.backstab(7)).set(ItemRightClickEffect.switchTo(ESISSItems.DERSITE_WAND)),
                    new MSItemProperties().durability(1200).component(ComponentRegistry.CASTING_IMPLEMENT,
                            Unit.INSTANCE)));
    public static final DeferredItem<Item> LICH_CROWN = ITEMS.register("lich_crown", () -> new LichCrownISS());
    public static final DeferredItem<Item> NETHER_LICH_CROWN = ITEMS.register("nether_lich_crown",
            () -> new NetherLichCrownISS());
    public static final DeferredItem<Item> COSMIC_PLAGUE_HELMET = ITEMS.register("cosmic_plague_helmet",
            () -> new CosmicPlagueISS(ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(37))));
    public static final DeferredItem<Item> COSMIC_PLAGUE_CHESTPLATE = ITEMS.register("cosmic_plague_chestplate",
            () -> new CosmicPlagueISS(ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));
    public static final DeferredItem<Item> COSMIC_PLAGUE_LEGGINGS = ITEMS.register("cosmic_plague_leggings",
            () -> new CosmicPlagueISS(ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));
    public static final DeferredItem<Item> COSMIC_PLAGUE_BOOTS = ITEMS.register("cosmic_plague_boots",
            () -> new CosmicPlagueISS(ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(37))));
    // #endregion Armory

    // TODO thorn ring (strong ice bonus + health malus, unremovable)
    // * will probably require a pair of classes for curios loading/missing

    public static Collection<DeferredItem<Item>> getSpellbooks() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(GRIMOIRE);
        list.add(GEMINI_SPELLBOOK_BLUE);
        list.add(GEMINI_SPELLBOOK_RED);
        list.add(MAGE_GUY);
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
        list.add(PROSPITIAN_WAND);
        list.add(DERSITE_WAND);

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

    /**
     * Attempts to cast currently active spell
     */
    private static boolean shieldCastSpell(LivingShieldBlockEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            Level level = player.level();
            SpellSelectionManager spellSelectionManager = new SpellSelectionManager(player);
            SpellSelectionManager.SelectionOption selectionOption = spellSelectionManager.getSelection();
            if (selectionOption != null && !selectionOption.spellData.equals(SpellData.EMPTY)) {
                SpellData spellData = selectionOption.spellData;
                int spellLevel = spellData.getSpell().getLevelFor(spellData.getLevel(), player);
                ItemStack shield = player.getUseItem();
                String slot;

                if (player.getItemInHand(InteractionHand.MAIN_HAND).equals(shield)) {
                    slot = SpellSelectionManager.MAINHAND;
                } else {
                    slot = SpellSelectionManager.OFFHAND;
                }

                if (spellData.getSpell().attemptInitiateCast(shield, spellLevel, level, player,
                        selectionOption.getCastSource(), true, slot))
                    return true;
            }
        }

        return false;
    }
}
