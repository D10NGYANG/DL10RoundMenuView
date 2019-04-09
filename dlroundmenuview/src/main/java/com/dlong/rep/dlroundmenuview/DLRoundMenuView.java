package com.dlong.rep.dlroundmenuview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.dlong.rep.dlroundmenuview.Interface.OnMenuClickListener;
import com.dlong.rep.dlroundmenuview.Interface.OnMenuLongClickListener;
import com.dlong.rep.dlroundmenuview.utils.DrawableUtils;

/**
 * 类遥控器的圆形控制面板
 * @author  dlong
 * created at 2019/4/9 3:33 PM
 */
public class DLRoundMenuView extends View {
    private Context mContext;

    /** 点击外面 */
    public static final int DL_TOUCH_OUTSIDE = -2;
    /** 点击中间点 */
    public static final int DL_TOUCH_CENTER = -1;
    /** 预设长按时间 */
    public static final int DL_DEFAULT_LONG_CLICK_TIME = 400;

    /** 中心点的坐标X */
    private float mCoreX;
    /** 中心点的坐标Y */
    private float mCoreY;
    /** 是否有中心按钮 */
    private boolean mHasCoreMenu;
    /** 中心按钮的默认背景 */
    private int mCoreMenuNormalBackgroundColor;
    /** 中间按钮的描边颜色 */
    private int mCoreMenuStrokeColor;
    /** 中间按钮的描边边框大小 */
    private float mCoreMenuStrokeSize;
    /** 中间按钮选中时的背景颜色 */
    private int mCoreMenuSelectedBackgroundColor;
    /** 中心按钮图片 */
    private Bitmap mCoreMenuDrawable;
    /** 中心按钮圆形半径 */
    private float mCoreMenuRoundRadius;
    /** 菜单数量 */
    private int mRoundMenuNumber;
    /** 菜单偏移角度 */
    private float mRoundMenuDeviationDegree;
    /** 菜单图片 */
    private Bitmap mRoundMenuDrawable;
    /** 是否画每个菜单扇形到中心点的直线 */
    private boolean mIsDrawLineToCenter;
    /** 菜单正常背景颜色 */
    private int mRoundMenuNormalBackgroundColor;
    /** 菜单点击背景颜色 */
    private int mRoundMenuSelectedBackgroundColor;
    /** 菜单描边颜色 */
    private int mRoundMenuStrokeColor;
    /** 菜单描边宽度 */
    private float mRoundMenuStrokeSize;
    /** 菜单图片与中心点的距离 百分数 */
    private float mRoundMenuDistance;
    /** 点击状态 -2是无点击，-1是点击中心圆，其他是点击菜单 */
    private int onClickState = DL_TOUCH_OUTSIDE;
    /** 记录按下时间，超过预设时间算长按按钮 */
    private long mTouchTime;

    /** 设置接口 */
    private OnMenuClickListener mMenuClickListener;
    private OnMenuLongClickListener mMenuLongClickListener;

    /**
     * 设置点击监听
     * @param onMenuClickListener
     */
    public void setOnItemClickListener(OnMenuClickListener onMenuClickListener){
        this.mMenuClickListener = onMenuClickListener;
    }

    /**
     * 设置长按监听
     * @param onMenuLongClickListener
     */
    public void setOnLongItemClickListener(OnMenuLongClickListener onMenuLongClickListener){
        this.mMenuLongClickListener = onMenuLongClickListener;
    }


    public DLRoundMenuView(Context context) {
        super(context);
        init(context, null);
    }

