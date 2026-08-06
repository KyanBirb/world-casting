package dev.kyanbirb.world_casting.events;

import at.petrak.hexcasting.api.player.Sentinel;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.ryanhcode.sable.Sable;
import dev.ryanhcode.sable.api.sublevel.SubLevelObserver;
import dev.ryanhcode.sable.sublevel.SubLevel;
import dev.ryanhcode.sable.sublevel.storage.SubLevelRemovalReason;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;

public class SentinelSubLevelObserver implements SubLevelObserver {

    private final ServerLevel level;
    public SentinelSubLevelObserver(ServerLevel level) {
        this.level = level;
    }

    @Override
    public void onSubLevelRemoved(SubLevel subLevel, SubLevelRemovalReason reason) {
        if(reason == SubLevelRemovalReason.REMOVED) {
            for (ServerPlayer player : level.players()) {
                Sentinel sentinel = IXplatAbstractions.INSTANCE.getSentinel(player);
                if(sentinel == null) continue;

                if(Sable.HELPER.getContaining(this.level, sentinel.position()) == subLevel) {
                    Vec3 projected = subLevel.logicalPose().transformPosition(sentinel.position());
                    IXplatAbstractions.INSTANCE.setSentinel(player, new Sentinel(sentinel.extendsRange(), projected, sentinel.dimension()));
                }
            }
        }
    }
}
