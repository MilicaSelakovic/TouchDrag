package com.example.descretegeometrycalculations;

import java.util.Vector;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.util.Log;


/**
 * Created by milica on 19.11.16..
 */

public class DescreteCurvature {


    public DescreteCurvature(){
    }

    public static double Curvature(Vector<Point> points){
        int len = points.size();

        if(len < 5){
            return -1.;
        }

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

            intensity += tmp.x*tmp.x + tmp.y*tmp.y;
        }
    /*Debug*/
        Log.d("Intezitet", Double.toString(intensity));
        return 2* Math.sqrt(result.x*result.x + result.y*result.y)/intensity;
    }


}
