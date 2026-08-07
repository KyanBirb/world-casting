package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.ryanhcode.sable.companion.math.JOMLConversion
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysicsData

class OpGetAirPressure : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getVec3(0, argc)
        val airPressure = DimensionPhysicsData.getAirPressure(env.world, JOMLConversion.toJOML(pos))
        return airPressure.asActionResult
    }
}