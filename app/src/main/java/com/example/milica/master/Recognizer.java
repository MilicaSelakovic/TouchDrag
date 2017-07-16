package com.example.milica.master;

import android.graphics.PointF;

import com.example.descretegeometrycalculations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Recognizer {
    private UniqueID uniqueID;

    public Recognizer(UniqueID identifier) {
        uniqueID = identifier;
    }


    public GeometricObject recognizeCurrent(Vector<PointF> points) {
        return DiscreteCurvature.getGeometricObject(points);
    }

    public boolean recognize(Vector<PointF> points, HashMap<String, GeometricObject> objects, Vector<String> commands) {
        GeometricObject recognized = DiscreteCurvature.getGeometricObject(points);

        if (recognized == null) {
            return false;
        }

        String id = uniqueID.getID();
        recognized.setId(id);


        for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
            if (entry.getValue().connection(recognized, commands)) {
                objects.put(id, recognized);
                return true;
            }
        }

        if (recognized instanceof GeomPoint) {
            commands.add("point " + id);
        }

        if (recognized instanceof Circle) {
            GeomPoint center = ((Circle) recognized).getCenter();
            Double r = ((Circle) recognized).getRadius();
            String ID = uniqueID.getID();
            center.setId(ID);
            objects.put(ID, center);
            commands.add("circle " + id + " " + Double.toString(r) + " " + ID);
        }

        if (recognized instanceof Line) {
            GeomPoint x = ((Line) recognized).getBegin();
            GeomPoint y = ((Line) recognized).getEnd();

            String idx = uniqueID.getID();
            x.setId(idx);
            String idy = uniqueID.getID();
            y.setId(idy);
            objects.put(idx, x);
            objects.put(idy, y);

            commands.add("line " + id + " " + idx + " " + idy);

        }

        if (recognized instanceof Triangle) {
            Vector<GeomPoint> polyPoints = ((Polygon) recognized).getPoints();

            String command = "triangle " + id + " ";

            for (GeomPoint p : polyPoints) {
                String idp = uniqueID.getID();
                p.setId(idp);
                objects.put(idp, p);
                command += idp + " ";
            }

            String idA = uniqueID.getID();
            String idB = uniqueID.getID();
            String idC = uniqueID.getID();

            ((Triangle) recognized).setIDLines(idA, idB, idC);
            // TODO srediti ove linije
            objects.put(idA, ((Triangle) recognized).getLineA());
            objects.put(idB, ((Triangle) recognized).getLineB());
            objects.put(idC, ((Triangle) recognized).getLineC());

            commands.add("line " + idA + " " + polyPoints.elementAt(1).getId() + " " + polyPoints.elementAt(2).getId());
            commands.add("line " + idB + " " + polyPoints.elementAt(0).getId() + " " + polyPoints.elementAt(2).getId());
            commands.add("line " + idC + " " + polyPoints.elementAt(0).getId() + " " + polyPoints.elementAt(1).getId());
            commands.add(command);
            objects.put(id, recognized);
            return true;
        }


        if (recognized instanceof Polygon) {
            Vector<GeomPoint> polyPoints = ((Polygon) recognized).getPoints();

            String command = "polygon " + id + " ";

            for (GeomPoint p : polyPoints) {
                String idp = uniqueID.getID();
                p.setId(idp);
                objects.put(idp, p);
                command += idp + " ";
                objects.put(idp, p);
            }

            commands.add(command);

        }


        objects.put(id, recognized);

        return true;
    }

}
