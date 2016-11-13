package ua.abond.generics.cache;

import org.junit.Test;
import ua.abond.util.Memory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertTrue;

public class SoftInputStreamProxyTest {
    private static final String TEST_FILE = "/image.jpg";

    @Test(expected = NullPointerException.class)
    public void testSupplierReturnNull() {
        new SoftInputStreamProxy(() -> null);
    }

    @Test
    public void testOutOfMemoryException() {
        assertTrue(Memory.forceOutOfMemory());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInputStreamConstructor() throws Exception {
        InputStream bais = new ByteArrayInputStream(new byte[]{});
        InputStream is = new SoftInputStreamProxy(bais);
        bais = null;

        Reference<InputStream> o = getReference(is);
        assertTrue(!Objects.isNull(o));
        assertTrue(!Objects.isNull(o.get()));

        assertTrue(Memory.forceOutOfMemory());

        assertFalse(Objects.isNull(o.get()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInputStreamSupplierVariable() throws Exception {
        InputStream bais = new ByteArrayInputStream(new byte[]{});
        InputStream is = new SoftInputStreamProxy(() -> bais);
//        bais = null;

        Reference<InputStream> o = getReference(is);
        assertTrue(!Objects.isNull(o));
        assertTrue(!Objects.isNull(o.get()));

        assertTrue(Memory.forceOutOfMemory());

        assertFalse(Objects.isNull(o.get()));
    }

    @Test
    public void testInputStreamSupplierVariableArgument() throws Exception {
        byte[] bytes = new byte[]{};
        InputStream is = new SoftInputStreamProxy(() -> new ByteArrayInputStream(bytes));

        Reference<InputStream> o = getReference(is);
        assertTrue(!Objects.isNull(o));
        assertTrue(!Objects.isNull(o.get()));

        assertTrue(Memory.forceOutOfMemory());

        assertTrue(Objects.isNull(o.get()));
    }

    @Test
    public void testSoftRefAfterOutOfMemory() throws Exception {
        InputStream is = new SoftInputStreamProxy(() -> SoftInputStreamProxy.class.getResourceAsStream(TEST_FILE));

        Reference<InputStream> o = getReference(is);
        assertTrue(!Objects.isNull(o));
        assertTrue(!Objects.isNull(o.get()));

        assertTrue(Memory.forceOutOfMemory());

        assertTrue(Objects.isNull(o.get()));
    }

    @Test
    public void testSoftRefAfterRegularGCSupplier() throws Exception {
        InputStream is = new SoftInputStreamProxy(
                () -> SoftInputStreamProxy.class.getResourceAsStream(TEST_FILE)
        );

        Reference<InputStream> ref = getReference(is);

        assertTrue(!Objects.isNull(ref));
        assertTrue(!Objects.isNull(ref.get()));

        for (int i = 0; i < 100; i++) {
            System.gc();
            System.runFinalization();
        }

//        assertTrue(Objects.isNull(ref.get()));
    }

    private Reference<InputStream> getReference(InputStream is) throws Exception {
        Field reference = SoftInputStreamProxy.class.getDeclaredField("reference");
        reference.setAccessible(true);
        return (Reference<InputStream>) reference.get(is);
    }
}