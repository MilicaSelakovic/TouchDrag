package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;


public class Orthocenter extends GeomPoint implements SignificantObject {

    private GeomPoint A, B, C;
    private boolean visible = false;

    Orthocenter(GeomPoint A, GeomPoint B, GeomPoint C){
        super(0, 0, false);
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
        visible = true;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void construct() {
        Line hb = GeometricConstructions.w10(B, new Line(A, C));
        Line ha = GeometricConstructions.w10(A, new Line(B, C));

        GeomPoint H = GeometricConstructions.w03(hb, ha);

        setX(H.X());
        setY(H.Y());
    }
}
