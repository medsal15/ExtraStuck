package com.medsal15.data;

import com.medsal15.ESItems;
import com.medsal15.ExtraStuck;
import java.util.function.Supplier;
import net.minecraft.world.item.Item;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ESLangProvider extends LanguageProvider {
    public ESLangProvider(PackOutput output) {
        super(output, ExtraStuck.MODID, "en_us");
    }

    public static final String SHIELD_DAMAGE_KEY = ExtraStuck.MODID + ".shield_damage";
    public static final String SHIELD_EFFECT_KEY = ExtraStuck.MODID + ".shield_effect";

    @Override
    protected void addTranslations() {
        add("itemGroup.extrastuck", "ExtraStuck");

        add(SHIELD_DAMAGE_KEY, "Deals %s damage to melee attackers");
        add(SHIELD_EFFECT_KEY, "Applies %s (%s) to melee attackers");

        addItem(ESItems.WOODEN_SHIELD, "Wooden Shield");
        addItemTooltip(ESItems.WOODEN_SHIELD, "This cheap shield is 100% fire-ready");
        addItem(ESItems.FLAME_SHIELD, "Flame Shield");
        addItemTooltip(ESItems.FLAME_SHIELD, "Uh oh, it seems your wooden shield is burning!");
        addItem(ESItems.HALT_SHIELD, "Halt Shield");
        addItemTooltip(ESItems.HALT_SHIELD, "Forces your enemies to turn back");
        addItem(ESItems.IRON_SHIELD, "Iron Shield");
        addItem(ESItems.GOLD_SHIELD, "Gold Shield");
        addItem(ESItems.DIAMOND_SHIELD, "Diamond Shield");
        addItem(ESItems.NETHERITE_SHIELD, "Netherite Shield");
    }

    protected void addItemTooltip(Supplier<? extends Item> key, String name) {
        add(((Item) (key.get())).getDescriptionId() + ".tooltip", name);
    }
}
