package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import dev.kyanbirb.world_casting.util.SubLevelUtil
import dev.ryanhcode.sable.Sable
import dev.ryanhcode.sable.sublevel.ServerSubLevel

class OpGetVelocity : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getVec3(0, argc)
        val subLevel = (Sable.HELPER.getContaining(env.world, pos)?: throw MishapBadLocation(pos, "not_in_sub_level")) as ServerSubLevel
        if(subLevel.isRemoved || subLevel.massTracker.centerOfMass == null) {
            throw MishapBadLocation(pos, "not_in_sub_level")
        }

        return SubLevelUtil.getVelocityAt(env.world, subLevel, pos).asActionResult
    }
}