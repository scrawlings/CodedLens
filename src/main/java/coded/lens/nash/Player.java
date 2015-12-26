package coded.lens.nash;

public class Player {
    private final int[] strategy;
    private final int points;
    private final int n;

    public Player(int n) {
        this.n = n;
        strategy = new int[n];
        for (int i = 0; i < n; i++) {
            strategy[i] = n;
        }

        points = n * n;
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

    public void reduce(int s) {
        if (strategy[s] > 0) {
            strategy[s]--;
            int i = (int)Math.random() * n;
            strategy[i]++;
        }
    }

    public void increase(int s) {
        int i = (int)Math.random() * n;
        if (strategy[i] != 0) {
            strategy[i]--;
            strategy[s]++;
        }
    }
}
