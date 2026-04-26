package dev.kyanbirb.world_casting.content.action.pattern

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import dev.kyanbirb.world_casting.util.asActionResult
import dev.ryanhcode.sable.Sable

class OpGetSubLevelAt : ConstMediaAction {
    override val argc = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getVec3(0, argc)
        val subLevel = Sable.HELPER.getContaining(env.world, pos)
        if(subLevel != null && env.isVecInRange(pos)) {
            return subLevel.asActionResult
        }
        return listOf(NullIota())
    }
}