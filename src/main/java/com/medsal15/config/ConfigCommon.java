package com.medsal15.config;

import com.medsal15.ExtraStuck;

import net.neoforged.neoforge.common.ModConfigSpec;

public class ConfigCommon {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.BooleanValue INTERPRETERS_CREATE = BUILDER
            .comment("If true, default create interpreters are loaded").define("interpreters.create", true);
    public static final ModConfigSpec.BooleanValue INTERPRETERS_FARMERSDELIGHT = BUILDER
            .comment("If true, default farmer's delight interpreters are loaded")
            .define("interpreters.farmersdelight", true);

    public static final ModConfigSpec.BooleanValue ISS_GRIST_COSTS = BUILDER
            .comment("If true, adds grist costs to Iron's Spells & Spellbooks items")
            .define("integration.irons_spellbooks.grist_costs", true);
    public static final ModConfigSpec.BooleanValue ISS_COMBINATIONS = BUILDER
            .comment("If true, adds combination recipes to multiple Iron's Spells & Spellbooks items")
            .define("integration.irons_spellbooks.combinations", true);
    public static final ModConfigSpec.BooleanValue ISS_LOOT_COMBINATIONS = BUILDER
            .comment(
                    "If true, adds combination recipes to Iron's Spells & Spellbooks items that are dropped by entities",
                    "These include the Rotten Spell Book, Blood Staff, and Hither-Thither Wand",
                    "Basically, most non-craftable Iron's Spellbooks items")
            .define("integration.irons_spellbooks.loot_combinations", false);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean configEnabled(String name) {
        switch (name) {
            case "interpreters.create":
                return INTERPRETERS_CREATE.getAsBoolean();
            case "interpreters.farmersdelight":
                return INTERPRETERS_FARMERSDELIGHT.getAsBoolean();
            case "integration.irons_spellbooks.grist_costs":
                return ISS_GRIST_COSTS.getAsBoolean();
            case "integration.irons_spellbooks.combinations":
                return ISS_COMBINATIONS.getAsBoolean();
            case "integration.irons_spellbooks.loot_combinations":
                return ISS_LOOT_COMBINATIONS.getAsBoolean();
            default:
                ExtraStuck.LOGGER.warn("Unknown or non-boolean config option: {}", name);
                return false;
        }
    }
}
