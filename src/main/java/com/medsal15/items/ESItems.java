package com.medsal15.items;

import static com.medsal15.ExtraStuck.modid;
import static com.medsal15.data.ESItemTags.AMMO_HANDGUN;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.medsal15.ESSounds;
import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.computer.ESProgramTypes;
import com.medsal15.data.ESLangProvider;
import com.medsal15.data.loot_tables.ESLootSubProvider;
import com.medsal15.entities.ESEntities;
import com.medsal15.entities.projectiles.arrows.AmethystArrow;
import com.medsal15.entities.projectiles.arrows.CandyArrow;
import com.medsal15.entities.projectiles.arrows.CardboardArrow;
import com.medsal15.entities.projectiles.arrows.DragonArrow;
import com.medsal15.entities.projectiles.arrows.EndArrow;
import com.medsal15.entities.projectiles.arrows.ExplosiveArrow;
import com.medsal15.entities.projectiles.arrows.FlameArrow;
import com.medsal15.entities.projectiles.arrows.GlassArrow;
import com.medsal15.entities.projectiles.arrows.HealingArrow;
import com.medsal15.entities.projectiles.arrows.IronArrow;
import com.medsal15.entities.projectiles.arrows.LightningArrow;
import com.medsal15.entities.projectiles.arrows.MiningArrow;
import com.medsal15.entities.projectiles.arrows.MissedArrow;
import com.medsal15.entities.projectiles.arrows.NetherArrow;
import com.medsal15.entities.projectiles.arrows.PrismarineArrow;
import com.medsal15.entities.projectiles.arrows.QuartzArrow;
import com.medsal15.entities.projectiles.arrows.TeleportArrow;
import com.medsal15.entities.projectiles.bullets.ESBullet;
import com.medsal15.items.ESDataComponents.GristLayer;
import com.medsal15.items.armor.ChefArmorItem;
import com.medsal15.items.armor.DarkKnightArmorItem;
import com.medsal15.items.armor.PropellerHatItem;
import com.medsal15.items.armor.SalesmanGogglesItem;
import com.medsal15.items.crossbow.MechanicalRadBowItem;
import com.medsal15.items.crossbow.RadBowItem;
import com.medsal15.items.food.FortuneCookie;
import com.medsal15.items.guns.ESGun;
import com.medsal15.items.melee.AltGunWeapon;
import com.medsal15.items.melee.BrushWeapon;
import com.medsal15.items.melee.InnateEnchantsWeapon;
import com.medsal15.items.modus.MastermindCardItem;
import com.medsal15.items.projectiles.ESArrowItem;
import com.medsal15.items.projectiles.ESBulletItem;
import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.ESShield.BlockFuncs;
import com.medsal15.items.throwables.SwapTrident;
import com.mraof.minestuck.item.MSItemProperties;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.armor.MSArmorItem;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.item.weapon.ItemRightClickEffect;
import com.mraof.minestuck.item.weapon.MagicRangedRightClickEffect;
import com.mraof.minestuck.item.weapon.OnHitEffect;
import com.mraof.minestuck.item.weapon.WeaponItem;
import com.mraof.minestuck.util.MSSoundEvents;

import net.minecraft.core.component.DataComponents;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterials;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.BrushItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
import net.minecraft.world.item.component.Unbreakable;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.phys.Vec3;
import net.neoforged.fml.ModList;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import vazkii.patchouli.common.item.ItemModBook;

