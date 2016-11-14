package ua.abond.generics.factory;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class SuppliableFactory<T> extends PhantomFactory<T> {
    private final Supplier<T> supplier;

    public SuppliableFactory(int maxRefCount, Class<? extends T> type, Supplier<T> supplier) {
        super(maxRefCount);
        checkSupplier(type, supplier);
        this.supplier = supplier;
    }

    private void checkSupplier(Class<? extends T> type, Supplier<? extends T> supplier) {
        Class<? extends Supplier> aClass = supplier.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field f : declaredFields) {
            Class<?> fType = f.getType();
            if (type.isAssignableFrom(fType)) {
                throw new IllegalArgumentException("InputStream was captured by lambda expression.");
            }
        }
    }


    @Override
    protected T newInstance() {
        return supplier.get();
    }
}
