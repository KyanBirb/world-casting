package dev.kyanbirb.world_casting.util;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.math.OrientedBoundingBox3d;
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.companion.math.JOMLConversion;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaterniond;
import org.joml.Vector3d;

import java.util.List;

public class SubLevelUtil {

    public static Vec3 projectInto(Level level, Vec3 position, @Nullable SubLevel subLevel) {
        SubLevel thisSubLevel = Sable.HELPER.getContaining(level, position);
        if(thisSubLevel != subLevel) {
            if(thisSubLevel != null && subLevel != null) {
                position = thisSubLevel.logicalPose().transformPosition(position);
                position = subLevel.logicalPose().transformPositionInverse(position);
            } else {
                if(thisSubLevel != null) {
                    position = thisSubLevel.logicalPose().transformPosition(position);
                } else {
                    position = subLevel.logicalPose().transformPositionInverse(position);
                }
            }
        }

        return position;
    }

    public static double distanceToSubLevel(Vec3 pos, SubLevel subLevel) {
        Level level = subLevel.getLevel();
        Vec3 projected = projectInto(level, pos, subLevel);
        return Math.sqrt(subLevel.getPlot().getBoundingBox().toAABB().distanceToSqr(projected));
    }

    public static Iterable<BlockPos> plotIterator(SubLevel subLevel) {
        BoundingBox3ic box = subLevel.getPlot().getBoundingBox();
        if(box.equals(BoundingBox3i.EMPTY)) {
            return List.of();
        }
        return BlockPos.betweenClosed(box.minX(), box.minY(), box.minZ(), box.maxX(), box.maxY(), box.maxZ());
    }

    public static double getClosestYaw(SubLevel subLevel) {
        if(subLevel == null) return 0.0;
        Quaterniond orientation = subLevel.logicalPose().orientation();
        final double d = OrientedBoundingBox3d.UP.dot(new Vector3d(orientation.x(), orientation.y(), orientation.z()));
        return 2.0 * Math.atan2(-d, orientation.w());
    }

    public static Vec3 getVelocityAt(Level level, ServerSubLevel subLevel, Vec3 pos) {
        SubLevelPhysicsSystem system = SubLevelPhysicsSystem.get(level);
        RigidBodyHandle handle = system.getPhysicsHandle(subLevel);

        Vector3d linear = handle.getLinearVelocity(new Vector3d());
        Vector3d angular = handle.getAngularVelocity(new Vector3d());
        double dist = Sable.HELPER.distanceSquaredWithSubLevels(level, subLevel.logicalPose().position(), pos.x, pos.y, pos.z);

        return JOMLConversion.toMojang(linear.add(angular.mul(dist)));
    }
}
