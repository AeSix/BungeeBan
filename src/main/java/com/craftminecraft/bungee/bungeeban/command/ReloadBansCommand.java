package com.craftminecraft.bungee.bungeeban.command;

import com.craftminecraft.bungee.bungeeban.BanManager;

import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.plugin.Command;

public class ReloadBansCommand extends Command {

	public ReloadBansCommand() {
		super("reloadbans", "bungeeban.command.reloadbans");
	}

	@Override
	public void execute(CommandSender sender, String[] args) {
		BanManager.reload();
	}
}