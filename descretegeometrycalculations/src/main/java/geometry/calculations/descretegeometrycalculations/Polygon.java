package geometry.calculations.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.Log;

import java.util.HashMap;
import java.util.Vector;


public class Polygon extends GeometricObject {
    String id;

    public Vector<GeomPoint> getPoints() {
        return points;
    }

    public void setPoints(Vector<GeomPoint> points) {
        this.points = points;
    }

    Vector<GeomPoint> points;

    Polygon(Vector<GeomPoint> points) {
        this.points = new Vector<>(points);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose, PointInformations pointInformations) {

        int n = points.size();
        for (int i = 0; i < n; i++) {
            GeomPoint P = points.elementAt(i % n);
            GeomPoint Q = points.elementAt((i + 1) % n);
            if (P == null || Q == null) {
                return;
            }
            drawSegment(canvas, paint, P, Q);
        }
    }

    @Override
    public String toString() {
        return "Poligon " + Integer.toString(points.size());
    }


    public boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects) {

        return false;

    }

    public boolean isUnderCursor(float x, float y) {
        return false;
    }

    public void translate(float x, float y) {
    }

    @Override
    public void scale(float scaleFactor) {

    }

    @Override
    public void setConstants(Constants constants) {
        super.setConstants(constants);

        for (GeomPoint point : points) {
            point.setConstants(constants);
        }
    }

    private int checkPoints(int h, int w) {

        int n = points.size();

        for (int i = 0; i < n; i++) {
            if (checkPoint(points.elementAt(i), h, w)) {
                return i;
            }
        }

        return -1;
    }

    private boolean checkPoint(GeomPoint P, int h, int w) {
        return P.X() >= 0 && P.X() <= w + 10 && P.Y() >= 0 && P.Y() <= h + 10;
    }


    private GeomPoint intersection(GeomPoint in, GeomPoint out, int h, int w) {
        int x = -1, y = -1;

        if (out.X() < 0) {
            x = 0;
        }

        if (out.X() > w) {
            x = w;
        }

        if (out.Y() < 0) {
            y = 0;
        }

        if (out.Y() > h) {
            y = h;
        }


        if (y >= 0) {
            GeomPoint P = new GeomPoint(0, y);
            GeomPoint Q = new GeomPoint(w, y);
            GeomPoint X = GeometricConstructions.w03(new Line(in, out), new Line(P, Q));


            if (X != null && checkPoint(X, h, w)) {
                X.setY(y);
                return X;
            }
        }

        if (x >= 0) {
            GeomPoint P = new GeomPoint(x, 0);
            GeomPoint Q = new GeomPoint(x, h);
            GeomPoint X = GeometricConstructions.w03(new Line(in, out), new Line(P, Q));

            if (X != null && checkPoint(X, h, w)) {
                X.setX(x);
                return X;
            }
        }

        return null;
    }

    private boolean onSameBorder(GeomPoint X, GeomPoint Y, int h, int w) {
        if (X.X() == Y.X() && (X.X() == 0 || X.X() == w)) {
            return true;
        }

        return (X.Y() == Y.Y() && (X.Y() == 0 || X.Y() == h));
    }

    private void drawSegment(Canvas canvas, Paint paint, GeomPoint P, GeomPoint Q) {
        int h = canvas.getHeight();
        int w = canvas.getWidth();

        if (checkPoint(P, h, w) && checkPoint(Q, h, w)) {
            canvas.drawLine(P.X(), P.Y(), Q.X(), Q.Y(), paint);
            return;
        }

        if (checkPoint(P, h, w)) {
            // samo je P unutra
            GeomPoint R = intersection(P, Q, h, w);
            canvas.drawLine(P.X(), P.Y(), R.X(), R.Y(), paint);
            return;
        }

        if (checkPoint(Q, h, w)) {
            //samo je Q unutra;
            GeomPoint R = intersection(Q, P, h, w);
            canvas.drawLine(Q.X(), Q.Y(), R.X(), R.Y(), paint);
            return;
        }

        GeomPoint R = intersection(P, Q, h, w);
        GeomPoint S = intersection(Q, P, h, w);
        canvas.drawLine(S.X(), S.Y(), R.X(), R.Y(), paint);

        return;
    }

}
