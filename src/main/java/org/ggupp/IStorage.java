package org.ggupp;

public interface IStorage<T, F> {
    void save(T t, F f);

    T load(F f);

    void delete(F f);

}