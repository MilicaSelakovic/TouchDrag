package com.example.descretegeometrycalculations;

import java.util.Vector;
import android.graphics.Point;
import android.support.annotation.NonNull;

/**
 * Created by milica on 19.11.16..
 */

public class DescreteCurvature {

    private Vector<Point> points;


    public DescreteCurvature(Point beginPoint){
        points.add(beginPoint);
    }


    private void addPoint(Point point){
        points.add(point);
    }

    private Double Curvature(){
        int len = points.size();
        if( len % 2 == 0){
            points.remove(0);
            len --;
        }

        Point P = points.elementAt((len-1)/2 + 1);
        points.remove((len-1)/2 + 1);

        Point result = new Point(0,0);
        double intensity = 0;
        for(Point Q : points){
            Point tmp = new Point(Q.x - P.x, Q.y - P.y);
            result = new Point(result.x + tmp.x, result.y + tmp.y);

            intensity += tmp.x^2 + tmp.y^2;
        }

        return 2* Math.sqrt(result.x^2 + result.y^2)/intensity;
    }

}
