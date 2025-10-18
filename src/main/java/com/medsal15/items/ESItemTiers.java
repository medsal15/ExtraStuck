package com.medsal15.items;

import com.medsal15.utils.ESTags;

import net.minecraft.world.item.Items;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.SimpleTier;

public final class ESItemTiers {
    public static final Tier COPPER_TIER = new SimpleTier(ESTags.Blocks.INCORRECT_FOR_COPPER_TIER, 150, 4F, 1.5F, 10,
            () -> Ingredient.of(Items.COPPER_INGOT));
}
