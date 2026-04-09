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
    private static final ModConfigSpec.BooleanValue DISPLAY_INNATE_ENCHANTMENTS = BUILDER
            .comment("Whether to show innate enchantments if they are present",
                    "In case you have another mod that shows them anyways")
            .define("tooltip.innate_enchantments", true);
    private static final ModConfigSpec.BooleanValue DISPLAY_VISION_WARNING = BUILDER
            .comment("If enabled, infused visions will include a warning about the use limit")
            .define("tooltip.vision_warning", true);
    private static final ModConfigSpec.BooleanValue ADD_MISSING_MOD_TOOLTIP = BUILDER
            .comment("If enabled, compat items which require a specific mod will mention it in their tooltip")
            .define("tooltip.missing_mod", true);

    private static final ModConfigSpec.EnumValue<DisplayLocation> GRIST_DISPLAY_LOCATION = BUILDER
            .comment("Specifies the grist viewers display the current location's grist layer info",
                    "TOP/CENTER/BOTTOM: Where the grist is shown vertically",
                    "LEFT/MIDDLE/RIGHT: Where the grist is shown horizontally")
            .defineEnum("grist_viewers.location", DisplayLocation.TOP_LEFT);
    private static final ModConfigSpec.BooleanValue GRIST_DISPLAY_COLORED = BUILDER
            .comment("If enabled, the grist display will be colored with the grist's color")
            .define("grist_viewers.colored", false);
    private static final ModConfigSpec.BooleanValue GRIST_DISPLAY_NAME = BUILDER
            .comment("If enabled, the grist display will be followed with the grist's name")
            .define("grist_viewers.name", true);
    private static final ModConfigSpec.BooleanValue GRIST_DISPLAY_TEXTURE = BUILDER
            .comment("If enabled, the grist display will be followed with the grist's texture")
            .define("grist_viewers.grist", false);
    private static final ModConfigSpec.BooleanValue GRIST_DISPLAY_CUSTOM = BUILDER
            .comment(
                    "If enabled, the grist display will be aligned based on the given coordinates instead of the screen size",
                    "This means that TOP_LEFT will treat the coordinates as the top left corner of the screen",
                    "and that BOTTOM_MIDDLE will treat the coordinates as the bottom middle of the screen")
            .define("grist_viewers.custom_location.enabled", false);
    private static final ModConfigSpec.IntValue GRIST_DISPLAY_X = BUILDER
            .comment("Horizontal coordinates of the display, from the left")
            .defineInRange("grist_viewers.custom_location.x", 0, 0, Integer.MAX_VALUE);
    private static final ModConfigSpec.IntValue GRIST_DISPLAY_Y = BUILDER
            .comment("Vertical coordinates of the display, from the top")
            .defineInRange("grist_viewers.custom_location.y", 0, 0, Integer.MAX_VALUE);

    private static final ModConfigSpec.BooleanValue ADD_CONVERTION_RECIPES = BUILDER
            .comment(
                    "If enabled and Create is installed, mysterious convertion recipes will be added for Extrastuck items")
            .define("compat.convertion_recipes", true);
    private static final ModConfigSpec.BooleanValue ADD_MINESTUCK_PONDER = BUILDER
            .comment("If enabled and Ponder is installed, Minestuck machines will have ponder entries")
            .define("compat.ponder_minestuck_machines", true);

    public static enum BoondollarDisplayMode {
        DISABLED,
        RAW,
        AVERAGE,
        RANDOM;
    }

    public static enum DisplayLocation {
        TOP_LEFT,
        TOP_MIDDLE,
        TOP_RIGHT,
        CENTER_LEFT,
        CENTER_MIDDLE,
        CENTER_RIGHT,
        BOTTOM_LEFT,
        BOTTOM_MIDDLE,
        BOTTOM_RIGHT;

        public int getX(int screenWidth, int textWidth) {
            switch (this) {
                default:
                case TOP_LEFT, CENTER_LEFT, BOTTOM_LEFT:
                    return 0;
                case TOP_MIDDLE, CENTER_MIDDLE, BOTTOM_MIDDLE:
                    return (screenWidth - textWidth) / 2;
                case TOP_RIGHT, CENTER_RIGHT, BOTTOM_RIGHT:
                    return screenWidth - textWidth;
            }
        }

        public int getY(int screenHeight, int textHeight) {
            switch (this) {
                default:
                case TOP_LEFT, TOP_MIDDLE, TOP_RIGHT:
                    return 0;
                case CENTER_LEFT, CENTER_MIDDLE, CENTER_RIGHT:
                    return (screenHeight - textHeight) / 2;
                case BOTTOM_LEFT, BOTTOM_MIDDLE, BOTTOM_RIGHT:
                    return screenHeight - textHeight;
            }
        }
    }

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static BoondollarDisplayMode boondollarDisplayMode;
    public static boolean displayShieldInfo;
    public static boolean displayInnateEnchants;
    public static boolean displayVisionWarning;
    public static boolean addConvertionRecipes;
    public static boolean addMissingModTooltip;
    public static boolean addPonderMinestuckEntries;
    public static DisplayLocation gristDisplayLocation;
    public static boolean gristDisplayColored;
    public static boolean gristDisplayName;
    public static boolean gristDisplayTexture;
    public static boolean gristDisplayCustom;
    public static int gristDisplayCustomX;
    public static int gristDisplayCustomY;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event) {
        displayShieldInfo = DISPLAY_SHIELD_INFO.get();
        displayInnateEnchants = DISPLAY_INNATE_ENCHANTMENTS.get();
        displayVisionWarning = DISPLAY_VISION_WARNING.get();
        boondollarDisplayMode = BOONDOLLAR_DISPLAY_MODE.get();
        addConvertionRecipes = ADD_CONVERTION_RECIPES.get();
        addMissingModTooltip = ADD_MISSING_MOD_TOOLTIP.get();
        addPonderMinestuckEntries = ADD_MINESTUCK_PONDER.get();
        gristDisplayLocation = GRIST_DISPLAY_LOCATION.get();
        gristDisplayColored = GRIST_DISPLAY_COLORED.get();
        gristDisplayName = GRIST_DISPLAY_NAME.get();
        gristDisplayTexture = GRIST_DISPLAY_TEXTURE.get();
        gristDisplayCustom = GRIST_DISPLAY_CUSTOM.get();
        gristDisplayCustomX = GRIST_DISPLAY_X.get();
        gristDisplayCustomY = GRIST_DISPLAY_Y.get();
    }
}
