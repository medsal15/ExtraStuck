package com.medsal15.config;

import com.medsal15.ExtraStuck;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ExtraStuck.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ConfigClient {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISPLAY_SHIELD_INFO = BUILDER
            .comment("Whether to show the shield special effects in the tooltip")
            .define("displayShieldInfo", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean displayShieldInfo;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        displayShieldInfo = DISPLAY_SHIELD_INFO.get();
    }
}
