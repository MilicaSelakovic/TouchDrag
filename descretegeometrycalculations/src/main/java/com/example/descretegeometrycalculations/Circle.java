package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Circle implements GeometricObject {


    private GeomPoint center;

    private String id;

    private double radius;


    Circle(double x, double y, double r) {
        center = new GeomPoint((float) x, (float) y);
        radius = r;
    }

    Circle(GeomPoint X, double r){
        this.center = X;
        this.radius = r;
    }

    public GeomPoint getCenter() {
        return center;
    }

    public void setCenter(GeomPoint center) {
        this.center = center;
    }


    public double getRadius() {
        return radius;
    }
    public void setRadius(double radius) {
        this.radius = radius;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose, PointInformations pointInformations) {
        if (finished)
            center.draw(canvas, paint, finished, choose, pointInformations);
        canvas.drawCircle(center.X(), center.Y(), (float) radius, paint);
    }

    @Override
    public String toString() {
        return "Krug  " + Double.toString(radius);
    }

    public boolean choose(float x, float y, HashMap<String, Vector<String>> trics) {
        return false;
    }


    public boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects) {
        if (object instanceof GeomPoint) {
            if (contain((GeomPoint) object)) {
                for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
                    if (entry.getValue() instanceof Circle && entry.getValue() != this &&
                            ((Circle) entry.getValue()).contain((GeomPoint) object)) {
                        GeomPoint P1 = new GeomPoint(0, 0);
                        GeomPoint P2 = new GeomPoint(0, 0);
                        int d = GeometricConstructions.w07(this, (Circle) entry.getValue(), P1, P2);

                        if (d == 2) {
                            if (P1.distance((GeomPoint) object) < P2.distance((GeomPoint) object)) {
                                ((GeomPoint) object).setX(P1.X());
                                ((GeomPoint) object).setY(P1.Y());
                                commands.add("w07 " + object.getId() + " " + "R " + getId() + " " + entry.getValue().getId());
                                ((GeomPoint) object).setMove(false);

                                return true;

                            } else {
                                ((GeomPoint) object).setX(P2.X());
                                ((GeomPoint) object).setY(P2.Y());
                                commands.add("w07 " + "R " + object.getId() + " " + getId() + " " + entry.getValue().getId());
                                ((GeomPoint) object).setMove(false);

                                return true;
                            }
                        }

                        if (d == 1) {
                            ((GeomPoint) object).setX(P1.X());
                            ((GeomPoint) object).setY(P1.Y());
                            commands.add("w07 " + object.getId() + " " + "R " + getId() + " " + entry.getValue().getId());
                            ((GeomPoint) object).setMove(false);

                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }


    public boolean isUnderCursor(float x, float y){
       return false;
    }

    public void translate(float x, float y) {
    }


    public boolean equal(Circle circle) {
        double d = center.distance(circle.getCenter());
        double dr = Math.abs(radius - circle.getRadius());
        return d < 50 && dr < 170;
    }

    @Override
    public void scale(float scaleFactor) {
        radius *= scaleFactor;
    }

    public boolean contain(GeomPoint point) {
        double x2 = Math.pow(point.X() - center.X(), 2);
        double y2 = Math.pow(point.Y() - center.Y(), 2);
        double r2 = radius * radius;
        double d = Math.abs(x2 + y2) / r2;
        return d > 0.9 && d < 1.1;
    }

}
