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

    private String freePoint1;
    private String freePoint2;
    private String freePoint3;

    private Line a, b, c;

    public Triangle(Vector<GeomPoint> points) {
        super(points);
        significatObjects = new HashMap<>(30);
        A = points.elementAt(0);
        B = points.elementAt(1);
        C = points.elementAt(2);

        significatObjects.put("A", A);
        significatObjects.put("B", B);
        significatObjects.put("C", C);
        a = new Line(B, C);
        b = new Line(A, C);
        c = new Line(A, B);

        freePoint1 = "A";
        freePoint2 = "B";
        freePoint3 = "C";

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

    private boolean isFree(String point) {
        return point == freePoint1 || point == freePoint2 || point == freePoint3;
    }
    @Override
    public void setChoose() {
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (entry.getValue() instanceof GeomPoint && entry.getValue() != null) {
                if (isFree(entry.getKey())) {
                    ((GeomPoint) entry.getValue()).setCanChoose(1);
                } else {
                    ((GeomPoint) entry.getValue()).setCanChoose(-1);
                }
            }

        }
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose) {
        super.draw(canvas, paint, finished, choose);

    }


    @Override
    public boolean connection(GeometricObject object) {
        return false;
    }

    public boolean connection(GeometricObject object, Vector<String> commands) {
        boolean ind = false;
        if(object instanceof Line){
            Line l = (Line) object;
            if (bisector(l, commands)) {
                Log.d("Simetrala", "ugla");
                return true;
            }

            if (median(l, commands)) {
                Log.d("Tezisna", "linija");
                return true;
            }

            if (altitude(l, commands)) {
                Log.d("Visina", "trougla");
                return true;
            }

            if (prepBisector(l, commands)) {
                Log.d("Srediste", "stranice");
                return true;
            }

        }

        if (object instanceof GeomPoint) {
            GeomPoint p = (GeomPoint) object;
            if (orthocenter(p, commands)) {
                p.setMove(false);
                return true;
            }

            if (circumcenter(p, commands)) {
                p.setMove(false);
                return true;
            }

            if (incenter(p, commands)) {
                p.setMove(false);
                return true;
            }

            if (centroid(p, commands)) {
                p.setMove(false);
                return true;
            }
        }

        return ind;
    }

    public boolean isUnderCursor(float x, float y) {

        return false;
    }

    public void translate(float x, float y){
    }


    private boolean bisector(Line line, Vector<String> commands) {

        if(ConnectionCalculations.isBisector(A, B, C, line)){
            Line s = GeometricConstructions.bisectorAngle(A, B, C);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            significatObjects.put("symB", line);
            commands.add("bisectorAngle " + line.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
//            commands.add("add symB" + " " + id + " " + line.getId());
            return true;
        }


        if(ConnectionCalculations.isBisector(C, A, B, line)){
            Line s = GeometricConstructions.bisectorAngle(C, A, B);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            significatObjects.put("symA", line);
            commands.add("bisectorAngle " + line.getId() + " " + C.getId() + " " + A.getId() + " " + B.getId());
//            commands.add("add symA" + " " + id + " " + line.getId());
            return true;
        }

        if(ConnectionCalculations.isBisector(B, C, A, line)){
            Line s = GeometricConstructions.bisectorAngle(B, C, A);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            significatObjects.put("symC", line);
            commands.add("bisectorAngle " + line.getId() + " " + B.getId() + " " + C.getId() + " " + A.getId());
//            commands.add("add symC" + " " + id + " " + line.getId());
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
//            commands.add("add hb" + " " + id + " " + line.getId());
            significatObjects.put("hb", line);
            return true;
        }

        if(ConnectionCalculations.altitude(B, C, A, line)){
            Line h = GeometricConstructions.w10(C, c);
            line.setBegin(h.getBegin());
            line.setEnd(h.getEnd());
            commands.add("w10 " + line.getId() + " " + C.getId() + " " + c.getId());
//            commands.add("add hc" + " " + id + " " + line.getId());
            significatObjects.put("hc", line);
            return true;
        }

        if(ConnectionCalculations.altitude(C, A, B, line)){
            Line h = GeometricConstructions.w10(A, a);
            line.setBegin(h.getBegin());
            line.setEnd(h.getEnd());
            commands.add("w10 " + line.getId() + " " + A.getId() + " " + a.getId());
//            commands.add("add ha" + " " + id + " " + line.getId());
            significatObjects.put("ha", line);
            return true;
        }

        return false;
    }

    private boolean median(Line line, Vector<String> commands) {

        if(ConnectionCalculations.centroidLine(A, B, C, line)){
            Line t = new Median(A, B, C);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("tb", line);

            commands.add("centroid " + line.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
//            commands.add("add tb" + " " + id + " " + line.getId());

            return  true;
        }

        if(ConnectionCalculations.centroidLine(B, C, A, line)){
            Line t = new Median(B, C, A);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("tc", line);

            commands.add("centroid " + line.getId() + " " + B.getId() + " " + C.getId() + " " + A.getId());
//            commands.add("add tc" + " " + id + " " + line.getId());
            return true;
        }


        if (ConnectionCalculations.centroidLine(C, A, B, line)) {
            Line t = new Median(C, A, B);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("ta", line);

            commands.add("centroid " + line.getId() + " " + C.getId() + " " + A.getId() + " " + B.getId());
//            commands.add("add ta" + " " + id + " " + line.getId());
            return true;
        }

        return false;
    }

    private boolean prepBisector(Line line, Vector<String> commands) {
        if(ConnectionCalculations.isSegmentCentar(A, B, line)){
            Line l = GeometricConstructions.w14(A, B);
            line.setBegin(l.getBegin());
            line.setEnd(l.getEnd());

            significatObjects.put("symAB", line);
            commands.add("w14 " + line.getId() + " " + A.getId() + " " + B.getId());
//            commands.add("add symAB " + id + " " + line.getId());

            return true;
        }


        if(ConnectionCalculations.isSegmentCentar(C, A, line)){
            Line l = GeometricConstructions.w14(A, C);
            line.setBegin(l.getBegin());
            line.setEnd(l.getEnd());

            significatObjects.put("symAC", line);
            commands.add("w14 " + line.getId() + " " + A.getId() + " " + C.getId());
//            commands.add("add symAC " + id + " " + line.getId());

            return true;
        }



        if(ConnectionCalculations.isSegmentCentar( B, C, line)){
            Line l = GeometricConstructions.w14(B, C);
            line.setBegin(l.getBegin());
            line.setEnd(l.getEnd());

            significatObjects.put("symBC", line);
            commands.add("w14 " + line.getId() + " " + B.getId() + " " + C.getId());
//            commands.add("add symBC " + id + " " + line.getId());

            return true;
        }

        return false;
    }


    private boolean orthocenter(GeomPoint point, Vector<String> commands) {
        GeomPoint H = new Orthocenter(A, B, C);

        if (H.distance(point) > 20) {
            return false;
        }
        point.setX(H.X());
        point.setY(H.Y());
        Line ha = (Line) significatObjects.get("ha");
        Line hb = (Line) significatObjects.get("hb");
        Line hc = (Line) significatObjects.get("hc");

        if (ha != null && hb != null) {
            significatObjects.put("H", point);
            commands.add("w03 " + point.getId() + " " + ha.getId() + " " + hb.getId());
            return true;
        }

        if (hc != null && hb != null) {
            significatObjects.put("H", point);
            commands.add("w03 " + point.getId() + " " + hc.getId() + " " + hb.getId());
            return true;
        }
        if (ha != null && hc != null) {
            significatObjects.put("H", point);
            commands.add("w03 " + point.getId() + " " + ha.getId() + " " + hc.getId());
            return true;
        }
        return false;
    }

    private boolean incenter(GeomPoint point, Vector<String> commands) {
        GeomPoint O = new Incenter(A, B, C);

        if (O.distance(point) > 20) {
            return false;
        }
        point.setX(O.X());
        point.setY(O.Y());
        Line symA = (Line) significatObjects.get("symA");
        Line symB = (Line) significatObjects.get("symB");
        Line symC = (Line) significatObjects.get("symC");

        if (symA != null && symB != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + symA.getId() + " " + symB.getId());
            return true;
        }

        if (symC != null && symB != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + symC.getId() + " " + symB.getId());
            return true;
        }
        if (symA != null && symC != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + symA.getId() + " " + symC.getId());
            return true;
        }
        return false;
    }

    private boolean centroid(GeomPoint point, Vector<String> commands) {
        GeomPoint T = new Incenter(A, B, C);

        if (T.distance(point) > 20) {
            return false;
        }
        point.setX(T.X());
        point.setY(T.Y());
        Line ta = (Line) significatObjects.get("ta");
        Line tb = (Line) significatObjects.get("tb");
        Line tc = (Line) significatObjects.get("tc");

        if (ta != null && tb != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + ta.getId() + " " + tb.getId());
            return true;
        }

        if (tc != null && tb != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + tc.getId() + " " + tb.getId());
            return true;
        }
        if (ta != null && tc != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + ta.getId() + " " + tc.getId());
            return true;
        }
        return false;
    }


    private boolean circumcenter(GeomPoint point, Vector<String> commands) {
        GeomPoint O = new Circumcenter(A, B, C);

        if (O.distance(point) > 20) {
            return false;
        }
        point.setX(O.X());
        point.setY(O.Y());
        Line symAB = (Line) significatObjects.get("symAB");
        Line symBC = (Line) significatObjects.get("symBC");
        Line symAC = (Line) significatObjects.get("symAC");

        if (symAB != null && symBC != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + symAB.getId() + " " + symBC.getId());
            return true;
        }

        if (symAC != null && symBC != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + symAC.getId() + " " + symBC.getId());
            return true;
        }
        if (symAB != null && symAC != null) {
            significatObjects.put("O", point);
            commands.add("w03 " + point.getId() + " " + symAB.getId() + " " + symAC.getId());
            return true;
        }
        return false;
    }


}
