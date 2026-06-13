package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getDouble
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.misc.MediaConstants
import dev.kyanbirb.world_casting.content.action.RenderedSpellThatReturnsSomething
import dev.kyanbirb.world_casting.util.asActionResult
import dev.ryanhcode.sable.api.SubLevelAssemblyHelper
import dev.ryanhcode.sable.companion.math.BoundingBox3i
import dev.ryanhcode.sable.sublevel.SubLevel
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component
import kotlin.math.absoluteValue
import kotlin.math.sqrt

object OpAssembleRadius : SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        val position = args.getVec3(0)
        val radius = args.getDouble(1).absoluteValue

        val center = BlockPos.containing(position)
        env.assertPosInRangeForEditing(center)
        if(env.world.getBlockState(center).isAir) {
            throw MishapBadBlock(center, Component.literal("non air"))
        }

        val toAssemble = ArrayList<BlockPos>()
        val pos = BlockPos.MutableBlockPos().set(center)

        val radiusInt = radius.toInt()
        if(radiusInt == 0) {
            toAssemble.add(pos)
        } else {
            for(z in -radiusInt..radiusInt) {
                for(y in -radiusInt..radiusInt) {
                    for(x in -radiusInt..radiusInt) {
                        pos.setWithOffset(center, x, y, z)
                        env.assertPosInRangeForEditing(pos)

                        val centerPos = pos.center
                        val distance = sqrt(center.distToCenterSqr(centerPos))
                        if(distance <= radius && !env.world.getBlockState(pos).isAir) {
                            toAssemble.add(pos.immutable())
                        }
                    }
                }
            }
        }

        return SpellAction.Result(
            Spell(toAssemble, center),
            MediaConstants.DUST_UNIT * toAssemble.size,
            listOf()
        )
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