package ua.abond.generics.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class IsSortedTest {

    @Test
    public void testIsSortedEmpty() throws Exception {
        assertTrue(Lists.isSorted(Collections.emptyList(), Integer::compare));
    }

    @Test
    public void testIsSortedOneElement() throws Exception {
        assertTrue(Lists.isSorted(Collections.singletonList(1), Integer::compare));
    }

    @Test
    public void testIsSortedOneNullElement() throws Exception {
        assertTrue(Lists.isSorted(Collections.singletonList(null), Integer::compare));
    }

    @Test
    public void testIsSortedNullElements() throws Exception {
        assertFalse(Lists.isSorted(Arrays.asList(null, null), Integer::compare));
        assertFalse(Lists.isSorted(Arrays.asList(null, null, null), Integer::compare));
    }

    @Test
    public void testIsNotSorted() throws Exception {
        Comparator<Integer> cmp = Integer::compare;
        assertFalse(Lists.isSorted(Arrays.asList(10, 9, 10, 11, 12, 13), cmp));
        assertFalse(Lists.isSorted(Arrays.asList(9, 10, 0, 11, 12), cmp));
        assertFalse(Lists.isSorted(Arrays.asList(13, 12, 11, 10), cmp));
    }
}