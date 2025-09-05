package com.medsal15.subevents;

import java.text.NumberFormat;

import com.medsal15.ClientConfig;
import com.medsal15.ExtraStuck;
import com.medsal15.data.ESLangProvider;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.ESShield.BlockFuncs;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.energy.IEnergyStorage;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;

@EventBusSubscriber(modid = ExtraStuck.MODID, bus = EventBusSubscriber.Bus.GAME, value = Dist.CLIENT)
public final class ClientGameEvents {
    @SubscribeEvent
    public static void addCustomTooltip(ItemTooltipEvent event) {
        int i = 1;
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();

        // Only for this mod
        final ResourceLocation itemId = BuiltInRegistries.ITEM.getKey(stack.getItem());
        if (itemId == null || !itemId.getNamespace().equals(ExtraStuck.MODID))
            return;

        // Fancy item descriptions
        String tooltip_key = stack.getDescriptionId() + ".tooltip";
        if (I18n.exists(tooltip_key)) {
            event.getToolTip().add(i,
                    Component.translatable(tooltip_key).withStyle(ChatFormatting.GRAY));
            i++;
        }

        // Shield info
        if (ClientConfig.displayShieldInfo) {
            if (item instanceof ESShield shield && shield.hasOnBlock(BlockFuncs.DAMAGE)) {
                event.getToolTip().add(i,
                        Component.translatable(ESLangProvider.SHIELD_DAMAGE_KEY,
                                stack.get(ESDataComponents.SHIELD_DAMAGE)
                                        .intValue())
                                .withStyle(ChatFormatting.GRAY));
                i++;
            }
        }

        // RF
        @SuppressWarnings("null")
        IEnergyStorage energyStorage = Capabilities.EnergyStorage.ITEM.getCapability(stack, null);
        if (energyStorage != null) {
            event.getToolTip().add(i, Component.translatable(ESLangProvider.ENERGY_STORAGE_KEY,
                    NumberFormat.getInstance().format(energyStorage.getEnergyStored()),
                    NumberFormat.getInstance().format(energyStorage.getMaxEnergyStored())));
            i++;
        }
    }
}
