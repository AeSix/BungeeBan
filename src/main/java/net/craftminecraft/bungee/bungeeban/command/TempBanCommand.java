package net.craftminecraft.bungee.bungeeban.command;

import java.text.ParseException;
import java.text.SimpleDateFormat;



import net.craftminecraft.bungee.bungeeban.BanManager;
import net.craftminecraft.bungee.bungeeban.banstore.BanEntry;
import net.craftminecraft.bungee.bungeeban.util.MainConfig;
import net.craftminecraft.bungee.bungeeban.util.Utils;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class TempBanCommand extends Command {

	public TempBanCommand() {
		super("tempban");
	}
	

	@Override
	public void execute(CommandSender sender, String[] args) {
		ProxiedPlayer player = null;
		BanEntry.Builder newban;
		
		if (sender instanceof ProxiedPlayer) {
			player = (ProxiedPlayer) sender;
			if (args.length < 1) {
				sender.sendMessage(ChatColor.RED + "Wrong command format. <required> [optional]");
				sender.sendMessage(ChatColor.RED + "/tempban <username> [time] [server] [reason]");
				return;
			}
		} else {
			sender.sendMessage(ChatColor.RED + "Console can't local-ban yet. Get your ass in-game.");
			if (args.length < 2) {
				sender.sendMessage(ChatColor.RED + "Wrong command format. <required> [optional]");
				sender.sendMessage(ChatColor.RED + "/tempban <username> [time] <server> [reason]");
				return;
			}
		}

		newban = new BanEntry.Builder(args[0])
					.source(sender.getName())
					.expiry();
		
		if (args.length > 1) {
			String reason;
			try {
				// If first argument is time.
				newban.expiry(Utils.parseDate(args[1]));
				if (ProxyServer.getInstance().getServerInfo(args[2]) != null) {
					newban.server(args[2]);
					reason = Utils.buildReason(args, 3);
				} else if (player != null) {
					newban.server(player.getServer().getInfo().getName());
					reason = Utils.buildReason(args, 2);
				} else {
					// console sender
					sender.sendMessage(ChatColor.RED + args[1] + " is not a valid server.");
					return;
				}

			} catch (IllegalArgumentException ex) {
				// If first argument ISN'T a time.
				if (ProxyServer.getInstance().getServerInfo(args[1]) != null) {
					newban.server(args[1]);
					reason = Utils.buildReason(args, 2);
				} else if (player != null) {
					newban.server(player.getServer().getInfo().getName());
					reason = Utils.buildReason(args, 1);
				} else {
					// Console sender
					sender.sendMessage(ChatColor.RED + args[1] + " is not a valid server.");
					return;
				}
			}
			if (!reason.isEmpty()) {
				newban.reason(reason);
			}
		}
		
		BanEntry entry = newban.build();
		if (!Utils.hasPermission(sender, "tempban", entry.getServer())) {
			sender.sendMessage(ChatColor.RED + "You don't have permission to do this.");
		}
		
		BanManager.ban(entry);
	}
}