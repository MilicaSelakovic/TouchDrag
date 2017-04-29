package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PointF;

/**
 * Created by milica on 27.11.16..
 */

public class GeomPoint implements GeometricObject{
    private float x;
    private float y;

    public GeomPoint(float x, float y){
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawPoint(x, y, paint);
    }

    @Override
    public String toString() {
        return "Tacka";
    }


    public void connection(GeometricObject object){

    }

    public float X(){
        return x;
    }


    public float Y(){
        return y;
    }
}
