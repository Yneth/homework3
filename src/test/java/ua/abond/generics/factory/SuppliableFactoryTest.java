package ua.abond.generics.factory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SuppliableFactoryTest {
    private static final int GC_COUNT = 10;

    @Test
    public void testShouldNotLoop() throws Exception {
        int count = 10;

        PhantomFactory<String> factory =
                new SuppliableFactory<>(count, String.class, () -> new String("sadboy"));

        String val = factory.create();
        val = null;

        for (int i = 0; i < GC_COUNT; i++) {
            System.gc();
        }

        for (int i = 0; i < count; i++) {
            factory.create();
        }
    }

    @Test
    public void testCount() throws Exception {
        int count = 10;

        PhantomFactory<NoArg> factory =
                new SuppliableFactory<>(count, NoArg.class, NoArg::new);

        for (int i = 0; i < count << 2; i++) {
            factory.create();
            assertEquals(Math.min(i + 1, count), factory.liveObjectsCount());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadSupplier() {
        String string = "";
        new SuppliableFactory<>(10, String.class, () -> string);
    }
}
