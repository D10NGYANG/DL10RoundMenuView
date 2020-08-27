package com.dlong.rep.dlroundmenuview.Interface

import android.view.MotionEvent

/**
 * 触摸监听
 *
 * @author D10NG
 * @date on 2020/4/15 9:41 AM
 */
interface OnMenuTouchListener {
    /**
     * 触摸
     * @param event MotionEvent? 触摸事件
     * @param position Int 触摸位置
     */
    fun OnTouch(event: MotionEvent?, position: Int)
}