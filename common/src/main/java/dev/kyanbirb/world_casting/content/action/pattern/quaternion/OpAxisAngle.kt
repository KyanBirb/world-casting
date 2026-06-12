package dev.kyanbirb.world_casting.content.action.pattern.quaternion

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.content.iota.FragmentIota
import dev.kyanbirb.world_casting.util.asActionResult
import dev.kyanbirb.world_casting.util.getFragment
import dev.kyanbirb.world_casting.util.getQuaternion
import net.minecraft.util.Mth
import org.joml.AxisAngle4d
import org.joml.Matrix3d
import org.joml.Quaterniond
import org.joml.Vector3d
import kotlin.math.cos
import kotlin.math.sin

class OpAxisAngle : ConstMediaAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val axis = args.getVec3(0)
        val angle = args.getDouble(1)
        val axisAngle = AxisAngle4d(angle, axis.toVector3f().normalize())
        return axisAngle.get(Quaterniond()).asActionResult
    }
}