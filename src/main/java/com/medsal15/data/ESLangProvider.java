package com.medsal15.data;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;
import com.medsal15.entities.ESEntities;
import com.medsal15.items.ESItems;

import java.util.function.Supplier;

import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.item.Item;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ESLangProvider extends LanguageProvider {
    public ESLangProvider(PackOutput output) {
        super(output, ExtraStuck.MODID, "en_us");
    }

    public static final String SHIELD_DAMAGE_KEY = ExtraStuck.MODID + ".shield_damage";
    public static final String SHIELD_EFFECT_KEY = ExtraStuck.MODID + ".shield_effect";
    public static final String SHIELD_SELF_EFFECT_KEY = ExtraStuck.MODID + ".shield_self_effect";
    public static final String SLIED_DROP_KEY = ESItems.SLIED.get().getDescriptionId() + ".drop";
    public static final String ENERGY_STORAGE_KEY = ExtraStuck.MODID + ".energy_storage";

    @Override
    protected void addTranslations() {
        add("itemGroup.extrastuck", "ExtraStuck");

        add(SHIELD_DAMAGE_KEY, "Deals %s damage to melee attackers");
        add(SHIELD_EFFECT_KEY, "Applies %s (%s) to melee attackers");
        add(SHIELD_SELF_EFFECT_KEY, "Applies %s (%s) when attacked");
        add(ENERGY_STORAGE_KEY, "%s / %s RF");

        add("patchouli.extrastuck.title", "ExtraStuck Guide");
        add("patchouli.extrastuck.landing", "Unofficial ExtraStuck Walkthrough (100%% official)");

        addItem(ESItems.WOODEN_SHIELD, "Wooden Shield");
        addItemTooltip(ESItems.WOODEN_SHIELD, "This cheap shield is 100% fire-ready");
        addItem(ESItems.FLAME_SHIELD, "Flame Shield");
        addItemTooltip(ESItems.FLAME_SHIELD, "Uh oh, it seems your wooden shield is burning!");
        addItem(ESItems.HALT_SHIELD, "Halt Shield");
        addItemTooltip(ESItems.HALT_SHIELD, "Forces your enemies to turn back");
        addItem(ESItems.LIGHT_SHIELD, "Light Shield");
        addItem(ESItems.NON_CONTACT_CONTRACT, "Non-Contact Contract");
        addItemTooltip(ESItems.NON_CONTACT_CONTRACT, "This piece of paper has the ability to prevent contact with you");
        addItem(ESItems.SLIED, "slied");
        addItemTooltip(ESItems.SLIED, "Can this even block damage?");
        add(SLIED_DROP_KEY, "Expectedly, it could not");
        addItem(ESItems.RIOT_SHIELD, "Riot Shield");
        addItem(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE, "Captain Justice's Shield Throwable");
        addItemTooltip(ESItems.CAPTAIN_JUSTICE_SHIELD_THROWABLE, "Straight from a comic book and ready to throw down!");
        addItem(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD, "Captain Justice's Throwable Shield");
        addItemTooltip(ESItems.CAPTAIN_JUSTICE_THROWABLE_SHIELD, "Straight from a comic book and ready to protect!");
        addEntityType(ESEntities.CAPTAIN_JUSTICE_SHIELD, "Captain Justice Shield");
        addItem(ESItems.CAPITASHIELD, "Capitashield");
        addItemTooltip(ESItems.CAPITASHIELD, "Protects you, at a cost");
        addItem(ESItems.IRON_SHIELD, "Iron Shield");
        addItem(ESItems.SPIKES_ON_A_SLAB, "Spikes on a Slab");
        addItem(ESItems.JAWBITER, "Jawbiter");
        addItem(ESItems.ELDRITCH_SHIELD, "Eldritch Shield");
        addItem(ESItems.GOLD_SHIELD, "Gold Shield");
        addItem(ESItems.FLUX_SHIELD, "Flux Shield");
        addItem(ESItems.DIAMOND_SHIELD, "Diamond Shield");
        addItem(ESItems.NETHERITE_SHIELD, "Netherite Shield");
        addItem(ESItems.GARNET_SHIELD, "Garnet Shield");
        addItem(ESItems.POGO_SHIELD, "Pogo Shield");
        addItemTooltip(ESItems.POGO_SHIELD, "The shield that bounces back!");
        addItem(ESItems.RETURN_TO_SENDER, "Return to Sender");
        addItemTooltip(ESItems.RETURN_TO_SENDER, "The shield that shoots back!");

        addItem(ESItems.NETHER_ARROW, "Nether Arrow");
        addItemTooltip(ESItems.NETHER_ARROW, "Likes the heat");
        addEntityType(ESEntities.NETHER_ARROW, "Nether Arrow");
        addItem(ESItems.FLAME_ARROW, "Flame Arrow");
        addItemTooltip(ESItems.FLAME_ARROW, "Extra hot");
        addEntityType(ESEntities.FLAME_ARROW, "Flame Arrow");
        addItem(ESItems.CARDBOARD_ARROW, "Cardboard Arrow");
        addEntityType(ESEntities.CARDBOARD_ARROW, "Cardboard Arrow");
        addItem(ESItems.MISSED_YOU, "Missed You");
        addEntityType(ESEntities.MISSED_ARROW, "Missed Arrow");
        addItem(ESItems.SWEET_TOOTH, "Sweet Tooth");
        addItemTooltip(ESItems.SWEET_TOOTH, "Delicious and dangerous!");
        addEntityType(ESEntities.CANDY_ARROW, "Sweet Tooth");
        addItem(ESItems.LIGHTNING_ARROW, "Lightning Bolt");
        addEntityType(ESEntities.LIGHTNING_ARROW, "Lighting Arrow");
        addItem(ESItems.EXPLOSIVE_ARROW, "Explosive Arrow");
        addEntityType(ESEntities.EXPLOSIVE_ARROW, "Explosive Arrow");
        addItem(ESItems.IRON_ARROW, "Iron Arrow");
        addEntityType(ESEntities.IRON_ARROW, "Iron Arrow");
        addItem(ESItems.QUARTZ_ARROW, "Quartz Arrow");
        addEntityType(ESEntities.QUARTZ_ARROW, "Quartz Arrow");
        addItem(ESItems.PRISMARINE_ARROW, "Prismarine Arrow");
        addItemTooltip(ESItems.PRISMARINE_ARROW, "Best swimmer in class");
        addEntityType(ESEntities.PRISMARINE_ARROW, "Prismarine Arrow");
        addItem(ESItems.GLASS_ARROW, "Glass Arrow");
        addItemTooltip(ESItems.GLASS_ARROW, "Fragile");
        addEntityType(ESEntities.GLASS_ARROW, "Glass Arrow");
        addItem(ESItems.AMETHYST_ARROW, "Amethyst Arrow");
        addEntityType(ESEntities.AMETHYST_ARROW, "Amethyst Arrow");
        addItem(ESItems.PROJECDRILL, "Projecdrill");
        addEntityType(ESEntities.MINING_ARROW, "Mining Arrow");

        addDeathMessages(ESDamageTypes.CAPTAIN_JUSTICE_PROJECTILE, "%1$s was shot by %2$s",
                "%1$s was shot by %2$s with %3$s");
        addDeathMessages(ESDamageTypes.THORN_SHIELD, "%1$s struck %2$s's shield too hard",
                "%1$s struck %2$s's %3$s too hard");
    }

    protected void addItemTooltip(Supplier<? extends Item> key, String text) {
        add(((Item) (key.get())).getDescriptionId() + ".tooltip", text);
    }

    protected void addDeathMessages(ResourceKey<DamageType> damage, String generic, String namedItem) {
        add("death.attack." + damage.location().toString(), generic);
        add("death.attack." + damage.location().toString() + ".item", namedItem);
    }
}
