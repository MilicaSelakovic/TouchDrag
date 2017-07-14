package com.example.descretegeometrycalculations;

import android.util.Log;

import java.util.HashMap;
import java.util.Vector;
import java.util.regex.PatternSyntaxException;

public class Contructor {

    public Contructor() {

    }


    public void reconstruct(Vector<String> commands, HashMap<String, GeometricObject> objects) {
        for (String command : commands) {
            executeCommand(command, objects);
        }

    }


    private void executeCommand(String command, HashMap<String, GeometricObject> objects) {
        try {
            String[] array = command.split("\\s+");
            GeomPoint X, P;
            Line l, p;
            Circle k;
            switch (array[0].trim()) {
                case "w01":
                    X = GeometricConstructions.w01((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[4]), Float.parseFloat(array[5]));
                    GeomPoint Y = (GeomPoint) objects.get(array[1]);
                    if (Y == null) {
                        Y = new GeomPoint(0, 0);
                        Y.setId(array[1]);
                    }
                    Y.setX(X.X());
                    Y.setY(X.Y());
                    objects.put(array[1], Y);
                    break;
                case "w02":
                    l = GeometricConstructions.w02((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }
                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    objects.put(p.getId(), p);
                    break;
                case "w03":
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
                    X = GeometricConstructions.w05(((Line) objects.get(array[2])), ((Circle) objects.get(array[3])),
                            (GeomPoint) (objects.get(array[4])));
                    P = (GeomPoint) objects.get(array[1]);
                    if (P == null) {
                        P = new GeomPoint(0, 0);
                        P.setId(array[1]);
                    }
                    P.setX(X.X());
                    P.setY(X.Y());
                    objects.put(P.getId(), P);

                    break;
                case "w06":
                    k = GeometricConstructions.w06((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    Circle c = (Circle) objects.get(array[1]);
                    if (c == null) {
                        c = new Circle(0, 0, 0);
                        c.setId(array[1]);
                    }

                    c.setRadius(k.getRadius());
                    c.setCenter(k.getCenter());
                    objects.put(array[1], c);
                    break;
                case "w07":
                    break;
                case "w08":
                    break;
                case "w09":
                    break;
                case "w10":
                    l = GeometricConstructions.w10((GeomPoint) objects.get(array[2]), (Line) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    break;
                case "w11":
                    break;
                case "w12":
                    break;
                case "w13":
                    break;
                case "w14":
                    l = GeometricConstructions.w14((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    objects.put(p.getId(), p);
                    break;
                case "w15":
                    break;
                case "w16":
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
                    l = GeometricConstructions.bisectorAngle((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[4]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    objects.put(p.getId(), p);
                    break;
                case "centroid":
                    l = GeometricConstructions.centroid((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]),
                            (GeomPoint) objects.get(array[4]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    objects.put(p.getId(), p);
                    break;
                case "line":
                    l = new Line((GeomPoint) objects.get(array[2]), (GeomPoint) objects.get(array[3]));
                    p = (Line) objects.get(array[1]);
                    if (p == null) {
                        p = new Line(null, null);
                        p.setId(array[1]);
                    }

                    p.setBegin(l.getBegin());
                    p.setEnd(l.getEnd());
                    objects.put(p.getId(), p);
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
}
