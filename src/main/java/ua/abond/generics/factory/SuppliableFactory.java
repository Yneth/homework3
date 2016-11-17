package ua.abond.generics.factory;

import java.lang.reflect.Field;
import java.util.function.Supplier;

public class SuppliableFactory<T> extends PhantomFactory<T> {
    private final Supplier<? extends T> supplier;

    protected SuppliableFactory(int maxRefCount, Supplier<? extends T> supplier) {
        super(maxRefCount);
        this.supplier = supplier;
    }

    @Override
    protected T newInstance() {
        return supplier.get();
    }

    public static <T> SuppliableFactory<T> create(int maxRefCount,
                                                  Class<? extends T> type,
                                                  Supplier<? extends T> supplier) {
        checkSupplier(type, supplier);
        return new SuppliableFactory<>(maxRefCount, supplier);
    }

    private static <T> void checkSupplier(Class<? extends T> type, Supplier<? extends T> supplier) {
        Class<? extends Supplier> aClass = supplier.getClass();
        Field[] declaredFields = aClass.getDeclaredFields();
        for (Field f : declaredFields) {
            Class<?> fType = f.getType();
            if (type.isAssignableFrom(fType)) {
                throw new IllegalArgumentException("Supplied value was captured by lambda expression.");
            }
        }
    }
}
