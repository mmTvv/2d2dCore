package org.ggupp.tablist.worker;

import lombok.RequiredArgsConstructor;
import org.ggupp.tablist.TabSection;
import org.bukkit.Bukkit;


@RequiredArgsConstructor
public class TabWorker implements Runnable {
    private final TabSection main;
    @Override
    public void run() {
        Bukkit.getOnlinePlayers().forEach(main::setTab);
    }
}
