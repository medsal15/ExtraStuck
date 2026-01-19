package com.medsal15.data.tags;

import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.fluid.MSFluids;

import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ESFluidTags extends FluidTagsProvider {
    public ESFluidTags(PackOutput output, CompletableFuture<Provider> lookupProvider, ExistingFileHelper fileHelper) {
        super(output, lookupProvider, ExtraStuck.MODID, fileHelper);
    }

    @Override
    protected void addTags(@Nonnull Provider provider) {
        tag(ESTags.Fluids.REACTOR_FLUIDS)
                .add(Fluids.WATER, MSFluids.BLOOD.get(), MSFluids.BRAIN_JUICE.get(), MSFluids.ENDER.get(),
                        MSFluids.LIGHT_WATER.get(), MSFluids.OIL.get(), MSFluids.WATER_COLORS.get(),
                        MSFluids.CAULK.get())
                .addTag(Tags.Fluids.WATER);
    }
}
