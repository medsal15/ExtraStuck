package com.medsal15;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ExtraStuck.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.DEDICATED_SERVER)
public class ServerConfig {
        private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

        private static final ModConfigSpec.BooleanValue WARN_NO_MINESTUCK = BUILDER
                        .comment("Whether to warn if Minestuck is not loaded")
                        .define("warnNoMinestuck", true);

        public static final ModConfigSpec SPEC = BUILDER.build();

        public static boolean warnNoMinestuck;

        @SubscribeEvent
        static void onLoad(final ModConfigEvent event) {
                warnNoMinestuck = WARN_NO_MINESTUCK.get();
        }
}
