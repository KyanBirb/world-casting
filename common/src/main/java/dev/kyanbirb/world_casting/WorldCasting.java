package dev.kyanbirb.world_casting;

import dev.kyanbirb.world_casting.index.*;
import dev.kyanbirb.world_casting.util.quaternions.DefaultQuaternionProvider;
import dev.kyanbirb.world_casting.util.quaternions.QuaternionProvider;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WorldCasting {
    public static final String MOD_ID = "world_casting";
    public static final String MOD_NAME = "World Casting";
    public static final Logger LOG = LoggerFactory.getLogger(MOD_NAME);
    public static QuaternionProvider QUATERNION_PROVIDER = new DefaultQuaternionProvider();

    public static void init() {
        WorldCastingBlocks.init();
        WorldCastingBlockEntityTypes.init();
        WorldCastingIotaTypes.init();
        WorldCastingActions.init();
        WorldCastingArithmetics.init();
    }

    public static ResourceLocation path(String path) {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
    }
}
