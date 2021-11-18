package com.application.spevents.animation

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.core.view.isVisible

object ViewAnimation {
    fun showIn(v: View) {
        v.isVisible = true
        v.alpha = 0f
        v.translationY = v.height.toFloat()
        v.animate()
            .setDuration(200)
            .translationY(0F)
            .setListener(object : AnimatorListenerAdapter() {})
            .alpha(1f)
            .start()
    }

    fun showOut(v: View) {
        v.isVisible = true
        v.alpha = 1f
        v.translationY = 0F
        v.animate()
            .setDuration(200)
            .translationY(v.height.toFloat())
            .setListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator?) {
                    v.isVisible = false
                    super.onAnimationEnd(animation)
                }
            }).alpha(0f)
            .start()
    }

    fun rotateFab(v: View, rotate: Boolean): Boolean {
        v.animate().setDuration(200)
            .setListener(object : AnimatorListenerAdapter() {
            })
            .rotation(if (rotate) 135f else 0f)
        return rotate
    }

    fun init(v: View) {
        v.isVisible = false
        v.translationY = v.height.toFloat()
        v.alpha = 0f
    }
}