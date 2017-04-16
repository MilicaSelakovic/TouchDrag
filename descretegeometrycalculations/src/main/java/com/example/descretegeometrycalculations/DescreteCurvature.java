package com.example.descretegeometrycalculations;

import java.util.Vector;


import android.graphics.PointF;
//import android.support.annotation.Nullable;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.CvType;



public class DescreteCurvature {


    public DescreteCurvature(){
    }


   /* public static double Curvature(Vector<Point> points){
        int len = points.size();

        if(len < 5){
            return -1;
        }
        if( len % 2 == 0){
            points.remove(0);
            len --;
        }

        Point P = points.elementAt((len-1)/2);
        points.remove((len-1)/2);

        Point result = new Point(0,0);
        double intensity = 0;
        for(Point Q : points){
            Point tmp = new Point(Q.x - P.x, Q.y - P.y);
            result = new Point(result.x + tmp.x, result.y + tmp.y);

            intensity += tmp.x*tmp.x + tmp.y*tmp.y;
        }

        return 2* Math.sqrt(result.x*result.x + result.y*result.y)/intensity;
    }
*/

/*    private static double Curvature(Vector<PointF> points, int index, int step){
        PointF P = points.get(index);
        //points.remove((len-1)/2);

        PointF result = new PointF(0,0);
        double intensity = 0;
        for(int i = index - step; i <= index + step; i++){
            if(i== index){
                continue;
            }
            PointF Q = points.get(i);
            PointF tmp = new PointF(Q.x - P.x, Q.y - P.y);
            result = new PointF(result.x + tmp.x, result.y + tmp.y);

            intensity += tmp.x*tmp.x + tmp.y*tmp.y;
        }



        return 2* Math.sqrt(result.x*result.x + result.y*result.y)/intensity;
    }

*/
    private static boolean checkCircle(double x0, double y0, double r, Vector<PointF> points){
      //  Log.d("poluprecnik", Double.toString(r));
        if( r < 0)
            return false;

        for (PointF p: points) {
            double d = ((p.x - x0)*(p.x - x0) + (p.y - y0)*(p.y - y0))/  r;
           // Log.d("odnos", Double.toString(d));
           // Log.d("r", Double.toString(r));
            if ( d > 1.5  || d < 0.75 || r > 300000) {
                return false;
            }
        }

        return true;
    }

    private static  GeometricObject  circle(Vector<PointF> points){

        int n = points.size();
        Mat A = Mat.ones(n, 3, CvType.CV_64FC1);
        Mat b = Mat.ones(n, 1, CvType.CV_64FC1);

        for(int i = 0; i < n ; i++){
            PointF p = points.elementAt(i);
            A.put(i, 0, 2*p.x);
            A.put(i, 1, 2*p.y);
            //A.put(i, 2, 1);
            b.put(i, 0, p.x*p.x + p.y*p.y);
        }


        Mat x = new Mat();
        Mat A1 = new Mat(3,3, CvType.CV_64FC1);
        Mat b1 = new Mat(3,1, CvType.CV_64FC1);

        Core.gemm(A.t(), A, 1, new Mat(), 0, A1);
        Core.gemm(A.t(), b, 1, new Mat(), 0, b1);

        Core.solve(A1, b1, x, Core.DECOMP_CHOLESKY);

        double x0 = x.get(0,0)[0];
        double y0 = x.get(1,0)[0];
        double c = x.get(2,0)[0];

        double r = c + x0*x0 + y0*y0;

        boolean isCrircle =  checkCircle(x0, y0, r, points);

        if(isCrircle){
            return new Circle(x0, y0, Math.sqrt(r));
        } else {
            return null;
        }
    }

    private static Vector<Point> proredi(Vector<PointF> points){
        Vector<Point> newPoints = new Vector<>();

        Vector<Point> oldPoints = new Vector<>();

        boolean ind = true;

        for(PointF point : points){
            Point P = new Point(point.x, point.y);

            oldPoints.add(P);
            if(ind){
                newPoints.add(P);
                ind = false;
            } else {
                Point diff = new Point(P.x - newPoints.lastElement().x, P.y  - newPoints.lastElement().y);
                double normdiff =  Math.sqrt(diff.dot(diff));
              //  Log.d("diff", Double.toString(normdiff));
                if(normdiff> 40){
                    newPoints.add(P);
                }

            }

        }

        if(newPoints.size() < 4)
            return oldPoints;

        return  newPoints;
    }

   // @Nullable
    public static GeometricObject getGeometricObject(Vector<PointF> points){
        int n = points.size();

        if( n <= 0)
            return null;

        if( n < 3){
            return new GeomPoint(points.firstElement());
        }

//        PointF begin = points.get(0);
//        PointF end = points.get(n-1);


//        Log.d("tacke", Integer.toString(n) + " " + points.toString());

        Vector<Point> newPoints = proredi(points);

        n = newPoints.size();

       //Log.d("Nove tacke", Integer.toString(n) + " " + newPoints.toString());

        GeometricObject obj = circle(points);

        if(obj != null){
            return  obj;
        }

        int lessThenPI = 0;
        boolean isLine = true;

        Vector<Point> breakPoints = new Vector<>();

        breakPoints.add(newPoints.firstElement());

        for (int i = 1; i <  n - 1; i++){


            Point begin = newPoints.get(i-1);
            Point end = newPoints.get(i+1);

            Point Q = newPoints.get(i);


            Point P = new Point(Q.x - begin.x, Q.y - begin.y);
            Point R = new Point(Q.x - end.x, Q.y - end.y);

            double dprod = P.dot(R);
            double normp = Math.sqrt(P.dot(P));
            double normr = Math.sqrt(R.dot(R));

            double angle = Math.acos(dprod/(normp*normr));
//            Log.d("ugao",  Double.toString(angle));
//
            if(angle < 0.90*Math.PI && angle > 0){
                lessThenPI ++;

                if(lessThenPI > 2) {
                    isLine = false;
                    break;
                }
            } else {
                if(lessThenPI > 0){
                    breakPoints.add(begin);
                    lessThenPI = 0;
                }

            }
        }

        breakPoints.add(newPoints.lastElement());

        if(isLine){
            if(breakPoints.size() > 2){
                return new Polygon(breakPoints);
            } else if (breakPoints.size() == 2){
                return new Line(breakPoints.firstElement(), breakPoints.lastElement());
            }
        }

        return null;

      }

}
