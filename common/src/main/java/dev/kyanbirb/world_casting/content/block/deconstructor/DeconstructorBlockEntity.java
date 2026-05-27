package dev.kyanbirb.world_casting.content.block.deconstructor;

import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import dev.kyanbirb.world_casting.config.CommonConfig;
import dev.kyanbirb.world_casting.index.WorldCastingBlockEntityTypes;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.companion.math.BoundingBox3i;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysics;
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysicsData;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import dev.ryanhcode.sable.sublevel.plot.ServerLevelPlot;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static dev.kyanbirb.world_casting.WorldCasting.path;

public class DeconstructorBlockEntity extends BlockEntity {
    private static final TagKey<Block> FRAME_TAG = TagKey.create(Registries.BLOCK, path("valid_frame"));

    public DeconstructorBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(WorldCastingBlockEntityTypes.DECONSTRUCTOR.value(), pPos, pBlockState);
    }

    public @Nullable AABB getFrame(BlockPos start) {
        BlockPos minCorner = findCorner(start, new BlockPos(-1, -1, -1));
        if(minCorner == null) return null;

        BlockPos maxCorner = findCorner(start, new BlockPos(1, 1, 1));
        if(maxCorner == null) return null;

        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();

        int minX = minCorner.getX();
        int minY = minCorner.getY();
        int minZ = minCorner.getZ();
        int maxX = maxCorner.getX();
        int maxY = maxCorner.getY();
        int maxZ = maxCorner.getZ();

        int maxWidth = CommonConfig.FRAME_MAX_WIDTH.getAsInt();
        int maxHeight = CommonConfig.FRAME_MAX_HEIGHT.getAsInt();
        int maxLength = CommonConfig.FRAME_MAX_LENGTH.getAsInt();

        int width = maxX - minX;
        int height = maxY - minY;
        int length = maxZ - minZ;


        if(width < 1 || height < 1 || length < 1) return null;
        if(width > maxWidth || height > maxHeight || length > maxLength) return null;

        // check if start is actually part of the frame
        if(!((startX == minX || startX == maxX) ||
                (startY == minY || startY == maxY) ||
                (startZ == minZ || startZ == maxZ))) {
            return null;
        }

        // check every block in the frame
        for (int i = 0; i < width; i++) {
            if(!(
                    isFrame(minX + i, minY, minZ) &&
                    isFrame(minX + i, minY + height, minZ) &&
                    isFrame(minX + i, minY, minZ + length) &&
                    isFrame(minX + i, minY + height, minZ + length)
            ))
                return null;
        }

        for (int i = 0; i < height; i++) {
            if(!(
                    isFrame(minX, minY + i, minZ) &&
                    isFrame(minX + width, minY + i, minZ) &&
                    isFrame(minX, minY + i, minZ + length) &&
                    isFrame(minX + width, minY + i, minZ + length)
            ))
                return null;
        }

        for (int i = 0; i < length; i++) {
            if(!(
                    isFrame(minX, minY, minZ + i) &&
                    isFrame(minX + width, minY, minZ + i) &&
                    isFrame(minX, minY + height, minZ + i) &&
                    isFrame(minX + width, minY + height, minZ + i)
            ))
                return null;
        }

        return new AABB(minX + 1, minY + 1, minZ + 1, maxX, maxY, maxZ);
    }

    public @Nullable BlockPos findCorner(BlockPos start, BlockPos dir) {
        int xDir = dir.getX();
        int yDir = dir.getY();
        int zDir = dir.getZ();
        int startX = start.getX();
        int startY = start.getY();
        int startZ = start.getZ();

        // Not inside frame
        if(!isFrame(start)) {
            return null;
        }

        // Already at corner
        if(     !isFrame(startX + xDir, startY, startZ) &&
                !isFrame(startX, startY + yDir, startZ) &&
                !isFrame(startX, startY, startZ + zDir)) {
            return start;
        }

        boolean foundX = false;
        boolean foundY = false;
        boolean foundZ = false;

        BlockPos.MutableBlockPos current = start.mutable();

        int limit = 100;
        int count = 0;

        while ((!foundX || !foundY || !foundZ)) {
            if(count >= limit) break;

            // Cannot go further
            if(     !isFrame(current.offset(xDir, 0, 0)) &&
                    !isFrame(current.offset(0, yDir, 0)) &&
                    !isFrame(current.offset(0, 0, zDir))) {
                break;
            }

            while (!foundX && isFrame(current.offset(xDir, 0, 0))) {
                if(count >= limit) break;

                current.setWithOffset(current, xDir, 0, 0);
                if(!isFrame(current.offset(xDir, 0, 0))) {
                    foundX = true;
                }

                count++;
            }

            while (!foundY && isFrame(current.offset(0, yDir, 0))) {
                if(count >= limit) break;

                current.setWithOffset(current, 0, yDir, 0);
                if(!isFrame(current.offset(0, yDir, 0))) {
                    foundY = true;
                }

                count++;
            }

            while (!foundZ && isFrame(current.offset(0, 0, zDir))) {
                if(count >= limit) break;

                current.setWithOffset(current, 0, 0, zDir);
                if(!isFrame(current.offset(0, 0, zDir))) {
                    foundZ = true;
                }

                count++;
            }

            count++;
        }

        if(count >= limit) {
            return null;
        }

        return current;
    }

    public boolean isFrame(BlockPos blockPos) {
        return isFrame(getBlock(blockPos));
    }

    public boolean isFrame(int x, int y, int z) {
        return isFrame(getBlock(x, y, z));
    }

    public boolean isFrame(BlockState state) {
        return state.is(FRAME_TAG);
    }

    public BlockState getBlock(int x, int y, int z) {
        return getBlock(new BlockPos(x, y, z));
    }

    public BlockState getBlock(BlockPos blockPos) {
        return level.getBlockState(blockPos);
    }

    public void storeSubLevel(ServerSubLevel subLevel) {
        DimensionPhysics dimensionPhysics = DimensionPhysicsData.of(subLevel.getLevel());
        JsonElement orThrow = DimensionPhysics.CODEC.encodeStart(JsonOps.INSTANCE, dimensionPhysics).getOrThrow();
        System.out.println(orThrow);

        AABB frame = getFrame(getBlockPos());
        if(frame == null) return;
        if(!(getLevel() instanceof ServerLevel serverLevel)) return;
        ServerLevelPlot plot = subLevel.getPlot();
        BoundingBox3ic plotBoundingBox = plot.getBoundingBox();
        Vector3i centerPos = plotBoundingBox.center(new Vector3i());
        BlockPos centerBlock = new BlockPos(centerPos.x, centerPos.y, centerPos.z);


        if(frame.getXsize() < plotBoundingBox.width() || frame.getYsize() < plotBoundingBox.height() || frame.getZsize() < plotBoundingBox.length()) return;

        List<BlockPos> blocks = new ArrayList<>();
        Iterator<BlockPos> iterator = BlockPos.betweenClosedStream(plotBoundingBox.toMojang()).iterator();
        while (iterator.hasNext()) {
            BlockPos next = iterator.next();
            if(!level.getBlockState(next).isAir()) {
                blocks.add(next.immutable());
            }
        }

        int xOff = (((int) frame.getXsize() & 1) == 0) ? 1 : 0;
        int yOff = (((int) frame.getYsize() & 1) == 0) ? 1 : 0;
        int zOff = (((int) frame.getZsize() & 1) == 0) ? 1 : 0;


        SubLevelAssemblyHelper.AssemblyTransform transform = new SubLevelAssemblyHelper.AssemblyTransform(centerBlock,
                BlockPos.containing(frame.getCenter().subtract(xOff, yOff, zOff)), 0, Rotation.NONE, serverLevel);

        SubLevelAssemblyHelper.moveBlocks(serverLevel, transform, blocks);
        SubLevelAssemblyHelper.moveOtherStuff(serverLevel, transform, blocks, plotBoundingBox);
        SubLevelAssemblyHelper.moveTrackingPoints(serverLevel, plotBoundingBox, subLevel, transform);

    }

    public boolean hasSubLevel() {
        AABB frame = getFrame(getBlockPos());
        if(frame == null) return false;

        List<BlockPos> list = new ArrayList<>();
        Iterable<BlockPos> iterable = BlockPos.betweenClosed((int) frame.minX, (int) frame.minY, (int) frame.minZ, (int) frame.maxX - 1, (int) frame.maxY - 1, (int) frame.maxZ - 1);
        for (BlockPos pos : iterable) {
            if(!level.getBlockState(pos).isAir()) {
                list.add(pos.immutable());
            }
        }

        return !list.isEmpty();
    }

    public @Nullable ServerSubLevel createSubLevel() {
        AABB frame = getFrame(getBlockPos());
        if(frame == null) return null;

        List<BlockPos> list = new ArrayList<>();
        Iterable<BlockPos> iterable = BlockPos.betweenClosed((int) frame.minX, (int) frame.minY, (int) frame.minZ, (int) frame.maxX - 1, (int) frame.maxY - 1, (int) frame.maxZ - 1);
        for (BlockPos pos : iterable) {
            if(!level.getBlockState(pos).isAir()) {
                list.add(pos.immutable());
            }
        }

        if(list.isEmpty()) return null;

        return SubLevelAssemblyHelper.assembleBlocks((ServerLevel) getLevel(), BlockPos.containing(frame.getCenter()), list, new BoundingBox3i((int) frame.minX + 1, (int) frame.minY + 1, (int) frame.minZ + 1, (int) frame.maxX - 2, (int) frame.maxY - 2, (int) frame.maxZ - 2));
    }

}
