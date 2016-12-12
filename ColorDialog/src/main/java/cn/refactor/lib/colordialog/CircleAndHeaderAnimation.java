package cn.refactor.lib.colordialog;

import android.animation.Animator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.FrameLayout;
import android.widget.ImageView;

/**
 * Created by aomel on 01/12/2016.
 */

public class CircleAndHeaderAnimation extends Animation implements Animation.AnimationListener {

    private final static float CIRCLE_DEGREE_ROTATION = -360f;
    private OutterCircle mOutterCircle;
    private int mWidth;
    private int mHeight;
    private int mStrokeDP;
    private float mBigStartAngle;
    private float mBigToangle;
    private float mSmallStartAngle;
    private float mSmallToangle;
    private float newbigstart;
    private float newbigsweep;
    private float newsmallstart;
    private float newsmallsweep;
    private float bigstart, bigsweep, smallstart, smallsweep;
    private Animator headerAnimation;
    private Animator triangleAnimation;
    private FrameLayout mHeaderFrame;
    private ImageView mTriangleIv;

    public CircleAndHeaderAnimation(OutterCircle outterCircle) {
        mBigStartAngle = 40;
        mSmallStartAngle = 50;
        mBigToangle = -260;
        mSmallToangle = 340 - Math.abs(mBigToangle);
        mOutterCircle = outterCircle;
        setDuration(600);
        newbigstart = 77;
        newbigsweep = -334;
        newsmallstart = 85;
        newsmallsweep = 10;
        bigstart = 0;
        bigsweep = 0;
        smallstart = 0;
        smallsweep = 0;
    }

    @Override protected void applyTransformation(float interpolatedTime, Transformation transformation) {
        if (interpolatedTime <= 0.5) {  // close small circle arc
            bigstart = mBigStartAngle + (newbigstart - mBigStartAngle) * ((interpolatedTime) / 0.5f);
            bigsweep = mBigToangle + (newbigsweep - mBigToangle) * ((interpolatedTime) / 0.5f);
            smallstart = mSmallStartAngle + (newsmallstart - mSmallStartAngle) * ((interpolatedTime) / 0.5f);
            smallsweep = mSmallToangle + (newsmallsweep - mSmallToangle) * ((interpolatedTime) / 0.5f);
        } else if (interpolatedTime > 0.5) {
            bigstart = newbigstart + (mBigStartAngle - newbigstart) * ((interpolatedTime - 0.5f) / 0.5f);
            bigsweep = newbigsweep + (mBigToangle - newbigsweep) * ((interpolatedTime - 0.5f) / 0.5f);
            smallstart = newsmallstart + (mSmallStartAngle - newsmallstart) * ((interpolatedTime - 0.5f) / 0.5f);
            smallsweep = newsmallsweep + (mSmallToangle - newsmallsweep) * ((interpolatedTime - 0.5f) / 0.5f);
        }
        float degreerotation = CIRCLE_DEGREE_ROTATION * (interpolatedTime);
        mOutterCircle.setRotation(degreerotation);
        mOutterCircle.setmBigStartAngle(bigstart);
        mOutterCircle.setmBigToangle(bigsweep);
        mOutterCircle.setmSmallStartAngle(smallstart);
        mOutterCircle.setmSmallToangle(smallsweep);
        mOutterCircle.invalidate();
    }


    public void setHeaderObj(Animator headerAnimation, FrameLayout mHeaderFrame) {
        this.headerAnimation = headerAnimation;
        this.mHeaderFrame = mHeaderFrame;
    }

    public void setTriangleObj(Animator triangleAnimation, ImageView triangleIv) {
        this.triangleAnimation = triangleAnimation;
        this.mTriangleIv = triangleIv;
    }

    @Override public void onAnimationStart(Animation animation) {
        mTriangleIv.setVisibility(View.VISIBLE);
        mHeaderFrame.setVisibility(View.VISIBLE);
        if (headerAnimation != null && triangleAnimation != null) {
            headerAnimation.start();
            triangleAnimation.start();
        }
    }

    @Override public void onAnimationEnd(Animation animation) {

    }

    @Override public void onAnimationRepeat(Animation animation) {

    }
}
