package com.medsal15.compat.alchemyexpanded;

import static com.medsal15.subevents.ClientEvents.registerBiConvertion;

import com.medsal15.compat.alchemyexpanded.items.AEESMissingItems;
import com.medsal15.items.ESItems;

public class AEESMUtils {
    public static void registerConvertions() {
        registerBiConvertion(AEESMissingItems.HANDGUN, ESItems.OFFICE_KEY);
    }
}
