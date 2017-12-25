package touch.drag.milica.master;

import android.graphics.DashPathEffect;
import android.graphics.PointF;
import android.util.Log;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.graphics.Color;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.opencv.core.Point;

import java.util.HashMap;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

import geometry.calculations.descretegeometrycalculations.*;

public class DrawingView extends View {


    public enum Mode {
        MODE_MOVE, MODE_USUAL, MODE_SELECT;

    }

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint, objectPaint, drawObject;

    //canvas bitmap
    private Bitmap canvasBitmap;

    private Vector<PointF> points;
    private HashMap<String, GeometricObject> geometricObjects;
    private HashMap<String, GeometricObject> oldObjects;

    private Stack<Vector<String>> commands;
    private Stack<String> komande; // add ili translate
    private Stack<String> redo;
    private Stack<Vector<String>> redoCommands;
    private GeometricObject current;


    private float prevX, prevY;

    private Recognizer recognizer;
    private UniqueID uniqueID;
    private Constructor constructor;
    boolean actionDown;


    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.f;

    private HashMap<String, Vector<String>> trics;

    Triangle currentTriangle;

    private Mode mode;
    private PointInformations pointInformations;
    private DiscreteCurvature discreteCurvature;
    private Constants constants;

    private TextView textView;

    public DrawingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        pointInformations = new PointInformations();
        setupDrawing();

        uniqueID = new UniqueID();
        recognizer = new Recognizer(uniqueID);
        constructor = new Constructor();
        actionDown = false;
        mode = Mode.MODE_USUAL;
        points = new Vector<>();
        geometricObjects = new HashMap<>();
        oldObjects = new HashMap<>();

        commands = new Stack<>();
        redoCommands = new Stack<>();
        komande = new Stack<>();
        redo = new Stack<>();

        currentTriangle = null;

