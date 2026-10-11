package com.medsal15.compat.patchouli;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.patchouli.pages.CombinationPage;
import com.medsal15.compat.patchouli.pages.GristCostPage;

import vazkii.patchouli.client.book.ClientBookRegistry;

public class ESBookRegistry {
    public static void register() {
        addPagesTypes();
    }

    private static void addPagesTypes() {
        ClientBookRegistry.INSTANCE.pageTypes.put(ExtraStuck.modid("combination"), CombinationPage.class);
        ClientBookRegistry.INSTANCE.pageTypes.put(ExtraStuck.modid("grist_cost"), GristCostPage.class);
    }
}
