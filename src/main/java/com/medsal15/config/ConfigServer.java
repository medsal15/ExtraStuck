package com.medsal15.config;

import java.util.List;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

public final class ConfigServer {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ConfigValue<Integer> REACTOR_URANIUM_STORAGE = BUILDER
            .comment("How much uranium power should a reactor hold",
                    "Should be higher than the amount produced per tick, otherwise reactors won't run")
            .defineInRange("reactor.uranium_storage", 512, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> REACTOR_FE_STORAGE = BUILDER
            .comment("How much FE should a reactor hold",
                    "Should be higher than the amount produced per tick, otherwise reactors won't run")
            .defineInRange("reactor.fe_storage", 2_560_000, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> REACTOR_FLUID_STORAGE = BUILDER
            .comment("How much fluid should a reactor hold",
                    "Should be higher than the amount consumed per tick, otherwise reactors won't run")
            .defineInRange("reactor.fluid_storage", 4_000, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> REACTOR_CHARGE_TICK = BUILDER
            .comment("How much FE is produced per tick while the reactor is working")
            .defineInRange("reactor.fe_tick", 5_000, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> REACTOR_URANIUM_TICK = BUILDER
            .comment("How much uranium is produced per tick while the reactor is working")
            .defineInRange("reactor.uranium_tick", 1, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> REACTOR_FLUID_TICK = BUILDER
            .comment("How much fluid is consumed per tick, in mB, while the reactor is working")
            .defineInRange("reactor.fluid_tick", 10, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> REACTOR_MAX_FE_TRANSFER = BUILDER
            .comment("Limit to how much FE can be sent in a tick to each neighbors",
                    "Meaning that if all 6 neighbors can receive FE, 6 times this amount will be sent every tick")
            .defineInRange("reactor.max_fe_transfer", 10_000, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> REACTOR_MAX_URANIUM_TRANSFER = BUILDER
            .comment("Limit to how much uranium power can be sent in a tick to each neighbors",
                    "Meaning that if all 6 neighbors can receive uranium power, 6 times this amount will be sent every tick")
            .defineInRange("reactor.max_uranium_transfer", 8, 1, Short.MAX_VALUE);
    public static final ModConfigSpec.BooleanValue REACTOR_EXPLODE = BUILDER
            .comment("If true, reactors explode if they have fuel but no fluid")
            .define("reactor.explode", false);

    public static final ConfigValue<Integer> CHARGER_URANIUM_STORAGE = BUILDER
            .comment("How much uranium power should a charger hold",
                    "Shouldn't be lower than 32, as that would prevent uranium chunks from being used")
            .defineInRange("charger.uranium_storage", 128, 0, Short.MAX_VALUE);
    public static final ConfigValue<Integer> CHARGER_FE_STORAGE = BUILDER
            .comment("How much FE should a charger hold",
                    "Should be higher than the amount than the amount produced per tick, otherwise charger won't self-charge")
            .defineInRange("charger.fe_storage", 64_000, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> CHARGER_CHARGE_TICK = BUILDER
            .comment("How much FE is produced per tick, at the cost of 1 uranium power")
            .defineInRange("charger.fe_tick", 500, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> CHARGER_TRANSFER_TICK = BUILDER
            .comment("How much FE is transferred between storage and item per tick", "Mostly to make charging fancier")
            .defineInRange("charger.fe_transfer", 1_000, 1, Integer.MAX_VALUE);

    public static final ConfigValue<Integer> BLASTER_URANIUM_STORAGE = BUILDER
            .comment("How much uranium power should a blaster hold",
                    "Note that each shot consumes 1 uranium power",
                    "Shouldn't be lower than 32, as that would prevent uranium chunks from being used")
            .defineInRange("blaster.uranium_storage", 128, 1, Short.MAX_VALUE);

    public static final ConfigValue<Integer> RADBOW_CHARGES = BUILDER
            .comment("How many charges a radbow gets from an uranium rod")
            .defineInRange("radbow_charge", 10, 1, Integer.MAX_VALUE);

    public static final ConfigValue<List<? extends String>> COSMIC_SPORE_DIMENSIONS = BUILDER
            .comment("Determines which dimensions trigger the Cosmic Plague Spore's inventory effect",
                    "This is separate from the \"extrastuck:cosmic_dimension_types\" tag and should only be used in case multiple dimensions use the same type",
                    " Default: []")
            .defineList("cosmic_spore.dimensions", List.of(), () -> "minecraft:overworld",
                    s -> s instanceof String str && str.contains(":"));
    public static final ModConfigSpec.BooleanValue COSMIC_SPORE_HARDMODE = BUILDER
            .comment(
                    "If enabled, Cosmic Plague Spore will give Cosmic Plague II, which can spread to nearby entities",
                    "Otherwise, it will only give Poison V")
            .define("cosmic_spore.hard_mode", false);

    public static final ModConfigSpec.BooleanValue COSMIC_PLAGUE_SPREAD = BUILDER
            .comment(
                    "If true, Cosmic Plague attempts to spread to nearby entities that are not tagged \"extrastuck:cosmic_plague_immune\"",
                    "Does not prevent initial application, use the tag for that")
            .define("cosmic_plague.spread", true);
    public static final ConfigValue<Double> COSMIC_PLAGUE_RANGE = BUILDER
            .comment("Sets the range for Cosmic Plague's spread", "Does not have to be an integer")
            .defineInRange("cosmic_plague.range", (double) 5, 0, Integer.MAX_VALUE);

    public static final ConfigValue<Integer> MASTERMIND_DIFFICULTY = BUILDER
            .comment("How many colors are available by default in mastermind cards",
                    "Existing cards will not be affected")
            .defineInRange("modus.mastermind.difficulty", 3, 1, 6);
    public static final ModConfigSpec.BooleanValue MASTERMIND_HARDER = BUILDER
            .comment(
                    "If true, captchalogued mastermind cards will be stored in a more difficult card when retrieved from a mastermind modus")
            .define("modus.mastermind.incremental", false);
    public static final ModConfigSpec.BooleanValue MASTERMIND_CHANGE = BUILDER
            .comment("If true, the Mastermind Modus Card can have its default difficulty changed")
            .define("modus.mastermind.change", true);
    public static final ModConfigSpec.BooleanValue MASTERMIND_CHANGE_PC = BUILDER
            .comment("If true, Mastermind Codebreaker can change a Mastermind Modus Card's default difficulty",
                    "Only affects data components if change is disabled")
            .define("modus.mastermind.change_with_pc", true);

    public static final ConfigValue<Integer> FURNACE_DEFAULT_COST = BUILDER
            .comment("How much fuel (in ticks) does it cost to extract an item from the Furnace Modus",
                    "Does not apply to items that can be smelted in a furnace (these have the cost to smelt 1)")
            .defineInRange("modus.furnace.default_cost", 200, 0, Integer.MAX_VALUE);

    public static final ConfigValue<Integer> STEAM_FUEL_THRESHOLD = BUILDER
            .comment("How much fuel (in ticks) allows refueling a steam-powered weapon",
                    "That is to say, if the weapon's fuel is below or equal to this number, it can be refueled",
                    "Note that if the value is too low, it may prevent the item from being refuelable")
            .defineInRange("weapons.steam_powered.fuel_threshold", 200, 0, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> STEAM_FUEL_CONSUME = BUILDER
            .comment("How much fuel (in ticks) is consumed to attack with a steam-powered weapon")
            .defineInRange("weapons.steam_powered.fuel_consume", 200, 0, Integer.MAX_VALUE);

    public static final ModConfigSpec.BooleanValue VISION_TUNING = BUILDER
            .comment("If true, empty visions can be tuned by aspect holders to their own aspect")
            .define("vision.tuning", true);
    public static final ModConfigSpec.BooleanValue VISION_TUNING_SAFE = BUILDER
            .comment("If true, players with no aspect cannot tune visions into useless ones")
            .define("vision.safe_tuning", false);
    public static final ConfigValue<Integer> VISION_LIMIT = BUILDER
            .comment("How many visions can be active at once for a player", "This is limited to the amount of aspects")
            .defineInRange("vision.limit", 1, 0, 12);
    public static final ConfigValue<Integer> VISION_RUNG = BUILDER
            .comment("Minimum rung needed to attune a vision", "Set to -1 for no minimum")
            .define("vision.rung", -1);
    public static final ModConfigSpec.BooleanValue RAINBOW_WHITELIST = BUILDER
            .comment(
                    "If true, the Rainbow Bow will pick effects from the \"extrastuck:rainbowbow_effects\" tag instead of being fully random")
            .define("rainbowbow_whitelist", false);

    public static final ConfigValue<Integer> ISS_CREATIVE_SHOCK_SURVIVAL = BUILDER
            .comment("Which amplifier of Creative Shock should prevent spell casting for non-creative players",
                    "Note that an amplifier of 3 means level IV")
            .define("integration.irons_spellbooks.creative_shock_survival", 1);
    public static final ConfigValue<Integer> ISS_CREATIVE_SHOCK_CREATIVE = BUILDER
            .comment("Which amplifier of Creative Shock should prevent spell casting for creative players",
                    "Note that an amplifier of 3 means level IV")
            .define("integration.irons_spellbooks.creative_shock_creative", 4);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
