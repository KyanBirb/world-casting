package dev.kyanbirb.world_casting.content.action.pattern.quaternion

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.util.asActionResult
import org.joml.Quaterniond
import java.util.function.BiFunction

class OpQuaternionRotation(val rotationOp: BiFunction<Quaterniond, Double, Quaterniond>) : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val angle = args.getDouble(0, argc)
        val rotation = Quaterniond()
        return rotationOp.apply(rotation, angle).asActionResult
    }
}