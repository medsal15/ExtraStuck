package com.medsal15.client.screen;

import java.text.NumberFormat;

import javax.annotation.Nonnull;

import com.medsal15.ExtraStuck;
import com.medsal15.blockentities.ReactorBlockEntity;
import com.medsal15.config.ConfigCommon;
import com.medsal15.data.ESLangProvider;
import com.medsal15.menus.ReactorMenu;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mraof.minestuck.Minestuck;
import com.mraof.minestuck.client.gui.MachineScreen;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;

public class ReactorScreen extends MachineScreen<ReactorMenu> {
    private static final ResourceLocation BACKGROUND_TEXTURE = ExtraStuck.modid("textures/gui/reactor.png");
    private static final ResourceLocation FUEL_BAR_TEXTURE = ExtraStuck.modid("textures/gui/progress/reactor.png");
    private static final ResourceLocation CHARGE_BAR_TEXTURE = ExtraStuck.modid("textures/gui/progress/charger.png");
    private static final ResourceLocation URANIUM_BAR_TEXTURE = Minestuck.id("textures/gui/progress/uranium_level.png");
    private static final int FUEL_BAR_X = 115;
    private static final int FUEL_BAR_Y = 15;
    private static final int FUEL_BAR_WIDTH = 22;
    private static final int FUEL_BAR_HEIGHT = 46;
    private static final int CHARGE_BAR_X = 8;
    private static final int CHARGE_BAR_Y = 15;
    private static final int CHARGE_BAR_WIDTH = 30;
    private static final int CHARGE_BAR_HEIGHT = 46;
    private static final int URANIUM_BAR_X = 45;
    private static final int URANIUM_BAR_Y = 18;
    private static final int URANIUM_BAR_WIDTH = 35;
    private static final int URANIUM_BAR_HEIGHT = 39;
    private static final int FLUID_BAR_X = 87;
    private static final int FLUID_BAR_Y = 15;
    private static final int FLUID_BAR_WIDTH = 21;
    private static final int FLUID_BAR_HEIGHT = 46;

    public ReactorScreen(ReactorMenu menu, Inventory inventory, Component title) {
        super(menu, inventory, title);
    }

    @Override
    public void render(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.render(guiGraphics, mouseX, mouseY, partialTick);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }

    @Override
    protected void renderTooltip(@Nonnull GuiGraphics guiGraphics, int mouseX, int mouseY) {
        super.renderTooltip(guiGraphics, mouseX, mouseY);

        int x = mouseX - leftPos;
        int y = mouseY - topPos;
        if (x >= CHARGE_BAR_X && x <= CHARGE_BAR_X + CHARGE_BAR_WIDTH && y >= CHARGE_BAR_Y
                && y <= CHARGE_BAR_Y + CHARGE_BAR_HEIGHT) {
            Component tooltip = Component.translatable(ESLangProvider.ENERGY_STORAGE_KEY,
                    NumberFormat.getInstance().format(menu.getCharge()),
                    NumberFormat.getInstance().format(ConfigCommon.REACTOR_FE_STORAGE.get()));
            guiGraphics.renderTooltip(font, tooltip, mouseX, mouseY);
        }

        if (x >= FLUID_BAR_X && x <= FLUID_BAR_X + FLUID_BAR_WIDTH && y >= FLUID_BAR_Y
                && y <= FLUID_BAR_Y + FLUID_BAR_HEIGHT) {
            Minecraft mc = minecraft;
            Component tooltip = null;
            if (menu.getFluidAmount() > 0 && mc != null && mc.player != null) {
                @SuppressWarnings("deprecation")
                BlockEntity be = mc.player.level().getBlockEntity(menu.machinePos);

                if (be instanceof ReactorBlockEntity reactor) {
                    tooltip = Component.translatable(ESLangProvider.FLUID_STORAGE_KEY,
                            NumberFormat.getInstance().format(menu.getFluidAmount()),
                            NumberFormat.getInstance().format(ConfigCommon.REACTOR_FLUID_STORAGE.get()),
                            reactor.getFluid().getFluidType().getDescription());
                }
            } else if (menu.getFluidAmount() <= 0) {
                tooltip = Component.translatable(ESLangProvider.FLUID_STORAGE_KEY,
                        NumberFormat.getInstance().format(menu.getFluidAmount()),
                        NumberFormat.getInstance().format(ConfigCommon.REACTOR_FLUID_STORAGE.get()),
                        Fluids.EMPTY.getFluidType().getDescription());
            }

            if (tooltip != null) {
                guiGraphics.renderTooltip(font, tooltip, mouseX, mouseY);
            }
        }
    }

