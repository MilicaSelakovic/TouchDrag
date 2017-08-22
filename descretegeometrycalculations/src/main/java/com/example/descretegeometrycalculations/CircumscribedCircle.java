package com.example.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

public class CircumscribedCircle extends Circle implements SignificantObject {
    private GeomPoint A;
    private boolean visible = false;

    CircumscribedCircle(GeomPoint center, GeomPoint A){
        super(center, 0);
        this.A = A;
        construct();
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose, PointInformations pointInformations) {
        if(visible)
            super.draw(canvas, paint, finished, choose, pointInformations);
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
        this.setRadius(A.distance(getCenter()));
    }


}
