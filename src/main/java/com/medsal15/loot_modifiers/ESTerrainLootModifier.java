package com.medsal15.loot_modifiers;

import javax.annotation.Nonnull;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ESTerrainLootModifier extends ESLootModifier {
    /** Location of the table to inject */
    private final ResourceLocation inject;
    /** Location of the table to watch */
    private final ResourceLocation target;
    private final TerrainLandType terrain;

    public static final MapCodec<ESTerrainLootModifier> CODEC = RecordCodecBuilder
            .mapCodec(inst -> LootModifier.codecStart(inst).and(inst.group(
                    ResourceLocation.CODEC.fieldOf("inject").forGetter(e -> e.inject),
                    ResourceLocation.CODEC.fieldOf("target").forGetter(e -> e.target),
                    LandTypes.TERRAIN_REGISTRY.byNameCodec().fieldOf("terrain").forGetter(e -> e.terrain)))
                    .apply(inst, ESTerrainLootModifier::new));

    /**
     * @param conditions
     * @param inject     Location of the table to inject
     * @param target     Location of the table to inject into
     * @param terrain    Title of the land
     */
    public ESTerrainLootModifier(LootItemCondition[] conditions, ResourceLocation inject, ResourceLocation target,
            TerrainLandType terrain) {
        super(conditions);
        this.inject = inject;
        this.target = target;
        this.terrain = terrain;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(@Nonnull ObjectArrayList<ItemStack> generatedLoot,
            @Nonnull LootContext context) {
        var terrain = getTerrain(context);

        if (terrain == this.terrain && context.getQueriedLootTableId().equals(target)) {
            generatedLoot.addAll(runTable(context.getLevel(), inject));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
