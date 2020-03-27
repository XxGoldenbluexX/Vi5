package fr.vi5team.vi5.commands;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import fr.vi5team.vi5.Game;
import fr.vi5team.vi5.Vi5Main;
public class Vi5BaseCommand implements CommandExecutor {

	private final Vi5Main mainref;
	
	public Vi5BaseCommand(Vi5Main main) {
		mainref=main;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command, String arg2, String[] args) {
		if (args.length<1) {
			return false;
		}
		switch (args[0]) {
		case "game":
			 return gameCommand(sender,args);
		case "map":
			return mapCommand(sender,args);
		case "glow":
			if (sender instanceof Player) {
				Player player = (Player)sender;
				mainref.packetGlowPlayer(player, Bukkit.getPlayer(args[1]));
			}
			break;
		}
		return false;
	}
	
	public boolean gameCommand(CommandSender sender,String[] args) {
		if (args.length<2) {
			sender.sendMessage("usage: /vi5 game create/start/fstart/setMap/list");
			return true;
		}
		switch (args[1]) {
		case "create":
			if (args.length>2) {
				Game g = mainref.createGame(args[2]);
				if (sender instanceof Player) {
					Player p = (Player)sender;
					if (!mainref.isPlayerIngame(p)) {
						g.addPlayer((Player)sender);
					}
				}
				return true;
			}else {
				sender.sendMessage("usage: /vi5 game create <GameName>");
				return true;
			}
		case "start":
			if (args.length>2) {
				Game g = mainref.getGame(args[2]);
				if (g!=null) {
					if (g.is_playersReady()) {
						g.start();
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"Everyone is not ready");
						return true;
					}
				}
			}else {
				sender.sendMessage("usage: /vi5 game start <GameName>");
				return true;
			}
			break;
		case "fstart":
			if (args.length>2) {
				Game g = mainref.getGame(args[2]);
				if (g!=null) {
					g.start();
					return true;
				}else {
					sender.sendMessage("There is no game with this name");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 game start <GameName>");
				return true;
			}
		case "setMap":
			if (args.length>3) {
				Game g = mainref.getGame(args[2]);
				if (g!=null) {
					g.setMapName(args[3]);
					sender.sendMessage(ChatColor.GREEN+"Map set!");
					return true;
				}else {
					sender.sendMessage("There is no game with this name");
					return true;
				}
			}else {
				if (args.length>2) {
					Game g = mainref.getGame(args[2]);
					if (g!=null) {
						sender.sendMessage("usage: /vi5 game setMap <GameName> <MapName>");
						sender.sendMessage("Current map for this game: "+g.getMapName());
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"There is no game with this name");
						return true;
					}
				}else {
					sender.sendMessage("usage: /vi5 game setMap <GameName> <MapName>");
					return true;
				}
			}
		case "restart":
			break;
		case "stop":
			break;
		case "join":
			break;
		case "leave":
			break;
		case "list":
			sender.sendMessage(ChatColor.DARK_GREEN+"Games: ");
			for (Game g : mainref.getGamesList()) {
				sender.sendMessage(ChatColor.AQUA+"- "+g.getName()+ChatColor.LIGHT_PURPLE+" ("+g.getMapName()+")");
			}
			return true;
		}
		return false;
	}
	
	public boolean mapCommand(CommandSender sender,String[] args) {
		if (args.length<2) {
			sender.sendMessage("usage: /vi5 map list/setGuardSpawn");
			return true;
		}
		switch (args[1]) {
		case "create":
			if (args.length>2) {
				mainref.getCfgmanager().createMapConfig(args[2]);
				sender.sendMessage(ChatColor.DARK_GREEN+"Map created (except if it was already created)");
				return true;
			}else {
				sender.sendMessage("usage: /vi5 map create <MapName>");
				return true;
			}
		case "setGuardSpawn":
			if (args.length>2) {
				if (sender instanceof Player) {
					Player p = (Player)sender;
					YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
					if (cfg==null) {
						sender.sendMessage(ChatColor.RED+"There is no map with this name");
						return true;
					}
					cfg.set("gardeSpawn",p.getLocation());
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
					return true;
				}else {
					sender.sendMessage(ChatColor.DARK_RED+"You need to be a player to use this command");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map setGuardSpawn <MapName>");
				return true;
			}
		case "setVoleurMinimapSpawn":
			if (args.length>2) {
				if (sender instanceof Player) {
					Player p = (Player)sender;
					YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
					if (cfg==null) {
						sender.sendMessage(ChatColor.RED+"There is no map with this name");
						return true;
					}
					cfg.set("voleurMinimapSpawn",p.getLocation());
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
					return true;
				}else {
					sender.sendMessage(ChatColor.DARK_RED+"You need to be a player to use this command");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map setVoleurMinimapSpawn <MapName>");
				return true;
			}
		case "list":
			sender.sendMessage(ChatColor.DARK_GREEN+"Maps:");
			for (String s : mainref.getCfgmanager().getMapList()) {
				sender.sendMessage(ChatColor.AQUA+"- "+s);
			};
			return true;
		case "rename":
			break;
		}
		return false;
	}
}