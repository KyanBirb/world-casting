package dev.kyanbirb.world_casting.mixin.sable_compat;

import at.petrak.hexcasting.api.player.Sentinel;
import at.petrak.hexcasting.xplat.IXplatAbstractions;
import dev.kyanbirb.world_casting.config.CommonConfig;
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper;
import dev.ryanhcode.sable.companion.math.BoundingBox3ic;
import dev.ryanhcode.sable.sublevel.ServerSubLevel;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SubLevelAssemblyHelper.class)
public class SubLevelAssemblyHelperMixin {

    // FIXME may not always be correct, unsure
    @Inject(method = "moveTrackingPoints", at = @At("TAIL"))
    private static void world_casting$moveOtherStuff(ServerLevel level, BoundingBox3ic bounds, ServerSubLevel subLevel, SubLevelAssemblyHelper.AssemblyTransform transform, CallbackInfo ci) {
        if(!CommonConfig.SENTINEL_FOLLOWS_SUB_LEVELS.getAsBoolean()) return;
        for (ServerPlayer player : level.players()) {
            Sentinel sentinel = IXplatAbstractions.INSTANCE.getSentinel(player);
            if(sentinel == null) continue;

            Vec3 position = sentinel.position();
            AABB aabb = bounds.toAABB().expandTowards(1, 1, 1);
            if(aabb.contains(position.x(), position.y(), position.z())) {
                Vec3 newPosition = transform.apply(position);
                IXplatAbstractions.INSTANCE.setSentinel(player, new Sentinel(sentinel.extendsRange(), newPosition, sentinel.dimension()));
            }
        }
    }

}
