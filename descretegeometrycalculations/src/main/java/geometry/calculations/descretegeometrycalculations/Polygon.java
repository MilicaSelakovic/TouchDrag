package geometry.calculations.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.HashMap;
import java.util.Vector;


public class Polygon extends GeometricObject {
    private String id;

    public Vector<GeomPoint> getPoints() {
        return points;
    }

    public void setPoints(Vector<GeomPoint> points) {
        this.points = points;
    }

    private Vector<GeomPoint> points;
    private Path path;

    Polygon(Vector<GeomPoint> points) {
        this.points = new Vector<>(points);
        path = new Path();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose, PointInformations pointInformations) {
        path.reset();
        int n = points.size();
        for (int i = 0; i < n; i++) {
            GeomPoint P = points.elementAt(i % n);
            GeomPoint Q = points.elementAt((i + 1) % n);
            if (P == null || Q == null) {
                return;
            }
            drawSegment(canvas, P, Q);
        }
        canvas.drawPath(path, paint);
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

    public void translate(float x, float y, HashMap<String, GeometricObject> objects) {
    }


    @Override
    public void scale(float scaleFactor, float w, float h) {

    }

    @Override
    public void setConstants(Constants constants) {
        super.setConstants(constants);

        for (GeomPoint point : points) {
            point.setConstants(constants);
        }
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

    private void drawSegment(Canvas canvas, GeomPoint P, GeomPoint Q) {
        int h = canvas.getHeight();
        int w = canvas.getWidth();

        if (checkPoint(P, h, w) && checkPoint(Q, h, w)) {
            path.moveTo(P.X(), P.Y());
            path.lineTo(Q.X(), Q.Y());
            return;
        }

        if (checkPoint(P, h, w)) {
            // samo je P unutra
            GeomPoint R = intersection(P, Q, h, w);
            path.moveTo(P.X(), P.Y());
            path.lineTo(R.X(), R.Y());
            return;
        }

        if (checkPoint(Q, h, w)) {
            //samo je Q unutra;
            GeomPoint R = intersection(Q, P, h, w);
            path.moveTo(Q.X(), Q.Y());
            path.lineTo(R.X(), R.Y());
            return;
        }

        GeomPoint R = intersection(P, Q, h, w);
        GeomPoint S = intersection(Q, P, h, w);
        if (R == null || S == null) {
            return;
        }
        path.moveTo(S.X(), S.Y());
        path.lineTo(R.X(), R.Y());
    }

}
