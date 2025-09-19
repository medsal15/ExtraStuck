package com.medsal15.items;

import java.util.List;
import java.util.Map;

import com.medsal15.ExtraStuck;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.Tags;

public final class ESArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister
            .create(Registries.ARMOR_MATERIAL, ExtraStuck.MODID);

    public static final Holder<ArmorMaterial> CHEF_ARMOR = ARMOR_MATERIALS.register("chef_armor",
            () -> new ArmorMaterial(Map.of(ArmorItem.Type.CHESTPLATE, 2, ArmorItem.Type.HELMET, 1), 5,
                    SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(ItemTags.WOOL),
                    List.of(new ArmorMaterial.Layer(ExtraStuck.modid("chef_armor"))), 0, 0));
    public static final Holder<ArmorMaterial> PROPELLER_HAT = ARMOR_MATERIALS.register("propeller_hat",
            () -> new ArmorMaterial(Map.of(ArmorItem.Type.HELMET, 1), 5,
                    SoundEvents.ARMOR_EQUIP_LEATHER, () -> Ingredient.of(Tags.Items.INGOTS_IRON),
                    List.of(new ArmorMaterial.Layer(ExtraStuck.modid("propeller_hat"))), 0, 0));
    public static final Holder<ArmorMaterial> DARK_KNIGHT = ARMOR_MATERIALS.register("dark_knight",
            () -> new ArmorMaterial(Map.of(
                    ArmorItem.Type.HELMET, 4,
                    ArmorItem.Type.CHESTPLATE, 9,
                    ArmorItem.Type.LEGGINGS, 7,
                    ArmorItem.Type.BOOTS, 4), 13, SoundEvents.ARMOR_EQUIP_NETHERITE, () -> null,
                    List.of(new ArmorMaterial.Layer(ExtraStuck.modid("knight_armor"))), 3F, .1F));
}
