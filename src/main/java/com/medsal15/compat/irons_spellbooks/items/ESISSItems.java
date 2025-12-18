package com.medsal15.compat.irons_spellbooks.items;

import java.util.ArrayList;
import java.util.Collection;

import com.medsal15.ExtraStuck;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public final class ESISSItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ExtraStuck.MODID);

    public static final DeferredItem<Item> GRIMOIRE = ITEMS.register("grimoire", () -> new SpellBook(8)
            .withSpellbookAttributes(new AttributeContainer(AttributeRegistry.MAX_MANA, 266, Operation.ADD_VALUE),
                    new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, .1666,
                            Operation.ADD_MULTIPLIED_BASE)));
    public static final DeferredItem<Item> GEMINI_SPELLBOOK_RED = ITEMS.register("gemini_spellbook_red",
            () -> new DoubleSpellbook(6, ESISSItems.GEMINI_SPELLBOOK_BLUE)
                    .withSpellbookAttributes(
                            new AttributeContainer(AttributeRegistry.MAX_MANA, 300, Operation.ADD_VALUE)));
    public static final DeferredItem<Item> GEMINI_SPELLBOOK_BLUE = ITEMS.register("gemini_spellbook_blue",
            () -> new DoubleSpellbook(6, ESISSItems.GEMINI_SPELLBOOK_RED)
                    .withSpellbookAttributes(
                            new AttributeContainer(AttributeRegistry.MAX_MANA, 300, Operation.ADD_VALUE)));
    // TODO mage guy

    public static Collection<DeferredItem<Item>> getSpellbooks() {
        ArrayList<DeferredItem<Item>> list = new ArrayList<>();

        list.add(GRIMOIRE);
        list.add(GEMINI_SPELLBOOK_BLUE);
        list.add(GEMINI_SPELLBOOK_RED);

        return list;
    }
}
