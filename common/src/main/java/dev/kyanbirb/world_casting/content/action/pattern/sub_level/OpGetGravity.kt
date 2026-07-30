package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.util.getFragment
import dev.ryanhcode.sable.companion.math.JOMLConversion
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysics
import dev.ryanhcode.sable.physics.config.dimension_physics.DimensionPhysicsData
import dev.ryanhcode.sable.sublevel.ServerSubLevel

class OpGetGravity : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getVec3(0, argc)
        val gravity = DimensionPhysicsData.getGravity(env.world, JOMLConversion.toJOML(pos))
        return JOMLConversion.toMojang(gravity).asActionResult
    }
}