package rwa.patrec;

import java.util.Set;
import java.util.function.Supplier;

public interface PatternDetector {

    void addStrictDictChannel(Supplier<Object> supplier, Set<Object> dictionary);

    void addAdaptiveDictChannel(Supplier<Object> supplier, Set<Object> dictionary);

    int[] next();

    void reset();
}
