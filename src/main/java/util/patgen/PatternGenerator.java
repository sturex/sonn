package util.patgen;

import org.apache.commons.lang3.tuple.MutableTriple;
import org.apache.commons.lang3.tuple.Triple;

import java.util.*;
import java.util.stream.IntStream;

public class PatternGenerator implements Iterable<int[]> {

    private final int size;
    private final Random random;
    private final int limit;
    private final double probability;
    private final List<MutableTriple<Pattern, Double, Double>> pats;
    private double fuzzyIndex;
    private Iterator<int[]> iterator;

    private PatternGenerator(List<MutableTriple<Pattern, Double, Double>> pats, int seed, int size, int limit, double probability) {
        this.pats = pats;
        this.size = size;
        this.limit = limit;
        this.probability = probability;
        random = new Random(seed);
        double total = pats.stream().mapToDouble(Triple::getMiddle).sum();
        pats.forEach(triple -> triple.setMiddle(triple.getMiddle() / total));
        pats.sort(Comparator.comparingDouble(MutableTriple::getMiddle));
    }

    private int[] generateRandomIntegerArray() {
        final int[] arr = new int[size];
        IntStream.range(0, arr.length).forEach(i -> arr[i] = random.nextInt());
        return arr;
    }

    public int getSize() {
        return size;
    }

    @Override
    public Iterator<int[]> iterator() {
        return new Iterator<>() {
            private int curPos = 0;

            @Override
            public boolean hasNext() {
                return limit == 0 || curPos++ < limit;
            }

            @Override
            public int[] next() {
                if (pats.isEmpty()) {
                    return generateRandomIntegerArray();
                } else if (iterator == null || !iterator.hasNext()) {
                    if (random.nextDouble() < probability) {
                        double testProportion = random.nextDouble();
                        for (Triple<Pattern, Double, Double> triple : pats) {
                            iterator = triple.getLeft().iterator();
                            fuzzyIndex = triple.getRight();
                            if (testProportion > triple.getMiddle()) {
                                break;
                            }
                        }
                    }
                }
                if (iterator != null && iterator.hasNext()) {
                    int[] next = iterator.next();
                    int[] outArr = generateRandomIntegerArray();
                    for (int i = 0; i < size && i < next.length; i++) {
                        if (random.nextDouble() < fuzzyIndex) {
                            outArr[i] = random.nextInt();
                        } else {
                            outArr[i] = next[i];
                        }
                    }
                    return outArr;
                } else {
                    return generateRandomIntegerArray();
                }
            }
        };

    }

    public static Builder newBuilder(int size) {
        return new Builder(size);
    }

    public static class Builder {

        private final List<MutableTriple<Pattern, Double, Double>> pats = new ArrayList<>();
        private int seed = new Random().nextInt();
        private double probability = new Random().nextDouble();
        private final int size;
        private int limit = 0;

        private Builder(int size) {
            if (size <= 0) {
                throw new IllegalArgumentException();
            }
            this.size = size;
        }

        /**
         * Adds pattern to a list
         *
         * @param pattern    {@link Pattern} that is scheduled for random appearance
         * @param proportion the share of the pattern in total amount of such shares
         * @param fuzzyIndex A double value from 0 to 1 answering on:
         *                   Should the pattern be mixed with random data from generator? If yes, with what probability?
         */
        public Builder addPattern(Pattern pattern, double proportion, double fuzzyIndex) {
            Objects.requireNonNull(pattern);
            if (proportion <= 0) {
                throw new IllegalArgumentException("Not valid proportion: " + proportion + " Expected > 0");
            }
            if (fuzzyIndex < 0 || fuzzyIndex > 1) {
                throw new IllegalArgumentException("Not valid fuzzyIndex: " + fuzzyIndex + " Expected in [0;1]");
            }
            pats.add(MutableTriple.of(pattern, proportion, fuzzyIndex));
            return this;
        }

        /**
         * Use certain seed for certainty
         */
        public Builder withSeed(int seed) {
            this.seed = seed;
            return this;
        }

        /**
         * @param probability The probability of starting new random pattern generation after previous one has ended
         */
        public Builder withProbability(double probability) {
            if (probability < 0 || probability > 1) {
                throw new IllegalArgumentException("Not valid probability: " + probability + " Expected in [0;1]");
            }
            this.probability = probability;
            return this;
        }

        public PatternGenerator build() {
            return new PatternGenerator(pats, seed, size, limit, probability);
        }

        public Builder withLimit(int limit) {
            if (limit <= 0) {
                throw new IllegalArgumentException();
            }
            this.limit = limit;
            return this;
        }
    }
}
