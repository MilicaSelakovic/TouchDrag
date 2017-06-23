package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;


public class Incenter extends GeomPoint implements SignificantObject {

    private GeomPoint A, B, C;
    private boolean visible;

    Incenter(GeomPoint A, GeomPoint B, GeomPoint C){
        super(0,0);

        this.A = A;
        this.B = B;
        this.C = C;

        construct();
    }


    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished) {
        if (visible)
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
        Line sb = GeometricConstructions.bisectorAngle(A, B, C);
        Line sc = GeometricConstructions.bisectorAngle(B, C, A);

        GeomPoint I = GeometricConstructions.w03(sb, sc);

        setX(I.X());
        setY(I.Y());
    }
}