public final class ESItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    // #region Shields
    public static final DeferredItem<Item> FLAME_SHIELD = ITEMS.registerItem("flame_shield",
            p -> new ESShield(p.durability(80).component(ESDataComponents.BURN_DURATION, 100),
                    BlockFuncs::burn));
    public static final DeferredItem<Item> WOODEN_SHIELD = ITEMS.registerItem("wooden_shield",
            p -> new ESShield(p.durability(80),
                    BlockFuncs.replace(ESItems.FLAME_SHIELD, DamageTypeTags.IS_FIRE)));
    public static final DeferredItem<Item> HALT_SHIELD = ITEMS.registerItem("halt_shield",
            p -> new ESShield(p.durability(243), BlockFuncs::strongerKnockback, BlockFuncs.turn(180)));
    public static final DeferredItem<Item> NON_CONTACT_CONTRACT = ITEMS.registerItem("non_contact_contract",
            ESShield::new, new Item.Properties().durability(328));
    public static final DeferredItem<Item> SLIED = ITEMS.registerItem("slied",
            p -> new ESShield(p.durability(59),
                    BlockFuncs.selfDropChance(.25F, () -> ESLangProvider.SLIED_DROP_KEY)));
    public static final DeferredItem<Item> RIOT_SHIELD = ITEMS.registerItem("riot_shield",
            ESShield::new, new Item.Properties().durability(328));
    public static final DeferredItem<Item> CAPITASHIELD = ITEMS.registerItem("capitashield",
            p -> new ESShield(p.durability(130), BlockFuncs::consumeBoondollars));
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
                            new AttributeModifier(ExtraStuck.modid("netherite_shield"), 0.1,
                                    Operation.ADD_VALUE),
                            EquipmentSlotGroup.OFFHAND).build()));
    public static final DeferredItem<Item> GARNET_SHIELD = ITEMS.registerItem("garnet_shield", ESShield::new,
            new Item.Properties().durability(2560)
                    .attributes(ItemAttributeModifiers.builder().add(Attributes.ATTACK_SPEED,
                            new AttributeModifier(ExtraStuck.modid("garnet_shield"), 0.1,
                                    Operation.ADD_VALUE),
                            EquipmentSlotGroup.OFFHAND).build()));
    public static final DeferredItem<Item> POGO_SHIELD = ITEMS.registerItem("pogo_shield",
            p -> new ESShield(p.durability(450),
                    BlockFuncs.bounceProjectiles((projectile, entity, random) -> {
                        // randomly multiply by 1 / 5 - 5
                        double mx = random.nextDouble() * 4D + 1D;
                        double fx = random.nextBoolean() ? mx : 1 / mx;
                        double mz = random.nextDouble() * 4D + 1D;
                        double fz = random.nextBoolean() ? mz : 1 / mz;
                        projectile.setDeltaMovement(
                                projectile.getDeltaMovement().multiply(fx, 1, fz));
                    })));
    public static final DeferredItem<Item> RETURN_TO_SENDER = ITEMS.registerItem("return_to_sender",
            p -> new ESShield(p.durability(1353),
                    BlockFuncs.bounceProjectiles((projectile, entity, random) -> {
                        if (entity != null) {
                            Vec3 vec3 = entity.getLookAngle().normalize().multiply(-4, -4,
                                    -4);
                            projectile.setDeltaMovement(vec3);
                            projectile.hasImpulse = true;
                        }
                    })));
    public static final DeferredItem<Item> SPIKES_ON_A_SLAB = ITEMS.registerItem("spikes_on_a_slab",
            p -> new ESShield(p.durability(732).component(ESDataComponents.SHIELD_DAMAGE, 6F),
                    BlockFuncs.DAMAGE));
    public static final DeferredItem<Item> JAWBITER = ITEMS.registerItem("jawbiter",
            p -> new ESShield(p.durability(612).component(ESDataComponents.SHIELD_DAMAGE, 8F),
                    BlockFuncs.DAMAGE,
                    BlockFuncs::dropCandy));
    public static final DeferredItem<Item> FLUX_SHIELD = ITEMS.registerItem("flux_shield",
            p -> new ESShield(p.durability(490)
                    .component(ESDataComponents.ENERGY_STORAGE, 100_000)
                    .component(ESDataComponents.FLUX_MULTIPLIER, 100), BlockFuncs.USE_POWER));
    public static final DeferredItem<Item> LIGHT_SHIELD = ITEMS.registerItem("light_shield",
            p -> new ESShield(p.durability(880).component(ESDataComponents.BURN_DURATION, 600),
                    BlockFuncs::burn));
    public static final DeferredItem<Item> ELDRITCH_SHIELD = ITEMS.registerItem("eldritch_shield",
            p -> new ESShield(p.durability(1441).component(ESDataComponents.SHIELD_DAMAGE, 10F),
                    BlockFuncs.DAMAGE,
                    BlockFuncs.gainEffect(MobEffects.DAMAGE_BOOST, 100)));
    /** Shield variant */
    public static final DeferredItem<Item> CAPTAIN_JUSTICE_THROWABLE_SHIELD = ITEMS.registerItem(
            "captain_justice_throwable_shield",
            p -> new ESShield(p.durability(789), ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE));
    /** Throwable variant */
    public static final DeferredItem<Item> CAPTAIN_JUSTICE_SHIELD_THROWABLE = ITEMS.registerItem(
            "captain_justice_shield_throwable",
            p -> new SwapTrident(p.durability(789), CAPTAIN_JUSTICE_THROWABLE_SHIELD));
    public static final DeferredItem<Item> GIFT_OF_PROTECTION = ITEMS.registerItem("gift_protection",
            p -> new ESShield(p.durability(624),
                    BlockFuncs.itemDropChance(() -> ESItems.GIFT.toStack(), .1f,
                            () -> ESLangProvider.GIFT_PROTECTION_GIFT_KEY)));
    // #endregion Shields

    // #region Arrows
    public static final DeferredItem<Item> NETHER_ARROW = ITEMS.registerItem("nether_arrow",
            p -> new ESArrowItem(p, NetherArrow::new, NetherArrow::new));
    public static final DeferredItem<Item> FLAME_ARROW = ITEMS.registerItem("flame_arrow",
            p -> new ESArrowItem(p, FlameArrow::new, FlameArrow::new));
    public static final DeferredItem<Item> CARDBOARD_ARROW = ITEMS.registerItem("cardboard_arrow",
            p -> new ESArrowItem(p, CardboardArrow::new, CardboardArrow::new));
    public static final DeferredItem<Item> MISSED_YOU = ITEMS.registerItem("missed_you",
            p -> new ESArrowItem(p, MissedArrow::new, MissedArrow::new));
    public static final DeferredItem<Item> SWEET_TOOTH = ITEMS.registerItem("sweet_tooth",
            p -> new ESArrowItem(p, CandyArrow::new, CandyArrow::new),
            new Item.Properties().food(ESFoods.SWEET_TOOTH));
    public static final DeferredItem<Item> LIGHTNING_ARROW = ITEMS.registerItem("lightning_arrow",
            p -> new ESArrowItem(p, LightningArrow::new, LightningArrow::new));
    public static final DeferredItem<Item> EXPLOSIVE_ARROW = ITEMS.registerItem("explosive_arrow",
            p -> new ESArrowItem(p, ExplosiveArrow::new, ExplosiveArrow::new));
    public static final DeferredItem<Item> IRON_ARROW = ITEMS.registerItem("iron_arrow",
            p -> new ESArrowItem(p, IronArrow::new, IronArrow::new));
    public static final DeferredItem<Item> QUARTZ_ARROW = ITEMS.registerItem("quartz_arrow",
            p -> new ESArrowItem(p, QuartzArrow::new, QuartzArrow::new));
    public static final DeferredItem<Item> PRISMARINE_ARROW = ITEMS.registerItem("prismarine_arrow",
            p -> new ESArrowItem(p, PrismarineArrow::new, PrismarineArrow::new));
    public static final DeferredItem<Item> GLASS_ARROW = ITEMS.registerItem("glass_arrow",
            p -> new ESArrowItem(p, GlassArrow::new, GlassArrow::new));
    public static final DeferredItem<Item> AMETHYST_ARROW = ITEMS.registerItem("amethyst_arrow",
            p -> new ESArrowItem(p, AmethystArrow::new, AmethystArrow::new));
    public static final DeferredItem<Item> PROJECDRILL = ITEMS.registerItem("projecdrill",
            p -> new ESArrowItem(p, MiningArrow::new, MiningArrow::new));
    public static final DeferredItem<Item> CRUSADER_CROSSBOLT = ITEMS.registerItem("crusader_crossbolt",
            p -> new ESArrowItem(p, HealingArrow::new, HealingArrow::new));
    public static final DeferredItem<Item> END_ARROW = ITEMS.registerItem("end_arrow",
            p -> new ESArrowItem(p, EndArrow::new, EndArrow::new));
    public static final DeferredItem<Item> TELERROW = ITEMS.registerItem("telerrow",
            p -> new ESArrowItem(p, TeleportArrow::new, TeleportArrow::new));
    public static final DeferredItem<Item> DRAGON_ARROW = ITEMS.registerItem("dragon_arrow",
            p -> new ESArrowItem(p, DragonArrow::new, DragonArrow::new));
    // #endregion Arrows

    // #region Weapons
    // #region Hammers
    public static final DeferredItem<Item> GEM_BREAKER = ITEMS.register("gem_breaker",
            () -> new InnateEnchantsWeapon(
                    new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 7, -3.2F).efficiency(7.0F)
                            .set(MSItemTypes.HAMMER_TOOL),
                    new MSItemProperties().durability(1250), Map.of(Enchantments.FORTUNE, 1)));
    public static final DeferredItem<Item> BELL_HAMMER = ITEMS.register("bell_hammer",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.GOLD, 8, -3.2F).efficiency(12F)
                            .add(OnHitEffect.playSound(() -> SoundEvents.BELL_BLOCK)),
                    new MSItemProperties().durability(2500)));
    public static final DeferredItem<Item> BLIND_HAMMER = ITEMS.register("blind_hammer",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.DENIZEN_TIER, 8, -3.2F).efficiency(15F)
                            .add(OnHitEffect.playSound(() -> SoundEvents.BELL_BLOCK))
                            .add(OnHitEffect.enemyPotionEffect(() -> new MobEffectInstance(
                                    MobEffects.BLINDNESS, 100)))
                            .add(ESInventoryTickEffects::blind),
                    new MSItemProperties().durability(2500)));
    // #endregion Hammers
    // #region Dice
    public static final DeferredItem<Item> GOLD_COIN = ITEMS.register("gold_coin",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.GOLD, 1, -3F).efficiency(1F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(ESHitEffects.randomDamage(2)),
                    new MSItemProperties().durability(128)));
    public static final DeferredItem<Item> STICKY_DIE = ITEMS.register("sticky_die",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.PAPER_TIER, 0, -3F).efficiency(1F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(OnHitEffect
                                    .enemyPotionEffect(
                                            () -> new MobEffectInstance(
                                                    MobEffects.MOVEMENT_SLOWDOWN,
                                                    60, 2)))
                            .add(ESHitEffects.randomDamage(3)),
                    new MSItemProperties().durability(333)));
    public static final DeferredItem<Item> ANTI_DIE = ITEMS.registerItem("anti_die", p -> new Item(p.stacksTo(1)));
    public static final DeferredItem<Item> TOKEN_TETRAHEDRON = ITEMS.register("token_tetrahedron",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 1, -3F).efficiency(2F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(ESHitEffects.chanceDrop(
                                    () -> ESItems.LUCK_TOKEN.toStack(1), .05F,
                                    () -> ESLangProvider.TOKEN_TETRAHEDRON_TOKEN_KEY))
                            .add(ESHitEffects.randomDamage(4)),
                    new MSItemProperties().durability(444)));
    public static final DeferredItem<Item> D_ICE = ITEMS.register("d_ice",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.ICE_TIER, 1, -3F).efficiency(1F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(OnHitEffect.ICE_SHARD)
                            .add(ESHitEffects.randomDamage(6)),
                    new MSItemProperties().durability(260)));
    public static final DeferredItem<Item> SLICE_AND_DICE = ITEMS.register("slice_and_dice",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.GOLD, 2, -2F).efficiency(1F)
                            .set(ESItemTypes.DICE_TOOL, MSItemTypes.KNIFE_TOOL)
                            .add(ESHitEffects.randomDamage(6)),
                    new MSItemProperties().durability(333)));
    public static final DeferredItem<Item> DONE = ITEMS.register("done",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.SBAHJ_TIER, 1, -3F).efficiency(1F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(OnHitEffect.SORD_DROP),
                    new Item.Properties()));
    public static final DeferredItem<Item> D10 = ITEMS.register("d10",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.DIAMOND, 1, -3F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(ESHitEffects::timeStop)
                            .add(ESHitEffects.randomDamage(10)),
                    new MSItemProperties().durability(1010)));
    public static final DeferredItem<Item> RAINBOW_D7 = ITEMS.register("rainbow_d7",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.PRISMARINE_TIER, 0, -3F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(ESHitEffects.randomDamage(7))
                            .add(ESHitEffects::rainbowEffect),
                    new MSItemProperties().durability(777)));
    public static final DeferredItem<Item> D8_NIGHT = ITEMS.register("d8_night",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.REGI_TIER, 4, -1F)
                            .set(ESItemTypes.DICE_TOOL, MSItemTypes.BATON_TOOL)
                            .add(ESHitEffects.randomDamage(8))
                            .add(ESHitEffects.dayNightEffect((stack, target, attacker) -> {
                                target.addEffect(new MobEffectInstance(
                                        MobEffects.DARKNESS, 160));
                            }, (stack, target, attacker) -> {
                                attacker.addEffect(new MobEffectInstance(
                                        MobEffects.NIGHT_VISION, 160));
                            })),
                    new MSItemProperties().durability(888)));
    public static final DeferredItem<Item> CAN_DIE = ITEMS.register("can_die",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.CANDY_TIER, 5, -3F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(ESHitEffects.randomDamage(6))
                            .add(OnHitEffect.SET_CANDY_DROP_FLAG),
                    new Item.Properties()));
    public static final DeferredItem<Item> INFINI_DIE = ITEMS.register("infini_die",
            () -> new WeaponItem(
                    new WeaponItem.Builder(MSItemTypes.ZILLY_TIER, 0, -3F)
                            .set(ESItemTypes.DICE_TOOL)
                            .add(ESHitEffects::randomMaxDamage),
                    new Item.Properties()
                            .component(DataComponents.UNBREAKABLE, new Unbreakable(true))
                            .rarity(Rarity.EPIC)));
    // #endregion Dice
    // #region Clubs
    public static final DeferredItem<Item> SILVER_BAT = ITEMS.register("silver_bat",
            () -> new InnateEnchantsWeapon(
                    new WeaponItem.Builder(Tiers.IRON, 4, -2.8f).efficiency(2f)
                            .set(MSItemTypes.CLUB_TOOL),
                    new MSItemProperties().durability(500), Map.of(Enchantments.SMITE, 1)));
    public static final DeferredItem<Item> GOLDEN_PAN = ITEMS.register("golden_pan",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.GOLD, 3, -2.8F).set(MSItemTypes.CLUB_TOOL)
                            .add(OnHitEffect.playSound(ESSounds.GOLDEN_PAN_HIT))
                            .add(OnHitEffect.enemyKnockback(1F)),
                    new MSItemProperties().durability(500).rarity(Rarity.RARE)));
    public static final DeferredItem<Item> ROLLING_PIN = ITEMS.register("rolling_pin",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.WOOD, 1, -2.8F).set(MSItemTypes.CLUB_TOOL)
                            .add(OnHitEffect.enemyKnockback(.1F)),
                    new Item.Properties()));
    // #endregion Clubs
    // #region Keys
    public static final DeferredItem<Item> KEY_OF_TRIALS = ITEMS.register("key_of_trials",
            () -> new WeaponItem(
                    new WeaponItem.Builder(ESItemTiers.COPPER_TIER, 0, -1F).efficiency(1.5F)
                            .set(MSItemTypes.KEY_TOOL),
                    new Item.Properties()));
    public static final DeferredItem<Item> KEY_OF_OMINOUS_TRIALS = ITEMS.register("key_of_ominous_trials",
            () -> new InnateEnchantsWeapon(
                    new WeaponItem.Builder(Tiers.DIAMOND, 0, -1F).efficiency(2F)
                            .set(MSItemTypes.KEY_TOOL)
                            .add(ESHitEffects::stealLuck),
                    new MSItemProperties().durability(500), Map.of(Enchantments.LOOTING, 1)));
    public static final DeferredItem<Item> OFFICE_KEY = ITEMS.register("office_key",
            () -> new AltGunWeapon(
                    new WeaponItem.Builder(Tiers.IRON, 0, -1F).efficiency(1F)
                            .set(MSItemTypes.KEY_TOOL)
                            .set(ItemRightClickEffect.switchTo(ESItems.HANDGUN)),
                    new Item.Properties().component(DataComponents.CONTAINER,
                            ItemContainerContents.EMPTY)));
    // #endregion Keys
    // #region Wands
    public static final DeferredItem<Item> BAGUETTE_MAGIQUE = ITEMS.register("baguette_magique",
            () -> new WeaponItem(new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 1, -1F).efficiency(1F)
                    .set(MSItemTypes.WAND_TOOL).set(MagicRangedRightClickEffect.STANDARD_MAGIC)
                    .add(OnHitEffect.SPAWN_BREADCRUMBS),
                    new Item.Properties()));
    // #endregion Wands
    // #region Canes
    public static final DeferredItem<Item> BROOM = ITEMS.register("broom",
            () -> new BrushWeapon(
                    new WeaponItem.Builder(MSItemTypes.ORGANIC_TIER, 3, -2F)
                            .set(MSItemTypes.CANE_TOOL)
                            .set(ESRightClickBlockEffects::brush)
                            .add(OnHitEffect.enemyKnockback(1F)),
                    new Item.Properties()));
    // #endregion Canes
    // #region Forks
    public static final DeferredItem<Item> MAGNEFORK = ITEMS.register("magnefork",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.IRON, 3, -2.6F).efficiency(1F)
                            .set(MSItemTypes.FORK_TOOL)
                            .add(ESHitEffects.attractItemsGrist(5)),
                    new MSItemProperties().durability(450)));
    public static final DeferredItem<Item> OVERCHARGED_MAGNEFORK = ITEMS.register("overcharged_magnefork",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.GOLD, 8, -2.6F).efficiency(3F)
                            .set(MSItemTypes.FORK_TOOL)
                            .add(ESHitEffects.requireCharge(300,
                                    ESHitEffects.attractItemsGrist(10)))
                            .set(ItemRightClickEffect
                                    .switchTo(ESItems.UNDERCHARGED_MAGNEFORK))
                            .add(OnHitEffect.playSound(MSSoundEvents.EVENT_ELECTRIC_SHOCK,
                                    0.6F, 1.0F)),
                    new MSItemProperties().durability(750)
                            .component(ESDataComponents.ENERGY_STORAGE, 30_000)));
    public static final DeferredItem<Item> UNDERCHARGED_MAGNEFORK = ITEMS.register("undercharged_magnefork",
            () -> new WeaponItem(
                    new WeaponItem.Builder(Tiers.GOLD, 8, -2.6F).efficiency(3F)
                            .set(MSItemTypes.FORK_TOOL)
                            .set(ItemRightClickEffect
                                    .switchTo(ESItems.OVERCHARGED_MAGNEFORK)),
                    new MSItemProperties().durability(750)
                            .component(ESDataComponents.ENERGY_STORAGE, 30_000)));
    // #endregion Forks
    // #region Crossbows
    public static final DeferredItem<Item> RADBOW = ITEMS.register("radbow",
            () -> new RadBowItem(new Properties().durability(350).stacksTo(1)));
    public static final DeferredItem<Item> MECHANICAL_RADBOW = ITEMS.register("mechanical_radbow",
            () -> new MechanicalRadBowItem(new Properties().durability(933).stacksTo(1)));
    // #endregion Crossbows
    // #region Guns
    public static final DeferredItem<Item> HANDGUN = ITEMS.register("handgun",
            () -> new ESGun(
                    new ESGun.Builder().ammo(AMMO_HANDGUN).maxBullets(6).zoom(.8F)
                            .switchTo(ESItems.OFFICE_KEY),
                    new MSItemProperties().durability(250)));
    // #endregion Guns
    // #region Ammo
    public static final DeferredItem<Item> HANDGUN_BULLET = ITEMS.registerItem("handgun_bullet",
            (p) -> new ESBulletItem(p.stacksTo(99).component(ESDataComponents.AMMO_DAMAGE, 2f),
                    ESBullet.createArrow(ESEntities.HANDGUN_BULLET.get()),
                    ESBullet.asProjectile(ESEntities.HANDGUN_BULLET.get())));
    public static final DeferredItem<Item> HEAVY_HANDGUN_BULLET = ITEMS.registerItem("heavy_handgun_bullet",
            (p) -> new ESBulletItem(p.stacksTo(99).component(ESDataComponents.AMMO_DAMAGE, 4f),
                    ESBullet.createArrow(ESEntities.HEAVY_HANDGUN_BULLET.get()),
                    ESBullet.asProjectile(ESEntities.HEAVY_HANDGUN_BULLET.get())));
    // #endregion Ammo
    // #endregion Weapons

    // #region Armors
    public static final DeferredItem<Item> CHEF_HAT = ITEMS.register("chef_hat",
            () -> new ChefArmorItem(ESArmorMaterials.CHEF_ARMOR, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(10))));
    public static final DeferredItem<Item> CHEF_APRON = ITEMS.register("chef_apron",
            () -> new ChefArmorItem(ESArmorMaterials.CHEF_ARMOR, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(10))));
    public static final DeferredItem<Item> HEAVY_BOOTS = ITEMS.register("heavy_boots",
            () -> new MSArmorItem(ArmorMaterials.IRON, ArmorItem.Type.BOOTS, new Item.Properties()
                    .durability(ArmorItem.Type.CHESTPLATE.getDurability(17))
                    .attributes(ItemAttributeModifiers.builder()
                            .add(Attributes.GRAVITY,
                                    new AttributeModifier(ExtraStuck.modid(
                                            "heavy_boots_gravity"), 1F,
                                            Operation.ADD_MULTIPLIED_BASE),
                                    EquipmentSlotGroup.FEET)
                            .add(Attributes.ARMOR,
                                    new AttributeModifier(ExtraStuck
                                            .modid("heavy_boots_armor"), 3F,
                                            Operation.ADD_VALUE),
                                    EquipmentSlotGroup.FEET)
                            .build())));
    public static final DeferredItem<Item> PROPELLER_HAT = ITEMS.register("propeller_hat",
            () -> new PropellerHatItem(ESArmorMaterials.PROPELLER_HAT, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(17))
                            .attributes(ItemAttributeModifiers.builder()
                                    .add(Attributes.GRAVITY,
                                            new AttributeModifier(ExtraStuck
                                                    .modid("propeller_hat_gravity"),
                                                    -.5F,
                                                    Operation.ADD_MULTIPLIED_TOTAL),
                                            EquipmentSlotGroup.HEAD)
                                    .add(Attributes.ARMOR,
                                            new AttributeModifier(ExtraStuck
                                                    .modid("propeller_hat_armor"),
                                                    1F,
                                                    Operation.ADD_VALUE),
                                            EquipmentSlotGroup.HEAD)
                                    .build())));
    public static final DeferredItem<Item> SALESMAN_GOGGLES = ITEMS.register("salesman_goggles",
            () -> new SalesmanGogglesItem(ESArmorMaterials.SALESMAN_GLASSES, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(13))));
    // #region Dark Knight
    public static final DeferredItem<Item> DARK_KNIGHT_HELMET = ITEMS.register("dark_knight_helmet",
            () -> new DarkKnightArmorItem(ESArmorMaterials.DARK_KNIGHT, ArmorItem.Type.HELMET,
                    new Item.Properties().durability(ArmorItem.Type.HELMET.getDurability(40))));
    public static final DeferredItem<Item> DARK_KNIGHT_CHESTPLATE = ITEMS.register("dark_knight_chestplate",
            () -> new DarkKnightArmorItem(ESArmorMaterials.DARK_KNIGHT, ArmorItem.Type.CHESTPLATE,
                    new Item.Properties().durability(ArmorItem.Type.CHESTPLATE.getDurability(40))));
    public static final DeferredItem<Item> DARK_KNIGHT_LEGGINGS = ITEMS.register("dark_knight_leggings",
            () -> new DarkKnightArmorItem(ESArmorMaterials.DARK_KNIGHT, ArmorItem.Type.LEGGINGS,
                    new Item.Properties().durability(ArmorItem.Type.LEGGINGS.getDurability(40))));
    public static final DeferredItem<Item> DARK_KNIGHT_BOOTS = ITEMS.register("dark_knight_boots",
            () -> new DarkKnightArmorItem(ESArmorMaterials.DARK_KNIGHT, ArmorItem.Type.BOOTS,
                    new Item.Properties().durability(ArmorItem.Type.BOOTS.getDurability(40))));
    // #endregion Dark Knight
    /*
     * //TODO axolotl armor
     * regen on damage
     * prismarine armor && axolotl bucket/ghast tear?
     */
    // #endregion Armors

    // #region Tools
    public static final DeferredItem<Item> OLD_BRUSH = ITEMS.registerItem("old_brush", BrushItem::new,
            new Item.Properties().stacksTo(1).durability(320));
    public static final DeferredItem<Item> MAGNET = ITEMS.registerItem("magnet", p -> new MagnetItem(Tiers.IRON, p),
            new MSItemProperties().stacksTo(1).durability(160));
    public static final DeferredItem<Item> FIELD_CHARGER = ITEMS.registerItem("field_charger",
            p -> new ChargerItem(p.stacksTo(1).component(ESDataComponents.ENERGY_STORAGE, 50_000)));
    public static final DeferredItem<Item> GRIST_DETECTOR = ITEMS.registerItem("grist_detector",
            p -> new GristDetectorItem(p.component(ESDataComponents.GRIST_LAYER, GristLayer.COMMON)));
    // #endregion Tools

    // #region Modus
    public static final DeferredItem<Item> PILE_MODUS_CARD = ITEMS.registerItem("pile_modus_card", Item::new,
            new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> FORTUNE_MODUS_CARD = ITEMS.registerItem("fortune_modus_card", Item::new,
            new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> ORE_MODUS_CARD = ITEMS.registerItem("ore_modus_card", Item::new,
            new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> ARCHEOLOGY_MODUS_CARD = ITEMS.registerItem("archeology_modus_card",
            Item::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> VOID_MODUS_CARD = ITEMS.registerItem("void_modus_card",
            Item::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> ENDER_MODUS_CARD = ITEMS.registerItem("ender_modus_card",
            Item::new, new Item.Properties().stacksTo(1));
    public static final DeferredItem<Item> MASTERMIND_MODUS_CARD = ITEMS.registerItem("mastermind_modus_card",
            Item::new, new Item.Properties().stacksTo(1));

    public static final DeferredItem<BlockItem> CARD_ORE = ITEMS.registerSimpleBlockItem(ESBlocks.CARD_ORE);
    public static final DeferredItem<Item> FORTUNE_COOKIE = ITEMS.registerItem("fortune_cookie", FortuneCookie::new,
            new Item.Properties().food(new FoodProperties.Builder().alwaysEdible().build()));
    public static final DeferredItem<Item> MASTERMIND_CARD = ITEMS.registerItem("mastermind_card",
            MastermindCardItem::new, new Item.Properties());
    // #endregion Modus

    // #region Food
    public static final DeferredItem<BlockItem> PIZZA = ITEMS.registerSimpleBlockItem(ESBlocks.PIZZA);
    public static final DeferredItem<Item> PIZZA_SLICE = ITEMS.registerItem("pizza_slice",
            p -> new Item(p.food(ESFoods.PIZZA_SLICE)));
    public static final DeferredItem<Item> SUSHROOM_STEW = ITEMS.registerItem("sushroom_stew",
            p -> new Item(p.food(ESFoods.SUSHROOM_STEW).stacksTo(16).craftRemainder(Items.BOWL)));
    public static final DeferredItem<Item> RADBURGER = ITEMS.registerItem("radburger",
            p -> new Item(p.food(ESFoods.RADBURGER)));
    public static final DeferredItem<BlockItem> DIVINE_TEMPTATION_BLOCK = ITEMS
            .registerSimpleBlockItem(ESBlocks.DIVINE_TEMPTATION_BLOCK);
    public static final DeferredItem<Item> DIVINE_TEMPTATION = ITEMS.registerItem("divine_temptation",
            p -> new Item(p.food(ESFoods.DIVINE_TEMPTATION).craftRemainder(Items.BOWL).stacksTo(16)));
    // #endregion Food

    public static final DeferredItem<Item> EMPTY_ENERGY_CORE = ITEMS.registerItem("empty_energy_core", Item::new);
    public static final DeferredItem<Item> INCOMPLETE_MECHANICAL_RADBOW = ITEMS
            .registerItem("incomplete_mechanical_radbow", Item::new);
    public static final DeferredItem<Item> MASTERMIND_DISK = ITEMS.registerItem("mastermind_disk",
            (p) -> new Item(
                    p.stacksTo(1).component(MSItemComponents.PROGRAM_TYPE, ESProgramTypes.MASTERMIND_CODEBREAKER)));

    // #region Blocks
    // #region Machines
    public static final DeferredItem<BlockItem> PRINTER = ITEMS.registerSimpleBlockItem(ESBlocks.PRINTER);
    public static final DeferredItem<BlockItem> CHARGER = ITEMS.registerSimpleBlockItem(ESBlocks.CHARGER);
    public static final DeferredItem<BlockItem> REACTOR = ITEMS.registerSimpleBlockItem(ESBlocks.REACTOR);
    // #endregion Machines
    // #region Garnet
    public static final DeferredItem<BlockItem> CUT_GARNET = ITEMS.registerSimpleBlockItem(ESBlocks.CUT_GARNET);
    public static final DeferredItem<BlockItem> CUT_GARNET_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_GARNET_STAIRS);
    public static final DeferredItem<BlockItem> CUT_GARNET_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_GARNET_SLAB);
    public static final DeferredItem<BlockItem> CUT_GARNET_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_GARNET_WALL);
    public static final DeferredItem<BlockItem> GARNET_BRICKS = ITEMS
            .registerSimpleBlockItem(ESBlocks.GARNET_BRICKS);
    public static final DeferredItem<BlockItem> GARNET_BRICK_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.GARNET_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> GARNET_BRICK_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.GARNET_BRICK_SLAB);
    public static final DeferredItem<BlockItem> GARNET_BRICK_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.GARNET_BRICK_WALL);
    public static final DeferredItem<BlockItem> CHISELED_GARNET_BRICKS = ITEMS
            .registerSimpleBlockItem(ESBlocks.CHISELED_GARNET_BRICKS);
    // #endregion Garnet
    // #region Ruby
    public static final DeferredItem<BlockItem> CUT_RUBY = ITEMS.registerSimpleBlockItem(ESBlocks.CUT_RUBY);
    public static final DeferredItem<BlockItem> CUT_RUBY_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_RUBY_STAIRS);
    public static final DeferredItem<BlockItem> CUT_RUBY_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_RUBY_SLAB);
    public static final DeferredItem<BlockItem> CUT_RUBY_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_RUBY_WALL);
    public static final DeferredItem<BlockItem> RUBY_BRICKS = ITEMS.registerSimpleBlockItem(ESBlocks.RUBY_BRICKS);
    public static final DeferredItem<BlockItem> RUBY_BRICK_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.RUBY_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> RUBY_BRICK_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.RUBY_BRICK_SLAB);
    public static final DeferredItem<BlockItem> RUBY_BRICK_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.RUBY_BRICK_WALL);
    public static final DeferredItem<BlockItem> CHISELED_RUBY_BRICKS = ITEMS
            .registerSimpleBlockItem(ESBlocks.CHISELED_RUBY_BRICKS);
    // #endregion Ruby
    // #region Cobalt
    public static final DeferredItem<BlockItem> COBALT_BLOCK = ITEMS.registerSimpleBlockItem(ESBlocks.COBALT_BLOCK);
    public static final DeferredItem<BlockItem> COBALT_BARS = ITEMS.registerSimpleBlockItem(ESBlocks.COBALT_BARS);
    public static final DeferredItem<BlockItem> COBALT_DOOR = ITEMS.registerSimpleBlockItem(ESBlocks.COBALT_DOOR);
    public static final DeferredItem<BlockItem> COBALT_TRAPDOOR = ITEMS
            .registerSimpleBlockItem(ESBlocks.COBALT_TRAPDOOR);
    public static final DeferredItem<BlockItem> COBALT_PRESSURE_PLATE = ITEMS
            .registerSimpleBlockItem(ESBlocks.COBALT_PRESSURE_PLATE);
    // #endregion Cobalt
    // #region Sulfur
    public static final DeferredItem<BlockItem> SULFUROUS_STONE = ITEMS
            .registerSimpleBlockItem(ESBlocks.SULFUROUS_STONE);
    public static final DeferredItem<BlockItem> SULFUROUS_STONE_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.SULFUROUS_STONE_STAIRS);
    public static final DeferredItem<BlockItem> SULFUROUS_STONE_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.SULFUROUS_STONE_SLAB);
    public static final DeferredItem<BlockItem> SULFUROUS_STONE_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.SULFUROUS_STONE_WALL);
    // #endregion Sulfur
    // #region Marble
    public static final DeferredItem<BlockItem> MARBLE = ITEMS
            .registerSimpleBlockItem(ESBlocks.MARBLE);
    public static final DeferredItem<BlockItem> MARBLE_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.MARBLE_STAIRS);
    public static final DeferredItem<BlockItem> MARBLE_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.MARBLE_SLAB);
    public static final DeferredItem<BlockItem> MARBLE_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.MARBLE_WALL);
    public static final DeferredItem<BlockItem> POLISHED_MARBLE = ITEMS
            .registerSimpleBlockItem(ESBlocks.POLISHED_MARBLE);
    public static final DeferredItem<BlockItem> POLISHED_MARBLE_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.POLISHED_MARBLE_STAIRS);
    public static final DeferredItem<BlockItem> POLISHED_MARBLE_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.POLISHED_MARBLE_SLAB);
    public static final DeferredItem<BlockItem> POLISHED_MARBLE_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.POLISHED_MARBLE_WALL);
    public static final DeferredItem<BlockItem> MARBLE_BRICKS = ITEMS
            .registerSimpleBlockItem(ESBlocks.MARBLE_BRICKS);
    public static final DeferredItem<BlockItem> MARBLE_BRICK_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.MARBLE_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> MARBLE_BRICK_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.MARBLE_BRICK_SLAB);
    public static final DeferredItem<BlockItem> MARBLE_BRICK_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.MARBLE_BRICK_WALL);
    // #endregion Marble
    // #region Zillium
    public static final DeferredItem<BlockItem> ZILLIUM_BRICKS = ITEMS
            .registerSimpleBlockItem(ESBlocks.ZILLIUM_BRICKS);
    public static final DeferredItem<BlockItem> ZILLIUM_BRICK_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.ZILLIUM_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> ZILLIUM_BRICK_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.ZILLIUM_BRICK_SLAB);
    public static final DeferredItem<BlockItem> ZILLIUM_BRICK_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.ZILLIUM_BRICK_WALL);
    // #endregion Zillium
    public static final DeferredItem<BlockItem> NORMAL_CAT_PLUSH = ITEMS
            .registerSimpleBlockItem(ESBlocks.NORMAL_CAT_PLUSH);
    // #endregion Blocks

    public static final DeferredItem<Item> GIFT = ITEMS.registerItem("gift",
            (p) -> new GiftItem(
                    p.component(ESDataComponents.GIFT_TABLE, ESLootSubProvider.GIFT_LOOT_TABLE)));
    public static final DeferredItem<Item> LUCK_TOKEN = ITEMS.registerItem("luck_token", p -> new Tokenitem(p));

    public static void addToCreativeTab(CreativeModeTab.ItemDisplayParameters parameters,
            CreativeModeTab.Output output) {
        if (ModList.get().isLoaded("patchouli")) {
            output.accept(ItemModBook
                    .forBook(modid("extrastuck")));
        }

        for (DeferredItem<Item> card : ESItems.getModusCards()) {
            output.accept(card.get());
        }
        for (DeferredItem<Item> tool : ESItems.getTools()) {
            output.accept(tool.get());
        }
        output.accept(GRIST_DETECTOR);
        output.accept(MASTERMIND_DISK);

        for (DeferredItem<Item> item : ESItems.getShields()) {
            output.accept(item.get());
        }
        output.accept(CAPTAIN_JUSTICE_SHIELD_THROWABLE);
        output.accept(GIFT);

        for (DeferredItem<Item> item : ESItems.getMeleeWeapons()) {
            output.accept(item.get());
        }
        output.accept(ANTI_DIE);
        output.accept(LUCK_TOKEN);

        for (DeferredItem<Item> item : ESItems.getRangedWeapons()) {
            output.accept(item.get());
        }

        for (DeferredItem<Item> item : ESItems.getArrows()) {
            output.accept(item.get());
        }
        for (DeferredItem<Item> item : ESItems.getAmmo()) {
            output.accept(item.get());
        }

        for (DeferredItem<Item> item : ESItems.getArmor()) {
            output.accept(item.get());
        }

        for (DeferredItem<? extends Item> item : ESItems.getFoods()) {
            output.accept(item.get());
        }

        output.accept(EMPTY_ENERGY_CORE);

        for (DeferredItem<BlockItem> item : ESItems.getBlocks()) {
            output.accept(item.get());
        }
    }

    public static Collection<DeferredItem<Item>> getModusCards() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(PILE_MODUS_CARD);
        list.add(FORTUNE_MODUS_CARD);
        list.add(ORE_MODUS_CARD);
        list.add(ARCHEOLOGY_MODUS_CARD);
        list.add(VOID_MODUS_CARD);
        list.add(ENDER_MODUS_CARD);
        list.add(MASTERMIND_MODUS_CARD);
        return list;
    }

    public static Collection<DeferredItem<Item>> getTools() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(OLD_BRUSH);
        list.add(MAGNET);
        list.add(FIELD_CHARGER);
        return list;
    }

    public static Collection<DeferredItem<Item>> getShields() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(WOODEN_SHIELD);
        list.add(FLAME_SHIELD);
        list.add(HALT_SHIELD);
        list.add(LIGHT_SHIELD);
        list.add(NON_CONTACT_CONTRACT);
        list.add(GIFT_OF_PROTECTION);
        list.add(SLIED);
        list.add(RIOT_SHIELD);
        list.add(CAPTAIN_JUSTICE_THROWABLE_SHIELD);
        list.add(CAPITASHIELD);
        list.add(IRON_SHIELD);
        list.add(SPIKES_ON_A_SLAB);
        list.add(JAWBITER);
        list.add(ELDRITCH_SHIELD);
        list.add(GOLD_SHIELD);
        list.add(FLUX_SHIELD);
        list.add(DIAMOND_SHIELD);
        list.add(NETHERITE_SHIELD);
        list.add(GARNET_SHIELD);
        list.add(POGO_SHIELD);
        list.add(RETURN_TO_SENDER);
        return list;
    }

    public static Collection<DeferredItem<Item>> getArrows() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(NETHER_ARROW);
        list.add(FLAME_ARROW);
        list.add(CARDBOARD_ARROW);
        list.add(MISSED_YOU);
        list.add(SWEET_TOOTH);
        list.add(LIGHTNING_ARROW);
        list.add(EXPLOSIVE_ARROW);
        list.add(IRON_ARROW);
        list.add(QUARTZ_ARROW);
        list.add(PRISMARINE_ARROW);
        list.add(GLASS_ARROW);
        list.add(AMETHYST_ARROW);
        list.add(PROJECDRILL);
        list.add(CRUSADER_CROSSBOLT);
        list.add(END_ARROW);
        list.add(TELERROW);
        list.add(DRAGON_ARROW);
        return list;
    }

    public static Collection<DeferredItem<Item>> getMeleeWeapons() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        // Hammers
        list.add(GEM_BREAKER);
        list.add(BELL_HAMMER);
        list.add(BLIND_HAMMER);
        // Dice
        list.add(GOLD_COIN);
        list.add(STICKY_DIE);
        list.add(TOKEN_TETRAHEDRON);
        list.add(D_ICE);
        list.add(SLICE_AND_DICE);
        list.add(DONE);
        list.add(D10);
        list.add(RAINBOW_D7);
        list.add(D8_NIGHT);
        list.add(CAN_DIE);
        list.add(INFINI_DIE);
        // Clubs
        list.add(SILVER_BAT);
        list.add(GOLDEN_PAN);
        list.add(ROLLING_PIN);
        // Keys
        list.add(KEY_OF_TRIALS);
        list.add(KEY_OF_OMINOUS_TRIALS);
        list.add(OFFICE_KEY);
        // Wands
        list.add(BAGUETTE_MAGIQUE);
        // Canes
        list.add(BROOM);
        // Forks
        list.add(MAGNEFORK);
        list.add(OVERCHARGED_MAGNEFORK);
        list.add(UNDERCHARGED_MAGNEFORK);
        return list;
    }

    public static Collection<DeferredItem<Item>> getMiningTools() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(GEM_BREAKER);
        list.add(BELL_HAMMER);
        list.add(BLIND_HAMMER);
        return list;
    }

    public static Collection<DeferredItem<Item>> getRangedWeapons() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(RADBOW);
        list.add(MECHANICAL_RADBOW);
        list.add(HANDGUN);
        return list;
    }

    public static Collection<DeferredItem<Item>> getCrossbows() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(RADBOW);
        list.add(MECHANICAL_RADBOW);
        return list;
    }

    public static Collection<DeferredItem<Item>> getAmmo() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN_BULLET);
        list.add(HEAVY_HANDGUN_BULLET);
        return list;
    }

    public static Collection<DeferredItem<Item>> getArmor() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(CHEF_HAT);
        list.add(CHEF_APRON);

        list.add(PROPELLER_HAT);
        list.add(HEAVY_BOOTS);
        list.add(SALESMAN_GOGGLES);

        list.add(DARK_KNIGHT_HELMET);
        list.add(DARK_KNIGHT_CHESTPLATE);
        list.add(DARK_KNIGHT_LEGGINGS);
        list.add(DARK_KNIGHT_BOOTS);

        return list;
    }

    public static Collection<DeferredItem<Item>> getHelmets() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(CHEF_HAT);
        list.add(PROPELLER_HAT);
        list.add(SALESMAN_GOGGLES);
        list.add(DARK_KNIGHT_HELMET);

        return list;
    }

    public static Collection<DeferredItem<Item>> getChestplates() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(CHEF_APRON);
        list.add(DARK_KNIGHT_CHESTPLATE);

        return list;
    }

    public static Collection<DeferredItem<Item>> getLeggings() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(DARK_KNIGHT_LEGGINGS);

        return list;
    }

    public static Collection<DeferredItem<Item>> getBoots() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(HEAVY_BOOTS);
        list.add(DARK_KNIGHT_BOOTS);

        return list;
    }

    public static Collection<DeferredItem<? extends Item>> getFoods() {
        ArrayList<DeferredItem<? extends Item>> list = new ArrayList<>();
        list.add(PIZZA);
        list.add(PIZZA_SLICE);
        list.add(FORTUNE_COOKIE);
        list.add(SUSHROOM_STEW);
        list.add(RADBURGER);
        list.add(DIVINE_TEMPTATION_BLOCK);
        list.add(DIVINE_TEMPTATION);
        return list;
    }

    public static Collection<DeferredItem<BlockItem>> getBlocks() {
        ArrayList<DeferredItem<BlockItem>> list = new ArrayList<>();
        list.add(PRINTER);
        list.add(CHARGER);
        list.add(REACTOR);

        list.add(CUT_GARNET);
        list.add(CUT_GARNET_STAIRS);
        list.add(CUT_GARNET_SLAB);
        list.add(CUT_GARNET_WALL);
        list.add(GARNET_BRICKS);
        list.add(GARNET_BRICK_STAIRS);
        list.add(GARNET_BRICK_SLAB);
        list.add(GARNET_BRICK_WALL);
        list.add(CHISELED_GARNET_BRICKS);

        list.add(CUT_RUBY);
        list.add(CUT_RUBY_STAIRS);
        list.add(CUT_RUBY_SLAB);
        list.add(CUT_RUBY_WALL);
        list.add(RUBY_BRICKS);
        list.add(RUBY_BRICK_STAIRS);
        list.add(RUBY_BRICK_SLAB);
        list.add(RUBY_BRICK_WALL);
        list.add(CHISELED_RUBY_BRICKS);

        list.add(COBALT_BLOCK);
        list.add(COBALT_BARS);
        list.add(COBALT_DOOR);
        list.add(COBALT_TRAPDOOR);
        list.add(COBALT_PRESSURE_PLATE);

        list.add(SULFUROUS_STONE);
        list.add(SULFUROUS_STONE_STAIRS);
        list.add(SULFUROUS_STONE_SLAB);
        list.add(SULFUROUS_STONE_WALL);

        list.add(MARBLE);
        list.add(MARBLE_STAIRS);
        list.add(MARBLE_SLAB);
        list.add(MARBLE_WALL);
        list.add(POLISHED_MARBLE);
        list.add(POLISHED_MARBLE_STAIRS);
        list.add(POLISHED_MARBLE_SLAB);
        list.add(POLISHED_MARBLE_WALL);
        list.add(MARBLE_BRICKS);
        list.add(MARBLE_BRICK_STAIRS);
        list.add(MARBLE_BRICK_SLAB);
        list.add(MARBLE_BRICK_WALL);

        list.add(ZILLIUM_BRICKS);
        list.add(ZILLIUM_BRICK_STAIRS);
        list.add(ZILLIUM_BRICK_SLAB);
        list.add(ZILLIUM_BRICK_WALL);

        list.add(CARD_ORE);

        return list;
    }
}
