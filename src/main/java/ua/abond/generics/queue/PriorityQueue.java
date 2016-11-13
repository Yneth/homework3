package ua.abond.generics.queue;

import java.util.*;

public class PriorityQueue<T extends Comparable<? super T>> implements Iterable<T> {
    private static final int DEFAULT_CAPACITY = 16;

    private int size = 0;
    private T[] queue;

    public PriorityQueue() {
        this(DEFAULT_CAPACITY);
    }

    public PriorityQueue(int capacity) {
        this.size = 0;
        this.queue = (T[]) new Comparable[capacity];
    }

    public PriorityQueue(Collection<T> collection) {
        this(collection.size());
        collection.forEach(this::add);
    }

    public boolean add(T elem) {
        Objects.requireNonNull(elem);

        resize();

        queue[++size] = elem;
        swim(size);
        return true;
    }

    public boolean remove(Object o) {
        if (o == null) {
            return false;
        }
        int i = indexOf((T) o);
        if (i < 0) {
            return false;
        }
        remove(i);
        return true;
    }

    public void clear() {
        Arrays.fill(queue, null);
        size = 0;
    }

    public boolean offer(T t) {
        return add(t);
    }

    public T remove() {
        T elem = poll();
        if (elem == null) {
            throw new NoSuchElementException();
        }
        return elem;
    }

    public T peek() {
        if (isEmpty()) {
            return null;
        }
        return queue[1];
    }

    public T poll() {
        T elem = queue[1];
        remove(1);
        return elem;
    }

    public T element() {
        T elem = peek();
        if (elem == null) {
            throw new NoSuchElementException();
        }
        return elem;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public boolean contains(T o) {
        return indexOf(o) != -1;
    }

    public Object[] toArray() {
        return Arrays.copyOf(queue, size);
    }

    @Override
    public Iterator<T> iterator() {
        return new PriorityQueueIterator();
    }

    private T remove(int index) {
        T elem = queue[index];

        swap(index, size--);
        sink(index);
        queue[size + 1] = null;

        return elem;
    }

    private int indexOf(T o) {
        if (o != null) {
            for (int i = 1; i <= size; i++) {
                if (o.compareTo(queue[i]) == 0) {
                    return i;
                }
            }
        }
        return -1;
    }

    private void resize() {
        if (size != queue.length - 1) {
            return;
        }

        int newSize = queue.length << 1;
        queue = Arrays.copyOf(queue, newSize);
    }

    private void sink(int index) {
        int i = index;

        while (i << 1 <= size) {
            int j = i << 1;
            if (j < size && less(j, j + 1))
                j++;
            if (!less(i, j))
                break;
            swap(i, j);
            i = j;
        }
    }

    private void swim(int index) {
        int i = index;
        while (i > 1 && less(i >> 1, i)) {
            swap(i, i >> 1);
            i = i >> 1;
        }
    }

    private void swap(int i, int j) {
        T temp = queue[i];
        queue[i] = queue[j];
        queue[j] = temp;
    }

    private boolean less(int i0, int i1) {
        return queue[i0].compareTo(queue[i1]) < 0;
    }

    public class PriorityQueueIterator implements Iterator<T> {
        private int current = 1;

        @Override
        public boolean hasNext() {
            return (current - 1) < size;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return queue[current++];
        }
    }
}
