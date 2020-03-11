package fr.vi5team.vi5.commands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.Vi5Main;
public class Vi5BaseCommand implements CommandExecutor {

	private final Vi5Main mainref;
	
	public Vi5BaseCommand(Vi5Main main) {
		mainref=main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2, String[] args) {
		switch (args[0]) {
		case "game":
			gameCommand(sender,args);
			break;
		case "map":
			mapCommand(sender,args);
			break;
		case "glow":
			if (sender instanceof Player) {
				Player player = (Player)sender;
				mainref.packetGlowPlayer(player, Bukkit.getPlayer(args[1]));
			}
			break;
		}
		return false;
	}
	
	public void gameCommand(CommandSender sender,String[] args) {
		switch (args[1]) {
		case "create":
			break;
		case "start":
			break;
		case "restart":
			break;
		case "stop":
			break;
		case "join":
			break;
		case "leave":
			break;
		case "list":
			break;
		}
	}
	
	public void mapCommand(CommandSender sender,String[] args) {
		switch (args[1]) {
		case "create":
			break;
		case "rename":
			break;
		case "list":
			break;
		}
	}
}