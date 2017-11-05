package geometry.calculations.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Vector;


public abstract class GeometricObject {
    double EPISLON = 10; //TODO nastelovati konstantu
    double density;

    public abstract void draw(Canvas canvas, Paint paint, boolean finished, boolean choose, PointInformations pointInformations);

    public abstract boolean choose(float x, float y, HashMap<String, Vector<String>> trics);

    public abstract boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects);


    public abstract boolean isUnderCursor(float x, float y);

    public abstract void translate(float x, float y);

    abstract public void scale(float scaleFactor);

    abstract public String getId();

    abstract public void setId(String id);

    public void setDensity(double density) {
        this.density = density;
    }


}
