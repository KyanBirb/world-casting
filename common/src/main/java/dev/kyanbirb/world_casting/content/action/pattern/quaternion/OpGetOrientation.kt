package dev.kyanbirb.world_casting.content.action.pattern.quaternion

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.content.iota.FragmentIota
import dev.kyanbirb.world_casting.util.asActionResult
import dev.kyanbirb.world_casting.util.getFragment
import net.minecraft.util.Mth
import org.joml.Matrix3d
import org.joml.Quaterniond
import org.joml.Vector3d
import kotlin.math.cos
import kotlin.math.sin

class OpGetOrientation : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        if (args[0] is FragmentIota) {
            val fragment = args.getFragment(0, argc)
            val subLevel = fragment.getSubLevel(env.world)
            return Quaterniond(subLevel.logicalPose().orientation()).asActionResult
        } else {
            val entity = args.getEntity(env.world, 0, argc)
            val headRot = Mth.DEG_TO_RAD * entity.yHeadRot
            val right = Vector3d(cos(headRot.toDouble()), 0.0, sin(headRot).toDouble())
            val forward = Vector3d(entity.lookAngle.toVector3f())
            val up = forward.cross(right, Vector3d())

            val matrix = Matrix3d(right, up, forward)
            val quaternion = Quaterniond()
            quaternion.setFromNormalized(matrix)

            return quaternion.asActionResult
        }
    }

}