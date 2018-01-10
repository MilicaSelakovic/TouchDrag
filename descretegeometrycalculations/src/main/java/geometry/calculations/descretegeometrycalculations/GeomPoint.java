package geometry.calculations.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Paint;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


public class GeomPoint extends GeometricObject {

    private float x;
    private float y;
    private Paint circlePaint;

    private boolean move;


    public enum Type {
        TRIANGLE_FREE, TRIANGLE_CANFREE, TRIANGLE_CANNOTFREE, OTHER;
    }

    public Type getType() {
        return type;
    }

    private Type type;


    private String labelNumber = "";
    private String pointNum = "-1";

    private String id;

    GeomPoint(float x, float y) {
        this.x = x;
        this.y = y;
        this.move = true;
        this.type = Type.OTHER;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        id = "";


    }

    GeomPoint(float x, float y, boolean move) {
        this.x = x;
        this.y = y;
        this.move = move;
        this.type = Type.OTHER;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);

        id = "";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if (label().compareTo("") == 0) {
            setLabel("P");
        }
    }

    public void setPointId(String id) {
        pointNum = id;
        if (labelNumber.compareTo("") == 0)
            labelNumber += id;
    }

    public String getPointId() {
        return pointNum;
    }


    void setType(Type type) {
        this.type = type;
    }

    public void setLabelNum(String l) {
        labelNumber = l;
    }


    @Override
    public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose, PointInformations pointInformations) {
        if (move) {
            circlePaint.setColor(pointInformations.getMoveColor());

        } else {
            circlePaint.setColor(pointInformations.getFixedColor());
        }

        if (choose) {
            switch (type) {
                case TRIANGLE_CANNOTFREE:
                    circlePaint.setColor(pointInformations.getCannotChooseColor());
                    break;
                case OTHER:
                    circlePaint.setColor(pointInformations.getOtherColor());
                    break;
                case TRIANGLE_FREE:
                    circlePaint.setColor(pointInformations.getActiveColor());
                    break;
                case TRIANGLE_CANFREE:
                    circlePaint.setColor(pointInformations.getCanChooseColor());
                    break;
            }


        }

        if (isInfoObject()) {
            circlePaint.setColor(pointInformations.getInfoColor());
        }

        canvas.drawCircle(x, y, pointInformations.getPointSize(), circlePaint);
        circlePaint.setTextSize(pointInformations.getTextSize());

        if (!isInfoObject()) {
            circlePaint.setColor(pointInformations.getTextColor());
        }
        if (pointInformations.isLabel()) {
            canvas.drawText(label(), x + 20f, y + 20f, circlePaint);
            if (labelNumber.compareTo("") == 0 || Integer.parseInt(labelNumber) > 0) {
                circlePaint.setTextSize(pointInformations.getTextSize() / 2);
                canvas.drawText(labelNumber, x + pointInformations.getTextSize(), y + 20f, circlePaint);
            }
        }
    }

    @Override
    public String toString() {
        return "Tacka" + Float.toString(x) + ", " + Float.toString(y);
    }

    public boolean isUnderCursor(float x, float y) {
        return move && Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2)) < constants.getDistance_point();
    }

    public boolean underCursor(float x, float y) {
        return Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2)) < 2 * constants.getDistance_point();
    }


    public void translate(float x, float y, HashMap<String, GeometricObject> objects) {
        this.x = x;
        this.y = y;

        if (getTriangle() != null) {
            getTriangle().translate(x, y, );
        }
    }

    @Override
    public void translateWhole(float x, float y) {
        super.translateWhole(x, y);
        this.x -= x;
        this.y -= y;
    }

    @Override
    public void scale(float scaleFactor, float w, float h) {
        x *= scaleFactor;
        y *= scaleFactor;
        if (scaleFactor < 1) {
            x += (1 - scaleFactor) * w / 2;
            y += (1 - scaleFactor) * h / 2;
        } else {
            x -= (1 - 1 / scaleFactor) * w / 2;
            y -= (1 - 1 / scaleFactor) * h / 2;
        }
    }


    public boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects) {
        if (object instanceof Line) {
            Line l = (Line) object;
            if (l.contain(this)) {

                for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
                    if (entry.getValue() instanceof GeomPoint && entry.getValue() != this &&
                            l.contain((GeomPoint) entry.getValue())) {
                        l.setBegin(this);
                        l.setEnd((GeomPoint) entry.getValue());
                        if (commands != null) {
                            commands.add("w02 " + object.getId() + " " + getId() + " " + entry.getValue().getId());
                        }
                        return true;
                    }
                }

                if (l.getBegin().distance(this) < l.getEnd().distance(this)) {
                    l.setEnd(this);
                } else {
                    l.setBegin(this);
                }


            }

        }

        if (object instanceof GeomPoint) {
            if (object == this) {
                return true;
            }


        }

        if (object instanceof Circle) {
            Circle c = (Circle) object;
            if (c.contain(this)) {
                GeomPoint P = null;
                for (Map.Entry<String, GeometricObject> entry : objects.entrySet()) {
                    if (entry.getValue() instanceof GeomPoint && entry.getValue() != this &&
                            c.contain((GeomPoint) entry.getValue())) {

                        if (P == null) {
                            P = (GeomPoint) entry.getValue();
                        } else {
                            Circle k = GeometricConstructions.circleAroundTriangle(this, P, (GeomPoint) entry.getValue());

                            if (k != null) {

                                c.setCenter(k.getCenter());
                                c.setRadius(k.getRadius());
                                if (commands != null) {
                                    commands.add("circleAroundTriangle " + object.getId() + " " + this.getId() + " " + P.getId()
                                            + " " + entry.getValue().getId());
                                }

                                return true;
                            }
                        }
                    }
                }

                if (P != null) {

                } else {
                    // krug sa precnikom koji sadrzi ovu tacku
                    GeomPoint center = c.getCenter();
                    c.setRadius(distance(center));

                    if (commands != null) {
                        center.setId(uniqueID.getID());
                        center.setPointId(uniqueID.getPointNum());
                        objects.put(center.getId(), center);
                        commands.add("w06 " + c.getId() + " " + this.getId() + " " + center.getId());
                    }
                }
            }
        }
        return false;
    }

    public float X() {
        return x;
    }


    public float Y() {
        return y;
    }

    void setX(float x) {
        this.x = x;
    }

    void setY(float y) {
        this.y = y;
    }

    void setMove(boolean value) {
        move = value;
    }

    boolean equal(GeomPoint Y) {
        return Math.abs(x - Y.X()) < constants.getDistance_point() && Math.abs(y - Y.Y()) < constants.getDistance_point();
    }

    double distance(GeomPoint X) {
        return Math.sqrt((x - X.X()) * (x - X.X()) + (y - X.Y()) * (y - X.Y()));
    }

    public void changeType(HashMap<String, Vector<String>> trics) {

        switch (type) {
            case TRIANGLE_FREE:
                getTriangle().fix(this, trics);
                break;
            case TRIANGLE_CANFREE:
                getTriangle().free(this, trics);
                break;
            default:
                break;
        }

    }

    @Override
    public void setFree(boolean free) {
        super.setFree(free);
        this.type = Type.TRIANGLE_FREE;
    }
}
