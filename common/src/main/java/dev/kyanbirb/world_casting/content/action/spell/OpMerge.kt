package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kyanbirb.world_casting.config.CommonConfig
import dev.kyanbirb.world_casting.content.mishap.MishapInvalidDistance
import dev.kyanbirb.world_casting.util.SubLevelUtil
import dev.ryanhcode.sable.Sable
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper
import dev.ryanhcode.sable.companion.math.BoundingBox3i
import dev.ryanhcode.sable.sublevel.ServerSubLevel
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.Rotation
import kotlin.math.round
import kotlin.math.sqrt

object OpMerge : SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        val fromPos = args.getBlockPos(0)
        val toPos = args.getBlockPos(1)
        env.assertPosInRange(fromPos)
        env.assertPosInRange(toPos)

        val distance = sqrt(Sable.HELPER.distanceSquaredWithSubLevels(env.world, fromPos.center, toPos.center))
        if(distance > CommonConfig.MAX_MERGE_DISTANCE.asDouble) {
            throw MishapInvalidDistance(fromPos.center, toPos.center, CommonConfig.MAX_MERGE_DISTANCE.asDouble)
        }

        val fromSubLevel = Sable.HELPER.getContaining(env.world, fromPos)
            ?: throw MishapBadLocation(fromPos.center, "not_in_sub_level")

        val toSubLevel = Sable.HELPER.getContaining(env.world, toPos)
        if (fromSubLevel == toSubLevel) {
            throw MishapBadLocation(toPos.center, "in_same_grid")
        }

        val yaw = SubLevelUtil.getClosestYaw(toSubLevel) - SubLevelUtil.getClosestYaw(fromSubLevel)
        val turns = round(yaw / (Math.PI / 2.0)).toInt()
        val rotation = when(Math.floorMod(turns, 4)) {
            0 -> Rotation.NONE
            1 -> Rotation.COUNTERCLOCKWISE_90
            2 -> Rotation.CLOCKWISE_180
            3 -> Rotation.CLOCKWISE_90
            else -> null
        }

        val transform = SubLevelAssemblyHelper.AssemblyTransform(fromPos, toPos, turns, rotation, env.world)
        val blocks = ArrayList<BlockPos>()
        for (plotPos in SubLevelUtil.plotIterator(fromSubLevel)) {
            val resultingPos = transform.apply(plotPos.immutable())
            val plotState = env.world.getBlockState(plotPos)
            if(!plotState.isAir) {
                blocks.add(plotPos.immutable())
                if (!env.world.getBlockState(resultingPos).canBeReplaced()) {
                    throw MishapBadBlock.of(resultingPos, "replaceable")
                }
            }
        }

        return SpellAction.Result(
            Spell(fromSubLevel as ServerSubLevel, blocks, transform),
            MediaConstants.DUST_UNIT * blocks.size / 4,
            listOf()
        )
    }

    private data class Spell(val fromSubLevel: ServerSubLevel, val blocks: List<BlockPos>, val transform: SubLevelAssemblyHelper.AssemblyTransform): RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            val boundingBox = BoundingBox3i.from(blocks)
            SubLevelAssemblyHelper.moveTrackingPoints(env.world, boundingBox, fromSubLevel, transform)
            SubLevelAssemblyHelper.moveOtherStuff(env.world, transform, blocks, boundingBox)
            SubLevelAssemblyHelper.moveBlocks(env.world, transform, blocks)
        }
    }
}