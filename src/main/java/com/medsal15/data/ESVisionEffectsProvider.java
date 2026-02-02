package com.medsal15.data;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

import javax.annotation.Nonnull;

import com.google.gson.JsonElement;
import com.medsal15.items.tools.VisionItem.VisionEffect;
import com.medsal15.items.tools.VisionItem.VisionEffectEntry;
import com.mojang.serialization.JsonOps;
import com.mraof.minestuck.player.EnumAspect;

import net.minecraft.core.Holder;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;

public class ESVisionEffectsProvider implements DataProvider {
    private final Map<ResourceLocation, VisionEffectEntry> effects = new HashMap<>();
    private final PackOutput output;
    private final String modid;

    public ESVisionEffectsProvider(PackOutput output, String modid) {
        this.output = output;
        this.modid = modid;
    }

    protected void registerEffects() {
        add(EnumAspect.BLOOD, MobEffects.ABSORPTION);
        add(EnumAspect.BREATH, MobEffects.MOVEMENT_SPEED);
        add(EnumAspect.DOOM, MobEffects.DAMAGE_RESISTANCE);
        add(EnumAspect.HEART, MobEffects.ABSORPTION);
        add(EnumAspect.HOPE, MobEffects.FIRE_RESISTANCE);
        add(EnumAspect.LIFE, MobEffects.REGENERATION);
        add(EnumAspect.LIGHT, MobEffects.LUCK);
        add(EnumAspect.MIND, MobEffects.NIGHT_VISION);
        add(EnumAspect.RAGE, MobEffects.DAMAGE_BOOST);
        add(EnumAspect.SPACE, MobEffects.JUMP);
        add(EnumAspect.TIME, MobEffects.DIG_SPEED);
        add(EnumAspect.VOID, MobEffects.INVISIBILITY);
    }

    protected void add(EnumAspect aspect, Holder<MobEffect> effect) {
        add(ResourceLocation.fromNamespaceAndPath(modid, aspect.toString().toLowerCase()), aspect, effect, 0);
    }

    protected void add(ResourceLocation name, EnumAspect aspect, Holder<MobEffect> effect, int amplifier) {
        effects.put(name, new VisionEffectEntry(aspect, List.of(new VisionEffect(effect, amplifier))));
    }

    @Override
    public CompletableFuture<?> run(@Nonnull CachedOutput cache) {
        registerEffects();

        Path outputPath = output.getOutputFolder();
        List<CompletableFuture<?>> futures = new ArrayList<>(effects.size());

        for (Map.Entry<ResourceLocation, VisionEffectEntry> entry : effects.entrySet()) {
            Path effectPath = getPath(outputPath, entry.getKey());
            JsonElement json = VisionEffectEntry.CODEC.encodeStart(JsonOps.INSTANCE, entry.getValue()).getOrThrow();
            futures.add(DataProvider.saveStable(cache, json, effectPath));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));
    }

    private static Path getPath(Path outputPath, ResourceLocation id) {
        return outputPath
                .resolve("data/" + id.getNamespace() + "/extrastuck/config/vision_effects/" + id.getPath() + ".json");
    }

    @Override
    public String getName() {
        return "Vision Effects";
    }
}
