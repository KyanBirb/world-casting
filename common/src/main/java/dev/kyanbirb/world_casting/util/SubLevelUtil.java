package dev.kyanbirb.world_casting.util;

import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.api.sublevel.ServerSubLevelContainer;
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.Pose3d;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.plot.LevelPlot;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
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

    public static @Nullable ServerSubLevel createSingleBlock(ServerLevel level, BlockState state, Pose3d pose3d) {
        ServerSubLevelContainer container = SubLevelContainer.getContainer(level);

        SubLevel subLevel = container.allocateNewSubLevel(pose3d);
        LevelPlot plot = subLevel.getPlot();
        plot.newEmptyChunk(plot.getCenterChunk());

        if(state.canSurvive(level, plot.getCenterBlock())) {
            level.setBlock(plot.getCenterBlock(), state,2);
            return (ServerSubLevel) subLevel;
        }

        return null;
    }

    public static ServerSubLevel assembleRadius(ServerLevel level, BlockPos center, int radius) {
        List<BlockPos> toAssemble = new ArrayList<>();


        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos().set(center);
        for (int z = -radius; z < radius; z++) {
            for (int y = -radius; y < radius; y++) {
                for (int x = -radius; x < radius; x++) {
                    pos.setWithOffset(center, x, y, z);
                    Vec3 centerPos = pos.getCenter();
                    double distance = center.distToCenterSqr(centerPos);
                    if(distance <= radius) {
                        toAssemble.add(pos.immutable());
                    }
                }
            }
        }

        BlockPos min = new BlockPos(-radius, -radius, -radius);
        BlockPos max = new BlockPos(radius, radius, radius);

        return SubLevelAssemblyHelper.assembleBlocks(level, center, toAssemble, new BoundingBox3i(min, max));
    }
}
