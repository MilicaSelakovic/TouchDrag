package com.example.descretegeometrycalculations;

import android.util.Log;

import java.util.Vector;

public class Triangle extends Polygon {
    private GeomPoint A, B, C;
//    private GeomPoint H = null, T = null, Oi = null, O = null, Sa = null, Sb = null, Sc = null;

    public Triangle(Vector<GeomPoint> points) {
        super(points);

        A = points.elementAt(0);
        B = points.elementAt(1);
        C = points.elementAt(2);
        boolean t = jednakostranican(A, B, C);
        if (t)
            Log.d("Jednakostranican", "jeste");

    }

    boolean jednakostranican(GeomPoint A, GeomPoint B, GeomPoint C){
        double AB = Math.sqrt(ConnectionCalculations.dotProduct(A, B));
        double AC = Math.sqrt(ConnectionCalculations.dotProduct(A, C));
        double BC = Math.sqrt(ConnectionCalculations.dotProduct(C, B));

        // TODO konstanta epsilon
        double eps = 100;
        Log.d("a", Double.toString(AB));
        Log.d("b", Double.toString(AC));
        Log.d("c", Double.toString(BC));
        if( Math.abs(AB - AC) < eps  && Math.abs(AC- BC) < eps
                && Math.abs(AB-BC) < eps) {
            double k = Math.sqrt(3)/2;
            double x = (A.X() + B.X())/2;
            double y = (A.Y() + B.Y())/2;

            double dx = k*(A.Y() - B.Y());
            double dy = k*(A.X() - B.X());

            if( Math.abs(C.X() - x - dx) < Math.abs(C.X() - x + dx) ){
                C.setX((float) (x + dx));
                C.setY((float) (y - dy));
            } else {
                C.setX((float) (x - dx));
                C.setY((float)(y + dy));
            }

            return true;
        }

        return false;
    }

}
