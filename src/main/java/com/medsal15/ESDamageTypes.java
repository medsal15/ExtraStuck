package com.medsal15;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageType;

public final class ESDamageTypes {
    public static ResourceKey<DamageType> THORN_SHIELD = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID, "thorn_shield"));
    public static ResourceKey<DamageType> CAPTAIN_JUSTICE_PROJECTILE = ResourceKey.create(Registries.DAMAGE_TYPE,
            ResourceLocation.fromNamespaceAndPath(ExtraStuck.MODID, "captain_justice_projectile"));
}
