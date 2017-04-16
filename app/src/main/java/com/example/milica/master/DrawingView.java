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

import com.example.descretegeometrycalculations.DescreteCurvature;
import com.example.descretegeometrycalculations.GeometricObject;

import java.util.Vector;

public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint, objectPaint;
    //initial color
    private int paintColor = Color.parseColor("#808080");
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private Vector<PointF> points;
    private Vector<GeometricObject> geometricObjects;
    private GeometricObject current;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
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

        objectPaint = new Paint();
        objectPaint.setColor(Color.BLACK);
        objectPaint.setAntiAlias(true);
        objectPaint.setStrokeWidth(5);
        objectPaint.setStyle(Paint.Style.STROKE);
        objectPaint.setStrokeJoin(Paint.Join.ROUND);
        objectPaint.setStrokeCap(Paint.Cap.ROUND);
        objectPaint.setPathEffect(new DashPathEffect(new float[]{10, 10}, 0));
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

        current = DescreteCurvature.getGeometricObject(points);
      if(current != null) {
//            Log.d("Objekat", current.toString());
           current.draw(canvas, objectPaint);
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
 //               Log.d("novo", "OPET");
                points.removeAllElements();
                points.add(touchPoint);
                break;
            case MotionEvent.ACTION_MOVE:
                points.add(touchPoint);
                drawPath.lineTo(touchX, touchY);

                break;
            case MotionEvent.ACTION_UP:
//                drawCanvas.restore();
                if(current != null) {
//                    Log.d("sta je", current.toString());
                    geometricObjects.add(current);
                    current = null;
                }
                for(GeometricObject object : geometricObjects){
                    object.draw(drawCanvas, objectPaint);
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
