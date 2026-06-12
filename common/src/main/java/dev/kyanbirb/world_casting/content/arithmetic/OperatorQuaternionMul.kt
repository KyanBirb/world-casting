package dev.kyanbirb.world_casting.content.arithmetic

import at.petrak.hexcasting.api.casting.arithmetic.operator.OperatorBasic
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaMultiPredicate.*
import at.petrak.hexcasting.api.casting.arithmetic.predicates.IotaPredicate.ofType
import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.common.lib.hex.HexIotaTypes
import dev.kyanbirb.world_casting.content.iota.QuaternionIota
import dev.kyanbirb.world_casting.index.WorldCastingIotaTypes.QUATERNION
import dev.kyanbirb.world_casting.util.asActionResult
import net.minecraft.network.chat.Component
import net.minecraft.world.phys.Vec3
import org.joml.Quaterniond
import org.joml.Vector3d

class OperatorQuaternionMul : OperatorBasic(2,
    either(all(ofType(QUATERNION.value())), pair(ofType(HexIotaTypes.VEC3), ofType(QUATERNION.value())))
) {
    override fun apply(iotas: Iterable<Iota>, env: CastingEnvironment): Iterable<Iota> {
        val it = iotas.iterator().withIndex()
        val a = it.next().value
        val b = it.next().value

        if(b !is QuaternionIota)
            throw MishapInvalidIota(b, 1, Component.literal("quaternion"))

        when (a) {
            is QuaternionIota -> {
                return a.quaternion.mul(b.quaternion, Quaterniond()).asActionResult
            }

            is Vec3Iota -> {
                val vec = Vector3d(a.vec3.x, a.vec3.y, a.vec3.z)
                b.quaternion.transform(vec)
                return Vec3(vec.x, vec.y, vec.z).asActionResult
            }

            else -> {
                throw MishapInvalidIota(b, 1, Component.literal("quaternion or vector"))
            }
        }
    }
}