package com.medsal15.items;

import com.medsal15.utils.ESTags;
import com.mraof.minestuck.item.weapon.MSToolType;

import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;

public final class ESItemTypes {
    public static final MSToolType DICE_TOOL = new MSToolType(ESTags.Blocks.MINEABLE_WITH_DICE, new ItemAbility[0]);
    public static final MSToolType HOE_TOOL = new MSToolType(BlockTags.MINEABLE_WITH_HOE, ItemAbilities.HOE_DIG,
            ItemAbilities.HOE_TILL);

    /**
     * Converts drops into boondollars
     */
    public static final ItemAbility BOONDOLLAR_MINING = ItemAbility.get("boondollar_mining");
}
