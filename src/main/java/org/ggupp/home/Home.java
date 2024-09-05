package org.ggupp.home;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class Home {
    private final String name;
    private Location location;
}
