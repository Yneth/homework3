package ua.abond.generics.queue;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class PriorityQueueTest {

    @Test
    public void testIsEmpty() throws Exception {
        assertTrue(new PriorityQueue<Integer>().isEmpty());
        assertTrue(new PriorityQueue<Integer>(12).isEmpty());

        assertTrue(new PriorityQueue<Integer>().size() == 0);
    }

    @Test
    public void testIsNotEmpty() throws Exception {
        assertFalse(new PriorityQueue<>(Arrays.asList(1)).isEmpty());
    }

    @Test
    public void testClear() {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(1, 2, 3));
        pq.clear();
        assertTrue(pq.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testElementEmptyQueue() {
        new PriorityQueue<>().element();
    }

    @Test(expected = NoSuchElementException.class)
    public void testElementRemoveEmptyQueue() {
        new PriorityQueue<>().remove();
    }

    @Test(expected = NullPointerException.class)
    public void testAddNull() throws Exception {
        new PriorityQueue<>().add(null);
    }

    @Test
    public void testAdd() throws Exception {
        PriorityQueue<Integer> pq = new PriorityQueue<>();
        pq.add(10);
        pq.add(9);
        assertFalse(pq.isEmpty());
        assertTrue(pq.size() == 2);
        assertEquals(new Integer(10), pq.peek());
    }

    @Test
    public void testPeek() throws Exception {
        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(12, 11, 10, -2));
        assertEquals(new Integer(12), pq.peek());
    }

    @Test
    public void testPoll() throws Exception {
        List<Integer> list = Arrays.asList(-1, -2, -3, -4);
        PriorityQueue<Integer> pq = new PriorityQueue<>(list);

        assertEquals(pq.peek(), pq.poll());
        assertEquals(list.size() - 1, pq.size());
    }

    @Test
    public void testRemove() {
        Integer[] array = {1, 2, 3};
        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(1, 2, 3));

        assertTrue(pq.remove(1));

        assertEquals(array.length - 1, pq.size());
        assertEquals(Arrays.stream(array).max(Integer::compare).get(), pq.peek());
    }

    @Test
    public void testRemoveEmpty() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        assertFalse(pq.remove(1));
    }

    @Test
    public void testRemoveOneElement() {
        PriorityQueue<Integer> pq = new PriorityQueue<>();

        assertFalse(pq.remove(1));
        assertTrue(pq.isEmpty());
    }

    @Test
    public void testIteratorOnEmpty() {
        boolean looped = false;
        for (Integer i : new PriorityQueue<Integer>()) {
            looped = true;
        }
        assertFalse(looped);
    }

    @Test
    public void testIteratorOneElement() {
        Integer val = 1;
        boolean looped = false;
        for (Integer i : new PriorityQueue<>(Arrays.asList(val))) {
            assertEquals(val, i);
            looped = true;
        }
        assertTrue(looped);
    }

    @Test
    public void testContains() {
        Integer[] array = {213, 21, 22, 1};
        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(array));
        for (Integer i : array) {
            assertTrue(pq.contains(i));
        }
    }

    @Test
    public void testIterator() {
        Integer[] array = {4, 3, 2, 1};
        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(array));

        int index = 0;
        for (Integer i : pq) {
            assertEquals(array[index++], i);
        }
        assertEquals(array.length, index);
    }
}