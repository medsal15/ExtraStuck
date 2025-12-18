package com.medsal15.compat.irons_spellbooks.items;

import javax.annotation.Nonnull;

import org.jetbrains.annotations.Nullable;

import io.redspace.ironsspellbooks.api.spells.ISpellContainer;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.registries.ComponentRegistry;
import net.minecraft.core.Holder;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import top.theillusivec4.curios.api.SlotContext;

/**
 * Assumes the other item can also handle the
 * <code>extrastuck:alt_spell_container</code> component
 */
public class DoubleSpellbook extends SpellBook {
    private final Holder<Item> other;

    public DoubleSpellbook(Holder<Item> other) {
        super();

        this.other = other;
    }

    public DoubleSpellbook(int slots, Holder<Item> other) {
        super(slots);

        this.other = other;
    }

    // Unbreaks the ability to swap items
    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(@Nonnull Level level, @Nonnull Player player,
            @Nonnull InteractionHand usedHand) {
        ItemStack held = player.getItemInHand(usedHand);

        if (player.isCrouching() && other != null && ISpellContainer.isSpellContainer(held)) {
            ItemStack swap = new ItemStack(other, held.getCount(), held.getComponentsPatch());

            @Nullable
            ISpellContainer spells = held.get(ComponentRegistry.SPELL_CONTAINER);
            @Nullable
            ISpellContainer otherSpells = held.get(ESISSComponents.ALT_SPELL_CONTAINER);

            int max = getMaxSpellSlots();
            if (swap.getItem() instanceof SpellBook swapBook) {
                max = swapBook.getMaxSpellSlots();
            }

            if (spells != null) {
                swap.set(ESISSComponents.ALT_SPELL_CONTAINER, spells);
            } else {
                swap.set(ESISSComponents.ALT_SPELL_CONTAINER, ISpellContainer.create(max, true, true));
            }
            if (otherSpells != null) {
                ISpellContainer.set(swap, otherSpells);
            } else {
                ISpellContainer.set(swap, ISpellContainer.create(max, true, true));
            }

            return InteractionResultHolder.success(swap);
        }

        return super.use(level, player, usedHand);
    }

    @Override
    public void initializeSpellContainer(ItemStack itemStack) {
        super.initializeSpellContainer(itemStack);

        if (!itemStack.has(ESISSComponents.ALT_SPELL_CONTAINER)) {
            itemStack.set(ESISSComponents.ALT_SPELL_CONTAINER, ISpellContainer.create(getMaxSpellSlots(), true, true));
        }
    }
}
