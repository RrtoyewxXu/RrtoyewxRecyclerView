package com.rrtoyewx.recyclerviewlibrary.utils;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

/**
 * Created by Rrtoyewx on 16/8/18.
 * AnimationUtil
 */
public class AnimationUtil {

    public static Animator generateRotateAnimation(View targetView, long duration, float... values) {
        ObjectAnimator rotateAnimator = ObjectAnimator.ofFloat(targetView, View.ROTATION, values);
        rotateAnimator.setDuration(duration);
        return rotateAnimator;
    }
}
