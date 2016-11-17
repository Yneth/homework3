package ua.abond.generics.util;

import java.util.*;

public final class Lists {

    private Lists() {

    }

    public static <T> List<T> union(List<? extends T> a, List<? extends T> b,
                                    Comparator<? super T> cmp) {
        return sort(listUnion(a, b), cmp);
    }

    public static <T extends Comparable<? super T>> List<T> union(
            List<? extends T> a, List<? extends T> b) {
        List<T> result = listUnion(a, b);
        result.sort(null);
        return result;
    }

    private static <T> List<T> listUnion(List<? extends T> a, List<? extends T> b) {
        Objects.requireNonNull(a);
        Objects.requireNonNull(b);

        List<T> result = new ArrayList<>(a);
        result.addAll(b);
        return result;
    }

    public static <T> List<T> sort(List<T> toSort, Comparator<? super T> cmp) {
        Objects.requireNonNull(toSort);

        if (toSort.size() <= 1) {
            return toSort;
        }

        T[] array = (T[]) new Object[toSort.size()];
        toSort.toArray(array);

        if (cmp == null) {
            if (!(toSort.get(0) instanceof Comparable)) {
                throw new IllegalArgumentException("Elements of the list should implement Comparable");
            }
            sortInnerComparable(array, 0, toSort.size() - 1);
        } else {
            sortInner(array, 0, toSort.size() - 1, cmp);
        }

        ListIterator<T> iterator = toSort.listIterator();
        for (T a : array) {
            iterator.next();
            iterator.set(a);
        }
        return toSort;
    }

    private static <T> void sortInnerComparable(T[] toSort, int lo, int hi) {
        if (hi <= lo) {
            return;
        }

        int j = partition(toSort, lo, hi);
        sortInnerComparable(toSort, lo, j - 1);
        sortInnerComparable(toSort, j + 1, hi);
    }

    private static <T> int partition(T[] toSort, int lo, int hi) {
        int i = lo;
        int j = hi + 1;
        T val = toSort[lo];
        while (true) {
            while (less((Comparable<? super T>) toSort[++i], val))
                if (i >= hi)
                    break;

            while (less((Comparable<? super T>) val, toSort[--j]))
                if (j <= lo)
                    break;

            if (i >= j)
                break;

            swap(toSort, i, j);
        }
        swap(toSort, lo, j);
        return j;
    }

    private static <T> void sortInner(T[] toSort, int lo, int hi, Comparator<? super T> cmp) {
        if (hi <= lo)
            return;

        int j = partition(toSort, lo, hi, cmp);
        sortInner(toSort, lo, j - 1, cmp);
        sortInner(toSort, j + 1, hi, cmp);
    }

    private static <T> int partition(T[] toSort, int lo, int hi, Comparator<? super T> cmp) {
        int i = lo;
        int j = hi + 1;
        T val = toSort[lo];
        while (true) {
            while (less(toSort[++i], val, cmp))
                if (i >= hi)
                    break;

            while (less(val, toSort[--j], cmp))
                if (j <= lo)
                    break;

            if (i >= j)
                break;

            swap(toSort, i, j);
        }
        swap(toSort, lo, j);
        return j;
    }

    private static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    private static <T> void sortInner(List<T> toSort, int lo, int hi, Comparator<? super T> cmp) {
        if (hi <= lo)
            return;

        int j = partition(toSort, lo, hi, cmp);
        sortInner(toSort, lo, j - 1, cmp);
        sortInner(toSort, j + 1, hi, cmp);
    }

    private static <T> int partition(List<T> toSort, int lo, int hi, Comparator<? super T> cmp) {
        int i = lo;
        int j = hi + 1;
        ListIterator<T> forward = toSort.listIterator(lo);
        ListIterator<T> backward = toSort.listIterator(hi + 1);
        T v = forward.next();
        while (true) {
            while (++i <= hi && less(forward.next(), v, cmp)) ;

            while (--j >= lo && less(v, backward.previous(), cmp)) ;

            if (i >= j)
                break;

            Collections.swap(toSort, i, j);
        }
        Collections.swap(toSort, lo, j);

        return j;
    }

    private static <T> boolean less(T t0, T t1, Comparator<? super T> comparator) {
        return comparator.compare(t0, t1) < 0;
    }

    private static <T> boolean equal(T t0, T t1, Comparator<? super T> cmp) {
        return cmp.compare(t0, t1) == 0;
    }

    private static <T> boolean lessOrEqual(T t0, T t1, Comparator<? super T> cmp) {
        return less(t0, t1, cmp) || equal(t0, t1, cmp);
    }

    private static <T> boolean less(Comparable<? super T> a, T b) {
        return a.compareTo(b) < 0;
    }

    private static <T> boolean equal(Comparable<? super T> t0, T t1) {
        return t0.compareTo(t1) == 0;
    }

    private static <T> boolean lessOrEqual(Comparable<? super T> t0, T t1) {
        return less(t0, t1) || equal(t0, t1);
    }

    public static <T> boolean isSorted(List<T> list, Comparator<? super T> cmp) {
        if (list.size() <= 1) {
            return true;
        }
        ListIterator<T> iterator = list.listIterator();
        T current = nextOrNull(iterator);
        do {
            T next = nextOrNull(iterator);
            if (Objects.isNull(current) || Objects.isNull(next)) {
                return false;
            }
            if (!lessOrEqual(current, next, cmp)) {
                return false;
            }
            current = next;
        } while (iterator.hasNext());
        return true;
    }

    public static <T extends Comparable<? super T>> boolean isSorted(List<? extends T> list) {
        ListIterator<? extends T> iterator = list.listIterator();
        Comparable<T> current = (Comparable<T>) nextOrNull(iterator);
        do {
            T next = nextOrNull(iterator);
            if (Objects.isNull(current) || Objects.isNull(next)) {
                return false;
            }
            if (!lessOrEqual(current, next)) {
                return false;
            }
            current = (Comparable<T>) next;
        } while (iterator.hasNext());
        return true;
    }

    private static <T> T nextOrNull(ListIterator<T> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
