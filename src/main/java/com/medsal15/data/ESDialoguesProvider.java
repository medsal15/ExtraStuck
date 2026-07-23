package com.medsal15.data;

import static com.mraof.minestuck.data.dialogue.SelectableDialogueProvider.defaultWeight;
import static com.mraof.minestuck.entity.dialogue.condition.Conditions.isInTerrainLand;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import com.medsal15.ExtraStuck;
import com.medsal15.world.land.ESLandTypes;
import com.mraof.minestuck.data.dialogue.ChainBuilder;
import com.mraof.minestuck.data.dialogue.DialogueLangHelper;
import com.mraof.minestuck.data.dialogue.DialogueProvider;
import com.mraof.minestuck.data.dialogue.DialogueProvider.MessageProducer;
import com.mraof.minestuck.data.dialogue.DialogueProvider.NodeBuilder;
import com.mraof.minestuck.data.dialogue.DialogueProvider.ResponseBuilder;
import com.mraof.minestuck.data.dialogue.SelectableDialogueProvider;
import com.mraof.minestuck.entity.dialogue.DialogueAnimationData;
import com.mraof.minestuck.entity.dialogue.RandomlySelectableDialogue;
import com.mraof.minestuck.entity.dialogue.Trigger;
import com.mraof.minestuck.item.loot.MSLootTables;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;

public final class ESDialoguesProvider {
    private static final Trigger.OpenConsortMerchantGui SHOP_TRIGGER = new Trigger.OpenConsortMerchantGui(
            MSLootTables.CONSORT_FOOD_STOCK);

    public static DataProvider consort(PackOutput output, LanguageProvider languageProvider,
            CompletableFuture<HolderLookup.Provider> lookup) {
        SelectableDialogueProvider provider = new SelectableDialogueProvider(ExtraStuck.MODID,
                RandomlySelectableDialogue.DialogueCategory.CONSORT, lookup, output);
        DialogueLangHelper helper = new DialogueLangHelper(ExtraStuck.MODID, languageProvider);

        consortDialogues(provider, helper);

        return provider;
    }

    public static DataProvider generalShop(PackOutput output, LanguageProvider languageProvider,
            CompletableFuture<HolderLookup.Provider> lookup) {
        SelectableDialogueProvider provider = new SelectableDialogueProvider(ExtraStuck.MODID,
                RandomlySelectableDialogue.DialogueCategory.CONSORT_GENERAL_MERCHANT, lookup, output);
        DialogueLangHelper helper = new DialogueLangHelper(ExtraStuck.MODID, languageProvider);

        generalShopDialogues(provider, helper);

        return provider;
    }

    public static DataProvider foodShop(PackOutput output, LanguageProvider languageProvider,
            CompletableFuture<HolderLookup.Provider> lookup) {
        SelectableDialogueProvider provider = new SelectableDialogueProvider(ExtraStuck.MODID,
                RandomlySelectableDialogue.DialogueCategory.CONSORT_FOOD_MERCHANT, lookup, output);
        DialogueLangHelper helper = new DialogueLangHelper(ExtraStuck.MODID, languageProvider);

        foodShopDialogues(provider, helper);

        return provider;
    }

    private static void consortDialogues(SelectableDialogueProvider provider, DialogueLangHelper helper) {
        provider.getLookupProvider().thenCompose(__ -> {
            provider.addRandomlySelectable("dark",
                    defaultWeight(isInTerrainLand(ESLandTypes.DARK)),
                    new NodeBuilder(helper
                            .defaultKeyMsg("I love moving around holding my lantern up because there is no light."))
                            .animation(DialogueAnimationData.ANGRY_EMOTION));

            provider.addRandomlySelectable("sneak",
                    defaultWeight(isInTerrainLand(ESLandTypes.DARK)),
                    new ChainBuilder()
                            .node(new NodeBuilder(helper.defaultKeyMsg("Nothing beats sneaking out at night.")))
                            .node(new NodeBuilder(
                                    helper.defaultKeyMsg("Mostly because there are too many sculk sensors..."))
                                    .animation(DialogueAnimationData.ANXIOUS_EMOTION)));

            provider.addRandomlySelectable("ancient_city_fake",
                    defaultWeight(isInTerrainLand(ESLandTypes.DARK)),
                    new ChainBuilder()
                            .node(new NodeBuilder(helper.defaultKeyMsg(
                                    "There are rumors of ancient ruined cities located deep underground.")))
                            .node(new NodeBuilder(helper.defaultKeyMsg("As if. Only an idiot would believe that."))
                                    .animation(DialogueAnimationData.ANGRY_EMOTION)));

            provider.addRandomlySelectable("ancient_city_real",
                    defaultWeight(isInTerrainLand(ESLandTypes.DARK)),
                    new ChainBuilder()
                            .node(new NodeBuilder(helper.defaultKeyMsg(
                                    "There are rumors of ancient ruined cities located deep underground.")))
                            .node(new NodeBuilder(
                                    helper.defaultKeyMsg("Can you imagine all the treasures hidden down there?"))
                                    .animation(DialogueAnimationData.HAPPY_EMOTION)));

            provider.addRandomlySelectable("ancient_city_warden",
                    defaultWeight(isInTerrainLand(ESLandTypes.DARK)),
                    new ChainBuilder()
                            .node(new NodeBuilder(helper.defaultKeyMsg(
                                    "There are rumors of ancient ruined cities located deep underground.")))
                            .node(new NodeBuilder(
                                    helper.defaultKeyMsg("I hope not. I heard stories about Wardens..."))
                                    .animation(DialogueAnimationData.ANXIOUS_EMOTION)));

            return CompletableFuture.allOf();
        });
    }

    private static void generalShopDialogues(SelectableDialogueProvider provider, DialogueLangHelper helper) {
        provider.getLookupProvider().thenCompose(__ -> {
            provider.addRandomlySelectable("dark",
                    defaultWeight(isInTerrainLand(ESLandTypes.DARK)),
                    new NodeBuilder(helper
                            .defaultKeyMsg(
                                    "I love looting old structures. I never know what is for me and what is for sale!"))
                            .animation(DialogueAnimationData.HAPPY_EMOTION)
                            .addResponse(
                                    createResponseBuilder(DialogueLangHelper.msg(DialogueProvider.ARROW)).orElseThrow()
                                            .addTrigger(SHOP_TRIGGER)));

            return CompletableFuture.allOf();
        });
    }

    private static void foodShopDialogues(SelectableDialogueProvider provider, DialogueLangHelper helper) {
        provider.getLookupProvider().thenCompose(__ -> {
            provider.addRandomlySelectable("dark",
                    defaultWeight(isInTerrainLand(ESLandTypes.DARK)),
                    new NodeBuilder(helper.defaultKeyMsg("Welcome to Darkonald's"))
                            .addResponse(
                                    createResponseBuilder(DialogueLangHelper.msg(DialogueProvider.ARROW)).orElseThrow()
                                            .addTrigger(SHOP_TRIGGER)));

            return CompletableFuture.allOf();
        });
    }

    private static Optional<ResponseBuilder> createResponseBuilder(MessageProducer message) {
        try {
            // WHY IS IT NOT PUBLIC
            Constructor<ResponseBuilder> constructor = (Constructor<ResponseBuilder>) ResponseBuilder.class
                    .getDeclaredConstructor(MessageProducer.class);
            constructor.setAccessible(true);
            return Optional.of(constructor.newInstance(message));
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
