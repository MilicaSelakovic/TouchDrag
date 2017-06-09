package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Triangle extends Polygon {
    private HashMap<String, GeometricObject> znacajniObjecti;

    private String movingPoint = "";
    private boolean isMovingSet = false;

    public Triangle(Vector<GeomPoint> points) {
        super(points);
        // TODO proveriti da li ih je 30 na kraju
        znacajniObjecti = new HashMap<>(30);
        fillMap(points);
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        super.draw(canvas, paint, finished);
        for (Map.Entry<String, GeometricObject> entry: znacajniObjecti.entrySet()){
            if(entry.getValue() != null)
                entry.getValue().draw(canvas, paint, finished);
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

        }

        if(object instanceof Circle){

        }

        return ind;
    }

    public boolean isUnderCursor(float x, float y){

        for(Map.Entry<String, GeometricObject> entry : znacajniObjecti.entrySet()){
            if(entry.getValue() == null)
                continue;
            if( entry.getValue().isUnderCursor(x, y)){
                movingPoint = entry.getKey();
                isMovingSet = true;
                return true;
            }
        }

        isMovingSet = false;

        return false;
    }

    public void translate(float x, float y){
        if(isMovingSet) {
            znacajniObjecti.get(movingPoint).translate(x, y);
            vertexMoved();
        }
    }


    private void vertexMoved(){

        for(Map.Entry<String, GeometricObject> entry : znacajniObjecti.entrySet()){
            if (entry.getKey() == "A" || entry.getKey() == "B" || entry.getKey() == "C"){
                continue;
            }

            ((SignificantObject)entry.getValue()).construct();
        }

    }


    private boolean bisector(Line line){
        GeomPoint A = (GeomPoint) znacajniObjecti.get("A");
        GeomPoint B = (GeomPoint) znacajniObjecti.get("B");
        GeomPoint C = (GeomPoint) znacajniObjecti.get("C");


        if(ConnectionCalculations.isBisector(A, B, C, line)){
            ((SignificantObject)znacajniObjecti.get("symB")).setVisible(true);


            if(((SignificantObject) znacajniObjecti.get("symC")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("symA")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("I")).setVisible(true);
            }
            return true;
        }


        if(ConnectionCalculations.isBisector(C, A, B, line)){
            ((SignificantObject)znacajniObjecti.get("symA")).setVisible(true);


            if(((SignificantObject) znacajniObjecti.get("symC")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("symB")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("I")).setVisible(true);
            }
            return true;
        }

        if(ConnectionCalculations.isBisector(B, C, A, line)){
            ((SignificantObject)znacajniObjecti.get("symC")).setVisible(true);


            if(((SignificantObject) znacajniObjecti.get("symB")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("symA")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("I")).setVisible(true);
            }
            return true;
        }


        return false;
    }

    private boolean altitude(Line line){
        GeomPoint A = (GeomPoint) znacajniObjecti.get("A");
        GeomPoint B = (GeomPoint) znacajniObjecti.get("B");
        GeomPoint C = (GeomPoint) znacajniObjecti.get("C");

        if(ConnectionCalculations.altitude(A, B, C, line)){
            ((SignificantObject) znacajniObjecti.get("Hb")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("hb")).setVisible(true);

            if(((SignificantObject) znacajniObjecti.get("ha")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("hc")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("H")).setVisible(true);
            }
            return true;
        }

        if(ConnectionCalculations.altitude(B, C, A, line)){
            ((SignificantObject) znacajniObjecti.get("Hc")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("hc")).setVisible(true);

            if(((SignificantObject) znacajniObjecti.get("ha")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("hb")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("H")).setVisible(true);
            }
            return true;
        }

        if(ConnectionCalculations.altitude(C, A, B, line)){
            ((SignificantObject) znacajniObjecti.get("Ha")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("ha")).setVisible(true);

            if(((SignificantObject) znacajniObjecti.get("hb")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("hc")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("H")).setVisible(true);
            }
            return true;
        }

        return false;
    }

    private boolean centroid(Line line){
        GeomPoint A = (GeomPoint) znacajniObjecti.get("A");
        GeomPoint B = (GeomPoint) znacajniObjecti.get("B");
        GeomPoint C = (GeomPoint) znacajniObjecti.get("C");

        if(ConnectionCalculations.centroidLine(A, B, C, line)){
            ((SignificantObject) znacajniObjecti.get("Sb")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("tb")).setVisible(true);

            if(((SignificantObject) znacajniObjecti.get("ta")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("tc")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("T")).setVisible(true);
            }
            return  true;
        }

        if(ConnectionCalculations.centroidLine(B, C, A, line)){
            ((SignificantObject) znacajniObjecti.get("Sc")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("tc")).setVisible(true);


            if(((SignificantObject) znacajniObjecti.get("ta")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("tb")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("T")).setVisible(true);
            }
            return  true;
        }


        if(ConnectionCalculations.centroidLine(C, A, B, line)){
            ((SignificantObject) znacajniObjecti.get("Sa")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("ta")).setVisible(true);


            if(((SignificantObject) znacajniObjecti.get("tb")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("tc")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("T")).setVisible(true);
            }
            return  true;
        }


        return false;
    }

    private boolean prepBisector(Line line){
        GeomPoint A = (GeomPoint) znacajniObjecti.get("A");
        GeomPoint B = (GeomPoint) znacajniObjecti.get("B");
        GeomPoint C = (GeomPoint) znacajniObjecti.get("C");

        if(ConnectionCalculations.isSegmentCentar(A, B, line)){
            ((SignificantObject) znacajniObjecti.get("Sc")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("symAB")).setVisible(true);


            if(((SignificantObject) znacajniObjecti.get("symBC")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("symAC")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("O")).setVisible(true);
            }

            return true;
        }


        if(ConnectionCalculations.isSegmentCentar(C, A, line)){
            ((SignificantObject) znacajniObjecti.get("Sb")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("symAC")).setVisible(true);


            if(((SignificantObject) znacajniObjecti.get("symBC")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("symAB")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("O")).setVisible(true);
            }
            return true;
        }



        if(ConnectionCalculations.isSegmentCentar( B, C, line)){
            ((SignificantObject) znacajniObjecti.get("Sa")).setVisible(true);
            ((SignificantObject) znacajniObjecti.get("symBC")).setVisible(true);


            if(((SignificantObject) znacajniObjecti.get("symAB")).isVisible() ||
                    ((SignificantObject) znacajniObjecti.get("symAC")).isVisible()){
                ((SignificantObject) znacajniObjecti.get("O")).setVisible(true);
            }

            return true;
        }

        return false;
    }

    private void  fillMap(Vector<GeomPoint> points){
        GeomPoint A = points.elementAt(0);
        GeomPoint B = points.elementAt(1);
        GeomPoint C = points.elementAt(2);

        Incenter I = new Incenter(A, B, C);
        Circumcenter O = new Circumcenter(A, B, C);
        znacajniObjecti.put("A", A);
        znacajniObjecti.put("B", B);
        znacajniObjecti.put("C", C);
        znacajniObjecti.put("H", new Orthocenter(A, B, C));
        znacajniObjecti.put("Ha", new FootOfAltitude(C, A, B));
        znacajniObjecti.put("Hb", new FootOfAltitude(A, B, C));
        znacajniObjecti.put("Hc", new FootOfAltitude(B, C, A));
        znacajniObjecti.put("T", new Centroid(A, B, C));
        znacajniObjecti.put("I", I);
        znacajniObjecti.put("O", O);
        znacajniObjecti.put("Sa", new Midpoint(C, A, B));
        znacajniObjecti.put("Sb", new Midpoint(A, B, C));
        znacajniObjecti.put("Sc", new Midpoint(B, C, A));

        znacajniObjecti.put("symBC", new PerpBisector(C, A, B));
        znacajniObjecti.put("symAC", new PerpBisector(A, B, C));
        znacajniObjecti.put("symAB", new PerpBisector(B, C, A));
        znacajniObjecti.put("symA", new AngleBisector(C, A, B));
        znacajniObjecti.put("symB", new AngleBisector(A, B, C));
        znacajniObjecti.put("symC", new AngleBisector(B, C, A));

        znacajniObjecti.put("ta", new Median(C, A, B));
        znacajniObjecti.put("tb", new Median(A, B, C));
        znacajniObjecti.put("tc", new Median(B, C, A));

        znacajniObjecti.put("ha", new Altitude(C, A, B));
        znacajniObjecti.put("hb", new Altitude(A, B, C));
        znacajniObjecti.put("hc", new Altitude(B, C, A));

        znacajniObjecti.put("ki", new Incircle(I, A, B, C));
        znacajniObjecti.put("kc", new CircumscribedCircle(O, A));
    }
}
