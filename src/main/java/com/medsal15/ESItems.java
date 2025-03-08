package com.medsal15;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.items.shields.BoondollarShield;
import com.medsal15.items.shields.BounceShield;
import com.medsal15.items.shields.ChangeShield;
import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.FlameShield;
import com.medsal15.items.shields.HaltShield;
import com.medsal15.items.shields.SbahjShield;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ESItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    // #region Shields
    public static final DeferredItem<Item> FLAME_SHIELD = ITEMS.registerItem("flame_shield",
            (properties) -> new FlameShield(properties, 100),
            new Item.Properties().durability(80));
    public static final DeferredItem<Item> WOODEN_SHIELD = ITEMS.registerItem("wooden_shield",
            (properties) -> new ChangeShield(properties, FLAME_SHIELD, DamageTypeTags.IS_FIRE),
            new Item.Properties().durability(80));
    public static final DeferredItem<Item> HALT_SHIELD = ITEMS.registerItem("halt_shield", HaltShield::new,
            new Item.Properties().durability(243));
    public static final DeferredItem<Item> NON_CONTACT_CONTRACT = ITEMS.registerItem("non_contact_contract",
            ESShield::new, new Item.Properties().durability(328));
    public static final DeferredItem<Item> SLIED = ITEMS.registerItem("slied",
            SbahjShield::new, new Item.Properties().durability(59));
    public static final DeferredItem<Item> RIOT_SHIELD = ITEMS.registerItem("riot_shield",
            ESShield::new, new Item.Properties().durability(328));
    public static final DeferredItem<Item> CAPITASHIELD = ITEMS.registerItem("capitashield",
            BoondollarShield::new, new Item.Properties().durability(130));
    public static final DeferredItem<Item> IRON_SHIELD = ITEMS.registerItem("iron_shield", ESShield::new,
            new Item.Properties().durability(480));
    public static final DeferredItem<Item> GOLD_SHIELD = ITEMS.registerItem("gold_shield", ESShield::new,
            new Item.Properties().durability(980));
    public static final DeferredItem<Item> DIAMOND_SHIELD = ITEMS.registerItem("diamond_shield", ESShield::new,
            new Item.Properties().durability(1561));
    public static final DeferredItem<Item> NETHERITE_SHIELD = ITEMS.registerItem("netherite_shield", ESShield::new,
            new Item.Properties().durability(1561).fireResistant()
                    .attributes(ItemAttributeModifiers.builder().add(
                            Attributes.KNOCKBACK_RESISTANCE,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(
                                    ExtraStuck.MODID, "netherite_shield"), 0.1,
                                    Operation.ADD_VALUE),
                            EquipmentSlotGroup.OFFHAND).build()));
    public static final DeferredItem<Item> GARNET_SHIELD = ITEMS.registerItem("garnet_shield", ESShield::new,
            new Item.Properties().durability(2560)
                    .attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_SPEED,
                            new AttributeModifier(ResourceLocation.fromNamespaceAndPath(
                                    ExtraStuck.MODID, "garnet_shield"), 0.1,
                                    Operation.ADD_VALUE),
                            EquipmentSlotGroup.OFFHAND).build()));
    public static final DeferredItem<Item> POGO_SHIELD = ITEMS.registerItem("pogo_shield",
            (properties) -> new BounceShield(properties, (projectile, entity, random) -> {
                // randomly multiply by 1 / 5 - 5
                var mx = random.nextDouble() * 4D + 1D;
                var fx = random.nextBoolean() ? mx : 1 / mx;
                var mz = random.nextDouble() * 4D + 1D;
                var fz = random.nextBoolean() ? mz : 1 / mz;
                projectile.setDeltaMovement(projectile.getDeltaMovement().multiply(fx, 1, fz));
            }),
            new Item.Properties().durability(450));
    public static final DeferredItem<Item> RETURN_TO_SENDER = ITEMS.registerItem("return_to_sender",
            (properties) -> new BounceShield(properties, (projectile, entity, random) -> {
                if (entity != null) {
                    // todo? should these be higher?
                    Vec3 vec3 = entity.getLookAngle().normalize().multiply(-4, -4, -4);
                    projectile.setDeltaMovement(vec3);
                    projectile.hasImpulse = true;
                }
            }), new Item.Properties().durability(1353));
    // #endregion Shields

    public static Collection<DeferredItem<Item>> getItems() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.addAll(getShields());
        return list;
    }

    public static Collection<DeferredItem<Item>> getShields() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(WOODEN_SHIELD);
        list.add(FLAME_SHIELD);
        list.add(HALT_SHIELD);
        list.add(NON_CONTACT_CONTRACT);
        list.add(SLIED);
        list.add(RIOT_SHIELD);
        list.add(CAPITASHIELD);
        list.add(IRON_SHIELD);
        list.add(GOLD_SHIELD);
        list.add(DIAMOND_SHIELD);
        list.add(NETHERITE_SHIELD);
        list.add(GARNET_SHIELD);
        list.add(POGO_SHIELD);
        list.add(RETURN_TO_SENDER);
        return list;
    }
}
