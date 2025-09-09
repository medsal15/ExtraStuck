package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.mraof.minestuck.fluid.MSFluids;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ESFluidTags extends FluidTagsProvider {
    public ESFluidTags(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
    }

    public static final TagKey<Fluid> REACTOR_FLUIDS = FluidTags.create(ExtraStuck.modid("reactor_fluids"));

    @Override
    protected void addTags(@Nonnull Provider provider) {
        tag(REACTOR_FLUIDS).add(Fluids.WATER, MSFluids.BLOOD.get(), MSFluids.BRAIN_JUICE.get(), MSFluids.ENDER.get(),
                MSFluids.LIGHT_WATER.get(), MSFluids.OIL.get(), MSFluids.WATER_COLORS.get());
    }
}
