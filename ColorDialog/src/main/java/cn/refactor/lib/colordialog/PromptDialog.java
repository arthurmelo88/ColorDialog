package cn.refactor.lib.colordialog;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Bundle;
import android.support.annotation.ColorRes;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import cn.refactor.lib.colordialog.util.DisplayUtil;

import static cn.refactor.lib.colordialog.PromptDialog.DialogType.DIALOG_TYPE_HELP;
import static cn.refactor.lib.colordialog.PromptDialog.DialogType.DIALOG_TYPE_INFO;
import static cn.refactor.lib.colordialog.PromptDialog.DialogType.DIALOG_TYPE_SUCCESS;
import static cn.refactor.lib.colordialog.PromptDialog.DialogType.DIALOG_TYPE_WARNING;
import static cn.refactor.lib.colordialog.PromptDialog.DialogType.DIALOG_TYPE_WRONG;

/**
 * Created by aomel on 01/12/2016.
 */
public class PromptDialog extends Dialog implements View.OnClickListener {

    private static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;
    public static DialogType DIALOG_TYPE_DEFAULT = DIALOG_TYPE_INFO;
    private static int DEFAULT_RADIUS = 3;
    private AnimationSet mAnimIn, mAnimOut;
    private LinearLayout mDialogView;
    private TextView mTitleTv, mContentTv, mPositiveBtn, mNegativeBtn;
    private OnPositiveListener mOnPositiveListener;
    private OnNegativeListener mOnNegativeListener;

    private DialogType mDialogType;
    private OutterCircle mOutterCircle;
    private boolean mIsShowAnim;
    private CharSequence mTitle, mContent, mBtnPositiveText, mBtnNegativeText;
    private ImageView triangleIv;

    private InsideCircleView mInsideCircleV;
    private FrameLayout mHeaderFrame;

    public PromptDialog(Context context) {
        this(context, 0, DIALOG_TYPE_INFO);
    }

    public PromptDialog(Context context, int theme, DialogType alertType) {
        super(context, R.style.color_dialog);
        mDialogType = alertType;
        init();
    }

