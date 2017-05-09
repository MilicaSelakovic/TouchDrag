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


    public void connection(GeometricObject object){

        if(points.size() == 3){
            if(object instanceof Line){
                if(angleSymetry((Line) object)){
                    Log.d("Simetrala", "ugla");
                }
            }
        }

    }

    private boolean trianglePoint(GeomPoint point){
        for(GeomPoint p : points){
            double d = Math.sqrt(Math.pow(point.X() - p.X(), 2) + Math.pow(point.Y() - p.Y(), 2));
            if(d < EPISLON){
                return true;
            }
        }

        return false;
    }

    private boolean angleSymetry(Line line){
        int n = points.size();

        for(int i = 0; i < n; i++){
            if(ConnectionCalculations.isSymetry(points.elementAt(i), points.elementAt((i+1)%n), points.elementAt((i+2)%n), line)){
                return true;
            }
        }

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
