package com.medsal15.compat.create.items;

import java.util.List;
import java.util.function.Supplier;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.create.items.GristFilterItem.GristFilterEntry;

import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESCreateComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
            .createDataComponents(Registries.DATA_COMPONENT_TYPE, ExtraStuck.MODID);

    public static final Supplier<DataComponentType<List<GristFilterEntry>>> GRIST_FILTER_DATA = DATA_COMPONENTS
            .registerComponentType("grist_filter", builder -> builder.persistent(GristFilterEntry.LIST_CODEC)
                    .networkSynchronized(GristFilterEntry.LIST_STREAM_CODEC));
}
