package dev.kyanbirb.world_casting.content.action.pattern.quaternion

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.content.iota.FragmentIota
import dev.kyanbirb.world_casting.util.asActionResult
import dev.kyanbirb.world_casting.util.getFragment
import dev.kyanbirb.world_casting.util.getQuaternion
import net.minecraft.util.Mth
import org.joml.Matrix3d
import org.joml.Quaterniond
import org.joml.Vector3d
import kotlin.math.cos
import kotlin.math.sin

class OpConjugate : ConstMediaAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val quaternion = args.getQuaternion(0, argc)
        return quaternion.conjugate(Quaterniond()).asActionResult
    }
}