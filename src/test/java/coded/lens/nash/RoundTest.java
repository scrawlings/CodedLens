package coded.lens.nash;

import org.junit.Test;

import static org.junit.Assert.*;

public class RoundTest {

    public static final Player ALWAYS_PLAYS_0 = new Player(2) {
        public int play() {
            return 0;
        }
    };

    public static final Player ALWAYS_PLAYS_1 = new Player(2) {
        public int play() {
            return 1;
        }
    };

    public static final Game ALWAYS_0_BEATS_1 = new Game(2) {
        public double score(int agent, int opponent) {
            if (agent == 0 && opponent == 1) {
                return 1.0;
            }
            else if (agent == 1 && opponent == 0) {
                return 0.0;
            }
            else {
                return 0.5;
            }
        }
    };

    @Test
    public void player1wins() throws Exception {
        Round r = new Round(ALWAYS_0_BEATS_1, ALWAYS_PLAYS_0, ALWAYS_PLAYS_1);

        r.play((w, ws, l, ls) -> assertSame(w, ALWAYS_PLAYS_0), (s) -> fail());
    }

    @Test
    public void player2wins() throws Exception {
        Round r = new Round(ALWAYS_0_BEATS_1, ALWAYS_PLAYS_1, ALWAYS_PLAYS_0);

        r.play((w, ws, l, ls) -> assertSame(w, ALWAYS_PLAYS_0), (s) -> fail());
    }

    @Test
    public void equalStrategyTies() throws Exception {
        Round r = new Round(ALWAYS_0_BEATS_1, ALWAYS_PLAYS_1, ALWAYS_PLAYS_1);

        r.play((w, ws, l, ls) -> fail(), (s) -> {});
    }
}