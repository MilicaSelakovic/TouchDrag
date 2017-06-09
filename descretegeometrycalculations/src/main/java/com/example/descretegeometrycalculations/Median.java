package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;


public class Median extends Line implements SignificantObject {
    private GeomPoint A, B, C;
    private boolean visible = false;

    Median(GeomPoint A, GeomPoint B, GeomPoint C){
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
        GeomPoint sB = new GeomPoint((A.X()+C.X())/ 2, (A.Y() + C.Y()) / 2);

        this.setBegin(B);
        this.setEnd(sB);
    }
}
