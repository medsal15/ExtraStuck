package com.medsal15.data.tags;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.utils.ESTags;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.TagsProvider;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ESMobEffectTags extends TagsProvider<MobEffect> {
    public ESMobEffectTags(PackOutput output, CompletableFuture<Provider> lookupProvider,
            ExistingFileHelper fileHelper) {
        super(output, Registries.MOB_EFFECT, lookupProvider, ExtraStuck.MODID, fileHelper);
    }

    @Override
    protected void addTags(@Nonnull Provider provider) {
        tag(ESTags.MobEffects.COSMIC_PLAGUE_IMMUNITY).add(MobEffects.POISON.getKey());
        tag(ESTags.MobEffects.COSMIC_PLAGUE_PARTIAL_IMMUNITY).add(MobEffects.WITHER.getKey());
    }
}
