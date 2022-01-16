package rwa.odet;

import neural.Bounds;

import java.util.function.DoubleSupplier;

public interface OutlierDetector {
    void addChannel(DoubleSupplier ds, Bounds bounds, int bucketCount);
    double next();
}
