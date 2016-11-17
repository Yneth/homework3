package ua.abond.generics.factory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SuppliableFactoryTest {
    private static final int GC_COUNT = 10;

    @Test
    public void testShouldNotLoop() throws Exception {
        int count = 10;
        PhantomFactory<String> factory =
                SuppliableFactory.create(count, String.class, () -> new String("test test"));

        String val = factory.create();
        val = null;

        for (int i = 0; i < count; i++) {
            factory.create();
        }
    }

    @Test
    public void testCount() throws Exception {
        int count = 10;

        PhantomFactory<NoArg> factory =
                SuppliableFactory.create(count, NoArg.class, NoArg::new);

        for (int i = 0; i < count << 2; i++) {
            factory.create();
            assertEquals(Math.min(i + 1, count), factory.liveObjectsCount());
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadSupplier() {
        String string = "";
        SuppliableFactory.create(10, String.class, () -> string);
    }
}
