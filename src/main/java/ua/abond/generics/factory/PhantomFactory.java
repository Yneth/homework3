package ua.abond.generics.factory;

import org.apache.log4j.Logger;

import java.lang.ref.PhantomReference;
import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.ArrayList;
import java.util.List;

public abstract class PhantomFactory<T> {
    private static final long TIMEOUT = 200;
    private static final Logger LOGGER = Logger.getLogger(PhantomFactory.class);

    private final List<Reference<? extends T>> refs = new ArrayList<>();

    protected final int maxRefCount;
    protected final ReferenceQueue<T> queue;

    protected int count;

    public PhantomFactory(final int maxRefCount) {
        if (maxRefCount <= 0) {
            throw new IllegalArgumentException("MaxRefCount should be > 0.");
        }

        this.maxRefCount = maxRefCount;
        this.queue = new ReferenceQueue<>();

        this.count = 0;
    }

    public T create() {
        while (true) {
            if (count < maxRefCount) {
                count++;
                return wrap(newInstance());
            }

            Reference<? extends T> ref;
            try {
                ref = queue.remove(TIMEOUT);
                if (ref == null)
                    continue;
                count--;
                refs.remove(ref);
            } catch (InterruptedException e) {
                LOGGER.error("Failed to remove reference from the queue.", e);
                Thread.currentThread().interrupt();
            }
        }
    }

    protected T wrap(T t) {
        Reference<T> ref = new PhantomReference<>(t, queue);
        refs.add(ref);

        return t;
    }

    protected abstract T newInstance();
}
