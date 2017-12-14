package geometry.calculations.descretegeometrycalculations;


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
                        if (Y != null && Y.isFree()) {
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
                        if (P != null && P.isFree()) {
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


                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w04":
                    P = (GeomPoint) newobjects.get(array[1]);
                    Q = (GeomPoint) newobjects.get(array[2]);

                    if (newobjects.get(array[3]) == null || newobjects.get(array[4]) == null) {
                        if (P != null && P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        if (Q != null && Q.isFree()) {
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

                    if (r == 1) {
                        newobjects.put(array[2], null);
                    }


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

                    P.setX(X.X());
                    P.setY(X.Y());
                    if (!P.getId().equals("R")) {
                        newobjects.put(P.getId(), P);
                    }
                    Q.setX(Y.X());
                    Q.setY(Y.Y());
                    if (r == 1 || Q.getId().equals("R")) {
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
                        if (P != null && P.isFree()) {
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
                        ;
                        c.getCenter().setMove(c.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                        k.setId(array[1]);
                    } else {
                        k.getCenter().setX(c.getCenter().X());
                        k.getCenter().setY(c.getCenter().Y());
                        k.setRadius(c.getRadius());
                        k.getCenter().setMove(k.getCenter().getType() == GeomPoint.Type.TRIANGLE_FREE);
                    }

                    newobjects.put(array[1], c);
                    break;
                case "w07":
                    P = (GeomPoint) newobjects.get(array[1]);
                    Q = (GeomPoint) newobjects.get(array[2]);

                    if (newobjects.get(array[3]) == null || newobjects.get(array[4]) == null) {
                        if (P != null && P.isFree()) {
                            newobjects.put(array[1], null);
                        }

                        if (Q != null && Q.isFree()) {
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

                    if (r == 1) {
                        newobjects.put(array[2], null);
                    }

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

                    P.setX(X.X());
                    P.setY(X.Y());
                    if (!P.getId().equals("R")) {
                        newobjects.put(P.getId(), P);
                    }

                    Q.setX(Y.X());
                    Q.setY(Y.Y());
                    if (r == 1 || Q.getId().equals("R")) {
                        break;
                    }

                    newobjects.put(Q.getId(), Q);
                    break;
                case "w08":
                    P = (GeomPoint) newobjects.get(array[1]);

                    if (newobjects.get(array[2]) == null || newobjects.get(array[3]) == null ||
                            newobjects.get(array[4]) == null) {
                        if (P != null && P.isFree()) {
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
                    GeometricConstructions.w12(((Circle) newobjects.get(array[3])), ((GeomPoint) newobjects.get(array[4])),
                            x, y);

                    p = (Line) newobjects.get(array[1]);
                    l = (Line) newobjects.get(array[2]);

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

                    p.setBegin(x.getBegin());
                    p.setEnd(x.getEnd());
                    newobjects.put(p.getId(), p);

                    l.setBegin(y.getBegin());
                    l.setEnd(y.getEnd());
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
                        if (P != null && P.isFree()) {
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
                        if (P != null && P.isFree()) {
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
                default:
                    break;
            }


        } catch (PatternSyntaxException e) {
            //Log.d("Greska sa regexom", "da");

        }
    }

    private void prepisi(HashMap<String, GeometricObject> newObjects, HashMap<String, GeometricObject> oldObject, String[] array) {
        for (String name : array) {
            if (newObjects.get(name) == null && oldObject.get(name) != null) {
                newObjects.put(name, oldObject.get(name));
            }
        }
    }
}
