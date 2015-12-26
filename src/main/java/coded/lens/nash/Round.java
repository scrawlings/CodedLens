package coded.lens.nash;

public class Round {
    private final Game game;
    private final Player p1;
    private final Player p2;

    public Round(Game game, Player p1, Player p2) {
        this.game = game;
        this.p1 = p1;
        this.p2 = p2;
    }

    public interface Outcome {
        public void outcome(Player winner, int player1strategy, double winnerScore, Player loser, int player2strategy, double loserScore);
    }

    public interface Tie {
        public void tie(int player1strategy, int player2strategy, double score);
    }

    public void play(Outcome result, Tie tie) {
        int s1 = p1.play();
        int s2 = p2.play();

        double player1score = game.score(s1, s2);
        double player2score = game.score(s2, s1);

        if (player1score == player2score) {
            tie.tie(s1, s2, player1score);
        } else if (player1score > player2score) {
            result.outcome(p1, s1, player1score, p2, s2, player2score);
        } else if (player2score > player1score) {
            result.outcome(p2, s2, player2score, p1, s1, player1score);
        }
    }


}
