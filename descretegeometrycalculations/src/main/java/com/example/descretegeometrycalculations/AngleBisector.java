package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

public class AngleBisector extends Line implements SignificantObject {

    private GeomPoint A, B, C;
    private boolean visible = false;

    AngleBisector(GeomPoint A, GeomPoint B, GeomPoint C){
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
        Line sB = GeometricConstructions.bisectorAngle(A, B, C);

        this.setBegin(sB.getBegin());
        this.setEnd(sB.getEnd());
    }
}
