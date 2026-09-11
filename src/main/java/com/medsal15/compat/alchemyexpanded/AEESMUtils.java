package com.medsal15.compat.alchemyexpanded;

import com.medsal15.compat.alchemyexpanded.items.AEESMissingItems;
import com.medsal15.subevents.ClientEvents;

public class AEESMUtils {
    public static void registerConvertions() {
        ClientEvents.registerBiConvertion(AEESMissingItems.HANDGUN, AEESMissingItems.OFFICE_KEY);
    }
}
