package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by milica on 27.11.16..
 */

public class GeomPoint implements GeometricObject{
    private PointF point;

    public GeomPoint(PointF p){
        point = p;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPoint(point.x, point.y, paint);
    }

    @Override
    public String toString() {
        return "Tacka";
    }
}
