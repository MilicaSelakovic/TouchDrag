package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;


public class Centroid extends GeomPoint implements SignificantObject {
    private GeomPoint A, B, C;
    private boolean visible = false;

    Centroid(GeomPoint A, GeomPoint B, GeomPoint C){
        super(0,0);
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
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void construct() {
        Line ta = new Line(A, new GeomPoint((B.X() + C.X())/ 2, (B.Y() + C.Y())/ 2));
        Line tb = new Line(B, new GeomPoint((A.X() + C.X())/ 2, (A.Y() + C.Y())/ 2));

        GeomPoint T = GeometricConstructions.w03(ta, tb);

        setX(T.X());
        setY(T.Y());
    }
}
