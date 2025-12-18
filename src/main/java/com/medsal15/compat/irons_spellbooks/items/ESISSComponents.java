package com.medsal15.compat.irons_spellbooks.items;

import java.util.function.Supplier;

import com.medsal15.ExtraStuck;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.capabilities.magic.SpellContainer;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESISSComponents {
    public static final DeferredRegister.DataComponents DATA_COMPONENTS = DeferredRegister
            .createDataComponents(Registries.DATA_COMPONENT_TYPE, ExtraStuck.MODID);

    public static final Supplier<DataComponentType<ISpellContainer>> ALT_SPELL_CONTAINER = DATA_COMPONENTS
            .registerComponentType("alt_spell_container", builder -> builder.persistent(SpellContainer.CODEC)
                    .networkSynchronized(SpellContainer.STREAM_CODEC).cacheEncoding());
}
