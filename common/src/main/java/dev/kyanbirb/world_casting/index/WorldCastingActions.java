package dev.kyanbirb.world_casting.index;

import at.petrak.hexcasting.api.casting.ActionRegistryEntry;
import at.petrak.hexcasting.api.casting.castables.Action;
import at.petrak.hexcasting.api.casting.math.HexPattern;
import dev.kyanbirb.world_casting.PlatformHelper;
import dev.kyanbirb.world_casting.content.action.pattern.quaternion.*;
import dev.kyanbirb.world_casting.content.action.pattern.sub_level.*;
import dev.kyanbirb.world_casting.content.action.spell.*;
import dev.kyanbirb.world_casting.content.iota.quaternion.QuaternionIota;
import net.minecraft.core.Holder;

import static at.petrak.hexcasting.api.casting.math.HexDir.*;

public class WorldCastingActions {
    public static final Holder<ActionRegistryEntry> GET_SUB_LEVEL = make(
            "sub_level.get_containing",
            HexPattern.fromAngleString("qqaqqdee", EAST),
            new OpGetContaining()
    );

    public static final Holder<ActionRegistryEntry> GET_ENTITY_SUB_LEVEL = make(
            "sub_level.get_entity",
            HexPattern.fromAngleString("qaqqqqaq", SOUTH_WEST),
            new OpGetEntitySubLevel()
    );

    public static final Holder<ActionRegistryEntry> GET_SUB_LEVEL_RADIUS = make(
            "sub_level.get_radius",
            HexPattern.fromAngleString("eeeeewaqa", NORTH_EAST),
            new OpGetSubLevels()
    );

    public static final Holder<ActionRegistryEntry> GET_MASS = make(
            "sub_level.mass",
            HexPattern.fromAngleString("aaqawa", NORTH_WEST),
            new OpGetMass()
    );

    public static final Holder<ActionRegistryEntry> GET_GRAVITY = make(
            "sub_level.gravity",
            HexPattern.fromAngleString("aawawa", WEST),
            new OpGetGravity()
    );

    public static final Holder<ActionRegistryEntry> GET_VELOCITY = make(
            "sub_level.velocity",
            HexPattern.fromAngleString("aaqwaq", SOUTH_WEST),
            new OpGetVelocity()
    );

    public static final Holder<ActionRegistryEntry> GET_AIR_PRESSURE = make(
            "sub_level.air_pressure",
            HexPattern.fromAngleString("aaeaqq", EAST),
            new OpGetAirPressure()
    );

    public static final Holder<ActionRegistryEntry> GET_POS = make(
            "sub_level.pos",
            HexPattern.fromAngleString("qaaqdee", EAST),
            new OpGetSubLevelPos()
    );

    public static final Holder<ActionRegistryEntry> GET_MIN_BOUNDS = make(
            "sub_level.min_bounds",
            HexPattern.fromAngleString("aaeqawa", WEST),
            new OpSubLevelBounds(true)
    );

    public static final Holder<ActionRegistryEntry> GET_MAX_BOUNDS = make(
            "sub_level.max_bounds",
            HexPattern.fromAngleString("aaewaqq", EAST),
            new OpSubLevelBounds(false)
    );

    public static final Holder<ActionRegistryEntry> PROJECT_POSITION = make(
            "sub_level.project_position",
            HexPattern.fromAngleString("eeeeewqqqqq", NORTH_EAST),
            new OpProjectPosition()
    );

    public static final Holder<ActionRegistryEntry> PUSH_SUB_LEVEL = make(
            "sub_level.push",
            HexPattern.fromAngleString("qwqqqeaw", NORTH_EAST),
            OpPush.INSTANCE
    );

    public static final Holder<ActionRegistryEntry> PLACE_SUB_LEVEL = make(
            "sub_level.place",
            HexPattern.fromAngleString("qqqqqaq", SOUTH_WEST),
            OpPlaceSubLevel.INSTANCE
    );

    public static final Holder<ActionRegistryEntry> ASSEMBLE = make(
            "sub_level.assemble",
            HexPattern.fromAngleString("aqwqawedwd", WEST),
            OpAssemble.INSTANCE
    );

    public static final Holder<ActionRegistryEntry> MERGE = make(
            "sub_level.merge",
            HexPattern.fromAngleString("awaqwdewed", NORTH_EAST),
            OpMerge.INSTANCE
    );

    public static final Holder<ActionRegistryEntry> NAME = make(
            "sub_level.name",
            HexPattern.fromAngleString("qwqqqwqwded", NORTH_EAST),
            OpNameSubLevel.INSTANCE
    );

    public static final Holder<ActionRegistryEntry> QUAT_IDENTITY = make(
            "quaternion.identity",
            HexPattern.fromAngleString("aqqa", EAST),
            Action.makeConstantOp(QuaternionIota.IDENTITY)
    );

    public static final Holder<ActionRegistryEntry> ORIENTATION = make(
            "quaternion.orientation",
            HexPattern.fromAngleString("waa", EAST),
            new OpGetOrientation()
    );

    public static final Holder<ActionRegistryEntry> CONJUGATE = make(
            "quaternion.conjugate",
            HexPattern.fromAngleString("deed", EAST),
            new OpConjugate()
    );

    public static final Holder<ActionRegistryEntry> QUAT_CONSTRUCT = make(
            "quaternion.construct",
            HexPattern.fromAngleString("wwaqq", EAST),
            new OpConstructQuaternion()
    );

    public static final Holder<ActionRegistryEntry> QUAT_DECONSTRUCT = make(
            "quaternion.deconstruct",
            HexPattern.fromAngleString("qeedw", EAST),
            new OpDeconstructQuaternion()
    );

    public static final Holder<ActionRegistryEntry> QUAT_AXIS_ANGLE = make(
            "quaternion.axis_angle",
            HexPattern.fromAngleString("adqqqqqdaaww", NORTH_WEST),
            new OpAxisAngle()
    );

    private static Holder<ActionRegistryEntry> make(String id, HexPattern pattern, Action action) {
        return PlatformHelper.registerAction(id, () -> new ActionRegistryEntry(pattern, action));
    }

    public static void init() {

    }
}
