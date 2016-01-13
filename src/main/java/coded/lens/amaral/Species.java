package coded.lens.amaral;

import javaslang.collection.List;

public class Species {
    private static int ids = 0;
    private final int id;

    public Species() {
        id = ids++;
    }

    @Override
    public String toString() {
        return "" + id;
    }
}
