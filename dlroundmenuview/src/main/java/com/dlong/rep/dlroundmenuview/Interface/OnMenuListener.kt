package com.dlong.rep.dlroundmenuview.Interface

import android.view.MotionEvent

/**
 * lambda 接口
 * @author D10NG
 * @date on 2020/8/28 8:57 AM
 */
class OnMenuListener: OnMenuClickListener, OnMenuLongClickListener, OnMenuTouchListener {

    private lateinit var clickListener: (position: Int) -> Unit

    fun onMenuClick(listener: (position: Int) -> Unit) {
        this.clickListener = listener
    }

    override fun OnMenuClick(position: Int) {
        this.clickListener.invoke(position)
    }

    private lateinit var longClickListener: (position: Int) -> Unit

    fun onMenuLongClick(listener: (position: Int) -> Unit) {
        this.longClickListener = listener
    }

    override fun OnMenuLongClick(position: Int) {
        this.longClickListener.invoke(position)
    }

    private lateinit var touchListener: (event: MotionEvent?, position: Int) -> Unit

    fun onTouch(listener: (event: MotionEvent?, position: Int) -> Unit) {
        this.touchListener = listener
    }

    override fun OnTouch(event: MotionEvent?, position: Int) {
        this.touchListener.invoke(event, position)
    }
}