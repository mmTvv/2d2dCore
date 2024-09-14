package org.ggupp.kitstart;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
public class KitStartData {
    private Location platformPos1;
    private Location platformPos2;
    private Location TeleportPos;
}
