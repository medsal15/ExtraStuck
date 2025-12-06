package com.medsal15.compat.irons_spellbooks;

import java.util.ArrayList;
import java.util.List;

import com.medsal15.ExtraStuck;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.fml.ModList;

public final class ISSAttributes {
    public static List<ItemAttributeModifiers.Entry> ancientVaultOpener() {
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();

        if (ModList.get().isLoaded("irons_spellbooks")) {
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.FIRE_SPELL_POWER,
                    new AttributeModifier(ExtraStuck.modid("ancient_vault_opener"), .1,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
        }

        return list;
    }

    public static List<ItemAttributeModifiers.Entry> vaultMelter() {
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();

        if (ModList.get().isLoaded("irons_spellbooks")) {
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.FIRE_SPELL_POWER,
                    new AttributeModifier(ExtraStuck.modid("vault_melter_fire_sp"), .15,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.FIRE_MAGIC_RESIST,
                    new AttributeModifier(ExtraStuck.modid("vault_melter_fire_res"), .1,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
        }

        return list;
    }

    public static List<ItemAttributeModifiers.Entry> cursedStaff() {
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();

        if (ModList.get().isLoaded("irons_spellbooks")) {
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.MANA_REGEN,
                    new AttributeModifier(ExtraStuck.modid("cursed_cat_staff_mana_regen"), .15,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.BLOOD_SPELL_POWER,
                    new AttributeModifier(ExtraStuck.modid("cursed_cat_staff_blood_spell_power"),
                            .2,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.ELDRITCH_SPELL_POWER,
                    new AttributeModifier(ExtraStuck.modid("cursed_cat_staff_eldritch_spell_power"),
                            .1,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
        }

        return list;
    }

    public static List<ItemAttributeModifiers.Entry> blessedStaff() {
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();

        if (ModList.get().isLoaded("irons_spellbooks")) {
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.MANA_REGEN,
                    new AttributeModifier(ExtraStuck.modid("cursed_cat_staff_mana_regen"), .15,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.HOLY_SPELL_POWER,
                    new AttributeModifier(ExtraStuck.modid("cursed_cat_staff_blood_spell_power"),
                            .15,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.LIGHTNING_SPELL_POWER,
                    new AttributeModifier(ExtraStuck.modid("cursed_cat_staff_eldritch_spell_power"),
                            .15,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
        }

        return list;
    }
}
