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
            prepisi(newobjects, objects, array);
            switch (array[0].trim()) {
                case "w01":
                    float d = array.length > 6 ? Float.parseFloat(array[5]) / Float.parseFloat(array[6])
                            : Float.parseFloat(array[5]);

                    X = GeometricConstructions.w01((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]), d);
                    Y = (GeomPoint) newobjects.get(array[1]);
                    if (Y == null) {
                        Y = new GeomPoint(0, 0);
                        Y.setId(array[1]);
                    }
                    Y.setX(X.X());
                    Y.setY(X.Y());
                    newobjects.put(array[1], Y);
                    break;
                case "w02":

                    l = GeometricConstructions.w02((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }
                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w03":

                    X = GeometricConstructions.w03((Line) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    P = (GeomPoint) newobjects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }


                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w04":
                    X = new GeomPoint(0, 0);
                    Y = new GeomPoint(0, 0);
                    GeometricConstructions.w04(((Line) newobjects.get(array[4])), ((Circle) newobjects.get(array[3])),
                            X, Y);

                    P = (GeomPoint) newobjects.get(array[1]);
                    Q = (GeomPoint) newobjects.get(array[2]);

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
                    String point = array[4];
                    if (array.length > 5) {
                        newobjects.put(array[5], newobjects.get(array[5]));
                        point = array[5];
                    }
                    X = GeometricConstructions.w05(((Line) newobjects.get(array[3])), ((Circle) newobjects.get(array[2])),
                            (GeomPoint) (objects.get(point)));
                    P = (GeomPoint) newobjects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);

                    break;
                case "w06":
                    k = GeometricConstructions.w06((GeomPoint) newobjects.get(array[3]), (GeomPoint) newobjects.get(array[2]));
                    c = (Circle) newobjects.get(array[1]);
                    if (c == null) {
                        c = new Circle(0, 0, 0);
                        c.setId(array[1]);
                    }

                    c.setRadius(k.getRadius());
                    c.setCenter(k.getCenter());
                    newobjects.put(array[1], c);
                    break;
                case "w07":
                    X = new GeomPoint(0, 0);
                    Y = new GeomPoint(0, 0);
                    GeometricConstructions.w07(((Circle) newobjects.get(array[3])), ((Circle) newobjects.get(array[4])),
                            X, Y);

                    P = (GeomPoint) newobjects.get(array[1]);
                    Q = (GeomPoint) newobjects.get(array[2]);

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
                    X = GeometricConstructions.w08(((Circle) newobjects.get(array[2])), ((Circle) newobjects.get(array[3])),
                            (GeomPoint) (objects.get(array[4])));
                    P = (GeomPoint) newobjects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);


                    break;
                case "w09":
                    c = GeometricConstructions.w09((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(null, 0);
                        k.setId(array[1]);
                    }
                    k.setCenter(c.getCenter());
                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);
                    break;
                case "w10":
                    l = GeometricConstructions.w10((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(array[1], p);
                    break;
                case "w11":
                    c = GeometricConstructions.w11((Line) newobjects.get(array[3]), (GeomPoint) newobjects.get(array[2]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(null, 0);
                        k.setId(array[1]);
                    }
                    k.setCenter(c.getCenter());
                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);
                    break;
                case "w12":
                    x = new Line(null, null);
                    y = new Line(null, null);
                    GeometricConstructions.w12(((Circle) newobjects.get(array[3])), ((GeomPoint) newobjects.get(array[4])),
                            x, y);

                    p = (Line) newobjects.get(array[1]);
                    l = (Line) newobjects.get(array[2]);

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
                    x = GeometricConstructions.w13(((Circle) newobjects.get(array[2])), ((GeomPoint) newobjects.get(array[3])),
                            (Line) (objects.get(array[5])));
                    l = (Line) newobjects.get(array[1]);
                    if (l == null) {
                        l = new Line(null, null);
                        l.setId(array[1]);
                    }

                    l.setBegin(x.getBegin());
                    l.setEnd(x.getEnd());
                    newobjects.put(l.getId(), l);

                    break;
                case "w14":
                    l = GeometricConstructions.w14((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w15":
                    l = GeometricConstructions.w15((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]),
                            Float.parseFloat(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w16":
                    l = GeometricConstructions.w16((GeomPoint) newobjects.get(array[2]), (Line) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w17":

                    l = GeometricConstructions.w17((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]), (GeomPoint) newobjects.get(array[7]),
                            Integer.parseInt(array[10]), Integer.parseInt(array[11]), Integer.parseInt(array[12]),
                            Integer.parseInt(array[13]));

                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);

                    break;
                case "w18":

                    l = GeometricConstructions.w17((GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]),
                            (GeomPoint) newobjects.get(array[7]), (GeomPoint) newobjects.get(array[8]), (GeomPoint) newobjects.get(array[9]),
                            Integer.parseInt(array[10]), Integer.parseInt(array[11]), Integer.parseInt(array[12]),
                            Integer.parseInt(array[13]));

                    p = (Line) newobjects.get(array[1]);

                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "w19":
                    X = GeometricConstructions.w19(((GeomPoint) newobjects.get(array[2])), ((GeomPoint) newobjects.get(array[3])),
                            (GeomPoint) (objects.get(array[4])));
                    P = (GeomPoint) newobjects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    newobjects.put(P.getId(), P);
                    break;
                case "w20":

                    c = GeometricConstructions.w20((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]), (GeomPoint) newobjects.get(array[5]), (GeomPoint) newobjects.get(array[6]),
                            Integer.parseInt(array[7]), Integer.parseInt(array[8]), Integer.parseInt(array[9]),
                            Integer.parseInt(array[10]));

                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(null, 0);
                        k.setId(array[1]);
                    }
                    k.setCenter(c.getCenter());
                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);

                    break;
                case "w22":

                    c = GeometricConstructions.w22((GeomPoint) newobjects.get(array[2]), (Circle) newobjects.get(array[3]));
                    k = (Circle) newobjects.get(array[1]);
                    if (k == null) {
                        k = new Circle(null, 0);
                        k.setId(array[1]);
                    }
                    k.setCenter(c.getCenter());
                    k.setRadius(c.getRadius());
                    newobjects.put(k.getId(), k);
                    break;
                case "bisectorAngle":
                    l = GeometricConstructions.bisectorAngle((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "medianLine":

                    l = GeometricConstructions.medianLine((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]),
                            (GeomPoint) newobjects.get(array[4]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
                    break;
                case "line":

                    l = new Line((GeomPoint) newobjects.get(array[2]), (GeomPoint) newobjects.get(array[3]));
                    p = (Line) newobjects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    newobjects.put(p.getId(), p);
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
