package com.medsal15.structures.processors;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.entity.MSEntityTypes;
import com.mraof.minestuck.entity.consort.ConsortEntity;
import com.mraof.minestuck.entity.consort.EnumConsort.MerchantType;
import com.mraof.minestuck.world.lands.LandTypePair;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureEntityInfo;

public final class PlaceConsortProcessor extends StructureProcessor {
    public static final MapCodec<PlaceConsortProcessor> CODEC = RecordCodecBuilder
            .mapCodec(inst -> inst
                    .group(MerchantType.CODEC.optionalFieldOf("merchant").forGetter(e -> e.merchant))
                    .apply(inst, PlaceConsortProcessor::new));

    private Optional<MerchantType> merchant;

    public PlaceConsortProcessor(Optional<MerchantType> merchant) {
        this.merchant = merchant;
    }

    @Override
    public StructureEntityInfo processEntity(@Nonnull LevelReader world, @Nonnull BlockPos seedPos,
            @Nonnull StructureEntityInfo rawEntityInfo, @Nonnull StructureEntityInfo entityInfo,
            @Nonnull StructurePlaceSettings placementSettings, @Nonnull StructureTemplate template) {
        var id = rawEntityInfo.nbt.getString("id");
        if (world instanceof ServerLevelAccessor && "minecraft:armor_stand".equals(id)) {
            var accessor = (ServerLevelAccessor) world;
            var oPair = LandTypePair.getTypes(accessor.getLevel());
            EntityType<? extends ConsortEntity> type;
            if (oPair.isPresent()) {
                var pair = oPair.get();
                type = pair.getTerrain().getConsortType();
            } else {
                type = MSEntityTypes.SALAMANDER.get();
            }

            ConsortEntity consort = type.create(accessor.getLevel());
            if (consort != null) {
                // TODO they run fast for some reason
                consort.setPos(entityInfo.pos);
                if (merchant.isPresent()) {
                    consort.merchantType = merchant.get();
                    consort.restrictTo(entityInfo.blockPos, 0);
                } else {
                    consort.merchantType = MerchantType.NONE;
                }
                consort.finalizeSpawn(accessor, accessor.getCurrentDifficultyAt(seedPos), MobSpawnType.STRUCTURE, null);

                var nbt = entityInfo.nbt;
                if (consort.save(nbt))
                    return new StructureEntityInfo(entityInfo.pos, entityInfo.blockPos, nbt);
            }
        }

        return super.processEntity(world, seedPos, rawEntityInfo, entityInfo, placementSettings, template);
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return ESProcessors.PLACE_CONSORT.get();
    }
}
