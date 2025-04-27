package com.medsal15.loot_modifiers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mraof.minestuck.world.lands.LandTypePair;
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
import net.neoforged.neoforge.common.loot.LootModifier;

public abstract class ESLootModifier extends LootModifier {
    public ESLootModifier(LootItemCondition[] conditions) {
        super(conditions);
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
