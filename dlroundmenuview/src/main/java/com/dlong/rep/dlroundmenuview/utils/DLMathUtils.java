package com.dlong.rep.dlroundmenuview.utils;

/**
 * 计算工具
 * @author  dlong
 * created at 2019/4/9 6:55 PM
 */
public class DLMathUtils {

    /**
     * 求两个点之间的距离
     * @param x1  点1 x
     * @param y1  点1 y
     * @param x2  点2 x
     * @param y2  点2 y
     * @return
     */
    public static double getDistanceFromTwoSpot(float x1, float y1, float x2, float y2) {
        float width, height;
        if (x1 > x2) {
            width = x1 - x2;
        } else {
            width = x2 - x1;
        }

        if (y1 > y2) {
            height = y2 - y1;
        } else {
            height = y2 - y1;
        }
        return Math.sqrt((width * width) + (height * height));
    }

    /**
     * 获取两条线的夹角
     * @param centerX
     * @param centerY
     * @param xInView
     * @param yInView
     * @return
     */
    public static double getRotationBetweenLines(float centerX, float centerY, float xInView, float yInView) {
        double rotation = 0;

        double k1 = (double) (centerY - centerY) / (centerX * 2 - centerX);
        double k2 = (double) (yInView - centerY) / (xInView - centerX);
        double tmpDegree = Math.atan((Math.abs(k1 - k2)) / (1 + k1 * k2)) / Math.PI * 180;

        if (xInView > centerX && yInView < centerY) {  //第一象限
            rotation = 90 - tmpDegree;
        } else if (xInView > centerX && yInView > centerY) { //第二象限
            rotation = 90 + tmpDegree;
        } else if (xInView < centerX && yInView > centerY) { //第三象限
            rotation = 270 - tmpDegree;
        } else if (xInView < centerX && yInView < centerY) { //第四象限
            rotation = 270 + tmpDegree;
        } else if (xInView == centerX && yInView < centerY) {
            rotation = 0;
        } else if (xInView == centerX && yInView > centerY) {
            rotation = 180;
        }
        return rotation;
    }
}
