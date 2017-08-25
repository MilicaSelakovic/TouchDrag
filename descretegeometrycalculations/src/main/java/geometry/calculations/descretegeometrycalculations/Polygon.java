package geometry.calculations.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.HashMap;
import java.util.Vector;


public class Polygon implements GeometricObject {
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
        Path path = new Path();
        path.moveTo(points.firstElement().X(), points.firstElement().Y());

        for (GeomPoint p : points) {
            path.lineTo(p.X(), p.Y());
        }
        path.lineTo(points.firstElement().X(), points.firstElement().Y());
        canvas.drawPath(path, paint);
    }

    @Override
    public String toString() {
        return "Poligon " + Integer.toString(points.size());
    }


    public boolean choose(float x, float y, HashMap<String, Vector<String>> trics) {

        return false;

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


    // veze sa segmentom pojedinacnim i to samo sa Linijama i tackama
    // mogu tacke preskeka da se generisu ?

    // trougao - krug
    // upisan, opisan

    // trougao - prava   done
    // simentrala ugla done
    // simetrala stranice
    // tezisna linija
    // visina


    // trougao tacka
    // teme
    // ortocentar
    // teziste
    // centri opisanog, upisanog kruga
    // sredine stranica
    //

}
