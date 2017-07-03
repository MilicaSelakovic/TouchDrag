package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Triangle extends Polygon {
    String id;
    private HashMap<String, GeometricObject> significatObjects;
    private GeomPoint A, B, C;
    private String movingPoint = "";
    private boolean isMovingSet = false;


    private Line a, b, c;

    public Triangle(Vector<GeomPoint> points) {
        super(points);
        // TODO proveriti da li ih je 30 na kraju
        significatObjects = new HashMap<>(30);
        // fillMap(points);
        A = points.elementAt(0);
        B = points.elementAt(1);
        C = points.elementAt(2);
        a = new Line(B, C);
        b = new Line(A, C);
        c = new Line(B, C);
//        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
//            if (entry.getKey() == "A" || entry.getKey() == "B" || entry.getKey() == "C") {
//                continue;
//            }
//
//            ((SignificantObject) entry.getValue()).setVisible(true);
//        }
    }

    public void setIDLines(String aId, String bId, String cId) {
        a.setId(aId);
        b.setId(bId);
        c.setId(cId);
    }

    public Line getLineA() {
        return a;
    }

    public void setLineA(Line a) {
        this.a = a;
    }

    public Line getLineB() {
        return b;
    }

    public void setLineB(Line b) {
        this.b = b;
    }

    public Line getLineC() {
        return c;
    }

    public void setLineC(Line c) {
        this.c = c;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        super.draw(canvas, paint, finished);
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if(entry.getValue() != null)
                entry.getValue().draw(canvas, paint, finished);
        }

    }


    @Override
    public boolean connection(GeometricObject object) {
//        boolean ind = false;
//        if(object instanceof Line){
//            Line l = (Line) object;
//            if(bisector(l)){
//                Log.d("Simetrala", "ugla");
//                ind = true;
//            }
//
//            if( centroid(l)){
//                Log.d("Tezisna", "linija");
//                ind = true;
//            }
//
//            if( altitude(l)){
//                Log.d("Visina", "trougla");
//                ind = true;
//            }
//
//            if (prepBisector(l)){
//                Log.d("Srediste", "stranice");
//                ind = true;
//            }
//
//        }
//
//        if(object instanceof Circle){
//
//        }
//
//        return ind;
        return false;
    }

    public boolean connection(GeometricObject object, Vector<String> commands) {
        boolean ind = false;
        if(object instanceof Line){
            Line l = (Line) object;
            if (bisector(l, commands)) {
                Log.d("Simetrala", "ugla");
                ind = true;
            }

            if (centroid(l, commands)) {
                Log.d("Tezisna", "linija");
                ind = true;
            }

            if (altitude(l, commands)) {
                Log.d("Visina", "trougla");
                ind = true;
            }

            if (prepBisector(l, commands)) {
                Log.d("Srediste", "stranice");
                ind = true;
            }

        }

        if(object instanceof Circle){

        }

        return ind;
    }
    public boolean isUnderCursor(float x, float y){

        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
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
            significatObjects.get(movingPoint).translate(x, y);
//            vertexMoved();
        }
    }


