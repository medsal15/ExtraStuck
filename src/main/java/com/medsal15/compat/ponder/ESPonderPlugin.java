package com.medsal15.compat.ponder;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.ponder.scenes.AlchemyScenes;
import com.medsal15.compat.ponder.scenes.CosmeticScenes;
import com.medsal15.config.ConfigClient;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.item.components.CardStoredItemComponent;
import com.mraof.minestuck.item.components.EncodedItemComponent;
import com.mraof.minestuck.item.components.MSItemComponents;
import com.mraof.minestuck.util.ColorHandler;

import net.createmod.ponder.api.registration.PonderPlugin;
import net.createmod.ponder.api.registration.PonderSceneRegistrationHelper;
import net.createmod.ponder.api.registration.PonderTagRegistrationHelper;
import net.createmod.ponder.foundation.PonderIndex;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * Ponder plugin that adds Ponder support to Minestuck
 * <p>
 * Feel free to copy into the official mod (I couldn't figure out how)
 */
public class ESPonderPlugin implements PonderPlugin {
    @Override
    public String getModId() {
        return ExtraStuck.MODID;
    }

    public static final ResourceLocation ALCHEMY = ExtraStuck.modid("alchemy");
    public static final ResourceLocation ALCHEMY_RECIPES = ExtraStuck.modid("alchemy/recipes");

    @Override
    public void registerTags(@Nonnull PonderTagRegistrationHelper<ResourceLocation> helper) {
        helper.registerTag(ALCHEMY).addToIndex().item(MSItems.ALCHEMITER, true, false).register();
        helper.registerTag(ALCHEMY_RECIPES).addToIndex().item(MSItems.CRUXITE_DOWEL, true, false).register();

        helper.addToTag(ALCHEMY)
                .add(MSItems.CRUXTRUDER.getId())
                .add(MSItems.PUNCH_DESIGNIX.getId())
                .add(MSItems.TOTEM_LATHE.getId())
                .add(MSItems.ALCHEMITER.getId())
                .add(MSItems.INTELLIBEAM_LASERSTATION.getId());
        helper.addToTag(ALCHEMY_RECIPES)
                .add(MSItems.CAPTCHA_CARD.getId())
                .add(MSItems.CRUXITE_DOWEL.getId());
    }

    @Override
    public void registerScenes(@Nonnull PonderSceneRegistrationHelper<ResourceLocation> helper) {
        if (!ConfigClient.addPonderMinestuckEntries)
            return;
        helper.forComponents(MSItems.CRUXTRUDER.getId(), MSItems.CRUXITE_DOWEL.getId()).addStoryBoard(
                ExtraStuck.modid("alchemy/cruxtruder"), AlchemyScenes::Cruxtruder, ALCHEMY);

        helper.forComponents(MSItems.PUNCH_DESIGNIX.getId(), MSItems.CAPTCHA_CARD.getId())
                .addStoryBoard(ExtraStuck.modid("alchemy/punch_designix"), AlchemyScenes::PunchDesignix, ALCHEMY)
                .addStoryBoard(ExtraStuck.modid("alchemy/punch_designix"), AlchemyScenes::ORAlchemy, ALCHEMY,
                        ALCHEMY_RECIPES);

        helper.forComponents(MSItems.TOTEM_LATHE.getId(), MSItems.CRUXITE_DOWEL.getId(), MSItems.CAPTCHA_CARD.getId())
                .addStoryBoard(
                        ExtraStuck.modid("alchemy/totem_lathe"), AlchemyScenes::TotemLathe, ALCHEMY)
                .addStoryBoard(
                        ExtraStuck.modid("alchemy/totem_lathe"), AlchemyScenes::ANDAlchemy, ALCHEMY, ALCHEMY_RECIPES);

        helper.forComponents(MSItems.ALCHEMITER.getId(), MSItems.CRUXITE_DOWEL.getId())
                .addStoryBoard(ExtraStuck.modid("alchemy/alchemiter"), AlchemyScenes::Alchemiter, ALCHEMY);

        helper.forComponents(MSItems.INTELLIBEAM_LASERSTATION.getId(), MSItems.PUNCH_DESIGNIX.getId(),
                MSItems.CAPTCHA_CARD.getId()).addStoryBoard(ExtraStuck.modid("alchemy/intellibeam_laserstation"),
                        AlchemyScenes::IntellibeamLaserstation, ALCHEMY);

        helper.forComponents(MSItems.HOLOPAD.getId(), MSItems.CAPTCHA_CARD.getId()).addStoryBoard(
                ExtraStuck.modid("cosmetic/holopad"), CosmeticScenes::Holopad);
    }

    public static ItemStack carvedDowel(Item contained) {
        return ColorHandler.setDefaultColor(EncodedItemComponent.createEncoded(MSItems.CRUXITE_DOWEL, contained));
    }

    public static ItemStack punchedCard(Item punched) {
        return EncodedItemComponent.createEncoded(MSItems.CAPTCHA_CARD, punched);
    }

    public static ItemStack contentCard(ItemStack contents, boolean ghost) {
        ItemStack card = MSItems.CAPTCHA_CARD.toStack();
        card.set(MSItemComponents.CARD_STORED_ITEM, new CardStoredItemComponent(contents, ghost));
        return card;
    }

    public static ItemStack contentCard(ItemStack contents) {
        return contentCard(contents, false);
    }

    public static void register() {
        PonderIndex.addPlugin(new ESPonderPlugin());
    }
}
