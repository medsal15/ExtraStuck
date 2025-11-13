package com.medsal15.compat.patchouli;

import java.util.function.UnaryOperator;

import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.GristTypes;
import com.mraof.minestuck.api.alchemy.MutableGristSet;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.mraof.minestuck.client.util.GuiUtil;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vazkii.patchouli.api.IComponentRenderContext;
import vazkii.patchouli.api.ICustomComponent;
import vazkii.patchouli.api.IVariable;

public class ComponentGristSet implements ICustomComponent {
    private transient int x, y;
    private transient GristSet gristSet = GristSet.EMPTY;

    String grist = "";
    IVariable item = IVariable.empty();

    @Override
    public void build(int componentX, int componentY, int pageNum) {
        x = componentX;
        y = componentY;
    }

    @Override
    public void onVariablesAvailable(UnaryOperator<IVariable> lookup, Provider registriesProvider) {
        if (!item.equals(IVariable.empty())) {
            IVariable var = lookup.apply(item);
            ItemStack stack = var.as(ItemStack.class);

            Level level = Minecraft.getInstance().level;
            if (!stack.isEmpty() && level != null) {
                gristSet = GristCostRecipe.findCostForItem(stack, null, true, level);
            }

            return;
        }

        String givenGrist = lookup.apply(IVariable.wrap(grist, registriesProvider)).asString();
        MutableGristSet set = MutableGristSet.newDefault();
        for (String entry : givenGrist.split(",")) {
            String[] split = entry.split("#", 2);
            if (split.length < 2)
                continue;
            ResourceLocation id = ResourceLocation.tryParse(split[0]);
            GristType grist = GristTypes.REGISTRY.get(id);
            if (grist == null)
                continue;
            int amount = Integer.parseInt(split[1]);
            set.add(grist, amount);
        }

        gristSet = set.asImmutable();
    }

    @Override
    public void render(GuiGraphics graphics, IComponentRenderContext context, float pticks, int mouseX, int mouseY) {
        if (gristSet != null) {
            Font font = Minecraft.getInstance().font;

            GuiUtil.drawGristBoard(graphics, gristSet, GuiUtil.GristboardMode.LARGE_ALCHEMITER, x, y, font);
        }
    }
}
