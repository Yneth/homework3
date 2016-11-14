package ua.abond.generics.factory;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ReflexiveFactoryTest {
    private static final int GC_COUNT = 10;

    @Test
    public void testShouldNotLoop() throws Exception {
        int count = 10;

        PhantomFactory<NoArg> factory =
                new ReflexiveFactory<>(count, NoArg.class);

        NoArg val = factory.create();
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
                new ReflexiveFactory<>(count, NoArg.class);

        for (int i = 0; i < count << 2; i++) {
            factory.create();
            assertEquals(Math.min(i + 1, count), factory.liveObjectsCount());
        }
    }
}

class NoArg {
    int i = 10;

    public NoArg() {
    }

    @Override
    public String toString() {
        return "NoArg{" +
                "i=" + i +
                '}';
    }
}