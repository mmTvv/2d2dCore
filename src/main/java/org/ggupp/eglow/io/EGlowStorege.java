package org.ggupp.eglow.io;

public interface EGlowStorege<T>{
    void save(T t);

    void load(T f);

    void delete(T f);
}
