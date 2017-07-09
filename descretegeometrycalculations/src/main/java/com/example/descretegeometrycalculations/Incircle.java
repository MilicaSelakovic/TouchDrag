package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Incircle extends Circle implements SignificantObject {
    private GeomPoint A, B, C;
    private boolean visible = false;

    Incircle(GeomPoint center, GeomPoint A, GeomPoint B, GeomPoint C){
        super(center, 0);
        this.A = A;
        this.B = B;
        this.C = C;

        construct();
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose) {
        if(visible)
            super.draw(canvas, paint, finished, choose);
    }

    @Override
    public void setVisible(boolean value) {
        visible = value;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }


    @Override
    public void construct() {
        Line a = new Line(B, C);
        this.setRadius(a.distance(getCenter()));
    }
}
