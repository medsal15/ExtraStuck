package com.medsal15.config;

import com.medsal15.ExtraStuck;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = ExtraStuck.MODID, value = Dist.CLIENT)
public class ConfigClient {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue DISPLAY_SHIELD_INFO = BUILDER
            .comment("Whether to show the shield special effects in the tooltip")
            .define("tooltip.shield_info", true);
    private static final ModConfigSpec.EnumValue<BoondollarDisplayMode> BOONDOLLAR_DISPLAY_MODE = BUILDER
            .comment("How boondollar costs are shown when wearing an item that makes them visible",
                    "DISABLED: completely hides the value",
                    "RAW: displays lowest and highest prices (when available)",
                    "AVERAGE: displays the middle ground for the above",
                    "RANDOM: displays a random value between the lowest and highest")
            .defineEnum("tooltip.boondollar_display", BoondollarDisplayMode.RANDOM);
    private static final ModConfigSpec.BooleanValue ADD_MISSING_MOD_TOOLTIP = BUILDER
            .comment("Whether to show in compat items that the required mod is missing")
            .define("tooltip.missing_mod", true);
    private static final ModConfigSpec.BooleanValue ADD_CONVERTION_RECIPES = BUILDER
            .comment("Whether to add mysterious convertion recipes when create is loaded or not")
            .define("compat.convertion_recipes", true);

    public static enum BoondollarDisplayMode {
        DISABLED,
        RAW,
        AVERAGE,
        RANDOM;
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static BoondollarDisplayMode boondollarDisplayMode;
    public static boolean displayShieldInfo;
    public static boolean addConvertionRecipes;
    public static boolean addMissingModTooltip;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        displayShieldInfo = DISPLAY_SHIELD_INFO.get();
        boondollarDisplayMode = BOONDOLLAR_DISPLAY_MODE.get();
        addConvertionRecipes = ADD_CONVERTION_RECIPES.get();
        addMissingModTooltip = ADD_MISSING_MOD_TOOLTIP.get();
    }
}
