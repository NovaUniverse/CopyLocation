package net.novauniverse.copylocation;

import org.bukkit.plugin.java.JavaPlugin;

import net.novauniverse.copylocation.command.CopyLocationCommand;
import net.zeeraa.novacore.spigot.command.CommandRegistry;

public class CopyLocation extends JavaPlugin {
	@Override
	public void onEnable() {
		CommandRegistry.registerCommand(new CopyLocationCommand(this));
	}
}