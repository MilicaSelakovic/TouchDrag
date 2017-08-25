package geometry.calculations.descretegeometrycalculations;


import android.graphics.Color;

public class PointInformations {
    private int moveColor;
    private int fixedColor;
    private int activeColor;
    private int canChooseColor;
    private int cannotChooseColor;
    private int otherColor;

    private float pointSize;
    private boolean label;
    private float textSize;


    public PointInformations() {
        setToDefaults();
    }

    public void setToDefaults() {
        moveColor = Color.BLUE;
        fixedColor = Color.GRAY;
        activeColor = Color.rgb(27, 226, 98);
        canChooseColor = Color.YELLOW;
        cannotChooseColor = Color.RED;
        otherColor = Color.BLACK;

        pointSize = 20f;
        label = true;
        textSize = 50;
    }

    public int getMoveColor() {
        return moveColor;
    }

    public void setMoveColor(int moveColor) {
        this.moveColor = moveColor;
    }

    public int getFixedColor() {
        return fixedColor;
    }

    public void setFixedColor(int fixedColor) {
        this.fixedColor = fixedColor;
    }

    public int getActiveColor() {
        return activeColor;
    }

    public void setActiveColor(int activeColor) {
        this.activeColor = activeColor;
    }

    public int getCanChooseColor() {
        return canChooseColor;
    }

    public void setCanChooseColor(int canChooseColor) {
        this.canChooseColor = canChooseColor;
    }

    public int getCannotChooseColor() {
        return cannotChooseColor;
    }

    public void setCannotChooseColor(int cannotChooseColor) {
        this.cannotChooseColor = cannotChooseColor;
    }

    public int getOtherColor() {
        return otherColor;
    }

    public void setOtherColor(int otherColor) {
        this.otherColor = otherColor;
    }

    public boolean isLabel() {
        return label;
    }

    public void setLabel(boolean label) {
        this.label = label;
    }

    public float getPointSize() {
        return pointSize;
    }

    public float getTextSize() {
        return textSize;
    }

    public void setPointSize(float pointSize) {
        this.pointSize = pointSize;
    }

    public void setTextSize(float textSize) {
        this.textSize = textSize;
    }
}
