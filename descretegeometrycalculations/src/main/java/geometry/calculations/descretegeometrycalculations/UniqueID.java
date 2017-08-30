package geometry.calculations.descretegeometrycalculations;


import java.util.Vector;

public class UniqueID {
    private int current;
    private int triangleNum;

    private int redoLast;
    private int redoTrin;


    public UniqueID() {
        current = 0;
        triangleNum = 0;
    }


    public String getID() {
        current++;
        return Integer.toString(current);
    }

    public String getTrinagleNum() {
        triangleNum++;
        return Integer.toString(triangleNum);
    }


    public void setRedoLast(int id) {
        redoLast = id;
    }


    public void setRedoTrin(int id) {
        redoTrin = id;
    }


    public void restore() {
        current = redoLast - 1;
        triangleNum = redoTrin - 1;
    }

    public void reset() {
        current = 0;
        triangleNum = 0;
    }
}
