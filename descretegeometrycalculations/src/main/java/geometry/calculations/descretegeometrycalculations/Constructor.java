package geometry.calculations.descretegeometrycalculations;


import android.util.Log;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

public class Constructor {

    public Constructor() {

    }


    public void reconstruct(Vector<Vector<String>> mCommands, HashMap<String, GeometricObject> objects) {
        for (Vector<String> commands : mCommands) {
            for (String command : commands) {
                executeCommand(command, objects, objects);
            }
        }
    }

    public void reconstructNew(Vector<Vector<String>> mCommands, HashMap<String, GeometricObject> newobjects,
                               HashMap<String, GeometricObject> objects) {
        for (Vector<String> commands : mCommands) {
            for (String command : commands) {
                executeCommand(command, newobjects, objects);
            }
        }
    }

    private void executeCommand(String command, HashMap<String, GeometricObject> newobjects, HashMap<String, GeometricObject> objects) {
        try {
            String[] array = command.split("\\s+");
            GeomPoint X, Y, P, Q;
            Line l, p, x, y;
            Circle k, c;
            int r;
            prepisi(newobjects, objects, array);
            switch (array[0].trim()) {
                case "w01":
                    float d = array.length > 6 ? Float.parseFloat(array[5]) / Float.parseFloat(array[6])
                            : Float.parseFloat(array[5]);

                    Y = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (Y != null && !Y.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.w01((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]), d);

                    if (Y == null) {
                        Y = new GeomPoint(0, 0);
                        Y.setConstants(newobjects.get(array[2]).constants);
                        Y.setId(array[1]);
                        Y.setMove(Y.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }
                    Y.setX(X.X());
                    Y.setY(X.Y());
                    newobjects.put(array[1], Y);
                    break;
                case "w02":

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = GeometricConstructions.w02((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }
                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w03":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.w03((Line) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setMove(P.getType() == GeomPoint.Type.TRIANGLE_FREE);
                        P.setConstants(newobjects.get(array[2]).constants);
                    }

                    if (X == null) {
                        newobjects.put(P.getId(), null);
                        break;
                    }

                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w04":
                    P = (GeomPoint) newobjects.get(array[1]);
                    Q = (GeomPoint) newobjects.get(array[2]);

                    if (newobjects.get(array[3]) == null || newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        if (Q != null && !Q.isFree()) {
                            newobjects.put(array[2], null);
                        }

                        break;
                    }


                    X = new GeomPoint(0, 0);
                    Y = new GeomPoint(0, 0);
                    r = GeometricConstructions.w04(((Line) newobjects.get(array[4])), ((Circle) newobjects.get(array[3])),
                            X, Y);

                    if (r == 0) {
                        newobjects.put(array[1], null);
                        newobjects.put(array[2], null);
                        break;
                    }

                    boolean heuristic = circleIntersectionHeuristic(P, Q, X, Y, r);

                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[3]).constants);
                        P.setMove(P.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }

                    if (Q == null) {
                        Q = new GeomPoint(0, 0);
                        Q.setId(array[2]);
                        Q.setConstants(newobjects.get(array[3]).constants);
                        Q.setMove(Q.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }
                    if (!heuristic) {
                        P.setId(array[2]);
                        Q.setId(array[1]);
                    }

                    P.setX(X.X());
                    P.setY(X.Y());
                    if (!P.getId().equals("R")) {
                        newobjects.put(P.getId(), P);
                    }
                    Q.setX(Y.X());
                    Q.setY(Y.Y());
                    if (r == 1) {
                        Q.setX(X.X());
                        Q.setY(X.Y());
                    }
                    if (Q.getId().equals("R")) {
                        break;
                    }
                    newobjects.put(Q.getId(), Q);
                    break;
                case "w05":

                    String point = array[4];
                    if (array.length > 5) {
                        newobjects.put(array[5], newobjects.get(array[5]));
                        point = array[5];
                    }
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(point) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.w05(((Line) newobjects.get(array[3])), ((Circle) newobjects.get(array[2])),
                            (GeomPoint) (objects.get(point)));
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[2]).constants);
                        P.setMove(P.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w06":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.w06((GeomPoint) newobjects.get(array[3]), (GeomPoint) newobjects.get(array[2]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(c.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                    }

                    newobjects.put(array[1], k);
                    break;
                case "w07":
                    P = (GeomPoint) newobjects.get(array[1]);
                    Q = (GeomPoint) newobjects.get(array[2]);

                    if (newobjects.get(array[3]) == null || newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        if (Q != null && !Q.isFree()) {
                            newobjects.put(array[2], null);
                        }

                        break;
                    }

                    X = new GeomPoint(0, 0);
                    Y = new GeomPoint(0, 0);
                    r = GeometricConstructions.w07(((Circle) newobjects.get(array[3])), ((Circle) newobjects.get(array[4])),
                            X, Y);

                    if (r == 0) {
                        newobjects.put(array[1], null);
                        newobjects.put(array[2], null);
                        break;
                    }


                    boolean heuristicCircle = circleIntersectionHeuristic(P, Q, X, Y, r);

                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[3]).constants);
                        P.setMove(P.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }

