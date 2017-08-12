package com.example.milica.master;

import android.graphics.DashPathEffect;
import android.graphics.PointF;
import android.util.Log;
import android.view.DragEvent;
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

import com.example.descretegeometrycalculations.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.Vector;

public class DrawingView extends View {

    public enum Mode {
        MODE_MOVE, MODE_USUAL, MODE_FREE, MODE_FIX;

    }

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint, objectPaint, drawObject;
    //initial color
    private int paintColor = Color.parseColor("#808080");

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

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
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
        Vector<String> konstrukcija = new Vector<>();
        konstrukcija.add("w01 Mc A A B 0.5");
        konstrukcija.add("w02 ha A H");
        konstrukcija.add("w02 hb B H");
        konstrukcija.add("w06 k Mc A");
        konstrukcija.add("w05 Ha ha k A");
        konstrukcija.add("w02 a Ha B");
        konstrukcija.add("w05 Hb hb k B");
        konstrukcija.add("w02 b Hb A");
        konstrukcija.add("w03 C a b");

        trics.put("A B H", konstrukcija);
        trics.put("A B C", new Vector<String>());

        mScaleGestureDetector = new ScaleGestureDetector(context, new ScaleListener());
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(10);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);

        drawObject = new Paint();
        drawObject.setColor(Color.BLUE);
        drawObject.setAntiAlias(true);
        drawObject.setStrokeWidth(3);
        drawObject.setStyle(Paint.Style.STROKE);
        drawObject.setStrokeJoin(Paint.Join.ROUND);
        drawObject.setStrokeCap(Paint.Cap.ROUND);
        drawObject.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));


        objectPaint = new Paint();
        objectPaint.setColor(Color.parseColor("#483D8B"));
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

        //canvas.scale(mScaleFactor, mScaleFactor);
        if (mode == Mode.MODE_USUAL) {
            if (actionDown) {
                canvas.drawPath(drawPath, drawPaint);
            }
            //current = DiscreteCurvature.getGeometricObject(points);
            if (current != null && actionDown) {
                current.draw(canvas, drawObject, false, false);
            }
        }

        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() != null)
                entry.getValue().draw(canvas, objectPaint, true, mode == Mode.MODE_FIX || mode == Mode.MODE_FREE);
        }


    }


    @Override
    public boolean onDragEvent(DragEvent event) {

        Log.d("Drag event", Float.toString(event.getX()) + " " + Float.toString(event.getY()));
        return super.onDragEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mScaleGestureDetector.onTouchEvent(event);

        float touchX = event.getX();
        float touchY = event.getY();
        PointF touchPoint = new PointF(touchX,touchY);

        if (mode == Mode.MODE_MOVE && mScaleGestureDetector.isInProgress()) {
            return true;
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
                    case MODE_FIX:
                        fix(touchX, touchY);
                        invalidate();
                        break;
                    case MODE_MOVE:
                        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                            if (entry.getValue().isUnderCursor(touchX, touchY)) {
                                current = entry.getValue();
                                prevX = ((GeomPoint) current).X();
                                prevY = ((GeomPoint) current).Y();

                                for (Map.Entry<String, GeometricObject> entry1 : geometricObjects.entrySet()) {
                                    if (entry1.getValue() instanceof Triangle) {
                                        if (((Triangle) entry1.getValue()).belong((GeomPoint) current)) {
                                            currentTriangle = (Triangle) entry1.getValue();
                                            break;
                                        }
                                    }
                                }

                                break;
                            }
                        }
                        break;
                    case MODE_FREE:
                        free(touchX, touchY);
                        invalidate();
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
                            if (currentTriangle != null)
                                currentTriangle.translate(touchX, touchY);
                            constructor.reconstruct(commands, geometricObjects);
                        }
                        invalidate();
                        break;
                    case MODE_USUAL:
                        points.add(touchPoint);
                        drawPath.lineTo(touchX, touchY);
                        current = recognizer.recognizeCurrent(points);
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
                            if (currentTriangle != null) {
                                currentTriangle.addCommand(komanda);
                                currentTriangle.translate(touchX, touchY);
                            }

                            komande.push(komanda);

                            redoCommands.clear();
                            oldObjects.clear();
                            redo.clear();
                            constructor.reconstruct(commands, geometricObjects);
                        }
                        current = null;
                        currentTriangle = null;
                        invalidate();
                        break;
                    case MODE_USUAL:
                        actionDown = false;
                        if (recognizer.recognize(points, geometricObjects, commands)) {
                            komande.push("add");
                            redoCommands.clear();
                            oldObjects.clear();
                            redo.clear();
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

        return  true;
    }

    public void setMoving(boolean value){
        if (value) {
            mode = Mode.MODE_MOVE;
        } else {
            mode = Mode.MODE_USUAL;
        }
    }

    public void clearPanel(){
        geometricObjects.clear();
        commands.clear();
        oldObjects.clear();
        komande.clear();
        redo.clear();
        redoCommands.clear();

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
            invalidate();
            return true;
        }
    }

    private void scaleObjects() {
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            entry.getValue().scale(mScaleFactor);
        }
    }


    public void redo() {
        if (!redo.isEmpty()) {
            String kom = redo.pop();
            if (kom.compareTo("add") == 0) {
                commands.push(redoCommands.pop());
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

            invalidate();
        }

    }

    public void undo() {

        if (!komande.isEmpty()) {
            if (oldObjects.isEmpty()) {
                for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                    oldObjects.put(entry.getKey(), entry.getValue());
                    if (entry.getValue() instanceof Triangle) {
                        ((Triangle) entry.getValue()).recolor(trics);
                    }
                }

            }

            String kom = komande.pop();
            if (kom.compareTo("add") == 0) {
                redoCommands.push(commands.pop());
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
                }
            }

            geometricObjects.clear();
            constructor.reconstructNew(commands, geometricObjects, oldObjects);

            invalidate();
        }

    }


    private void fix(float x, float y) {
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() instanceof GeomPoint && ((GeomPoint) entry.getValue()).underCursor(x, y)) {
                ((GeomPoint) entry.getValue()).setFixed(trics);
            }
        }
    }


    private void free(float x, float y) {
        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
            if (entry.getValue() instanceof GeomPoint && ((GeomPoint) entry.getValue()).underCursor(x, y)) {
                ((GeomPoint) entry.getValue()).setFree(trics);
            }
        }
    }
}


