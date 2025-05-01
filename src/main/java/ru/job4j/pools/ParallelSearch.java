package ru.job4j.pools;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class ParallelSearch<T> extends RecursiveTask<Integer> {
    private final T[] array;
    private final int from;
    private final int to;
    private final T target;

    public ParallelSearch(T[] array, int from, int to, T target) {
        this.array = array;
        this.from = from;
        this.to = to;
        this.target = target;
    }

    public static void main(String[] args) {
        Integer[] array = {2, 1, 4, 6, 3, 10, 5, 8, 7, 9, 0, 99};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        System.out.println((forkJoinPool.invoke(
                new ParallelSearch(array, 0, array.length, 10))));
    }

    @Override
    protected Integer compute() {
        int result = -1;
        if (to - from <= 10) {
            for (int i = from; i < to; i++) {
                if (array[i] == target) {
                    result = i;
                    break;
                }
            }
        } else {
            int middle = (from + to) / 2;
            ParallelSearch<T> leftSearch =
                    new ParallelSearch<>(array, from, middle, target);
            ParallelSearch<T> rightSearch =
                    new ParallelSearch<>(array, middle, to, target);
            leftSearch.fork();
            rightSearch.fork();
            int left = leftSearch.join();
            int right = rightSearch.join();
            if (left != -1) {
                result = left;
            } else if (right != -1) {
                result = right;
            }
        }
        return result;
    }
}
