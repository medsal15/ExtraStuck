package com.medsal15.items.tools;

import java.util.List;

import com.medsal15.items.tools.VisionItem.VisionEffect;

public interface IVision {
    default List<VisionEffect> getEffects() {
        return List.of();
    }
}
