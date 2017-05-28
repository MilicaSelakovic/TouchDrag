package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.Vector;

/**
 * Created by milica on 23.11.16..
 */

public class Polygon implements GeometricObject {
    Vector<GeomPoint> points;

    public Polygon(Vector<GeomPoint> points){
        this.points = new Vector<>(points);

    }


    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        Path path = new Path();
        path.moveTo( points.firstElement().X(), points.firstElement().Y());

        for (GeomPoint p: points) {
            path.lineTo( p.X(),  p.Y());
        }
        path.lineTo( points.firstElement().X(), points.firstElement().Y());
        canvas.drawPath(path, paint);
    }

    @Override
    public String toString() {
        return "Poligon " + Integer.toString(points.size());
    }


    public boolean connection(GeometricObject object){

        return false;

    }


    // veze sa segmentom pojedinacnim i to samo sa Linijama i tackama
    // mogu tacke preskeka da se generisu ?

    // trougao - krug
    // upisan, opisan

    // trougao - prava   done
    // simentrala ugla done
    // simetrala stranice
    // tezisna linija
    // visina


    // trougao tacka
    // teme
    // ortocentar
    // teziste
    // centri opisanog, upisanog kruga
    // sredine stranica
    //

}
