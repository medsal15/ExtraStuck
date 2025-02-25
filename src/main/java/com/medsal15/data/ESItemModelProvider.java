package com.medsal15.data;

import com.medsal15.ExtraStuck;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ESItemModelProvider extends ItemModelProvider {
    public ESItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, ExtraStuck.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
    }
}
