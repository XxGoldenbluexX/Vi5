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
	public int StringToInt(String st) {
		st.replaceAll("[^0-9]", "");
		try {
			int i = Integer.valueOf(st);
			return i;
		}catch(NumberFormatException e){
			return -1;
		}
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
		case "join":
            if(args.length>2) {
                Player p = (Player)sender;
                if (!mainref.isPlayerIngame(p)) {
                    Game g = mainref.getGame(args[2]);
                    g.addPlayer(p);
                    return true;
                }else {
                    sender.sendMessage("You are already in a game!");
                    return true;
                }
            }else {
                sender.sendMessage("usage: /vi5 game join <GameName>");
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
				}else {
					sender.sendMessage(ChatColor.RED+"This game does not exist!");
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
		case "leave":
			break;
		case "delete":
			break;
		case "list":
			sender.sendMessage(ChatColor.DARK_GREEN+"Games: ");
			for (Game g : mainref.getGamesList()) {
				sender.sendMessage(ChatColor.AQUA+"- "+g.getName()+ChatColor.LIGHT_PURPLE+" ("+g.getMapName()+")");
			}
			return true;
		default:
			sender.sendMessage(ChatColor.RED+"Wrong Command! "+ChatColor.BLUE+ChatColor.UNDERLINE+"Usage: "+ChatColor.WHITE+"/vi5 game "+ChatColor.DARK_GREEN+"...");
			sender.sendMessage(ChatColor.DARK_GREEN+"create"+ChatColor.WHITE+"/"+ChatColor.DARK_GREEN+"setMap"+ChatColor.WHITE+"/"+ChatColor.DARK_GREEN+"list"+ChatColor.WHITE+"/"+ChatColor.DARK_GREEN+"delete");
			sender.sendMessage(ChatColor.DARK_GREEN+"join"+ChatColor.WHITE+"/"+ChatColor.DARK_GREEN+"leave"+ChatColor.WHITE+"/");
			sender.sendMessage(ChatColor.DARK_GREEN+"start"+ChatColor.WHITE+"/"+ChatColor.DARK_GREEN+"fstart"+ChatColor.WHITE+"/"+ChatColor.DARK_GREEN+"restart"+ChatColor.WHITE+"/"+ChatColor.DARK_GREEN+"stop");
			return true;
		}
		return false;
	}
	
	public boolean mapCommand(CommandSender sender,String[] args) {
		if (args.length<2) {
			sender.sendMessage("usage: /vi5 map create/rename/list/delete/setGuardSpawn/setVoleurMinimapSpawn/Objects/Entrances/Escapes");
			return true;
		}
		switch (args[1]) {
		case "Objects":
			sender.sendMessage("Usages: /vi5 map <objectList,addMapObject,removeMapObject,setObjBlock,setObjLoc,setObjSize>");
			return true;
		case "Entrances":
			sender.sendMessage("Usages: /vi5 map <entranceList,addMapEntrance,removeMapEntrance,setEntranceBlock,setEntranceLoc,setEntranceSize>");
			return true;
		case "Escapes":	
			sender.sendMessage("Usages: /vi5 map <escapeList,addMapEscape,removeMapEscape,setEscapeLoc,setEscapeSize>");
			return true;
		case "create":
			if (args.length>2) {
				mainref.getCfgmanager().createMapConfig(args[2]);
				sender.sendMessage(ChatColor.DARK_GREEN+"Map created (except if it was already created)");
				return true;
			}else {
				sender.sendMessage("usage: /vi5 map create <MapName>");
				return true;
			}
		case "delete":
			if (args.length>2) {
				if(mainref.getCfgmanager().deleteMapConfig(args[2])) {
					sender.sendMessage(ChatColor.DARK_GREEN+"Map deleted!");
					return true;
				}else {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map delete <MapName>");
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
			if (args.length>3) {
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
		case "addMapObject":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				List<String> l = mainref.getCfgmanager().getObjectNamesList(args[2]);
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
			if(args.length>2) {
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
				sender.sendMessage("usage: /vi5 map objectList <MapName>");
				return true;
			}
		case "setObjLoc":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getObjectNamesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						Block blockCenter = p.getTargetBlock(null, 10);
						cfg.set("mapObjects."+args[3]+".centerLocation", blockCenter.getLocation());
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
				sender.sendMessage("usage: /vi5 map setObjLoc <MapName> <ObjectName>");
				return true;
			}
		case "setObjBlock":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getObjectNamesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						Block b = p.getTargetBlock(null, 10);
						cfg.set("mapObjects."+args[3]+".blockType", b.getType().name());
						cfg.set("mapObjects."+args[3]+".blockData", b.getBlockData().getAsString());
						cfg.set("mapObjects."+args[3]+".blockLocation", b.getLocation());
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
			if(args.length>6) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getObjectNamesList(args[2]).contains(args[3])) {
					cfg.set("mapObjects."+args[3]+".sizex", StringToInt(args[4]));
					cfg.set("mapObjects."+args[3]+".sizey", StringToInt(args[5]));
					cfg.set("mapObjects."+args[3]+".sizez", StringToInt(args[6]));
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
				}else {
					sender.sendMessage(ChatColor.RED+"There is no object with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map setObjSize <MapName> <ObjectName> <x> <y> <z>");
				return true;
			}
		case "removeMapObject":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				List<String> objectsList = mainref.getCfgmanager().getObjectNamesList(args[2]);
				if (objectsList.contains(args[3])) {
					objectsList.remove(args[3]);
					mainref.getCfgmanager().setObjectNamesList(args[2], objectsList);
					cfg.set("mapObjects."+args[3], null);
					return true;
				}else {
					sender.sendMessage(ChatColor.RED+"There is no object with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map removeMapObject <MapName> <ObjectName>");
				return true;
			}	
		case "addEscape":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				List<String> escapesList = mainref.getCfgmanager().getMapEscapesList(args[2]);
				if (escapesList.contains(args[3])) {
					sender.sendMessage(ChatColor.RED+"This escape already exist for this map");
					return true;
				}else {
					escapesList.add(args[3]);
					mainref.getCfgmanager().setMapEscapesList(args[2], escapesList);
					sender.sendMessage(ChatColor.GREEN+"Map escape created!");
					return true;
				}
			} else {
				sender.sendMessage("usage: /vi5 map addMapEscape <MapName> <EscapeName>");
				return true;
			}
		case "setEscapeLoc":
			if(args.length>3) {
				if (mainref.getCfgmanager().getMapEscapesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
						if(cfg==null) {
							sender.sendMessage(ChatColor.RED+"There is no map with this name");
							return true;
						}
						Player player = (Player)sender;
						Block blockCenter = player.getTargetBlock(null, 10);
						cfg.set("mapEscapes."+args[3]+".centerLocation", blockCenter.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Escape's center location set!");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player to use this command");
						return true;
					}
				} else {
					sender.sendMessage(ChatColor.RED+"There is no escape with this name for this map");
					return true;
				}
			} else {
				sender.sendMessage("usage: /vi5 map setEscapeLoc <MapName> <EscapeName>");
				return true;
			}
		case "setEscapeSize":
			if(args.length>6) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getMapEscapesList(args[2]).contains(args[3])) {
					cfg.set("mapEscapes."+args[3]+".sizex", StringToInt(args[4]));
					cfg.set("mapEscapes."+args[3]+".sizey", StringToInt(args[5]));
					cfg.set("mapEscapes."+args[3]+".sizez", StringToInt(args[6]));
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
					sender.sendMessage(ChatColor.GREEN+"Map escape's size set!");
					return true;
				}else {
					sender.sendMessage(ChatColor.RED+"There is no espace with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map setEscapeSize <MapName> <EscapeName> <x> <y> <z>");
				return true;
			}
		case "addEntrance":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				List<String> entrancesList = mainref.getCfgmanager().getMapEntrancesList(args[2]);
				if (entrancesList.contains(args[3])) {
					sender.sendMessage(ChatColor.RED+"This escape already exist for this map");
					return true;
				}else {
					entrancesList.add(args[3]);
					mainref.getCfgmanager().setMapEntrancesList(args[2], entrancesList);
					return true;
				}
			} else {
				sender.sendMessage("usage: /vi5 map addMapEntrance <MapName> <EntranceName>");
				return true;
			}
		case "setEntranceBlock":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getMapEntrancesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						cfg.set("mapEntrances."+args[3]+".blockLocation", p.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Entrance's teleport location set!");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player to use this command");
						return true;
					}
				}else {
					sender.sendMessage(ChatColor.RED+"There is no entrance with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map setEntranceBlock <MapName> <EntranceName>");
				return true;
			}
		case "setEntranceLoc":
			if(args.length>3) {
				if (mainref.getCfgmanager().getMapEntrancesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
						if(cfg==null) {
							sender.sendMessage(ChatColor.RED+"There is no map with this name");
							return true;
						}
						Player player = (Player)sender;
						Block blockCenter = player.getTargetBlock(null, 10);
						cfg.set("mapEntrances."+args[3]+".centerLocation", blockCenter.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Entrance's center location set!");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player to use this command");
						return true;
					}
				} else {
					sender.sendMessage(ChatColor.RED+"There is no entrance with this name for this map");
					return true;
				}
			} else {
				sender.sendMessage("usage: /vi5 map setEntranceLoc <MapName> <EntranceName>");
				return true;
			}
		case "setEntranceSize":
			if(args.length>6) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				if (mainref.getCfgmanager().getMapEntrancesList(args[2]).contains(args[3])) {
					cfg.set("mapEntrances."+args[3]+".sizex", StringToInt(args[4]));
					cfg.set("mapEntrances."+args[3]+".sizey", StringToInt(args[5]));
					cfg.set("mapEntrances."+args[3]+".sizez", StringToInt(args[6]));
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
					sender.sendMessage(ChatColor.GREEN+"Entrance's size location set!");
					return true;
				}else {
					sender.sendMessage(ChatColor.RED+"There is no entrance with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map setEntranceSize <MapName> <EntranceName> <x> <y> <z>");
				return true;
			}
		case "removeEntrance":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				List<String> entranceList = mainref.getCfgmanager().getMapEntrancesList(args[2]);
				if (entranceList.contains(args[3])) {
					entranceList.remove(args[3]);
					mainref.getCfgmanager().setMapEntrancesList(args[2], entranceList);
					cfg.set("mapEntrances."+args[3], null);
					sender.sendMessage(ChatColor.GREEN+"Entrance removed!");
					return true;
				}else {
					sender.sendMessage(ChatColor.RED+"There is no entrance with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map removeMapEntrance <MapName> <EntranceName>");
				return true;
			}	
		case "removeEscape":
			if(args.length>=3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				List<String> escapeList = mainref.getCfgmanager().getMapEscapesList(args[2]);
				if (escapeList.contains(args[3])) {
					escapeList.remove(args[3]);
					mainref.getCfgmanager().setMapEscapesList(args[2], escapeList);
					cfg.set("mapEscapes."+args[3], null);
					sender.sendMessage(ChatColor.GREEN+"Escape removed!");
					return true;
				}else {
					sender.sendMessage(ChatColor.RED+"There is no escape with this name for this map");
					return true;
				}
			}else {
				sender.sendMessage("usage: /vi5 map removeMapEscape <MapName> <EscapeName>");
				return true;
			}
		case "entranceList":
			if(args.length>2) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				sender.sendMessage(ChatColor.DARK_GREEN+"Entrances for map "+args[2]+" :");
				for (String s : mainref.getCfgmanager().getMapEntrancesList(args[2])) {
					sender.sendMessage(ChatColor.AQUA+"- "+s);
				}
				return true;
			}else {
				sender.sendMessage("usage: /vi5 map entranceList <MapName>");
				return true;
			}
		case "escapeList":
			if(args.length>2) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage(ChatColor.RED+"There is no map with this name");
					return true;
				}
				sender.sendMessage(ChatColor.DARK_GREEN+"Escapes for map "+args[2]+" :");
				for (String s : mainref.getCfgmanager().getMapEscapesList(args[2])) {
					sender.sendMessage(ChatColor.AQUA+"- "+s);
				}
				return true;
			}else {
				sender.sendMessage("usage: /vi5 map escapeList <MapName>");
				return true;
			}
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
									sender.sendMessage(ChatColor.GREEN+"This player is now a guard");
									return true;
								}else {
									sender.sendMessage(ChatColor.RED+"This player is not in a game");
									return true;
								}
							}
						}else {
							mainref.getPlayerWrapper(player).setTeam(Vi5Team.GARDE);
							sender.sendMessage(ChatColor.GREEN+"You are now a guard");
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
									sender.sendMessage(ChatColor.GREEN+"This player is now a thief");
									return true;
								}else {
									sender.sendMessage(ChatColor.RED+"This player is not in a game");
									return true;
								}
							}
						}else {
							mainref.getPlayerWrapper(player).setTeam(Vi5Team.VOLEUR);
							sender.sendMessage(ChatColor.GREEN+"You are now a thief");
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
									sender.sendMessage(ChatColor.GREEN+"This player is now a spectator");
									return true;
								}else {
									sender.sendMessage(ChatColor.RED+"This player is not in a game");
									return true;
								}
							}
						}else {
							mainref.getPlayerWrapper(player).setTeam(Vi5Team.SPECTATEUR);
							sender.sendMessage(ChatColor.GREEN+"You are now a spectator");
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