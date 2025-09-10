package com.medsal15.config;

import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.common.ModConfigSpec.ConfigValue;

public class ConfigCommon {
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
            .comment("Should reactors explode if they have fuel but no fluid")
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
            .defineInRange("charge.fe_tick", 500, 1, Integer.MAX_VALUE);
    public static final ConfigValue<Integer> CHARGER_TRANSFER_TICK = BUILDER
            .comment("How much FE is transferred between storage and item per tick", "Mostly to make charging fancier")
            .defineInRange("charge.fe_transfer", 1_000, 1, Integer.MAX_VALUE);

    public static final ModConfigSpec SPEC = BUILDER.build();
}
