package ru.job4j.pools;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

public class RolColSum {

    public static Sums[] sum(int[][] matrix) {
        int row = 0;
        int col = 0;
        Sums[] result = new Sums[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix.length; j++) {
                row += matrix[i][j];
                col += matrix[j][i];
            }
            result[i] = new Sums(row, col);
            row = 0;
            col = 0;
        }
        return result;
    }

    public static Sums[] asyncSum(int[][] matrix) throws ExecutionException, InterruptedException {
        Sums[] result = new Sums[matrix.length];
        CompletableFuture<Sums>[] futures = new CompletableFuture[matrix.length];
        for (int i = 0; i < matrix.length; i++) {
            final int index = i;
            futures[i] = CompletableFuture.supplyAsync(() -> {
                int row = 0;
                int col = 0;
                for (int j = 0; j < matrix.length; j++) {
                    row += matrix[index][j];
                    col += matrix[j][index];
                }
                return new Sums(row, col);
            });
        }
        CompletableFuture.allOf(futures).join();
        for (int i = 0; i < matrix.length; i++) {
            result[i] = futures[i].get();
        }
        return result;
    }

    public static void main(String[] args) {
        int[][] matrix = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };
        Sums[] sums = sum(matrix);
        for (Sums s : sums) {
            System.out.println(s.colSum());
            System.out.println(s.rowSum());
        }
    }
}