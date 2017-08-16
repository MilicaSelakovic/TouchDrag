package com.example.descretegeometrycalculations;

import org.opencv.core.Point;

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
            switch (array[0].trim()) {
                case "w01":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));
                    float d = array.length > 6 ? Float.parseFloat(array[5]) / Float.parseFloat(array[6])
                            : Float.parseFloat(array[5]);

                    X = GeometricConstructions.w01((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[4]), d);
                    Y = (GeomPoint) objects.get(array[1]);
                    if (Y == null) {
                        Y = new GeomPoint(0, 0);
                        Y.setId(array[1]);
                    }
                    Y.setX(X.X());
                    Y.setY(X.Y());
                    newobjects.put(array[1], Y);
                    break;
                case "w02":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    l = GeometricConstructions.w02((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }
                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w03":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    X = GeometricConstructions.w03((Line) objects.get(array[2]), (Line) objects.get(array[3]));
                    P = (GeomPoint) objects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }


                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w04":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));
                    X = new GeomPoint(0, 0);
                    Y = new GeomPoint(0, 0);
                    GeometricConstructions.w04(((Line) objects.get(array[4])), ((Circle) objects.get(array[3])),
                            X, Y);

                    P = (GeomPoint) objects.get(array[1]);
                    Q = (GeomPoint) objects.get(array[2]);

                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }

                    if (Q == null) {
                        Q = new GeomPoint(0, 0);
                        Q.setId(array[2]);
                    }

                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    Q.setX(Y.X());
                    Q.setY(Y.Y());
                    newobjects.put(Q.getId(), Q);
                    break;
                case "w05":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    String point = array[4];
                    if (array.length > 5) {
                        newobjects.put(array[5], objects.get(array[5]));
                        point = array[5];
                    }
                    X = GeometricConstructions.w05(((Line) objects.get(array[3])), ((Circle) objects.get(array[2])),
                            (GeomPoint) (objects.get(point)));
                    P = (GeomPoint) objects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w06":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    k = GeometricConstructions.w06((GeomPoint) objects.get(array[3]), (GeomPoint) objects.get(array[2]));
                    c = (Circle) objects.get(array[1]);
                    if (c == null) {
                        c = new Circle(0, 0, 0);
                        c.setId(array[1]);
                    }

                    c.setRadius(k.getRadius());
                    c.setCenter(k.getCenter());
                    newobjects.put(array[1], c);
                    break;
                case "w07":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));
                    X = new GeomPoint(0, 0);
                    Y = new GeomPoint(0, 0);
                    GeometricConstructions.w07(((Circle) objects.get(array[3])), ((Circle) objects.get(array[4])),
                            X, Y);

                    P = (GeomPoint) objects.get(array[1]);
                    Q = (GeomPoint) objects.get(array[2]);

                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }

                    if (Q == null) {
                        Q = new GeomPoint(0, 0);
                        Q.setId(array[2]);
                    }

                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    Q.setX(Y.X());
                    Q.setY(Y.Y());
                    newobjects.put(Q.getId(), Q);
                    break;
                case "w08":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));

                    X = GeometricConstructions.w08(((Circle) objects.get(array[2])), ((Circle) objects.get(array[3])),
                            (GeomPoint) (objects.get(array[4])));
                    P = (GeomPoint) objects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);


                    break;
                case "w09":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    c = GeometricConstructions.w09((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    k = (Circle) objects.get(array[1]);
                    if (k == null) {
                        k = new Circle(null, 0);
                        k.setId(array[1]);
                    }
                    k.setCenter(c.getCenter());
                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);
                    break;
                case "w10":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    l = GeometricConstructions.w10((GeomPoint) objects.get(array[2]), (Line) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(array[1], p);
                    break;
                case "w11":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    c = GeometricConstructions.w11((Line) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    k = (Circle) objects.get(array[1]);
                    if (k == null) {
                        k = new Circle(null, 0);
                        k.setId(array[1]);
                    }
                    k.setCenter(c.getCenter());
                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);
                    break;
                case "w12":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));
                    x = new Line(null, null);
                    y = new Line(null, null);
                    GeometricConstructions.w12(((Circle) objects.get(array[3])), ((GeomPoint) objects.get(array[4])),
                            x, y);

                    p = (Line) objects.get(array[1]);
                    l = (Line) objects.get(array[2]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    if (l == null) {
                        l = new Line(null, null);
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
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));

                    x = GeometricConstructions.w13(((Circle) objects.get(array[2])), ((GeomPoint) objects.get(array[3])),
                            (Line) (objects.get(array[4])));
                    l = (Line) objects.get(array[1]);
                    if (l == null) {
                        l = new Line(null, null);
                        l.setId(array[1]);
                    }

                    l.setBegin(x.getBegin());
                    l.setEnd(x.getEnd());
                    newobjects.put(l.getId(), l);

                    break;
                case "w14":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    l = GeometricConstructions.w14((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w15":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    l = GeometricConstructions.w15((GeomPoint) objects.get(array[2]), (Line) objects.get(array[3]),
                            Float.parseFloat(array[4]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w16":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    l = GeometricConstructions.w16((GeomPoint) objects.get(array[2]), (Line) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w17":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[5], objects.get(array[5]));
                    newobjects.put(array[6], objects.get(array[6]));
                    newobjects.put(array[7], objects.get(array[7]));

                    l = GeometricConstructions.w17((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[5]), (GeomPoint) objects.get(array[6]), (GeomPoint) objects.get(array[7]),
                            Integer.parseInt(array[10]), Integer.parseInt(array[11]), Integer.parseInt(array[12]),
                            Integer.parseInt(array[13]));

                    p = (Line) objects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w18":
                    break;
                case "w19":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));

                    X = GeometricConstructions.w19(((GeomPoint) objects.get(array[2])), ((GeomPoint) objects.get(array[3])),
                            (GeomPoint) (objects.get(array[4])));
                    P = (GeomPoint) objects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);
                    break;
                case "w20":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[5], objects.get(array[4]));
                    newobjects.put(array[5], objects.get(array[5]));
                    newobjects.put(array[6], objects.get(array[6]));

                    c = GeometricConstructions.w20((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[4]), (GeomPoint) objects.get(array[5]), (GeomPoint) objects.get(array[6]),
                            Integer.parseInt(array[7]), Integer.parseInt(array[8]), Integer.parseInt(array[9]),
                            Integer.parseInt(array[10]));

                    k = (Circle) objects.get(array[1]);
                    if (k == null) {
                        k = new Circle(null, 0);
                        k.setId(array[1]);
                    }
                    k.setCenter(c.getCenter());
                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);

                    break;
                case "w22":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    c = GeometricConstructions.w22((GeomPoint) objects.get(array[2]), (Circle) objects.get(array[3]));
                    k = (Circle) objects.get(array[1]);
                    if (k == null) {
                        k = new Circle(null, 0);
                        k.setId(array[1]);
                    }
                    k.setCenter(c.getCenter());
                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);
                    break;
                case "bisectorAngle":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));

                    l = GeometricConstructions.bisectorAngle((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[4]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "medianLine":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));

                    l = GeometricConstructions.medianLine((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[4]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "line":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    l = new Line((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "triangle":
                    newobjects.put(array[1], objects.get(array[1]));
                    break;
                case "polygon":
                    newobjects.put(array[1], objects.get(array[1]));
                    break;
                case "circle":
                    newobjects.put(array[1], objects.get(array[1]));
                    break;
                case "point":
                    newobjects.put(array[1], objects.get(array[1]));
                    break;
                default:
                    break;
            }


        } catch (PatternSyntaxException e) {
            //Log.d("Greska sa regexom", "da");

        }
    }
}
