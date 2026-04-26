package dev.kyanbirb.world_casting.content.action.pattern

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import dev.kyanbirb.world_casting.content.block.deconstructor.DeconstructorBlockEntity
import dev.kyanbirb.world_casting.util.asActionResult
import dev.kyanbirb.world_casting.util.getQuaternion
import dev.ryanhcode.sable.companion.math.JOMLConversion
import dev.ryanhcode.sable.companion.math.Pose3d
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component

class OpRecallSubLevel : ConstMediaAction {
    override val argc: Int
        get() = 3

    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val orientation = args.getQuaternion(2, argc)
        val spawnPos = args.getVec3(1, argc)
        val deconstructorPos = args.getVec3(0, argc)

        val blockPos = BlockPos.containing(deconstructorPos)
        val blockEntity = env.world.getBlockEntity(blockPos)

        if(blockEntity !is DeconstructorBlockEntity) {
            throw MishapBadBlock(blockPos, Component.literal("deconstructor"))
        }

        if(!blockEntity.hasSubLevel()) {
            throw MishapBadBlock(blockPos, Component.literal("non empty deconstructor"))
        }

        val initialPose = Pose3d()
        initialPose.position().set(JOMLConversion.toJOML(spawnPos))
        val subLevel = blockEntity.createSubLevel()

        val system = SubLevelPhysicsSystem.get(env.world)
        system.pipeline.teleport(subLevel, JOMLConversion.toJOML(spawnPos), orientation)

        return subLevel!!.asActionResult
    }

}