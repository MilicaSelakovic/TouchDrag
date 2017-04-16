package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;

import org.opencv.core.Point;

import java.util.Vector;

/**
 * Created by milica on 23.11.16..
 */

public class Polygon implements GeometricObject {
    Vector<Point> points;


    public Polygon(Vector<Point> points){
        this.points = new Vector<>(points);
    }


    @Override
    public void draw(Canvas canvas, Paint paint) {
        paint.setColor(Color.BLUE);
        Path path = new Path();
        path.moveTo((float) points.firstElement().x, (float) points.firstElement().y);

        for (Point p: points) {
            path.lineTo((float) p.x, (float) p.y);
        }

        canvas.drawPath(path, paint);
    }

    @Override
    public String toString() {
        return "Poligon " + Integer.toString(points.size());
    }
}
