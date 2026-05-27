package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.asActionResult
import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.content.block.deconstructor.DeconstructorBlockEntity
import net.minecraft.core.BlockPos

class OpContainsSubLevel : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val pos = args.getVec3(0, argc)

        val blockPos = BlockPos.containing(pos)
        val blockEntity = env.world.getBlockEntity(blockPos)

        if(blockEntity !is DeconstructorBlockEntity) return false.asActionResult;

        return blockEntity.hasSubLevel().asActionResult
    }
}