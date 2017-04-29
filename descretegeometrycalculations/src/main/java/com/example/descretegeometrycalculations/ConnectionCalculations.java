package com.example.descretegeometrycalculations;

import org.opencv.core.Point;

/**
 * Created by milica on 29.4.17..
 */

public class ConnectionCalculations {



    private static float dotProduct(GeomPoint x, GeomPoint y){
        return x.X()*y.X() + x.Y()*y.Y();
    }

    private static float crossProductNorm(GeomPoint point1, GeomPoint point2){
        return point1.X()*point2.Y() - point1.Y()*point2.X();
    }

    private static float angle(GeomPoint point1, GeomPoint point2){
        float dot = dotProduct(point1, point2);

        float norm1 = (float) Math.sqrt(point1.X()*point1.X() + point1.Y()*point1.Y());
        float norm2 = (float) Math.sqrt(point2.X()*point2.X() + point2.Y()*point2.Y());

        if(norm1 <= 0.00000001 || norm2 <= 0.00000000001){
            return 0;
        }

        float angle = (float) Math.acos(dot/(norm1*norm2));

        if(angle > Math.PI/2){
            return angle - (float) Math.PI/2;
        }

        return angle;
    }

    // Veze izmedju Kruga i Linije
    public static boolean tangentLine(Circle circle, Line line){

        GeomPoint v = line.getVector();
        GeomPoint center = circle.getCenter();

        float dotVC = dotProduct(v, center);
        float dotVV = dotProduct(v, v);

        GeomPoint orthogonalProj = new GeomPoint(dotVC*v.X()/dotVV, dotVC*v.Y()/dotVV);

        return circle.pointBelong(orthogonalProj);
    }

    // Veze izmedju dve Linije

    public static boolean parallelLine(Line l1, Line l2){
        return Math.abs(crossProductNorm(l1.getVector(), l2.getVector())) <= GeometricObject.EPISLON;
    }

    public static boolean normalLine(Line l1, Line l2){
        float dot = dotProduct(l1.getVector(), l2.getVector());
        return Math.abs(dot) <= GeometricObject.EPISLON;
    }


    // Veze izmedju trougla i linije

    public static boolean isSymetry(GeomPoint A, GeomPoint B, GeomPoint C, Line l){
        // proverava da li je prava l simetrala ugla ABC

        if(!l.belongTo(B))
            return false;
        float andleA = angle(l.getVector(), new GeomPoint(B.X()-A.X(), B.Y()-A.Y()));
        float angleB = angle(l.getVector(), new GeomPoint(B.X()-A.X(), B.Y()-A.Y()));

        return Math.abs(andleA-angleB) <= GeometricObject.EPISLON;
    }

    public static boolean isSegmentCentar(GeomPoint A, GeomPoint B, Line l){
        if(!normalLine(new Line(A, B), l)){
            return false;
        }


        GeomPoint center = new GeomPoint((A.X() + B.X())/2, (A.Y() + B.Y())/2);
        return l.belongTo(center);

    }

    public static boolean centroidLine(GeomPoint A, GeomPoint B, GeomPoint C, Line l){
        // proverava da li je l tezisna linija koja prolazi kroz teme B

        if(!l.belongTo(B)){
            return false;
        }

        GeomPoint center = new GeomPoint((A.X() + C.X())/2, (A.Y() + C.Y())/2);
        return l.belongTo(center);
    }

    public static boolean altitude(GeomPoint A, GeomPoint B, GeomPoint C, Line l){
        // proverava da li je l visina iz tacke B (hb)
        if(!l.belongTo(B)){
            return false;
        }

        return normalLine(l, new Line(A,C));

    }



}
