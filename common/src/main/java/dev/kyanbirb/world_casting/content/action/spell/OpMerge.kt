package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.Mishap
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadLocation
import at.petrak.hexcasting.api.casting.mishaps.MishapInvalidIota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kyanbirb.world_casting.content.action.RenderedSpellThatReturnsSomething
import dev.kyanbirb.world_casting.util.SubLevelUtil
import dev.kyanbirb.world_casting.util.asActionResult
import dev.ryanhcode.sable.Sable
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper
import dev.ryanhcode.sable.companion.math.BoundingBox3i
import dev.ryanhcode.sable.companion.math.BoundingBox3ic
import dev.ryanhcode.sable.sublevel.ServerSubLevel
import dev.ryanhcode.sable.sublevel.SubLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.network.chat.Component
import net.minecraft.util.Mth
import net.minecraft.world.level.block.Rotation
import org.joml.Vector3d
import kotlin.math.absoluteValue
import kotlin.math.floor
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

        val cost = blocks.size + sqrt(Sable.HELPER.distanceSquaredWithSubLevels(env.world, fromPos.center, toPos.center))

        return SpellAction.Result(
            Spell(fromSubLevel as ServerSubLevel, blocks, transform),
            (MediaConstants.DUST_UNIT * cost).toLong(),
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