package ua.abond.generics.factory;

import org.junit.Test;

public class SuppliableFactoryTest {
    private static final int GC_COUNT = 10;

    @Test
    public void testShouldNotLoop() throws Exception {
        int count = 10;

        PhantomFactory<String> factory =
                new SuppliableFactory<>(count, () -> new String("sadboy"));

        String val = factory.create();
        val = null;

        for (int i = 0; i < GC_COUNT; i++) {
            System.gc();
        }

        for (int i = 0; i < count; i++) {
            factory.create();
        }
    }
}
