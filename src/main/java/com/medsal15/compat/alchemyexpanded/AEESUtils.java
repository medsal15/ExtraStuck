package com.medsal15.compat.alchemyexpanded;

import static com.medsal15.subevents.ClientEvents.registerBiConvertion;

import com.medsal15.compat.alchemyexpanded.items.AEESItems;
import com.medsal15.config.ConfigClient;
import com.rosebushes.alchemyexpanded.item.AEItems;

public class AEESUtils {
    public static void registerConvertions() {
        registerBiConvertion(AEESItems.HANDGUN, AEESItems.OFFICE_KEY);

        if (ConfigClient.addAEConvertionRecipes) {
            registerBiConvertion(AEItems.RED_DRAGON_LIPSTICK, AEItems.DRAGON_SAW);
            registerBiConvertion(AEItems.KEY, AEItems.REVOLVER);
            registerBiConvertion(AEItems.DEULING_BLADE, AEItems.DAPPLE_DEULY);
            registerBiConvertion(AEItems.FRONTIER_AXE, AEItems.ANNIES_GOT_A_GUN);
            registerBiConvertion(AEItems.ASHEN_CHAINSAW, AEItems.BOOMSTICK);
            registerBiConvertion(AEItems.BLACK_ASSAULT_RIFLE, AEItems.BLACK_SCEPTER);
            registerBiConvertion(AEItems.GOLD_ASSAULT_RIFLE, AEItems.GOLD_SCEPTER);
            registerBiConvertion(AEItems.GREEN_SUN_CUESTAFF, AEItems.ENGLISH_EXECUTIONER);
            registerBiConvertion(AEItems.WHITE_WAND, AEItems.WHITE_MAGNUM);
        }
    }
}
