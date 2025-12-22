package com.medsal15.compat.irons_spellbooks.items;

import java.util.Optional;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.entity.MSAttributes;
import com.mraof.minestuck.entity.underling.UnderlingEntity;

import io.redspace.ironsspellbooks.api.registry.AttributeRegistry;
import io.redspace.ironsspellbooks.item.SpellBook;
import io.redspace.ironsspellbooks.item.weapons.AttributeContainer;
import net.minecraft.core.HolderSet.Named;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

/**
 * Spellbook equivalent of SburbDBWeapon
 */
public class SburbDBSpellbook extends SpellBook {
    public SburbDBSpellbook(int slots) {
        super(slots);
        withSpellbookAttributes(
                new AttributeContainer(AttributeRegistry.MAX_MANA, 80, Operation.ADD_VALUE),
                new AttributeContainer(MSAttributes.UNDERLING_DAMAGE_MODIFIER, .1,
                        Operation.ADD_MULTIPLIED_TOTAL));
    }

    // Unbreaks the ability to use on underlings
    @Override
    public boolean canEquipFromUse(SlotContext slotContext, ItemStack stack) {
        return false;
    }

    @Override
    public InteractionResult interactLivingEntity(@Nonnull ItemStack stack, @Nonnull Player player,
            @Nonnull LivingEntity target, @Nonnull InteractionHand usedHand) {
        if (target instanceof UnderlingEntity underling) {
            GristType primary = underling.getGristType();
            Optional<Named<GristType>> osecondaries = primary.getSecondaryTypes();

            if (osecondaries.isPresent() && osecondaries.get().size() > 0) {
                if (player.level().isClientSide) {
                    MutableComponent secondaries = osecondaries.get().stream()
                            .map(type -> Component.translatable(type.value().getTranslationKey()))
                            .reduce((a, b) -> a.append(", ").append(b)).get();

                    player.sendSystemMessage(
                            Component.translatable(ESLangProvider.SBURBDB_SECONDARIES_KEY, secondaries));
                }

                return InteractionResult.SUCCESS;
            }
        }

        return super.interactLivingEntity(stack, player, target, usedHand);
    }
}
