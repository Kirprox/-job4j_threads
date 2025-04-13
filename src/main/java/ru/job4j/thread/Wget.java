package ru.job4j.thread;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Wget implements Runnable {
    private final String url;
    private final int speed;

    public Wget(String url, int speed) {
        this.url = url;
        this.speed = speed;
    }

    @Override
    public void run() {
        var startAt = System.currentTimeMillis();
        var file = new File("tmp.xml");
        boolean isSpeedlimit = speed != 6000;
        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[1024];
            int bytesRead;
            long downSpeed;
            long currentSpeedNano;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                var downloadAt = System.nanoTime();
                output.write(dataBuffer, 0, bytesRead);
                currentSpeedNano = System.nanoTime() - downloadAt;
                System.out.println("Read 1024 bytes : " + currentSpeedNano + " nano.");
                downSpeed = Math.round(1024.0 / currentSpeedNano * 1_000_000);
                if (isSpeedlimit && downSpeed > speed) {
                    try {
                        Thread.sleep((downSpeed / speed));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        String url = args[0];
        int speed = Integer.parseInt(args[1]);
        validateUrlAndSpeed(url, speed);
        Thread wget = new Thread(new Wget(url, speed));
        wget.start();
        wget.join();
    }

    private static void validateUrlAndSpeed(String url, int speed) {
        String regex = "^(https?|ftp)://[^\\s/$.?#].[^\\s]*$";
        Pattern urlPattern = Pattern.compile(regex);
        Matcher matcher = urlPattern.matcher(url);
        if (!matcher.matches() || speed <= 0) {
            throw new IllegalArgumentException();
        }
    }
}