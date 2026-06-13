package dev.kyanbirb.world_casting.content.action.spell

import at.petrak.hexcasting.api.casting.ParticleSpray
import at.petrak.hexcasting.api.casting.castables.SpellAction
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.getVec3
import at.petrak.hexcasting.api.casting.iota.Iota
import at.petrak.hexcasting.api.casting.mishaps.MishapBadBlock
import at.petrak.hexcasting.api.casting.mishaps.MishapBadOffhandItem
import at.petrak.hexcasting.api.misc.MediaConstants
import at.petrak.hexcasting.ktxt.UseOnContext
import at.petrak.hexcasting.xplat.IXplatAbstractions
import dev.kyanbirb.world_casting.content.action.RenderedSpellThatReturnsSomething
import dev.kyanbirb.world_casting.util.asActionResult
import dev.kyanbirb.world_casting.util.getQuaternion
import dev.ryanhcode.sable.api.sublevel.SubLevelContainer
import dev.ryanhcode.sable.companion.math.Pose3d
import dev.ryanhcode.sable.sublevel.SubLevel
import net.minecraft.core.BlockPos
import net.minecraft.core.Direction
import net.minecraft.core.particles.BlockParticleOption
import net.minecraft.core.particles.ParticleTypes
import net.minecraft.server.level.ServerPlayer
import net.minecraft.sounds.SoundSource
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BlockItem
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.context.BlockPlaceContext
import net.minecraft.world.phys.BlockHitResult
import net.minecraft.world.phys.Vec3
import org.joml.Quaterniondc

object OpPlaceSubLevel : SpellAction {
    override val argc: Int
        get() = 2

    override fun execute(
        args: List<Iota>,
        env: CastingEnvironment
    ): SpellAction.Result {
        val position = args.getVec3(0, argc)
        val orientation = args.getQuaternion(1, argc)

        val blockPos = BlockPos.containing(position)
        env.assertPosInRangeForEditing(blockPos)

        val blockHit = BlockHitResult(
            Vec3.atCenterOf(blockPos), env.castingEntity?.direction ?: Direction.NORTH, blockPos, false
        )
        val itemUseCtx = env
            .queryForMatchingStack { it.item is BlockItem }
            ?.let {
                UseOnContext(
                    env.world,
                    env.castingEntity as? ServerPlayer,
                    env.castingHand,
                    it,
                    blockHit
                )
            }
            ?: throw MishapBadOffhandItem.of(ItemStack.EMPTY, "placeable")
        val placeContext = BlockPlaceContext(itemUseCtx)

        val worldState = env.world.getBlockState(blockPos)
        if (!worldState.canBeReplaced(placeContext))
            throw MishapBadBlock.of(blockPos, "replaceable")

        return SpellAction.Result(
            Spell(position, orientation),
            MediaConstants.DUST_UNIT / 8,
            listOf(ParticleSpray.cloud(Vec3.atCenterOf(blockPos), 1.0))
        )
    }

    private data class Spell(val placePos: Vec3, val orientation: Quaterniondc) : RenderedSpellThatReturnsSomething {
        lateinit var subLevel: SubLevel
        override fun cast(env: CastingEnvironment) {
            val caster = env.castingEntity

            val container = SubLevelContainer.getContainer(env.world)
            val pose = Pose3d()
            pose.position().set(placePos.x, placePos.y, placePos.z)
            pose.orientation().set(orientation)

            subLevel = container!!.allocateNewSubLevel(pose)
            val plot = subLevel.plot
            plot.newEmptyChunk(plot.centerChunk)

            val blockPos = plot.centerBlock
            val blockHit = BlockHitResult(
                Vec3.atCenterOf(blockPos), caster?.direction ?: Direction.NORTH, blockPos, false
            )

            val blockState = env.world.getBlockState(blockPos)
            val placeStack = env.queryForMatchingStack { it.item is BlockItem }
            if (placeStack != null) {
                if (!IXplatAbstractions.INSTANCE.isPlacingAllowed(env.world, blockPos, placeStack, caster as? ServerPlayer))
                    return

                if (!placeStack.isEmpty) {
                    // https://github.com/VazkiiMods/Psi/blob/master/src/main/java/vazkii/psi/common/spell/trick/block/PieceTrickPlaceBlock.java#L143
                    val spoofedStack = placeStack.copy()

                    // we temporarily give the player the stack, place it using mc code, then give them the old stack back.
                    spoofedStack.count = 1

                    val itemUseCtx =
                        UseOnContext(
                            env.world,
                            caster as? ServerPlayer,
                            env.otherHand,
                            spoofedStack,
                            blockHit
                        )
                    val placeContext = BlockPlaceContext(itemUseCtx)
                    if (blockState.canBeReplaced(placeContext)) {
                        if (env.withdrawItem({ ItemStack.isSameItemSameComponents(it, placeStack) }, 1, false)) {
                            val res = spoofedStack.useOn(placeContext)

                            if (res != InteractionResult.FAIL) {
                                env.withdrawItem({ ItemStack.isSameItemSameComponents(it, placeStack) }, 1, true)

                                env.world.playSound(
                                    caster as? ServerPlayer,
                                    blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(),
                                    blockState.soundType.placeSound, SoundSource.BLOCKS, 1.0f,
                                    1.0f + (Math.random() * 0.5 - 0.25).toFloat()
                                )
                                val particle = BlockParticleOption(ParticleTypes.BLOCK, blockState)
                                env.world.sendParticles(
                                    particle, blockPos.x.toDouble(), blockPos.y.toDouble(), blockPos.z.toDouble(),
                                    4, 0.1, 0.2, 0.1, 0.1
                                )
                            }
                        }
                    }
                }
            }
        }

        override fun getReturnValue(
            env: CastingEnvironment,
            image: CastingImage
        ): List<Iota> {
            return subLevel.asActionResult
        }
    }

}