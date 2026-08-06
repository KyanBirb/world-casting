package dev.kyanbirb.world_casting.events;

import dev.ryanhcode.sable.api.sublevel.SubLevelContainer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;

public class CommonEvents {
    public static void subLevelContainerReady(Level level, SubLevelContainer subLevelContainer) {
        if(level instanceof ServerLevel serverLevel) {
            subLevelContainer.addObserver(new SentinelSubLevelObserver(serverLevel));
        }
    }
}
