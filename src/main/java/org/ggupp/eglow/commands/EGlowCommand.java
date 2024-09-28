package org.ggupp.eglow.commands;

import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.ggupp.Localization;
import org.ggupp.eglow.EGlowSection;
import org.ggupp.eglow.data.DataManager;
import org.ggupp.eglow.data.EGlowEffect;
import org.ggupp.eglow.data.EGlowPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.ggupp.util.GlobalUtils.sendPrefixedLocalizedMessage;

@RequiredArgsConstructor
public class EGlowCommand implements TabExecutor {
    private final EGlowSection main;

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {
        if(sender instanceof Player player) {

            if (!main.config().getBoolean("Eglow.enableCommands")) {
                sendPrefixedLocalizedMessage(player, "eglow_disable_commands");
                return false;
            }

            EGlowPlayer eGlowPlayer = DataManager.getEGlowPlayer(player);

            EGlowEffect eGlowEffect = null;
            EGlowEffect currentEGlowEffect = eGlowPlayer.getGlowEffect();

            switch (args.length) {
                case (1):
                    eGlowEffect = DataManager.getEGlowEffect(args[0].replace("off", "none").replace("disable", "none"));

                    if (eGlowEffect == null && currentEGlowEffect != null) {
                        if (currentEGlowEffect.getName().contains(args[0].toLowerCase())) {
                            eGlowEffect = switchEffectSpeed(currentEGlowEffect);
                        } else {
                            eGlowEffect = DataManager.getEGlowEffect(args[0].toLowerCase() + currentEGlowEffect.getName() + "slow");
                        }
                    }
                    break;
                case (2):
                    eGlowEffect = DataManager.getEGlowEffect(args[0] + args[1]);

                    if (eGlowEffect == null && currentEGlowEffect != null) {
                        if (currentEGlowEffect.getName().contains((args[0] + args[1]).toLowerCase()))
                            eGlowEffect = switchEffectSpeed(currentEGlowEffect);
                    }
                    break;
            }

            if (eGlowEffect == null) {
                sendPrefixedLocalizedMessage(eGlowPlayer.getPlayer(), "eglow_sync_error");
                return false;
            }

            if (eGlowEffect.getName().equals("none")) {
                eGlowPlayer.disableGlow();
                sendPrefixedLocalizedMessage(eGlowPlayer.getPlayer(), "eglow_disable");
            } else {
                if(!player.hasPermission(eGlowEffect.getPermissionNode())){
                    sendPrefixedLocalizedMessage(eGlowPlayer.getPlayer(), "eglow_not_permission_effect", Localization.getLocalization(player.getLocale()).get("eglow_color." + eGlowEffect.getName()));
                    return false;
                }
                if (eGlowPlayer.getGlowEffect() != null && eGlowPlayer.getGlowEffect().equals(eGlowEffect)) {
                    sendPrefixedLocalizedMessage(eGlowPlayer.getPlayer(), "eglow_same");
                    return false;
                }

                eGlowPlayer.setEffect(eGlowEffect);
                sendPrefixedLocalizedMessage(player, "eglow_set", Localization.getLocalization(player.getLocale()).get("eglow_color." + eGlowEffect.getName()));
            }
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String s, @NotNull String[] strings) {
        if (!main.config().getBoolean("Eglow.enableCommands")) {
            return List.of();
        }

        if (sender instanceof Player && cmd.getName().equalsIgnoreCase("eglow")) {
            ArrayList<String> suggestions = new ArrayList<>();
            ArrayList<String> finalSuggestions = new ArrayList<>();

            switch (strings.length) {
                case (1):
                    for (EGlowEffect effect : DataManager.getEGlowEffects()) {
                        String name = effect.getName().replace("slow", "").replace("fast", "");

                        if (!name.contains("blink") && sender.hasPermission(effect.getPermissionNode()))
                            suggestions.add(name);
                    }
                    suggestions.add("off");
                    suggestions.add("disable");

                    StringUtil.copyPartialMatches(strings[0], suggestions, finalSuggestions);
                    break;
                case (2):
                    if (strings[0].equalsIgnoreCase("rainbow"))
                        suggestions = new ArrayList<>(Arrays.asList("slow", "fast"));

                    StringUtil.copyPartialMatches(strings[1], suggestions, finalSuggestions);
                    break;
                default:
                    return suggestions;
            }
            if (!finalSuggestions.isEmpty())
                return finalSuggestions;
            return suggestions;
        }

        return List.of();
    }

    private EGlowEffect switchEffectSpeed(EGlowEffect eGlowEffect) {
        String effectName = eGlowEffect.getName();

        if (effectName.contains("slow")) {
            return DataManager.getEGlowEffect(effectName.replace("slow", "fast"));
        } else {
            return DataManager.getEGlowEffect(effectName.replace("fast", "slow"));
        }
    }
}
