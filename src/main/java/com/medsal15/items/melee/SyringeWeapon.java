package com.medsal15.items.melee;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.medsal15.items.components.ESDataComponents;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.SlotAccess;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickAction;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionContents;

public class SyringeWeapon extends WeaponItem {
    public SyringeWeapon(Builder builder, Properties properties) {
        super(builder, properties);
    }

    @Override
    public boolean overrideOtherStackedOnMe(@Nonnull ItemStack stack, @Nonnull ItemStack other, @Nonnull Slot slot,
            @Nonnull ClickAction action, @Nonnull Player player, @Nonnull SlotAccess access) {
        if (action == ClickAction.SECONDARY && other.is(Items.POTION)) {
            PotionContents otherContents = other.get(DataComponents.POTION_CONTENTS);
            PotionContents selfContents = stack.get(DataComponents.POTION_CONTENTS);
            if (otherContents != null && selfContents == null) {
                stack.set(DataComponents.POTION_CONTENTS, otherContents);
                stack.set(ESDataComponents.ENERGY, 3);
                other.consume(1, player);
                player.playSound(SoundEvents.BREWING_STAND_BREW);
                return true;
            }
        }
        return super.overrideOtherStackedOnMe(stack, other, slot, action, player, access);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        PotionContents contents = stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY);
        int doses = stack.getOrDefault(ESDataComponents.ENERGY, 0);
        if (contents != PotionContents.EMPTY && doses > 0) {
            tooltipComponents.add(Component.empty());
            tooltipComponents
                    .add(Component.translatable(ESLangProvider.SYRINGE_CONTENTS, doses).withStyle(ChatFormatting.GRAY));
            contents.addPotionTooltip(tooltipComponents::add, 1f, context.tickRate());
        }
    }
}
