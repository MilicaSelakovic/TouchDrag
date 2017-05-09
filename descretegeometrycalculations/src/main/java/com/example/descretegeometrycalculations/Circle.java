package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Vector;

/**
 * Created by milica on 23.11.16..
 */

public class Circle implements GeometricObject {

    private GeomPoint center;
    private double radius;

    private Vector<Line> tangentLines = new Vector<>();

    public Circle(double x, double y, double r) {
        center = new GeomPoint((float) x, (float) y);
        radius = r;
    }

    public GeomPoint getCenter() {
        return center;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        if (finished)
            center.draw(canvas, paint, false);
        canvas.drawCircle(center.X(), center.Y(), (float) radius, paint);
    }

    @Override
    public String toString() {
        return "Krug  " + Double.toString(radius);
    }

    public boolean pointBelong(GeomPoint point) {
        double x = point.X() - center.X();
        double y = point.Y() - center.Y();
        double d = Math.sqrt(x * x + y * y) - radius;
        Log.d("Tacka", Double.toString(d));
        return Math.abs(d) <= 10; // TODO promeniti ovu konstantu
    }

    public void connection(GeometricObject object) {
        if (object instanceof Line) {
            connectionLine((Line) object);
            Log.d("Linija", "da");
        }
    }

    private void connectionLine(Line line) {
        // veze kruga i linije
        if (ConnectionCalculations.tangentLine(this, line)) {
            tangentLines.add(line);
            Log.d("Tangenta", "da");
        }

    }


    //TODO Veze

    // tangenta
    //
}
