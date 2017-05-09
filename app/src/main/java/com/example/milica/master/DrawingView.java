package com.example.milica.master;

import android.graphics.DashPathEffect;
import android.graphics.PointF;
//import android.util.Log;
import android.view.View;
import android.content.Context;
import android.util.AttributeSet;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.view.MotionEvent;

import android.graphics.Color;

import com.example.descretegeometrycalculations.DiscreteCurvature;
import com.example.descretegeometrycalculations.GeometricObject;

import java.util.Vector;

public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint, objectPaint, drawObject;
    //initial color
    private int paintColor = Color.parseColor("#808080");
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private Vector<PointF> points;
    private Vector<GeometricObject> geometricObjects;
    private GeometricObject current;

    boolean actionDown;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
        actionDown = false;
        points = new Vector<>();
        geometricObjects = new Vector<>();
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
        drawCanvas = new Canvas(canvasBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);

        current = DiscreteCurvature.getGeometricObject(points);
      if(current != null && actionDown) {
           current.draw(canvas, drawObject, false);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        PointF touchPoint = new PointF((int)touchX,(int)touchY);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                points.removeAllElements();
                points.add(touchPoint);
                actionDown = true;
                break;
            case MotionEvent.ACTION_MOVE:
                points.add(touchPoint);
                drawPath.lineTo(touchX, touchY);

                break;
            case MotionEvent.ACTION_UP:
                actionDown = false;
                if(current != null) {
                    geometricObjects.add(current);
                    for(GeometricObject object : geometricObjects){
                        object.connection(current);
                    }
                    current = null;
                }
                for(GeometricObject object : geometricObjects){
                    object.draw(drawCanvas, objectPaint, true);
                }
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return  true;
    }

}
