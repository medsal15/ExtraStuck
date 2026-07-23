package com.medsal15.data;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.data.BoondollarPriceProvider;

import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Items;

public class ESPriceProvider extends BoondollarPriceProvider {
    public ESPriceProvider(PackOutput output) {
        super(output, ExtraStuck.MODID);
    }

    @Override
    protected void registerPrices() {
        add(ESItems.SALESMAN_GOGGLES, 1997);
        add(ESItems.SALESWOMAN_GLASSES, 10_000);
        add(Items.AMETHYST_SHARD, 75, 150);
        add(Items.DISC_FRAGMENT_5, 250, 500);
        add(Items.SOUL_LANTERN, 25, 50);
        add(Items.DARK_OAK_LOG, 20, 32);
        add(Items.GLOW_BERRIES, 5, 8);
    }
}
