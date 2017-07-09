package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by milica on 9.6.17..
 */

public class PerpBisector extends Line implements SignificantObject {
    private GeomPoint A, B, C;
    private boolean visible = false;

    PerpBisector(GeomPoint A, GeomPoint B, GeomPoint C){
        super(null, null);
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
        GeomPoint sB = new GeomPoint((A.X()+C.X())/ 2, (A.Y() + C.Y()) / 2);
        Line s = GeometricConstructions.w10(sB, new Line(A, C));
        this.setBegin(s.getBegin());
        this.setEnd(s.getEnd());
    }
}