                    if (Q == null) {
                        Q = new GeomPoint(0, 0);
                        Q.setId(array[2]);
                        Q.setConstants(newobjects.get(array[3]).constants);
                        Q.setMove(Q.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }

                    if (!heuristicCircle) {
                        P.setId(array[2]);
                        Q.setId(array[1]);
                    }

                    P.setX(X.X());
                    P.setY(X.Y());
                    if (!P.getId().equals("R")) {
                        newobjects.put(P.getId(), P);
                    }

                    Q.setX(Y.X());
                    Q.setY(Y.Y());
                    if (r == 1) {
                        Q.setX(X.X());
                        Q.setY(X.Y());
                    }
                    if (Q.getId().equals("R")) {
                        break;
                    }

                    newobjects.put(Q.getId(), Q);
                    break;
                case "w08":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }
                    X = GeometricConstructions.w08(((Circle) newobjects.get(array[2])), ((Circle) newobjects.get(array[3])),
                            (GeomPoint) (objects.get(array[4])));
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[2]).constants);
                        P.setMove(P.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);


                    break;
                case "w09":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    c = GeometricConstructions.w09((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(c.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(k.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }

                    newobjects.put(k.getId(), k);
                    break;
                case "w10":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w10((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    if (!(newobjects.get(array[1]) instanceof Line)) {
                        Log.d("Sranje", "se desava");
                    }
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(array[1], p);
                    break;
                case "w11":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.w11((Line) newobjects.get(array[3]), (GeomPoint) newobjects.get(array[2]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(c.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(k.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }

                    newobjects.put(k.getId(), k);
                    break;
                case "w12":
                    if (newobjects.get(array[3]) == null || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        newobjects.put(array[2], null);
                        break;
                    }

                    x = new Line(null, null);
                    y = new Line(null, null);
                    r = GeometricConstructions.w12(((Circle) newobjects.get(array[3])), ((GeomPoint) newobjects.get(array[4])),
                            x, y);

                    if (r == 0) {
                        newobjects.put(array[1], null);
                        newobjects.put(array[2], null);
                        break;
                    }


                    p = (Line) newobjects.get(array[1]);
                    l = (Line) newobjects.get(array[2]);

                    heuristic = circleTangentHeuristic(p, l, x, y, r);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[3]).constants);
                        p.setId(array[1]);
                    }

                    if (l == null) {
                        l = new Line(null, null);
                        l.setConstants(newobjects.get(array[3]).constants);
                        l.setId(array[2]);
                    }

                    if (!heuristic) {
                        p.setId(array[2]);
                        l.setId(array[1]);
                    }

                    p.setBegin(x.getBegin());
                    p.setEnd(x.getEnd());
                    newobjects.put(p.getId(), p);

                    l.setBegin(y.getBegin());
                    l.setEnd(y.getEnd());

                    if (r == 1) {
                        l.setBegin(x.getBegin());
                        l.setEnd(x.getEnd());
                    }
                    newobjects.put(l.getId(), l);
                    break;
                case "w13":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[5]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    x = GeometricConstructions.w13(((Circle) newobjects.get(array[2])), ((GeomPoint) newobjects.get(array[3])),
                            (Line) (objects.get(array[5])));
                    l = (Line) newobjects.get(array[1]);
                    if (l == null) {
                        l = new Line(null, null);
                        l.setConstants(newobjects.get(array[2]).constants);
                        l.setId(array[1]);
                    }

                    l.setBegin(x.getBegin());
                    l.setEnd(x.getEnd());
                    newobjects.put(l.getId(), l);

                    break;
                case "w14":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w14((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w15":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w15((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]),
                            Float.parseFloat(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w16":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w16((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w17":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[5]) == null || newobjects.get(array[6]) == null
                            || newobjects.get(array[7]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w17((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]), (GeomPoint) newobjects.get(array[7]),
                            Integer.parseInt(array[10]), Integer.parseInt(array[11]), Integer.parseInt(array[12]),
                            Integer.parseInt(array[13]));

                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w18":
                    if (newobjects.get(array[5]) == null || newobjects.get(array[6]) == null
                            || newobjects.get(array[7]) == null || newobjects.get(array[8]) == null
                            || newobjects.get(array[9]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = GeometricConstructions.w18((GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]),
                            (GeomPoint) newobjects.get(array[7]), (GeomPoint) newobjects.get(array[8]), (GeomPoint) newobjects.get(array[9]),
                            Integer.parseInt(array[10]), Integer.parseInt(array[11]), Integer.parseInt(array[12]),
                            Integer.parseInt(array[13]));

                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w19":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.w19(((GeomPoint) newobjects.get(array[2])), ((GeomPoint) newobjects.get(array[3])),
                            (GeomPoint) (objects.get(array[4])));
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[2]).constants);
                        P.setMove(P.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);
                    break;
                case "w20":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null || newobjects.get(array[5]) == null
                            || newobjects.get(array[6]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.w20((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]), (GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]),
                            Integer.parseInt(array[7]), Integer.parseInt(array[8]), Integer.parseInt(array[9]),
                            Integer.parseInt(array[10]));

                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(c.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(k.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }


                    newobjects.put(k.getId(), k);

                    break;
                case "w22":

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }


                    c = GeometricConstructions.w22((GeomPoint) newobjects.get(array[2]), (Circle) newobjects.get(array[3]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(c.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(k.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }

                    newobjects.put(k.getId(), k);
                    break;
                case "bisectorAngle":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = GeometricConstructions.bisectorAngle((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "medianLine":

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = GeometricConstructions.medianLine((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "line":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = new Line((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "circleAroundTriangle":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.circleAroundTriangle((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));

                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(c.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(k.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }

                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);
                    break;
                case "circleInsideTriangle":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.circleInsideTriangle((GeomPoint) newobjects.get(array[2]),
                            (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));

                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(c.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(k.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }
                    newobjects.put(k.getId(), k);
                    break;
                case "eulerPoint":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.eulerPoint((Line) newobjects.get(array[2]),
                            (Circle) newobjects.get(array[3]),
                            (Line) newobjects.get(array[4]));

                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setConstants(newobjects.get(array[2]).constants);
                        P.setId(array[1]);
                        P.setMove(P.getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);
                    break;
                case "triangle":
                    break;
                case "polygon":
                    break;
                case "circle":
                    break;
                case "point":
                    break;
                case "setIDTriangleLine":
                    break;
                case "addTriangle":
                    if (newobjects.get(array[1]) != null) {
                        newobjects.get(array[1]).setTriangle((Triangle) newobjects.get(array[2]));
                        ((Triangle) newobjects.get(array[2])).addSignificant(newobjects.get(array[1]).label(), newobjects.get(array[1]));
                        if (newobjects.get(array[1]) instanceof GeomPoint) {
                            ((GeomPoint) newobjects.get(array[1])).setLabelNum(((Triangle) newobjects.get(array[2])).getNumber());
                        }
                    }
                    break;
                case "addLabel":
                    if (array.length > 2) {
                        if (newobjects.get(array[1]) != null) {
                            newobjects.get(array[1]).setLabel(array[2]);
                        }
                    }
                    break;
                default:
                    break;
            }


        } catch (PatternSyntaxException e) {
            //Log.d("Greska sa regexom", "da");

        }
    }

    private int executeCommand2(String command, HashMap<String, GeometricObject> newobjects, UniqueID id, Constants constants) {
        try {
            String[] array = command.split("\\s+");
            GeomPoint X, Y, P, Q;
            Line l, p, x, y;
            Circle k, c;
            Vector<GeomPoint> points = new Vector<>();
            points.clear();
            int r;
            switch (array[0].trim()) {
                case "w01":
                    float d = array.length > 6 ? Float.parseFloat(array[5]) / Float.parseFloat(array[6])
                            : Float.parseFloat(array[5]);

                    Y = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (Y != null && !Y.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.w01((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]), d);

                    if (Y == null) {
                        Y = new GeomPoint(0, 0);
                        Y.setConstants(newobjects.get(array[2]).constants);
                        Y.setId(array[1]);
                        Y.setMove(false);
                    }
                    Y.setX(X.X());
                    Y.setY(X.Y());
                    newobjects.put(array[1], Y);
                    break;
                case "w02":

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = GeometricConstructions.w02((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }
                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w03":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.w03((Line) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setMove(P.getType() == GeomPoint.Type.TRIANGLE_FREE);
                        P.setConstants(newobjects.get(array[2]).constants);
                    }

                    if (X == null) {
                        newobjects.put(P.getId(), null);
                        break;
                    }

                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w04":
                    P = (GeomPoint) newobjects.get(array[1]);
                    Q = (GeomPoint) newobjects.get(array[2]);

                    if (newobjects.get(array[3]) == null || newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        if (Q != null && !Q.isFree()) {
                            newobjects.put(array[2], null);
                        }

                        break;
                    }


                    X = new GeomPoint(0, 0);
                    Y = new GeomPoint(0, 0);
                    r = GeometricConstructions.w04(((Line) newobjects.get(array[4])), ((Circle) newobjects.get(array[3])),
                            X, Y);

                    if (r == 0) {
                        newobjects.put(array[1], null);
                        newobjects.put(array[2], null);
                        break;
                    }

                    boolean heuristic = circleIntersectionHeuristic(P, Q, X, Y, r);

                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[3]).constants);
                        P.setMove(false);
                    }

                    if (Q == null) {
                        Q = new GeomPoint(0, 0);
                        Q.setId(array[2]);
                        Q.setConstants(newobjects.get(array[3]).constants);
                        Q.setMove(false);
                    }
                    if (!heuristic) {
                        P.setId(array[2]);
                        Q.setId(array[1]);
                    }

                    P.setX(X.X());
                    P.setY(X.Y());
                    if (!P.getId().equals("R")) {
                        newobjects.put(P.getId(), P);
                    }
                    Q.setX(Y.X());
                    Q.setY(Y.Y());
                    if (r == 1) {
                        Q.setX(X.X());
                        Q.setY(X.Y());
                    }
                    if (Q.getId().equals("R")) {
                        break;
                    }
                    newobjects.put(Q.getId(), Q);
                    break;
                case "w05":

                    String point = array[4];
                    if (array.length > 5) {
                        newobjects.put(array[5], newobjects.get(array[5]));
                        point = array[5];
                    }
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(point) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.w05(((Line) newobjects.get(array[3])), ((Circle) newobjects.get(array[2])),
                            (GeomPoint) (newobjects.get(point)));
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[2]).constants);
                        P.setMove(false);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w06":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.w06((GeomPoint) newobjects.get(array[3]), (GeomPoint) newobjects.get(array[2]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(false);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                    }

                    newobjects.put(array[1], k);
                    break;
                case "w07":
                    P = (GeomPoint) newobjects.get(array[1]);
                    Q = (GeomPoint) newobjects.get(array[2]);

                    if (newobjects.get(array[3]) == null || newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        if (Q != null && !Q.isFree()) {
                            newobjects.put(array[2], null);
                        }

                        break;
                    }

                    X = new GeomPoint(0, 0);
                    Y = new GeomPoint(0, 0);
                    r = GeometricConstructions.w07(((Circle) newobjects.get(array[3])), ((Circle) newobjects.get(array[4])),
                            X, Y);

                    if (r == 0) {
                        newobjects.put(array[1], null);
                        newobjects.put(array[2], null);
                        break;
                    }


                    boolean heuristicCircle = circleIntersectionHeuristic(P, Q, X, Y, r);

                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[3]).constants);
                        P.setMove(false);
                    }

                    if (Q == null) {
                        Q = new GeomPoint(0, 0);
                        Q.setId(array[2]);
                        Q.setConstants(newobjects.get(array[3]).constants);
                        Q.setMove(false);
                    }

                    if (!heuristicCircle) {
                        P.setId(array[2]);
                        Q.setId(array[1]);
                    }

                    P.setX(X.X());
                    P.setY(X.Y());
                    if (!P.getId().equals("R")) {
                        newobjects.put(P.getId(), P);
                    }

                    Q.setX(Y.X());
                    Q.setY(Y.Y());
                    if (r == 1) {
                        Q.setX(X.X());
                        Q.setY(X.Y());
                    }
                    if (Q.getId().equals("R")) {
                        break;
                    }

                    newobjects.put(Q.getId(), Q);
                    break;
                case "w08":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }
                    X = GeometricConstructions.w08(((Circle) newobjects.get(array[2])), ((Circle) newobjects.get(array[3])),
                            (GeomPoint) (newobjects.get(array[4])));
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[2]).constants);
                        P.setMove(false);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);


                    break;
                case "w09":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    c = GeometricConstructions.w09((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(false);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(false);
                    }

                    newobjects.put(k.getId(), k);
                    break;
                case "w10":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w10((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(array[1], p);
                    break;
                case "w11":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.w11((Line) newobjects.get(array[3]), (GeomPoint) newobjects.get(array[2]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(false);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(false);
                    }

                    newobjects.put(k.getId(), k);
                    break;
                case "w12":
                    if (newobjects.get(array[3]) == null || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        newobjects.put(array[2], null);
                        break;
                    }

                    x = new Line(null, null);
                    y = new Line(null, null);
                    r = GeometricConstructions.w12(((Circle) newobjects.get(array[3])), ((GeomPoint) newobjects.get(array[4])),
                            x, y);

                    if (r == 0) {
                        newobjects.put(array[1], null);
                        newobjects.put(array[2], null);
                        break;
                    }


                    p = (Line) newobjects.get(array[1]);
                    l = (Line) newobjects.get(array[2]);

                    heuristic = circleTangentHeuristic(p, l, x, y, r);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[3]).constants);
                        p.setId(array[1]);
                    }

                    if (l == null) {
                        l = new Line(null, null);
                        l.setConstants(newobjects.get(array[3]).constants);
                        l.setId(array[2]);
                    }

                    if (!heuristic) {
                        p.setId(array[2]);
                        l.setId(array[1]);
                    }

                    p.setBegin(x.getBegin());
                    p.setEnd(x.getEnd());
                    newobjects.put(p.getId(), p);

                    l.setBegin(y.getBegin());
                    l.setEnd(y.getEnd());

                    if (r == 1) {
                        l.setBegin(x.getBegin());
                        l.setEnd(x.getEnd());
                    }
                    newobjects.put(l.getId(), l);
                    break;
                case "w13":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[5]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    x = GeometricConstructions.w13(((Circle) newobjects.get(array[2])), ((GeomPoint) newobjects.get(array[3])),
                            (Line) (newobjects.get(array[5])));
                    l = (Line) newobjects.get(array[1]);
                    if (l == null) {
                        l = new Line(null, null);
                        l.setConstants(newobjects.get(array[2]).constants);
                        l.setId(array[1]);
                    }

                    l.setBegin(x.getBegin());
                    l.setEnd(x.getEnd());
                    newobjects.put(l.getId(), l);

                    break;
                case "w14":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w14((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w15":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w15((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]),
                            Float.parseFloat(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w16":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w16((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w17":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[5]) == null || newobjects.get(array[6]) == null
                            || newobjects.get(array[7]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }
                    l = GeometricConstructions.w17((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]), (GeomPoint) newobjects.get(array[7]),
                            Integer.parseInt(array[10]), Integer.parseInt(array[11]), Integer.parseInt(array[12]),
                            Integer.parseInt(array[13]));

                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w18":
                    if (newobjects.get(array[5]) == null || newobjects.get(array[6]) == null
                            || newobjects.get(array[7]) == null || newobjects.get(array[8]) == null
                            || newobjects.get(array[9]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = GeometricConstructions.w18((GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]),
                            (GeomPoint) newobjects.get(array[7]), (GeomPoint) newobjects.get(array[8]), (GeomPoint) newobjects.get(array[9]),
                            Integer.parseInt(array[10]), Integer.parseInt(array[11]), Integer.parseInt(array[12]),
                            Integer.parseInt(array[13]));

                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w19":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.w19(((GeomPoint) newobjects.get(array[2])), ((GeomPoint) newobjects.get(array[3])),
                            (GeomPoint) (newobjects.get(array[4])));
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                        P.setConstants(newobjects.get(array[2]).constants);
                        P.setMove(false);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);
                    break;
                case "w20":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null || newobjects.get(array[5]) == null
                            || newobjects.get(array[6]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.w20((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]), (GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]),
                            Integer.parseInt(array[7]), Integer.parseInt(array[8]), Integer.parseInt(array[9]),
                            Integer.parseInt(array[10]));

                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(false);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(false);
                    }


                    newobjects.put(k.getId(), k);

                    break;
                case "w22":

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }


                    c = GeometricConstructions.w22((GeomPoint) newobjects.get(array[2]), (Circle) newobjects.get(array[3]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(false);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(false);
                    }

                    newobjects.put(k.getId(), k);
                    break;
                case "bisectorAngle":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = GeometricConstructions.bisectorAngle((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "medianLine":

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = GeometricConstructions.medianLine((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "line":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    l = new Line((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setConstants(newobjects.get(array[2]).constants);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "circleAroundTriangle":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.circleAroundTriangle((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));

                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(false);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(false);
                    }

                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);
                    break;
                case "circleInsideTriangle":
                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null
                            || newobjects.get(array[4]) == null) {
                        newobjects.put(array[1], null);
                        break;
                    }

                    c = GeometricConstructions.circleInsideTriangle((GeomPoint) newobjects.get(array[2]),
                            (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));

                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(c.getCenter(), c.getRadius());
                        k.setConstants(newobjects.get(array[2]).constants);
                        k.getCenter().setMove(false);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(false);
                    }
                    newobjects.put(k.getId(), k);
                    break;
                case "eulerPoint":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (P != null && !P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        break;
                    }

                    X = GeometricConstructions.eulerPoint((Line) newobjects.get(array[2]),
                            (Circle) newobjects.get(array[3]),
                            (Line) newobjects.get(array[4]));

                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setConstants(newobjects.get(array[2]).constants);
                        P.setId(array[1]);
                        P.setMove(false);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);
                    break;
                case "triangle":
                    points.add((GeomPoint) newobjects.get(array[2]));
                    points.add((GeomPoint) newobjects.get(array[3]));
                    points.add((GeomPoint) newobjects.get(array[4]));

                    Triangle triangle = new Triangle(points);
                    triangle.setId(array[1]);
                    triangle.setNumber(id.getTrinagleNum());
                    triangle.setConstants(constants);
                    newobjects.put(array[1], triangle);
                    break;
                case "polygon":
                    points.clear();

                    for (int i = 2; i < array.length; i++) {
                        points.add((GeomPoint) newobjects.get(array[i]));
                    }

                    Polygon polygon = new Polygon(points);
                    polygon.setId(array[1]);
                    polygon.setConstants(constants);
                    newobjects.put(array[1], polygon);
                    break;
                case "circle":
                    c = new Circle((GeomPoint) newobjects.get(array[3]), Double.parseDouble(array[2]));
                    c.setId(array[1]);
                    c.setConstants(constants);
                    newobjects.put(array[1], c);
                    break;
                case "point":
                    X = new GeomPoint(Float.parseFloat(array[2]), Float.parseFloat(array[3]));
                    X.setId(array[1]);
                    X.setMove(true);
                    X.setConstants(constants);
                    newobjects.put(array[1], X);
                    break;
                case "setIDTriangleLine":
                    Triangle t = (Triangle) newobjects.get(array[1]);
                    t.setIDLines(array[2], array[3], array[4]);
                    newobjects.put(array[2], t.getLineA());
                    newobjects.put(array[3], t.getLineB());
                    newobjects.put(array[4], t.getLineC());
                    break;
                case "addTriangle":
                    if (newobjects.get(array[1]) != null) {
                        newobjects.get(array[1]).setTriangle((Triangle) newobjects.get(array[2]));
                        ((Triangle) newobjects.get(array[2])).addSignificant(newobjects.get(array[1]).label(), newobjects.get(array[1]));
                        if (newobjects.get(array[1]) instanceof GeomPoint) {
                            ((GeomPoint) newobjects.get(array[1])).setLabelNum(((Triangle) newobjects.get(array[2])).getNumber());
                        }
                    }
                    break;
                case "addLabel":
                    if (array.length > 2) {
                        if (newobjects.get(array[1]) != null) {
                            newobjects.get(array[1]).setLabel(array[2]);
                            if (newobjects.get(array[1]) instanceof GeomPoint) {
                                ((GeomPoint) newobjects.get(array[1])).setPointId(id.getPointNum());
                            }
                        }
                    }
                    break;
                default:
                    break;
            }
            if (array.length > 1) {
                return Integer.parseInt(array[1]);
            }

        } catch (PatternSyntaxException e) {
            //Log.d("Greska sa regexom", "da");

        }

        return -1;
    }

    private void prepisi(HashMap<String, GeometricObject> newObjects, HashMap<String, GeometricObject> oldObject, String[] array) {
        for (String name : array) {
            if (newObjects.get(name) == null && oldObject.get(name) != null) {
                newObjects.put(name, oldObject.get(name));
            }
        }
    }


    private boolean circleIntersectionHeuristic(GeomPoint P, GeomPoint Q, GeomPoint X, GeomPoint Y, int r) {
        if (r == 1) {
            if (P != null && Q != null) {
                return P.distance(X) <= Q.distance(X);
            } else {
                return true;
            }
        } else if (r == 2) {
            if (P == null && Q == null) {
                return true;
            } else if (P == null) {
                return Q.distance(X) > Q.distance(Y);
            } else if (Q == null) {
                return P.distance(X) <= P.distance(Y);
            } else {
                if ((P.distance(X) <= Q.distance(X) && P.distance(Y) <= Q.distance(Y)) ||
                        (P.distance(X) > Q.distance(X) && P.distance(Y) > Q.distance(Y))) {
                    return P.distance(X) <= P.distance(Y);
                } else {
                    return P.distance(X) <= Q.distance(X);
                }
            }
        }

        return false;
    }

    private boolean circleTangentHeuristic(Line l1, Line l2, Line p1, Line p2, int r) {
        GeomPoint X = p1.getEnd();
        GeomPoint Y = p2.getEnd();
        if (r == 1) {
            if (l1 != null && l2 != null) {
                return l1.distance(X) <= l2.distance(X);
            } else {
                return true;
            }
        } else if (r == 2) {
            if (l1 == null && l2 == null) {
                return true;
            } else if (l1 == null) {
                return l2.distance(X) > l2.distance(Y);
            } else if (l2 == null) {
                return l1.distance(X) <= l1.distance(Y);
            } else {
                if ((l1.distance(X) <= l2.distance(X) && l1.distance(Y) <= l2.distance(Y)) ||
                        (l1.distance(X) > l2.distance(X) && l1.distance(Y) > l2.distance(Y))) {
                    return l1.distance(X) <= l1.distance(Y);
                } else {
                    return l1.distance(X) <= l2.distance(X);
                }
            }
        }

        return false;
    }

    public int openNew(Vector<String> commands, HashMap<String, GeometricObject> objects, UniqueID id, Constants constants) {
        int max = -1;
        for (String commad : commands) {
            int i = executeCommand2(commad, objects, id, constants);
            if (i > max) {
                max = i;
            }
        }

        return max;
    }
}
