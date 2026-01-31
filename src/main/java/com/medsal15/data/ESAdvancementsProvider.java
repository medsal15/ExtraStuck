package com.medsal15.data;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.items.ESItems;
import com.mraof.minestuck.Minestuck;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementRequirements;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.neoforged.neoforge.common.data.AdvancementProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ESAdvancementsProvider implements AdvancementProvider.AdvancementGenerator {
    public static final String EXTRA_MODI = "extrastuck.extra_modi";

    public static DataProvider create(PackOutput output, CompletableFuture<HolderLookup.Provider> provider,
            ExistingFileHelper fileHelper) {
        return new AdvancementProvider(output, provider, fileHelper, List.of(new ESAdvancementsProvider()));
    }

    @Override
    public void generate(@Nonnull Provider registries, @Nonnull Consumer<AdvancementHolder> saver,
            @Nonnull ExistingFileHelper existingFileHelper) {
        @SuppressWarnings({ "unused", "removal" })
        AdvancementHolder extraModi = Advancement.Builder.advancement().parent(Minestuck.id("minestuck/all_modi"))
                .display(ESItems.MASTERMIND_MODUS_CARD, Component.translatable(title(EXTRA_MODI)),
                        Component.translatable(desc(EXTRA_MODI)), null, AdvancementType.GOAL,
                        true, true, false)
                .addCriterion("pile",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.PILE_MODUS_CARD.asItem()))
                .addCriterion("fortune",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.FORTUNE_MODUS_CARD.asItem()))
                .addCriterion("ore",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.ORE_MODUS_CARD.asItem()))
                .addCriterion("archeology",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.ARCHEOLOGY_MODUS_CARD.asItem()))
                .addCriterion("void",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.VOID_MODUS_CARD.asItem()))
                .addCriterion("ender",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.ENDER_MODUS_CARD.asItem()))
                .addCriterion("mastermind",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.MASTERMIND_MODUS_CARD.asItem()))
                .addCriterion("furnace",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.FURNACE_MODUS_CARD.asItem()))
                .addCriterion("compact",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.COMPACT_MODUS_CARD.asItem()))
                .addCriterion("crafting",
                        InventoryChangeTrigger.TriggerInstance.hasItems(ESItems.CRAFTING_MODUS_CARD.asItem()))
                .requirements(AdvancementRequirements.Strategy.AND)
                .save(saver, ExtraStuck.modid(EXTRA_MODI).toString());
    }

    public static String title(String name) {
        return "advancements." + name + ".title";
    }

    public static String desc(String name) {
        return "advancements." + name + ".description";
    }
}
