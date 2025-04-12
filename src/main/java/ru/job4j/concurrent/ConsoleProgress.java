package ru.job4j.concurrent;

public class ConsoleProgress implements Runnable {

    @Override
    public void run() {
        var process = new char[]{'-', '\\', '|', '/'};
        while (!Thread.interrupted()) {
            for (char element : process) {
                System.out.print("\rload: " + element);
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