        trics = new HashMap<>();

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());

        pointInformations = new PointInformations();
        discreteCurvature = null;
        constants = null;
        textView = null;
    }

    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(pointInformations.getDrawPathColor());
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        drawObject = new Paint();
        drawObject.setColor(pointInformations.getTempObjectColor());
        drawObject.setAntiAlias(true);
        drawObject.setStrokeWidth(3);
        drawObject.setStyle(Paint.Style.STROKE);
        drawObject.setStrokeJoin(Paint.Join.ROUND);
        drawObject.setStrokeCap(Paint.Cap.ROUND);
        drawObject.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));


        objectPaint = new Paint();
        objectPaint.setColor(pointInformations.getPaintColor());
        objectPaint.setAntiAlias(true);
        objectPaint.setStrokeWidth(4);
        objectPaint.setStyle(Paint.Style.STROKE);
        objectPaint.setStrokeJoin(Paint.Join.ROUND);
        objectPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);

        if (mode == Mode.MODE_USUAL) {
            if (actionDown) {
                canvas.drawPath(drawPath, drawPaint);
            }

            if (current != null && actionDown) {
                current.draw(canvas, drawObject, false, false, pointInformations);
                if (textView != null) {
                    textView.setText(current.getLabel());
                }
            } else {
                if (textView != null) {
                    textView.setText("");
                }
            }
        }

        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().draw(canvas, objectPaint, true, mode == Mode.MODE_SELECT, pointInformations);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (mode == Mode.MODE_MOVE) {
            mScaleGestureDetector.onTouchEvent(event);
        }
        float touchX = event.getX();
        float touchY = event.getY();
        PointF touchPoint = new PointF(touchX, touchY);

        if (mode == Mode.MODE_MOVE) {
            if (mScaleGestureDetector.isInProgress()) {

                return true;
            }
        }


        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                switch (mode) {
                    case MODE_USUAL:
                        drawPath.moveTo(touchX, touchY);
                        points.removeAllElements();
                        points.add(touchPoint);
                        actionDown = true;
                        invalidate();
                        break;
                    case MODE_SELECT:
                        select(touchX, touchY);
                        invalidate();
                        break;
                    case MODE_MOVE:
                        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                            if (entry.getValue() != null && entry.getValue().isUnderCursor(touchX, touchY)) {
                                current = entry.getValue();
                                prevX = ((GeomPoint) current).X();
                                prevY = ((GeomPoint) current).Y();
                                break;
                            }
                        }
                        break;
                    default:
                        break;
                }
                break;
            case MotionEvent.ACTION_MOVE:

                switch (mode) {
                    case MODE_MOVE:
                        if (current != null) {
                            current.translate(touchX, touchY);
                            constructor.reconstruct(commands, geometricObjects);
                            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                                if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                                    ((Triangle) entry.getValue()).recolor(trics);
                                }
                            }
                        }
                        invalidate();
                        break;
                    case MODE_USUAL:
                        points.add(touchPoint);
                        drawPath.lineTo(touchX, touchY);
                        current = recognizer.recognizeCurrent(points, geometricObjects, discreteCurvature);
                        invalidate();
                        break;
                    default:
                        break;
                }
                break;

            case MotionEvent.ACTION_UP:
                switch (mode) {
                    case MODE_MOVE:
                        if (current != null) {
                            String komanda = "";
                            current.translate(touchX, touchY);
                            komanda += "translate " + current.getId()
                                    + " " + Float.toString(prevX - touchX) + " " + Float.toString(prevY - touchY);

                            komande.push(komanda);

                            redoCommands.clear();
                            oldObjects.clear();
                            redo.clear();
                            constructor.reconstruct(commands, geometricObjects);
                            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                                if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                                    ((Triangle) entry.getValue()).recolor(trics);
                                }
                            }
                        }
                        current = null;
                        invalidate();
                        break;
                    case MODE_USUAL:
                        actionDown = false;
                        if (!redo.empty())
                            uniqueID.restore();

                        redoCommands.clear();
                        oldObjects.clear();
                        redo.clear();

                        if (recognizer.recognize(points, geometricObjects, commands, discreteCurvature)) {
                            komande.push("add");
                        }
                        drawPath.reset();
                        current = null;
                        invalidate();
                        break;
                    default:
                        break;
                }
                break;
            default:
                return false;
        }

        return true;
    }


    public void clearPanel() {
        geometricObjects.clear();
        commands.clear();
        oldObjects.clear();
        komande.clear();
        redo.clear();
        redoCommands.clear();

        uniqueID.reset();
        invalidate();
    }


    public void setMode(Mode mode) {
        this.mode = mode;
        invalidate();
    }


    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            mScaleFactor = detector.getScaleFactor();

            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
            scaleObjects();
            constructor.reconstruct(commands, geometricObjects);

            invalidate();
            return true;
        }
    }

    private void scaleObjects() {
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().scale(mScaleFactor);
        }
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

                point.translate(point.X() + dx, point.Y() + dy);

                komande.push("translate " + array[1] + " " + Float.toString(-dx) + " " + Float.toString(-dy));

                if (array.length > 4) {
                    ((Triangle) geometricObjects.get(array[4])).recontruct(trics, array[5], array[6], array[7]);
                }
            }

            geometricObjects.clear();
            constructor.reconstructNew(commands, geometricObjects, oldObjects);
            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                    ((Triangle) entry.getValue()).recolor(trics);
                }
            }

            invalidate();
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

                point.translate(point.X() + dx, point.Y() + dy);

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
            constructor.reconstructNew(commands, geometricObjects, oldObjects);
            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                if (entry.getValue() != null && entry.getValue() instanceof Triangle) {
                    ((Triangle) entry.getValue()).recolor(trics);
                }
            }

            invalidate();
        }

    }


    private void select(float x, float y) {
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null && entry.getValue() instanceof GeomPoint && ((GeomPoint) entry.getValue()).underCursor(x, y)) {
                ((GeomPoint) entry.getValue()).changeType(trics);
            }
        }
    }

    public void setPointInformations(PointInformations pointInformations) {
        this.pointInformations = pointInformations;

        invalidate();
    }

    public void setDensity(double density, int densityDPI) {
        this.constants = new Constants(density, densityDPI);
        this.discreteCurvature = new DiscreteCurvature(constants);
    }

    public void setTextView(TextView textView) {
        this.textView = textView;
    }

    public void setTrics(HashMap<String, Vector<String>> trics) {
        this.trics = trics;
    }

    public void setSenisitivityFactor(float factor) {
        constants.setFactor(factor);
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
                    command = "point " + gobj.getId() + " " + ((GeomPoint) gobj).X() + " " + ((GeomPoint) gobj).X();
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

}


