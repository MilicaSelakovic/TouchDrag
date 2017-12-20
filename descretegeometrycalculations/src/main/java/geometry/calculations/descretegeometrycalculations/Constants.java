package geometry.calculations.descretegeometrycalculations;

public class Constants {
    static private double MILIMETER = 0.0393700787; // 1 mm u inchima

    private float factor;

    public Constants(double density, int densityDPI) {
        factor = 1.0f;
        double pixels = MILIMETER * densityDPI;

        distance_point = pixels * 2;
        minimalDistance = pixels * 2;

        radius_difference = pixels * 7.5;

        errorDrawing = (int) density;
        minimalNumberOfPoints = 3 * errorDrawing;
    }

    public double getDistance_point() {
        return factor * distance_point;
    }

    public double getCircle_min_ratio() {
        return circle_min_ratio;
    }

    public double getCircle_max_ratio() {
        return circle_max_ratio;
    }

    public int getErrorDrawing() {
        return (int) factor * errorDrawing;
    }

    public int getMinimalNumberOfPoints() {
        return (int) factor * minimalNumberOfPoints;
    }

    public double getMinAngle() {
        return minAngle;
    }

    public double getMaxAngle() {
        return maxAngle;
    }

    public double getMinimalDistance() {
        return factor * minimalDistance;
    }

    public double getMaxRadius() {
        return maxRadius;
    }

    public double getMinRatio() {
        return minRatio;
    }

    public double getMaxRatio() {
        return maxRatio;
    }

    public double getRadius_difference() {
        return factor * radius_difference;
    }

    public void setFactor(float factor) {
        this.factor = factor;
    }

    // udaljenonst tacke od trazene tacke u klasi Triangle metodi otrhocentar,.. i Point da li je tacka ispod kursora
    private double distance_point;

    private static double circle_min_ratio = 0.85;
    private static double circle_max_ratio = 1.15;

    private int errorDrawing;
    private int minimalNumberOfPoints;
    private static double minAngle = 0; // ne zavisi od gustine
    private static double maxAngle = 0.8; // takodje
    private double minimalDistance;
    private static double maxRadius = 300000;
    private static double minRatio = 0.75;
    private static double maxRatio = 1.5;

    private double radius_difference;


}
