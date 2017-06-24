package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by milica on 9.6.17..
 */

public class FootOfAltitude extends GeomPoint implements SignificantObject {

    private GeomPoint A, B, C;

    private boolean visible = false;

    FootOfAltitude(GeomPoint A, GeomPoint B, GeomPoint C){
        super(0, 0, false);
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
    public void construct() {

        Line h = GeometricConstructions.w10(B, new Line(A, C));

        GeomPoint H = GeometricConstructions.w03(h, new Line(A, C));

        setX(H.X());
        setY(H.Y());

    }

    @Override
    public void setVisible(boolean value) {
        visible = value;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

}
