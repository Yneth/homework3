package ua.abond.util;

import java.util.LinkedList;
import java.util.List;

public final class Memory {
    private Memory() {

    }

    public static boolean forceOutOfMemory() {
        try {
            final List<long[]> memhog = new LinkedList<long[]>();
            while (true) {
                memhog.add(new long[102400]);
            }
        } catch (OutOfMemoryError e) {
            return true;
        }
    }
}
