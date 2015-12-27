package coded.lens.nash;

import javaslang.Tuple;
import javaslang.Tuple2;
import javaslang.collection.Iterator;
import javaslang.collection.Stream;
import javaslang.control.Option;

public class ScoreAnalysis {

    /*
     * This is an O(N) solution maintaining a mutating circular buffer of the past N scores from
     * which to calculate the sum over a size N sliding window. In a pure function version this
     * could be implemented with javaslang.collection.queue.
     * A more stream style approach would zip with Stream::sliding() and recalulate the sum but
     * that becomes O(N*windowSize) (which is basically an O(N^2) solution).
     */
    public Stream<Double> windowSums(int windowSize, Stream<Double> scores) {
        double[] window = new double[windowSize];
        double sum = 0;
        for (int next = 0; next < windowSize; next++) {
            window[next] = scores.head();
            sum += scores.head();
            scores = scores.tail();
        }

        return windowSums(windowSize, scores, window, 0, sum);
    }

    private Stream<Double> windowSums(int windowSize, Stream<Double> scores, double[] window, int next, double sum) {
        return Stream.cons(sum,
                           () -> scores.headOption()
                                            .map(head -> {
                                                final double nextSum = sum + head - window[next];
                                                window[next] = head;
                                                final int nextNext = (next + 1) % windowSize;
                                                return windowSums(windowSize, scores.tail(), window, nextNext, nextSum);
                                            })
                                            .orElse(Stream.empty()));
    }

    public Double sumOfSqrOfDiff(Tuple2<Double, Stream<Double>> t) {
        Double mean = t._1;
        Stream<Double> scores = t._2;

        return scores.map(score -> Math.pow(mean - score, 2)).sum().doubleValue();
    }


    public Stream<Double> avgMeanSqrsOverWindow(int windowSize, Stream<Double> scores) {
        return windowSums(windowSize, scores)
                .map(sum -> sum / windowSize)
                .zip(scores.sliding(windowSize))
                .map(this::sumOfSqrOfDiff)
                .map(sqrOfDiff -> sqrOfDiff / windowSize);
    }

}
