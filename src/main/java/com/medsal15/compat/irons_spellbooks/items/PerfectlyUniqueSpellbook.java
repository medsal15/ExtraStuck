package com.medsal15.compat.irons_spellbooks.items;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.api.registry.SpellDataRegistryHolder;
import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.compat.Curios;
import io.redspace.ironsspellbooks.item.UniqueSpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

public class PerfectlyUniqueSpellbook extends UniqueSpellBook {
    public PerfectlyUniqueSpellbook() {
        super(new SpellDataRegistryHolder[] {
                new SpellDataRegistryHolder(SpellRegistry.WOLOLO_SPELL, 1),
        }, 0);
        withSpellbookAttributes(
                new AttributeContainer(AttributeRegistry.MAX_MANA, 200, AttributeModifier.Operation.ADD_VALUE),
                new AttributeContainer(AttributeRegistry.FIRE_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.ICE_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.LIGHTNING_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.HOLY_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.ENDER_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.BLOOD_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.EVOCATION_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.NATURE_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE),
                new AttributeContainer(AttributeRegistry.ELDRITCH_SPELL_POWER, .1,
                        AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
    }

    @Override
    public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext,
            ResourceLocation id, ItemStack stack) {
        Multimap<Holder<Attribute>, AttributeModifier> modifiers = super.getAttributeModifiers(slotContext, id, stack);
        ArrayListMultimap<Holder<Attribute>, AttributeModifier> map = ArrayListMultimap.create(modifiers);

        ISpellContainer container = ISpellContainer.get(stack);
        double emptySlots = container.getMaxSpellCount() - container.getActiveSpellCount();
        double bonusSpellPower = emptySlots / 10;
        AttributeContainer attribute = new AttributeContainer(AttributeRegistry.SPELL_POWER, bonusSpellPower,
                Operation.ADD_MULTIPLIED_BASE);

        map.put(attribute.attribute(), attribute.createModifier(Curios.SPELLBOOK_SLOT + "_pus"));

        return map;
    }
}