    @Override
    protected void renderBg(@Nonnull GuiGraphics guiGraphics, float partialTick, int mouseX, int mouseY) {
        RenderSystem.setShaderColor(1, 1, 1, 1);

        guiGraphics.blit(BACKGROUND_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);

        int charge_fill = getScaledValue(menu.getCharge(), ConfigCommon.REACTOR_FE_STORAGE.get(), CHARGE_BAR_HEIGHT);
        guiGraphics.blit(CHARGE_BAR_TEXTURE, leftPos + CHARGE_BAR_X,
                topPos + CHARGE_BAR_Y + CHARGE_BAR_HEIGHT - charge_fill, 0,
                CHARGE_BAR_HEIGHT - charge_fill, CHARGE_BAR_WIDTH, charge_fill, CHARGE_BAR_WIDTH, CHARGE_BAR_HEIGHT);

        int uranium_fill = getScaledValue(menu.getUranium(), ConfigCommon.REACTOR_URANIUM_STORAGE.get(),
                URANIUM_BAR_HEIGHT);
        guiGraphics.blit(URANIUM_BAR_TEXTURE, leftPos + URANIUM_BAR_X,
                topPos + URANIUM_BAR_Y + URANIUM_BAR_HEIGHT - uranium_fill, 0, URANIUM_BAR_HEIGHT - uranium_fill,
                URANIUM_BAR_WIDTH, uranium_fill, URANIUM_BAR_WIDTH, URANIUM_BAR_HEIGHT);

        Minecraft mc = minecraft;
        if (menu.getFluidAmount() > 0 && mc != null && mc.player != null) {
            @SuppressWarnings("deprecation")
            BlockEntity be = mc.player.level().getBlockEntity(menu.machinePos);
            if (be != null && be instanceof ReactorBlockEntity reactor) {
                int fluid_fill = getScaledValue(menu.getFluidAmount(), ConfigCommon.REACTOR_FLUID_STORAGE.get(),
                        FLUID_BAR_HEIGHT);
                renderFluid(guiGraphics, leftPos + FLUID_BAR_X, topPos + FLUID_BAR_Y + FLUID_BAR_HEIGHT,
                        FLUID_BAR_WIDTH, fluid_fill, reactor.getFluidStack());
            }
        }

        if (menu.getMaxFuel() > 0) {
            int fuel_fill = getScaledValue(menu.getFuel(), menu.getMaxFuel(), FUEL_BAR_HEIGHT);
            guiGraphics.blit(FUEL_BAR_TEXTURE, leftPos + FUEL_BAR_X, topPos + FUEL_BAR_Y + FUEL_BAR_HEIGHT - fuel_fill,
                    0, FUEL_BAR_HEIGHT - fuel_fill, FUEL_BAR_WIDTH, fuel_fill, FUEL_BAR_WIDTH, FUEL_BAR_HEIGHT);
        }
    }

    public void renderFluid(GuiGraphics guiGraphics, int x, int y, int width, int height, FluidStack fluidStack) {
        // Copied from Direwolf20
        // I have a decent idea on what it does (not why) (wtf is a tesselator)
        // (are u/v-0/1 texture positions?)
        Minecraft mc = minecraft;
        if (fluidStack.isEmpty() || mc == null || height == 0)
            return;

        Fluid fluid = fluidStack.getFluid();
        ResourceLocation stillTexture = IClientFluidTypeExtensions.of(fluid).getStillTexture();
        TextureAtlasSprite stillSprite = mc.getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(stillTexture);
        int fluidColor = IClientFluidTypeExtensions.of(fluid).getTintColor();

        // Ensure tinted fluids are tinted
        float red = (float) (fluidColor >> 16 & 255) / 255F;
        float green = (float) (fluidColor >> 8 & 255) / 255F;
        float blue = (float) (fluidColor & 255) / 255F;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);

        PoseStack poseStack = guiGraphics.pose();
        poseStack.pushPose();
        RenderSystem.setShaderColor(red, green, blue, 1F);

        int zlevel = 0;
        float uMin = stillSprite.getU0();
        float uMax = stillSprite.getU1();
        float vMin = stillSprite.getV0();
        float vMax = stillSprite.getV1();
        int texWidth = stillSprite.contents().width();
        int texHeight = stillSprite.contents().height();

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder vertextBuffer = tesselator.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        int yOffset = 0;
        while (yOffset < height) {
            int drawHeight = Math.min(texHeight, height - yOffset);
            int drawY = y - yOffset - drawHeight;

            float vMaxAdjusted = vMin + (vMax - vMin) * ((float) drawHeight / texHeight);

            int xOffset = 0;
            while (xOffset < width) {
                int drawWidth = Math.min(texWidth, width - xOffset);

                float uMaxAdjusted = uMin + (uMax - uMin) * ((float) drawWidth / texWidth);

                vertextBuffer.addVertex(poseStack.last().pose(), x + xOffset, drawY + drawHeight, zlevel).setUv(uMin,
                        vMaxAdjusted);
                vertextBuffer.addVertex(poseStack.last().pose(), x + xOffset + drawWidth, drawY + drawHeight, zlevel)
                        .setUv(uMaxAdjusted, vMaxAdjusted);
                vertextBuffer.addVertex(poseStack.last().pose(), x + xOffset + drawWidth, drawY, zlevel)
                        .setUv(uMaxAdjusted, vMin);
                vertextBuffer.addVertex(poseStack.last().pose(), x + xOffset, drawY, zlevel).setUv(uMin, vMin);

                xOffset += drawWidth;
            }
            yOffset += drawHeight;
        }

        BufferUploader.drawWithShader(vertextBuffer.buildOrThrow());
        poseStack.popPose();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.applyModelViewMatrix();
    }
}
