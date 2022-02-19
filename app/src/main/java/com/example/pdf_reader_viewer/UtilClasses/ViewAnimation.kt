package com.example.pdf_reader_viewer.UtilClasses

import android.animation.Animator

import android.animation.AnimatorListenerAdapter
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar


class ViewAnimation {

    companion object {
        fun showIn(v: View) {

            v.setVisibility(View.VISIBLE)
            v.setAlpha(0f)
            v.setTranslationY(v.getHeight().toFloat())

            v.animate().setDuration(300)
                .translationY(0f).setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        v.visibility=View.VISIBLE
                        super.onAnimationEnd(animation)
                    }
                }).alpha(1f).start()
        }
        fun showOut(v: View) {
          //  v.setVisibility(View.VISIBLE)
           // v.setAlpha(1f)
          //  v.setTranslationY(0f)
            v.animate().setDuration(200).translationY(v.getHeight().toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        v.setVisibility(View.GONE)
                        super.onAnimationEnd(animation)
                    }
                }).alpha(0f)
                .start()
        }

        fun init(v: View) {

            v.setVisibility(View.GONE)
            v.setTranslationY(v.getHeight().toFloat())
            v.setAlpha(0f)
        }
    }
}