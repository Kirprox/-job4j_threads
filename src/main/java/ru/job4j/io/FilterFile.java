package ru.job4j.io;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;

public class FilterFile {
    private final File file;

    public FilterFile(File file) {
        this.file = file;
    }

    public String getContent(Predicate<Character> filter) throws IOException {
        try (InputStream input = new FileInputStream(file)) {
            StringBuilder output = new StringBuilder();
            int data;
            while ((data = input.read()) > 0) {
                char character = (char) data;
                if (filter.test(character)) {
                    output.append(character);
                }
            }
            return output.toString();
        }
    }

    public String getContent() throws IOException {
        return getContent(x -> true);
    }

    public String getContentWithoutUnicode() throws IOException {
        return getContent(x -> x < 0x80);
    }

}
