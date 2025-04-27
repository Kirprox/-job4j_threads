package ru.job4j;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

class SimpleBlockingQueueTest {

    @Test
    void whenOffer() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(5);
        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
        List<Integer> result = new ArrayList<>();
        Thread consumer = new Thread(() -> {
            while (queue.getSize() != 0) {
                try {
                    queue.poll();
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        Thread producer = new Thread(() -> {
            for (int number : numbers) {
                try {
                    queue.offer(number);
                    result.add(number);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            consumer.interrupt();
        });
        producer.start();
        consumer.start();
        producer.join();
        consumer.join();
        assertThat(result).containsExactly(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    }
}