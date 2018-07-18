package touch.drag.milica.master;

import android.graphics.PointF;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import geometry.calculations.descretegeometrycalculations.Constants;
import geometry.calculations.descretegeometrycalculations.DiscreteCurvature;
import geometry.calculations.descretegeometrycalculations.GeomPoint;
import geometry.calculations.descretegeometrycalculations.GeometricConstructor;
import geometry.calculations.descretegeometrycalculations.GeometricObject;
import geometry.calculations.descretegeometrycalculations.Triangle;
import geometry.calculations.descretegeometrycalculations.UniqueID;

public class DrawingModel {
    private HashMap<String, GeometricObject> geometricObjects;
    private HashMap<String, GeometricObject> oldObjects;

    private Stack<Vector<String>> commands;
    private Stack<String> komande; // add ili translate
    private Stack<String> redo;
    private Stack<Vector<String>> redoCommands;

    public HashMap<String, GeometricObject> getGeometricObjects() {
        return geometricObjects;
    }

    public GeometricObject getCurrent() {
        return current;
    }

    private GeometricObject current;
    private Recognizer recognizer;
    private UniqueID uniqueID;
    private GeometricConstructor geometricConstructor;
    private HashMap<String, Vector<String>> trics;

    private DiscreteCurvature discreteCurvature;
    private Constants constants;

    private float prevX, prevY;

    public DrawingModel(){
        uniqueID = new UniqueID();
        recognizer = new Recognizer(uniqueID);
        geometricConstructor = new GeometricConstructor();

        geometricObjects = new HashMap<>();
        oldObjects = new HashMap<>();

        commands = new Stack<>();
        redoCommands = new Stack<>();
        komande = new Stack<>();
        redo = new Stack<>();

        trics = new HashMap<>();

        discreteCurvature = null;
        constants = null;

    }

    public void clear(){
        geometricObjects.clear();
        commands.clear();
        oldObjects.clear();
        komande.clear();
        redo.clear();
        redoCommands.clear();

        uniqueID.reset();
    }

    void setUp(Constants constants, DiscreteCurvature discreteCurvature){
        this.constants = constants;
        this.discreteCurvature = discreteCurvature;
    }

    public void setTrics(HashMap<String, Vector<String>> trics) {
        this.trics = trics;
    }

