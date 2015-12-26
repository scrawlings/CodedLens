package coded.lens.nash;

import javaslang.collection.Stream;

public class Tournament {
    private final Game game;
    private final Player player1;
    private final Player player2;

    public Tournament(int n) {
        game = new Game(n);
        player1 = new Player(n);
        player2 = new Player(n);
    }

    private static final class Result {
        double result = 0.0;

        public void set(double result) {
            this.result = result;
        }

        public double get() {
            return result;
        }
    }

    private double play() {
        Result result = new Result();

        Round.Outcome outcome = (w, wst, wsc, l, lst, lsc) -> {
            w.increase(wst);
            l.reduce(lst);
            result.set(wsc - lsc);
        };

        Round.Tie tie = (st1, st2, sc) -> { result.set(0); };


        Round round = new Round(game, player1, player2);
        round.play(outcome, tie);

        return result.get();
    }

    public Stream<Double> scores() {
        return Stream.cons(play(), () -> scores());
    }

}
