package ua.abond.generics.factory;

import java.util.function.Supplier;

public class SuppliableFactory<T> extends PhantomFactory<T> {
    private final Supplier<T> supplier;

    public SuppliableFactory(int maxRefCount, Supplier<T> supplier) {
        super(maxRefCount);
        this.supplier = supplier;
    }

    @Override
    protected T newInstance() {
        return supplier.get();
    }
}
