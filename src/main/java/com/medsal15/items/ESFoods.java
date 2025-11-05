package com.medsal15.items;

import com.medsal15.mobeffects.ESMobEffects;

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
    public static final FoodProperties DIVINE_TEMPTATION = new FoodProperties.Builder().nutrition(5)
            .saturationModifier(.45F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600), 1F)
            .usingConvertsTo(Items.BOWL).build();
    public static final FoodProperties YELLOWCAKE_SLICE = new FoodProperties.Builder().nutrition(3)
            .saturationModifier(.1F)
            .effect(() -> new MobEffectInstance(MobEffects.WITHER, 20, 1), 1)
            .build();
    public static final FoodProperties COOKED_BEE_LARVA = new FoodProperties.Builder().nutrition(5)
            .saturationModifier(.3F)
            .effect(() -> new MobEffectInstance(ESMobEffects.BEE_ANGRY), 1)
            .build();
    public static final FoodProperties DESERT_JUICE = new FoodProperties.Builder().alwaysEdible()
            .usingConvertsTo(Items.GLASS_BOTTLE)
            .effect(() -> new MobEffectInstance(MobEffects.HEAL), 1)
            .build();
    public static final FoodProperties APPLE_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.5F).fast().build();
    public static final FoodProperties BLUE_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.3F).fast()
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 150), 1)
            .build();
    public static final FoodProperties COLD_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.3F).fast()
            .effect(() -> new MobEffectInstance(MobEffects.WEAKNESS, 200, 1), 1)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1), 1)
            .build();
    public static final FoodProperties RED_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.1F).fast()
            .effect(() -> new MobEffectInstance(MobEffects.HEAL, 1), 1)
            .build();
    public static final FoodProperties HOT_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.1F).fast().build();
    public static final FoodProperties REVERSE_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.1F).fast().build();
    public static final FoodProperties FUCHSIA_CAKE_SLICE = new FoodProperties.Builder().nutrition(3)
            .saturationModifier(.5F).fast()
            .effect(() -> new MobEffectInstance(MobEffects.ABSORPTION, 350, 1), 1)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 200), 1)
            .build();
    public static final FoodProperties NEGATIVE_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.3F).fast()
            .effect(() -> new MobEffectInstance(MobEffects.BLINDNESS, 300), 1)
            .effect(() -> new MobEffectInstance(MobEffects.INVISIBILITY, 250), 1)
            .build();
    public static final FoodProperties CARROT_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .effect(() -> new MobEffectInstance(MobEffects.NIGHT_VISION, 200), 1)
            .saturationModifier(.3F).fast().build();
    public static final FoodProperties CHOCOLATEY_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.5F).fast().build();
    public static final FoodProperties MOON_CAKE_SLICE = new FoodProperties.Builder().nutrition(2)
            .saturationModifier(.5F).fast().build();
    public static final FoodProperties MORTAL_TEMPTATION = new FoodProperties.Builder().nutrition(5)
            .saturationModifier(.45F)
            .effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 600), 1F)
            .effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 400), 1F)
            .usingConvertsTo(Items.BOWL).build();
    public static final FoodProperties CANDY_CRUNCH = new FoodProperties.Builder().nutrition(3).saturationModifier(.2F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 600, 3), 1F)
            .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 900, 1), 1F)
            .build();
    public static final FoodProperties HOME_DONUT = new FoodProperties.Builder().nutrition(4).saturationModifier(.2F)
            .alwaysEdible().build();
}
