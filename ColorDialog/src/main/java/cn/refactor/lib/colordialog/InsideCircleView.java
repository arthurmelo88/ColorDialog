package cn.refactor.lib.colordialog;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;

import cn.refactor.lib.colordialog.util.DisplayUtil;

public class InsideCircleView extends View {
    private final Context mContext;
    PromptDialog.DialogType mDialogType = PromptDialog.DialogType.DIALOG_TYPE_INFO;
    private float CONST_BORDER_MARGIN = 0;
    private float CONST_RADIUS;
    /* success view */
    private float CONST_RECT_WEIGHT;
    private float CONST_LEFT_RECT_W;
    private float CONST_RIGHT_RECT_W;
    private float MIN_LEFT_RECT_W;
    private float MAX_RIGHT_RECT_W;
    private Paint mSuccessPaint;
    private float mMaxLeftRectWidth;
    private float VOLATILE_LEFT_RECT;
    private float VOLATILE_RIGHT_RECT;
    private boolean mLeftRectGrowMode;
    /* info view */
    private float CONST_CIRCLE_RADIUS;
    private Paint mInfoPaint;
    private float VOLATILE_FILL_DOWN = 0f;
    private float VOLATILE_MOVE_POINT = 0f;
    private int totalW;
    private int totalH;
    private boolean mFillingExclamation = false;

    public InsideCircleView(Context context) {
        super(context);
        this.mContext = context;
        init();
    }


    public InsideCircleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;

