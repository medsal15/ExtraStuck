package com.medsal15.interpreters;

import com.medsal15.ExtraStuck;
import com.medsal15.interpreters.create.CreateBasicInterpreter;
import com.medsal15.interpreters.create.ItemApplicationInterpreter;
import com.medsal15.interpreters.create.SequencedInterpreter;
import com.medsal15.interpreters.farmersdelight.CookingPotInterpreter;
import com.medsal15.interpreters.farmersdelight.CuttingBoardInterpreter;
import com.mojang.serialization.MapCodec;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.alchemy.recipe.generator.recipe.RecipeInterpreter;

import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESInterpretertypes {
    public static final DeferredRegister<MapCodec<? extends RecipeInterpreter>> INTERPRETER_TYPES = DeferredRegister
            .create(Minestuck.id("recipe_interpreter_codec"), ExtraStuck.MODID);

    static {
        INTERPRETER_TYPES.register("special_recipe", () -> SpecialInterpreter.CODEC);

        // Create
        INTERPRETER_TYPES.register("create_basic", () -> CreateBasicInterpreter.CODEC);
        INTERPRETER_TYPES.register("create_item_application", () -> ItemApplicationInterpreter.CODEC);
        INTERPRETER_TYPES.register("create_sequenced_assembly", () -> SequencedInterpreter.CODEC);

        // Farmer's Delight
        INTERPRETER_TYPES.register("farmersdelight_cooking", () -> CookingPotInterpreter.CODEC);
        INTERPRETER_TYPES.register("farmersdelight_cutting", () -> CuttingBoardInterpreter.CODEC);
    }
}
