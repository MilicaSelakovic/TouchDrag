package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Vector;

public class Triangle extends Polygon {
    private GeomPoint A, B, C;
    private GeomPoint H = null; // ortocentar
    private GeomPoint T = null; // teziste
    private GeomPoint Oi = null; // centar upisanog
    private GeomPoint O = null; // centar opisanog
    private GeomPoint Sa = null; // centar stranice BC
    private GeomPoint Sb = null; // centar stanice AC
    private GeomPoint Sc = null; // centar stranice AB

    private Line symA = null, symB = null, symC = null; // simetrale ugla kod temena
    private Line symAB = null, symAC = null, symBC = null; // simetrale stranica
    private Line ta = null, tb = null, tc = null; // tezisne linije
    private Line ha = null, hb = null, hc = null; // visine trougla
    private Circle inscribed = null, circumscribed = null; // upisan i opisan krug


    public Triangle(Vector<GeomPoint> points) {
        super(points);

        A = points.elementAt(0);
        B = points.elementAt(1);
        C = points.elementAt(2);
        //boolean t = jednakostranican(A, B, C);
        //if (t)
         //   Log.d("Jednakostranican", "jeste");

    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        super.draw(canvas, paint, finished);
        A.draw(canvas, paint, finished);
        B.draw(canvas, paint, finished);
        C.draw(canvas, paint, finished);

        if(H != null){
            H.draw(canvas, paint, finished);
        }

        if(T != null){
            T.draw(canvas, paint, finished);
        }

        if(Oi != null){
            Oi.draw(canvas, paint, finished);
        }

        if(O != null){
            O.draw(canvas, paint, finished);
        }

        if(Sa != null){
            Sa.draw(canvas, paint, finished);
        }


        if(Sb != null){
            Sb.draw(canvas, paint, finished);
        }


        if(Sc != null){
            Sc.draw(canvas, paint, finished);
        }

        if(symA != null){
            symA.draw(canvas, paint, finished);
        }

        if(symB != null){
            symB.draw(canvas, paint, finished);
        }

        if(symC != null){
            symC.draw(canvas, paint, finished);
        }

        if(symAB != null){
            symAB.draw(canvas, paint, finished);
        }

        if(symBC != null){
            symBC.draw(canvas, paint, finished);
        }

        if(symAC != null){
            symAC.draw(canvas, paint, finished);
        }

        if(ta != null){
            ta.draw(canvas, paint, finished);
        }

        if(tb != null){
            tb.draw(canvas, paint, finished);
        }

        if(tc != null){
            tc.draw(canvas, paint, finished);
        }

        if(ha != null){
            ha.draw(canvas, paint, finished);
        }

        if(hb != null){
            hb.draw(canvas, paint, finished);
        }

        if(hc != null){
            hc.draw(canvas, paint, finished);
        }

        if(inscribed != null){
            inscribed.draw(canvas, paint, finished);
        }

        if (circumscribed != null){
            circumscribed.draw(canvas,paint, finished);
        }

    }

    @Override
    public boolean connection(GeometricObject object) {
        boolean ind = false;
        if(object instanceof Line){
            Line l = (Line) object;
            if(bisector(l)){
                Log.d("Simetrala", "ugla");
                ind = true;
//                return true;
            }

            if( centroid(l)){
                Log.d("Tezisna", "linija");
                ind = true;
            }

            if( altitude(l)){
                Log.d("Visina", "trougla");
                ind = true;
            }

            if (prepBisector(l)){
                Log.d("Srediste", "stranice");
                ind = true;
            }

            Log.d("idiot", "la");

        }

        if(object instanceof Circle){

        }

        return ind;
    }

    private boolean jednakostranican(GeomPoint A, GeomPoint B, GeomPoint C){
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

    private boolean trianglePoint(GeomPoint point){
        for(GeomPoint p : points){
            double d = Math.sqrt(Math.pow(point.X() - p.X(), 2) + Math.pow(point.Y() - p.Y(), 2));
            if(d < EPISLON){
                return true;
            }
        }

        return false;
    }

    private boolean bisector(Line line){
        if(ConnectionCalculations.isBisector(A, B, C, line)){
            if(symB == null)
                symB = GeometricConstructions.bisectorAngle(A, B, C);
            return true;
        }


        if(ConnectionCalculations.isBisector(C, A, B, line)){
            if(symA == null)
                symA = GeometricConstructions.bisectorAngle(C, A, B);
            return true;
        }

        if(ConnectionCalculations.isBisector(B, C, A, line)){
            if(symC == null)
                symC = GeometricConstructions.bisectorAngle(B, C, A);
            return true;
        }


        return false;
    }

    private boolean altitude(Line line){
        if(ConnectionCalculations.altitude(A, B, C, line)){
            if(hb == null) {
                hb = GeometricConstructions.w10(B, new Line(A, C));
            }
            return true;
        }

        if(ConnectionCalculations.altitude(B, C, A, line)){
            if(hc == null)
                hc = GeometricConstructions.w10(C, new Line(A, B));
            return true;
        }

        if(ConnectionCalculations.altitude(C, A, B, line)){
            if(ha == null)
                ha = GeometricConstructions.w10(A, new Line(C, B));
            return true;
        }

        return false;
    }

    private boolean centroid(Line line){
        if(ConnectionCalculations.centroidLine(A, B, C, line)){
            if(Sb == null){
                Sb = GeometricConstructions.w01(A, A, C, 1.0f/2);
            }

            if(tb == null){
                tb = new Line(B, Sb);
            }
            return  true;
        }

        if(ConnectionCalculations.centroidLine(B, C, A, line)){
            if(Sc == null){
                Sc = GeometricConstructions.w01(A, A, B, 1.0f/2);
            }

            if(tc == null){
                tc = new Line(C, Sc);
            }
            return  true;
        }


        if(ConnectionCalculations.centroidLine(C, A, B, line)){
            if(Sa == null){
                Sa = GeometricConstructions.w01(B, B, C, 1.0f/2);
            }

            if(ta == null){
                ta = new Line(A, Sa);
            }
            return  true;
        }


        return false;
    }

    private boolean prepBisector(Line line){
        if(ConnectionCalculations.isSegmentCentar(A, B, line)){
            if(Sc == null){
                Sc = GeometricConstructions.w01(A, A, B, 1.0f/2);
            }

            if(symAB == null){
                symAB = GeometricConstructions.w10(Sc, new Line(A, B));
            }

            return true;
        }


        if(ConnectionCalculations.isSegmentCentar(C, A, line)){
            if(Sb == null){
                Sb = GeometricConstructions.w01(A, A, C, 1.0f/2);
            }

            if(symAC == null){
                symAC = GeometricConstructions.w10(Sb, new Line(A, C));
            }

            return true;
        }



        if(ConnectionCalculations.isSegmentCentar( B, C, line)){
            if(Sa == null){
                Sa = GeometricConstructions.w01(B, B, C, 1.0f/2);
            }

            if(symBC == null){
                symBC = GeometricConstructions.w10(Sa, new Line(B, C));
            }

            return true;
        }

        return false;
    }

}
