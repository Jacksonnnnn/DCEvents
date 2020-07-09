package com.Jacksonnn.DCEvents.Commands.SubCommands;

import com.Jacksonnn.DCEvents.Commands.EventSubCommand;
import com.Jacksonnn.DCEvents.Configuration.ConfigManager;
import com.Jacksonnn.DCEvents.Event;
import com.Jacksonnn.DCEvents.EventPlayer.EventPlayer;
import com.Jacksonnn.DCEvents.GeneralMethods;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Join implements EventSubCommand {
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public List<String> getAliases() {
        List<String> aliases = new ArrayList<>();

        aliases.add("");
        return aliases;
    }

    @Override
    public String getProperUse() {
        return "/dcevents join <eventName>";
    }

    @Override
    public String getDescription() {
        return ConfigManager.langConfig.get().getString("Events.CommandDescriptions.JoinCommand");
    }

    @Override
    public String getPermission() {
        return "DCEvents.Player.Join";
    }

    @Override
    public void execute(CommandSender sender, List<String> args) {
        if (sender instanceof Player) {
            Player player = (Player) sender;

            if (args.size() == 1) {
                String eventName = args.get(0);
                Event reqEvent = GeneralMethods.getEvent(eventName);

                if (reqEvent == null) {
                    sender.sendMessage(GeneralMethods.getEventsPrefix() + "Error! That is not an active event. Please check list for active events.");
                    return;
                }

                for (EventPlayer ePlayer : reqEvent.getEventPlayers()) {
                    if (ePlayer == player) {
                        sender.sendMessage(GeneralMethods.getEventsPrefix() + "Error! You are already apart of this event...");
                        return;
                    }
                }

                reqEvent.addPlayer(player);
                sender.sendMessage(GeneralMethods.getEventsPrefix() + "You have joined the " + reqEvent.getEventName() + " event!");

                Player host = reqEvent.getEventStaff();

                if (host == null || !host.isOnline()) {
                    Bukkit.getServer().getLogger().info(ChatColor.DARK_RED + "<DCEVENTS ERROR>: EventHost is not on to host event. Please end event and create a new instance with an active host.");
                    LocalDateTime timestamp = LocalDateTime.now();
                    sender.sendMessage(GeneralMethods.getEventsPrefix() + ChatColor.RED + "There has been a big error, please ask a staff member with console access to check logs for \'<DCEVENTS ERROR>\'. Timestamp: " + timestamp.getHour() + " : " + timestamp.getMinute() + " : " + timestamp.getSecond() + ".");

                    Bukkit.broadcast(ChatColor.BLUE + "[" + ChatColor.GREEN + ChatColor.BOLD + "EventHosts" + ChatColor.BLUE + "] " + ChatColor.YELLOW + "Player " + player.getName() + " has joined the " + reqEvent.getEventName() + " event.", "DCCore.AllowEvents");
                    return;
                }

                host.sendMessage(GeneralMethods.getEventsPrefix() + "Player " + player.getName() + " has joined the " + reqEvent.getEventName() + " event.");
            } else {
                sender.sendMessage(GeneralMethods.getEventsPrefix() + "Error! " + getProperUse());
            }
        } else {
            sender.sendMessage(GeneralMethods.getEventsPrefix() + "You must be a player to run this command!");
        }
    }
}
