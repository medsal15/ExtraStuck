package com.medsal15.compat.irons_spellbooks;

import java.util.ArrayList;
import java.util.List;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.ESCompatUtils;
import com.mraof.minestuck.entity.MSAttributes;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public final class ISSAttributes {
    public static List<ItemAttributeModifiers.Entry> ancientVaultOpener() {
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();

        if (ESCompatUtils.isLoaded("irons_spellbooks")) {
            list.add(new ItemAttributeModifiers.Entry(AttributeRegistry.FIRE_SPELL_POWER,
                    new AttributeModifier(ExtraStuck.modid("ancient_vault_opener"), .1,
                            Operation.ADD_MULTIPLIED_BASE),
                    EquipmentSlotGroup.MAINHAND));
        }

        return list;
    }

    public static List<ItemAttributeModifiers.Entry> vaultMelter() {
        List<ItemAttributeModifiers.Entry> list = new ArrayList<>();

        if (ESCompatUtils.isLoaded("irons_spellbooks")) {
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

    public static ItemAttributeModifiers lichCrown() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_armor"), 2F,
                                Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.ARMOR,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_armor_red"), -.25F,
                                Operation.ADD_MULTIPLIED_BASE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_damage"), .1F,
                                Operation.ADD_MULTIPLIED_BASE),
                        EquipmentSlotGroup.HEAD)
                .add(MSAttributes.UNDERLING_DAMAGE_MODIFIER,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_underling_damage"), .15F,
                                Operation.ADD_MULTIPLIED_BASE),
                        EquipmentSlotGroup.HEAD)
                .build();
    }

    public static ItemAttributeModifiers netheriteLichCrown() {
        return ItemAttributeModifiers.builder()
                .add(Attributes.ARMOR,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_armor"), 3F,
                                Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.ARMOR,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_armor_red"), -.25F,
                                Operation.ADD_MULTIPLIED_BASE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.ARMOR_TOUGHNESS,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_armor_toughness"), 3F,
                                Operation.ADD_VALUE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.KNOCKBACK_RESISTANCE,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_kb_resistance"), .1F,
                                Operation.ADD_MULTIPLIED_BASE),
                        EquipmentSlotGroup.HEAD)
                .add(Attributes.ATTACK_DAMAGE,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_damage"), .15F,
                                Operation.ADD_MULTIPLIED_BASE),
                        EquipmentSlotGroup.HEAD)
                .add(MSAttributes.UNDERLING_DAMAGE_MODIFIER,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_underling_damage"), .2F,
                                Operation.ADD_MULTIPLIED_BASE),
                        EquipmentSlotGroup.HEAD)
                .add(MSAttributes.UNDERLING_PROTECTION_MODIFIER,
                        new AttributeModifier(ExtraStuck
                                .modid("lich_crown_underling_protection"), .1F,
                                Operation.ADD_MULTIPLIED_BASE),
                        EquipmentSlotGroup.HEAD)
                .build();
    }
}
