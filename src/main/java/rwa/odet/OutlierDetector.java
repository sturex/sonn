package rwa.odet;

import neural.Bounds;

import java.util.Set;
import java.util.function.DoubleSupplier;
import java.util.function.Supplier;

public interface OutlierDetector {
    void addChannel(DoubleSupplier ds, Bounds bounds, int bucketCount);
    void addAdaptiveChannel(Supplier<Object> supplier, Set<Object> dictionary);
    double next();
}
