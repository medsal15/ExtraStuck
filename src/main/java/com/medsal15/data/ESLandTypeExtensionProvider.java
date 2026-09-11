package com.medsal15.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.medsal15.world.land.ESLandTypes;
import com.mraof.minestuck.world.gen.structure.blocks.StructureBlockRegistry;
import com.mraof.minestuck.world.lands.LandTypeExtensions;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;

public class ESLandTypeExtensionProvider implements DataProvider {
    private final PackOutput output;
    private final CompletableFuture<HolderLookup.Provider> provider;
    private final Map<ResourceLocation, LandTypeExtensions.ParsedExtension> extensionsMap = new HashMap<>();

    public ESLandTypeExtensionProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        this.output = output;
        this.provider = provider;
    }

    @Override
    public final CompletableFuture<?> run(@Nonnull CachedOutput cache) {
        return provider.thenCompose(provider -> {
            addExtensions(provider);

            List<CompletableFuture<?>> futures = new ArrayList<>(extensionsMap.size());

            for (Map.Entry<ResourceLocation, LandTypeExtensions.ParsedExtension> entry : extensionsMap.entrySet()) {
                Path path = getPath(entry.getKey());
                futures.add(DataProvider.saveStable(cache, provider, LandTypeExtensions.ParsedExtension.CODEC,
                        entry.getValue(), path));
            }

            return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
        });
    }

    protected void addExtensions(HolderLookup.Provider provider) {
        ESLandTypes.TERRAINS.getEntries().forEach(entry -> {
            TerrainLandType landType = entry.get();
            StructureBlockRegistry blockRegistry = new StructureBlockRegistry();
            landType.registerBlocks(blockRegistry);
            extensionsMap.put(entry.getKey().location().withPrefix("terrain/"),
                    landType.getExtensions(provider, blockRegistry));
        });
    }

    private Path getPath(ResourceLocation id) {
        return output.getOutputFolder(PackOutput.Target.DATA_PACK).resolve(id.getNamespace())
                .resolve(String.format("minestuck/land_type_extension/%s.json", id.getPath()));
    }

    @Override
    public String getName() {
        return "ExtraStuck Land Type Extensions";
    }
}
