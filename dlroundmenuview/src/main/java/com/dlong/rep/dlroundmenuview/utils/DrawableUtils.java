package com.dlong.rep.dlroundmenuview.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

/**
 * drawable工具
 * @author  dlong
 * created at 2019/4/9 4:48 PM
 */
public class DrawableUtils {

    /**
     * drawable 转换成bitmap
     * @param drawable
     * @return
     */
    public static Bitmap drawableToBitmap(Drawable drawable){
        if (null == drawable) return null;
        // 取drawable的长宽
        int width = drawable.getIntrinsicWidth();
        int height = drawable.getIntrinsicHeight();
        if (width <= 0 || height <= 0) return null;
        // 取drawable的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ?
                Bitmap.Config.ARGB_8888:Bitmap.Config.RGB_565;
        // 建立对应bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, config);
        // 建立对应bitmap的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, width, height);
        // 把drawable内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }
}