        TypedArray attrsArray = context.obtainStyledAttributes(attrs, R.styleable.EOAttrs, 0, 0);
        initAttributesArray(attrsArray);
        attrsArray.recycle();
        init();
    }

    /**
     * Initialize Attributes arrays
     *
     * @param attrsArray : Attributes array
     */
    private void initAttributesArray(TypedArray attrsArray) {

        mDialogType = PromptDialog.DialogType.values()[attrsArray.getInt(R.styleable.EOAttrs_eo_icontype, 0)];
        init();
    }

    private void init() {
        populateConstants(mDialogType);
    }

    private void populateConstants(PromptDialog.DialogType mDialogType) {
        totalW = getWidth();
        totalH = getHeight();
        CONST_BORDER_MARGIN = DisplayUtil.dp2px(mContext, 8f);
        CONST_RADIUS = DisplayUtil.dp2px(mContext, 1.5f);
        CONST_CIRCLE_RADIUS = OutterCircle.mStrokeDP;
        if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_INFO) {
            CONST_RECT_WEIGHT = DisplayUtil.dp2px(mContext, 4f);
            mInfoPaint = new Paint();
            mInfoPaint.setColor(Color.WHITE);
            mInfoPaint.setAntiAlias(true);
            mInfoPaint.setStyle(Paint.Style.FILL);
            mInfoPaint.setStrokeCap(Paint.Cap.BUTT);
            mInfoPaint.setStrokeWidth(DisplayUtil.dp2px(mContext, 3) * 1.2f);

        } else if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_WRONG) {

        } else if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_HELP) {

        } else if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_WARNING) {

        } else if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_SUCCESS) {
            CONST_RECT_WEIGHT = DisplayUtil.dp2px(mContext, 3.5f);
            CONST_LEFT_RECT_W = (totalH / 2.3f) * 0.6f;
            CONST_RIGHT_RECT_W = totalH / 2.3f;
            MIN_LEFT_RECT_W = CONST_LEFT_RECT_W * 0.6f;
            MAX_RIGHT_RECT_W = CONST_RIGHT_RECT_W * 1.4f;

            mSuccessPaint = new Paint();
            mSuccessPaint.setColor(Color.WHITE);
            mSuccessPaint.setAntiAlias(true);
            mSuccessPaint.setStyle(Paint.Style.FILL);
            mSuccessPaint.setStrokeCap(Paint.Cap.ROUND);
            VOLATILE_LEFT_RECT = 0;
            VOLATILE_RIGHT_RECT = 0;
            mLeftRectGrowMode = false;
        }
    }

    @Override public void draw(Canvas canvas) {
        super.draw(canvas);
        configureCanvas(mDialogType, canvas);

    }

    private void configureCanvas(PromptDialog.DialogType mDialogType, Canvas canvas) {
        totalW = getWidth();
        totalH = getHeight();
        if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_INFO) {
            RectF exclamationRect = new RectF();
            exclamationRect.left = totalW / 2 - (CONST_RECT_WEIGHT / 2);
            exclamationRect.top = VOLATILE_FILL_DOWN + CONST_BORDER_MARGIN;
            exclamationRect.right = totalW / 2 + (CONST_RECT_WEIGHT / 2);
            exclamationRect.bottom = totalH - CONST_BORDER_MARGIN - (3 * CONST_CIRCLE_RADIUS) + VOLATILE_FILL_DOWN;
            canvas.drawRoundRect(exclamationRect, CONST_RADIUS, CONST_RADIUS, mInfoPaint);
            canvas.drawCircle(totalW / 2, totalH - CONST_BORDER_MARGIN - CONST_CIRCLE_RADIUS + VOLATILE_MOVE_POINT, CONST_CIRCLE_RADIUS, mInfoPaint);
        } else if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_WRONG) {

        } else if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_HELP) {

        } else if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_WARNING) {

        } else if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_SUCCESS) {

            canvas.translate((-CONST_LEFT_RECT_W / 2) + (CONST_RECT_WEIGHT / 2), +CONST_RECT_WEIGHT / 2);
            canvas.rotate(45, totalW / 2, totalH / 2);

            mMaxLeftRectWidth = (totalH + CONST_LEFT_RECT_W + CONST_RECT_WEIGHT) / 2 - CONST_CIRCLE_RADIUS - CONST_RECT_WEIGHT/2;

            RectF leftRect = new RectF();
            if (mLeftRectGrowMode) {
                leftRect.left = totalW / 2 - totalH / 2 + CONST_CIRCLE_RADIUS +CONST_RECT_WEIGHT/2;
                leftRect.right = leftRect.left + VOLATILE_LEFT_RECT;
                leftRect.top = (totalH) / 2 - CONST_RECT_WEIGHT / 2;
                leftRect.bottom = leftRect.top + CONST_RECT_WEIGHT;

            } else {
                leftRect.right = totalW / 2 + CONST_LEFT_RECT_W / 2;
                leftRect.left = leftRect.right - VOLATILE_LEFT_RECT;
                leftRect.top = (totalH) / 2 - CONST_RECT_WEIGHT / 2;
                leftRect.bottom = leftRect.top + CONST_RECT_WEIGHT;

            }

            canvas.drawRoundRect(leftRect, CONST_RADIUS, CONST_RADIUS, mSuccessPaint);

            RectF rightRect = new RectF();
            rightRect.bottom = (totalH + CONST_RECT_WEIGHT) / 2;
            rightRect.left = totalW / 2 + CONST_LEFT_RECT_W / 2 - CONST_RECT_WEIGHT / 2 - CONST_RADIUS / 2;
            rightRect.right = rightRect.left + CONST_RECT_WEIGHT;
            rightRect.top = rightRect.bottom - VOLATILE_RIGHT_RECT;
            canvas.drawRoundRect(rightRect, CONST_RADIUS, CONST_RADIUS, mSuccessPaint);
        }
    }


    public void startAnimation(final PromptDialog.DialogType newDialog) {
        Animation animIn = null;
        Animation animOut = null;
        if (newDialog == PromptDialog.DialogType.DIALOG_TYPE_SUCCESS) {
            //hide tick

            invalidate();
            animIn = new Animation() {
                @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
                    if (0.34 < interpolatedTime && 0.6 >= interpolatedTime) {  // grow left and right rect to right
                        mLeftRectGrowMode = true;
                        VOLATILE_LEFT_RECT = mMaxLeftRectWidth * ((interpolatedTime - 0.34f) / 0.26f);
                        invalidate();
                    } else if (0.6 < interpolatedTime && 0.79 >= interpolatedTime) { // shorten left rect from right, still grow right rect
                        mLeftRectGrowMode = false;
                        VOLATILE_LEFT_RECT = mMaxLeftRectWidth * (1 - ((interpolatedTime - 0.6f) / 0.19f));
                        VOLATILE_LEFT_RECT = VOLATILE_LEFT_RECT < MIN_LEFT_RECT_W ? MIN_LEFT_RECT_W : VOLATILE_LEFT_RECT;
                        VOLATILE_RIGHT_RECT = MAX_RIGHT_RECT_W * ((interpolatedTime - 0.55f) / 0.24f);
                        invalidate();
                    } else if (0.79 < interpolatedTime && 1 >= interpolatedTime) { // restore left rect width, shorten right rect to const
                        mLeftRectGrowMode = false;
                        VOLATILE_LEFT_RECT = MIN_LEFT_RECT_W + (CONST_LEFT_RECT_W - MIN_LEFT_RECT_W) * ((interpolatedTime - 0.79f) / 0.21f);
                        VOLATILE_RIGHT_RECT = CONST_RIGHT_RECT_W + (MAX_RIGHT_RECT_W - CONST_RIGHT_RECT_W) * (1 - ((interpolatedTime - 0.79f) / 0.21f));
                        invalidate();
                    }
                }
            };

            animIn.setDuration(300);
            animIn.setStartOffset(50);
        }
        if (mDialogType == PromptDialog.DialogType.DIALOG_TYPE_INFO) {
            animOut = new Animation() {
                @Override protected void applyTransformation(float interpolatedTime, Transformation t) {
                    super.applyTransformation(interpolatedTime, t);
                    if (0 < interpolatedTime && 0.5 >= interpolatedTime) {
                        VOLATILE_MOVE_POINT = (CONST_BORDER_MARGIN + CONST_CIRCLE_RADIUS) * (interpolatedTime / 0.05f);
                        CONST_CIRCLE_RADIUS = CONST_CIRCLE_RADIUS * (1 - (interpolatedTime / 0.5f));
                    }
                    if (0 < interpolatedTime && 1 >= interpolatedTime) {  // fill down exclamation
                        VOLATILE_FILL_DOWN = (totalH - 2f * CONST_CIRCLE_RADIUS) * (interpolatedTime);
                        /*if (interpolatedTime > 0.5) {
                            CONST_CIRCLE_RADIUS = CONST_CIRCLE_RADIUS * (1 - interpolatedTime);
                        }*/
                    }
                    invalidate();
                }
            };
            animOut.setDuration(150);
        }
        final Animation finalAnim = animIn;
        if (animOut != null) {
            animOut.setAnimationListener(new Animation.AnimationListener() {
                @Override public void onAnimationStart(Animation animation) {

                }

                @Override public void onAnimationEnd(Animation animation) {
                    mDialogType = newDialog;
                    populateConstants(mDialogType);
                    startAnimation(finalAnim);
                }

                @Override public void onAnimationRepeat(Animation animation) {

                }
            });
        }
        startAnimation(animOut);
    }

}