package net.novauniverse.copylocation.command;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionDefault;
import org.bukkit.plugin.Plugin;
import org.json.JSONObject;

import net.zeeraa.novacore.spigot.command.AllowedSenders;
import net.zeeraa.novacore.spigot.command.NovaCommand;
import net.zeeraa.novacore.spigot.utils.LocationUtils;

public class CopyLocationCommand extends NovaCommand {
	public CopyLocationCommand(Plugin owner) {
		super("copylocation", owner);

		setAliases(generateAliasList("cl"));
		setAllowedSenders(AllowedSenders.PLAYERS);
		setPermission("copylocation.copylocation");
		setPermissionDefaultValue(PermissionDefault.OP);
		setPermissionDescription("Allow the player to use the copy location command");
		setUsage("/copylocation [indent] [-v | --verbose] [-c | --center]");
		setDescription("Copy the player location as json to the server clipboard");
		addHelpSubCommand();
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		int indent = 4;
		boolean verbose = false;
		boolean center = false;

		if (args.length > 0) {
			try {
				if (args[0].equalsIgnoreCase("-v") || args[0].equalsIgnoreCase("--verbose")) {
					verbose = true;
				} else if (args[0].equalsIgnoreCase("-c") || args[0].equalsIgnoreCase("--center")) {
					center = true;
				} else {
					indent = Integer.parseInt(args[0]);
				}
			} catch (Exception e) {
				sender.sendMessage(ChatColor.RED + "Please provide a valid number");
				return false;
			}
		}

		for (int i = 0; i < args.length; i++) {
			if (args[i].equalsIgnoreCase("-v") || args[i].equalsIgnoreCase("--verbose")) {
				verbose = true;
			} else if (args[i].equalsIgnoreCase("-c") || args[i].equalsIgnoreCase("--center")) {
				center = true;
			}
		}

		Player player = (Player) sender;

		Location location = player.getLocation();

		JSONObject json = new JSONObject();

		double x;
		double y = location.getY();
		double z;

		if (center) {
			x = LocationUtils.blockCenter(location.getBlockX());
			z = LocationUtils.blockCenter(location.getBlockZ());
		} else {
			x = location.getX();
			z = location.getZ();
		}

		json.put("x", x);
		json.put("y", y);
		json.put("z", z);

		json.put("yaw", location.getYaw());
		json.put("pitch", location.getPitch());

		String string;

		if (indent == -1) {
			string = json.toString();
		} else {
			string = json.toString(indent);
		}

		StringSelection stringSelection = new StringSelection(string);

		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		clipboard.setContents(stringSelection, null);

		player.sendMessage(ChatColor.GREEN + "ok");

		if (verbose) {
			player.sendMessage(ChatColor.AQUA + string);
		}

		return true;
	}
}