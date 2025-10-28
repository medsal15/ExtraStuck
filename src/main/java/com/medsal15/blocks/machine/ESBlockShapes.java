package com.medsal15.blocks.machine;

import com.mraof.minestuck.block.CustomVoxelShape;

public final class ESBlockShapes {
    public static final CustomVoxelShape CHARGER = new CustomVoxelShape(new double[][] {
            { 0, 0, 0, 16, 8, 16 }
    });

    public static final CustomVoxelShape PRINTER = new CustomVoxelShape(new double[][] {
            { 0, 0, 0, 16, 12, 16 }
    });
    public static final CustomVoxelShape PRINTER_DOWEL = new CustomVoxelShape(new double[][] {
            { 6, 12, 6, 10, 16, 10 }
    });

    public static final CustomVoxelShape NUCLEAR_REACTOR = new CustomVoxelShape(new double[][] {
            { 0, 0, 0, 16, 12, 16 },
            { 3, 12, 3, 13, 16, 13 }
    });

    public static final CustomVoxelShape URANIUM_BLASTER = new CustomVoxelShape(
            new double[][] { { 0, 0, 0, 16, 16, 16 } });
}
