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
import java.util.Vector;

public class DrawingView extends View {

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
    private Vector<String> commands;
    private GeometricObject current;

    private Recognizer recognizer;
    private UniqueID uniqueID;
    private Constructor constructor;
    boolean actionDown;

    boolean moveMode;
    boolean chooseMode;

    private ScaleGestureDetector mScaleGestureDetector;
    private float mScaleFactor = 1.f;

    private HashMap<String, Vector<String>> trics;

    Triangle currentTriangle;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();

        uniqueID = new UniqueID();
        recognizer = new Recognizer(uniqueID);
        constructor = new Constructor();
        actionDown = false;
        moveMode  = false;
        chooseMode = false;
        points = new Vector<>();
        geometricObjects = new HashMap<>();
        commands = new Vector<>();
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
        if (!moveMode && !chooseMode) {
            if (actionDown) {
                canvas.drawPath(drawPath, drawPaint);
            }
            //current = DiscreteCurvature.getGeometricObject(points);
            if (current != null && actionDown) {
                current.draw(canvas, drawObject, false, false);
            }
        }

        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {

            entry.getValue().draw(canvas, objectPaint, true, chooseMode);
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

        if (mScaleGestureDetector.isInProgress()) {
            return true;
        }

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (moveMode) {
                    for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                        if (entry.getValue().isUnderCursor(touchX, touchY)) {
                            current = entry.getValue();

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
                } else if (chooseMode) {
                    if (currentTriangle == null) {
                        for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                            if (entry.getValue().choose(touchX, touchY, trics)) {
                                currentTriangle = (Triangle) entry.getValue();
                                break;
                            }
                        }
                    } else {
                        if (currentTriangle.changeFree(touchX, touchY, trics)) {
                            currentTriangle.setChoose();
                            currentTriangle = null;
                        }

                    }
                    invalidate();
                } else {
                    drawPath.moveTo(touchX, touchY);
                    points.removeAllElements();
                    points.add(touchPoint);
                    actionDown = true;
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:

                if (moveMode) {
                    if (current != null) {
                        current.translate(touchX, touchY);
                        if (currentTriangle != null)
                            currentTriangle.translate(touchX, touchY);
                        constructor.reconstruct(commands, geometricObjects);
                    }
                } else if (chooseMode) {
                    break;
                } else {
                    points.add(touchPoint);
                    drawPath.lineTo(touchX, touchY);
                    current = recognizer.recognizeCurrent(points);
                }
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                if (moveMode) {
                    if (current != null) {
                        current.translate(touchX, touchY);
                        if (currentTriangle != null)
                            currentTriangle.translate(touchX, touchY);
                        constructor.reconstruct(commands, geometricObjects);
                    }
                    current = null;
                    currentTriangle = null;
                } else if (chooseMode) {
                    break;
                } else {
                    actionDown = false;
                    recognizer.recognize(points, geometricObjects, commands);
                    drawPath.reset();
                    current = null;
                    //   Log.d("komande", commands.toString());
                }

                invalidate();
                break;
            default:
                return false;
        }


        return  true;
    }

    public void setMoving(boolean value){
        moveMode = value;
    }

    public void clearPanel(){
        geometricObjects.clear();
        commands.clear();
        invalidate();
    }


    public void setChoose(boolean value) {
        chooseMode = value;
        if (value) {
            for (Map.Entry<String, GeometricObject> entry : geometricObjects.entrySet()) {
                entry.getValue().setChoose();
            }
        } else {
            currentTriangle = null;
        }

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
}


