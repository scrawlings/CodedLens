package coded.lens.nash;

import org.junit.Test;

import static org.junit.Assert.*;

public class TournamentTest {
    @Test
    public void seriesOfScores() throws Exception {
        Tournament T = new Tournament(5);
        for (Double difference : T.scores().drop(1000).take(100)) {
            System.out.println(difference + ", ");
        }

    }
}