package com.medsal15.items;

import static com.medsal15.ExtraStuck.modid;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;
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
import com.medsal15.items.guns.Handgun;
import com.medsal15.items.melee.AltGunWeapon;
import com.medsal15.items.melee.InnateEnchantsWeapon;
import com.medsal15.items.projectiles.ESArrowItem;
import com.medsal15.items.projectiles.ESBulletItem;
import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.ESShield.BlockFuncs;
import com.medsal15.items.throwables.SwapTrident;
import com.mraof.minestuck.item.MSItemTypes;
import com.mraof.minestuck.item.weapon.ItemRightClickEffect;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.core.component.DataComponents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.Tiers;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.minecraft.world.item.component.ItemContainerContents;
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
            p -> new ESShield(p.durability(80).component(ESDataComponents.BURN_DURATION, 100), BlockFuncs::burn));
    public static final DeferredItem<Item> WOODEN_SHIELD = ITEMS.registerItem("wooden_shield",
            p -> new ESShield(p.durability(80), BlockFuncs.replace(ESItems.FLAME_SHIELD, DamageTypeTags.IS_FIRE)));
    public static final DeferredItem<Item> HALT_SHIELD = ITEMS.registerItem("halt_shield",
            p -> new ESShield(p.durability(243), BlockFuncs::strongerKnockback, BlockFuncs.turn(180)));
    public static final DeferredItem<Item> NON_CONTACT_CONTRACT = ITEMS.registerItem("non_contact_contract",
            ESShield::new, new Item.Properties().durability(328));
    public static final DeferredItem<Item> SLIED = ITEMS.registerItem("slied",
            p -> new ESShield(p.durability(59), BlockFuncs.selfDropChance(.25F, () -> ESLangProvider.SLIED_DROP_KEY)));
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
            p -> new ESShield(p.durability(450), BlockFuncs.bounceProjectiles((projectile, entity, random) -> {
                // randomly multiply by 1 / 5 - 5
                double mx = random.nextDouble() * 4D + 1D;
                double fx = random.nextBoolean() ? mx : 1 / mx;
                double mz = random.nextDouble() * 4D + 1D;
                double fz = random.nextBoolean() ? mz : 1 / mz;
                projectile.setDeltaMovement(projectile.getDeltaMovement().multiply(fx, 1, fz));
            })));
    public static final DeferredItem<Item> RETURN_TO_SENDER = ITEMS.registerItem("return_to_sender",
            p -> new ESShield(p.durability(1353), BlockFuncs.bounceProjectiles((projectile, entity, random) -> {
                if (entity != null) {
                    Vec3 vec3 = entity.getLookAngle().normalize().multiply(-4, -4, -4);
                    projectile.setDeltaMovement(vec3);
                    projectile.hasImpulse = true;
                }
            })));
    public static final DeferredItem<Item> SPIKES_ON_A_SLAB = ITEMS.registerItem("spikes_on_a_slab",
            p -> new ESShield(p.durability(732).component(ESDataComponents.SHIELD_DAMAGE, 6F), BlockFuncs.DAMAGE));
    public static final DeferredItem<Item> JAWBITER = ITEMS.registerItem("jawbiter",
            p -> new ESShield(p.durability(612).component(ESDataComponents.SHIELD_DAMAGE, 8F), BlockFuncs.DAMAGE,
                    BlockFuncs::dropCandy));
    public static final DeferredItem<Item> FLUX_SHIELD = ITEMS.registerItem("flux_shield",
            p -> new ESShield(p.durability(490).component(ESDataComponents.ENERGY, 0)
                    .component(ESDataComponents.ENERGY_STORAGE, 100000)
                    .component(ESDataComponents.FLUX_MULTIPLIER, 100), BlockFuncs.USE_POWER));
    public static final DeferredItem<Item> LIGHT_SHIELD = ITEMS.registerItem("light_shield",
            p -> new ESShield(p.durability(880).component(ESDataComponents.BURN_DURATION, 600), BlockFuncs::burn));
    public static final DeferredItem<Item> ELDRITCH_SHIELD = ITEMS.registerItem("eldritch_shield",
            p -> new ESShield(p.durability(1441).component(ESDataComponents.SHIELD_DAMAGE, 10F), BlockFuncs.DAMAGE,
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
            new Item.Properties()
                    .food(new FoodProperties.Builder().fast().nutrition(1).saturationModifier(.5F).build()));
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
    // Hammers
    public static final DeferredItem<Item> GEM_BREAKER = ITEMS.register("gem_breaker",
            () -> new InnateEnchantsWeapon(
                    new WeaponItem.Builder(MSItemTypes.EMERALD_TIER, 7, -3.2F).efficiency(7.0F)
                            .set(MSItemTypes.HAMMER_TOOL),
                    new Item.Properties().durability(1250), Map.of(Enchantments.FORTUNE, 1)));

    // Clubs
    public static final DeferredItem<Item> SILVER_BAT = ITEMS.register("silver_bat",
            () -> new InnateEnchantsWeapon(
                    new WeaponItem.Builder(Tiers.IRON, 4, -2.8f).efficiency(2f).set(MSItemTypes.CLUB_TOOL),
                    new Item.Properties().durability(500), Map.of(Enchantments.SMITE, 1)));

    // Keys
    public static final DeferredItem<Item> KEY_OF_TRIALS = ITEMS.register("key_of_trials",
            () -> new WeaponItem(
                    new WeaponItem.Builder(ESItemTiers.COPPER_TIER, 0, -1F).efficiency(1.5F).set(MSItemTypes.KEY_TOOL),
                    new Item.Properties()));
    public static final DeferredItem<Item> KEY_OF_OMINOUS_TRIALS = ITEMS.register("key_of_ominous_trials",
            () -> new InnateEnchantsWeapon(
                    new WeaponItem.Builder(Tiers.DIAMOND, 0, -1F).efficiency(2F).set(MSItemTypes.KEY_TOOL)
                            .add(ESHitEffects::stealLuck),
                    new Item.Properties().durability(500), Map.of(Enchantments.LOOTING, 1)));
    public static final DeferredItem<Item> OFFICE_KEY = ITEMS.register("office_key",
            () -> new AltGunWeapon(new WeaponItem.Builder(Tiers.IRON, 0, -1F).efficiency(1F).set(MSItemTypes.KEY_TOOL)
                    .set(ItemRightClickEffect.switchTo(ESItems.HANDGUN)),
                    new Item.Properties().component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)));

    // Guns
    public static final DeferredItem<Item> HANDGUN = ITEMS.register("handgun",
            () -> new Handgun(
                    new Properties().stacksTo(1).component(DataComponents.CONTAINER, ItemContainerContents.EMPTY)
                            .durability(250))
                    .setNext(ESItems.OFFICE_KEY));

    // Ammo
    public static final DeferredItem<Item> HANDGUN_BULLET = ITEMS.registerItem("handgun_bullet",
            (p) -> new ESBulletItem(p.stacksTo(99).component(ESDataComponents.AMMO_DAMAGE, 2f),
                    ESBullet.createArrow(ESEntities.HANDGUN_BULLET.get()),
                    ESBullet.asProjectile(ESEntities.HANDGUN_BULLET.get())));
    public static final DeferredItem<Item> HEAVY_HANDGUN_BULLET = ITEMS.registerItem("heavy_handgun_bullet",
            (p) -> new ESBulletItem(p.stacksTo(99).component(ESDataComponents.AMMO_DAMAGE, 4f),
                    ESBullet.createArrow(ESEntities.HEAVY_HANDGUN_BULLET.get()),
                    ESBullet.asProjectile(ESEntities.HEAVY_HANDGUN_BULLET.get())));
    // #endregion Weapons

    // #region Blocks
    public static final DeferredItem<BlockItem> CUT_GARNET = ITEMS.registerSimpleBlockItem(ESBlocks.CUT_GARNET);
    public static final DeferredItem<BlockItem> CUT_GARNET_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_GARNET_STAIRS);
    public static final DeferredItem<BlockItem> CUT_GARNET_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_GARNET_SLAB);
    public static final DeferredItem<BlockItem> CUT_GARNET_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.CUT_GARNET_WALL);
    public static final DeferredItem<BlockItem> GARNET_BRICKS = ITEMS.registerSimpleBlockItem(ESBlocks.GARNET_BRICKS);
    public static final DeferredItem<BlockItem> GARNET_BRICK_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.GARNET_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> GARNET_BRICK_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.GARNET_BRICK_SLAB);
    public static final DeferredItem<BlockItem> GARNET_BRICK_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.GARNET_BRICK_WALL);
    public static final DeferredItem<BlockItem> CHISELED_GARNET_BRICKS = ITEMS
            .registerSimpleBlockItem(ESBlocks.CHISELED_GARNET_BRICKS);

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

    public static final DeferredItem<BlockItem> COBALT_BLOCK = ITEMS.registerSimpleBlockItem(ESBlocks.COBALT_BLOCK);
    public static final DeferredItem<BlockItem> COBALT_BARS = ITEMS.registerSimpleBlockItem(ESBlocks.COBALT_BARS);
    public static final DeferredItem<BlockItem> COBALT_DOOR = ITEMS.registerSimpleBlockItem(ESBlocks.COBALT_DOOR);
    public static final DeferredItem<BlockItem> COBALT_TRAPDOOR = ITEMS
            .registerSimpleBlockItem(ESBlocks.COBALT_TRAPDOOR);
    public static final DeferredItem<BlockItem> COBALT_PRESSURE_PLATE = ITEMS
            .registerSimpleBlockItem(ESBlocks.COBALT_PRESSURE_PLATE);

    public static final DeferredItem<BlockItem> SULFUROUS_STONE = ITEMS
            .registerSimpleBlockItem(ESBlocks.SULFUROUS_STONE);
    public static final DeferredItem<BlockItem> SULFUROUS_STONE_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.SULFUROUS_STONE_STAIRS);
    public static final DeferredItem<BlockItem> SULFUROUS_STONE_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.SULFUROUS_STONE_SLAB);
    public static final DeferredItem<BlockItem> SULFUROUS_STONE_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.SULFUROUS_STONE_WALL);

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

    public static final DeferredItem<BlockItem> ZILLIUM_BRICKS = ITEMS
            .registerSimpleBlockItem(ESBlocks.ZILLIUM_BRICKS);
    public static final DeferredItem<BlockItem> ZILLIUM_BRICK_STAIRS = ITEMS
            .registerSimpleBlockItem(ESBlocks.ZILLIUM_BRICK_STAIRS);
    public static final DeferredItem<BlockItem> ZILLIUM_BRICK_SLAB = ITEMS
            .registerSimpleBlockItem(ESBlocks.ZILLIUM_BRICK_SLAB);
    public static final DeferredItem<BlockItem> ZILLIUM_BRICK_WALL = ITEMS
            .registerSimpleBlockItem(ESBlocks.ZILLIUM_BRICK_WALL);
    // #endregion Blocks

    public static final DeferredItem<Item> GIFT = ITEMS.registerItem("gift",
            (p) -> new GiftItem(p.component(ESDataComponents.GIFT_TABLE, ESLootSubProvider.GIFT_LOOT_TABLE)));

    public static void addToCreativeTab(CreativeModeTab.ItemDisplayParameters parameters,
            CreativeModeTab.Output output) {

        if (ModList.get().isLoaded("patchouli")) {
            output.accept(ItemModBook
                    .forBook(modid("extrastuck")));
        }

        for (DeferredItem<Item> item : ESItems.getShields()) {
            output.accept(item.get());
        }
        output.accept(CAPTAIN_JUSTICE_SHIELD_THROWABLE);
        output.accept(GIFT);

        for (DeferredItem<Item> item : ESItems.getMeleeWeapons()) {
            output.accept(item.get());
        }
        for (DeferredItem<Item> item : ESItems.getRangedWeapons()) {
            output.accept(item.get());
        }

        for (DeferredItem<Item> item : ESItems.getArrows()) {
            output.accept(item.get());
        }
        for (DeferredItem<Item> item : ESItems.getAmmo()) {
            output.accept(item.get());
        }

        for (DeferredItem<BlockItem> item : ESItems.getBlocks()) {
            output.accept(item.get());
        }
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
        list.add(GEM_BREAKER);
        list.add(SILVER_BAT);
        list.add(KEY_OF_TRIALS);
        list.add(KEY_OF_OMINOUS_TRIALS);
        list.add(OFFICE_KEY);
        return list;
    }

    public static Collection<DeferredItem<Item>> getMiningTools() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(GEM_BREAKER);
        return list;
    }

    public static Collection<DeferredItem<Item>> getRangedWeapons() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN);
        return list;
    }

    public static Collection<DeferredItem<Item>> getAmmo() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();
        list.add(HANDGUN_BULLET);
        list.add(HEAVY_HANDGUN_BULLET);
        return list;
    }

    public static Collection<DeferredItem<BlockItem>> getBlocks() {
        ArrayList<DeferredItem<BlockItem>> list = new ArrayList<>();

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

        return list;
    }
}
