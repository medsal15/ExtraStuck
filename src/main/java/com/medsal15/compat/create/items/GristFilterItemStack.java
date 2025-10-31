package com.medsal15.compat.create.items;

import java.util.List;

import com.medsal15.items.ESDataComponents;
import com.medsal15.items.components.GristFilterEntry;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.simibubi.create.content.logistics.filter.FilterItemStack;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;

public class GristFilterItemStack extends FilterItemStack {
    public List<GristFilterEntry> entries;

    public GristFilterItemStack(ItemStack filter) {
        super(filter);

        entries = filter.getOrDefault(ESDataComponents.GRIST_FILTER_DATA, List.of());
    }

    @Override
    public boolean test(Level world, FluidStack stack, boolean matchNBT) {
        return false;
    }

    @Override
    public boolean test(Level world, ItemStack stack, boolean matchNBT) {
        GristSet cost = GristCostRecipe.findCostForItem(stack, null, true, world);
        // No cost, no pass
        if (cost == null)
            return false;

        for (GristFilterEntry entry : entries) {
            int amount = 0;
            if (cost.hasType(entry.grist())) {
                amount = (int) cost.getGrist(entry.grist());
            }
            if (!entry.test(amount))
                return false;
        }

        return true;
    }
}
