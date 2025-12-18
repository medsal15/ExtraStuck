package com.medsal15.compat;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.config.ConfigClient;
import com.medsal15.data.ESLangProvider;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

public class MissingModItem extends Item {
    private final String modname;
    private final String modid;

    public MissingModItem(Properties properties, String modname, String modid) {
        super(properties);

        this.modname = modname;
        this.modid = modid;
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (ConfigClient.addMissingModTooltip) {
            if (tooltipFlag.isAdvanced()) {
                tooltipComponents.add(Component.translatable(ESLangProvider.MISSING_MOD_KEY_ADVANCED, modname, modid)
                        .withStyle(ChatFormatting.DARK_RED));
            } else {
                tooltipComponents.add(
                        Component.translatable(ESLangProvider.MISSING_MOD_KEY, modname)
                                .withStyle(ChatFormatting.DARK_RED));
            }
        }
    }
}
