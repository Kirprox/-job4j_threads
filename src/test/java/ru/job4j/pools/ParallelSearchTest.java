package ru.job4j.pools;

import org.junit.jupiter.api.Test;

import java.util.concurrent.ForkJoinPool;

import static org.assertj.core.api.Assertions.*;

class ParallelSearchTest {
    @Test
    public void whenNumers() {
        Integer[] array = {2, 1, 4, 6, 3, 10, 5, 8, 7, 9, 11};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertThat(forkJoinPool.invoke(new ParallelSearch<>(
                array, 0, array.length, 6)).equals(3));

    }

    @Test
    public void whenStrings() {
        String[] array = {
                "apple", "banana", "cherry", "date", "elderberry",
                "fig", "grape", "honeydew", "kiwi", "lemon",
                "mango", "nectarine", "orange"
        };
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertThat(forkJoinPool.invoke(new ParallelSearch<>(
                array, 0, array.length, "kiwi"))).isEqualTo(8);
    }

    @Test
    public void whenShortArrNumers() {
        Integer[] array = {2, 1, 4, 6, 3, 10};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertThat(forkJoinPool.invoke(new ParallelSearch<>(
                array, 0, array.length, 10))).isEqualTo(5);

    }

    @Test
    public void elementNotFound() {
        Integer[] array = {2, 1, 4, 6, 3, 10};
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        assertThat(forkJoinPool.invoke(new ParallelSearch<>(
                array, 0, array.length, 100))).isEqualTo(-1);

    }
}