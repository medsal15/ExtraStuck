package com.medsal15.loot_modifiers;

import javax.annotation.Nonnull;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.item.BoondollarsItem;
import com.mraof.minestuck.item.MSItems;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class BoondollarLootModifier extends LootModifier {
    public static final MapCodec<BoondollarLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst -> codecStart(inst)
            .and(Codec.FLOAT.fieldOf("multiplier").forGetter(BoondollarLootModifier::getMultiplier))
            .apply(inst, BoondollarLootModifier::new));

    private final float multiplier;

    public BoondollarLootModifier(LootItemCondition[] conditions, float multiplier) {
        super(conditions);
        this.multiplier = multiplier;
    }

    public float getMultiplier() {
        return multiplier;
    }

    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @Override
    protected ObjectArrayList<ItemStack> doApply(@Nonnull ObjectArrayList<ItemStack> generatedLoot,
            @Nonnull LootContext context) {
        if (context.hasParam(LootContextParams.ORIGIN)) {
            ObjectArrayList<ItemStack> remainingLoot = new ObjectArrayList<>();
            int boondollars = 0;

            for (ItemStack stack : generatedLoot) {
                GristSet cost = GristCostRecipe.findCostForItem(stack, null, true, context.getLevel());
                if (cost == null || cost.isEmpty()) {
                    remainingLoot.add(stack);
                    continue;
                }

                boondollars += (int) Math.floor(Math.pow(cost.getValue(), 1 / 1.5) * multiplier);
            }

            if (boondollars > 0) {
                ItemStack boonstack = MSItems.BOONDOLLARS.toStack();
                boonstack = BoondollarsItem.setCount(boonstack, boondollars);
                remainingLoot.add(boonstack);
            }

            return remainingLoot;
        }
        return generatedLoot;
    }
}
