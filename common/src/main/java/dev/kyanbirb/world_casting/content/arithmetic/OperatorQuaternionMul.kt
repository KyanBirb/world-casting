package dev.kyanbirb.world_casting.content.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.all
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import dev.kyanbirb.world_casting.content.iota.QuaternionIota
import dev.kyanbirb.world_casting.index.WorldCastingIotaTypes.QUATERNION
import dev.kyanbirb.world_casting.util.asActionResult
import net.minecraft.network.chat.Component
import org.joml.Quaterniond

class OperatorQuaternionMul : OperatorBasic(2, all(ofType(QUATERNION.value()))) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val q0 = it.next().value
        val q1 = it.next().value

        if(q0 !is QuaternionIota)
            throw MishapInvalidIota(q0, 0, Component.literal("quaternion"))
        if(q1 !is QuaternionIota)
            throw MishapInvalidIota(q1, 1, Component.literal("quaternion"))

        val newQuaternion = q0.quaternion.mul(q1.quaternion, Quaterniond())
        return newQuaternion.asActionResult
    }
}