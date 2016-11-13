package ua.abond.generics.factory;

import org.apache.log4j.Logger;

public class ReflexiveFactory<T> extends PhantomFactory<T> {
    private static final Logger LOGGER = Logger.getLogger(ReflexiveFactory.class);

    private final Class<? extends T> type;

    public ReflexiveFactory(int maxRefCount, Class<? extends T> type) {
        super(maxRefCount);
        this.type = type;
    }

    @Override
    protected T newInstance() {
        try {
            return type.newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("Failed to create new instance.", e);
        } catch (IllegalAccessException e) {
            LOGGER.error("Reflection error occurred", e);
        }
        return null;
    }
}
