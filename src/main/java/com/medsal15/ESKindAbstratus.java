package com.medsal15;

import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.player.KindAbstratusList;
import com.mraof.minestuck.player.KindAbstratusType;

import net.minecraft.world.item.ShieldItem;

public final class ESKindAbstratus {
    public static final String DICE = "extrastuck.dice";
    public static final String SHIELD = "extrastuck.shield";

    public static void registerTypes() {
        KindAbstratusList.registerType(new KindAbstratusType(DICE).addItemId(MSItems.FLUORITE_OCTET.get()));
        KindAbstratusList.registerType(new KindAbstratusType(SHIELD).addItemClass(ShieldItem.class));
    }
}
