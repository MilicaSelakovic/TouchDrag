package geometry.calculations.descretegeometrycalculations;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;

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


    private Triangle triangle;
    private String label;
    private String labelNumber = "";
    private String pointNum = "";

    private String id;

    GeomPoint(float x, float y) {
        constants = new Constants(1);
        this.x = x;
        this.y = y;
        this.move = true;
        this.type = Type.OTHER;

        circlePaint = new Paint();

        circlePaint.setAntiAlias(true);
        circlePaint.setStyle(Paint.Style.FILL);
        triangle = null;
        label = "";

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

        triangle = null;
        label = "";

        id = "";
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
        if (label.compareTo("") == 0) {
            label = "P";
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


    public void setType(Type type) {
        this.type = type;
    }

    public void setLabel(String l) {
        label = l;
    }

    public void setLabelNum(String l) {
        labelNumber = l;
    }

    public void setTriangle(Triangle triangle) {
        this.triangle = triangle;
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

        canvas.drawCircle(x, y, pointInformations.getPointSize(), circlePaint);
        circlePaint.setTextSize(pointInformations.getTextSize());
        if (pointInformations.isLabel()) {
            canvas.drawText(label, x + 20f, y + 20f, circlePaint);
            if (labelNumber.compareTo("") == 0 || Integer.parseInt(labelNumber) != 0) {
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
        Log.d("lala", "lala");
        return move && Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2)) < constants.getDistance_point();
    }

    public boolean underCursor(float x, float y) {
        Log.d("lala", "lala");
        return Math.sqrt(Math.pow((this.x - x), 2) + Math.pow((this.y - y), 2)) < constants.getDistance_point();
    }


    public void translate(float x, float y) {
        this.x = x;
        this.y = y;

        if (triangle != null) {
            triangle.translate(x, y);
        }
    }

    @Override
    public void scale(float scaleFactor) {
        x *= scaleFactor;
        y *= scaleFactor;
    }


    public boolean choose(float x, float y, HashMap<String, Vector<String>> trics) {
        return false;
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

                        commands.add("w02 " + object.getId() + " " + getId() + " " + entry.getValue().getId());
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

                            c.setCenter(k.getCenter());
                            c.setRadius(k.getRadius());

                            commands.add("circleAroundTriangle " + object.getId() + " " + this.getId() + " " + P.getId()
                                    + " " + entry.getValue().getId());

                            return true;
                        }
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

    // TODO Gde se koristi ovaj equal
    boolean equal(GeomPoint Y) {
        return Math.abs(x - Y.X()) < constants.getDistance_point() && Math.abs(y - Y.Y()) < constants.getDistance_point();
    }

    double distance(GeomPoint X) {
        return Math.sqrt((x - X.X()) * (x - X.X()) + (y - X.Y()) * (y - X.Y()));
    }

    public void changeType(HashMap<String, Vector<String>> trics) {

        switch (type) {
            case TRIANGLE_FREE:
                triangle.fix(this, trics);
                break;
            case TRIANGLE_CANFREE:
                triangle.free(this, trics);
                break;
            default:
                break;
        }

    }


}
