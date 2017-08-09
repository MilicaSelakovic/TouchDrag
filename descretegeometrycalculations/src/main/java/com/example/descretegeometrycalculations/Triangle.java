package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;


import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

public class Triangle extends Polygon {
    private String id;
    private HashMap<String, GeometricObject> significatObjects;
    private GeomPoint A, B, C;

    private String freePoint1;
    private String freePoint2;
    private String freePoint3;

    private String chosen = "";

    private Line a, b, c;

    private Vector<String> reconstruction = null;

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

    public Line getLineB() {
        return b;
    }

    public Line getLineC() {
        return c;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private boolean isFree(String point) {
        return point.compareTo(freePoint1) == 0 || point.compareTo(freePoint2) == 0
                || point.compareTo(freePoint3) == 0;
    }


    private void recolor(HashMap<String, Vector<String>> trics) {
        ((GeomPoint) significatObjects.get(chosen)).setCanChoose(2);
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (entry.getValue() instanceof GeomPoint && entry.getValue() != null) {
                if (chosen.compareTo(freePoint1) == 0) {
                    if (canBeConstruct(trics, entry.getKey(), freePoint2, freePoint3) != null) {

                        ((GeomPoint) entry.getValue()).setCanChoose(3);

                    }
                }
                if (chosen.compareTo(freePoint2) == 0) {
                    if (canBeConstruct(trics, entry.getKey(), freePoint1, freePoint3) != null) {

                        ((GeomPoint) entry.getValue()).setCanChoose(3);

                    }
                }

                if (chosen.compareTo(freePoint3) == 0) {
                    if (canBeConstruct(trics, entry.getKey(), freePoint1, freePoint2) != null) {

                        ((GeomPoint) entry.getValue()).setCanChoose(3);

                    }
                }
            }

        }
    }

    private Vector<String> canBeConstruct(HashMap<String, Vector<String>> trics, String point, String free1, String free2) {
        Vector<String> construction = trics.get(point + " " + free1 + " " + free2);
        if (construction != null) {
            return construction;
        }

        construction = trics.get(point + " " + free2 + " " + free1);
        if (construction != null) {
            return construction;
        }

        construction = trics.get(free1 + " " + point + " " + free2);
        if (construction != null) {
            return construction;
        }


        construction = trics.get(free1 + " " + free2 + " " + point);
        if (construction != null) {
            return construction;
        }

        construction = trics.get(free2 + " " + free1 + " " + point);
        if (construction != null) {
            return construction;
        }

        construction = trics.get(free2 + " " + point + " " + free1);
        if (construction != null) {
            return construction;
        }


        return null;
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


    public boolean changeFree(float x, float y, HashMap<String, Vector<String>> trics) {
        boolean ind = false;
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {

            if (entry.getValue() instanceof GeomPoint && entry.getValue() != null && ((GeomPoint) entry.getValue()).underCursor(x, y)) {
                if (chosen.compareTo(freePoint1) == 0) {
                    if (canBeConstruct(trics, entry.getKey(), freePoint2, freePoint3) != null) {
                        reconstruction = canBeConstruct(trics, entry.getKey(), freePoint2, freePoint3);
                        freePoint1 = entry.getKey();
                        ind = true;

                    }
                }
                if (chosen.compareTo(freePoint2) == 0) {
                    if (canBeConstruct(trics, entry.getKey(), freePoint1, freePoint3) != null) {
                        reconstruction = canBeConstruct(trics, entry.getKey(), freePoint1, freePoint3);
                        freePoint2 = entry.getKey();
                        ind = true;
                    }
                }

                if (chosen.compareTo(freePoint3) == 0) {
                    if (canBeConstruct(trics, entry.getKey(), freePoint1, freePoint2) != null) {
                        reconstruction = canBeConstruct(trics, entry.getKey(), freePoint1, freePoint2);
                        freePoint3 = entry.getKey();
                        ind = true;
                    }
                }

                if (ind) {
                    ((GeomPoint) significatObjects.get(chosen)).setMove(false);
                    ((GeomPoint) significatObjects.get(chosen)).setCanChoose(-1);
                    ((GeomPoint) significatObjects.get(entry.getKey())).setMove(true);
                    ((GeomPoint) significatObjects.get(entry.getKey())).setCanChoose(1);
                    return true;
                }
            }

        }
        return false;
    }
    @Override
    public boolean choose(float x, float y, HashMap<String, Vector<String>> trics) {
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (entry.getValue() instanceof GeomPoint && entry.getValue() != null) {
                if (isFree(entry.getKey())) {
                    if (((GeomPoint) entry.getValue()).underCursor(x, y)) {
                        chosen = entry.getKey();
                        recolor(trics);
                        return true;
                    }
                }
            }

        }

        return false;
    }

