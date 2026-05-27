package dev.kyanbirb.world_casting.content.action.pattern.sub_level

import at.petrak.hexcasting.api.casting.castables.ConstMediaAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getEntity
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.iota.NullIota
import dev.kyanbirb.world_casting.util.asActionResult
import dev.ryanhcode.sable.Sable

class OpGetEntitySubLevel : ConstMediaAction {
    override val argc = 1
    override fun execute(args: List<Iota>, env: CastingEnvironment): List<Iota> {
        val entity = args.getEntity(env.world, 0, argc)
        val subLevel = Sable.HELPER.getTrackingOrVehicleSubLevel(entity)
        if(subLevel != null && env.isEntityInRange(entity)) {
            return subLevel.asActionResult
        }
        return listOf(NullIota())
    }
}