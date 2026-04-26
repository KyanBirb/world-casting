package dev.kyanbirb.world_casting.index;

import at.petrak.hexcasting.common.lib.HexBlocks;
import dev.kyanbirb.world_casting.PlatformHelper;
import dev.kyanbirb.world_casting.content.block.FrameBlock;
import dev.kyanbirb.world_casting.content.block.deconstructor.DeconstructorBlock;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;

public class WorldCastingBlocks {

    public static final Holder<DeconstructorBlock> DECONSTRUCTOR = PlatformHelper.registerBlockWithItem("deconstructor",
            () -> new DeconstructorBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE)));

    public static final Holder<FrameBlock> FRAME = PlatformHelper.registerBlockWithItem("frame",
            () -> new FrameBlock(BlockBehaviour.Properties.ofFullCopy(HexBlocks.EDIFIED_PLANKS)));

    public static void init() {

    }
}
