package com.evertrend.tiger.common.bean.mapview.utils;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

public class GraphicalUtil {
    public static void drawArrow(Canvas canvas, Paint paint, float fromX, float fromY, float toX, float toY,
                          float heigth, float bottom) {
        // heigth和bottom分别为三角形的高与底的一半,调节三角形大小
        canvas.drawLine(fromX, fromY, toX, toY, paint);
        float juli = (float) Math.sqrt((toX - fromX) * (toX - fromX)
                + (toY - fromY) * (toY - fromY));// 获取线段距离
        float juliX = toX - fromX;// 有正负，不要取绝对值
        float juliY = toY - fromY;// 有正负，不要取绝对值
        float dianX = toX - (heigth / juli * juliX);
        float dianY = toY - (heigth / juli * juliY);
        //终点的箭头
        Path path = new Path();
        path.moveTo(toX, toY);// 此点为三边形的起点
        path.lineTo(dianX + (bottom / juli * juliY), dianY
                - (bottom / juli * juliX));
        path.lineTo(dianX - (bottom / juli * juliY), dianY
                + (bottom / juli * juliX));
        path.close(); // 使这些点构成封闭的三边形
        canvas.drawPath(path, paint);
    }
}
