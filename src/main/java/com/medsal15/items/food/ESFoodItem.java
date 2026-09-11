package com.medsal15.items.food;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.config.ConfigClient;
import com.mojang.datafixers.util.Pair;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.FoodProperties.PossibleEffect;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.ItemAttributeModifiers;

public class ESFoodItem extends Item {
    public ESFoodItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(@Nonnull ItemStack stack, @Nonnull TooltipContext context,
            @Nonnull List<Component> tooltipComponents, @Nonnull TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);

        if (ConfigClient.displayFoodEffects) {
            // Mostly a copy of Farmer's Delight's TextUtils::addFoodEffectTooltip
            @SuppressWarnings("null")
            FoodProperties food = stack.getFoodProperties(null);
            if (food == null)
                return;

            List<PossibleEffect> effects = food.effects();
            List<Pair<Holder<Attribute>, AttributeModifier>> attributes = new ArrayList<>();
            MutableComponent text;

            if (!effects.isEmpty()) {
                for (PossibleEffect possibleEffect : effects) {
                    MobEffectInstance instance = possibleEffect.effect();
                    float probability = possibleEffect.probability();
                    text = Component.translatable(instance.getDescriptionId());
                    MobEffect effect = instance.getEffect().value();
                    effect.createModifiers(instance.getAmplifier(),
                            (holder, modifier) -> attributes.add(new Pair<>(holder, modifier)));

                    if (instance.getAmplifier() > 0)
                        text = Component.translatable("potion.withAmplifier", text,
                                Component.translatable("potion.potency." + instance.getAmplifier()));

                    if (instance.getDuration() > 20)
                        text = Component.translatable("potion.withDuration", text,
                                MobEffectUtil.formatDuration(instance, 1, context.tickRate()));

                    if (probability < 1 && probability > 0)
                        text = Component.translatable("potion.withDuration", text,
                                NumberFormat.getInstance().format(probability * 100) + "%");

                    tooltipComponents.add(text.withStyle(ChatFormatting.BLUE));
                }
            }

            if (!attributes.isEmpty()) {
                tooltipComponents.add(CommonComponents.EMPTY);
                tooltipComponents.add(Component.translatable("potion.whenDrank").withStyle(ChatFormatting.DARK_PURPLE));

                for (Pair<Holder<Attribute>, AttributeModifier> pair : attributes) {
                    Holder<Attribute> attribute = pair.getFirst();
                    AttributeModifier modifier = pair.getSecond();
                    double amount = modifier.amount();
                    double formattedAmount;
                    if (modifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_BASE
                            && modifier.operation() != AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL) {
                        formattedAmount = amount;
                    } else {
                        formattedAmount = amount * 100;
                    }

                    ChatFormatting color = null;
                    String type = null;
                    if (amount > 0.0) {
                        type = "plus";
                        color = ChatFormatting.BLUE;
                    } else if (amount < 0.0) {
                        formattedAmount *= -1.0;
                        type = "take";
                        color = ChatFormatting.RED;
                    }

                    if (type != null && color != null) {
                        tooltipComponents.add(
                                Component.translatable("attribute.modifier." + type + "." + modifier.operation().id(),
                                        ItemAttributeModifiers.ATTRIBUTE_MODIFIER_FORMAT.format(formattedAmount),
                                        Component.translatable(attribute.value().getDescriptionId())).withStyle(color));
                    }
                }
            }
        }
    }
}
