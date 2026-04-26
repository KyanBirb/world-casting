package dev.kyanbirb.world_casting.index;

import dev.kyanbirb.world_casting.PlatformHelper;
import dev.kyanbirb.world_casting.content.block.deconstructor.DeconstructorBlockEntity;
import net.minecraft.core.Holder;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class WorldCastingBlockEntityTypes {

    public static final Holder<BlockEntityType<DeconstructorBlockEntity>> DECONSTRUCTOR = PlatformHelper.registerBlockEntityType(
            "deconstructor", DeconstructorBlockEntity::new, WorldCastingBlocks.DECONSTRUCTOR);

    public static void init() {

    }

}
