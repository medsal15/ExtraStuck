package com.medsal15.data;

import com.medsal15.ESItems;
import com.medsal15.ExtraStuck;

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
        addItem(ESItems.THORN_SHIELD, "Thorn Shield");
        addItem(ESItems.WITHERED_SHIELD, "Withered Shield");
        addItem(ESItems.FLAME_SHIELD, "Flame Shield");
    }
}
