package com.example.descretegeometrycalculations;

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
            GeomPoint X, P;
            Line l, p;
            Circle k;
            switch (array[0].trim()) {
                case "w01":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));
                    X = GeometricConstructions.w01((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[4]), Float.parseFloat(array[5]));
                    GeomPoint Y = (GeomPoint) objects.get(array[1]);
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
                    objects.put(P.getId(), P);
                    break;
                case "w04":
                    break;
                case "w05":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));
                    newobjects.put(array[4], objects.get(array[4]));

                    X = GeometricConstructions.w05(((Line) objects.get(array[2])), ((Circle) objects.get(array[3])),
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
                case "w06":
                    newobjects.put(array[2], objects.get(array[2]));
                    newobjects.put(array[3], objects.get(array[3]));

                    k = GeometricConstructions.w06((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    Circle c = (Circle) objects.get(array[1]);
                    if (c == null) {
                        c = new Circle(0, 0, 0);
                        c.setId(array[1]);
                    }

                    c.setRadius(k.getRadius());
                    c.setCenter(k.getCenter());
                    newobjects.put(array[1], c);
                    break;
                case "w07":
                    break;
                case "w08":
                    break;
                case "w09":
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
                    break;
                case "w12":
                    break;
                case "w13":
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
                    break;
                case "w18":
                    break;
                case "w19":
                    break;
                case "w20":
                    break;
                case "w21":
                    break;
                case "w22":
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
