package geometry.calculations.descretegeometrycalculations;

import android.graphics.PointF;
import android.util.Log;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;

import java.util.Vector;

public class DiscreteCurvature {


    public DiscreteCurvature() {
    }

    private static boolean checkCircle(double x0, double y0, double r, Vector<PointF> points) {
        if (r < 0)
            return false;

        for (PointF p : points) {
            double d = ((p.x - x0) * (p.x - x0) + (p.y - y0) * (p.y - y0)) / r;
            if (d > 1.5 || d < 0.75 || r > 300000) {
                return false;
            }
        }

        return true;
    }

    /*
    * Checks if points representing circle
    * using least square method
    *
    *
    * */
    private static GeometricObject circle(Vector<PointF> points) {

        int n = points.size();
        Mat A = Mat.ones(n, 3, CvType.CV_64FC1);
        Mat b = Mat.ones(n, 1, CvType.CV_64FC1);

        for (int i = 0; i < n; i++) {
            PointF p = points.elementAt(i);
            A.put(i, 0, 2 * p.x);
            A.put(i, 1, 2 * p.y);

            b.put(i, 0, p.x * p.x + p.y * p.y);
        }


        Mat x = new Mat();
        Mat A1 = new Mat(3, 3, CvType.CV_64FC1);
        Mat b1 = new Mat(3, 1, CvType.CV_64FC1);

        Core.gemm(A.t(), A, 1, new Mat(), 0, A1);
        Core.gemm(A.t(), b, 1, new Mat(), 0, b1);

        Core.solve(A1, b1, x, Core.DECOMP_CHOLESKY);

        double x0 = x.get(0, 0)[0];
        double y0 = x.get(1, 0)[0];
        double c = x.get(2, 0)[0];

        double r = c + x0 * x0 + y0 * y0;

        boolean isCircle = checkCircle(x0, y0, r, points);

        if (isCircle) {
            return new Circle(x0, y0, Math.sqrt(r));
        } else {
            return null;
        }
    }

    private static Vector<Point> proredi(Vector<PointF> points) {
        Vector<Point> newPoints = new Vector<>();

        Vector<Point> oldPoints = new Vector<>();

        boolean ind = true;

        for (PointF point : points) {
            Point P = new Point(point.x, point.y);

            oldPoints.add(P);
            if (ind) {
                newPoints.add(P);
                ind = false;
            } else {
                Point diff = new Point(P.x - newPoints.lastElement().x, P.y - newPoints.lastElement().y);
                double normdiff = Math.sqrt(diff.dot(diff));
                if (normdiff > 40) {
                    newPoints.add(P);
                }

            }

        }

        if (newPoints.size() < 4)
            return oldPoints;

        return newPoints;
    }

    // @Nullable
    public static GeometricObject getGeometricObject(Vector<PointF> points, double denisty) {
        Log.d("density", Double.toString(denisty));
        int n = points.size();

        if (n <= 3)
            return null;

        if (n < 10) {
            GeometricObject point = new GeomPoint(points.firstElement().x, points.firstElement().y);
            point.setDensity(denisty);
            return point;
        }

        Vector<Point> newPoints = proredi(points);

        n = newPoints.size();

        GeometricObject obj = circle(points);

        if (obj != null) {
            obj.setDensity(denisty);
            return obj;
        }

        int lessThenPI = 0;
        boolean isLine = true;

        Vector<GeomPoint> breakPoints = new Vector<>();

        breakPoints.add(new GeomPoint((float) newPoints.firstElement().x, (float) newPoints.firstElement().y));

        for (int i = 1; i < n - 1; i++) {


            Point begin = newPoints.get(i - 1);
            Point end = newPoints.get(i + 1);

            Point Q = newPoints.get(i);


            Point P = new Point(Q.x - begin.x, Q.y - begin.y);
            Point R = new Point(Q.x - end.x, Q.y - end.y);

            double dprod = P.dot(R);
            double normp = Math.sqrt(P.dot(P));
            double normr = Math.sqrt(R.dot(R));

            double angle = Math.acos(dprod / (normp * normr));

            //TODO nastelovati ovaj ugao
            if (angle < 0.8 * Math.PI && angle > 0) {
                lessThenPI++;

                if (lessThenPI > 2) {
                    isLine = false;
                    break;
                }
            } else {
                if (lessThenPI > 0) {
                    breakPoints.add(new GeomPoint((float) begin.x, (float) begin.y));
                    lessThenPI = 0;
                }

            }
        }

        breakPoints.add(new GeomPoint((float) newPoints.lastElement().x, (float) newPoints.lastElement().y));

        if (isLine) {
            if (breakPoints.size() > 2) {
                if (breakPoints.size() == 3) {
                    GeometricObject triangle = new Triangle(breakPoints);
                    triangle.setDensity(denisty);
                    return triangle;
                }
                GeometricObject polygon = new Polygon(breakPoints);
                polygon.setDensity(denisty);
                return polygon;
            } else if (breakPoints.size() == 2) {
                GeometricObject line = new Line(breakPoints.firstElement(), breakPoints.lastElement());
                line.setDensity(denisty);
                return line;
            }
        }

        return null;

    }

}
