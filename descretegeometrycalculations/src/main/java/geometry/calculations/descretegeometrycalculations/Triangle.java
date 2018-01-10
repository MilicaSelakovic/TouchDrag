package geometry.calculations.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Triangle extends Polygon {
    private String id;
    private HashMap<String, GeometricObject> significatObjects;
    private Vector<GeometricObject> significatObjectsDraw;
    private GeomPoint A, B, C;
    private String idA, idB, idC;

    private String freePoint1;
    private String freePoint2;
    private String freePoint3;

    private Line a, b, c;

    private Vector<String> reconstruction = null;


    public String getNumber() {
        return number;
    }

    private String number = "";
    private boolean changed = false;

    Triangle(Vector<GeomPoint> points) {
        super(points);
        significatObjects = new HashMap<>(30);
        significatObjectsDraw = new Vector<>(50);
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
        A.setFree(true);
        freePoint2 = "B";
        B.setFree(true);
        freePoint3 = "C";
        C.setFree(true);

        populateSignificantObjects();

    }

    public void setIDLines(String aId, String bId, String cId) {
        a.setId(aId);
        a.setDraw(false);
        b.setId(bId);
        b.setDraw(false);
        c.setId(cId);
        c.setDraw(false);
    }

    public void setIDPoints() {
        idA = A.getId();
        idB = B.getId();
        idC = C.getId();
    }

    public void setNumber(String number) {
        this.number = number;
        significatObjects.get("A").setLabel("A");
        significatObjects.get("B").setLabel("B");
        significatObjects.get("C").setLabel("C");

        ((GeomPoint) significatObjects.get("A")).setLabelNum(number);
        ((GeomPoint) significatObjects.get("B")).setLabelNum(number);
        ((GeomPoint) significatObjects.get("C")).setLabelNum(number);


        significatObjects.get("A").setTriangle(this);
        significatObjects.get("B").setTriangle(this);
        significatObjects.get("C").setTriangle(this);


        ((GeomPoint) significatObjects.get("A")).setType(GeomPoint.Type.TRIANGLE_FREE);
        ((GeomPoint) significatObjects.get("B")).setType(GeomPoint.Type.TRIANGLE_FREE);
        ((GeomPoint) significatObjects.get("C")).setType(GeomPoint.Type.TRIANGLE_FREE);
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

    // Menjanje slobodnih tacaka
    private boolean isFree(String point) {
        return point.compareTo(freePoint1) == 0 || point.compareTo(freePoint2) == 0
                || point.compareTo(freePoint3) == 0;
    }

    void fix(GeomPoint point, HashMap<String, Vector<String>> trics) {
        unfreePoint(point);

        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (entry.getValue() instanceof GeomPoint) {
                ((GeomPoint) entry.getValue()).setMove(false);
            }
        }

        recolor(trics);
    }

    void free(GeomPoint point, HashMap<String, Vector<String>> trics) {
        String label = "";
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (entry.getValue() == point) {
                label = entry.getKey();
            }
        }

        point.setFree(true);

        if (freePoint1.compareTo("") == 0) {
            freePoint1 = label;
        } else if (freePoint2.compareTo("") == 0) {
            freePoint2 = label;
        } else if (freePoint3.compareTo("") == 0) {
            freePoint3 = label;
        }

        if (numOfFree() == 3) {
            ((GeomPoint) significatObjects.get(freePoint1)).setMove(true);
            ((GeomPoint) significatObjects.get(freePoint2)).setMove(true);
            ((GeomPoint) significatObjects.get(freePoint3)).setMove(true);

            reconstruction = canBeConstruct(trics, freePoint1, freePoint2, freePoint3);
        }

        recolor(trics);
    }

    public void recolor(HashMap<String, Vector<String>> trics) {

        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (entry.getValue() instanceof GeomPoint && entry.getValue() != null) {
                if (isFree(entry.getKey())) {
                    ((GeomPoint) entry.getValue()).setType(GeomPoint.Type.TRIANGLE_FREE);
                } else {
                    if (numOfFree() < 2) {
                        ((GeomPoint) entry.getValue()).setType(GeomPoint.Type.TRIANGLE_CANFREE);
                        continue;
                    }

                    if (numOfFree() == 2) {
                        if (canBeConstruct(trics, entry.getKey(), freePoint1, freePoint2) != null) {

                            ((GeomPoint) entry.getValue()).setType(GeomPoint.Type.TRIANGLE_CANFREE);

                        } else {
                            ((GeomPoint) entry.getValue()).setType(GeomPoint.Type.TRIANGLE_CANNOTFREE);
                        }

                        continue;
                    }

                    ((GeomPoint) entry.getValue()).setType(GeomPoint.Type.TRIANGLE_CANNOTFREE);
                }
            }

        }
    }

    private int numOfFree() {
        int br = 3;

        if (freePoint1.compareTo("") == 0) {
            br--;
        }

        if (freePoint2.compareTo("") == 0) {
            br--;
        }

        if (freePoint3.compareTo("") == 0) {
            br--;
        }

        return br;
    }

    private void unfreePoint(GeomPoint point) {
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (entry.getValue() == point) {
                if (freePoint1.compareTo(entry.getKey()) == 0) {
                    freePoint1 = "";
                    point.setFree(false);
                    break;
                }

                if (freePoint2.compareTo(entry.getKey()) == 0) {
                    freePoint2 = "";
                    point.setFree(false);
                    break;
                }
                if (freePoint3.compareTo(entry.getKey()) == 0) {
                    freePoint3 = "";
                    point.setFree(false);
                    break;
                }
            }
        }

        rearangeFree();
    }

    private void rearangeFree() {
        if (freePoint1.compareTo("") == 0 && freePoint2.compareTo("") != 0) {
            freePoint1 = freePoint2;
            freePoint2 = "";
        }

        if (freePoint1.compareTo("") == 0 && freePoint3.compareTo("") != 0) {
            freePoint1 = freePoint3;
            freePoint3 = "";
        }

        if (freePoint2.compareTo("") == 0 && freePoint3.compareTo("") != 0) {
            freePoint2 = freePoint3;
            freePoint3 = "";
        }

    }

    // ostaje
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
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose, PointInformations pointInformations) {
        if (A == null || B == null || C == null) {
            changed = true;
            return;
        } else {
            if (changed) {
                Vector<GeomPoint> points = new Vector<>();
                points.clear();
                points.add(A);
                points.add(B);
                points.add(C);
                setPoints(points);
            }
        }
        super.draw(canvas, paint, finished, choose, pointInformations);

        if (pointInformations.isShowSignInfo()) {
            Paint infoPaint = new Paint(paint);
            infoPaint.setColor(pointInformations.getInfoColor());
            infoPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));

            for (GeometricObject object : significatObjectsDraw) {
                if (object != null) {
                    object.setInfoObject(true);
                    object.draw(canvas, infoPaint, finished, choose, pointInformations);
                }
            }
        }

    }

    public boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects) {

        if (object instanceof Line) {
            Line l = (Line) object;
            if (bisector(l, commands) || altitude(l, commands) || median(l, commands) || prepBisector(l, commands)) {
                l.setTriangle(this);
                return true;
            }
        }

        if (object instanceof GeomPoint) {
            GeomPoint p = (GeomPoint) object;
            if (orthocenter(p, commands) || incenter(p, commands) || centroid(p, commands) || midpoint(p, commands)
                    || footOfAltitude(p, commands) || footOfBisectors(p, commands) || circumcenter(p, commands)
                    || eulerPoint(p, commands) || eulerPoint2(p, commands)) {
                p.setMove(false);
                p.setType(GeomPoint.Type.TRIANGLE_CANNOTFREE);
                return true;
            }

        }

        if (object instanceof Circle) {
            Circle circle = (Circle) object;
            if (eulerCircle(circle, commands, uniqueID) || circumscribedCircle(circle, commands, uniqueID) || inCircle(circle, commands, uniqueID)) {
                circle.setConstants(this.constants);
                circle.getCenter().setConstants(this.constants);
                circle.setTriangle(this);
                circle.getCenter().setTriangle(this);
                return true;
            }

        }

        return false;
    }

    public boolean isUnderCursor(float x, float y) {
        return false;
    }

    public void translate(float x, float y, HashMap<String, GeometricObject> objects) {
        if (canNotBeConstructed(freePoint1, freePoint2, freePoint3)) {
            significatObjects.put("C", null);
            if (C != null) {
                objects.put(C.getId(), null);
            }
            C = null;
            return;
        }
        if (reconstruction != null) {
            HashMap<String, GeometricObject> copy = new HashMap<>();

            for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
                copy.put(entry.getKey(), entry.getValue());
            }

            Constructor constructor = new Constructor();
            Vector<Vector<String>> tmp = new Vector<>();
            tmp.add(reconstruction);
            constructor.reconstruct(tmp, copy);

            A = (GeomPoint) copy.get("A");
            B = (GeomPoint) copy.get("B");
            C = (GeomPoint) copy.get("C");

            significatObjects.put("A", A);
            significatObjects.put("B", B);
            significatObjects.put("C", C);

            objects.put(idA, A);
            objects.put(idB, B);
            objects.put(idC, C);

        }

        populateSignificantObjects();
    }

    @Override
    public void scale(float scaleFactor, float w, float h) {
        super.scale(scaleFactor, w, h);
    }

    private boolean bisector(Line line, Vector<String> commands) {

        if (ConnectionCalculations.isBisector(A, B, C, line)) {
            Line s = GeometricConstructions.bisectorAngle(A, B, C);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            if (commands != null) {
                line.setLabel("symB");
                significatObjects.put("symB", line);
                commands.add("bisectorAngle " + line.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
            } else {
                line.recognizedLabel = "Bisector line of angle A" + number() + "B" + number() + "C" + number();
            }
            return true;
        }


        if (ConnectionCalculations.isBisector(C, A, B, line)) {
            Line s = GeometricConstructions.bisectorAngle(C, A, B);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            if (commands != null) {
                line.setLabel("symA");
                significatObjects.put("symA", line);
                commands.add("bisectorAngle " + line.getId() + " " + C.getId() + " " + A.getId() + " " + B.getId());
            } else {
                line.recognizedLabel = "Bisector line of angle C" + number() + "A" + number() + "B" + number();
            }
            return true;
        }

        if (ConnectionCalculations.isBisector(B, C, A, line)) {
            Line s = GeometricConstructions.bisectorAngle(B, C, A);
            line.setBegin(s.getBegin());
            line.setEnd(s.getEnd());
            if (commands != null) {
                line.setLabel("symC");
                significatObjects.put("symC", line);
                commands.add("bisectorAngle " + line.getId() + " " + B.getId() + " " + C.getId() + " " + A.getId());
            } else {
                line.recognizedLabel = "Bisector line of angle B" + number() + "C" + number() + "A" + number();
            }
            return true;
        }


        return false;
    }

    private boolean altitude(Line line, Vector<String> commands) {
        if (ConnectionCalculations.altitude(A, B, C, line)) {
            Line h = GeometricConstructions.w10(B, b);
            line.setBegin(h.getBegin());
            line.setEnd(h.getEnd());
            if (commands != null) {
                commands.add("w10 " + line.getId() + " " + B.getId() + " " + b.getId());
                line.setLabel("hb");
                significatObjects.put("hb", line);
            } else {
                line.recognizedLabel = "Altitude hb" + number();
            }
            return true;
        }

        if (ConnectionCalculations.altitude(B, C, A, line)) {
            Line h = GeometricConstructions.w10(C, c);
            line.setBegin(h.getBegin());
            line.setEnd(h.getEnd());
            if (commands != null) {
                commands.add("w10 " + line.getId() + " " + C.getId() + " " + c.getId());
                line.setLabel("hc");
                significatObjects.put("hc", line);
            } else {
                line.recognizedLabel = "Altitude hc" + number();
            }
            return true;
        }

        if (ConnectionCalculations.altitude(C, A, B, line)) {
            Line h = GeometricConstructions.w10(A, a);
            line.setBegin(h.getBegin());
            line.setEnd(h.getEnd());
            if (commands != null) {
                commands.add("w10 " + line.getId() + " " + A.getId() + " " + a.getId());
                line.setLabel("ha");
                significatObjects.put("ha", line);
            } else {
                line.recognizedLabel = "Altitude ha" + number();
            }
            return true;
        }

        return false;
    }

    private boolean median(Line line, Vector<String> commands) {

        if (ConnectionCalculations.centroidLine(A, B, C, line)) {
            Line t = GeometricConstructions.medianLine(A, B, C);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            if (commands != null) {
                line.setLabel("tb");
                significatObjects.put("tb", line);
                commands.add("medianLine " + line.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
            } else {
                line.recognizedLabel = "Median line tb" + number();
            }

            return true;
        }

        if (ConnectionCalculations.centroidLine(B, C, A, line)) {
            Line t = GeometricConstructions.medianLine(B, C, A);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            if (commands != null) {
                line.setLabel("tc");
                significatObjects.put("tc", line);
                commands.add("medianLine " + line.getId() + " " + B.getId() + " " + C.getId() + " " + A.getId());
            } else {
                line.recognizedLabel = "Median line tc" + number();
            }

            return true;
        }


        if (ConnectionCalculations.centroidLine(C, A, B, line)) {
            Line t = GeometricConstructions.medianLine(C, A, B);
            line.setBegin(t.getBegin());
            line.setEnd(t.getEnd());
            if (commands != null) {
                line.setLabel("ta");
                significatObjects.put("ta", line);
                commands.add("medianLine " + line.getId() + " " + C.getId() + " " + A.getId() + " " + B.getId());
            } else {
                line.recognizedLabel = "Median line ta" + number();
            }
            return true;
        }

        return false;
    }

    private boolean prepBisector(Line line, Vector<String> commands) {
        if (ConnectionCalculations.isSegmentCentar(A, B, line)) {
            Line l = GeometricConstructions.w14(A, B);
            line.setBegin(l.getBegin());
            line.setEnd(l.getEnd());

            if (commands != null) {
                line.setLabel("symAB");
                significatObjects.put("symAB", line);
                commands.add("w14 " + line.getId() + " " + A.getId() + " " + B.getId());
            } else {
                line.recognizedLabel = "Symmetrical line A" + number() + "B" + number();
            }

            return true;
        }


        if (ConnectionCalculations.isSegmentCentar(C, A, line)) {
            Line l = GeometricConstructions.w14(A, C);
            line.setBegin(l.getBegin());
            line.setEnd(l.getEnd());

            if (commands != null) {
                line.setLabel("symAC");
                significatObjects.put("symAC", line);
                commands.add("w14 " + line.getId() + " " + A.getId() + " " + C.getId());
            } else {
                line.recognizedLabel = "Symmetrical line A" + number() + "C" + number();
            }

            return true;
        }


        if (ConnectionCalculations.isSegmentCentar(B, C, line)) {
            Line l = GeometricConstructions.w14(B, C);
            line.setBegin(l.getBegin());
            line.setEnd(l.getEnd());

            if (commands != null) {
                line.setLabel("symBC");
                significatObjects.put("symBC", line);
                commands.add("w14 " + line.getId() + " " + B.getId() + " " + C.getId());
            } else {
                line.recognizedLabel = "Symmetrical line B" + number() + "C" + number();
            }
            return true;
        }

        return false;
    }


    private boolean orthocenter(GeomPoint point, Vector<String> commands) {
        GeomPoint H = GeometricConstructions.orthocenter(A, B, C);

        if (H == null) {
            return false;
        }

        if (H.distance(point) > constants.getDistance_point()) {
            return false;
        }
        point.setX(H.X());
        point.setY(H.Y());
        Line ha = (Line) significatObjects.get("ha");
        Line hb = (Line) significatObjects.get("hb");
        Line hc = (Line) significatObjects.get("hc");

        if (ha != null && hb != null) {
            if (commands != null) {
                significatObjects.put("H", point);
                commands.add("w03 " + point.getId() + " " + ha.getId() + " " + hb.getId());
                setData(point, "H", number, commands);
            }
            return true;
        }

        if (hc != null && hb != null) {
            if (commands != null) {
                significatObjects.put("H", point);
                commands.add("w03 " + point.getId() + " " + hc.getId() + " " + hb.getId());
                setData(point, "H", number, commands);
            }
            return true;
        }
        if (ha != null && hc != null) {
            if (commands != null) {
                significatObjects.put("H", point);
                commands.add("w03 " + point.getId() + " " + ha.getId() + " " + hc.getId());
                setData(point, "H", number, commands);
            }
            return true;
        }
        return false;
    }

    private boolean incenter(GeomPoint point, Vector<String> commands) {
        GeomPoint O = GeometricConstructions.incenter(A, B, C);

        if (O.distance(point) > constants.getDistance_point()) {
            return false;
        }
        point.setX(O.X());
        point.setY(O.Y());
        Line symA = (Line) significatObjects.get("symA");
        Line symB = (Line) significatObjects.get("symB");
        Line symC = (Line) significatObjects.get("symC");

        if (symA != null && symB != null) {
            if (commands != null) {
                significatObjects.put("I", point);
                commands.add("w03 " + point.getId() + " " + symA.getId() + " " + symB.getId());
                setData(point, "I", number, commands);
            }
            return true;
        }

        if (symC != null && symB != null) {
            if (commands != null) {
                significatObjects.put("I", point);
                commands.add("w03 " + point.getId() + " " + symC.getId() + " " + symB.getId());
                setData(point, "I", number, commands);
            }
            return true;
        }
        if (symA != null && symC != null) {
            if (commands != null) {
                significatObjects.put("I", point);
                commands.add("w03 " + point.getId() + " " + symA.getId() + " " + symC.getId());
                setData(point, "I", number, commands);
            }
            return true;
        }
        return false;
    }

    private boolean centroid(GeomPoint point, Vector<String> commands) {
        GeomPoint T = GeometricConstructions.centroid(A, B, C);

        if (T.distance(point) > constants.getDistance_point()) {
            return false;
        }
        Line ta = (Line) significatObjects.get("ta");
        Line tb = (Line) significatObjects.get("tb");
        Line tc = (Line) significatObjects.get("tc");

        if (ta != null && tb != null) {
            point.setX(T.X());
            point.setY(T.Y());


            if (commands != null) {
                significatObjects.put("G", point);
                commands.add("w03 " + point.getId() + " " + ta.getId() + " " + tb.getId());
                setData(point, "G", number, commands);
            }
            return true;
        }

        if (tc != null && tb != null) {
            point.setX(T.X());
            point.setY(T.Y());

            if (commands != null) {
                significatObjects.put("G", point);
                commands.add("w03 " + point.getId() + " " + tc.getId() + " " + tb.getId());
                setData(point, "G", number, commands);
            }
            return true;
        }
        if (ta != null && tc != null) {
            point.setX(T.X());
            point.setY(T.Y());

            if (commands != null) {
                significatObjects.put("G", point);
                commands.add("w03 " + point.getId() + " " + ta.getId() + " " + tc.getId());
                setData(point, "G", number, commands);
            }
            return true;
        }
        return false;
    }


    private boolean circumcenter(GeomPoint point, Vector<String> commands) {
        GeomPoint O = GeometricConstructions.circumcenter(A, B, C);

        if (O == null) {
            return false;
        }

        if (O.distance(point) > constants.getDistance_point()) {
            return false;
        }
        point.setX(O.X());
        point.setY(O.Y());
        Line symAB = (Line) significatObjects.get("symAB");
        Line symBC = (Line) significatObjects.get("symBC");
        Line symAC = (Line) significatObjects.get("symAC");

        if (symAB != null && symBC != null) {
            if (commands != null) {
                significatObjects.put("O", point);
                commands.add("w03 " + point.getId() + " " + symAB.getId() + " " + symBC.getId());

                setData(point, "O", number, commands);
            }
            return true;
        }

        if (symAC != null && symBC != null) {
            if (commands != null) {
                significatObjects.put("O", point);
                commands.add("w03 " + point.getId() + " " + symAC.getId() + " " + symBC.getId());

                setData(point, "O", number, commands);
            }
            return true;
        }
        if (symAB != null && symAC != null) {
            if (commands != null) {
                significatObjects.put("O", point);
                commands.add("w03 " + point.getId() + " " + symAB.getId() + " " + symAC.getId());

                setData(point, "O", number, commands);
            }
            return true;
        }
        return false;
    }

    private boolean midpoint(GeomPoint point, Vector<String> commands) {
        GeomPoint Ma = GeometricConstructions.w01(B, B, C, 0.5f);
        GeomPoint Mb = GeometricConstructions.w01(A, A, C, 0.5f);
        GeomPoint Mc = GeometricConstructions.w01(A, A, B, 0.5f);

        if (point.distance(Ma) < constants.getDistance_point()) {
            point.setX(Ma.X());
            point.setY(Ma.Y());

            if (commands != null) {
                significatObjects.put("Ma", point);
                commands.add("w01 " + point.getId() + " " + B.getId() + " " + B.getId() + " " + C.getId() + " 0.5");

                setData(point, "Ma", number, commands);
            }
            return true;
        }

        if (point.distance(Mb) < constants.getDistance_point()) {
            point.setX(Mb.X());
            point.setY(Mb.Y());

            if (commands != null) {
                significatObjects.put("Mb", point);
                commands.add("w01 " + point.getId() + " " + A.getId() + " " + A.getId() + " " + C.getId() + " 0.5");

                setData(point, "Mb", number, commands);
            }
            return true;
        }


        if (point.distance(Mc) < constants.getDistance_point()) {
            point.setX(Mc.X());
            point.setY(Mc.Y());

            if (commands != null) {
                significatObjects.put("Mc", point);
                commands.add("w01 " + point.getId() + " " + A.getId() + " " + A.getId() + " " + B.getId() + " 0.5");

                setData(point, "Mc", number, commands);
            }
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

            if (Ha != null && point.distance(Ha) < constants.getDistance_point()) {
                point.setX(Ha.X());
                point.setY(Ha.Y());


                if (commands != null) {
                    significatObjects.put("Ha", point);
                    commands.add("w03 " + point.getId() + " " + a.getId() + " " + ha.getId());

                    setData(point, "Ha", number, commands);
                }
                return true;
            }

        }


        if (hb != null) {
            GeomPoint Hb = GeometricConstructions.w03(hb, b);

            if (Hb != null && point.distance(Hb) < constants.getDistance_point()) {
                point.setX(Hb.X());
                point.setY(Hb.Y());


                if (commands != null) {
                    significatObjects.put("Hb", point);
                    commands.add("w03 " + point.getId() + " " + b.getId() + " " + hb.getId());

                    setData(point, "Hb", number, commands);
                }
                return true;
            }

        }

        if (hc != null) {
            GeomPoint Hc = GeometricConstructions.w03(hc, c);

            if (Hc != null && point.distance(Hc) < constants.getDistance_point()) {
                point.setX(Hc.X());
                point.setY(Hc.Y());


                if (commands != null) {
                    significatObjects.put("Hc", point);
                    commands.add("w03 " + point.getId() + " " + c.getId() + " " + hc.getId());

                    setData(point, "Hc", number, commands);
                }
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

            if (Ta != null && point.distance(Ta) < constants.getDistance_point()) {
                point.setX(Ta.X());
                point.setY(Ta.Y());
                if (commands != null) {
                    commands.add("w03 " + point.getId() + " " + a.getId() + " " + symA.getId());
                    significatObjects.put("Ta", point);

                    setData(point, "Ta", number, commands);
                }
                return true;
            }

        }


        if (symB != null) {
            GeomPoint Tb = GeometricConstructions.w03(symB, b);

            if (Tb != null && point.distance(Tb) < constants.getDistance_point()) {
                point.setX(Tb.X());
                point.setY(Tb.Y());
                if (commands != null) {
                    commands.add("w03 " + point.getId() + " " + b.getId() + " " + symB.getId());
                    significatObjects.put("Tb", point);

                    setData(point, "Tb", number, commands);
                }
                return true;
            }

        }

        if (symC != null) {
            GeomPoint Tc = GeometricConstructions.w03(symC, c);

            if (Tc != null && point.distance(Tc) < constants.getDistance_point()) {
                point.setX(Tc.X());
                point.setY(Tc.Y());
                if (commands != null) {
                    commands.add("w03 " + point.getId() + " " + c.getId() + " " + symC.getId());
                    significatObjects.put("Tc", point);

                    setData(point, "Tc", number, commands);
                }
                return true;
            }

        }
        return false;
    }


    private boolean circumscribedCircle(Circle circle, Vector<String> commands, UniqueID id) {
        Circle c = GeometricConstructions.circleAroundTriangle(A, B, C);

        if (circle.equal(c)) {
            circle.setCenter(c.getCenter());
            circle.setRadius(c.getRadius());
            if (commands != null) {
                circle.setLabel("cOUT");
                significatObjects.put("cOUT", circle);
                significatObjects.put("O", circle.getCenter());
                circle.getCenter().setId(id.getID());
                setData(circle.getCenter(), "O", number, commands);
                circle.getCenter().setMove(false);
                circle.getCenter().setType(GeomPoint.Type.TRIANGLE_CANNOTFREE);


                commands.add("circleAroundTriangle " + circle.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
            } else {
                circle.recognizedLabel = "Circumscribed circle of A" + number() + "B" + number() + "C" + number();
            }
            return true;
        }

        return false;
    }

    private boolean inCircle(Circle circle, Vector<String> commands, UniqueID id) {
        Circle c = GeometricConstructions.circleInsideTriangle(A, B, C);

        if (circle.equal(c)) {
            circle.setCenter(c.getCenter());
            circle.setRadius(c.getRadius());
            if (commands != null) {
                circle.setLabel("cIN");
                significatObjects.put("cIN", circle);
                significatObjects.put("I", circle.getCenter());
                circle.getCenter().setId(id.getID());
                setData(circle.getCenter(), "I", number, commands);
                circle.getCenter().setMove(false);
                circle.getCenter().setType(GeomPoint.Type.TRIANGLE_CANNOTFREE);


                commands.add("circleInsideTriangle " + circle.getId() + " " + A.getId() + " " + B.getId() + " " + C.getId());
            } else {
                circle.recognizedLabel = "Incircle of A" + number() + "B" + number() + "C" + number();
            }
            return true;
        }

        return false;
    }

    private boolean eulerCircle(Circle circle, Vector<String> commands, UniqueID id) {
        Vector<GeomPoint> point = new Vector<>();
        // Ha, Ma, Hb, Mb, Hc, Mc

        if (significatObjects.get("Ha") != null) {
            point.add((GeomPoint) significatObjects.get("Ha"));
        }

        if (significatObjects.get("Hb") != null) {
            point.add((GeomPoint) significatObjects.get("Hb"));
        }

        if (significatObjects.get("Hc") != null) {
            point.add((GeomPoint) significatObjects.get("Hc"));
        }

        if (significatObjects.get("Ma") != null) {
            point.add((GeomPoint) significatObjects.get("Ma"));
        }

        if (significatObjects.get("Mb") != null) {
            point.add((GeomPoint) significatObjects.get("Mb"));
        }

        if (significatObjects.get("Mc") != null) {
            point.add((GeomPoint) significatObjects.get("Mc"));
        }

        if (point.size() < 3) {
            return false;
        }

        Circle c = GeometricConstructions.circleAroundTriangle(point.elementAt(0),
                point.elementAt(1), point.elementAt(2));

        if (circle.equal(c)) {
            circle.setCenter(c.getCenter());
            circle.setRadius(c.getRadius());
            if (commands != null) {
                circle.setLabel("eCir");
                significatObjects.put("eCir", circle);
                significatObjects.put("N", circle.getCenter());
                circle.getCenter().setId(id.getID());
                setData(circle.getCenter(), "N", number, commands);
                circle.getCenter().setMove(false);
                circle.getCenter().setType(GeomPoint.Type.TRIANGLE_CANNOTFREE);

                commands.add("circleAroundTriangle " + circle.getId() + " " + point.elementAt(0).getId()
                        + " " + point.elementAt(1).getId() + " " + point.elementAt(2).getId());
            } else {
                circle.recognizedLabel = "Euler circle of A" + number() + "B" + number() + "C" + number();
            }
            return true;

        }

        return false;
    }

    private boolean eulerPoint(GeomPoint point, Vector<String> commands) {

        Line ha = (Line) significatObjects.get("ha");
        Line hb = (Line) significatObjects.get("hb");
        Line hc = (Line) significatObjects.get("hc");

        Circle euler = (Circle) significatObjects.get("eCir");

        if (ha != null && euler != null) {
            GeomPoint Ea = GeometricConstructions.eulerPoint(ha, euler, a);

            if (point.distance(Ea) < constants.getDistance_point()) {
                point.setX(Ea.X());
                point.setY(Ea.Y());
                if (commands != null) {
                    commands.add("eulerPoint " + point.getId() + " " + ha.getId() + " " + euler.getId()
                            + " " + a.getId());
                    significatObjects.put("Ea", point);
                    setData(point, "Ea", number, commands);
                }
                return true;
            }
        }


        if (hb != null && euler != null) {
            GeomPoint Eb = GeometricConstructions.eulerPoint(hb, euler, b);

            if (point.distance(Eb) < constants.getDistance_point()) {
                point.setX(Eb.X());
                point.setY(Eb.Y());
                if (commands != null) {

                    commands.add("eulerPoint " + point.getId() + " " + hb.getId() + " " + euler.getId()
                            + " " + b.getId());
                    significatObjects.put("Eb", point);
                    setData(point, "Eb", number, commands);
                }
                return true;
            }
        }

        if (hc != null && euler != null) {
            GeomPoint Ec = GeometricConstructions.eulerPoint(hc, euler, c);

            if (point.distance(Ec) < constants.getDistance_point()) {
                point.setX(Ec.X());
                point.setY(Ec.Y());
                if (commands != null) {
                    commands.add("eulerPoint " + point.getId() + " " + hc.getId() + " " + euler.getId()
                            + " " + c.getId());
                    significatObjects.put("Ec", point);
                    setData(point, "Ec", number, commands);
                }
                return true;
            }
        }

        return false;
    }

    private boolean eulerPoint2(GeomPoint point, Vector<String> commands) {
        GeomPoint H = (GeomPoint) significatObjects.get("H");

        if (H == null) {
            return false;
        }

        GeomPoint Ea = GeometricConstructions.w01(A, A, H, 0.5f);
        GeomPoint Eb = GeometricConstructions.w01(B, B, H, 0.5f);
        GeomPoint Ec = GeometricConstructions.w01(C, C, H, 0.5f);

        if (point.distance(Ea) < constants.getDistance_point()) {
            point.setX(Ea.X());
            point.setY(Ea.Y());
            if (commands != null) {

                significatObjects.put("Ea", point);

                commands.add("w01 " + point.getId() + " " + A.getId() + " " + A.getId() + " " + H.getId() + " 0.5");
                setData(point, "Ea", number, commands);
            }
            return true;
        }

        if (point.distance(Eb) < constants.getDistance_point()) {
            point.setX(Eb.X());
            point.setY(Eb.Y());
            if (commands != null) {
                significatObjects.put("Eb", point);
                commands.add("w01 " + point.getId() + " " + B.getId() + " " + B.getId() + " " + H.getId() + " 0.5");
                setData(point, "Eb", number, commands);
            }
            return true;
        }


        if (point.distance(Ec) < constants.getDistance_point()) {
            point.setX(Ec.X());
            point.setY(Ec.Y());
            if (commands != null) {
                significatObjects.put("Ec", point);
                commands.add("w01 " + point.getId() + " " + C.getId() + " " + C.getId() + " " + H.getId() + " 0.5");
                setData(point, "Ec", number, commands);
            }
            return true;
        }

        return false;
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

    private boolean canNotBeConstructed(String fP1, String fP2, String fP3) {
        if ((fP1.compareTo("A") == 0 && fP2.compareTo("B") == 0 && fP3.compareTo("I") == 0)) {
            GeomPoint I = (GeomPoint) significatObjects.get("I");

            double len = Math.sqrt(Math.pow((A.X() - B.X()), 2) + Math.pow((A.Y() - B.Y()), 2));
            double distance = c.distance(I);

            return distance > len;

        }

        return false;
    }

    @Override
    public void setConstants(Constants constants) {
        super.setConstants(constants);

        a.setConstants(constants);
        b.setConstants(constants);
        c.setConstants(constants);
    }


    private void populateSignificantObjects() {
        Line line;
        GeomPoint point;
        significatObjectsDraw.clear();

        Line symB = GeometricConstructions.bisectorAngle(A, B, C);
        significatObjectsDraw.add(symB);

        Line symA = GeometricConstructions.bisectorAngle(C, A, B);
        significatObjectsDraw.add(symA);

        Line symC = GeometricConstructions.bisectorAngle(B, C, A);
        significatObjectsDraw.add(symC);

        Line hb = GeometricConstructions.w10(B, b);
        significatObjectsDraw.add(hb);

        Line hc = GeometricConstructions.w10(C, c);
        significatObjectsDraw.add(hc);

        Line ha = GeometricConstructions.w10(A, a);
        significatObjectsDraw.add(ha);

        line = GeometricConstructions.medianLine(A, B, C);
        significatObjectsDraw.add(line);

        line = GeometricConstructions.medianLine(B, C, A);
        significatObjectsDraw.add(line);

        line = GeometricConstructions.medianLine(C, A, B);
        significatObjectsDraw.add(line);

        line = GeometricConstructions.w14(A, B);
        significatObjectsDraw.add(line);

        line = GeometricConstructions.w14(A, C);
        significatObjectsDraw.add(line);

        line = GeometricConstructions.w14(B, C);
        significatObjectsDraw.add(line);

        GeomPoint H = GeometricConstructions.orthocenter(A, B, C);
        if (H != null) {
            H.setLabel("H");
        }
        significatObjectsDraw.add(H);

        point = GeometricConstructions.incenter(A, B, C);
        if (point != null)
            point.setLabel("I");
        significatObjectsDraw.add(point);

        point = GeometricConstructions.centroid(A, B, C);
        if (point != null)
            point.setLabel("G");
        significatObjectsDraw.add(point);

        point = GeometricConstructions.circumcenter(A, B, C);
        if (point != null)
            point.setLabel("O");
        significatObjectsDraw.add(point);

        point = GeometricConstructions.w01(B, B, C, 0.5f);
        if (point != null)
            point.setLabel("Ma");
        significatObjectsDraw.add(point);

        point = GeometricConstructions.w01(A, A, C, 0.5f);
        if (point != null)
            point.setLabel("Mb");
        significatObjectsDraw.add(point);

        point = GeometricConstructions.w01(A, A, B, 0.5f);
        if (point != null) {
            point.setLabel("Mc");
        }
        significatObjectsDraw.add(point);

        GeomPoint Ha = GeometricConstructions.w03(ha, a);
        if (Ha != null) {
            Ha.setLabel("Ha");
        }
        significatObjectsDraw.add(Ha);

        GeomPoint Hb = GeometricConstructions.w03(hb, b);
        if (Hb != null) {
            Hb.setLabel("Hb");
        }
        significatObjectsDraw.add(Hb);

        GeomPoint Hc = GeometricConstructions.w03(hc, c);
        if (Hc != null) {
            Hc.setLabel("Hc");
        }
        significatObjectsDraw.add(Hc);


        point = GeometricConstructions.w03(symA, a);
        if (point != null) {
            point.setLabel("Ta");
        }
        significatObjectsDraw.add(point);

        point = GeometricConstructions.w03(symB, b);
        if (point != null) {
            point.setLabel("Tb");
        }
        significatObjectsDraw.add(point);

        point = GeometricConstructions.w03(symC, c);
        if (point != null) {
            point.setLabel("Tc");
        }
        significatObjectsDraw.add(point);

        Circle circle = GeometricConstructions.circleAroundTriangle(A, B, C);

        significatObjectsDraw.add(circle);

        circle = GeometricConstructions.circleInsideTriangle(A, B, C);
        significatObjectsDraw.add(circle);


        circle = GeometricConstructions.circleAroundTriangle(Ha, Hb, Hc);

        significatObjectsDraw.add(circle);
        if (circle != null) {
            circle.getCenter().setLabel("N");
            significatObjectsDraw.add(circle.getCenter());
        }

        point = GeometricConstructions.w01(A, A, H, 0.5f);
        if (point != null) {
            point.setLabel("Ea");
        }
        significatObjectsDraw.add(point);

        point = GeometricConstructions.w01(B, B, H, 0.5f);
        if (point != null) {
            point.setLabel("Eb");
        }
        significatObjectsDraw.add(point);

        point = GeometricConstructions.w01(C, C, H, 0.5f);
        if (point != null) {
            point.setLabel("Ec");
        }
        significatObjectsDraw.add(point);
    }

    public void addSignificant(String label, GeometricObject object) {
        significatObjects.put(label, object);
    }

    public void removeSignificant(String label) {
        if (isFree(label)) {
            freePoint1 = "A";
            freePoint2 = "B";
            freePoint3 = "C";
            reset();
        }
        significatObjects.remove(label);
    }


    private void reset() {
        for (Map.Entry<String, GeometricObject> entry : significatObjects.entrySet()) {
            if (isFree(entry.getKey())) {
                if (entry.getValue() != null && entry.getValue() instanceof GeomPoint) {
                    ((GeomPoint) entry.getValue()).setMove(true);
                    ((GeomPoint) entry.getValue()).setType(GeomPoint.Type.TRIANGLE_FREE);
                }
            } else {
                if (entry.getValue() != null && entry.getValue() instanceof GeomPoint) {
                    ((GeomPoint) entry.getValue()).setMove(false);
                    ((GeomPoint) entry.getValue()).setType(GeomPoint.Type.TRIANGLE_CANNOTFREE);

                }
            }
        }
    }

    private void setData(GeomPoint object, String label, String labelNum, Vector<String> commands) {
        object.setLabel(label);
        commands.add("addLabel " + object.getId() + " " + label);
        object.setLabelNum(labelNum);
        object.setTriangle(this);
        if (object.getId().compareTo("") == 0) {
            Log.d("Sranjce", "se desilo");
        }
        commands.add("addTriangle " + object.getId() + " " + getId());

    }

    private String number() {
        return Integer.parseInt(number) == 0 ? "" : number;
    }


}
