package org.ggupp.home;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Stream;

@Getter
@RequiredArgsConstructor
public class HomeData {
    private final List<Home> homes;
    public void addHome(Home home) {
        homes.add(home);
    }

    public void deleteHome(Home home) {
        homes.remove(home);
    }
    public Stream<Home> stream() {
        return homes.stream();
    }
    
   public boolean hasHomes() {
        return !homes.isEmpty();
   }
}
