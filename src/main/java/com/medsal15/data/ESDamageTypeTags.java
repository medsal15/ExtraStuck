package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ESDamageTypes;
import com.medsal15.ExtraStuck;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ESDamageTypeTags extends DamageTypeTagsProvider {
    public ESDamageTypeTags(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
            ExistingFileHelper fileHelper) {
        super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
    }

    @Override
    protected void addTags(@Nonnull Provider provider) {
        tag(DamageTypeTags.IS_PROJECTILE).add(ESDamageTypes.CAPTAIN_JUSTICE_PROJECTILE);
        tag(DamageTypeTags.BYPASSES_SHIELD).add(ESDamageTypes.THORN_SHIELD);
    }
}
