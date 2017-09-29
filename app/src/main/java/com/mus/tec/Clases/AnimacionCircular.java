package com.mus.tec.Clases;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;

/**
 * Created by andres on 28/09/17.
 */

public class AnimacionCircular {
    public static final String EXTRA_CIRCULAR_REVEAL_X = "EXTRA_CIRCULAR_REVEAL_X";
    public static final String EXTRA_CIRCULAR_REVEAL_Y = "EXTRA_CIRCULAR_REVEAL_Y";

    private final View mView;
    private Activity mActivity;

    private int revealX;
    private int revealY;

    public AnimacionCircular(View view, Intent intent, Activity activity) {
        mView = view;
        mActivity = activity;

        // Condición para ejecutar en API < 21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_X) &&
                intent.hasExtra(EXTRA_CIRCULAR_REVEAL_Y)) {
            view.setVisibility(View.INVISIBLE);

            revealX = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_X, 0);
            revealY = intent.getIntExtra(EXTRA_CIRCULAR_REVEAL_Y, 0);

            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        ActividadRevelada(revealX, revealY);
                        mView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {

            // Acción sin animación
            view.setVisibility(View.VISIBLE);
        }
    }

    public void ActividadRevelada(int x, int y) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius = (float) (Math.max(mView.getWidth(), mView.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(mView, x, y, 0, finalRadius);
            circularReveal.setDuration(900); // Duración < - - - - -
            circularReveal.setInterpolator(new AccelerateInterpolator());
            mView.setVisibility(View.VISIBLE);
            circularReveal.start();
        } else {
            mActivity.finish();
        }
    }

    public void ActividadReveladaInversamente() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            mActivity.finish();
        } else {
            float finalRadius = (float) (Math.max(mView.getWidth(), mView.getHeight()) * 1.1);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                    mView, revealX, revealY, finalRadius, 0);

            circularReveal.setDuration(900);    // Duración < - - - - -
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mView.setVisibility(View.INVISIBLE);
                    mActivity.finish();
                    mActivity.overridePendingTransition(0, 0);
                }
            });

            circularReveal.start();
        }
    }

}


