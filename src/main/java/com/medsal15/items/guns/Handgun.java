package com.medsal15.items.guns;

import static com.medsal15.data.ESItemTags.AMMO_HANDGUN;

public class Handgun extends ESGun {
    public Handgun(Properties properties) {
        super(properties, AMMO_HANDGUN);
    }

    @Override
    public int getMaxBullets() {
        return 6;
    }

    @Override
    public float getZoom() {
        return .8F;
    }
}
