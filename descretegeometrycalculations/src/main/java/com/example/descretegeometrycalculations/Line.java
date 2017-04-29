package com.example.descretegeometrycalculations;


import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;



import java.util.Vector;

/**
 * Created by milica on 23.11.16..
 */

public class Line implements GeometricObject {
    private GeomPoint begin;
    private GeomPoint end;

    private GeomPoint vector; // normiran vektor pravca

    public Line(GeomPoint x, GeomPoint y){
        begin = x ;
        end = y;
        float vX = end.X() - begin.Y();
        float vY = end.X() - begin.Y();
        float norm = (float) Math.sqrt(vX*vX + vY*vY);
        vector = new GeomPoint(vX / norm, vY/norm);
    }

   @Override
    public void draw(Canvas canvas, Paint paint) {
       paint.setColor(Color.BLUE);
        canvas.drawLine( begin.X(), begin.Y(), end.X(), end.Y(), paint);
    }

    @Override
    public String toString() {
        return "Linija";
    }


    public GeomPoint getVector() {
        return vector;
    }

    public boolean belongTo(GeomPoint point){
        return  Math.abs((point.Y() - begin.Y())*(end.X() - begin.X()) - (end.Y() - begin.Y())*(point.X() - begin.X())) < EPISLON;
    }
    public void connection(GeometricObject object){

    }

    //TODO Veze sa linijom

    // paralela
    // normala
    // presek ?
}
