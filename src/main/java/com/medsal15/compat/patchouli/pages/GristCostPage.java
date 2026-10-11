package com.medsal15.compat.patchouli.pages;

import java.util.List;

import javax.annotation.Nullable;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.api.alchemy.recipe.JeiGristCost;
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

public class GristCostPage extends PageSimpleProcessingRecipe<GristCostRecipe> {
    public GristCostPage() {
        super(MSRecipeTypes.GRIST_COST_TYPE.get());
    }

    @Override
    protected void drawRecipe(GuiGraphics graphics, GristCostRecipe recipe, int recipeX, int recipeY, int mouseX,
            int mouseY, boolean second) {
        Level level = Minecraft.getInstance().level;
        if (level == null)
            return;
        ItemStack output = getRecipeOutput(level, recipe);
        GristSet gristSet = GristCostRecipe.findCostForItem(output, null, true, level);
        if (gristSet == null)
            return;
        List<JeiGristCost> gristCosts = recipe.getJeiCosts(level);
        if (gristCosts.size() <= 0)
            return;
        JeiGristCost gristCost = gristCosts.get(0);

        parent.drawCenteredStringNoShadow(graphics, getTitle(second).getVisualOrderText(), GuiBook.PAGE_WIDTH / 2,
                recipeY, book.headerColor);
        int y = fontRenderer.split(FormattedText.of(text.asString()), GuiBook.PAGE_WIDTH).size()
                * fontRenderer.lineHeight;
        RenderSystem.enableBlend();
        graphics.blit(book.craftingTexture, recipeX + 40, recipeY + y + 10, 0, 181, 24, 24, 128, 256);
        parent.renderIngredient(graphics, recipeX + 40 + 4, recipeY + y + 14, mouseX, mouseY, gristCost.ingredient());

        y += getRecipeHeight();
        GuiUtil.drawGristBoard(graphics, gristSet, GuiUtil.GristboardMode.LARGE_ALCHEMITER, recipeX, recipeY + y,
                fontRenderer);
    }

    @Override
    protected ItemStack getRecipeOutput(Level level, GristCostRecipe recipe) {
        if (recipe == null || level == null)
            return ItemStack.EMPTY;
        List<JeiGristCost> gristCosts = recipe.getJeiCosts(level);
        if (gristCosts.size() <= 0)
            return ItemStack.EMPTY;
        JeiGristCost gristCost = gristCosts.get(0);
        var items = gristCost.ingredient().getItems();
        if (items.length <= 0)
            return ItemStack.EMPTY;
        return items[0];
    }

    @Override
    protected GristCostRecipe loadRecipe(Level level, BookContentsBuilder builder, BookEntry entry,
            ResourceLocation recipeId, boolean linkRecipe) {
        if (recipeId == null || level == null) {
            return null;
        }

        GristCostRecipe tempRecipe = getRecipe(level, recipeId);
        if (tempRecipe == null) {
            // this is hacky but it works around Forge requiring custom recipes to have the
            // prefix of the adding mod
            tempRecipe = getRecipe(level, ResourceLocation.fromNamespaceAndPath("crafttweaker", recipeId.getPath()));
        }

        if (tempRecipe != null) {
            List<JeiGristCost> gristCosts = tempRecipe.getJeiCosts(level);
            if (gristCosts.size() > 0) {
                JeiGristCost gristCost = gristCosts.get(0);
                var items = gristCost.ingredient().getItems();
                if (items.length > 0 && linkRecipe) {
                    for (ItemStack item : items) {
                        entry.addRelevantStack(builder, item, pageNum);
                    }
                }
                return tempRecipe;
            }
        }

        PatchouliAPI.LOGGER.warn("Recipe {} (of type {}) not found", recipeId,
                BuiltInRegistries.RECIPE_TYPE.getKey(MSRecipeTypes.GRIST_COST_TYPE.get()));
        return null;
    }

    @Nullable
    private GristCostRecipe getRecipe(Level level, ResourceLocation id) {
        RecipeManager manager = level.getRecipeManager();
        var recipeHolder = manager.byKey(id)
                .filter(recipe -> recipe.value().getType() == MSRecipeTypes.GRIST_COST_TYPE.get()).orElse(null);
        return recipeHolder != null ? (GristCostRecipe) recipeHolder.value() : null;
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
