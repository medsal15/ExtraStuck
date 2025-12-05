package com.medsal15.items.melee;

import java.util.List;
import java.util.function.Supplier;

import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

public class AttributeWeapon extends WeaponItem {
    private final List<ItemAttributeModifiers.Entry> modifiers;

    public AttributeWeapon(Builder builder, Properties properties,
            Supplier<List<ItemAttributeModifiers.Entry>> modifiers) {
        super(builder, properties);
        this.modifiers = modifiers.get();
        NeoForge.EVENT_BUS.addListener(this::adjustAttributes);
    }

    protected void adjustAttributes(final ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        // Ensure this only applies to stacks of this instance
        if (stack.getItem() == this) {
            modifiers.forEach(entry -> {
                event.addModifier(entry.attribute(), entry.modifier(), entry.slot());
            });
        }
    }
}
