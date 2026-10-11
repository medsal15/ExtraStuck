package com.medsal15.compat.patchouli.pages;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.CombinationRecipe;
import com.mraof.minestuck.api.alchemy.recipe.combination.JeiCombination;
import com.mraof.minestuck.client.util.GuiUtil;
import com.mraof.minestuck.item.crafting.MSRecipeTypes;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;
import vazkii.patchouli.client.book.gui.GuiBook;
import vazkii.patchouli.client.book.page.abstr.PageSimpleProcessingRecipe;

public class CombinationPage extends PageSimpleProcessingRecipe<CombinationRecipe> {
    boolean showGristCost = true;

    public CombinationPage() {
        super(MSRecipeTypes.COMBINATION_TYPE.get());
    }

    @Override
    protected void drawRecipe(GuiGraphics graphics, CombinationRecipe recipe, int recipeX, int recipeY, int mouseX,
            int mouseY, boolean second) {
        Level level = Minecraft.getInstance().level;
        if (level == null)
            return;
        List<JeiCombination> combinations = recipe.getJeiCombinations();
        if (combinations.size() <= 0)
            return;
        JeiCombination combination = combinations.getFirst();
        ItemStack output = combination.output();

        parent.drawCenteredStringNoShadow(graphics, getTitle(second).getVisualOrderText(), GuiBook.PAGE_WIDTH / 2,
                recipeY, book.headerColor);
        int y = fontRenderer.split(FormattedText.of(text.asString()), GuiBook.PAGE_WIDTH).size()
                * fontRenderer.lineHeight;
        RenderSystem.enableBlend();
        graphics.blit(book.craftingTexture, recipeX, recipeY + y + 10, 0, 181, 96, 24, 128, 256);

        parent.renderIngredient(graphics, recipeX + 4, recipeY + y + 14, mouseX, mouseY, combination.input1());
        parent.drawCenteredStringNoShadow(graphics, combination.mode().asBoolean() ? "&&" : "||", recipeX + 30,
                recipeY + y + 18, 0x1AB3F1);
        parent.renderIngredient(graphics, recipeX + 40, recipeY + y + 14, mouseX, mouseY, combination.input2());
        parent.drawCenteredStringNoShadow(graphics, "==>", recipeX + 66, recipeY + y + 18, 0x1AB3F1);
        parent.renderItemStack(graphics, recipeX + 76, recipeY + y + 14, mouseX, mouseY, output);
        y += getRecipeHeight();

        if (showGristCost) {
            GristSet gristSet = GristCostRecipe.findCostForItem(output, null, true, level);

            if (gristSet != null)
                GuiUtil.drawGristBoard(graphics, gristSet, GuiUtil.GristboardMode.LARGE_ALCHEMITER, recipeX,
                        recipeY + y, fontRenderer);
        }
    }

    @Override
    protected ItemStack getRecipeOutput(Level level, CombinationRecipe recipe) {
        if (recipe == null || level == null)
            return ItemStack.EMPTY;
        List<JeiCombination> combinations = recipe.getJeiCombinations();
        if (combinations.size() <= 0)
            return ItemStack.EMPTY;
        JeiCombination combination = combinations.getFirst();

        return combination.output();
    }

    @Override
    protected CombinationRecipe loadRecipe(Level level, BookContentsBuilder builder, BookEntry entry,
            ResourceLocation recipeId, boolean linkRecipe) {
        if (recipeId == null || level == null) {
            return null;
        }

        CombinationRecipe tempRecipe = getRecipe(level, recipeId);
        if (tempRecipe == null) {
            // this is hacky but it works around Forge requiring custom recipes to have the
            // prefix of the adding mod
            tempRecipe = getRecipe(level, ResourceLocation.fromNamespaceAndPath("crafttweaker", recipeId.getPath()));
        }

        if (tempRecipe != null) {
            List<JeiCombination> combinations = tempRecipe.getJeiCombinations();
            if (combinations.size() > 0) {
                if (linkRecipe) {
                    entry.addRelevantStack(builder, combinations.get(0).output(), pageNum);
                }
                return tempRecipe;
            }
        }

        PatchouliAPI.LOGGER.warn("Recipe {} (of type {}) not found", recipeId,
                BuiltInRegistries.RECIPE_TYPE.getKey(MSRecipeTypes.COMBINATION_TYPE.get()));
        return null;
    }

    @Nullable
    private CombinationRecipe getRecipe(Level level, ResourceLocation id) {
        RecipeManager manager = level.getRecipeManager();
        var recipeHolder = manager.byKey(id)
                .filter(recipe -> recipe.value().getType() == MSRecipeTypes.COMBINATION_TYPE.get()).orElse(null);
        return recipeHolder != null ? (CombinationRecipe) recipeHolder.value() : null;
    }

    @Override
    protected int getRecipeHeight() {
        return 40;
    }

    @Override
    public int getTextHeight() {
        return getY() + 10;
    }
}
