package coded.lens.nash;

import org.junit.Test;

import static org.junit.Assert.*;

public class PlayerTest {

    @Test(timeout=1000)
    public void willGenerateAllOptions() throws Exception {
        int n = 500;
        Player p1 = new Player(n);
        boolean[] plays = new boolean[n];

        while (!remainingUnplayed(plays)) {
            plays[p1.play()] = true;
        }
    }

    private boolean remainingUnplayed(boolean[] plays) {
        boolean played = true;
        for (boolean play : plays) {
            played = play && played;
        }
        return played;
    }
}