package dev.kyanbirb.world_casting.content.action.pattern

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.util.getFragment
import dev.ryanhcode.sable.sublevel.ServerSubLevel

class OpGetMass : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val fragment = args.getFragment(0, argc)
        val subLevel = fragment.getSubLevel(env.world) as ServerSubLevel
        return subLevel.massTracker.mass.asActionResult
    }
}