    public DLRoundMenuView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public DLRoundMenuView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化数据
     * @param context
     * @param attrs
     */
    private void init(Context context, @Nullable AttributeSet attrs) {
        mContext = context;
        // 加载默认资源
        final Resources res = getResources();
        final boolean defaultHasCoreMenu = res.getBoolean(R.bool.default_has_core_menu);
        final int defaultCoreMenuNormalBackgroundColor = res.getColor(R.color.default_core_menu_normal_background_color);
        final int defaultCoreMenuStrokeColor = res.getColor(R.color.default_core_menu_stroke_color);
        final float defaultCoreMenuStrokeSize = res.getDimension(R.dimen.default_core_menu_stroke_size);
        final int defaultCoreMenuSelectedBackgroundColor = res.getColor(R.color.default_core_menu_selected_background_color);
        final Drawable defaultCoreMenuDrawable = res.getDrawable(R.drawable.default_core_menu_drawable);
        final float defaultCoreMenuRoundRadius = res.getDimension(R.dimen.default_core_menu_round_radius);
        final int defaultRoundMenuNumber = res.getInteger(R.integer.default_round_menu_number);
        final int defaultRoundMenuDeviationDegree = res.getInteger(R.integer.default_round_menu_deviation_degree);
        final Drawable defaultRoundMenuDrawable = res.getDrawable(R.drawable.default_round_menu_drawable);
        final boolean defaultIsDrawLineToCenter = res.getBoolean(R.bool.default_is_draw_line_to_center);
        final int defaultRoundMenuNormalBackgroundColor = res.getColor(R.color.default_round_menu_normal_background_color);
        final int defaultRoundMenuSelectedBackgroundColor = res.getColor(R.color.default_round_menu_selected_background_color);
        final int defaultRoundMenuStrokeColor = res.getColor(R.color.default_round_menu_stroke_color);
        final float defaultRoundMenuStrokeSize = res.getDimension(R.dimen.default_round_menu_stroke_size);
        final float defaultRoundMenuDistance = res.getFraction(R.fraction.default_round_menu_distance, 1, 1);
        // 读取配置信息
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.DLRoundMenuView);
        mHasCoreMenu = a.getBoolean(R.styleable.DLRoundMenuView_RMHasCoreMenu, defaultHasCoreMenu);
        mCoreMenuNormalBackgroundColor = a.getColor(R.styleable.DLRoundMenuView_RMCoreMenuNormalBackgroundColor, defaultCoreMenuNormalBackgroundColor);
        mCoreMenuStrokeColor = a.getColor(R.styleable.DLRoundMenuView_RMCoreMenuStrokeColor, defaultCoreMenuStrokeColor);
        mCoreMenuStrokeSize = a.getDimension(R.styleable.DLRoundMenuView_RMCoreMenuStrokeSize, defaultCoreMenuStrokeSize);
        mCoreMenuSelectedBackgroundColor = a.getColor(R.styleable.DLRoundMenuView_RMCoreMenuSelectedBackgroundColor, defaultCoreMenuSelectedBackgroundColor);
        mCoreMenuRoundRadius = a.getDimension(R.styleable.DLRoundMenuView_RMCoreMenuRoundRadius, defaultCoreMenuRoundRadius);
        mRoundMenuNumber = a.getInteger(R.styleable.DLRoundMenuView_RMRoundMenuNumber, defaultRoundMenuNumber);
        mRoundMenuDeviationDegree = a.getInteger(R.styleable.DLRoundMenuView_RMRoundMenuDeviationDegree, defaultRoundMenuDeviationDegree);
        mIsDrawLineToCenter = a.getBoolean(R.styleable.DLRoundMenuView_RMIsDrawLineToCenter, defaultIsDrawLineToCenter);
        mRoundMenuNormalBackgroundColor = a.getColor(R.styleable.DLRoundMenuView_RMRoundMenuNormalBackgroundColor, defaultRoundMenuNormalBackgroundColor);
        mRoundMenuSelectedBackgroundColor = a.getColor(R.styleable.DLRoundMenuView_RMRoundMenuSelectedBackgroundColor, defaultRoundMenuSelectedBackgroundColor);
        mRoundMenuStrokeColor = a.getColor(R.styleable.DLRoundMenuView_RMRoundMenuStrokeColor, defaultRoundMenuStrokeColor);
        mRoundMenuStrokeSize = a.getDimension(R.styleable.DLRoundMenuView_RMRoundMenuStrokeSize, defaultRoundMenuStrokeSize);
        mRoundMenuDistance = a.getFraction(R.styleable.DLRoundMenuView_RMRoundMenuDistance, 1, 1, defaultRoundMenuDistance);
        Drawable drawable = a.getDrawable(R.styleable.DLRoundMenuView_RMCoreMenuDrawable);
        if (null == drawable){
            drawable = defaultCoreMenuDrawable;
        }
        if (null != drawable){
            mCoreMenuDrawable = DrawableUtils.drawableToBitmap(drawable);
        } else {
            mCoreMenuDrawable = null;
        }
        drawable = a.getDrawable(R.styleable.DLRoundMenuView_RMRoundMenuDrawable);
        if (null == drawable){
            drawable = defaultRoundMenuDrawable;
        }
        if (null != drawable){
            mRoundMenuDrawable = DrawableUtils.drawableToBitmap(drawable);
        } else {
            mRoundMenuDrawable = null;
        }

