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

    private int paintColor;

    private int drawPathColor;
    private int tempObjectColor;
    private int textColor;
    private int infoColor;

    private boolean showSignInfo;

    public PointInformations(int density) {
        setToDefaults(density);
    }

    public void setToDefaults(int density) {
        moveColor = Color.parseColor("#65be00");
        fixedColor = Color.parseColor("#e20000");
        activeColor = Color.parseColor("#65be00");
        canChooseColor = Color.parseColor("#f69100");
        cannotChooseColor = Color.parseColor("#e20000");
        otherColor = Color.parseColor("#e20000");
        paintColor = Color.WHITE;
        tempObjectColor = Color.WHITE;
        drawPathColor = Color.WHITE;
        textColor = Color.WHITE;

//        paintColor = Color.BLACK;
//        tempObjectColor = Color.parseColor("#16355b");
//        drawPathColor = Color.parseColor("#16355b");
//        textColor = Color.BLACK;

        infoColor = Color.parseColor("#46D4D4D4");

        pointSize = (float) Constants.MILIMETER * density;
        label = true;
        textSize = 2.5f * pointSize;

        showSignInfo = false;

    }


    public boolean isShowSignInfo() {
        return showSignInfo;
    }

    public void setShowSignInfo(boolean showSignInfo) {
        this.showSignInfo = showSignInfo;
    }

    public int getInfoColor() {
        return infoColor;
    }
    public int getPaintColor() {
        return paintColor;
    }

    public int getDrawPathColor() {
        return drawPathColor;
    }

    public int getTempObjectColor() {
        return tempObjectColor;
    }

    public int getTextColor() {
        return textColor;
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
