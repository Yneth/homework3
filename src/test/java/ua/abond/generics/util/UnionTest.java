package ua.abond.generics.util;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UnionTest {

    @Test(expected = NullPointerException.class)
    public void testComparableNullArg0() {
        Lists.union(null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testComparableNullArg1() {
        Lists.union(new ArrayList<Integer>(), null);
    }

    @Test(expected = NullPointerException.class)
    public void testComparatorNullArg0() {
        Lists.union(null, null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testComparatorNullArg1() {
        Lists.union(new ArrayList<Integer>(), null, null);
    }

    @Test(expected = NullPointerException.class)
    public void testComparatorNullArg2() {
        Lists.union(new ArrayList<Integer>(), new ArrayList<>(), null);
    }

    @Test
    public void testEmptyComparatorUnion() {
        List<Integer> union =
                Lists.union(new ArrayList<>(), new ArrayList<>(), Integer::compare);
        assertTrue(union.isEmpty());
    }

    @Test
    public void testEmptyComparableUnion() {
        List<Integer> union =
                Lists.union(new ArrayList<Integer>(), new ArrayList<Integer>());
        assertTrue(union.isEmpty());
    }

    @Test
    public void testOneElementComparatorUnion() {
        List<Integer> list = Arrays.asList(1);
        List<Integer> union =
                Lists.union(list, new ArrayList<>(), Integer::compare);
        assertEquals(list.size(), union.size());


        union = Lists.union(new ArrayList<>(), list, Integer::compare);
        assertEquals(list.size(), union.size());
    }

    @Test
    public void testOneElementComparableUnion() {
        List<Integer> list = Arrays.asList(1);
        List<Integer> union =
                Lists.union(list, new ArrayList<>());
        assertEquals(list.size(), union.size());


        union = Lists.union(new ArrayList<>(), list);
        assertEquals(list.size(), union.size());
    }

    @Test
    public void testComparableUnion() {
        List<Integer> left = new LinkedList<>(Arrays.asList(12, 0, -100));
        List<Integer> right = new ArrayList<>(Arrays.asList(100, 231, -100));
        List<Integer> union = Lists.union(left, right);

        assertEquals(left.size() + right.size(), union.size());
        Integer max = Math.max(left.stream().max(Integer::compare).get(), right.stream().max(Integer::compare).get());
        Integer min = Math.min(left.stream().min(Integer::compare).get(), right.stream().min(Integer::compare).get());
        assertEquals(max, union.get(union.size() - 1));
        assertEquals(min, union.get(0));
    }

    @Test
    public void tesComparatorUnion() {
        List<Integer> left = new LinkedList<>(Arrays.asList(12, 0, -100));
        List<Integer> right = new ArrayList<>(Arrays.asList(100, 231, -100));
        List<Integer> union = Lists.union(left, right, Integer::compare);

        assertEquals(left.size() + right.size(), union.size());
        Integer max = Math.max(left.stream().max(Integer::compare).get(), right.stream().max(Integer::compare).get());
        Integer min = Math.min(left.stream().min(Integer::compare).get(), right.stream().min(Integer::compare).get());
        assertEquals(max, union.get(union.size() - 1));
        assertEquals(min, union.get(0));
    }
}
