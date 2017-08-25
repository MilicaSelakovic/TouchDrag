package geometry.calculations.descretegeometrycalculations;


public class UniqueID {
    private int current;
    private int triangleNum;


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

}