    public void redo() {
        if (!redo.isEmpty()) {
            String kom = redo.pop();
            if (kom.compareTo("add") == 0) {
                Vector<String> redoCom = redoCommands.pop();
                int max = -1;
                int maxPoint = -1;

                for (String command : redoCom) {
                    String array[] = command.split("\\s+");
                    GeometricObject gobj = geometricObjects.get(array[1]);
                    if (gobj != null && gobj.getTriangle() != null) {
                        gobj.getTriangle().addSignificant(gobj.label(), gobj);
                    }
                    if (gobj != null && gobj instanceof Triangle) {
                        String tid = ((Triangle) gobj).getNumber();
                        uniqueID.setRedoTrin(Integer.parseInt(tid));
                    }
                    int i = Integer.parseInt(array[1]);
                    if (gobj != null && gobj instanceof GeomPoint) {
                        int k = Integer.parseInt(((GeomPoint) gobj).getPointId());
                        if (k > maxPoint) {
                            maxPoint = k;
                        }
                    }
                    if (i > max) {
                        max = i;
                    }
                }

                uniqueID.setRedoLast(max);

                if (maxPoint != -1) {
                    uniqueID.setRedoPoint(maxPoint);
                }

                commands.push(redoCom);
                komande.push("add");
            } else {
                String[] array = kom.split("\\s+");
                float dx = Float.parseFloat(array[2]);
                float dy = Float.parseFloat(array[3]);

                GeomPoint point = (GeomPoint) geometricObjects.get(array[1]);

                point.translate(point.X() + dx, point.Y() + dy, geometricObjects);

                komande.push("translate " + array[1] + " " + Float.toString(-dx) + " " + Float.toString(-dy));

                if (array.length > 4) {
                    ((Triangle) geometricObjects.get(array[4])).recontruct(trics, array[5], array[6], array[7]);
                }
            }

            geometricObjects.clear();
            geometricConstructor.reconstructNew(commands, geometricObjects, oldObjects);
            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                    ((Triangle) entry.getValue()).recolor(trics);
                }
            }

        }

    }

    public void undo() {

        if (!komande.isEmpty()) {
            if (oldObjects.isEmpty()) {
                for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                    oldObjects.put(entry.getKey(), entry.getValue());
                    if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                        ((Triangle) entry.getValue()).recolor(trics);
                    }
                }

            }

            String kom = komande.pop();
            if (kom.compareTo("add") == 0) {
                Vector<String> redoCom = commands.pop();
                int min = -1;
                int minPoint = -1;

                for (String command : redoCom) {
                    String array[] = command.split("\\s+");
                    GeometricObject gobj = geometricObjects.get(array[1]);
                    if (gobj != null && gobj.getTriangle() != null) {
                        gobj.getTriangle().removeSignificant(gobj.label());
                    }
                    if (gobj != null && gobj instanceof Triangle) {
                        String tid = ((Triangle) gobj).getNumber();
                        uniqueID.setRedoTrin(Integer.parseInt(tid));
                    }

                    if (gobj != null && gobj instanceof GeomPoint) {
                        int k = Integer.parseInt(((GeomPoint) gobj).getPointId());
                        if (k != -1 && (k < minPoint || minPoint == -1)) {
                            minPoint = k;
                        }
                    }
                    int i = Integer.parseInt(array[1]);
                    if (i < min || min == -1) {
                        min = i;
                    }
                }


                uniqueID.setRedoLast(min);
                if (minPoint != -1) {
                    uniqueID.setRedoPoint(minPoint);
                }
                redoCommands.push(redoCom);
                redo.push("add");
                // geometricObjects.clear();
            } else {
                String[] array = kom.split("\\s+");
                float dx = Float.parseFloat(array[2]);
                float dy = Float.parseFloat(array[3]);

                GeomPoint point = (GeomPoint) geometricObjects.get(array[1]);

                point.translate(point.X() + dx, point.Y() + dy, geometricObjects);

                redo.push("translate " + array[1] + " " + Float.toString(-dx) + " " + Float.toString(-dy));

                if (array.length > 4) {
                    ((Triangle) geometricObjects.get(array[4])).recontruct(trics, array[5], array[6], array[7]);
                    for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                        if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                            ((Triangle) entry.getValue()).recolor(trics);
                        }
                    }
                }
            }

            geometricObjects.clear();
            geometricConstructor.reconstructNew(commands, geometricObjects, oldObjects);
            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                    ((Triangle) entry.getValue()).recolor(trics);
                }
            }

        }

    }

    public String save() {
        Iterator<Vector<String>> stackedCommands = commands.iterator();
        StringBuilder stringBuilder = new StringBuilder();
        while (stackedCommands.hasNext()) {
            Vector<String> scommands = stackedCommands.next();
            for (String command : scommands) {
                String array[] = command.split("\\s+");
                GeometricObject gobj = geometricObjects.get(array[1]);
                if (gobj == null) {
                    continue;
                }

                String triangle = "";
                if (array[0].compareTo("point") == 0) {
                    command = "point " + gobj.getId() + " " + ((GeomPoint) gobj).X() + " " + ((GeomPoint) gobj).Y();
                } else {
                    if (gobj.getTriangle() != null) {
                        triangle += "addTriangle " + gobj.getId() + " " + gobj.getTriangle().getId();
                    }
                }
                stringBuilder.append(command).append("\n");
                stringBuilder.append("addLabel ").append(gobj.getId()).append(" ").append(gobj.label()).append("\n");
                stringBuilder.append(triangle).append("\n");
            }
        }


        return stringBuilder.toString();
    }


    public void load(Vector<String> file) {
        oldObjects.clear();
        komande.clear();
        redo.clear();
        redoCommands.clear();
        uniqueID.reset();
        geometricObjects.clear();
        int i = geometricConstructor.openNew(file, geometricObjects, uniqueID, constants);
        komande.add("add");
        commands.add(file);
        uniqueID.setRedoLast(i + 1);
        uniqueID.setRedoPoint(Integer.parseInt(uniqueID.getPointNum()) - 1);
        uniqueID.setRedoTrin(Integer.parseInt(uniqueID.getTrinagleNum()) - 1);
        uniqueID.restore();
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                ((Triangle) entry.getValue()).recolor(trics);
            }
        }
    }


    public void center(int width, int height) {
        float minX = 0, minY = 0, maxX = 0, maxY = 0;
        boolean id = true;

        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof GeomPoint) {
                float x = ((GeomPoint) entry.getValue()).X();
                float y = ((GeomPoint) entry.getValue()).Y();

                if (id) {
                    minX = x;
                    minY = y;
                    maxX = x;
                    maxY = y;
                    id = false;

                } else {
                    if (x < minX) {
                        minX = x;
                    }

                    if (x > maxX) {
                        maxX = x;
                    }

                    if (y < minY) {
                        minY = y;
                    }

                    if (y > maxY) {
                        maxY = y;
                    }
                }
            }
        }

        if (id) {
            return;
        }

        float dx = (maxX + minX) / 2;
        float w = width / 2.0f;
        float dy = (maxY + minY) / 2;
        float h = height / 2.0f;

        float fx = (maxX - minX) / (0.95f * w);
        float fy = (maxY - minY) / (0.95f * h);

        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null) {
                entry.getValue().translateWhole(dx - w, dy - h);
                float min = fx > fy ? fx : fy;
                if (min > 0.01f) {
                    entry.getValue().scale(1.f / min, w, h);
                }
            }
        }

        geometricConstructor.reconstruct(commands, geometricObjects);

    }

    public void setSenisitivity(float factor){
        constants.setFactor(factor);
    }

    public void underCurrsor(float touchX, float touchY) {
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null && entry.getValue().isUnderCursor(touchX, touchY)) {
                current = entry.getValue();
                prevX = ((GeomPoint)current).X();
                prevY = ((GeomPoint)current).Y();
                break;
            }
        }
    }

    public void translate(float touchX, float touchY){
        if (current != null) {
            current.translate(touchX, touchY, geometricObjects);
            geometricConstructor.reconstruct(commands, geometricObjects);
            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                    ((Triangle) entry.getValue()).recolor(trics);
                }
            }
        }
    }

    public void recognize( Vector<PointF> points){
        current = recognizer.recognizeCurrent(points, geometricObjects, discreteCurvature);
    }

    public void translateFinish(float touchX, float touchY){
        if (current != null) {
            String komanda = "";
            current.translate(touchX, touchY, geometricObjects);
            komanda += "translate " + current.getId()
                    + " " + Float.toString(prevX - touchX) + " " + Float.toString(prevY - touchY);

            komande.push(komanda);

            redoCommands.clear();
            oldObjects.clear();
            redo.clear();
            geometricConstructor.reconstruct(commands, geometricObjects);
            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                    ((Triangle) entry.getValue()).recolor(trics);
                }
            }
        }
        current = null;

    }

    public void drawingFinished(Vector<PointF> points){
        if (!redo.empty())
            uniqueID.restore();

        redoCommands.clear();
        oldObjects.clear();
        redo.clear();

        if (recognizer.recognize(points, geometricObjects, commands, discreteCurvature)) {
            komande.push("add");
        }
        current = null;

    }

    public void select(float x, float y) {
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof GeomPoint && ((GeomPoint) entry.getValue()).underCursor(x, y)) {
                ((GeomPoint) entry.getValue()).changeType(trics);
            }
        }
    }


    public void update(float scaleFactor, int w, int h){
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().scale(scaleFactor, w, h);
        }
        geometricConstructor.reconstruct(commands, geometricObjects);
    }

}
