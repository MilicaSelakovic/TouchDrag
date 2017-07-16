package com.example.descretegeometrycalculations;


import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.Log;

import java.util.HashMap;
import java.util.Vector;


public class Line implements GeometricObject {


    private String id;
    private GeomPoint begin;
    private GeomPoint end;

    private GeomPoint vector; // normiran vektor pravca
    //private TreeSet<Line> parallel = new TreeSet<>();

    Line(GeomPoint x, GeomPoint y){
        id = "";
        begin = x ;
        end = y;
        if(x != null && y != null) {
            float vX = end.X() - begin.X();
            float vY = end.Y() - begin.Y();
            float norm = (float) Math.sqrt(vX * vX + vY * vY);
            vector = new GeomPoint(vX / norm, vY / norm);
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private float yCoord(float x, float y){

        if(Math.abs(end.X() - begin.X()) <= 10e-5){
            return y;
        }

        return (end.Y() - begin.Y()) / (end.X() - begin.X()) * (x - begin.X()) + begin.Y();
    }


   @Override
   public void draw(Canvas canvas, Paint paint, boolean finished, boolean choose) {
       float x1 = 0;
       float x2  = canvas.getWidth();

       if(Math.abs(end.X() - begin.X()) <= 10e-5){
           x2 = begin.X();
           x1 = x2;
       }
       int y = canvas.getHeight();
       canvas.drawLine( x1, yCoord(x1, 0), x2, yCoord(x2, y), paint);
    }

    @Override
    public String toString() {
        return "Linija";
    }

    public boolean isUnderCursor(float x, float y){
        return false;
    }
    public void translate(float x, float y){

    }


    public GeomPoint getVector() {
        return vector;
    }

    public GeomPoint getBegin() {
        return begin;
    }

    public void setBegin(GeomPoint begin) {
        this.begin = begin;
        updateVector();
    }

    private void updateVector() {
        if (begin != null && end != null) {
            float vX = end.X() - begin.X();
            float vY = end.Y() - begin.Y();
            float norm = (float) Math.sqrt(vX * vX + vY * vY);
            if (vector == null) {
                vector = new GeomPoint(0, 0);
            }
            vector.setX(vX / norm);
            vector.setY(vY / norm);
        }
    }

    public GeomPoint getEnd() {
        return end;
    }

    public void setEnd(GeomPoint end) {
        this.end = end;
        updateVector();
    }

    double distance(GeomPoint point){
        double n1 = end.Y() - begin.Y();
        double n2 = begin.X() - end.X();
        double n3 = -begin.Y()*n2 - begin.X()*n1;

        double d = Math.abs(n1*point.X() + n2*point.Y() + n3);
        double norm = Math.sqrt(n1*n1 + n2*n2);
        return d/norm;
    }

    float ACoef(){
        return end.Y() - begin.Y();
    }

    float BCoef(){
        return -end.X() + begin.X();
    }

    float CCoef(){
        return ACoef()*begin.X() + BCoef()*begin.Y();
    }

    boolean contain(GeomPoint point){
        double n1 = end.Y() - begin.Y();
        double n2 = begin.X() - end.X();
        double n3 = -begin.Y()*n2 - begin.X()*n1;

        double d = Math.abs(n1*point.X() + n2*point.Y() + n3)/Math.sqrt(n1*n1 + n2*n2);
        return  d < EPISLON;
    }

    public boolean choose(float x, float y, HashMap<String, Vector<String>> trics) {
        return false;
    }

    public boolean connection(GeometricObject object, Vector<String> commands, UniqueID uniqueID, HashMap<String, GeometricObject> objects) {
        if (object instanceof Line) {

            if (connectionLine((Line) object, commands, uniqueID, objects))
                return true;
        }

        if (object instanceof GeomPoint) {
            return contain((GeomPoint) object);
        }

        return false;
    }

    @Override
    public void setChoose() {

    }

    private boolean connectionLine(Line line, Vector<String> commands, UniqueID id, HashMap<String, GeometricObject> objects) {
        if (ConnectionCalculations.normalLine(this, line)) {
            line.getBegin().setId(id.getID());
            objects.put(line.getBegin().getId(), line.getBegin());
            commands.add("w10 " + line.getId() + " " + line.getBegin().getId() + " " + this.getId());
            Line l = GeometricConstructions.w10(line.getBegin(), this);
            line.setEnd(l.getEnd());
            return true;
        }

        if (ConnectionCalculations.parallelLine(this, line)) {

            Log.d("Linija", "tangenta");
            line.getBegin().setId(id.getID());
            objects.put(line.getBegin().getId(), line.getBegin());

            commands.add("w16 " + line.getId() + " " + line.getBegin().getId() + " " + this.getId());

            Line l = GeometricConstructions.w16(line.getBegin(), this);
            l.setEnd(l.getEnd());
            return true;

        }

        return false;
    }
}
