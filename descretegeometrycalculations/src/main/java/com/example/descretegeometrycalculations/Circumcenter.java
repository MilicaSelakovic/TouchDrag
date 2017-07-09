package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

public class Circumcenter extends GeomPoint implements SignificantObject {
    private GeomPoint A, B, C;
    private boolean visible = false;

    Circumcenter(GeomPoint A, GeomPoint B, GeomPoint C){
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
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void construct() {
        Line sBC = GeometricConstructions.w10(new GeomPoint((B.X() + C.X())/ 2, (B.Y() + C.Y())/ 2), new Line(B, C));
        Line sAC = GeometricConstructions.w10(new GeomPoint((A.X() + C.X())/ 2, (A.Y() + C.Y())/ 2), new Line(A, C));

        GeomPoint O = GeometricConstructions.w03(sBC, sAC);

        setX(O.X());
        setY(O.Y());
    }
}
