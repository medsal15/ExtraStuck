package com.medsal15.compat.ponder.scenes;

import com.medsal15.compat.ponder.ESPonderPlugin;
import com.mraof.minestuck.block.machine.HolopadBlock;
import com.mraof.minestuck.item.MSItems;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;

public final class CosmeticScenes {
    public static void Holopad(SceneBuilder builder, SceneBuildingUtil util) {
        PonderSceneBuilder scene = new PonderSceneBuilder(builder.getScene());
        BlockPos holopadPos = util.grid().at(2, 1, 2);

        // Start
        scene.title("holopad", "header");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.world().showSection(util.select().position(5, 3, 5), Direction.DOWN);
        scene.idle(5);

        // Place & describe Holopad
        scene.world().showIndependentSection(util.select().fromTo(2, 1, 2, 2, 1, 2), Direction.DOWN);
        scene.idle(25);

        scene.overlay().showText(40).text("holopad.text_1").attachKeyFrame().placeNearTarget()
                .pointAt(util.vector().blockSurface(holopadPos, Direction.NORTH));
        scene.idle(60);

        // Add punched card
        Vec3 holopadSelection = util.vector().blockSurface(holopadPos, Direction.UP);
        scene.overlay().showText(30).text("punch_designix.text_2").attachKeyFrame().placeNearTarget()
                .pointAt(holopadSelection);
        scene.overlay().showControls(holopadSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.punchedCard(MSItems.CRUXITE_APPLE.get()));
        scene.idle(15);
        scene.world().cycleBlockProperty(holopadPos, HolopadBlock.HAS_CARD);
        scene.world().createItemEntity(holopadSelection, Vec3.ZERO, MSItems.CRUXITE_APPLE.toStack());
        scene.idle(35);
    }
}
