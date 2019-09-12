package com.dgr.squarekick.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View

fun View.show(animate: Boolean) {
    if (animate) {
        animationShow(this)
    } else {
        visibility = View.VISIBLE
    }
}

fun View.hide(animate: Boolean) {
    if (animate) {
        animationHide(this)
    } else {
        visibility = View.GONE
    }
}


fun animationHide(view: View) {
    view.animate().alpha(0f).setDuration(1500).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            super.onAnimationEnd(animation)
            view.visibility = View.GONE
        }
    })
}

fun animationShow(view: View) {
    view.animate().alpha(1f).setDuration(1500).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationStart(animation: Animator) {
            super.onAnimationStart(animation)
            view.visibility = View.VISIBLE
        }
    })
}

