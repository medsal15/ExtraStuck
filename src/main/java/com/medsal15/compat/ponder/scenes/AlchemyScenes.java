package com.medsal15.compat.ponder.scenes;

import com.medsal15.compat.ponder.ESPonderPlugin;
import com.mraof.minestuck.block.EnumDowelType;
import com.mraof.minestuck.block.MSBlocks;
import com.mraof.minestuck.block.machine.AlchemiterBlock;
import com.mraof.minestuck.block.machine.PunchDesignixBlock;
import com.mraof.minestuck.block.machine.TotemLatheBlock;
import com.mraof.minestuck.blockentity.machine.AlchemiterBlockEntity;
import com.mraof.minestuck.blockentity.machine.TotemLatheBlockEntity;
import com.mraof.minestuck.item.MSItems;
import com.mraof.minestuck.util.ColorHandler;

import net.createmod.catnip.math.Pointing;
import net.createmod.ponder.api.scene.SceneBuilder;
import net.createmod.ponder.api.scene.SceneBuildingUtil;
import net.createmod.ponder.enums.PonderGuiTextures;
import net.createmod.ponder.foundation.PonderSceneBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public final class AlchemyScenes {
    public static void Cruxtruder(SceneBuilder builder, SceneBuildingUtil util) {
        PonderSceneBuilder scene = new PonderSceneBuilder(builder.getScene());
        BlockPos lidPos = util.grid().at(2, 4, 2);

        // Start
        scene.title("cruxtruder", "header");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.world().showSection(util.select().position(5, 5, 5), Direction.DOWN);
        scene.idle(5);

        // Place & describe Cruxtruder
        scene.world().showIndependentSection(util.select().fromTo(1, 1, 1, 3, 5, 3), Direction.DOWN);
        scene.idle(25);

        scene.overlay().showText(40).text("cruxtruder.text_1").attachKeyFrame().placeNearTarget()
                .pointAt(util.vector().topOf(2, 3, 2));
        scene.idle(60);

        // Remove lid
        Vec3 lidSelection = util.vector().blockSurface(lidPos, Direction.DOWN);
        scene.overlay().showControls(lidSelection, Pointing.UP, 25).leftClick();
        scene.overlay().showText(40).text("cruxtruder.text_2").attachKeyFrame().placeNearTarget().pointAt(lidSelection);
        scene.idle(20);
        scene.world().destroyBlock(lidPos);
        scene.idle(40);

        // Add raw cruxite
        scene.overlay().showControls(lidSelection, Pointing.DOWN, 15).withItem(MSItems.RAW_CRUXITE.toStack());
        scene.overlay().showText(30).text("cruxtruder.text_3").attachKeyFrame().pointAt(lidSelection);
        scene.idle(50);

        // Turn wheel and get dowel
        Vec3 wheelSelection = util.vector().blockSurface(lidPos.below(), Direction.NORTH);
        scene.overlay().showControls(wheelSelection, Pointing.RIGHT, 15).rightClick();
        scene.overlay().showText(40).text("cruxtruder.text_4").attachKeyFrame().placeNearTarget()
                .pointAt(wheelSelection);
        scene.idle(20);
        scene.world().setBlock(lidPos, MSBlocks.EMERGING_CRUXITE_DOWEL.get().defaultBlockState(), false);
        scene.overlay().showText(20).text("cruxtruder.text_5").pointAt(lidSelection);
        scene.idle(40);

        // Get the Dowel
        scene.overlay().showControls(lidSelection, Pointing.DOWN, 15).rightClick();
        scene.overlay().showText(40).text("cruxtruder.text_6").attachKeyFrame().pointAt(lidSelection);
        scene.world().setBlock(lidPos, Blocks.AIR.defaultBlockState(), false);
        scene.world().createItemEntity(lidSelection, Vec3.ZERO, MSItems.CRUXITE_DOWEL.toStack());
        scene.idle(20);
    }

    public static void TotemLathe(SceneBuilder builder, SceneBuildingUtil util) {
        PonderSceneBuilder scene = new PonderSceneBuilder(builder.getScene());
        BlockPos dowelPos = util.grid().at(2, 2, 2);
        BlockPos slotPos = util.grid().at(3, 1, 2);

        // Start
        scene.title("totem_lathe", "header");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.world().showSection(util.select().position(5, 5, 5), Direction.DOWN);
        scene.idle(5);

        // Place & describe Totem Lathe
        scene.world().showIndependentSection(util.select().fromTo(1, 1, 1, 3, 5, 3), Direction.DOWN);
        scene.idle(25);

        scene.overlay().showText(40).text("totem_lathe.text_1").attachKeyFrame().placeNearTarget()
                .pointAt(util.vector().blockSurface(dowelPos.west(), Direction.NORTH));
        scene.idle(60);

        // Place Dowel
        Vec3 dowelSelection = util.vector().blockSurface(dowelPos, Direction.NORTH);
        scene.overlay().showText(30).text("totem_lathe.text_2").attachKeyFrame().placeNearTarget()
                .pointAt(dowelSelection);
        scene.overlay().showControls(dowelSelection, Pointing.DOWN, 15).withItem(MSItems.CRUXITE_DOWEL.toStack());
        scene.idle(15);
        scene.world().modifyBlockEntity(slotPos, TotemLatheBlockEntity.class,
                be -> be.setDowel(ColorHandler.setDefaultColor(MSItems.CRUXITE_DOWEL.toStack())));
        scene.idle(35);

        // Add Card
        Vec3 slotSelection = util.vector().centerOf(slotPos);
        scene.overlay().showText(30).text("totem_lathe.text_3").attachKeyFrame().placeNearTarget()
                .pointAt(slotSelection);
        scene.overlay().showControls(slotSelection, Pointing.RIGHT, 15)
                .withItem(ESPonderPlugin.punchedCard(MSItems.CRUXITE_APPLE.asItem()));
        scene.idle(15);
        scene.world().modifyBlock(slotPos, state -> state.setValue(TotemLatheBlock.Slot.COUNT, 1), false);
        scene.idle(35);

        // Run Lathe
        Vec3 activationSelection = util.vector().centerOf(dowelPos.above());
        scene.overlay().showControls(activationSelection, Pointing.UP, 15).rightClick();
        scene.overlay().showText(30).text("totem_lathe.text_4").attachKeyFrame().placeNearTarget()
                .pointAt(activationSelection);
        scene.idle(15);
        scene.world().modifyBlockEntity(slotPos, TotemLatheBlockEntity.class,
                be -> be.setDowel(ESPonderPlugin.carvedDowel(MSItems.CRUXITE_APPLE.get())));
        scene.overlay().showText(30).text("totem_lathe.text_5").placeNearTarget().pointAt(dowelSelection);
        scene.idle(35);

        // Take it back
        scene.overlay().showControls(dowelSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.carvedDowel(MSItems.CRUXITE_APPLE.get()));
        scene.overlay().showControls(slotSelection, Pointing.UP, 15)
                .withItem(ESPonderPlugin.punchedCard(MSItems.CRUXITE_APPLE.get()));
        scene.overlay().showText(30).text("totem_lathe.text_6").attachKeyFrame().placeNearTarget()
                .pointAt(activationSelection);
        scene.idle(15);
        scene.world().modifyBlockEntity(slotPos, TotemLatheBlockEntity.class,
                be -> be.setDowel(ItemStack.EMPTY));
        scene.world().modifyBlock(slotPos, state -> state.setValue(TotemLatheBlock.Slot.COUNT, 0), false);
        scene.idle(35);
    }

    public static void ANDAlchemy(SceneBuilder builder, SceneBuildingUtil util) {
        PonderSceneBuilder scene = new PonderSceneBuilder(builder.getScene());
        BlockPos dowelPos = util.grid().at(2, 2, 2);
        BlockPos slotPos = util.grid().at(3, 1, 2);

        // Start
        scene.title("and_alchemy", "header");
        scene.configureBasePlate(0, 0, 5);
        scene.showBasePlate();
        scene.world().showSection(util.select().position(5, 5, 5), Direction.DOWN);
        scene.idle(5);

        // Place & describe Totem Lathe
        scene.world().showIndependentSection(util.select().fromTo(1, 1, 1, 3, 5, 3), Direction.DOWN);
        scene.world().modifyBlockEntity(slotPos, TotemLatheBlockEntity.class,
                be -> be.setDowel(ColorHandler.setDefaultColor(MSItems.CRUXITE_DOWEL.toStack())));
        scene.idle(25);

        scene.overlay().showText(40).text("and_alchemy.text_1").attachKeyFrame().placeNearTarget()
                .pointAt(util.vector().blockSurface(dowelPos.west(), Direction.NORTH));
        scene.idle(60);

        // Add Cards
        Vec3 slotSelection = util.vector().centerOf(slotPos);
        scene.overlay().showText(40).text("and_alchemy.text_2").attachKeyFrame().placeNearTarget()
                .pointAt(slotSelection);
        scene.overlay().showControls(slotSelection, Pointing.RIGHT, 15)
                .withItem(MSItems.SLEDGE_HAMMER.toStack());
        scene.overlay().showControls(slotSelection, Pointing.LEFT, 15)
                .withItem(new ItemStack(Items.IRON_BLOCK));
        scene.idle(15);
        scene.world().modifyBlock(slotPos, state -> state.setValue(TotemLatheBlock.Slot.COUNT, 2), false);
        scene.idle(35);

        // Run Lathe
        Vec3 activationSelection = util.vector().centerOf(dowelPos.above());
        scene.overlay().showControls(activationSelection, Pointing.UP, 15).rightClick();
        scene.overlay().showText(40).text("and_alchemy.text_3").attachKeyFrame().placeNearTarget()
                .pointAt(activationSelection);
        scene.idle(15);
        scene.world().modifyBlockEntity(slotPos, TotemLatheBlockEntity.class,
                be -> be.setDowel(ESPonderPlugin.carvedDowel(MSItems.CRUXITE_APPLE.get())));
        scene.idle(35);

        // Take it back
        Vec3 dowelSelection = util.vector().blockSurface(dowelPos, Direction.WEST);
        scene.overlay().showControls(dowelSelection, Pointing.LEFT, 15)
                .withItem(MSItems.BLACKSMITH_HAMMER.toStack());
        scene.overlay().showText(30).text("and_alchemy.text_4").attachKeyFrame().placeNearTarget()
                .pointAt(activationSelection);
        scene.idle(15);
        scene.world().modifyBlockEntity(slotPos, TotemLatheBlockEntity.class,
                be -> be.setDowel(ItemStack.EMPTY));
        scene.idle(35);
    }

    public static void Alchemiter(SceneBuilder builder, SceneBuildingUtil util) {
        PonderSceneBuilder scene = new PonderSceneBuilder(builder.getScene());
        BlockPos dowelPos = util.grid().at(4, 2, 4);
        BlockPos centerPos = util.grid().at(3, 1, 3);

        // Start
        scene.title("alchemiter", "header");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();
        scene.world().showSection(util.select().position(6, 5, 6), Direction.DOWN);
        scene.idle(5);

        // Place & describe Alchemiter
        scene.world().showIndependentSection(util.select().fromTo(1, 1, 1, 4, 2, 4), Direction.DOWN);
        scene.idle(25);

        scene.overlay().showText(40).text("alchemiter.text_1").attachKeyFrame().placeNearTarget()
                .pointAt(util.vector().blockSurface(centerPos, Direction.NORTH));
        scene.idle(60);

        // Place Dowel
        Vec3 dowelSelection = util.vector().blockSurface(dowelPos, Direction.NORTH);
        scene.overlay().showText(30).text("alchemiter.text_2").attachKeyFrame().placeNearTarget()
                .pointAt(dowelSelection);
        scene.overlay().showControls(dowelSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.carvedDowel(MSItems.CRUXITE_APPLE.get()));
        scene.idle(15);
        scene.world().modifyBlockEntity(centerPos, AlchemiterBlockEntity.class, be -> {
            be.setDowel(ESPonderPlugin.carvedDowel(MSItems.CRUXITE_APPLE.get()));
        });
        // Required to ensure the dowel renders
        scene.world().modifyBlock(dowelPos,
                state -> state.setValue(AlchemiterBlock.Pad.DOWEL, EnumDowelType.CARVED_DOWEL), false);
        scene.idle(35);

        // Print
        Vec3 centerSelection = util.vector().topOf(centerPos);
        scene.overlay().showText(40).text("alchemiter.text_3").attachKeyFrame().placeNearTarget()
                .pointAt(util.vector().blockSurface(centerPos, Direction.NORTH));
        scene.idle(15);
        scene.world().createItemEntity(centerSelection, Vec3.ZERO, MSItems.CRUXITE_APPLE.toStack());
        scene.idle(35);
    }

    public static void PunchDesignix(SceneBuilder builder, SceneBuildingUtil util) {
        PonderSceneBuilder scene = new PonderSceneBuilder(builder.getScene());
        BlockPos keyboardPos = util.grid().at(2, 2, 2);
        BlockPos slotPos = util.grid().at(3, 2, 2);

        // Start
        scene.title("punch_designix", "header");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();
        scene.world().showSection(util.select().position(6, 3, 6), Direction.DOWN);
        scene.idle(5);

        // Place & describe Punch Designix
        scene.world().showIndependentSection(util.select().fromTo(2, 1, 2, 4, 2, 3), Direction.DOWN);
        scene.idle(25);

        scene.overlay().showText(40).text("punch_designix.text_1").attachKeyFrame().placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardPos, Direction.NORTH));
        scene.idle(60);

        // Add card to punch
        Vec3 slotSelection = util.vector().blockSurface(slotPos, Direction.UP);
        scene.overlay().showText(30).text("punch_designix.text_2").attachKeyFrame().placeNearTarget()
                .pointAt(slotSelection);
        scene.overlay().showControls(slotSelection, Pointing.DOWN, 15).withItem(MSItems.CAPTCHA_CARD.toStack());
        scene.idle(15);
        scene.world().cycleBlockProperty(slotPos, PunchDesignixBlock.Slot.HAS_CARD);
        scene.idle(35);

        // Punch card
        Vec3 keyboardSelection = util.vector().centerOf(keyboardPos);
        scene.overlay().showText(30).text("punch_designix.text_3").attachKeyFrame().placeNearTarget()
                .pointAt(keyboardSelection);
        scene.idle(35);
        scene.overlay().showText(30).text("punch_designix.text_4").placeNearTarget()
                .pointAt(keyboardSelection);
        scene.overlay().showControls(keyboardSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.contentCard(MSItems.CRUXITE_APPLE.toStack()));
        scene.idle(45);

        // Retrieve punched card
        scene.overlay().showText(30).text("punch_designix.text_5").attachKeyFrame().placeNearTarget()
                .pointAt(slotSelection);
        scene.overlay().showControls(slotSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.punchedCard(MSItems.CRUXITE_APPLE.get()));
        scene.world().cycleBlockProperty(slotPos, PunchDesignixBlock.Slot.HAS_CARD);
        scene.idle(50);
    }

    public static void ORAlchemy(SceneBuilder builder, SceneBuildingUtil util) {
        PonderSceneBuilder scene = new PonderSceneBuilder(builder.getScene());
        BlockPos keyboardPos = util.grid().at(2, 2, 2);
        BlockPos slotPos = util.grid().at(3, 2, 2);

        // Start
        scene.title("or_alchemy", "header");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();
        scene.world().showSection(util.select().position(6, 3, 6), Direction.DOWN);
        scene.idle(5);

        // Place & describe Punch Designix
        scene.world().showIndependentSection(util.select().fromTo(2, 1, 2, 4, 2, 3), Direction.DOWN);
        scene.world().cycleBlockProperty(slotPos, PunchDesignixBlock.Slot.HAS_CARD);
        scene.idle(25);
        scene.overlay().showText(40).text("or_alchemy.text_1").attachKeyFrame().placeNearTarget()
                .pointAt(util.vector().blockSurface(keyboardPos, Direction.NORTH));
        scene.idle(60);

        // Punch cards
        Vec3 keyboardSelection = util.vector().centerOf(keyboardPos);
        scene.overlay().showText(40).text("or_alchemy.text_2").attachKeyFrame().placeNearTarget()
                .pointAt(keyboardSelection);
        scene.overlay().showControls(keyboardSelection, Pointing.DOWN, 15).withItem(new ItemStack(Items.STONE_SWORD));
        scene.idle(25);
        scene.overlay().showControls(keyboardSelection, Pointing.DOWN, 15).withItem(new ItemStack(Items.PAPER));
        scene.idle(35);

        // Retrieve punched card
        Vec3 slotSelection = util.vector().blockSurface(slotPos, Direction.UP);
        scene.overlay().showText(30).text("or_alchemy.text_3").attachKeyFrame().placeNearTarget()
                .pointAt(slotSelection);
        scene.overlay().showControls(slotSelection, Pointing.DOWN, 15).withItem(MSItems.PAPER_SWORD.toStack());
        scene.world().cycleBlockProperty(slotPos, PunchDesignixBlock.Slot.HAS_CARD);
        scene.idle(50);
    }

    public static void IntellibeamLaserstation(SceneBuilder builder, SceneBuildingUtil util) {
        PonderSceneBuilder scene = new PonderSceneBuilder(builder.getScene());
        BlockPos keyboardPos = util.grid().at(1, 2, 2);
        BlockPos slotPos = util.grid().at(2, 2, 2);
        BlockPos intellibeamPos = util.grid().at(4, 1, 2);

        // Start
        scene.title("intellibeam", "header");
        scene.configureBasePlate(0, 0, 6);
        scene.showBasePlate();
        scene.world().showSection(util.select().position(6, 3, 6), Direction.DOWN);
        scene.idle(5);

        // Place Punch Designix & describe unreadable
        Vec3 keyboardSelection = util.vector().blockSurface(keyboardPos, Direction.NORTH);
        scene.world().showIndependentSection(util.select().fromTo(1, 1, 2, 3, 2, 2), Direction.DOWN);
        scene.world().cycleBlockProperty(slotPos, PunchDesignixBlock.Slot.HAS_CARD);
        scene.idle(25);
        scene.overlay().showText(40).text("intellibeam.text_1").attachKeyFrame().placeNearTarget()
                .pointAt(keyboardSelection);
        scene.overlay().showControls(keyboardSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.contentCard(MSItems.CRUXITE_APPLE.toStack()));
        scene.idle(15);
        scene.overlay().showControls(keyboardSelection, Pointing.DOWN, 15)
                .showing(PonderGuiTextures.ICON_PONDER_CLOSE);
        scene.idle(45);

        // Place & describe Intellibeam Laserstation
        Vec3 intellibeamSelection = util.vector().blockSurface(intellibeamPos, Direction.UP);
        scene.world().showIndependentSection(util.select().fromTo(4, 1, 2, 4, 2, 2), Direction.DOWN);
        scene.idle(25);
        scene.overlay().showText(40).text("intellibeam.text_2").attachKeyFrame().placeNearTarget()
                .pointAt(intellibeamSelection);
        scene.idle(60);

        // Use Intellibeam Laserstation
        scene.overlay().showText(40).text("intellibeam.text_3").attachKeyFrame().placeNearTarget()
                .pointAt(intellibeamSelection);
        scene.overlay().showControls(intellibeamSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.contentCard(MSItems.CRUXITE_APPLE.toStack(), true));
        scene.idle(15);
        scene.overlay().showText(40).text("intellibeam.text_4").pointAt(intellibeamSelection);
        for (int i = 0; i < 3; i++) {
            scene.overlay().showControls(intellibeamSelection, Pointing.DOWN, 5).rightClick();
            scene.idle(10);
        }
        scene.idle(10);
        scene.overlay().showText(40).text("intellibeam.text_5").attachKeyFrame().placeNearTarget()
                .pointAt(intellibeamSelection);
        scene.overlay().showControls(intellibeamSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.contentCard(MSItems.CRUXITE_APPLE.toStack()));
        scene.idle(15);
        scene.overlay().showControls(keyboardSelection, Pointing.DOWN, 15)
                .withItem(ESPonderPlugin.contentCard(MSItems.CRUXITE_APPLE.toStack()));
        scene.idle(30);
    }
}
