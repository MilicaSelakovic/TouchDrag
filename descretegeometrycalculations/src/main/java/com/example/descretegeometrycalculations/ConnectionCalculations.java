package com.example.descretegeometrycalculations;

import android.util.Log;

class ConnectionCalculations {


    private static float dotProduct(GeomPoint x, GeomPoint y) {
        return x.X()*y.X() + x.Y()*y.Y();
    }

    private static float crossProductNorm(GeomPoint point1, GeomPoint point2){
        return point1.X()*point2.Y() - point1.Y()*point2.X();
    }

    private static float angle(GeomPoint point1, GeomPoint point2){
        float dot = dotProduct(point1, point2);

        float norm1 = (float) Math.sqrt(point1.X()*point1.X() + point1.Y()*point1.Y());
        float norm2 = (float) Math.sqrt(point2.X()*point2.X() + point2.Y()*point2.Y());

        if(norm1 <= 10e-5 || norm2 <= 10e-5){
            return 0;
        }

        float angle = (float) Math.acos(dot/(norm1*norm2));

        if(angle > Math.PI/2){
            return angle - (float) Math.PI/2;
        }

        return angle;
    }

    // Veze izmedju dve Linije

    public static boolean parallelLine(Line l1, Line l2){
        return Math.abs(crossProductNorm(l1.getVector(), l2.getVector())) <= GeometricObject.EPISLON;
    }

    public static boolean normalLine(Line l1, Line l2) {
        float dot = dotProduct(l1.getVector(), l2.getVector());
        // TODO konstanta
        return Math.abs(dot) <= 0.1;
    }


    // Veze izmedju trougla i linije

    static boolean isBisector(GeomPoint A, GeomPoint B, GeomPoint C, Line l){

        if(!l.contain(B)) {
            return false;
        }
        float angleA = angle(l.getVector(), new GeomPoint(B.X()-A.X(), B.Y()-A.Y()));
        float angleB = angle(l.getVector(), new GeomPoint(B.X()-C.X(), B.Y()-C.Y()));
        // TODO konstanta
        return Math.abs(angleA-angleB) <= 0.1;
    }


    static boolean isSegmentCentar(GeomPoint A, GeomPoint B, Line l){
        if(!normalLine(new Line(A, B), l)){
            return false;
        }


        GeomPoint center = new GeomPoint((A.X() + B.X())/2, (A.Y() + B.Y())/2);
        return l.contain(center);

    }

    static boolean centroidLine(GeomPoint A, GeomPoint B, GeomPoint C, Line l){

        if(!l.contain(B)){
            return false;
        }

        GeomPoint center = new GeomPoint((A.X() + C.X())/2, (A.Y() + C.Y())/2);
        return l.contain(center);
    }

    static boolean altitude(GeomPoint A, GeomPoint B, GeomPoint C, Line l) {
        // proverava da li je l visina iz tacke B (hb)
        return l.contain(B) && normalLine(l, new Line(A, C));

    }

    //

    public static boolean segmentContains(GeomPoint A, GeomPoint B, GeomPoint C){
        // proverava da li C pripada segmentu A B

        Line ab = new Line(A, B);

        if(!ab.contain(C)){
            return false;
        }

        boolean xACB = A.X() <= C.X() && B.X() <= C.X();
        boolean xBCA = A.X() >= C.X() && B.X() >= C.X();
        boolean yACB = A.Y() <= C.Y() && B.Y() <= C.Y();
        boolean yBCA = A.Y() <= C.Y() && B.Y() <= C.Y();

        return (xACB || xBCA) && (yACB || yBCA);
    }



}
