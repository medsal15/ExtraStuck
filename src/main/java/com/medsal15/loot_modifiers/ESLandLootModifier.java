package com.medsal15.loot_modifiers;

import java.util.Optional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.lands.LandTypePair;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.terrain.TerrainLandType;
import com.mraof.minestuck.world.lands.title.TitleLandType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

/**
 * Special modifier that checks both terrain and title
 *
 * Only rolls the loot table to inject if terrain is blank or matches the land's
 * terrain, and same with title
 *
 * Effectively means that an empty terrain and title will always be rolled
 */
public class ESLandLootModifier extends LootModifier {
    /** Location of the table to inject */
    private final ResourceLocation inject;
    /** Location of the table to watch */
    private final ResourceLocation target;
    private final Optional<TerrainLandType> terrain;
    private final Optional<TitleLandType> title;

    public static final MapCodec<ESLandLootModifier> CODEC = RecordCodecBuilder
            .mapCodec(inst -> LootModifier.codecStart(inst).and(inst.group(
                    ResourceLocation.CODEC.fieldOf("inject").forGetter(e -> e.inject),
                    ResourceLocation.CODEC.fieldOf("target").forGetter(e -> e.target),
                    LandTypes.TERRAIN_REGISTRY.byNameCodec().optionalFieldOf("terrain").forGetter(e -> e.terrain),
                    LandTypes.TITLE_REGISTRY.byNameCodec().optionalFieldOf("title").forGetter(e -> e.title)))
                    .apply(inst, ESLandLootModifier::new));

    /**
     * @param conditions
     * @param inject     Location of the table to inject
     * @param target     Location of the table to inject into
     * @param title      Title of the land
     * @param terrain    Terrain of the land
     */
    public ESLandLootModifier(LootItemCondition[] conditions, ResourceLocation inject, ResourceLocation target,
            Optional<TerrainLandType> terrain, Optional<TitleLandType> title) {
        super(conditions);
        this.inject = inject;
        this.target = target;
        this.title = title;
        this.terrain = terrain;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(@Nonnull ObjectArrayList<ItemStack> generatedLoot,
            @Nonnull LootContext context) {
        var title = getTitle(context);
        var terrain = getTerrain(context);
        var title_correct = this.title.isEmpty() || this.title.get() == title;
        var terrain_correct = this.terrain.isEmpty() || this.terrain.get() == terrain;
        if (title_correct && terrain_correct && context.getQueriedLootTableId().equals(target)) {
            generatedLoot.addAll(runTable(context.getLevel(), inject));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Nullable
    public static TitleLandType getTitle(@Nonnull LootContext context) {
        LandTypePair aspects = LandTypePair.getTypes(context.getLevel()).orElse(null);
        if (aspects != null)
            return aspects.getTitle();
        return null;
    }

    @Nullable
    public static TerrainLandType getTerrain(@Nonnull LootContext context) {
        LandTypePair aspects = LandTypePair.getTypes(context.getLevel()).orElse(null);
        if (aspects != null)
            return aspects.getTerrain();
        return null;
    }

    public static ObjectArrayList<ItemStack> runTable(ServerLevel level, ResourceLocation loot_table) {
        var key = ResourceKey.create(Registries.LOOT_TABLE, loot_table);
        var table = level.getServer().reloadableRegistries().getLootTable(key);
        var builder = new LootParams.Builder(level);
        var params = builder.create(LootContextParamSet.builder().build());
        var rewards = table.getRandomItems(params);
        return rewards;
    }
}
