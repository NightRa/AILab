package util;

import java.util.Iterator;

public class IntBuffer implements Iterable<Integer> {
    private int[] arr;
    private int size;

    public IntBuffer(int size) {
        this.arr = new int[size];
        this.size = 0;
    }

    public void add(int x) {
        // unchecked.
        arr[size] = x;
        size++;
    }

    public void clear() {
        size = 0;
    }

    public int get(int i) {
        if(i < 0 || i >= size){
            throw new IndexOutOfBoundsException("Size: " + size + ", Index: " + i);
        }
        return arr[i];
    }

    public int size() {
        return size;
    }

    @Override
    public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
            int i;
            @Override
            public boolean hasNext() {
                return i < size;
            }

            @Override
            public Integer next() {
                return get(i++);
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntBuffer other = (IntBuffer) o;

        if (size != other.size) return false;
        for (int i = 0; i < size; i++) {
            if(arr[i] != other.arr[i]) return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + arr[i];
        }
        result = 31 * result + size;
        return result;
    }
}
