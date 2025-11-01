package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.util.MSTags;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredItem;

public final class ESItemTags extends ItemTagsProvider {
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

        for (DeferredItem<Item> crossbow : ESItems.getCrossbows()) {
            tag(Tags.Items.TOOLS_CROSSBOW).add(crossbow.get());
            tag(ItemTags.CROSSBOW_ENCHANTABLE).add(crossbow.get());
            tag(ItemTags.DURABILITY_ENCHANTABLE).add(crossbow.get());
            tag(ItemTags.VANISHING_ENCHANTABLE).add(crossbow.get());
        }

        for (DeferredItem<Item> tool : ESItems.getMiningTools()) {
            tag(ItemTags.MINING_ENCHANTABLE).add(tool.get());
            tag(ItemTags.MINING_LOOT_ENCHANTABLE).add(tool.get());
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

        tag(ESTags.Items.AMMO).addTag(ESTags.Items.AMMO_HANDGUN);
        tag(ESTags.Items.AMMO_HANDGUN).add(ESItems.HANDGUN_BULLET.get(), ESItems.HEAVY_HANDGUN_BULLET.get());
        tag(ESTags.Items.SHOW_VALUE).add(ESItems.SALESMAN_GOGGLES.get());

        tagShield(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE);

        tag(ItemTags.PIGLIN_LOVED).add(ESItems.GOLD_SHIELD.get(), ESItems.GOLDEN_PAN.get(), ESItems.GOLD_COIN.get());

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

        tag(MSTags.Items.UNREADABLE).add(ESItems.ANTI_DIE.get());
        tag(MSTags.Items.LEGENDARY).add(ESItems.INFINI_DIE.get());

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
