package com.medsal15.loot_modifiers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.world.lands.LandTypes;
import com.mraof.minestuck.world.lands.title.TitleLandType;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class ESTitleLootModifier extends ESLootModifier {
    /** Location of the table to inject */
    private final ResourceLocation inject;
    /** Location of the table to watch */
    private final ResourceLocation target;
    @Nullable
    private final TitleLandType title;

    public static final MapCodec<ESTitleLootModifier> CODEC = RecordCodecBuilder
            .mapCodec(inst -> LootModifier.codecStart(inst).and(inst.group(
                    ResourceLocation.CODEC.fieldOf("inject").forGetter(e -> e.inject),
                    ResourceLocation.CODEC.fieldOf("target").forGetter(e -> e.target),
                    LandTypes.TITLE_REGISTRY.byNameCodec().fieldOf("title").forGetter(e -> e.title)))
                    .apply(inst, ESTitleLootModifier::new));

    /**
     * @param conditions
     * @param inject     Location of the table to inject
     * @param target     Location of the table to inject into
     * @param title      Title of the land
     */
    public ESTitleLootModifier(LootItemCondition[] conditions, ResourceLocation inject, ResourceLocation target,
            TitleLandType title) {
        super(conditions);
        this.inject = inject;
        this.target = target;
        this.title = title;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(@Nonnull ObjectArrayList<ItemStack> generatedLoot,
            @Nonnull LootContext context) {
        var title = getTitle(context);
        if (title == this.title && context.getQueriedLootTableId().equals(target)) {
            generatedLoot.addAll(runTable(context.getLevel(), inject));
        }
        return generatedLoot;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }
}
