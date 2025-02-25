package com.medsal15.data;

import java.util.concurrent.CompletableFuture;

import com.medsal15.ExtraStuck;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID, bus = EventBusSubscriber.Bus.MOD)
public final class ESData {
    @SubscribeEvent
    public static final void gatherData(GatherDataEvent event) {
        DataGenerator gen = event.getGenerator();
        PackOutput output = gen.getPackOutput();
        ExistingFileHelper fileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        gen.addProvider(event.includeClient(), new ESLangProvider(output));
        gen.addProvider(event.includeClient(), new ESItemModelProvider(output, fileHelper));

        gen.addProvider(event.includeServer(), new ESRecipeProvider(output, lookupProvider));
    }
}
