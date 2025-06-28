package com.medsal15.items.melee;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.annotation.Nonnull;

import com.medsal15.data.ESLangProvider;
import com.mraof.minestuck.item.weapon.WeaponItem;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.core.Holder.Reference;
import net.minecraft.core.HolderLookup.RegistryLookup;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.ItemEnchantments;

public class InnateEnchantsWeapon extends WeaponItem {
    private Map<ResourceKey<Enchantment>, Integer> innate;

    public InnateEnchantsWeapon(WeaponItem.Builder builder, Properties properties,
            Map<ResourceKey<Enchantment>, Integer> enchants) {
        super(builder, properties);
        this.innate = enchants;
    }

    @Override
    public int getEnchantmentLevel(@Nonnull ItemStack stack, @Nonnull Holder<Enchantment> enchantment) {
        int level = super.getEnchantmentLevel(stack, enchantment);

        if (innate.getOrDefault(enchantment, 0) > 0) {
            level += innate.get(enchantment.getKey());
        }

        return level;
    }

    @Override
    public ItemEnchantments getAllEnchantments(@Nonnull ItemStack stack, @Nonnull RegistryLookup<Enchantment> lookup) {
        ItemEnchantments list = super.getAllEnchantments(stack, lookup);
        ItemEnchantments.Mutable mutable = new ItemEnchantments.Mutable(list);

        for (ResourceKey<Enchantment> key : innate.keySet()) {
            int extra = innate.get(key);
            Optional<Reference<Enchantment>> enchant = lookup.get(key);
            if (enchant.isPresent())
                mutable.upgrade(enchant.get(), extra);
        }

        return mutable.toImmutable();
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        for (ResourceKey<Enchantment> key : innate.keySet()) {
            int extra = innate.get(key);
            MutableComponent ench = Component.translatable(getEnchantmentKey(key));
            if (extra == 1) {
                tooltipComponents.add(Component.translatable(ESLangProvider.INNATE_ENCHANT_KEY, 1, ench)
                        .withStyle(ChatFormatting.GRAY));
            } else if (extra > 1) {
                tooltipComponents.add(Component.translatable(ESLangProvider.INNATE_ENCHANTS_KEY, extra, ench)
                        .withStyle(ChatFormatting.GRAY));
            }
        }
    }

    private static String getEnchantmentKey(ResourceKey<Enchantment> enchantment) {
        ResourceLocation location = enchantment.location();
        return "enchantment." + location.getNamespace() + "." + location.getPath();
    }
}
