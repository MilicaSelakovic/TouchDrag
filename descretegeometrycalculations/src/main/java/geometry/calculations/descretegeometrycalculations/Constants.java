package geometry.calculations.descretegeometrycalculations;

public class Constants {

    Constants(double density) {

    }

    public double getEpsilon_distance() {
        return epsilon_distance;
    }

    public double getDistance_point() {
        return distance_point;
    }

    public double getCircle_min_ratio() {
        return circle_min_ratio;
    }

    public double getCircle_max_ratio() {
        return circle_max_ratio;
    }

    public int getErrorDrawing() {
        return errorDrawing;
    }

    public int getMinimalNumberOfPoints() {
        return minimalNumberOfPoints;
    }

    public double getMinAngle() {
        return minAngle;
    }

    public double getMaxAngle() {
        return maxAngle;
    }

    public double getMinimalDistance() {
        return minimalDistance;
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

    // u Klasi Line
    private double epsilon_distance = 10;
    // udaljenonst tacke od trazene tacke u klasi Triangle metodi otrhocentar,.. i Point da li je tacka ispod kursora
    private double distance_point = 20;

    private double circle_min_ratio = 0.85;
    private double circle_max_ratio = 1.15;

    private int errorDrawing = 3;
    private int minimalNumberOfPoints = 10;
    private double minAngle = 0; // ne zavisi od gustine
    private double maxAngle = 0.8; // takodje
    private double minimalDistance = 40;
    private double maxRadius = 3000;
    private double minRatio = 0.75;
    private double maxRatio = 1.5;


}
