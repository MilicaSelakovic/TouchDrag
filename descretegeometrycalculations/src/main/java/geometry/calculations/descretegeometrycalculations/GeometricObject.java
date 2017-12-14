package geometry.calculations.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Vector;


public abstract class GeometricObject {
    protected Constants constants;
    protected boolean draw = true;
    protected String recognizedLabel = "";
    private boolean free = false;

    public abstract void draw(Canvas canvas, Paint paint, boolean finished, boolean choose, PointInformations pointInformations);

    public abstract boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects);

    public abstract boolean isUnderCursor(float x, float y);

    public abstract void translate(float x, float y);

    abstract public void scale(float scaleFactor);

    abstract public String getId();

    abstract public void setId(String id);

    public void setDraw(boolean draw) {
        this.draw = draw;
    }

    public String getLabel() {
        return recognizedLabel;
    }

    public void setConstants(Constants constants) {
        this.constants = constants;
    }

    public boolean isFree() {
        return free;
    }

    public void setFree(boolean free) {
        this.free = free;
    }

}
