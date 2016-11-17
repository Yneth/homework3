package ua.abond.generics.cache;

import org.junit.Test;
import sun.misc.IOUtils;
import ua.abond.util.Memory;

import java.io.IOException;
import java.lang.ref.Reference;
import java.lang.reflect.Field;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;

import static org.junit.Assert.*;

public class FileCacheTest {
    private static final String TEST_FILE = "/image.jpg";

    @Test(expected = NullPointerException.class)
    public void testGetArgNull() throws Exception {
        new FileCache().get(null);
    }

    @Test(expected = NullPointerException.class)
    public void testPutArgNull() throws Exception {
        new FileCache().put(null);
    }

    @Test
    public void testGetFromEmptyCache() throws Exception {
        assertNull(new FileCache().get(Paths.get("")));
    }


    @Test(expected = NoSuchFileException.class)
    public void testPutMissingFile() throws Exception {
        FileCache fileCache = new FileCache();
        String fileName = "sadsadas";
        Path path = Paths.get(fileName);
        fileCache.put(path);
    }

    @Test
    public void testGet() throws Exception {
        FileCache fileCache = new FileCache();
        String fileName = getResourcePath(TEST_FILE);
        Path path = Paths.get(fileName);

        fileCache.put(path);

        assertNotNull(fileCache.get(path));
        assertTrue(Arrays.equals(getBytes(TEST_FILE), fileCache.get(path)));
    }

    @Test
    public void testSoftReferencesClearOnOutOfMemory() throws Exception {
        FileCache fileCache = new FileCache();
        String fileName = getResourcePath(TEST_FILE);
        Path path = Paths.get(fileName);
        fileCache.put(path);

        Memory.forceOutOfMemory();

        Map<Path, Reference<byte[]>> map = getMap(fileCache);

        assertNull(map.get(path).get());
    }

    private Map<Path, Reference<byte[]>> getMap(FileCache cache)
            throws NoSuchFieldException, IllegalAccessException {
        Field fileCache = cache.getClass().getDeclaredField("cache");
        fileCache.setAccessible(true);
        return (Map<Path, Reference<byte[]>>) fileCache.get(cache);
    }

    private byte[] getBytes(String fileName) throws IOException {
        return IOUtils.readFully(FileCache.class.getResourceAsStream(fileName), -1, false);
    }

    private String getResourcePath(String fileName) {
        return FileCache.class.getResource(fileName).getPath().substring(1);
    }
}