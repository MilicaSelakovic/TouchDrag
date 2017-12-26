package touch.drag.milica.master;

import android.graphics.PointF;

import geometry.calculations.descretegeometrycalculations.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class Recognizer {
    private UniqueID uniqueID;

    public Recognizer(UniqueID identifier) {
        uniqueID = identifier;
    }


    public GeometricObject recognizeCurrent(Vector<PointF> points, HashMap<String, GeometricObject> objects, DiscreteCurvature discreteCurvature) {
        GeometricObject recognized = discreteCurvature.getGeometricObject(points);

        if (recognized == null || recognized instanceof GeomPoint) {
            return recognized;
        }

        for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
            if (entry.getValue() == null)
                continue;
            if (entry.getValue() instanceof Triangle && entry.getValue().connection(recognized, null, null, objects)) {
                return recognized;
            }
        }

        for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
            if (entry.getValue() == null)
                continue;
            if (entry.getValue() instanceof GeomPoint && entry.getValue().connection(recognized, null, null, objects)) {
                return recognized;
            }
        }

        for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
            if (entry.getValue() == null)
                continue;
            if (!(entry.getValue() instanceof GeomPoint) &&
                    entry.getValue().connection(recognized, null, null, objects)) {
                return recognized;
            }
        }

        return recognized;
    }

    public boolean recognize(Vector<PointF> points, HashMap<String, GeometricObject> objects, Vector<Vector<String>> mCommands, DiscreteCurvature discreteCurvature) {
        Vector<String> commands = new Vector<>();
        GeometricObject recognized = discreteCurvature.getGeometricObject(points);

        if (recognized == null) {
            return false;
        }

        String id = uniqueID.getID();
        recognized.setId(id);


        for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
            if (entry.getValue() == null)
                continue;
            if (entry.getValue() instanceof Triangle && entry.getValue().connection(recognized, commands, uniqueID, objects)) {
                objects.put(id, recognized);
                if (recognized instanceof Circle) {
                    objects.put(((Circle) recognized).getCenter().getId(), ((Circle) recognized).getCenter());
                }
                mCommands.add(commands);
                return true;
            }
        }

        for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
            if (entry.getValue() == null)
                continue;
            if (entry.getValue() instanceof GeomPoint && entry.getValue().connection(recognized, commands, uniqueID, objects)) {
                objects.put(id, recognized);

                mCommands.add(commands);
                return true;
            }
        }

        for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
            if (entry.getValue() == null)
                continue;
            if (!(entry.getValue() instanceof GeomPoint) &&
                    entry.getValue().connection(recognized, commands, uniqueID, objects)) {
                objects.put(id, recognized);
                mCommands.add(commands);
                return true;
            }
        }

        if (recognized instanceof GeomPoint) {
            ((GeomPoint) recognized).setPointId(uniqueID.getPointNum());
            commands.add("point " + id);
        }

        if (recognized instanceof Circle) {
            GeomPoint center = ((Circle) recognized).getCenter();
            Double r = ((Circle) recognized).getRadius();
            String ID = uniqueID.getID();
            center.setId(ID);
            center.setPointId(uniqueID.getPointNum());
            objects.put(ID, center);
            commands.add("point " + ID);
            commands.add("circle " + id + " " + Double.toString(r) + " " + ID);
        }

        if (recognized instanceof Line) {
            GeomPoint x = ((Line) recognized).getBegin();
            GeomPoint y = ((Line) recognized).getEnd();

            if (x.getId().compareTo("") == 0) {
                String idx = uniqueID.getID();
                x.setId(idx);
                x.setPointId(uniqueID.getPointNum());
                objects.put(idx, x);
                commands.add("point " + idx);
            }

            if (y.getId().compareTo("") == 0) {
                String idy = uniqueID.getID();
                y.setPointId(uniqueID.getPointNum());
                y.setId(idy);
                objects.put(idy, y);
                commands.add("point " + idy);

            }
            commands.add("line " + id + " " + x.getId() + " " + y.getId());

        }

        if (recognized instanceof Triangle) {
            ((Triangle) recognized).setNumber(uniqueID.getTrinagleNum());

            Vector<GeomPoint> polyPoints = ((Polygon) recognized).getPoints();

            String command = "triangle " + id + " ";

            for (GeomPoint p : polyPoints) {
                String idp = uniqueID.getID();
                p.setId(idp);
                objects.put(idp, p);
                commands.add("point " + idp);
                command += idp + " ";
            }

            String idA = uniqueID.getID();
            String idB = uniqueID.getID();
            String idC = uniqueID.getID();

            ((Triangle) recognized).setIDLines(idA, idB, idC);
            objects.put(idA, ((Triangle) recognized).getLineA());
            objects.put(idB, ((Triangle) recognized).getLineB());
            objects.put(idC, ((Triangle) recognized).getLineC());

            commands.add(command);
            commands.add("setIDTriangleLine " + id + " " + idA + " " + idB + " " + idC);

            objects.put(id, recognized);

            mCommands.add(commands);
            return true;
        }


        if (recognized instanceof Polygon) {
            Vector<GeomPoint> polyPoints = ((Polygon) recognized).getPoints();

            String command = "polygon " + id + " ";

            for (GeomPoint p : polyPoints) {
                String idp = uniqueID.getID();
                p.setId(idp);
                p.setPointId(uniqueID.getPointNum());
                command += idp + " ";
                objects.put(idp, p);
                commands.add("point " + idp);
            }

            commands.add(command);

        }


        objects.put(id, recognized);
        mCommands.add(commands);
        return true;
    }
}
