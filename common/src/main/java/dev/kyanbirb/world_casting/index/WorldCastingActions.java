package dev.kyanbirb.world_casting.index;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import dev.kyanbirb.world_casting.PlatformHelper;
import dev.kyanbirb.world_casting.content.action.pattern.*;
import dev.kyanbirb.world_casting.content.action.spell.OpImpulseSubLevel;
import dev.kyanbirb.world_casting.content.action.spell.OpNameSubLevel;
import dev.kyanbirb.world_casting.content.action.spell.OpStoreSubLevel;
import dev.kyanbirb.world_casting.content.iota.QuaternionIota;
import net.minecraft.core.Holder;
import org.joml.Quaterniond;

import static at.petrak.hexcasting.api.casting.math.HexDir.*;

public class WorldCastingActions {
    public static final Holder<ActionRegistryEntry> GET_SUB_LEVEL = make(
            "sub_level.get_containing",
            HexPattern.fromAngles("edeeeede", SOUTH_EAST),
            new OpGetContaining()
    );

    public static final Holder<ActionRegistryEntry> GET_ENTITY_SUB_LEVEL = make(
            "sub_level.get_entity",
            HexPattern.fromAngles("qaqqqqaq", SOUTH_WEST),
            new OpGetEntitySubLevel()
    );

    public static final Holder<ActionRegistryEntry> GET_SUB_LEVEL_RADIUS = make(
            "sub_level.get_radius",
            HexPattern.fromAngles("eeeeewaqa", NORTH_EAST),
            new OpGetSubLevels()
    );

    public static final Holder<ActionRegistryEntry> GET_MASS = make(
            "sub_level.mass",
            HexPattern.fromAngles("waawaaw", NORTH_WEST),
            new OpGetMass()
    );

    public static final Holder<ActionRegistryEntry> GET_POS = make(
            "sub_level.pos",
            HexPattern.fromAngles("qaaqdee", EAST),
            new OpGetSubLevelPos()
    );

    public static final Holder<ActionRegistryEntry> PROJECT_POSITION = make(
            "sub_level.project_position",
            HexPattern.fromAngles("eeeeewqqqqq", NORTH_EAST),
            new OpProjectPosition()
    );

    public static final Holder<ActionRegistryEntry> IMPULSE_SUB_LEVEL = make(
            "sub_level.impulse",
            HexPattern.fromAngles("dweeewdew", NORTH_WEST),
            OpImpulseSubLevel.INSTANCE
    );

    public static final Holder<ActionRegistryEntry> ASSEMBLE_RADIUS = make(
            "sub_level.assemble_radius",
            HexPattern.fromAngles("aqwqawedwd", WEST),
            new OpAssembleRadius()
    );

    public static final Holder<ActionRegistryEntry> NAME = make(
            "sub_level.name",
            HexPattern.fromAngles("qwqqqwqwded", NORTH_EAST),
            OpNameSubLevel.INSTANCE
    );

    public static final Holder<ActionRegistryEntry> STORE_SUB_LEVEL = make(
            "sub_level.store",
            HexPattern.fromAngles("edade", NORTH_WEST),
            OpStoreSubLevel.INSTANCE
    );

    public static final Holder<ActionRegistryEntry> RECALL_SUB_LEVEL = make(
            "sub_level.recall",
            HexPattern.fromAngles("qadaq", NORTH_EAST),
            new OpRecallSubLevel()
    );

    public static final Holder<ActionRegistryEntry> CONTAINS_SUB_LEVEL = make(
            "sub_level.contains",
            HexPattern.fromAngles("edadee", NORTH_WEST),
            new OpContainsSubLevel()
    );

    public static final Holder<ActionRegistryEntry> QUAT_IDENTITY = make(
            "quaternion.identity",
            HexPattern.fromAngles("aqqa", EAST),
            Action.makeConstantOp(QuaternionIota.IDENTITY)
    );

    public static final Holder<ActionRegistryEntry> ORIENTATION = make(
            "quaternion.orientation",
            HexPattern.fromAngles("waa", EAST),
            new OpGetOrientation()
    );

    public static final Holder<ActionRegistryEntry> QUAT_ROTATION_X = make(
            "quaternion.rotation_x",
            HexPattern.fromAngles("qqawwa", NORTH_WEST),
            new OpQuaternionRotation(Quaterniond::rotationX)
    );

    public static final Holder<ActionRegistryEntry> QUAT_ROTATION_Y = make(
            "quaternion.rotation_y",
            HexPattern.fromAngles("qqawww", NORTH_WEST),
            new OpQuaternionRotation(Quaterniond::rotationY)
    );

    public static final Holder<ActionRegistryEntry> QUAT_ROTATION_Z = make(
            "quaternion.rotation_z",
            HexPattern.fromAngles("qqawwd", NORTH_WEST),
            new OpQuaternionRotation(Quaterniond::rotationZ)
    );

    private static Holder<ActionRegistryEntry> make(String id, HexPattern pattern, Action action) {
        return PlatformHelper.registerAction(id, () -> new ActionRegistryEntry(pattern, action));
    }

    public static void init() {

    }
}
