package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.util.SubLevelUtil
import dev.kyanbirb.world_casting.util.asActionResult
import net.minecraft.core.BlockPos

class OpAssembleRadius : ConstMediaAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val position = args.getVec3(0)
        val radius = args.getDouble(1)

        val subLevel = SubLevelUtil.assembleRadius(env.world, BlockPos.containing(position), radius.toInt())
        return subLevel.asActionResult
    }
}