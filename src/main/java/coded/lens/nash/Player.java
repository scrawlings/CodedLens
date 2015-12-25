package coded.lens.nash;

public class Player {
    private final int[] strategy;
    private final int points;
    private final int n;

    public Player(int n) {
        this.n = n;
        strategy = new int[n];
        for (int i = 0; i < n; i++) {
            strategy[i] = (int)Math.pow(n, 2);
        }

        points = (int)Math.pow(n, 3);
    }

    public int play() {
        double choice = points * Math.random();

        int total = 0;
        for (int i = 0; i < n; i++) {
            total += strategy[i];
            if (total >= choice) {
                return i;
            }
        }

        throw new IllegalStateException("should always find a choice before this line");
    }
}
