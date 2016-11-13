package ua.abond.generics.cache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.Objects;
import java.util.function.Supplier;

public class SoftInputStreamProxy extends InputStream {
    private final Supplier<InputStream> supplier;

    private SoftReference<InputStream> reference;

    public SoftInputStreamProxy(final InputStream inputStream) {
        this(() -> inputStream);
    }

    public SoftInputStreamProxy(Supplier<? extends InputStream> supplier) {
        Objects.requireNonNull(supplier);
        checkSupplier(supplier);

        this.supplier = () -> Objects.requireNonNull(supplier.get());
        this.reference = new SoftReference<>(this.supplier.get());
    }

    @Override
    public int read() throws IOException {
        return getStream().read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        return getStream().read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        return getStream().read(b, off, len);
    }

    @Override
    public long skip(long n) throws IOException {
        return getStream().skip(n);
    }

    @Override
    public int available() throws IOException {
        return getStream().available();
    }

    @Override
    public void close() throws IOException {
        getStream().close();
    }

    @Override
    public synchronized void mark(int readlimit) {
        getStream().mark(readlimit);
    }

    @Override
    public synchronized void reset() throws IOException {
        getStream().reset();
    }

    @Override
    public boolean markSupported() {
        return getStream().markSupported();
    }

    private void checkSupplier(Supplier<? extends InputStream> supplier) {
        Class<? extends Supplier> aClass = supplier.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field f : declaredFields) {
            Class<?> type = f.getType();
            if (InputStream.class.isAssignableFrom(type)) {
                throw new IllegalArgumentException("InputStream was captured by lambda expression.");
            }
        }
    }

    private InputStream getStream() {
        InputStream fis = reference.get();
        if (fis == null) {
            fis = supplier.get();
            reference = new SoftReference<>(fis);
        }
        return fis;
    }
}
