package org.ggupp.tablist.worker;

import lombok.RequiredArgsConstructor;
import org.bukkit.Bukkit;
import org.ggupp.tablist.TabSection;


@RequiredArgsConstructor
public class TabWorker implements Runnable {
    private final TabSection main;
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(main::setTab);
    }
}
