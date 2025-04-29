package ru.job4j.cache;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Stream;

public class Cache {
    private final Map<Integer, Base> memory = new ConcurrentHashMap<>();

    public boolean add(Base model) {
        return memory.putIfAbsent(model.id(), model) == null;
    }

    public boolean update(Base model) throws OptimisticException {
        return memory.computeIfPresent(model.id(), (k, v) -> {
            if (v.version() != model.version()) {
                throw new OptimisticException("Versions are not equal");
            }
            int newVersion = v.version() + 1;
            return new Base(k, model.name(), newVersion);
        }) != null;
    }

    public void delete(int id) {
        Base temp = memory.get(id);
        if (temp != null) {
            memory.remove(id, temp);
        }
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