    public static int getColorResId(DialogType mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.color.color_type_info;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.color.color_type_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.color.color_type_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.color.color_type_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.color.color_type_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return R.color.color_type_warning;
        }
        return R.color.color_type_info;
    }

    private void init() {
        mAnimIn = AnimationLoader.getInAnimation(getContext());
        mAnimOut = AnimationLoader.getOutAnimation(getContext());
    }

    @Override public void onClick(View view) {
        if (view.getId() == R.id.btnPositive && mOnPositiveListener != null) {
            PromptDialog.OnPositiveListener positiveRunned = mOnPositiveListener;
            mOnNegativeListener = null;
            mOnPositiveListener = null;
            positiveRunned.onClick(PromptDialog.this);
        } else if (view.getId() == R.id.btnNegative && mOnNegativeListener != null) {
            PromptDialog.OnNegativeListener negativeRunned = mOnNegativeListener;
            mOnNegativeListener = null;
            mOnPositiveListener = null;
            negativeRunned.onClick(PromptDialog.this);
        } else {
            dismissWithAnimation(mIsShowAnim);
        }
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_promptdialog);
        initView(true, mDialogType);
        initAnimListener();

    }

    private void initView(boolean fromCreate, DialogType newDialogType) {
        if (fromCreate) {
            mOutterCircle = (OutterCircle) findViewById(R.id.outterCircle);
            mDialogView = (LinearLayout) findViewById(R.id.loading);
            mTitleTv = (TextView) findViewById(R.id.tvTitle);
            mContentTv = (TextView) findViewById(R.id.tvContent);
            mPositiveBtn = (TextView) findViewById(R.id.btnPositive);
            mPositiveBtn.setOnClickListener(this);
            mNegativeBtn = (TextView) findViewById(R.id.btnNegative);
            mNegativeBtn.setOnClickListener(this);
            LinearLayout llBtnGroup = (LinearLayout) findViewById(R.id.llBtnGroup);
            setBottomCorners(llBtnGroup);
            FrameLayout topLayout = (FrameLayout) findViewById(R.id.topLayout);
            ImageView triangleStart = new ImageView(getContext());
            triangleIv = new ImageView(getContext());
            triangleIv.setVisibility(View.INVISIBLE);
            triangleStart.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(getContext(), 10)));
            triangleIv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, DisplayUtil.dp2px(getContext(), 10)));
            topLayout.setBackgroundColor(Color.WHITE);
            topLayout.addView(triangleStart);
            topLayout.addView(triangleIv);
            FrameLayout mIconFrame = (FrameLayout) findViewById(R.id.icon_frame);
            mInsideCircleV = (InsideCircleView) mIconFrame.findViewById(R.id.InsideCircleView);
            mHeaderFrame = (FrameLayout) mIconFrame.findViewById(R.id.header_frame);
            mHeaderFrame.setVisibility(View.INVISIBLE);
            int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
            float[] outerRadii = new float[]{radius, radius, radius, radius, 0, 0, 0, 0};

            triangleStart.setImageBitmap(createTriangel((int) (DisplayUtil.getScreenSize(getContext()).x * 0.7), DisplayUtil.dp2px(getContext(), 10), mDialogType));
            triangleIv.setImageBitmap(createTriangel((int) (DisplayUtil.getScreenSize(getContext()).x * 0.7), DisplayUtil.dp2px(getContext(), 10), mDialogType));
            setBtnBackground(mPositiveBtn, mDialogType);
            mNegativeBtn.setTextColor(createColorStateList(getContext().getResources().getColor(getColorResId(DIALOG_TYPE_WARNING)),
                                                           getContext().getResources().getColor(R.color.color_dialog_gray)));
            mNegativeBtn.setBackgroundDrawable(getContext().getResources().getDrawable(getSelBtn(DIALOG_TYPE_WARNING)));
            RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
            final ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
            shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
            shapeDrawable.getPaint().setColor(getContext().getResources().getColor(getColorResId(mDialogType)));
            mIconFrame.setBackgroundDrawable(shapeDrawable);
            mTitleTv.setText(mTitle);
            mContentTv.setText(mContent);
            mPositiveBtn.setText(mBtnPositiveText);
        }
        if (mBtnNegativeText == null || mOnNegativeListener == null) {
            mNegativeBtn.setVisibility(View.GONE);
            mPositiveBtn.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 2));

        } else {
            mNegativeBtn.setText(mBtnNegativeText);
        }
        resizeDialog();
        if (!fromCreate) {
            playAnimation(newDialogType);
        }
    }

    private Animator animateRevealColorFromCoordinates(View viewRoot, @ColorRes int color, int x, int y, DialogType newDialogType) {
        final float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
        Animator anim = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            anim = ViewAnimationUtils.createCircularReveal(viewRoot, x, y, 0, finalRadius);
            anim.setDuration(700);
            anim.setInterpolator(new AnticipateOvershootInterpolator());
        }
        if (viewRoot instanceof ImageView) {
            ((ImageView) viewRoot).setImageBitmap(createTriangel((int) (DisplayUtil.getScreenSize(getContext()).x * 0.7), DisplayUtil.dp2px(getContext(), 10), newDialogType));
        } else {
            viewRoot.setBackgroundColor(getContext().getResources().getColor(color));
        }
        return anim;
    }

    private void playAnimation(DialogType newDialogType) {
        setBtnBackground(mPositiveBtn, newDialogType);
        mTitleTv.setText(mTitle);
        mContentTv.setText(mContent);
        mPositiveBtn.setText(mBtnPositiveText);
        mNegativeBtn.setText(mBtnNegativeText);
        mOutterCircle.setDialogType(newDialogType);
        CircleAndHeaderAnimation mCircleAndHeaderAnimation = new CircleAndHeaderAnimation(mOutterCircle);
        /*if (mDialogType == DialogType.DIALOG_TYPE_WRONG) {
            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        }*/
        if (mDialogType == DIALOG_TYPE_INFO) {
            mInsideCircleV.startAnimation(newDialogType);
        }
        Animator changeTriangleColor = animateRevealColorFromCoordinates(triangleIv, getColorResId(newDialogType), mHeaderFrame.getWidth() / 2, -mHeaderFrame.getHeight() / 2,
                                                                         newDialogType);
        Animator changeHeaderColor = animateRevealColorFromCoordinates(mHeaderFrame, getColorResId(newDialogType), mHeaderFrame.getWidth() / 2, mHeaderFrame.getHeight() / 2,
                                                                       newDialogType);
        mCircleAndHeaderAnimation.setHeaderObj(changeHeaderColor, mHeaderFrame);
        mCircleAndHeaderAnimation.setTriangleObj(changeTriangleColor, triangleIv);
        mCircleAndHeaderAnimation.setAnimationListener(mCircleAndHeaderAnimation);
        mOutterCircle.startAnimation(mCircleAndHeaderAnimation);
    }

    private void resizeDialog() {
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = (int) (DisplayUtil.getScreenSize(getContext()).x * 0.7);
        getWindow().setAttributes(params);
    }

    @Override protected void onStart() {
        super.onStart();
        startWithAnimation(mIsShowAnim);
    }

    @Override public void dismiss() {
        dismissWithAnimation(mIsShowAnim);
    }

    private void startWithAnimation(boolean showInAnimation) {
        if (showInAnimation) {
            mDialogView.startAnimation(mAnimIn);
        }
    }

    private void dismissWithAnimation(boolean showOutAnimation) {
        if (showOutAnimation) {
            mDialogView.startAnimation(mAnimOut);
        } else {
            super.dismiss();
        }
    }

    private int getLogoResId(DialogType mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.mipmap.ic_info;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.mipmap.ic_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.mipmap.ic_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.mipmap.ic_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.mipmap.ic_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return R.mipmap.icon_warning;
        }
        return R.mipmap.ic_info;
    }

    private int getSelBtn(DialogType mDialogType) {
        if (DIALOG_TYPE_DEFAULT == mDialogType) {
            return R.drawable.sel_btn;
        }
        if (DIALOG_TYPE_INFO == mDialogType) {
            return R.drawable.sel_btn_info;
        }
        if (DIALOG_TYPE_HELP == mDialogType) {
            return R.drawable.sel_btn_help;
        }
        if (DIALOG_TYPE_WRONG == mDialogType) {
            return R.drawable.sel_btn_wrong;
        }
        if (DIALOG_TYPE_SUCCESS == mDialogType) {
            return R.drawable.sel_btn_success;
        }
        if (DIALOG_TYPE_WARNING == mDialogType) {
            return R.drawable.sel_btn_warning;
        }
        return R.drawable.sel_btn;
    }

    private void initAnimListener() {
        mAnimOut.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationStart(Animation animation) {
            }

            @Override public void onAnimationEnd(Animation animation) {
                mDialogView.post(new Runnable() {
                    @Override public void run() {
                        callDismiss();
                    }
                });
            }

            @Override public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    private void callDismiss() {
        super.dismiss();
    }

    private Bitmap createTriangel(int width, int height, DialogType dialogType) {
        if (width <= 0 || height <= 0) {
            return null;
        }
        return getBitmap(width, height, getContext().getResources().getColor(getColorResId(dialogType)));
    }

    private Bitmap getBitmap(int width, int height, int backgroundColor) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, BITMAP_CONFIG);
        Canvas canvas = new Canvas(bitmap);

        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(backgroundColor);
        Path path = new Path();
        path.moveTo(0, 0);
        path.lineTo(width, 0);
        path.lineTo(width / 2, height);
        path.close();

        canvas.drawPath(path, paint);
        return bitmap;

    }

    private void setBtnBackground(final TextView btnOk, DialogType dialogType) {
        btnOk.setTextColor(createColorStateList(getContext().getResources().getColor(getColorResId(dialogType)), getContext().getResources().getColor(R.color.color_dialog_gray)));
        btnOk.setBackgroundDrawable(getContext().getResources().getDrawable(getSelBtn(dialogType)));
    }

    private void setBottomCorners(View llBtnGroup) {
        int radius = DisplayUtil.dp2px(getContext(), DEFAULT_RADIUS);
        float[] outerRadii = new float[]{0, 0, 0, 0, radius, radius, radius, radius};
        RoundRectShape roundRectShape = new RoundRectShape(outerRadii, null, null);
        ShapeDrawable shapeDrawable = new ShapeDrawable(roundRectShape);
        shapeDrawable.getPaint().setColor(Color.WHITE);
        shapeDrawable.getPaint().setStyle(Paint.Style.FILL);
        llBtnGroup.setBackgroundDrawable(shapeDrawable);
    }

    private ColorStateList createColorStateList(int normal, int pressed) {
        return createColorStateList(normal, pressed, Color.BLACK, Color.BLACK);
    }

    private ColorStateList createColorStateList(int normal, int pressed, int focused, int unable) {
        int[] colors = new int[]{pressed, focused, normal, focused, unable, normal};
        int[][] states = new int[6][];
        states[0] = new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled};
        states[1] = new int[]{android.R.attr.state_enabled, android.R.attr.state_focused};
        states[2] = new int[]{android.R.attr.state_enabled};
        states[3] = new int[]{android.R.attr.state_focused};
        states[4] = new int[]{android.R.attr.state_window_focused};
        states[5] = new int[]{};
        ColorStateList colorList = new ColorStateList(states, colors);
        return colorList;
    }

    public PromptDialog setAnimationEnable(boolean enable) {
        mIsShowAnim = enable;
        return this;
    }

    public PromptDialog setTitleText(CharSequence title) {
        mTitle = title;
        return this;
    }

    public PromptDialog setTitleText(int resId) {
        return setTitleText(getContext().getString(resId));
    }

    public PromptDialog setContentText(CharSequence content) {
        mContent = content;
        return this;
    }

    public PromptDialog setContentText(int resId) {
        return setContentText(getContext().getString(resId));
    }

    public TextView getTitleTextView() {
        return mTitleTv;
    }

    public TextView getContentTextView() {
        return mContentTv;
    }

    public DialogType getDialogType() {
        return mDialogType;
    }

    public PromptDialog setDialogType(DialogType type) {
        mDialogType = type;
        return this;
    }

    public PromptDialog setPositiveListener(CharSequence btnText, OnPositiveListener l) {
        mBtnPositiveText = btnText;
        return setPositiveListener(l);
    }

    public PromptDialog setPositiveListener(int stringResId, OnPositiveListener l) {
        return setPositiveListener(getContext().getString(stringResId), l);
    }

    public PromptDialog setPositiveListener(OnPositiveListener l) {
        mOnPositiveListener = l;
        return this;
    }

    public PromptDialog setNegativeListener(CharSequence txt, OnNegativeListener l) {
        mBtnNegativeText = txt;
        return setNegativeListener(l);
    }

    public PromptDialog setNegativeListener(int stringResId, OnNegativeListener l) {
        return setNegativeListener(getContext().getString(stringResId), l);
    }

    public PromptDialog setNegativeListener(OnNegativeListener l) {
        mOnNegativeListener = l;
        return this;
    }

    public PromptDialog setAnimationIn(AnimationSet animIn) {
        mAnimIn = animIn;
        return this;
    }

    public PromptDialog setAnimationOut(AnimationSet animOut) {
        mAnimOut = animOut;
        initAnimListener();
        return this;
    }

    public void changeAlertType(DialogType newDialogType) {
        initView(false, newDialogType);
        initAnimListener();
    }

    public enum DialogType {
        DIALOG_TYPE_INFO, DIALOG_TYPE_HELP, DIALOG_TYPE_WRONG, DIALOG_TYPE_SUCCESS, DIALOG_TYPE_WARNING

    }

    public static interface OnPositiveListener {
        public void onClick(PromptDialog dialog);
    }

    public static interface OnNegativeListener {
        public void onClick(PromptDialog dialog);
    }

}
