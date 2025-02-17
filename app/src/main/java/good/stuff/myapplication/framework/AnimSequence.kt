package good.stuff.myapplication.framework

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.view.View
import android.animation.TimeInterpolator
import android.view.animation.AccelerateDecelerateInterpolator

class AnimSequence {
    private val animations = mutableListOf<Pair<Long, Animator>>()

    @SuppressLint("Recycle")
    fun append(
        view: View,
        property: String,
        from: Float,
        to: Float,
        duration: Long,
        delay: Long = 0,
        interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()
    ): AnimSequence {
        val anim = ObjectAnimator.ofFloat(view, property, from, to).apply {
            this.duration = duration
            startDelay = delay
            this.interpolator = interpolator
        }
        animations.add(delay to anim)
        return this
    }

    @SuppressLint("Recycle")
    fun append(
        view: View,
        property: String,
        from: Float,
        to: Float,
        duration: Long,
        interpolator: TimeInterpolator = AccelerateDecelerateInterpolator()
    ): AnimSequence {
        val delay = 0L
        val anim = ObjectAnimator.ofFloat(view, property, from, to).apply {
            this.duration = duration
            startDelay = delay
            this.interpolator = interpolator
        }
        animations.add(delay to anim)
        return this
    }

    fun play() {
        if (animations.isNotEmpty()) {
            val groupedAnimations = animations
                .groupBy { it.first }
                .toSortedMap()

            val sequence = mutableListOf<Animator>()

            for ((_, group) in groupedAnimations) {
                AnimatorSet().apply {
                    playTogether(group.map { it.second })
                    sequence.add(this)
                }
            }

            AnimatorSet().apply {
                playSequentially(sequence)
                start()
            }
        }
    }
}
