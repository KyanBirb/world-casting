package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.util.SubLevelUtil
import dev.kyanbirb.world_casting.util.getFragmentOrNull

class OpProjectPosition : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val position = args.getVec3(0, argc)
        val space = args.getFragmentOrNull(1, argc)

        return SubLevelUtil.projectInto(env.world, position, space?.getSubLevel(env.world)).asActionResult
    }
}