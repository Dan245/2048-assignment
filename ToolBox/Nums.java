package ToolBox;

public class Nums {
    public static double constrain(double value, double min, double max) {
        return Math.min(Math.max(value, min), max);
    }

    public static boolean inRange(int value, int min, int max) {
        return (value >= min) && (value <= max);
    }
}
