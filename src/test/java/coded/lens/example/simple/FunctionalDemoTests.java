package coded.lens.example.simple;


import javaslang.collection.Stream;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class FunctionalDemoTests {

    private Stream<BigInteger> fibs() {
        return fibs(BigInteger.ZERO, BigInteger.ONE);
    }

    private Stream<BigInteger> fibs(final BigInteger a, final BigInteger b) {
        return Stream.cons(a, () -> fibs(b, a.add(b)));
    }

    @Test
    public void dropAndTakeFromFibs() throws Exception {

        for (BigInteger l : fibs().drop(10000).take(3)) {
            System.out.println(l);
        }

    }

    @Test
    public void getFromFibs() throws Exception {
        Stream<BigInteger> fibs = fibs();

        System.out.println(fibs.head()); fibs = fibs.tail();
        System.out.println(fibs.head()); fibs = fibs.tail();
        System.out.println(fibs.head()); fibs = fibs.tail();
        System.out.println(fibs.head()); fibs = fibs.tail();
        System.out.println(fibs.head()); fibs = fibs.tail();
        System.out.println(fibs.head()); fibs = fibs.tail();
        System.out.println(fibs.head()); fibs = fibs.tail();

    }
}
