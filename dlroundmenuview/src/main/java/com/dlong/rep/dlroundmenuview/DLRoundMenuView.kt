package com.dlong.rep.dlroundmenuview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import com.dlong.rep.dlroundmenuview.Interface.OnMenuClickListener
import com.dlong.rep.dlroundmenuview.Interface.OnMenuLongClickListener
import com.dlong.rep.dlroundmenuview.Interface.OnMenuTouchListener
import com.dlong.rep.dlroundmenuview.utils.DLMathUtils
import com.dlong.rep.dlroundmenuview.utils.DrawableUtils
import java.util.*

/**
 * 类遥控器的圆形控制面板
 * @author  dlong
 * created at 2019/4/9 3:33 PM
 */
class DLRoundMenuView constructor(
    context: Context,
    attrs: AttributeSet?,
    defStyle: Int
) : View(context, attrs, defStyle) {

    constructor(context: Context) : this(context, null, 0)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    companion object {
        /** 点击外面  */
        const val DL_TOUCH_OUTSIDE = -2

        /** 点击中间点  */
        const val DL_TOUCH_CENTER = -1
    }

    /** 预设长按时间  */
    private var DL_DEFAULT_LONG_CLICK_TIME = 400
    /** 中心点的坐标X  */
    private var mCoreX = 0f
    /** 中心点的坐标Y  */
    private var mCoreY = 0f
    /** 是否有中心按钮  */
    private var mHasCoreMenu = false
    /** 中心按钮的默认背景  */
    private var mCoreMenuNormalBackgroundColor = 0
    /** 中间按钮的描边颜色  */
    private var mCoreMenuStrokeColor = 0
    /** 中间按钮的描边边框大小  */
    private var mCoreMenuStrokeSize = 0f
    /** 中间按钮选中时的背景颜色  */
    private var mCoreMenuSelectedBackgroundColor = 0
    /** 中心按钮图片  */
    private var mCoreMenuDrawable: Bitmap? = null
    /** 中心按钮圆形半径  */
    private var mCoreMenuRoundRadius = 0f
    /** 菜单数量  */
    private var mRoundMenuNumber = 0
    /** 菜单偏移角度  */
    private var mRoundMenuDeviationDegree = 0f
    /** 菜单图片  */
    private val mRoundMenuDrawableList = ArrayList<Bitmap?>()
    /** 是否画每个菜单扇形到中心点的直线  */
    private var mIsDrawLineToCenter = false
    /** 菜单正常背景颜色  */
    private var mRoundMenuNormalBackgroundColor = 0
    /** 菜单点击背景颜色  */
    private var mRoundMenuSelectedBackgroundColor = 0
    /** 菜单描边颜色  */
    private var mRoundMenuStrokeColor = 0
    /** 菜单描边宽度  */
    private var mRoundMenuStrokeSize = 0f
    /** 菜单图片与中心点的距离 百分数  */
    private var mRoundMenuDistance = 0f
    /** 点击状态 -2是无点击，-1是点击中心圆，其他是点击菜单  */
    private var onClickState = DL_TOUCH_OUTSIDE
    /** 记录按下时间，超过预设时间算长按按钮  */
    private var mTouchTime: Long = 0

    /** 设置接口  */
    private var mMenuClickListener: OnMenuClickListener? = null
    private var mMenuLongClickListener: OnMenuLongClickListener? = null
    private var mTouchListener: OnMenuTouchListener? = null

    @SuppressLint("HandlerLeak")
    private val mHandler: Handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            when (msg.what) {
                1 -> mMenuLongClickListener?.OnMenuLongClick(onClickState)
            }
        }
    }

    init {
        // 加载默认资源
        val res = resources
        val defaultHasCoreMenu = res.getBoolean(R.bool.default_has_core_menu)
        val defaultCoreMenuNormalBackgroundColor = ContextCompat.getColor(context, R.color.default_core_menu_normal_background_color)
        val defaultCoreMenuStrokeColor = ContextCompat.getColor(context, R.color.default_core_menu_stroke_color)
        val defaultCoreMenuStrokeSize = res.getDimension(R.dimen.default_core_menu_stroke_size)
        val defaultCoreMenuSelectedBackgroundColor = ContextCompat.getColor(context, R.color.default_core_menu_selected_background_color)
        val defaultCoreMenuDrawable = ContextCompat.getDrawable(context, R.drawable.default_core_menu_drawable)
        val defaultCoreMenuRoundRadius = res.getDimension(R.dimen.default_core_menu_round_radius)
        val defaultRoundMenuNumber = res.getInteger(R.integer.default_round_menu_number)
        val defaultRoundMenuDeviationDegree = res.getInteger(R.integer.default_round_menu_deviation_degree)
        val defaultRoundMenuDrawable = ContextCompat.getDrawable(context, R.drawable.default_round_menu_drawable)
        val defaultIsDrawLineToCenter = res.getBoolean(R.bool.default_is_draw_line_to_center)
        val defaultRoundMenuNormalBackgroundColor = ContextCompat.getColor(context, R.color.default_round_menu_normal_background_color)
        val defaultRoundMenuSelectedBackgroundColor = ContextCompat.getColor(context, R.color.default_round_menu_selected_background_color)
        val defaultRoundMenuStrokeColor = ContextCompat.getColor(context, R.color.default_round_menu_stroke_color)
        val defaultRoundMenuStrokeSize = res.getDimension(R.dimen.default_round_menu_stroke_size)
        val defaultRoundMenuDistance = res.getFraction(R.fraction.default_round_menu_distance, 1, 1)
        // 读取配置信息
        val a = context.obtainStyledAttributes(attrs, R.styleable.DLRoundMenuView)
        mHasCoreMenu = a.getBoolean(R.styleable.DLRoundMenuView_RMHasCoreMenu, defaultHasCoreMenu)
        mCoreMenuNormalBackgroundColor = a.getColor(R.styleable.DLRoundMenuView_RMCoreMenuNormalBackgroundColor, defaultCoreMenuNormalBackgroundColor)
        mCoreMenuStrokeColor = a.getColor(R.styleable.DLRoundMenuView_RMCoreMenuStrokeColor, defaultCoreMenuStrokeColor)
        mCoreMenuStrokeSize = a.getDimension(R.styleable.DLRoundMenuView_RMCoreMenuStrokeSize, defaultCoreMenuStrokeSize)
        mCoreMenuSelectedBackgroundColor = a.getColor(R.styleable.DLRoundMenuView_RMCoreMenuSelectedBackgroundColor, defaultCoreMenuSelectedBackgroundColor)
        mCoreMenuRoundRadius = a.getDimension(R.styleable.DLRoundMenuView_RMCoreMenuRoundRadius, defaultCoreMenuRoundRadius)
        mRoundMenuNumber = a.getInteger(R.styleable.DLRoundMenuView_RMRoundMenuNumber, defaultRoundMenuNumber)
        mRoundMenuDeviationDegree = a.getInteger(R.styleable.DLRoundMenuView_RMRoundMenuDeviationDegree, defaultRoundMenuDeviationDegree).toFloat()
        mIsDrawLineToCenter = a.getBoolean(R.styleable.DLRoundMenuView_RMIsDrawLineToCenter, defaultIsDrawLineToCenter)
        mRoundMenuNormalBackgroundColor = a.getColor(R.styleable.DLRoundMenuView_RMRoundMenuNormalBackgroundColor, defaultRoundMenuNormalBackgroundColor)
        mRoundMenuSelectedBackgroundColor = a.getColor(R.styleable.DLRoundMenuView_RMRoundMenuSelectedBackgroundColor, defaultRoundMenuSelectedBackgroundColor)
        mRoundMenuStrokeColor = a.getColor(R.styleable.DLRoundMenuView_RMRoundMenuStrokeColor, defaultRoundMenuStrokeColor)
        mRoundMenuStrokeSize = a.getDimension(R.styleable.DLRoundMenuView_RMRoundMenuStrokeSize, defaultRoundMenuStrokeSize)
        mRoundMenuDistance = a.getFraction(R.styleable.DLRoundMenuView_RMRoundMenuDistance, 1, 1, defaultRoundMenuDistance)
        var drawable = a.getDrawable(R.styleable.DLRoundMenuView_RMCoreMenuDrawable)?: defaultCoreMenuDrawable
        mCoreMenuDrawable = if (null != drawable) DrawableUtils.drawableToBitmap(drawable) else null
        drawable = a.getDrawable(R.styleable.DLRoundMenuView_RMRoundMenuDrawable)?: defaultRoundMenuDrawable
        val roundMenuDrawable = if (null != drawable) DrawableUtils.drawableToBitmap(drawable) else null
        for (i in 0 until mRoundMenuNumber) {
            mRoundMenuDrawableList.add(i, roundMenuDrawable)
        }
        // 释放内存，回收资源
        a.recycle()
    }

    /**
     * 测量
     * @param widthMeasureSpec
     * @param heightMeasureSpec
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSpecSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSpecSize = MeasureSpec.getSize(heightMeasureSpec)
        // 左右减去2，留足空间给算术裁切
        setMeasuredDimension(widthSpecSize - 2, heightSpecSize - 2)
    }

    /**
     * 绘制
     * @param canvas
     */
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        // 拿到中心位置
        mCoreX = width.toFloat() / 2
        mCoreY = height.toFloat() / 2
        // 搞到一个正方形画板
        val rect = RectF(
            mRoundMenuStrokeSize, mRoundMenuStrokeSize,
            width - mRoundMenuStrokeSize, height - mRoundMenuStrokeSize
        )
        // 菜单数量要大于0
        if (mRoundMenuNumber > 0) {
            // 每个菜单弧形的角度
            val sweepAngle = 360.toFloat() / mRoundMenuNumber
            // 一个重要的点 0度在正X轴上，所以需要让它回到正Y轴上
            // 计算真正的偏移角度
            // -90度回到正Y轴；-每个菜单占据角度的一半，使得菜单中央回到正Y轴；再加上用户自己想修改的角度偏移
            /** 真实菜单偏移角度  */
            val mRealRoundMenuDeviationDegree = mRoundMenuDeviationDegree - sweepAngle / 2 - 90
            for (i in 0 until mRoundMenuNumber) {
                // 画扇形
                var paint = Paint()
                paint.isAntiAlias = true
                paint.color = if (onClickState == i) mRoundMenuSelectedBackgroundColor else mRoundMenuNormalBackgroundColor
                canvas.drawArc(rect,
                    mRealRoundMenuDeviationDegree + i * sweepAngle,
                    sweepAngle, true, paint)
                // 画扇形描边
                paint = Paint()
                paint.isAntiAlias = true
                paint.strokeWidth = mRoundMenuStrokeSize
                paint.style = Paint.Style.STROKE
                paint.color = mRoundMenuStrokeColor
                canvas.drawArc(rect,
                    mRealRoundMenuDeviationDegree + i * sweepAngle,
                    sweepAngle, mIsDrawLineToCenter, paint)
                // 画图案
                val roundMenuDrawable = mRoundMenuDrawableList[i]
                if (null != roundMenuDrawable) {
                    val matrix = Matrix()
                    matrix.postTranslate(
                        (mCoreX + width / 2 * mRoundMenuDistance - roundMenuDrawable.width / 2),
                        mCoreY - roundMenuDrawable.height.toFloat() / 2
                    )
                    matrix.postRotate(
                        mRoundMenuDeviationDegree - 90 + i * sweepAngle,
                        mCoreX,
                        mCoreY
                    )
                    canvas.drawBitmap(roundMenuDrawable, matrix, null)
                }
            }
        }

        //画中心圆圈
        if (mHasCoreMenu) {
            // 画中心圆
            val rect1 = RectF(
                mCoreX - mCoreMenuRoundRadius, mCoreY - mCoreMenuRoundRadius,
                mCoreX + mCoreMenuRoundRadius, mCoreY + mCoreMenuRoundRadius
            )
            var paint = Paint()
            paint.isAntiAlias = true
            paint.strokeWidth = mCoreMenuStrokeSize
            paint.color = if (onClickState == -1) mCoreMenuSelectedBackgroundColor else mCoreMenuNormalBackgroundColor
            canvas.drawArc(rect1, 0f, 360f, true, paint)
            //画描边
            paint = Paint()
            paint.isAntiAlias = true
            paint.strokeWidth = mCoreMenuStrokeSize
            paint.style = Paint.Style.STROKE
            paint.color = mCoreMenuStrokeColor
            canvas.drawArc(rect1, 0f, 360f, true, paint)
            if (mCoreMenuDrawable != null) {
                //画中心圆圈的“OK”图标
                canvas.drawBitmap(
                    mCoreMenuDrawable!!, mCoreX - mCoreMenuDrawable!!.width.toFloat() / 2,
                    mCoreY - mCoreMenuDrawable!!.height.toFloat() / 2, null
                )
            }
        }
    }

    /**
     * 触摸事件拦截
     * @param event
     * @return
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val textX: Float
        val textY: Float
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                // 记录按下的时间
                mTouchTime = Date().time
                // 记录按下的位置
                textX = event.x
                textY = event.y
                // 距离中心点之间的直线距离
                val distance = DLMathUtils.getDistanceFromTwoSpot(mCoreX, mCoreY, textX, textY)
                if (distance <= mCoreMenuRoundRadius) {
                    // 点击的是中心圆
                    onClickState = DL_TOUCH_CENTER
                } else if (distance <= width / 2) {
                    // 点击的是某个扇形
                    // 每个弧形的角度
                    val sweepAngle = 360.toFloat() / mRoundMenuNumber
                    // 计算这根线的角度
                    var angle = DLMathUtils.getRotationBetweenLines(mCoreX, mCoreY, textX, textY)
                    // 这个angle的角度是从加上偏移角度，所以需要计算一下
                    angle = (angle + 360 + sweepAngle / 2 - mRoundMenuDeviationDegree.toInt()) % 360
                    // 根据角度得出点击的是那个扇形
                    onClickState = (angle / sweepAngle).toInt()
                    if (onClickState >= mRoundMenuNumber) onClickState = 0
                } else {
                    //点击了外面
                    onClickState = DL_TOUCH_OUTSIDE
                }
                mHandler.sendEmptyMessageDelayed(1, DL_DEFAULT_LONG_CLICK_TIME.toLong())
                invalidate()
            }
            MotionEvent.ACTION_UP -> {
                mHandler.removeMessages(1)
                if (Date().time - mTouchTime < DL_DEFAULT_LONG_CLICK_TIME) {
                    //点击小于400毫秒算点击
                    mMenuClickListener?.OnMenuClick(onClickState)
                }
                onClickState = DL_TOUCH_OUTSIDE
                invalidate()
            }
            MotionEvent.ACTION_CANCEL, MotionEvent.ACTION_OUTSIDE -> {
                mHandler.removeMessages(1)
                onClickState = DL_TOUCH_OUTSIDE
                invalidate()
            }
        }
        mTouchListener?.OnTouch(event, onClickState)
        return true
    }

    /**
     * 设置点击监听
     * @param onMenuClickListener
     */
    fun setOnMenuClickListener(onMenuClickListener: OnMenuClickListener?) {
        mMenuClickListener = onMenuClickListener
    }

    /**
     * 设置长按监听
     * @param onMenuLongClickListener
     */
    fun setOnMenuLongClickListener(onMenuLongClickListener: OnMenuLongClickListener?) {
        mMenuLongClickListener = onMenuLongClickListener
    }

    /**
     * 设置触摸监听
     * @param onMenuTouchListener
     */
    fun setOnMenuTouchListener(onMenuTouchListener: OnMenuTouchListener?) {
        mTouchListener = onMenuTouchListener
    }

    /**
     * 设定长按时间判断
     * @param millisecond  毫秒
     */
    fun setLongClickTime(millisecond: Int) {
        DL_DEFAULT_LONG_CLICK_TIME = millisecond
        invalidate()
    }

    /**
     * 设定是否有中间按钮
     * @param hasCoreMenu true
     */
    fun setHasCoreMenu(hasCoreMenu: Boolean) {
        mHasCoreMenu = hasCoreMenu
        invalidate()
    }

    /**
     * 设定中间按钮的正常情况下的背景颜色
     * @param color 颜色 0xffffffff
     */
    fun setCoreMenuNormalBackgroundColor(color: Int) {
        mCoreMenuNormalBackgroundColor = color
        invalidate()
    }

    /**
     * 设定中间按钮的点击情况下的背景颜色
     * @param color 颜色 0xffffffff
     */
    fun setCoreMenuSelectedBackgroundColor(color: Int) {
        mCoreMenuSelectedBackgroundColor = color
        invalidate()
    }

    /**
     * 设定中间按钮的描边颜色
     * @param color 颜色 0xffffffff
     */
    fun setCoreMenuStrokeColor(color: Int) {
        mCoreMenuStrokeColor = color
        invalidate()
    }

    /**
     * 设定中间按钮的描边宽度
     * @param size  宽度 1.0f
     */
    fun setCoreMenuStrokeSize(size: Float) {
        mCoreMenuStrokeSize = size
        invalidate()
    }

    /**
     * 设定中间按钮的圆形半径
     * @param radius 半径 40.0f
     */
    fun setCoreMenuRoundRadius(radius: Float) {
        mCoreMenuRoundRadius = radius
        invalidate()
    }

    /**
     * 设定中间按钮的居中图片
     * @param drawable 图片
     */
    fun setCoreMenuDrawable(drawable: Drawable?) {
        mCoreMenuDrawable = DrawableUtils.drawableToBitmap(drawable)
        invalidate()
    }

    /**
     * 设定菜单数量
     * @param number 数量 4
     */
    fun setRoundMenuNumber(number: Int) {
        mRoundMenuNumber = number
        invalidate()
    }

    /**
     * 设定偏移角度
     * @param degree 角度 45.0f
     */
    fun setRoundMenuDeviationDegree(degree: Float) {
        mRoundMenuDeviationDegree = degree
        invalidate()
    }

    /**
     * 设定菜单的图片
     * @param drawable 图片
     */
    fun setRoundMenuDrawable(
        index: Int,
        drawable: Drawable?
    ) {
        if (index < 0 || index > mRoundMenuNumber) return
        val bitmap = DrawableUtils.drawableToBitmap(drawable)
        mRoundMenuDrawableList[index] = bitmap
        invalidate()
    }

    /**
     * 设定正常情况下的菜单背景颜色
     * @param color 颜色 0xffffffff
     */
    fun setRoundMenuNormalBackgroundColor(color: Int) {
        mRoundMenuNormalBackgroundColor = color
        invalidate()
    }

    /**
     * 设定点击情况下的菜单背景颜色
     * @param color 颜色 0xffffffff
     */
    fun setRoundMenuSelectedBackgroundColor(color: Int) {
        mRoundMenuSelectedBackgroundColor = color
        invalidate()
    }

    /**
     * 设定菜单描边颜色
     * @param color 颜色 0xffffffff
     */
    fun setRoundMenuStrokeColor(color: Int) {
        mRoundMenuStrokeColor = color
        invalidate()
    }

    /**
     * 设定菜单描边宽度
     * @param size 宽度 1.0f
     */
    fun setRoundMenuStrokeSize(size: Float) {
        mRoundMenuStrokeSize = size
        invalidate()
    }

    /**
     * 设定菜单图片到中心点的距离 百分数
     * @param distance 百分数 0.70f
     */
    fun setRoundMenuDistance(distance: Float) {
        if (distance > 1) return
        mRoundMenuDistance = distance
        invalidate()
    }

    /**
     * 设定菜单是否连线到中心点
     * @param is false
     */
    fun setIsDrawLineToCenter(`is`: Boolean) {
        mIsDrawLineToCenter = `is`
        invalidate()
    }
}