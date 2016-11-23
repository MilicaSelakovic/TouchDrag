package com.example.milica.master;

import android.graphics.Point;
import android.util.Log;
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
import android.graphics.Point;
import java.util.Vector;
/**
 * Created by milica on 19.11.16..
 */

public class DrawingView extends View {

    //drawing path
    private Path drawPath;
    //drawing and canvas paint
    private Paint drawPaint, canvasPaint;
    //initial color
    private int paintColor = Color.parseColor("#808080");
    //canvas
    private Canvas drawCanvas;
    //canvas bitmap
    private Bitmap canvasBitmap;

    private Vector<Point> points;

    public DrawingView(Context context, AttributeSet attrs){
        super(context, attrs);
        setupDrawing();
        points = new Vector<>();
    }

    private void setupDrawing(){
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);
        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(20);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);
        canvasPaint = new Paint(Paint.DITHER_FLAG);
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

        double cur = DescreteCurvature.Curvature(points);

        Log.d("krivina", Double.toString(cur));
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();
        Point touchPoint = new Point((int)touchX,(int)touchY);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                drawPath.moveTo(touchX, touchY);
                //Log.d("novo", "OPET");
                points.removeAllElements();
                points.add(touchPoint);
                break;
            case MotionEvent.ACTION_MOVE:
                points.add(touchPoint);
                drawPath.lineTo(touchX, touchY);

                break;
            case MotionEvent.ACTION_UP:
                //drawCanvas.drawPath(drawPath, drawPaint); ovo sluzi da ostavi nacrtano a to nam ne treba
                drawPath.reset();
                break;
            default:
                return false;
        }

        invalidate();
        return  true;
    }

}
