package org.ggupp.command.commands;

import org.ggupp.Localization;
import org.ggupp.Main;
import org.ggupp.command.BaseCommand;
import org.ggupp.util.GlobalUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;
import static org.ggupp.util.GlobalUtils.translateChars;


public class TpsinfoCommand extends BaseCommand {
    private final Main plugin;
    public TpsinfoCommand(Main plugin) {
        super(
                "tpsinfo",
                "/tpsinfo",
                "2d2dcore.tpsinfo",
                "Show TPS information");
        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("This command can only be used by players.");
            return;
        }

        if(!sender.hasPermission("2d2dcore.tpsinfo") && !sender.isOp()){
            sendPrefixedLocalizedMessage(player,"tps_failed");
            return;
        }


        double tps = GlobalUtils.getCurrentRegionTps();
        double mspt = getServerMSPT();
        int onlinePlayers = plugin.getServer().getOnlinePlayers().size();

        Localization loc = Localization.getLocalization(player.locale().getLanguage());
        String tpsMsg = String.join("\n", loc.getStringList("TpsMessage"));
        String strTps = String.format("%.2f", tps);
        String strMspt = String.format("%.2f", mspt);
        getLowestRegionTPS().thenAccept(lowestTPS -> player.sendMessage( translateChars(String.format(tpsMsg, strTps, strMspt, String.format("%.2f", lowestTPS), onlinePlayers))));

    }

    private double getServerMSPT() {
        return 1000.0 / GlobalUtils.getCurrentRegionTps();
    }

    private CompletableFuture<Double> getLowestRegionTPS() {
        List<CompletableFuture<Double>> futures = new ArrayList<>();

        for (Player player : Bukkit.getOnlinePlayers()) {
            futures.add(GlobalUtils.getRegionTps(player.getLocation()));
        }

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> {
                    double lowestTPS = Double.MAX_VALUE;
                    for (CompletableFuture<Double> future : futures) {
                        try {
                            double regionTPS = future.get();
                            if (regionTPS < lowestTPS) {
                                lowestTPS = regionTPS;
                            }
                        } catch (InterruptedException | ExecutionException e) {
                            //ignore
                        }
                    }
                    return lowestTPS;
                });
    }
}
