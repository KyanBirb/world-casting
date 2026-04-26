package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import dev.kyanbirb.world_casting.content.block.deconstructor.DeconstructorBlockEntity
import dev.kyanbirb.world_casting.util.getFragment
import dev.ryanhcode.sable.sublevel.ServerSubLevel
import dev.ryanhcode.sable.sublevel.SubLevel
import net.minecraft.core.BlockPos
import net.minecraft.network.chat.Component

object OpStoreSubLevel : SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val pos = args.getVec3(1, argc)
        val fragment = args.getFragment(0, argc)

        val blockPos = BlockPos.containing(pos)
        val blockEntity = env.world.getBlockEntity(blockPos)
        val subLevel = fragment.getSubLevel(env.world)

        if(blockEntity !is DeconstructorBlockEntity) {
            throw MishapBadBlock(blockPos, Component.literal("deconstructor"))
        }

        if(blockEntity.hasSubLevel()) {
            throw MishapBadBlock(blockPos, Component.literal("empty deconstructor"))
        }

        return SpellAction.Result(
            Spell(subLevel, blockEntity),
            1,
            listOf(
                ParticleSpray(
                    blockPos.bottomCenter.add(0.0, 1.0, 0.0),
                    blockPos.above(2).center.normalize(),
                    0.0,
                    0.1
                )
            )
        )
    }

    private data class Spell(val subLevel: SubLevel, val deconstructor: DeconstructorBlockEntity): RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            deconstructor.storeSubLevel(subLevel as ServerSubLevel)
        }
    }
}