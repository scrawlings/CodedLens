package coded.lens.example.simple;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class R3Test {
    @Test
    public void testName() throws Exception {
        // read lines of text from a file

        BufferedReader br = inputFile();
        List<String> inputLines = new ArrayList<>();
        try {
            String line = br.readLine();
            while (line != null) {
                inputLines.add(line);
                line = br.readLine();
            }
        } finally {
            if (br != null) {
                br.close();
            }
        }

        assert(inputLines.size() > 0);

        // shuffle
        // List<String> randomised = randomiseList(inputLines);

        // print to screen
        for (int i : randomIndex(inputLines.size())) {
            System.out.println(inputLines.get(i));
        }
    }

    int[] randomIndex(int size) {
        int[] index = new int[size];
        for (int i = 0; i < size; i++) {
            index[i] = i;
        }

        // Fisher-Yates
//        for i from n−1 downto 1 do
//            j ← random integer such that 0 ≤ j ≤ i
//            exchange a[j] and a[i]

        for (int i = index.length - 1; i > 0; i--) {
            int j = (int)(Math.random() * (i + 1));
            int x = index[j];
            index[j] = index[i];
            index[i] = x;
        }

        return index;
    }

    private List<String> randomiseList(List<String> inputLines) {
        List<String> randomised = new ArrayList<>();

        while (inputLines.size() > 0) {
            String removedLine = inputLines.remove((int) (Math.random() * inputLines.size()));
            randomised.add(removedLine);
        }

        return randomised;
    }

    private BufferedReader inputFile() throws IOException {
        return new BufferedReader(new FileReader("/Users/jbk/Programming/CodedLens/src/test/java/coded/lens/example/simple/FunctionalTests.java"));
    }
}
