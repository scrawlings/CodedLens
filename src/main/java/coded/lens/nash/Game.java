package coded.lens.nash;

public class Game {
    private final double[][] score;

    public Game(int n) {
        score = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                double r = (Math.random() * 2) - 1;
                double s = (Math.random() * 2) - 1;
                score[i][j] = r * s;
            }
        }
    }

    public double score(int agent, int opponent) {
        return score[agent][opponent];
    }
}
