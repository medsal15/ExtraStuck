package com.medsal15.data;

import com.medsal15.ExtraStuck;
import com.medsal15.particles.ESParticleTypes;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.ParticleDescriptionProvider;

public class ESParticleDescriptions extends ParticleDescriptionProvider {
    public ESParticleDescriptions(PackOutput output, ExistingFileHelper helper) {
        super(output, helper);
    }

    @Override
    protected void addDescriptions() {
        spriteSet(ESParticleTypes.URANIUM_BLAST.get(), ExtraStuck.modid("uranium_blast"), 8, false);
    }
}
