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
        Base stored = memory.get(model.id());
        if (stored.version() != model.version()) {
            throw new OptimisticException("Versions are not equal");
        }
        int newVersion = stored.version() + 1;
        Base newBase = new Base(model.id(), model.name(), newVersion);
        int id = model.id();
        return memory.computeIfPresent(id, (k, v) -> newBase) != null;
    }

    public void delete(int id) {
        Optional<Base> optionalBase = findById(id);
        if (optionalBase.isPresent()) {
            Base temp = optionalBase.get();
            memory.remove(id, temp);
        }
    }

    public Optional<Base> findById(int id) {
        return Stream.of(memory.get(id))
                .filter(Objects::nonNull)
                .findFirst();
    }
}
