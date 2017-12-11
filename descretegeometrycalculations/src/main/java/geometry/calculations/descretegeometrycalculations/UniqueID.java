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
        current = 0;
        triangleNum = 0;
        pointNum = 0;
    }


    public String getID() {
        current++;
        return Integer.toString(current - 1);
    }

    public String getTrinagleNum() {
        triangleNum++;
        return Integer.toString(triangleNum - 1);
    }

    public String getPointNum() {
        pointNum++;
        return Integer.toString(pointNum - 1);
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
        current = 0;
        triangleNum = 0;
        pointNum = 0;

        redoLast = 1;
        redoPoint = 1;
        redoTrin = 1;
    }
}
