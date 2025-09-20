package com.medsal15.data;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.data.BoondollarPriceProvider;

import net.minecraft.data.PackOutput;

public class ESPriceProvider extends BoondollarPriceProvider {
    public ESPriceProvider(PackOutput output) {
        super(output, ExtraStuck.MODID);
    }

    @Override
    protected void registerPrices() {
        add(ESItems.SALESMAN_GOGGLES, 1997);
    }
}
