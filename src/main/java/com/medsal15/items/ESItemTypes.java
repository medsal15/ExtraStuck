package com.medsal15.items;

import com.medsal15.utils.ESTags;
import com.mraof.minestuck.item.weapon.MSToolType;

import net.neoforged.neoforge.common.ItemAbility;

public final class ESItemTypes {
    public static final MSToolType DICE_TOOL = new MSToolType(ESTags.Blocks.MINEABLE_WITH_DICE, new ItemAbility[0]);

    /**
     * Converts drops into boondollars
     */
    public static final ItemAbility BOONDOLLAR_MINING = ItemAbility.get("boondollar_mining");
}
