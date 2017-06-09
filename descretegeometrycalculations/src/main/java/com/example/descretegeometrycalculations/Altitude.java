package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Altitude extends Line implements SignificantObject {
    private GeomPoint A, B, C;
    private boolean visible = false;

    Altitude(GeomPoint A, GeomPoint B, GeomPoint C){
        super(null, null);
        this.A = A;
        this.B = B;
        this.C = C;

        construct();
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        if(visible)
            super.draw(canvas, paint, finished);
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
        Line h = GeometricConstructions.w10(B, new Line(A, C));

        this.setBegin(h.getBegin());
        this.setEnd(h.getEnd());
    }
}