    public boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects) {

        if(object instanceof Line){
            Line l = (Line) object;
            if (bisector(l, commands)) {
                //Log.d("Simetrala", "ugla");
                return true;
            }

            if (median(l, commands)) {
                //Log.d("Tezisna", "linija");
                return true;
            }

            if (altitude(l, commands)) {
                //Log.d("Visina", "trougla");
                return true;
            }

            if (prepBisector(l, commands)) {
                //Log.d("Srediste", "stranice");
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
            if (midpoint(p, commands)) {
                p.setMove(false);
                return true;
            }

            if (footOfAltitude(p, commands)) {
                p.setMove(false);
                return true;
            }
        }

        return false;
    }

    public boolean belong(GeomPoint point) {
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (point == entry.getValue())
                return true;
        }

        return false;
    }

    public boolean isUnderCursor(float x, float y) {
        return false;
    }

    public void translate(float x, float y){
        if (reconstruction != null) {
            HashMap<String, GeometricObject> copy = new HashMap<>();

            for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
                copy.put(entry.getKey(), entry.getValue());
            }

            Constructor constructor = new Constructor();
            Vector<Vector<String>> tmp = new Vector<>();
            tmp.add(reconstruction);
            constructor.reconstruct(tmp, copy);
        }
    }

    @Override
    public void scale(float scaleFactor) {

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
            Line t = GeometricConstructions.medianLine(A, B, C);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("tb", line);

            commands.add("medianLine " + line.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
//            commands.add("add tb" + " " + id + " " + line.getId());

            return  true;
        }

        if(ConnectionCalculations.centroidLine(B, C, A, line)){
            Line t = GeometricConstructions.medianLine(B, C, A);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("tc", line);

            commands.add("medianLine " + line.getId() + " " + B.getId() + " " + C.getId() + " " + A.getId());
//            commands.add("add tc" + " " + id + " " + line.getId());
            return true;
        }


        if (ConnectionCalculations.centroidLine(C, A, B, line)) {
            Line t = GeometricConstructions.medianLine(C, A, B);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            significatObjects.put("ta", line);

            commands.add("medianLine " + line.getId() + " " + C.getId() + " " + A.getId() + " " + B.getId());
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

            return true;
        }


        if(ConnectionCalculations.isSegmentCentar(C, A, line)){
            Line l = GeometricConstructions.w14(A, C);
            line.setBegin(l.getBegin());
            line.setEnd(l.getEnd());

            significatObjects.put("symAC", line);
            commands.add("w14 " + line.getId() + " " + A.getId() + " " + C.getId());

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
        GeomPoint H = GeometricConstructions.orthocenter(A, B, C);

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
        GeomPoint O = GeometricConstructions.incenter(A, B, C);

        if (O.distance(point) > 20) {
            return false;
        }
        point.setX(O.X());
        point.setY(O.Y());
        Line symA = (Line) significatObjects.get("symA");
        Line symB = (Line) significatObjects.get("symB");
        Line symC = (Line) significatObjects.get("symC");

        if (symA != null && symB != null) {
            significatObjects.put("I", point);
            commands.add("w03 " + point.getId() + " " + symA.getId() + " " + symB.getId());
            return true;
        }

        if (symC != null && symB != null) {
            significatObjects.put("I", point);
            commands.add("w03 " + point.getId() + " " + symC.getId() + " " + symB.getId());
            return true;
        }
        if (symA != null && symC != null) {
            significatObjects.put("I", point);
            commands.add("w03 " + point.getId() + " " + symA.getId() + " " + symC.getId());
            return true;
        }
        return false;
    }

    private boolean centroid(GeomPoint point, Vector<String> commands) {
        GeomPoint T = GeometricConstructions.centroid(A, B, C);

        if (T.distance(point) > 20) {
            return false;
        }
        Line ta = (Line) significatObjects.get("ta");
        Line tb = (Line) significatObjects.get("tb");
        Line tc = (Line) significatObjects.get("tc");

        if (ta != null && tb != null) {
            point.setX(T.X());
            point.setY(T.Y());


            significatObjects.put("G", point);
            commands.add("w03 " + point.getId() + " " + ta.getId() + " " + tb.getId());
            return true;
        }

        if (tc != null && tb != null) {
            point.setX(T.X());
            point.setY(T.Y());

            significatObjects.put("G", point);
            commands.add("w03 " + point.getId() + " " + tc.getId() + " " + tb.getId());
            return true;
        }
        if (ta != null && tc != null) {
            point.setX(T.X());
            point.setY(T.Y());

            significatObjects.put("G", point);
            commands.add("w03 " + point.getId() + " " + ta.getId() + " " + tc.getId());
            return true;
        }
        return false;
    }


    private boolean circumcenter(GeomPoint point, Vector<String> commands) {
        GeomPoint O = GeometricConstructions.circumcenter(A, B, C);

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

    private boolean midpoint(GeomPoint point, Vector<String> commands) {
        GeomPoint Ma = GeometricConstructions.w01(B, B, C, 0.5f);
        GeomPoint Mb = GeometricConstructions.w01(A, A, C, 0.5f);
        GeomPoint Mc = GeometricConstructions.w01(A, A, B, 0.5f);

        if (point.distance(Ma) < 20) {
            point.setX(Ma.X());
            point.setY(Ma.Y());

            significatObjects.put("Ma", point);

            commands.add("w01 " + point.getId() + " " + B.getId() + " " + B.getId() + " " + C.getId() + " 0.5");

            return true;
        }

        if (point.distance(Mb) < 20) {
            point.setX(Mb.X());
            point.setY(Mb.Y());

            significatObjects.put("Mb", point);
            commands.add("w01 " + point.getId() + " " + A.getId() + " " + A.getId() + " " + C.getId() + " 0.5");

            return true;
        }


        if (point.distance(Mc) < 20) {
            point.setX(Mc.X());
            point.setY(Mc.Y());

            significatObjects.put("Mc", point);
            commands.add("w01 " + point.getId() + " " + A.getId() + " " + A.getId() + " " + B.getId() + " 0.5");

            return true;
        }

        return false;
    }


    private boolean footOfAltitude(GeomPoint point, Vector<String> commands) {

        Line ha = (Line) significatObjects.get("ha");
        Line hb = (Line) significatObjects.get("hb");
        Line hc = (Line) significatObjects.get("hc");

        if (ha != null) {
            GeomPoint Ha = GeometricConstructions.w03(ha, a);

            if (point.distance(Ha) < 20) {
                point.setX(Ha.X());
                point.setY(Ha.Y());

                commands.add("w03 " + point.getId() + " " + a.getId() + " " + ha.getId());
                significatObjects.put("Ha", point);

                return true;
            }

        }


        if (hb != null) {
            GeomPoint Hb = GeometricConstructions.w03(hb, b);

            if (point.distance(Hb) < 20) {
                point.setX(Hb.X());
                point.setY(Hb.Y());

                commands.add("w03 " + point.getId() + " " + b.getId() + " " + hb.getId());
                significatObjects.put("Hb", point);

                return true;
            }

        }

        if (hc != null) {
            GeomPoint Hc = GeometricConstructions.w03(hc, c);

            if (point.distance(Hc) < 20) {
                point.setX(Hc.X());
                point.setY(Hc.Y());

                commands.add("w03 " + point.getId() + " " + c.getId() + " " + hc.getId());
                significatObjects.put("Hc", point);

                return true;
            }

        }
        return false;
    }


    private boolean footOfBisectors(GeomPoint point, Vector<String> commands) {

        Line symA = (Line) significatObjects.get("symA");
        Line symB = (Line) significatObjects.get("symB");
        Line symC = (Line) significatObjects.get("symC");

        if (symA != null) {
            GeomPoint Ta = GeometricConstructions.w03(symA, a);

            if (point.distance(Ta) < 20) {
                point.setX(Ta.X());
                point.setY(Ta.Y());

                commands.add("w03 " + point.getId() + " " + a.getId() + " " + symA.getId());
                significatObjects.put("Ta", point);

                return true;
            }

        }


        if (symB != null) {
            GeomPoint Tb = GeometricConstructions.w03(symB, b);

            if (point.distance(Tb) < 20) {
                point.setX(Tb.X());
                point.setY(Tb.Y());

                commands.add("w03 " + point.getId() + " " + b.getId() + " " + symB.getId());
                significatObjects.put("Tb", point);

                return true;
            }

        }

        if (symC != null) {
            GeomPoint Tc = GeometricConstructions.w03(symC, c);

            if (point.distance(Tc) < 20) {
                point.setX(Tc.X());
                point.setY(Tc.Y());

                commands.add("w03 " + point.getId() + " " + c.getId() + " " + symC.getId());
                significatObjects.put("Tc", point);

                return true;
            }

        }
        return false;
    }


    public void addCommand(String komanda) {
        komanda += " " + getId() + " " + freePoint1 + " " + freePoint2 + " " + freePoint3;
    }

    public void recontruct(HashMap<String, Vector<String>> trics, String fP1, String fP2, String fP3) {
        Vector<String> rec = canBeConstruct(trics, fP1, fP2, fP3);

        if (rec != null) {
            HashMap<String, GeometricObject> copy = new HashMap<>();

            for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
                copy.put(entry.getKey(), entry.getValue());
            }

            Constructor constructor = new Constructor();
            Vector<Vector<String>> tmp = new Vector<>();
            tmp.add(rec);
            constructor.reconstruct(tmp, copy);


        }
    }

    public void recolor() {
        ((GeomPoint) significatObjects.get(freePoint1)).setCanChoose(1);
        ((GeomPoint) significatObjects.get(freePoint2)).setCanChoose(1);
        ((GeomPoint) significatObjects.get(freePoint3)).setCanChoose(1);
    }
}
