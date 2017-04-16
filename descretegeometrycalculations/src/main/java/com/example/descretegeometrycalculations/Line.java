package com.example.descretegeometrycalculations;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import org.opencv.core.Point;

import java.util.Vector;

/**
 * Created by milica on 23.11.16..
 */

public class Line implements GeometricObject {
    private Point begin;
    private Point end;

    public Line(Point x, Point y){
        begin = x;
        end = y;
    }

   @Override
    public void draw(Canvas canvas, Paint paint) {
       paint.setColor(Color.BLUE);
        canvas.drawLine( (float) begin.x, (float) begin.y, (float) end.x, (float) end.y, paint);
    }

    @Override
    public String toString() {
        return "Linija";
    }
}
