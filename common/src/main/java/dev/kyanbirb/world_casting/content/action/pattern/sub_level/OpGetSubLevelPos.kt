package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.util.getFragment
import dev.ryanhcode.sable.companion.math.JOMLConversion
import dev.ryanhcode.sable.sublevel.ServerSubLevel

class OpGetSubLevelPos : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val fragment = args.getFragment(0, argc)
        val subLevel = fragment.getSubLevel(env.world) as ServerSubLevel
        return JOMLConversion.toMojang(subLevel.logicalPose().position()).asActionResult
    }
}