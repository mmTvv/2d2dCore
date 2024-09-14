package org.ggupp.command.commands;

import org.ggupp.command.BaseCommand;
import org.ggupp.command.CommandSection;
import org.bukkit.command.CommandSender;

import static org.ggupp.util.GlobalUtils.sendMessage;

public class BaseCmd extends BaseCommand {
    private final CommandSection main;
    public BaseCmd(CommandSection main) {
        super("ef",
                "/ef reload | version | help",
                "2d2dcore.command.ef",
                "Base command of the plugin",
                new String[]{"reload::Reload the config file", "version::Show the version of the plugin", "help::Shows the help for the plugin"});
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 0) {
            switch (args[0]) {
                case "reload":
                    main.getPlugin().reloadConfig();
                    sendMessage(sender, "&aSuccessfully reloaded configuration file");
                    break;
                case "version":
                    sendMessage(sender, "&dVersion &r&5" + main.getPlugin().getPluginMeta().getVersion());
                    break;
                case "help":
                    sendMessage(sender, "&5--- " + PREFIX + " &dHelp &r&5---");
                    main.getCommandHandler().getCommands().forEach(command -> {
                        String helpMsg = "&5---&r&d&l /" + command.getName() + "&r Help &r&1---";
                        sendMessage(sender, helpMsg);
                        sendMessage(sender, "&dDescription: " + command.getDescription());
                        if (command.getSubCommands() != null) {
                            if (command.getSubCommands().length > 0) {
                                for (String subCommand : command.getSubCommands()) {
                                    String[] split = subCommand.split("::");
                                    if (split.length > 0) {
                                        sendMessage(sender, "&d/" + command.getName() + " " + split[0] + " |&r&d " + split[1]);
                                    } else sendMessage(sender, "&d/" + command.getName() + " " + subCommand);
                                }
                                sendMessage(sender, "&5--------------------");
                            }
                        }
                        sendMessage(sender, "");
                    });
                    break;
                default:
                    sendMessage(sender, "&dUnknown command&r&5 %s&5", args[0]);
                    break;
            }
        } else sendErrorMessage(sender, getUsage());
    }
}