        // 释放内存，回收资源
        a.recycle();
    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
        // 左右减去2，留足空间给算术裁切
        setMeasuredDimension(widthSpecSize - 2, heightSpecSize - 2);
    }

    /**
     * 绘制
     * @param canvas
     */
    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        // 拿到中心位置
        mCoreX = (float) getWidth() / 2;
        mCoreY = (float) getHeight() / 2;
        // 搞到一个正方形画板
        RectF rect = new RectF(1, 1, getWidth() - 1, getHeight() - 1);
        // 菜单数量要大于0
        if (mRoundMenuNumber > 0) {
            // 每个菜单弧形的角度
            float sweepAngle = (float) 360 / mRoundMenuNumber;
            for (int i = 0; i < mRoundMenuNumber; i++) {
                // 画扇形
                Paint paint = new Paint();
                paint.setAntiAlias(true);
                paint.setColor(onClickState == i?mRoundMenuSelectedBackgroundColor:mRoundMenuNormalBackgroundColor);
                canvas.drawArc(rect, mRoundMenuDeviationDegree + (i * sweepAngle), sweepAngle, true, paint);
                // 画扇形描边
                paint = new Paint();
                paint.setAntiAlias(true);
                paint.setStrokeWidth(mRoundMenuStrokeSize);
                paint.setStyle(Paint.Style.STROKE);
                paint.setColor(mRoundMenuStrokeColor);
                canvas.drawArc(rect, mRoundMenuDeviationDegree + (i * sweepAngle), sweepAngle, mIsDrawLineToCenter, paint);
                // 画图案
                Matrix matrix = new Matrix();
                matrix.postTranslate((float) ((coreX + getWidth() / 2 * roundMenu.iconDistance) - (roundMenu.icon.getWidth() / 2)), coreY - (roundMenu.icon.getHeight() / 2));
                matrix.postRotate(((i + 1) * sweepAngle + 90), coreX, coreY);
                canvas.drawBitmap(roundMenu.icon, matrix, null);
            }
        }

        //画中心圆圈
        if (isCoreMenu) {
            //填充
            RectF rect1 = new RectF(coreX - roundRadius, coreY - roundRadius, coreX + roundRadius, coreY + roundRadius);
            Paint paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(coreMenuStrokeSize);
            if (onClickState == -1) {
                paint.setColor(coreMenuSelectColor);
            } else {
                paint.setColor(coreMenuColor);
            }
            canvas.drawArc(rect1, 0, 360, true, paint);

            //画描边
            paint = new Paint();
            paint.setAntiAlias(true);
            paint.setStrokeWidth(coreMenuStrokeSize);
            paint.setStyle(Paint.Style.STROKE);
            paint.setColor(coreMenuStrokeColor);
            canvas.drawArc(rect1, 0, 360, true, paint);
            if (coreBitmap != null) {
                //画中心圆圈的“OK”图标
                canvas.drawBitmap(coreBitmap, coreX - coreBitmap.getWidth() / 2, coreY - coreBitmap.getHeight() / 2, null);//在 0，0坐标开始画入src
            }
        }
    }
}
