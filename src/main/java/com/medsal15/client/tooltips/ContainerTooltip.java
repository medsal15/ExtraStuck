package com.medsal15.client.tooltips;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.component.ItemContainerContents;

public record ContainerTooltip(ItemContainerContents contents) implements TooltipComponent {
}
