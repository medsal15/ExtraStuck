package com.medsal15.items.melee;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.data.ESLangProvider;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.entity.MSAttributes;
import com.mraof.minestuck.entity.underling.UnderlingEntity;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.core.HolderSet.Named;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class SburbDBWeapon extends AttributeWeapon {
    public SburbDBWeapon(WeaponItem.Builder builder, Properties properties) {
        super(builder, properties, () -> {
            return List.of(
                    new ItemAttributeModifiers.Entry(MSAttributes.UNDERLING_DAMAGE_MODIFIER,
                            new AttributeModifier(ExtraStuck.modid("sburbdb_mainhand"), .1,
                                    Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.MAINHAND),
                    new ItemAttributeModifiers.Entry(MSAttributes.UNDERLING_DAMAGE_MODIFIER,
                            new AttributeModifier(ExtraStuck.modid("sburbdb_offhand"), .1,
                                    Operation.ADD_MULTIPLIED_BASE),
                            EquipmentSlotGroup.OFFHAND));
        });
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
