package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import dev.kyanbirb.world_casting.util.getFragment
import dev.ryanhcode.sable.sublevel.SubLevel
import net.minecraft.core.component.DataComponents
import net.minecraft.network.chat.Component
import net.minecraft.world.item.Items

object OpNameSubLevel : SpellAction {
    override val argc: Int
        get() = 1

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val fragment = args.getFragment(0, argc)

        val blockPos = env.castingEntity!!.blockPosition()
        val item = env.castingEntity?.getItemInHand(env.otherHand)
        if(item?.`is`(Items.NAME_TAG) == true && item.has(DataComponents.CUSTOM_NAME)) {
            val name = item.get(DataComponents.CUSTOM_NAME)

            return SpellAction.Result(
                Spell(fragment.getSubLevel(env.world), name!!.string),
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

        throw MishapBadOffhandItem(item, Component.literal("named name tag"))
    }

    private data class Spell(val subLevel: SubLevel, val name: String): RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            subLevel.name = name
        }
    }
}