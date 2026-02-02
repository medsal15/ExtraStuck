package com.medsal15.items.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.google.common.collect.ImmutableMap;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.medsal15.ExtraStuck;
import com.medsal15.config.ConfigClient;
import com.medsal15.config.ConfigServer;
import com.medsal15.data.ESLangProvider;
import com.medsal15.utils.ESTags;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.player.EnumAspect;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.AddReloadListenerEvent;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;

@EventBusSubscriber
public class VisionItem extends Item implements IVision {
    private static Map<EnumAspect, List<VisionEffect>> effects = new HashMap<>();

    public static List<VisionEffect> getEffects(EnumAspect aspect) {
        return effects.get(aspect);
    }

    @Nullable
    private final EnumAspect aspect;

    public VisionItem(Properties properties) {
        super(properties);
        aspect = null;
    }

    public VisionItem(Properties properties, EnumAspect aspect) {
        super(properties);
        this.aspect = aspect;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (ConfigClient.displayVisionWarning) {
            int limit = ConfigServer.VISION_LIMIT.get();
            if (limit == 1) {
                tooltipComponents
                        .add(Component.translatable(ESLangProvider.VISION_HINT_ONE).withStyle(ChatFormatting.GRAY));
            } else if (limit > 1) {
                tooltipComponents
                        .add(Component.translatable(ESLangProvider.VISION_HINT_MANY, limit)
                                .withStyle(ChatFormatting.GRAY));
            }
        }
    }

    @Override
    public void inventoryTick(@Nonnull ItemStack stack, @Nonnull Level level, @Nonnull Entity entity, int slotId,
            boolean isSelected) {
        // Handled in CommonEvents.java
        if (ModList.get().isLoaded("curios"))
            return;
        if (!(entity instanceof LivingEntity livingEntity) || aspect == null || level.isClientSide)
            return;

        int limit = ConfigServer.VISION_LIMIT.get();
        if (limit == 0)
            return;

        int visions = 0;
        if (livingEntity instanceof ServerPlayer player) {
            for (ItemStack itemStack : player.getInventory().items) {
                if (itemStack.is(ESTags.Items.ACTIVE_VISION))
                    visions++;
            }
        }
        if (visions <= limit && effects.containsKey(aspect)) {
            getEffects().forEach(eff -> livingEntity.addEffect(new MobEffectInstance(eff.effect, 100, eff.amplifier)));
        }
    }

    @Override
    public List<VisionEffect> getEffects() {
        return aspect != null ? effects.getOrDefault(aspect, List.of()) : List.of();
    }

    @SubscribeEvent
    public static void onServerStopped(final ServerStoppedEvent event) {
        effects = Map.of();
    }

    @SubscribeEvent
    public static void onResourceReload(final AddReloadListenerEvent event) {
        event.addListener(new Loader());
    }

    private static final class Loader extends SimpleJsonResourceReloadListener {
        Loader() {
            super(new GsonBuilder().create(), "extrastuck/config/vision_effects");
        }

        @Override
        protected void apply(@Nonnull Map<ResourceLocation, JsonElement> object,
                @Nonnull ResourceManager resourceManager, @Nonnull ProfilerFiller profiler) {
            Map<EnumAspect, List<VisionEffect>> newEffects = new HashMap<>();

            for (Map.Entry<ResourceLocation, JsonElement> entry : object.entrySet()) {
                VisionEffectEntry.CODEC.parse(JsonOps.INSTANCE, entry.getValue())
                        .resultOrPartial(message -> ExtraStuck.LOGGER.error("Couldn't parse vision effect {}: {}",
                                entry.getKey(), message))
                        .ifPresent(e -> {
                            EnumAspect aspect = e.aspect;
                            List<VisionEffect> visionEffects;
                            if (newEffects.containsKey(aspect)) {
                                visionEffects = newEffects.get(aspect);
                            } else {
                                visionEffects = new ArrayList<>();
                                newEffects.put(aspect, visionEffects);
                            }
                            visionEffects.addAll(e.effects);
                        });
            }

            effects = ImmutableMap.copyOf(newEffects);
        }
    }

    public record VisionEffect(Holder<MobEffect> effect, int amplifier) {
        public static final Codec<VisionEffect> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                MobEffect.CODEC.fieldOf("effect").forGetter(VisionEffect::effect),
                Codec.INT.optionalFieldOf("amplifier", 0).forGetter(VisionEffect::amplifier))
                .apply(inst, VisionEffect::new));
    }

    public record VisionEffectEntry(EnumAspect aspect, List<VisionEffect> effects) {
        public static final Codec<VisionEffectEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                EnumAspect.CODEC.fieldOf("aspect").forGetter(VisionEffectEntry::aspect),
                VisionEffect.CODEC.listOf().optionalFieldOf("effects", List.of()).forGetter(VisionEffectEntry::effects))
                .apply(inst, VisionEffectEntry::new));
    }
}
