package com.medsal15.items;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class OtherModItem extends Item {
    private final String modname;

    public OtherModItem(Properties properties, String modname) {
        super(properties);

        this.modname = modname;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        tooltipComponents.add(
                Component.translatable(ESLangProvider.MISSING_MOD_ITEM, modname).withStyle(ChatFormatting.DARK_RED));
    }
}
