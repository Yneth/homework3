package ua.abond.generics.util;

import java.util.*;

public final class Lists {

    private Lists() {

    }

    public static <T extends Comparable<? super T>> List<T> union(List<T> a, List<T> b) {
        List<T> result = listUnion(a, b);
        result.sort(null);
        return result;
    }

    public static <T> List<T> union(List<? extends T> a, List<? extends T> b,
                                    Comparator<? super T> cmp) {
        Objects.requireNonNull(cmp);
        return sort(listUnion(a, b), cmp);
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
        Objects.requireNonNull(cmp);

        sortInner(toSort, 0, toSort.size() - 1, cmp);
        return toSort;
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

    private static <T> T nextOrNull(ListIterator<T> iterator) {
        return iterator.hasNext() ? iterator.next() : null;
    }
}
