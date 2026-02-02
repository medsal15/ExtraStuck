package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;
import com.medsal15.blocks.ESBlocks;
import com.medsal15.compat.irons_spellbooks.items.ESISSItems;
import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;
import com.medsal15.mobeffects.ESMobEffects;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.fluid.MSFluids;
import com.mraof.minestuck.util.MSTags;

import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.DatapackBuiltinEntriesProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ESTagsProvider {
    public static void gatherData(DataGenerator gen, PackOutput output,
            CompletableFuture<HolderLookup.Provider> lookupProvider, ExistingFileHelper fileHelper,
            DatapackBuiltinEntriesProvider datapackProvider) {
        ESBlockTags blockTags = gen.addProvider(true, new ESBlockTags(output, lookupProvider, fileHelper));
        gen.addProvider(true, new ESItemTags(output, lookupProvider, blockTags.contentsGetter(), fileHelper));
        gen.addProvider(true, new ESFluidTags(output, lookupProvider, fileHelper));
        gen.addProvider(true, new ESEntityTypeTags(output, lookupProvider, fileHelper));
        gen.addProvider(true, new ESMobEffectTags(output, lookupProvider, fileHelper));
        gen.addProvider(true, new ESDamageTypeTags(output, datapackProvider.getRegistryProvider(), fileHelper));
    }

    public static class ESBlockTags extends BlockTagsProvider {
        public ESBlockTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                ExistingFileHelper fileHelper) {
            super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
        }

        @Override
        protected void addTags(@Nonnull Provider provider) {
            tag(ESTags.Blocks.PRYABLE).add(MSBlocks.PYXIS_LID.get(), MSBlocks.CRUXTRUDER_LID.get());

            tag(ESTags.Blocks.INCORRECT_FOR_COPPER_TIER).addTag(BlockTags.INCORRECT_FOR_STONE_TOOL);

            tag(BlockTags.MINEABLE_WITH_PICKAXE).add(
                    ESBlocks.CUT_GARNET.get(), ESBlocks.CUT_GARNET_STAIRS.get(), ESBlocks.CUT_GARNET_SLAB.get(),
                    ESBlocks.CUT_GARNET_WALL.get(), ESBlocks.GARNET_BRICKS.get(), ESBlocks.GARNET_BRICK_STAIRS.get(),
                    ESBlocks.GARNET_BRICK_SLAB.get(), ESBlocks.GARNET_BRICK_WALL.get(),
                    ESBlocks.CHISELED_GARNET_BRICKS.get(), ESBlocks.CUT_RUBY.get(), ESBlocks.CUT_RUBY_STAIRS.get(),
                    ESBlocks.CUT_GARNET_SLAB.get(), ESBlocks.CUT_GARNET_WALL.get(), ESBlocks.RUBY_BRICKS.get(),
                    ESBlocks.RUBY_BRICK_STAIRS.get(), ESBlocks.RUBY_BRICK_SLAB.get(), ESBlocks.RUBY_BRICK_WALL.get(),
                    ESBlocks.CHISELED_RUBY_BRICKS.get(), ESBlocks.COBALT_BLOCK.get(), ESBlocks.COBALT_BARS.get(),
                    ESBlocks.COBALT_DOOR.get(), ESBlocks.COBALT_TRAPDOOR.get(), ESBlocks.COBALT_PRESSURE_PLATE.get(),
                    ESBlocks.SULFUROUS_STONE.get(), ESBlocks.SULFUROUS_STONE_STAIRS.get(),
                    ESBlocks.SULFUROUS_STONE_SLAB.get(), ESBlocks.SULFUROUS_STONE_WALL.get(), ESBlocks.MARBLE.get(),
                    ESBlocks.MARBLE_STAIRS.get(), ESBlocks.MARBLE_SLAB.get(), ESBlocks.MARBLE_WALL.get(),
                    ESBlocks.POLISHED_MARBLE.get(), ESBlocks.POLISHED_MARBLE_STAIRS.get(),
                    ESBlocks.POLISHED_MARBLE_SLAB.get(), ESBlocks.POLISHED_MARBLE_WALL.get(),
                    ESBlocks.MARBLE_BRICKS.get(), ESBlocks.MARBLE_BRICK_STAIRS.get(),
                    ESBlocks.MARBLE_BRICK_SLAB.get(), ESBlocks.MARBLE_BRICK_WALL.get(),
                    ESBlocks.ZILLIUM_BRICKS.get(), ESBlocks.ZILLIUM_BRICK_STAIRS.get(),
                    ESBlocks.ZILLIUM_BRICK_SLAB.get(), ESBlocks.ZILLIUM_BRICK_WALL.get(),
                    ESBlocks.CARD_ORE.get(), ESBlocks.PRINTER.get(), ESBlocks.CHARGER.get(), ESBlocks.REACTOR.get(),
                    ESBlocks.URANIUM_BLASTER.get(), ESBlocks.DISPRINTER.get());
            tag(Tags.Blocks.NEEDS_WOOD_TOOL).add(ESBlocks.PRINTER.get(), ESBlocks.CHARGER.get(), ESBlocks.REACTOR.get(),
                    ESBlocks.URANIUM_BLASTER.get(), ESBlocks.DISPRINTER.get());

            tag(BlockTags.WALLS).add(ESBlocks.CUT_GARNET_WALL.get(), ESBlocks.GARNET_BRICK_WALL.get(),
                    ESBlocks.CUT_RUBY_WALL.get(), ESBlocks.RUBY_BRICK_WALL.get(), ESBlocks.SULFUROUS_STONE_WALL.get(),
                    ESBlocks.MARBLE_WALL.get(), ESBlocks.POLISHED_MARBLE_WALL.get(), ESBlocks.MARBLE_BRICK_WALL.get(),
                    ESBlocks.ZILLIUM_BRICK_WALL.get());

            tag(BlockTags.DOORS).add(ESBlocks.COBALT_DOOR.get());
            tag(BlockTags.TRAPDOORS).add(ESBlocks.COBALT_TRAPDOOR.get());

            tag(BlockTags.INFINIBURN_OVERWORLD).add(ESBlocks.SULFUROUS_STONE.get(),
                    ESBlocks.SULFUROUS_STONE_STAIRS.get(),
                    ESBlocks.SULFUROUS_STONE_SLAB.get(), ESBlocks.SULFUROUS_STONE_WALL.get());
            tag(BlockTags.INFINIBURN_NETHER).add(ESBlocks.SULFUROUS_STONE.get(), ESBlocks.SULFUROUS_STONE_STAIRS.get(),
                    ESBlocks.SULFUROUS_STONE_SLAB.get(), ESBlocks.SULFUROUS_STONE_WALL.get());
            tag(BlockTags.INFINIBURN_END).add(ESBlocks.SULFUROUS_STONE.get(), ESBlocks.SULFUROUS_STONE_STAIRS.get(),
                    ESBlocks.SULFUROUS_STONE_SLAB.get(), ESBlocks.SULFUROUS_STONE_WALL.get());
        }
    }

    public static class ESItemTags extends ItemTagsProvider {
        public ESItemTags(PackOutput output, CompletableFuture<Provider> lookupProvider,
                CompletableFuture<TagsProvider.TagLookup<Block>> blockTags, ExistingFileHelper fileHelper) {
            super(output, lookupProvider, blockTags, ExtraStuck.MODID, fileHelper);
        }

        @Override
        protected void addTags(@Nonnull Provider provider) {
            for (DeferredItem<Item> shield : ESItems.getShields()) {
                tagShield(shield);
            }

            for (DeferredItem<Item> arrow : ESItems.getArrows()) {
                tag(ItemTags.ARROWS).add(arrow.get());
            }

            for (DeferredItem<Item> weapon : ESItems.getMeleeWeapons()) {
                tagWeapon(weapon);
            }
            for (DeferredItem<Item> weapon : ESItems.getRangedWeapons()) {
                tagRanged(weapon);
            }
            for (DeferredItem<Item> weapon : ESItems.getMagicWeapons()) {
                tag(MSTags.Items.MAGIC_WEAPON).add(weapon.get());
            }

            for (DeferredItem<Item> crossbow : ESItems.getCrossbows()) {
                tag(Tags.Items.TOOLS_CROSSBOW).add(crossbow.get());
                tag(ItemTags.CROSSBOW_ENCHANTABLE).add(crossbow.get());
                tag(ItemTags.DURABILITY_ENCHANTABLE).add(crossbow.get());
                tag(ItemTags.VANISHING_ENCHANTABLE).add(crossbow.get());
            }
            for (DeferredItem<Item> spellbook : ESISSItems.getSpellbooks()) {
                tag(ESTags.Items.ISS_SPELLBOOKS).add(spellbook.get());
            }
            for (DeferredItem<Item> staff : ESISSItems.getStaves()) {
                tag(ESTags.Items.ISS_STAVES).add(staff.get());
                tag(MSTags.Items.MAGIC_WEAPON).add(staff.get());
            }

            for (DeferredItem<Item> tool : ESItems.getMiningTools()) {
                tag(ItemTags.MINING_ENCHANTABLE).add(tool.get());
                tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(tool.get());
            }
            for (DeferredItem<Item> shovel : ESItems.getShovels()) {
                tag(ItemTags.SHOVELS).add(shovel.get());
            }

            for (DeferredItem<Item> armor : ESItems.getArmor()) {
                tag(ItemTags.ARMOR_ENCHANTABLE).add(armor.get());
            }
            for (DeferredItem<Item> helmet : ESItems.getHelmets()) {
                tag(ItemTags.HEAD_ARMOR).add(helmet.get());
            }
            for (DeferredItem<Item> chestplate : ESItems.getChestplates()) {
                tag(ItemTags.CHEST_ARMOR).add(chestplate.get());
            }
            for (DeferredItem<Item> leggings : ESItems.getLeggings()) {
                tag(ItemTags.LEG_ARMOR).add(leggings.get());
            }
            for (DeferredItem<Item> boot : ESItems.getBoots()) {
                tag(ItemTags.FOOT_ARMOR).add(boot.get());
            }

            for (DeferredItem<? extends Item> food : ESItems.getFoods()) {
                tag(Tags.Items.FOODS).add(food.get());
            }
            for (DeferredItem<? extends Item> drink : ESItems.getDrinks()) {
                tag(ESTags.Items.DRINKS).add(drink.get());
            }

            for (DeferredItem<Item> card : ESItems.getModusCards()) {
                tag(MSTags.Items.MODUS_CARD).add(card.get());
            }

            for (DeferredItem<Item> tool : ESItems.getTools()) {
                tag(Tags.Items.TOOLS).add(tool.get());
                tag(ItemTags.DURABILITY_ENCHANTABLE).add(tool.get());
                tag(ItemTags.VANISHING_ENCHANTABLE).add(tool.get());
            }

            for (DeferredItem<Item> vision : ESItems.getVisions()) {
                tag(MSTags.Items.UNREADABLE).add(vision.get());
                tag(ESTags.Items.CURIOS_CURIO).add(vision.get());
                tag(ESTags.Items.VISION).add(vision.get());
            }
            for (DeferredItem<Item> vision : ESItems.getActiveVisions()) {
                tag(ESTags.Items.ACTIVE_VISION).add(vision.get());
            }

            tag(ESTags.Items.AMMO).addTag(ESTags.Items.AMMO_HANDGUN);
            tag(ESTags.Items.AMMO_HANDGUN).add(ESItems.HANDGUN_BULLET.get(), ESItems.HEAVY_HANDGUN_BULLET.get());
            tag(ESTags.Items.SHOW_VALUE).add(ESItems.SALESMAN_GOGGLES.get(), ESItems.SALESWOMAN_GLASSES.get());
            tag(ESTags.Items.DROPS_BOONDOLLARS).add(ESItems.DEBT_REAPER.get(), ESItems.STOCKS_UPTICKER.get(),
                    ESItems.PIRATE_HOOK.get(), ESItems.NONE_OF_YOUR_BUSINESS.get(), ESItems.INVESTLANCE.get(),
                    ESItems.MONEY_MAGIC.get(), ESItems.CASHGRABBERS.get());
            tag(ESTags.Items.COSMIC_PLAGUE_ARMOR).add(ESISSItems.COSMIC_PLAGUE_HELMET.get(),
                    ESISSItems.COSMIC_PLAGUE_CHESTPLATE.get(), ESISSItems.COSMIC_PLAGUE_LEGGINGS.get(),
                    ESISSItems.COSMIC_PLAGUE_BOOTS.get());

            tagShield(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE);

            tag(ItemTags.PIGLIN_LOVED).add(ESItems.GOLD_SHIELD.get(), ESItems.GOLDEN_PAN.get(),
                    ESItems.GOLD_COIN.get());

            tag(ESTags.Items.TOOLS_ROLLING_PIN).add(ESItems.ROLLING_PIN.get());
            tag(Tags.Items.TOOLS).addTag(ESTags.Items.TOOLS_ROLLING_PIN);
            tag(Tags.Items.TOOLS_SPEAR).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                    ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
            tag(Tags.Items.RANGED_WEAPON_TOOLS).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                    ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
            tag(ItemTags.TRIDENT_ENCHANTABLE).add(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD.get(),
                    ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
            tag(ItemTags.BREAKS_DECORATED_POTS).add(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE.get());
            tag(Tags.Items.TOOLS_BRUSH).add(ESItems.OLD_BRUSH.get(), ESItems.BROOM.get());
            tag(Tags.Items.TOOLS_IGNITER).add(ESItems.LEAFBURNER.get());

            tag(Tags.Items.ORES).add(ESItems.CARD_ORE.get());

            tag(Tags.Items.FOODS).add(ESItems.SWEET_TOOTH.get());
            tag(Tags.Items.FOODS_EDIBLE_WHEN_PLACED).add(ESItems.PIZZA.get(), ESItems.DIVINE_TEMPTATION_BLOCK.get(),
                    ESItems.MORTAL_TEMPTATION_BLOCK.get());
            tag(Tags.Items.FOODS_SOUP).add(ESItems.SUSHROOM_STEW.get(), ESItems.DIVINE_TEMPTATION.get());
            tag(Tags.Items.EGGS).add(ESItems.BEE_LARVA.get());
            tag(ESTags.Items.BEE_EGGS).add(ESItems.BEE_LARVA.get());
            tag(MSTags.Items.BUGS).add(ESItems.COOKED_BEE_LARVA.get());

            tag(ItemTags.DOORS).add(ESItems.COBALT_DOOR.get());
            tag(ItemTags.TRAPDOORS).add(ESItems.COBALT_TRAPDOOR.get());

            tag(ItemTags.BOOKSHELF_BOOKS).add(ESItems.BOONDOLLARS_FOR_IDIOTS.get());

            tag(MSTags.Items.UNREADABLE).add(ESItems.ANTI_DIE.get(), ESISSItems.PERFECTLY_UNIQUE_SPELLBOOK.get())
                    .addOptional(ItemRegistry.DIVINE_SOULSHARD.getId()).addOptional(ItemRegistry.PYRIUM_INGOT.getId())
                    .addOptional(ItemRegistry.LOST_KNOWLEDGE_FRAGMENT.getId())
                    .addOptional(ItemRegistry.ELDRITCH_PAGE.getId());
            tag(MSTags.Items.LEGENDARY).add(ESItems.INFINI_DIE.get(), ESISSItems.BRANCH_OF_YGGDRASIL.get(),
                    ESISSItems.STAFF_OF_YGGDRASIL.get(), ESItems.END_OF_CIVILIZATION.get());

            tag(ESTags.Items.IGNORE_BYPRODUCT_CUTTING).add(Items.BONE_MEAL);
        }

        private void tagShield(DeferredItem<Item> item) {
            tag(ItemTags.DURABILITY_ENCHANTABLE).add(item.get());
            tag(ItemTags.VANISHING_ENCHANTABLE).add(item.get());
            tag(Tags.Items.TOOLS_SHIELD).add(item.get());
        }

        private void tagWeapon(DeferredItem<Item> item) {
            tag(ItemTags.DURABILITY_ENCHANTABLE).add(item.get());
            tag(ItemTags.VANISHING_ENCHANTABLE).add(item.get());
            tag(ItemTags.SWORD_ENCHANTABLE).add(item.get());
            tag(ItemTags.WEAPON_ENCHANTABLE).add(item.get());
            tag(Tags.Items.MELEE_WEAPON_TOOLS).add(item.get());
        }

        private void tagRanged(DeferredItem<Item> item) {
            tag(Tags.Items.RANGED_WEAPON_TOOLS).add(item.get());
            tag(ItemTags.DURABILITY_ENCHANTABLE).add(item.get());
            tag(ItemTags.VANISHING_ENCHANTABLE).add(item.get());
        }
    }

    public static class ESFluidTags extends FluidTagsProvider {
        public ESFluidTags(PackOutput output, CompletableFuture<Provider> lookupProvider,
                ExistingFileHelper fileHelper) {
            super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
        }

        @Override
        protected void addTags(@Nonnull Provider provider) {
            tag(ESTags.Fluids.REACTOR_FLUIDS)
                    .add(Fluids.WATER, MSFluids.BLOOD.get(),
                            MSFluids.BRAIN_JUICE.get(), MSFluids.ENDER.get(), MSFluids.LIGHT_WATER.get(),
                            MSFluids.OIL.get(), MSFluids.WATER_COLORS.get(), MSFluids.CAULK.get())
                    .addTag(Tags.Fluids.WATER);
        }
    }

    public static class ESEntityTypeTags extends EntityTypeTagsProvider {
        public ESEntityTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                ExistingFileHelper fileHelper) {
            super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
        }

        @Override
        protected void addTags(@Nonnull Provider provider) {
            for (EntityType<? extends AbstractArrow> arrow : ESEntities.getArrows()) {
                tag(EntityTypeTags.ARROWS).add(arrow);
            }

            tag(ESTags.EntityTypes.BEENADE_ACCEPTS).add(EntityType.BEE);
            tag(ESTags.EntityTypes.COSMIC_PLAGUE_IMMUNE).add(EntityType.ENDER_DRAGON);
        }
    }

    public static class ESMobEffectTags extends TagsProvider<MobEffect> {
        public ESMobEffectTags(PackOutput output, CompletableFuture<Provider> lookupProvider,
                ExistingFileHelper fileHelper) {
            super(output, Registries.MOB_EFFECT, lookupProvider, ExtraStuck.MODID, fileHelper);
        }

        @Override
        protected void addTags(@Nonnull Provider provider) {
            tag(ESTags.MobEffects.COSMIC_PLAGUE_IMMUNITY).add(MobEffects.POISON.getKey(),
                    ESMobEffects.COSMIC_PLAGUE.getKey());
            tag(ESTags.MobEffects.COSMIC_PLAGUE_PARTIAL_IMMUNITY).add(MobEffects.WITHER.getKey());

            tag(MSTags.Effects.SOPOR_SICKNESS_WHITELIST).add(ESMobEffects.COSMIC_PLAGUE.getKey());
        }
    }

    public static class ESDamageTypeTags extends DamageTypeTagsProvider {
        public ESDamageTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                ExistingFileHelper fileHelper) {
            super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
        }

        @Override
        protected void addTags(@Nonnull Provider provider) {
            tag(DamageTypeTags.IS_PROJECTILE).add(ESDamageTypes.CAPTAIN_JUSTICE_PROJECTILE);
            tag(DamageTypeTags.BYPASSES_SHIELD).add(ESDamageTypes.THORN_SHIELD);
            tag(DamageTypeTags.BYPASSES_ARMOR).add(ESDamageTypes.COSMIC_PLAGUE);
            tag(DamageTypeTags.BYPASSES_WOLF_ARMOR).add(ESDamageTypes.COSMIC_PLAGUE);
            tag(DamageTypeTags.NO_KNOCKBACK).add(ESDamageTypes.COSMIC_PLAGUE);
            tag(DamageTypeTags.PANIC_CAUSES).add(ESDamageTypes.COSMIC_PLAGUE);
        }
    }
}
