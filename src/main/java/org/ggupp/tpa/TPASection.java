package org.ggupp.tpa;

import lombok.RequiredArgsConstructor;
import org.ggupp.Main;
import org.ggupp.Section;
import org.ggupp.tpa.commands.TPAAcceptCommand;
import org.ggupp.tpa.commands.TPACommand;
import org.ggupp.tpa.commands.TPADenyCommand;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.ggupp.util.GlobalUtils.info;
import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class TPASection implements Section {
    private final Main plugin;
    private static final HashMap<Player, Player> lastRequest = new HashMap<>();
    private static final HashMap<Player, List<Player>> requests = new HashMap<>();
    private ConfigurationSection config;

    @Override
    public void enable() {
        config = plugin.getSectionConfig(this);
        plugin.register(new LeaveListener(this));
        plugin.getCommand("tpa").setExecutor(new TPACommand(this));
        plugin.getCommand("tpayes").setExecutor(new TPAAcceptCommand(this));
        plugin.getCommand("tpano").setExecutor(new TPADenyCommand(this));
    }

    @Override
    public void disable() {

    }

    @Override
    public void reloadConfig() {
        config = plugin.getSectionConfig(this);
    }

    @Override
    public String getName() {
        return "TPA";
    }

    public void registerRequest(Player requester, Player requested) {
        lastRequest.put(requested, requester);
        if (requests.get(requested) == null) {
            requests.put(requested, new ArrayList<>());
            requests.get(requested).add(requester);
        } else requests.get(requested).add(requester);

        plugin.getExecutorService().schedule(() -> {
            if (!requests.get(requested).contains(requester)) return;
            sendPrefixedLocalizedMessage(requested, "tpa_request_timeout");
            sendPrefixedLocalizedMessage(requester, "tpa_request_timeout");
            lastRequest.remove(requested);
            requests.get(requested).remove(requester);
            if (!requests.get(requested).isEmpty()) lastRequest.put(requested, requests.get(requested).get(0));
        }, config.getInt("RequestTimeout"), TimeUnit.MINUTES);
    }

    public Player getLastRequest(Player requested) {
        return lastRequest.get(requested);
    }

    public boolean hasRequested(Player requester, Player requested) {
        if (requests.get(requested) == null) return false;
        if (!requests.get(requested).contains(requester)) return false;
        return requests.get(requested).contains(requester);
    }

    public void removeRequest(Player requester, Player requested) {
        if (requests.get(requested).indexOf(requester) == 0) {
            requests.get(requested).remove(0);
            if (requests.get(requested).size() > 1) {
                lastRequest.put(requested, requests.get(requested).get(1));
            } else lastRequest.remove(requested);
        } else requests.get(requested).remove(requester);
    }

    public List<Player> getRequests(Player to) {
        requests.computeIfAbsent(to, k -> new ArrayList<>());
        return requests.get(to);
    }
}
