package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import dev.kyanbirb.world_casting.util.getFragment
import dev.ryanhcode.sable.api.physics.handle.RigidBodyHandle
import dev.ryanhcode.sable.companion.math.JOMLConversion
import dev.ryanhcode.sable.sublevel.ServerSubLevel
import dev.ryanhcode.sable.sublevel.system.SubLevelPhysicsSystem
import org.joml.Vector3d

object OpImpulseSubLevel : SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(args: List<Iota>, env: CastingEnvironment): SpellAction.Result {
        val fragment = args.getFragment(0, argc)
        val impulse = args.getVec3(1, argc)

        val subLevel = fragment.getSubLevel(env.world)
        val physicsSystem = SubLevelPhysicsSystem.get(env.world)
        val handle = physicsSystem.getPhysicsHandle(subLevel as ServerSubLevel)
        val vector3d = Vector3d(impulse.toVector3f())
        subLevel.logicalPose().transformNormalInverse(vector3d)

        val particlePos = JOMLConversion.toMojang(subLevel.logicalPose().position())

        return SpellAction.Result(
            Spell(handle, vector3d),
            1,
            listOf(
                ParticleSpray(
                    particlePos,
                    impulse.normalize(),
                    0.0,
                    0.1
                )
            )
        )
    }

    private data class Spell(val handle: RigidBodyHandle, val impulse: Vector3d) : RenderedSpell {
        override fun cast(env: CastingEnvironment) {
            handle.applyLinearImpulse(impulse)
        }
    }

}