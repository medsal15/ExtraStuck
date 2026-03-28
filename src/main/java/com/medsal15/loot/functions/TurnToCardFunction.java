package com.medsal15.loot.functions;

import java.util.List;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.item.CaptchaCardItem;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.functions.LootItemConditionalFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;

public class TurnToCardFunction extends LootItemConditionalFunction {
    public static final MapCodec<TurnToCardFunction> CODEC = RecordCodecBuilder.mapCodec(inst -> commonFields(inst)
            .and(Codec.BOOL.optionalFieldOf("punched", false).forGetter(obj -> obj.punched))
            .apply(inst, TurnToCardFunction::new));

    private final boolean punched;

    public TurnToCardFunction(List<LootItemCondition> conditions, boolean punched) {
        super(conditions);

        this.punched = punched;
    }

    @Override
    public LootItemFunctionType<? extends LootItemConditionalFunction> getType() {
        return ESLootFunctions.TURN_TO_CARD.get();
    }

    @Override
    protected ItemStack run(@Nonnull ItemStack stack, @Nonnull LootContext context) {
        ExtraStuck.LOGGER.info("turning {} into a card", stack);
        if (punched) {
            return CaptchaCardItem.createPunchedCard(stack.getItem());
        } else {
            return CaptchaCardItem.createCardWithItem(stack, context.getLevel().getServer());
        }
    }

    public static LootItemConditionalFunction.Builder<?> builder(boolean punched) {
        return simpleBuilder(conditions -> new TurnToCardFunction(conditions, punched));
    }
}
