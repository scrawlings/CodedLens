package coded.lens.nash;

import javaslang.Tuple2;
import javaslang.collection.Stream;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class ScoreAnalysisTest {
    @Test
    public void sumsOverWindow() throws Exception {
        ScoreAnalysis scoreAnalysis = new ScoreAnalysis();

        Stream<Double> sumsStream = scoreAnalysis.windowSums(3, Stream.of(1.0, 3.0, 8.0, 2.0, 5.0, 9.0));

        Double[] sums = sumsStream.toJavaArray(Double.class);
        Double[] expectedSums = {12.0, 13.0, 15.0, 16.0};

        assertArrayEquals(sums, expectedSums);
    }

    @Test
    public void sumsOverTournamentScoreSpread() throws Exception {
        ScoreAnalysis scoreAnalysis = new ScoreAnalysis();
        Tournament tournament = new Tournament(50);

        Stream<Double> sums = scoreAnalysis.windowSums(10, tournament.scores());
        sums.drop(10000).take(5).forEach(sum -> System.out.printf("%.4f\n", sum));
    }

    @Test
    public void streamZipper() throws Exception {
        ScoreAnalysis scoreAnalysis = new ScoreAnalysis();

        int windowSize = 3;
        Stream<Double> scores = Stream.of(1.0, 3.0, 8.0, 2.0, 5.0, 9.0);
        Stream<Double> sumsStream = scoreAnalysis.windowSums(windowSize, scores);

        Stream<Tuple2<Double, Stream<Double>>> zipped = sumsStream.zip(scores.sliding(windowSize));
        zipped.forEach(t -> {
            System.out.print(t._1 + ": ");
            t._2.forEach(x -> System.out.print("" + x + ", "));
            System.out.println();
        });
    }

    @Test
    public void progressionOfDeviationsByWindow() throws Exception {
        ScoreAnalysis scoreAnalysis = new ScoreAnalysis();
        Tournament tournament = new Tournament(10);

        int windowSize = 100;
        Stream<Double> avgMeanSqrsOverWindow = scoreAnalysis.avgMeanSqrsOverWindow(windowSize, tournament.scores());
        avgMeanSqrsOverWindow.sliding(1,100).take(100).map(ls -> ls.head()).forEach(sum -> System.out.printf("%.4f\n", sum));
    }

    @Test
    public void progressionOfDeviationsByWindowOverGeometricSeries() throws Exception {
        ScoreAnalysis scoreAnalysis = new ScoreAnalysis();

        Stream<Double> doubles = Stream.from(1).map(x -> 1 / (x * 0.01));
        int windowSize = 4;
        Stream<Double> avgMeanSqrsOverWindow = scoreAnalysis.avgMeanSqrsOverWindow(windowSize, doubles);
        avgMeanSqrsOverWindow.drop(1).take(10).forEach(sum -> System.out.printf("%.2f\n", sum));
    }
}