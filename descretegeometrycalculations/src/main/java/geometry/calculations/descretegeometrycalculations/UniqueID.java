package geometry.calculations.descretegeometrycalculations;


import java.util.Vector;

public class UniqueID {
    private int current;
    private int triangleNum;
    private int pointNum;

    private int redoLast;
    private int redoTrin;
    private int redoPoint;


    public UniqueID() {
        current = -1;
        triangleNum = -1;
        pointNum = -1;
    }


    public String getID() {
        current++;
        return Integer.toString(current);
    }

    public String getTrinagleNum() {
        triangleNum++;
        return Integer.toString(triangleNum);
    }

    public String getPointNum() {
        pointNum++;
        return Integer.toString(pointNum);
    }

    public void setRedoLast(int id) {
        redoLast = id;
    }

    public void setRedoPoint(int id) {
        redoPoint = id;
    }


    public void setRedoTrin(int id) {
        redoTrin = id;
    }


    public void restore() {
        current = redoLast - 1;
        triangleNum = redoTrin - 1;
        pointNum = redoPoint - 1;
    }

    public void reset() {
        current = -1;
        triangleNum = -1;
        pointNum = -1;
    }
}
