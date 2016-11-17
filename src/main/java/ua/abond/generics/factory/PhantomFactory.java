package ua.abond.generics.factory;

import org.apache.log4j.Logger;

import java.lang.ref.*;
import java.util.ArrayList;
import java.util.List;

public abstract class PhantomFactory<T> {
    private static final long TIMEOUT = 200;
    private static final Logger LOGGER = Logger.getLogger(PhantomFactory.class);

    private final List<Reference<? extends T>> references = new ArrayList<>();

    protected final int maxRefCount;
    protected final ReferenceQueue<T> queue;

    public PhantomFactory(final int maxRefCount) {
        if (maxRefCount <= 0) {
            throw new IllegalArgumentException("MaxRefCount should be > 0.");
        }

        this.maxRefCount = maxRefCount;
        this.queue = new ReferenceQueue<>();
    }

    public T create() throws InterruptedException {
        while (true) {
            if (references.size() < maxRefCount) {
                return wrap(newInstance());
            }

            Reference<? extends T> ref;
            try {
                ref = queue.remove(TIMEOUT);
                if (ref == null) {
                    System.gc();
                    continue;
                }
                references.remove(ref);
                ref.clear();
            } catch (InterruptedException e) {
                LOGGER.error("Failed to remove reference from the queue.", e);
                throw e;
            }
        }
    }

    public int liveObjectsCount() {
        return references.size();
    }

    protected T wrap(T t) {
        references.add(new WeakReference<>(t, queue));
        return t;
    }

    protected abstract T newInstance();
}