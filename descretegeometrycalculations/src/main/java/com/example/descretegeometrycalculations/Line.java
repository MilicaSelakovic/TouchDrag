package com.example.descretegeometrycalculations;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.support.annotation.NonNull;


import org.opencv.core.Mat;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

/**
 * Created by milica on 23.11.16..
 */

public class Line implements GeometricObject {
    private GeomPoint begin;
    private GeomPoint end;
    private float k;
    private float n;

    private GeomPoint vector; // normiran vektor pravca
    private TreeSet<Line> parallel = new TreeSet<>();

    public Line(GeomPoint x, GeomPoint y){
        begin = x ;
        end = y;
        float vX = end.X() - begin.X();
        float vY = end.Y() - begin.Y();
        float norm = (float) Math.sqrt(vX*vX + vY*vY);
        vector = new GeomPoint(vX / norm, vY/norm);

    }

    private float yCoord(float x, float y){

        if(Math.abs(end.X() - begin.X()) <= 10e-5){
            return y;
        }

        return (end.Y() - begin.Y()) / (end.X() - begin.X()) * (x - begin.X()) + begin.Y();
    }

   @Override
    public void draw(Canvas canvas, Paint paint) {
       float x1 = 0;
       float x2  = canvas.getWidth();

       if(Math.abs(end.X() - begin.X()) <= 10e-5){
           x2 = begin.X();
           x1 = x2;
       }
       int y = canvas.getHeight();
       canvas.drawLine( x1, yCoord(x1, 0), x2, yCoord(x2, y), paint);
    }

    @Override
    public String toString() {
        return "Linija";
    }


    public GeomPoint getVector() {
        return vector;
    }

    public double distance(GeomPoint point){
        double n1 = end.Y() - begin.Y();
        double n2 = begin.X() - end.X();
        double n3 = -begin.Y()*n2 - begin.X()*n1;

        double d = Math.abs(n1*point.X() + n2*point.Y() + n3);
        double norm = Math.sqrt(n1*n1 + n2*n2);
        return d/norm;
    }

    public boolean contain(GeomPoint point){
        return  Math.abs((point.Y() - begin.Y())*(end.X() - begin.X()) - (end.Y() - begin.Y())*(point.X() - begin.X())) < EPISLON;
    }
    public void connection(GeometricObject object){

    }

    //TODO Veze sa linijom

    // paralela
    // normala
    // presek ?
}
