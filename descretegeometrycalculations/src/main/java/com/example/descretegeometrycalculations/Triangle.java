package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.Vector;

public class Triangle extends Polygon {
    enum movingPoint {
        nothing, A, B, C, H, T, Oi, O, Sa, Sb, Sc
    };
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

    private movingPoint point = movingPoint.nothing;

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

//            Log.d("idiot", "la");

        }

        if(object instanceof Circle){

        }

        return ind;
    }

    public boolean isUnderCursor(float x, float y){
        if(A.isUnderCursor(x, y)){
            point = movingPoint.A;
            return true;
        }

        if(B.isUnderCursor(x, y)){
            point = movingPoint.B;
            return true;
        }

        if(C.isUnderCursor(x, y)){
            point = movingPoint.C;
            return true;
        }

        // TODO: 28.5.17. ispisati i za ostale tacke
        point = movingPoint.nothing;
        return false;
    }
    public void translate(float x, float y){
        switch (point){
            case A:
                A.translate(x, y);
                vertexMoved();
                break;
            case B:
                B.translate(x, y);
                vertexMoved();
                break;
            case C:
                C.translate(x, y);
                vertexMoved();
                break;
            default:
                return;
        }
    }


    private void vertexMoved(){
        if(symB != null)
            symB = GeometricConstructions.bisectorAngle(A, B, C);

        if(symA != null)
            symA = GeometricConstructions.bisectorAngle(C, A, B);

        if(symC != null)
            symC = GeometricConstructions.bisectorAngle(B, C, A);

        if(Oi != null) {
            Line l1 = GeometricConstructions.bisectorAngle(A, B, C);
            Line l2 = GeometricConstructions.bisectorAngle(C, A, B);
            Oi = GeometricConstructions.w03(l1, l2);
        }

        if(hb != null) {
            hb = GeometricConstructions.w10(B, new Line(A, C));
        }

        if(hc != null) {
            hc = GeometricConstructions.w10(C, new Line(A, B));
        }

        if(ha != null) {
            ha = GeometricConstructions.w10(A, new Line(C, B));
        }


        if(H != null){
            if(ha != null) {
                if(hb != null)
                    H = GeometricConstructions.w03(ha, hb);
                else
                    H = GeometricConstructions.w03(ha, hc);
            } else {
                H = GeometricConstructions.w03(hb, hc);
            }
        }

        if(Sb != null){
            Sb = GeometricConstructions.w01(A, A, C, 1.0f/2);
        }

        if(tb != null){
            tb = new Line(B, Sb);
        }


        if(Sc != null){
            Sc = GeometricConstructions.w01(A, A, B, 1.0f/2);
        }

        if(tc != null){
            tc = new Line(C, Sc);
        }

        if(Sa != null){
            Sa = GeometricConstructions.w01(B, B, C, 1.0f/2);
        }

        if(ta != null){
            ta = new Line(A, Sa);
        }


        if(T != null) {
            if (ta != null) {
                if (tb != null) {
                    T = GeometricConstructions.w03(ta, tb);
                } else if (tc != null) {
                    T = GeometricConstructions.w03(tc, ta);
                }
            } else if(tb != null && tc != null) {
                T = GeometricConstructions.w03(tc, ta);
            }
        }


        if(symAB != null){
            symAB = GeometricConstructions.w10(Sc, new Line(A, B));
        }


        if(symAC != null){
            symAC = GeometricConstructions.w10(Sb, new Line(A, C));
        }
        if(symBC != null){
            symBC = GeometricConstructions.w10(Sa, new Line(B, C));
        }

        if(O != null){
            GeomPoint S1 = GeometricConstructions.w01(A, A, C, 1.0f/2);
            GeomPoint S2 = GeometricConstructions.w01(A, A, B, 1.0f/2);
            Line s1 = GeometricConstructions.w10(S1, new Line(A, C));
            Line s2 = GeometricConstructions.w10(S2, new Line(A, B));

            O = GeometricConstructions.w03(s1, s2);
        }

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

            if(Oi == null) {
                if (symA != null) {
                    Oi = GeometricConstructions.w03(symA, symB);
                } else if (symC != null) {
                    Oi = GeometricConstructions.w03(symB, symC);
                }
            }
            return true;
        }


        if(ConnectionCalculations.isBisector(C, A, B, line)){
            if(symA == null)
                symA = GeometricConstructions.bisectorAngle(C, A, B);

            if(Oi == null) {
                if (symB != null) {
                    Oi = GeometricConstructions.w03(symA, symB);
                } else if (symC != null) {
                    Oi = GeometricConstructions.w03(symA, symC);
                }
            }
            return true;
        }

        if(ConnectionCalculations.isBisector(B, C, A, line)){
            if(symC == null)
                symC = GeometricConstructions.bisectorAngle(B, C, A);

            if(Oi == null) {
                if (symA != null) {
                    Oi = GeometricConstructions.w03(symA, symC);
                } else if (symB != null) {
                    Oi = GeometricConstructions.w03(symB, symC);
                }
            }
            return true;
        }


        return false;
    }

    private boolean altitude(Line line){
        if(ConnectionCalculations.altitude(A, B, C, line)){
            if(hb == null) {
                hb = GeometricConstructions.w10(B, new Line(A, C));
            }

            if(H == null){
                if(ha != null){
                    H = GeometricConstructions.w03(ha, hb);
                } else if(hc != null ){
                    H = GeometricConstructions.w03(hc, hb);
                }
            }

            return true;
        }

        if(ConnectionCalculations.altitude(B, C, A, line)){
            if(hc == null)
                hc = GeometricConstructions.w10(C, new Line(A, B));


            if(H == null){
                if(ha != null){
                    H = GeometricConstructions.w03(ha, hc);
                } else if(hb != null ){
                    H = GeometricConstructions.w03(hc, hb);
                }
            }
            return true;
        }

        if(ConnectionCalculations.altitude(C, A, B, line)){
            if(ha == null)
                ha = GeometricConstructions.w10(A, new Line(C, B));


            if(H == null){
                if(hb != null){
                    H = GeometricConstructions.w03(ha, hb);
                } else if(hc != null ){
                    H = GeometricConstructions.w03(hc, ha);
                }
            }

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


            if(T == null){
                if(ta != null){
                    T = GeometricConstructions.w03(ta, tb);
                } else if(tc != null ){
                    T = GeometricConstructions.w03(tc, tb);
                }
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


            if(T == null){
                if(ta != null){
                    T = GeometricConstructions.w03(ta, tc);
                } else if(tb != null ){
                    T = GeometricConstructions.w03(tc, tb);
                }
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


            if(T == null){
                if(tb != null){
                    T = GeometricConstructions.w03(ta, tb);
                } else if(tc != null ){
                    T = GeometricConstructions.w03(tc, ta);
                }
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

            if(O == null){
                if(symAC != null){
                    O = GeometricConstructions.w03(symAB, symAC);
                } else if(symBC != null){
                    O = GeometricConstructions.w03(symAB, symBC);
                }
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


            if(O == null){
                if(symAB != null){
                    O = GeometricConstructions.w03(symAB, symAC);
                } else if(symBC != null){
                    O = GeometricConstructions.w03(symAC, symBC);
                }
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


            if(O == null){
                if(symAC != null){
                    O = GeometricConstructions.w03(symBC, symAC);
                } else if(symAB != null){
                    O = GeometricConstructions.w03(symAB, symBC);
                }
            }

            return true;
        }

        return false;
    }

}
