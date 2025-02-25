package com.medsal15.data;

import com.medsal15.ESItems;
import com.medsal15.ExtraStuck;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public class ESLangProvider extends LanguageProvider {
    public ESLangProvider(PackOutput output) {
        super(output, ExtraStuck.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup.extrastuck", "ExtraStuck");

        addItem(ESItems.WOODEN_SHIELD, "Wooden Shield");
    }
}
