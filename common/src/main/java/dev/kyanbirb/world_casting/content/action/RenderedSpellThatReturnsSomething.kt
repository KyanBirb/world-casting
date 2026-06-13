package dev.kyanbirb.world_casting.content.action

import at.petrak.hexcasting.api.casting.RenderedSpell
import at.petrak.hexcasting.api.casting.eval.CastingEnvironment
import at.petrak.hexcasting.api.casting.eval.vm.CastingImage
import at.petrak.hexcasting.api.casting.iota.Iota

interface RenderedSpellThatReturnsSomething : RenderedSpell {
    override fun cast(
        env: CastingEnvironment,
        image: CastingImage
    ): CastingImage? {
        super.cast(env, image)
        val stack = ArrayList(image.stack)
        stack.addAll(getReturnValue(env, image))

        return image.copy(
            stack = stack,
            parenCount = image.parenCount,
            parenthesized = image.parenthesized,
            escapeNext = image.escapeNext,
            opsConsumed = image.opsConsumed,
            userData = image.userData
        )
    }

    fun getReturnValue(env: CastingEnvironment, image: CastingImage): List<Iota>
}