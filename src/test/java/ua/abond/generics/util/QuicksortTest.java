package ua.abond.generics.util;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertTrue;

public class QuicksortTest {

    @Test(expected = NullPointerException.class)
    public void testSortWithNullList() {
        Lists.sort(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSortNonComparable() throws Exception {
        List<A> list = Arrays.asList(new A(), new A(), new A(), new A());
        Lists.sort(list, null);
    }

    @Test
    public void testSortComparable() throws Exception {
        List<Integer> list = Arrays.asList(10, 1, 100, 10, 22);
        Lists.sort(list, null);
        assertTrue(Lists.isSorted(list));
    }

    @Test
    public void testSortArrayList() throws Exception {
        Comparator<Integer> cmp = Integer::compare;
        List<Integer> list = Arrays.asList(10, 1, 2, 2, 4, 0);
        Lists.sort(list, cmp);
        assertTrue(Lists.isSorted(list, cmp));
    }

    @Test
    public void testSortArrayList1() throws Exception {
        Comparator<Integer> cmp = Integer::compare;
        List<Integer> list = Arrays.asList(1, 2, 2, 4, 0);
        Lists.sort(list, cmp);
        assertTrue(Lists.isSorted(list, cmp));
    }

    @Test
    public void testSortLinkedList() throws Exception {
        Comparator<Integer> cmp = Integer::compare;
        List<Integer> list = new LinkedList<>(Arrays.asList(1, 2, 2, 4, 0));
        Lists.sort(list, cmp);

        assertTrue(Lists.isSorted(list, cmp));
    }

    @Test
    public void testSortEmptyLinkedList() throws Exception {
        Comparator<Integer> cmp = Integer::compare;
        List<Integer> list = new LinkedList<>(Arrays.asList());

        Lists.sort(list, cmp);

        assertTrue(Lists.isSorted(list, cmp));
    }

    @Test
    public void testSortEmptyArrayList() throws Exception {
        Comparator<Integer> cmp = Integer::compare;
        List<Integer> list = Arrays.asList();

        Lists.sort(list, cmp);

        assertTrue(Lists.isSorted(list, cmp));
    }

    @Test
    public void testCompilesWithParentComparator() {
        Comparator<A> cmp = (a0, a1) -> 1;
        List<B> list = Arrays.asList(new B());

        Lists.sort(list, cmp);


        assertTrue(Lists.isSorted(list, cmp));
    }

    private static class A {
    }

    private static class B extends A {
    }
}