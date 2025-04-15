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
        String[] path = url.split("/");
        var file = new File(path[path.length - 1]);

        try (var input = new URL(url).openStream();
             var output = new FileOutputStream(file)) {
            System.out.println("Open connection: " + (System.currentTimeMillis() - startAt) + " ms");
            var dataBuffer = new byte[1024];
            int bytesRead;
            startAt = System.currentTimeMillis();
            int downBytes = 0;
            while ((bytesRead = input.read(dataBuffer, 0, dataBuffer.length)) != -1) {
                downBytes += bytesRead;
                if (downBytes > speed) {
                    long downloadTime = System.currentTimeMillis() - startAt;
                    if (downloadTime < 1000) {
                        Thread.sleep(1000 - downloadTime);
                    }
                    startAt = System.currentTimeMillis();
                    downBytes = 0;
                }
                output.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        try {
            System.out.println(Files.size(file.toPath()) + " bytes");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        if (args.length < 2) {
            throw new IllegalArgumentException();
        }
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