package ua.abond.generics.cache;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.ref.SoftReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FileCache {
    private final Map<Path, Reference<byte[]>> fileCache;

    public FileCache() {
        this(new HashMap<>());
    }

    public FileCache(int capacity) {
        this(new HashMap<>(capacity));
    }

    private FileCache(Map<Path, Reference<byte[]>> map) {
        Objects.requireNonNull(map);
        this.fileCache = map;
    }

    public byte[] get(Path path) throws IOException {
        Objects.requireNonNull(path);

        byte[] result = null;
        if (fileCache.containsKey(path)) {
            Reference<byte[]> ref = fileCache.get(path);
            result = ref.get();
            if (result == null) {
                result = getBytes(path);
                ref = new SoftReference<>(result);
                fileCache.replace(path, ref);
            }
        }
        return result;
    }

    public void put(Path path) throws IOException {
        Objects.requireNonNull(path);

        if (fileCache.containsKey(path)) {
            return;
        }
        byte[] bytes = getBytes(path);
        Reference<byte[]> ref = new SoftReference<>(bytes);
        fileCache.put(path, ref);
    }

    private byte[] getBytes(Path path) throws IOException {
        return Files.readAllBytes(path);
    }
}
