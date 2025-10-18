package com.medsal15.subevents;

import java.text.NumberFormat;

import javax.annotation.Nullable;

import com.medsal15.ExtraStuck;
import com.medsal15.config.ConfigClient;
import com.medsal15.data.ESLangProvider;
import com.medsal15.items.ESDataComponents;
import com.medsal15.items.shields.ESShield;
import com.medsal15.items.shields.ESShield.BlockFuncs;
import com.medsal15.utils.ESTags;
import com.mraof.minestuck.entity.consort.BoondollarPrices;

import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
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
    private static RandomSource random = RandomSource.create();
    private static int lastValue = 0;
    @Nullable
    private static Item lastItem = null;

    @SubscribeEvent
    public static void addCustomTooltip(final ItemTooltipEvent event) {
        int i = 1;
        ItemStack stack = event.getItemStack();
        Item item = stack.getItem();
        Player player = event.getEntity();

        if (player != null && !stack.isEmpty()) {
            boolean show_value = false;
            for (ItemStack armor : player.getInventory().armor) {
                if (armor.is(ESTags.Items.SHOW_VALUE)) {
                    show_value = true;
                    break;
                }
            }

            if (show_value) {
                if (item != lastItem) {
                    lastItem = item;
                    lastValue = BoondollarPrices.getInstance().findPrice(stack, random).orElse(0);
                }
                if (lastValue != 0) {
                    event.getToolTip().add(i, Component.translatable(ESLangProvider.BOONDOLLAR_VALUE_KEY,
                            NumberFormat.getInstance().format(lastValue)));
                    i++;
                }
            }
        }

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
        if (ConfigClient.displayShieldInfo) {
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
