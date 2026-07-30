package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getBlockPos
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.Vec3Iota
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kyanbirb.world_casting.content.action.RenderedSpellThatReturnsSomething
import dev.kyanbirb.world_casting.util.asActionResult
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper
import dev.ryanhcode.sable.companion.math.BoundingBox3i
import dev.ryanhcode.sable.sublevel.SubLevel
import net.minecraft.core.BlockPos
import kotlin.math.sqrt

object OpAssemble : SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        val start = args.getBlockPos(0)
        env.assertPosInRange(start)

        var toAssemble: List<BlockPos>

        if(args[1] is Vec3Iota) {
            val end = args.getBlockPos(1)
            env.assertPosInRange(end)
            toAssemble = gatherCube(env, start, end)
        } else {
            val radius = args.getDouble(1)
            toAssemble = gatherSphere(env, start, radius)
        }

        return SpellAction.Result(
            Spell(toAssemble, start),
            MediaConstants.DUST_UNIT * toAssemble.size,
            listOf()
        )
    }

    private fun gatherSphere(
        env: CastingEnvironment,
        start: BlockPos,
        radius: Double
    ): List<BlockPos> {
        val blocks = ArrayList<BlockPos>()

        val currentPos = BlockPos.MutableBlockPos().set(start)
        val radiusInt = radius.toInt()
        if(radiusInt == 0) {
            blocks.add(currentPos)
        } else {
            for(z in -radiusInt..radiusInt) {
                for(y in -radiusInt..radiusInt) {
                    for(x in -radiusInt..radiusInt) {
                        currentPos.setWithOffset(start, x, y, z)
                        env.assertPosInRangeForEditing(currentPos)

                        val centerPos = currentPos.center
                        val distance = sqrt(start.distToCenterSqr(centerPos))
                        if(distance <= radius && !env.world.getBlockState(currentPos).isAir) {
                            blocks.add(currentPos.immutable())
                        }
                    }
                }
            }
        }

        return blocks
    }

    private fun gatherCube(
        env: CastingEnvironment,
        start: BlockPos,
        end: BlockPos
    ): List<BlockPos> {
        val blocks = ArrayList<BlockPos>()

        for (currentPos in BlockPos.betweenClosed(start, end)) {
            env.assertPosInRangeForEditing(currentPos)
            if(!env.world.getBlockState(currentPos).isAir) {
                blocks.add(currentPos.immutable())
            }
        }

        return blocks
    }

    private data class Spell(val toAssemble: List<BlockPos>, val center: BlockPos): RenderedSpellThatReturnsSomething {
        lateinit var subLevel: SubLevel

        override fun cast(env: CastingEnvironment) {
            val box = BoundingBox3i.from(toAssemble)
            subLevel = SubLevelAssemblyHelper.assembleBlocks(env.world, center, toAssemble, box)
        }

        override fun getReturnValue(
            env: CastingEnvironment,
            image: CastingImage
        ): List<Iota> {
            return subLevel.asActionResult
        }
    }
}