package cn.refactor.lib.colordialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.View;

import cn.refactor.lib.colordialog.util.ArcUtils;

/**
 * Created by aomel on 01/12/2016.
 */

public class OutterCircle extends View {

    public static float mStrokeDP;
    private Paint mPaintCircle;
    private Paint mPaintPoint;
    private int mWidth;
    private int mHeight;
    private float mBigToangle;
    private float mSmallToangle;
    private float mBigStartAngle;
    private float mSmallStartAngle;
    public OutterCircle(Context context) {
        super(context);
        new OutterCircle(context, null);
    }

    public OutterCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*int[] attrsArray = new int[] {
                android.R.attr.id, // 0
                android.R.attr.background, // 1
                android.R.attr.layout_width, // 2
                android.R.attr.layout_height // 3
        };
        TypedArray ta = context.obtainStyledAttributes(attrs, attrsArray);
        mId = ta.getResourceId(0 *//* index of attribute in attrsArray *//*, View.NO_ID);
        mCircleDrawable = ta.getDrawable(1);
        mWidth = ta.getDimensionPixelSize(2, ViewGroup.LayoutParams.MATCH_PARENT);
        mHeight = ta.getDimensionPixelSize(3, ViewGroup.LayoutParams.MATCH_PARENT);
        ta.recycle();*/
        init();
    }

    public Paint getmPaintPoint() {
        return mPaintPoint;
    }

    public Paint getmPaintCircle() {
        return mPaintCircle;
    }

    public void setmSmallStartAngle(float mSmallStartAngle) {
        this.mSmallStartAngle = mSmallStartAngle;
    }

    public void setmBigStartAngle(float mBigStartAngle) {
        this.mBigStartAngle = mBigStartAngle;
    }

    public void setmSmallToangle(float mSmallToangle) {
        this.mSmallToangle = mSmallToangle;
    }

    public void init() {
        mStrokeDP = (int) dip2px(3f);

        //circle paint
        mPaintCircle = new Paint();
        mPaintCircle.setAntiAlias(true);
        mPaintCircle.setStyle(Paint.Style.STROKE);
        mPaintCircle.setStrokeCap(Paint.Cap.ROUND);
        mPaintCircle.setStrokeWidth(mStrokeDP);
        //Circle color
        mPaintCircle.setColor(Color.WHITE);

        //pointpaint
        mPaintPoint = new Paint();
        mPaintPoint.setAntiAlias(true);
        mPaintPoint.setStyle(Paint.Style.STROKE);
        mPaintPoint.setStrokeCap(Paint.Cap.ROUND);
        mPaintPoint.setStrokeWidth(mStrokeDP * 1.2f);
        //Circle color
        mPaintPoint.setColor(Color.WHITE);

        mBigStartAngle = 40;
        mSmallStartAngle = 50;
        mBigToangle = -260;
        mSmallToangle = 340 - Math.abs(mBigToangle);
    }

    @Override public void draw(Canvas canvas) {
        super.draw(canvas);
        mWidth = getWidth();
        mHeight = getHeight();


        //Initial Angle (optional, it can be zero)
        ArcUtils.drawArc(canvas, new PointF(mWidth / 2, mHeight / 2), (mHeight / 2) - (mStrokeDP / 2), mBigStartAngle, mBigToangle, mPaintCircle);
        ArcUtils.drawArc(canvas, new PointF(mWidth / 2, mHeight / 2), (mHeight / 2) - (mStrokeDP * 1.3f / 2), mSmallStartAngle, mSmallToangle, mPaintPoint);
    }

    public float getmBigToangle() {
        return mBigToangle;
    }

    public void setmBigToangle(float mToangle) {
        this.mBigToangle = mToangle;
    }

    public float dip2px(float dpValue) {
        final float scale = getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
