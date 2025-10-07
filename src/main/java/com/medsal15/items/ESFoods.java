package com.medsal15.items;

import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Items;

public final class ESFoods {
    public static final FoodProperties SWEET_TOOTH = new FoodProperties.Builder().fast().nutrition(1)
            .saturationModifier(.5F).build();
    public static final FoodProperties PIZZA_SLICE = new FoodProperties.Builder().nutrition(2).saturationModifier(.1F)
            .build();
    public static final FoodProperties SUSHROOM_STEW = new FoodProperties.Builder().nutrition(6).saturationModifier(.5F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 50), .15F)
            .effect(() -> new MobEffectInstance(MobEffects.POISON, 50), .15F).usingConvertsTo(Items.BOWL).build();
    public static final FoodProperties RADBURGER = new FoodProperties.Builder().nutrition(13).saturationModifier(.7F)
            .effect(() -> new MobEffectInstance(MobEffects.WITHER, 100, 1), .6F)
            .build();
}
