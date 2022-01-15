package neural;

public record Bounds(double lowerBound, double upperBound) {
    @Override
    public double lowerBound() {
        return lowerBound;
    }

    @Override
    public double upperBound() {
        return upperBound;
    }
}
