package coded.lens.example.simple;


import javaslang.collection.Stream;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

public class functionalTests {

    private Stream<BigInteger> fibs(final BigInteger a, final BigInteger b) {
        return Stream.cons(a, () -> fibs(b, a.add(b)));
    }

    @Test
    public void testName() throws Exception {
        Stream<BigInteger> fibs = fibs(BigInteger.ZERO, BigInteger.ONE);
        
        for (BigInteger l : fibs.drop(1000000).take(3)) {
            System.out.println(l);
        }

    }
}
