package fr.vi5team.vi5.commands;

import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import fr.vi5team.vi5.Game;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.Vi5Team;

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
		case "team":
			return teamCommand(sender,args);
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
			sender.sendMessage("usage: /vi5 map create/list/setGuardSpawn/setVoleurMinimapSpawn/rename/addMapObject/setObjLoc/setObjBlock/setObjSize/objectList");
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
			if (args.length>2) {
				if (mainref.getCfgmanager().renameMapConfig(args[2], args[3])) {
					sender.sendMessage(ChatColor.GREEN+"Success!");
				}else {
					sender.sendMessage(ChatColor.DARK_RED+"Unable to rename this");
				}
				return true;
			}else {
				sender.sendMessage("usage: /vi5 map rename <MapName> <NewName>");
				return true;
			}
		case "addMapObject":/*map=0;addMapObject=1;mapname=2;objName=3*/
			List<String> l = mainref.getCfgmanager().getObjectNamesList(args[2]);
			if(args.length>=4) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (l.contains(args[3])) {
					sender.sendMessage(ChatColor.RED+"This object already exist for this map");
					return true;
				}else {
					l.add(args[3]);
					mainref.getCfgmanager().setObjectNamesList(args[2], l);
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map addMapObject <MapName> <ObjectName>");
				return true;
			}
		case "objectList":
			if(args.length>=4) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				sender.sendMessage(ChatColor.DARK_GREEN+"Objects for map "+args[2]+" :");
				for (String s : mainref.getCfgmanager().getObjectNamesList(args[2])) {
					sender.sendMessage(ChatColor.AQUA+"- "+s);
				}
				return true;
			}else {
				sender.sendMessage("usage: /vi5 map addMapObject <MapName> <ObjectName>");
				return true;
			}
		case "setObjLoc":
			if(args.length>=3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getObjectNamesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						cfg.set("mapObjects."+args[3]+"centerLocation", p.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Object's center location set!");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player to use this command");
						return true;
					}
				}else {
					sender.sendMessage(ChatColor.RED+"There is no object with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map addMapObject <MapName> <ObjectName>");
				return true;
			}
		case "setObjBlock":
			if(args.length>=3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getObjectNamesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						Block b = p.getTargetBlock(null, 10);
						cfg.set("mapObjects."+args[3]+"blockType", b.getType().name());
						cfg.set("mapObjects."+args[3]+"blockData", b.getBlockData().getAsString());
						cfg.set("mapObjects."+args[3]+"blockLocation", b.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Object's block set!");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player to use this command");
						return true;
					}
				}else {
					sender.sendMessage(ChatColor.RED+"There is no object with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map setObjBlock <MapName> <ObjectName>");
				return true;
			}
		case "setObjSize":
			if(args.length>=6) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getObjectNamesList(args[2]).contains(args[3])) {
					cfg.set("mapObjects."+args[3]+"sizex", args[4]);
					cfg.set("mapObjects."+args[3]+"sizey", args[5]);
					cfg.set("mapObjects."+args[3]+"sizez", args[6]);
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
				}else {
					sender.sendMessage(ChatColor.RED+"There is no object with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map setObjSize <MapName> <ObjectName> <x> <y> <z>");
				return true;
			}
		case "addMapEscape":
			break;
		case "removeMapObject":
			break;
		case "removeMapEntrance":
			break;
		case "removeMapEscape":
			break;
		}
		return false;
	}
	public boolean teamCommand(CommandSender sender,String[] args) {
		if (args.length<2) {
			sender.sendMessage("usage: /vi5 team <guard/thief/spectator> [PlayerName]");
			return true;
		}else {
			if (sender instanceof Player) {
				Player player = (Player)sender;
				if (mainref.isPlayerIngame(player)) {
					switch (args[1]) {
					case "guard":
						if (args.length>2) {
							Player p = mainref.getServer().getPlayer(args[2]);
							if (p==null) {
								sender.sendMessage(ChatColor.RED+"This player is not online");
								return true;
							}else {
								if (mainref.isPlayerIngame(p)) {
									mainref.getPlayerWrapper(p).setTeam(Vi5Team.GARDE);
									return true;
								}else {
									sender.sendMessage(ChatColor.RED+"This player is not in a game");
									return true;
								}
							}
						}else {
							mainref.getPlayerWrapper(player).setTeam(Vi5Team.GARDE);
							return true;
						}
					case "thief":
						if (args.length>2) {
							Player p = mainref.getServer().getPlayer(args[2]);
							if (p==null) {
								sender.sendMessage(ChatColor.RED+"This player is not online");
								return true;
							}else {
								if (mainref.isPlayerIngame(p)) {
									mainref.getPlayerWrapper(p).setTeam(Vi5Team.VOLEUR);
									return true;
								}else {
									sender.sendMessage(ChatColor.RED+"This player is not in a game");
									return true;
								}
							}
						}else {
							mainref.getPlayerWrapper(player).setTeam(Vi5Team.VOLEUR);
							return true;
						}
					case "spectator":
						if (args.length>2) {
							Player p = mainref.getServer().getPlayer(args[2]);
							if (p==null) {
								sender.sendMessage(ChatColor.RED+"This player is not online");
								return true;
							}else {
								if (mainref.isPlayerIngame(p)) {
									mainref.getPlayerWrapper(p).setTeam(Vi5Team.SPECTATEUR);
									return true;
								}else {
									sender.sendMessage(ChatColor.RED+"This player is not in a game");
									return true;
								}
							}
						}else {
							mainref.getPlayerWrapper(player).setTeam(Vi5Team.SPECTATEUR);
							return true;
						}
					default:
						sender.sendMessage(ChatColor.RED+"Only thief/guard/spectator are valid teams");
						return true;
					}
				}else {
					sender.sendMessage(ChatColor.RED+"You need to be in game to use this command");
					return true;
				}
			}else {
				sender.sendMessage(ChatColor.RED+"You need to be a player to use this command");
				return true;
			}
		}
	}
}