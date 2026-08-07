package dev.kyanbirb.world_casting.content.action.pattern.quaternion

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.common.casting.actions.math.OpConstructVec
import dev.kyanbirb.world_casting.content.mishap.MishapGeneric
import dev.kyanbirb.world_casting.util.asActionResult
import org.joml.Quaterniond

class OpConstructQuaternion : ConstMediaAction {
    override val argc: Int
        get() = 4

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val w = args.getDouble(0, OpConstructVec.argc)
        val x = args.getDouble(1, OpConstructVec.argc)
        val y = args.getDouble(2, OpConstructVec.argc)
        val z = args.getDouble(3, OpConstructVec.argc)
        val quaternion = Quaterniond(x, y, z, w)
        if(quaternion.lengthSquared() == 0.0) {
            throw MishapGeneric("expected_non_zero")
        }

        return quaternion.normalize().asActionResult
    }
}