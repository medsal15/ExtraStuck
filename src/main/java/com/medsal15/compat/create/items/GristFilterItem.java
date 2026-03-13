package com.medsal15.compat.create.items;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import com.medsal15.ExtraStuck;
import com.medsal15.compat.create.client.menus.GristFilterMenu;
import com.medsal15.data.ESLangProvider;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.mraof.minestuck.api.alchemy.GristSet;
import com.mraof.minestuck.api.alchemy.GristType;
import com.mraof.minestuck.api.alchemy.recipe.GristCostRecipe;
import com.simibubi.create.AllKeys;
import com.simibubi.create.content.logistics.filter.FilterItem;
import com.simibubi.create.content.logistics.filter.FilterItemStack;
import com.simibubi.create.foundation.item.TooltipHelper;

import net.createmod.catnip.lang.FontHelper.Palette;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.network.codec.NeoForgeStreamCodecs;

public class GristFilterItem extends FilterItem {
    public static final String LIST_TITLE = "gui.extrastuck.grist_filter.list_title";

    public GristFilterItem(Properties properties) {
        super(properties);
    }

    @Override
    public FilterItemStack makeStackWrapper(ItemStack filter) {
        return new GristFilterItemstack(filter);
    }

    @Override
    public DataComponentType<?> getComponentType() {
        return ESCreateComponents.GRIST_FILTER_DATA.get();
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add(TooltipHelper.holdShift(Palette.GRAY, AllKeys.shiftDown()));

        if (!AllKeys.shiftDown()) {
            List<Component> makeSummary = makeSummary(stack);
            if (!makeSummary.isEmpty()) {
                tooltip.add(CommonComponents.SPACE);
                tooltip.addAll(makeSummary);
            }
        } else {
            tooltip.add(CommonComponents.SPACE);
            tooltip.addAll(TooltipHelper.cutTextComponent(Component.translatable(ESLangProvider.GRIST_FILTER_SUMMARY),
                    Palette.STANDARD_CREATE));
            tooltip.add(CommonComponents.SPACE);
            tooltip.addAll(TooltipHelper.cutTextComponent(Component.translatable(ESLangProvider.GRIST_FILTER_CONDITION),
                    Palette.ALL_GRAY));
            tooltip.addAll(TooltipHelper.cutTextComponent(
                    Component.literal(" ").append(Component.translatable(ESLangProvider.GRIST_FILTER_BEHAVIOR)),
                    Palette.STANDARD_CREATE));
        }
    }

    @Override
    public List<Component> makeSummary(ItemStack filter) {
        List<Component> list = new ArrayList<>();

        list.add(Component.translatable(LIST_TITLE).withStyle(ChatFormatting.GOLD));
        int count = 0;
        List<GristFilterEntry> entries = filter.getOrDefault(ESCreateComponents.GRIST_FILTER_DATA, List.of());
        for (GristFilterEntry entry : entries) {
            if (count > 3) {
                list.add(Component.literal("- ...").withStyle(ChatFormatting.DARK_GRAY));
                break;
            }
            list.add(Component.literal("- ").append(entry.text()));
            count++;
        }

        if (count == 0)
            return List.of();

        return list;
    }

    @Override
    public ItemStack[] getFilterItems(ItemStack stack) {
        return new ItemStack[0];
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return GristFilterMenu.create(id, inv, player.getMainHandItem());
    }

    public static class GristFilterItemstack extends FilterItemStack {
        public List<GristFilterEntry> entries;

        public GristFilterItemstack(ItemStack filter) {
            super(filter);

            entries = filter.getOrDefault(ESCreateComponents.GRIST_FILTER_DATA, List.of());
        }

        @Override
        public boolean test(Level world, FluidStack stack, boolean matchNBT) {
            return false;
        }

        @Override
        public boolean test(Level world, ItemStack stack, boolean matchNBT) {
            GristSet cost = GristCostRecipe.findCostForItem(stack, null, true, world);
            // No cost, no pass
            if (cost == null)
                return false;

            for (GristFilterEntry entry : entries) {
                int amount = 0;
                if (cost.hasType(entry.grist()))
                    amount = (int) cost.getGrist(entry.grist());

                if (!entry.test(amount))
                    return false;
            }

            return true;
        }
    }

    public static record GristFilterEntry(GristType grist, Integer amount, ComparingMode mode) {
        public static final Codec<GristFilterEntry> CODEC = RecordCodecBuilder.create(inst -> inst.group(
                GristType.CODEC.fieldOf("grist").forGetter(GristFilterEntry::grist),
                Codec.INT.optionalFieldOf("amount", 0).forGetter(GristFilterEntry::amount),
                StringRepresentable.fromEnum(ComparingMode::values).fieldOf("mode").forGetter(GristFilterEntry::mode))
                .apply(inst, GristFilterEntry::new));
        public static final Codec<List<GristFilterEntry>> LIST_CODEC = CODEC.listOf();
        public static final StreamCodec<RegistryFriendlyByteBuf, GristFilterEntry> STREAM_CODEC = StreamCodec.composite(
                GristType.STREAM_CODEC, GristFilterEntry::grist,
                ByteBufCodecs.INT, GristFilterEntry::amount,
                NeoForgeStreamCodecs.enumCodec(ComparingMode.class), GristFilterEntry::mode,
                GristFilterEntry::new);
        public static final StreamCodec<RegistryFriendlyByteBuf, List<GristFilterEntry>> LIST_STREAM_CODEC = STREAM_CODEC
                .apply(ByteBufCodecs.list());

        public MutableComponent text() {
            return Component.translatable(mode.key(), amount, Component.translatable(grist.getTranslationKey()));
        }

        public boolean test(int amount) {
            return mode.test(amount, this.amount);
        }

        public static enum ComparingMode implements StringRepresentable {
            GREATER_THAN_EQUAL,
            GREATER_THAN,
            EQUAL,
            NOT_EQUAL,
            LESS_THAN_EQUAL,
            LESS_THAN;

            @Override
            public String getSerializedName() {
                return this.name().toLowerCase(Locale.ROOT);
            }

            public static ComparingMode fromName(String string) throws IllegalArgumentException {
                for (ComparingMode mode : ComparingMode.values()) {
                    if (mode.name().toLowerCase().equals(string))
                        return mode;
                }
                throw new IllegalArgumentException("Invalid comparing mode " + string);
            }

            public String key() {
                return ExtraStuck.MODID + ".grist_filter." + name().toLowerCase();
            }

            public String symbol() {
                return ExtraStuck.MODID + ".grist_filter." + name().toLowerCase() + ".symbol";
            }

            public boolean test(int left, int right) {
                return switch (this) {
                    case GREATER_THAN_EQUAL -> left >= right;
                    case GREATER_THAN -> left > right;
                    case EQUAL -> left == right;
                    case NOT_EQUAL -> left != right;
                    case LESS_THAN_EQUAL -> left <= right;
                    case LESS_THAN -> left < right;
                };
            }
        }
    }
}
