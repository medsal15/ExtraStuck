package com.medsal15.items.melee;

import static com.medsal15.data.ESLangProvider.ALT_GUN_EMPTY_KEY;
import static com.medsal15.data.ESLangProvider.ALT_GUN_HEAVY_KEY;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.EquipmentSlotGroup;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.attributes.AttributeModifier.Operation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.ItemAttributeModifierEvent;

public class AltGunWeapon extends WeaponItem {
    public AltGunWeapon(WeaponItem.Builder builder, Properties properties) {
        super(builder, properties);
        NeoForge.EVENT_BUS.addListener(this::adjustDamge);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        if (contents == null || contents.equals(ItemContainerContents.EMPTY)) {
            tooltipComponents.add(Component.translatable(ALT_GUN_EMPTY_KEY).withStyle(ChatFormatting.GRAY));
        } else {
            tooltipComponents.add(Component.translatable(ALT_GUN_HEAVY_KEY).withStyle(ChatFormatting.GRAY));
        }
    }

    private void adjustDamge(ItemAttributeModifierEvent event) {
        ItemStack stack = event.getItemStack();
        if (!(stack.getItem() instanceof AltGunWeapon))
            return;

        ItemContainerContents contents = stack.get(DataComponents.CONTAINER);
        if (contents != null && !contents.equals(ItemContainerContents.EMPTY)) {
            event.addModifier(Attributes.ATTACK_DAMAGE,
                    new AttributeModifier(ExtraStuck.modid("alt_gun"), 1, Operation.ADD_VALUE),
                    EquipmentSlotGroup.MAINHAND);
        }
    }
}
