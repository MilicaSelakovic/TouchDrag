package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * sredisnja tacka duzi A B
 */

public class Midpoint extends GeomPoint implements SignificantObject {

    private GeomPoint A, B, C;
    private boolean visible = false;


    Midpoint(GeomPoint A, GeomPoint B, GeomPoint C) {
        super(0, 0);
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
    public void construct(){
        float x = (A.X() + C.X()) / 2;
        float y = (A.Y() + C.Y()) / 2;

        setX(x);

        setY(y);
    }
}
