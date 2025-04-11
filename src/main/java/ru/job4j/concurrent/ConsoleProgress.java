package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        while (!Thread.interrupted()) {
            for (int i = 0; i < 4; i++) {
            var process = new char[] {'-', '\\', '|', '/'};
            System.out.print("\rload: " + process[i]);
            }
        }
    }

    public static void main(String[] args) {
        Thread progress = new Thread(new ConsoleProgress());
        progress.start();
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
           Thread.currentThread().interrupt();
        }
        progress.interrupt();
    }
}
