package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getPositiveDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.util.asActionResult
import dev.ryanhcode.sable.Sable
import dev.ryanhcode.sable.companion.math.BoundingBox3d
import dev.ryanhcode.sable.companion.math.JOMLConversion
import net.minecraft.core.Position

class OpGetSubLevels : ConstMediaAction {
    override val argc = 2
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getVec3(0, argc)
        val radius = args.getPositiveDouble(1, argc)
        env.assertVecInRange(pos)

        val min = pos.subtract(radius, radius, radius) as Position
        val max = pos.add(radius, radius, radius) as Position

        val box = BoundingBox3d(min, max)
        val subLevels = Sable.HELPER.getAllIntersecting(env.world, box).sortedBy {
            Sable.HELPER.distanceSquaredWithSubLevels(env.world, JOMLConversion.toJOML(pos), it.logicalPose().position())
        }

        return subLevels.asActionResult
    }
}