//    private void vertexMoved(){
//
//        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
//            if (entry.getKey() == "A" || entry.getKey() == "B" || entry.getKey() == "C"){
//                continue;
//            }
//
//            ((SignificantObject)entry.getValue()).construct();
//        }
//
//    }


    private boolean bisector(Line line, Vector<String> commands) {
//        GeomPoint I = new Incenter(A, B, C);

        if(ConnectionCalculations.isBisector(A, B, C, line)){
//            ((SignificantObject) significatObjects.get("symB")).setVisible(true);
            Line s = GeometricConstructions.bisectorAngle(A, B, C);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            significatObjects.put("symB", line);
            commands.add("bisectorAngle " + line.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
            commands.add("add symB" + " " + id + " " + line.getId());
//            if (significatObjects.get("symC") != null ) {
//
//                significatObjects.put("I", I);
//                commands.add("w03 " + I.getId())
//
//            } else if (){
//                ((SignificantObject) significatObjects.get("I")).setVisible(true);
//            }
//
//
//            }
            return true;
        }


        if(ConnectionCalculations.isBisector(C, A, B, line)){
            Line s = GeometricConstructions.bisectorAngle(C, A, B);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            significatObjects.put("symA", line);
            commands.add("bisectorAngle " + line.getId() + " " + C.getId() + " " + A.getId() + " " + B.getId());
            commands.add("add symA" + " " + id + " " + line.getId());
//            ((SignificantObject) significatObjects.get("symA")).setVisible(true);
//
//
//            if (((SignificantObject) significatObjects.get("symC")).isVisible() ||
//                    ((SignificantObject) significatObjects.get("symB")).isVisible()) {
//                ((SignificantObject) significatObjects.get("I")).setVisible(true);
//            }
            return true;
        }

        if(ConnectionCalculations.isBisector(B, C, A, line)){
            Line s = GeometricConstructions.bisectorAngle(B, C, A);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            significatObjects.put("symC", line);
            commands.add("bisectorAngle " + line.getId() + " " + B.getId() + " " + C.getId() + " " + A.getId());
            commands.add("add symC" + " " + id + " " + line.getId());
//            ((SignificantObject) significatObjects.get("symC")).setVisible(true);
//
//
//            if (((SignificantObject) significatObjects.get("symB")).isVisible() ||
//                    ((SignificantObject) significatObjects.get("symA")).isVisible()) {
//                ((SignificantObject) significatObjects.get("I")).setVisible(true);
//            }
            return true;
        }


        return false;
    }

    private boolean altitude(Line line, Vector<String> commands) {
        if(ConnectionCalculations.altitude(A, B, C, line)){
            Line h = GeometricConstructions.w10(B, b);
            line.setBegin(h.getBegin());
            line.setEnd(h.getEnd());
            commands.add("w10 " + line.getId() + " " + B.getId() + " " + b.getId());
            commands.add("add hb" + " " + id + " " + line.getId());
            significatObjects.put("hb", line);
//            ((SignificantObject) significatObjects.get("Hb")).setVisible(true);
//            ((SignificantObject) significatObjects.get("hb")).setVisible(true);
//
//            if (((SignificantObject) significatObjects.get("ha")).isVisible() ||
//                    ((SignificantObject) significatObjects.get("hc")).isVisible()) {
//                ((SignificantObject) significatObjects.get("H")).setVisible(true);
//            }
            return true;
        }

        if(ConnectionCalculations.altitude(B, C, A, line)){
            Line h = GeometricConstructions.w10(C, c);
            line.setBegin(h.getBegin());
            line.setEnd(h.getEnd());
            commands.add("w10 " + line.getId() + " " + C.getId() + " " + c.getId());
            commands.add("add hc" + " " + id + " " + line.getId());
            significatObjects.put("hc", line);
//            ((SignificantObject) significatObjects.get("Hc")).setVisible(true);
//            ((SignificantObject) significatObjects.get("hc")).setVisible(true);
//
//            if (((SignificantObject) significatObjects.get("ha")).isVisible() ||
//                    ((SignificantObject) significatObjects.get("hb")).isVisible()) {
//                ((SignificantObject) significatObjects.get("H")).setVisible(true);
//            }
            return true;
        }

        if(ConnectionCalculations.altitude(C, A, B, line)){
            Line h = GeometricConstructions.w10(A, a);
            line.setBegin(h.getBegin());
            line.setEnd(h.getEnd());
            commands.add("w10 " + line.getId() + " " + A.getId() + " " + a.getId());
            commands.add("add ha" + " " + id + " " + line.getId());
            significatObjects.put("hc", line);
//            ((SignificantObject) significatObjects.get("Ha")).setVisible(true);
//            ((SignificantObject) significatObjects.get("ha")).setVisible(true);
//
//            if (((SignificantObject) significatObjects.get("hb")).isVisible() ||
//                    ((SignificantObject) significatObjects.get("hc")).isVisible()) {
//                ((SignificantObject) significatObjects.get("H")).setVisible(true);
//            }
            return true;
        }

        return false;
    }

    private boolean centroid(Line line, Vector<String> commands) {

        if(ConnectionCalculations.centroidLine(A, B, C, line)){
            Line t = new Median(A, B, C);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("tb", line);

            commands.add("centroid " + line.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
            commands.add("add tb" + " " + id + " " + line.getId());

//            ((SignificantObject) significatObjects.get("Sb")).setVisible(true);
//            ((SignificantObject) significatObjects.get("tb")).setVisible(true);
//
//            if (((SignificantObject) significatObjects.get("ta")).isVisible() ||
//                    ((SignificantObject) significatObjects.get("tc")).isVisible()) {
//                ((SignificantObject) significatObjects.get("T")).setVisible(true);
//            }
            return  true;
        }

        if(ConnectionCalculations.centroidLine(B, C, A, line)){
            Line t = new Median(B, C, A);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("tc", line);

            commands.add("centroid " + line.getId() + " " + B.getId() + " " + C.getId() + " " + A.getId());
            commands.add("add tc" + " " + id + " " + line.getId());
//            ((SignificantObject) significatObjects.get("Sc")).setVisible(true);
//            ((SignificantObject) significatObjects.get("tc")).setVisible(true);
//
//
//            if (((SignificantObject) significatObjects.get("ta")).isVisible() ||
//                    ((SignificantObject) significatObjects.get("tb")).isVisible()) {
//                ((SignificantObject) significatObjects.get("T")).setVisible(true);
//            }
//            return  true;
        }


        if (ConnectionCalculations.centroidLine(C, A, B, line)) {
            Line t = new Median(C, A, B);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("ta", line);

            commands.add("centroid " + line.getId() + " " + C.getId() + " " + A.getId() + " " + B.getId());
            commands.add("add ta" + " " + id + " " + line.getId());
//            ((SignificantObject) significatObjects.get("Sa")).setVisible(true);
//            ((SignificantObject) significatObjects.get("ta")).setVisible(true);
//
//
//            if (((SignificantObject) significatObjects.get("tb")).isVisible() ||
//                    ((SignificantObject) significatObjects.get("tc")).isVisible()) {
//                ((SignificantObject) significatObjects.get("T")).setVisible(true);
//            }
//            return  true;
        }

        return false;
    }

    private boolean prepBisector(Line line, Vector<String> commands) {
        GeomPoint A = (GeomPoint) significatObjects.get("A");
        GeomPoint B = (GeomPoint) significatObjects.get("B");
        GeomPoint C = (GeomPoint) significatObjects.get("C");

        if(ConnectionCalculations.isSegmentCentar(A, B, line)){
            ((SignificantObject) significatObjects.get("Sc")).setVisible(true);
            ((SignificantObject) significatObjects.get("symAB")).setVisible(true);


            if (((SignificantObject) significatObjects.get("symBC")).isVisible() ||
                    ((SignificantObject) significatObjects.get("symAC")).isVisible()) {
                ((SignificantObject) significatObjects.get("O")).setVisible(true);
            }

            return true;
        }


        if(ConnectionCalculations.isSegmentCentar(C, A, line)){
            ((SignificantObject) significatObjects.get("Sb")).setVisible(true);
            ((SignificantObject) significatObjects.get("symAC")).setVisible(true);


            if (((SignificantObject) significatObjects.get("symBC")).isVisible() ||
                    ((SignificantObject) significatObjects.get("symAB")).isVisible()) {
                ((SignificantObject) significatObjects.get("O")).setVisible(true);
            }
            return true;
        }



        if(ConnectionCalculations.isSegmentCentar( B, C, line)){
            ((SignificantObject) significatObjects.get("Sa")).setVisible(true);
            ((SignificantObject) significatObjects.get("symBC")).setVisible(true);


            if (((SignificantObject) significatObjects.get("symAB")).isVisible() ||
                    ((SignificantObject) significatObjects.get("symAC")).isVisible()) {
                ((SignificantObject) significatObjects.get("O")).setVisible(true);
            }

            return true;
        }

        return false;
    }

//    private void  fillMap(Vector<GeomPoint> points){
//        GeomPoint A = points.elementAt(0);
//        GeomPoint B = points.elementAt(1);
//        GeomPoint C = points.elementAt(2);
//
//        Incenter I = new Incenter(A, B, C);
//        Circumcenter O = new Circumcenter(A, B, C);
//        significatObjects.put("A", A);
//        significatObjects.put("B", B);
//        significatObjects.put("C", C);
//        significatObjects.put("H", new Orthocenter(A, B, C));
//        significatObjects.put("Ha", new FootOfAltitude(C, A, B));
//        significatObjects.put("Hb", new FootOfAltitude(A, B, C));
//        significatObjects.put("Hc", new FootOfAltitude(B, C, A));
//        significatObjects.put("T", new Centroid(A, B, C));
//        significatObjects.put("I", I);
//        significatObjects.put("O", O);
//        significatObjects.put("Sa", new Midpoint(C, A, B));
//        significatObjects.put("Sb", new Midpoint(A, B, C));
//        significatObjects.put("Sc", new Midpoint(B, C, A));
//
//        significatObjects.put("symBC", new PerpBisector(C, A, B));
//        significatObjects.put("symAC", new PerpBisector(A, B, C));
//        significatObjects.put("symAB", new PerpBisector(B, C, A));
//        significatObjects.put("symA", new AngleBisector(C, A, B));
//        significatObjects.put("symB", new AngleBisector(A, B, C));
//        significatObjects.put("symC", new AngleBisector(B, C, A));
//
//        significatObjects.put("ta", new Median(C, A, B));
//        significatObjects.put("tb", new Median(A, B, C));
//        significatObjects.put("tc", new Median(B, C, A));
//
//        significatObjects.put("ha", new Altitude(C, A, B));
//        significatObjects.put("hb", new Altitude(A, B, C));
//        significatObjects.put("hc", new Altitude(B, C, A));
//
//        significatObjects.put("ki", new Incircle(I, A, B, C));
//        significatObjects.put("kc", new CircumscribedCircle(O, A));
//    }
}
