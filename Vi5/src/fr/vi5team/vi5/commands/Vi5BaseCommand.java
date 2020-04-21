package fr.vi5team.vi5.commands;

import java.util.ArrayList;
import java.util.Collections;
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
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.Vi5Team;

public class Vi5BaseCommand implements CommandExecutor {

	private final Vi5Main mainref;
	ArrayList<String> baseCommands = new ArrayList<String>();
	ArrayList<String> gameCommands = new ArrayList<String>();
	ArrayList<String> mapCommands = new ArrayList<String>();
	ArrayList<String> teamCommands = new ArrayList<String>();
	
	public Vi5BaseCommand(Vi5Main main) {
		mainref=main;
		baseCommands.add("game");
		baseCommands.add("map");
		baseCommands.add("team");
		baseCommands.add("glow");
		
		gameCommands.add("create");
		gameCommands.add("join");
		gameCommands.add("leave");
		gameCommands.add("setMap");
		gameCommands.add("list");
		gameCommands.add("delete");
		gameCommands.add("playerList");
		gameCommands.add("ready");
		gameCommands.add("start");
		gameCommands.add("fstart");
		gameCommands.add("restart");
		gameCommands.add("stop");
		
		mapCommands.add("Objects");
		mapCommands.add("Entrances");
		mapCommands.add("Escapes");
		mapCommands.add("Walls");
		mapCommands.add("create");
		mapCommands.add("rename");
		mapCommands.add("list");
		mapCommands.add("delete");
		mapCommands.add("setGuardSpawn");
		mapCommands.add("setThiefMinimapSpawn");
		
		teamCommands.add("guard");
		teamCommands.add("thief");
		teamCommands.add("spectator");
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
			return true;
		default:
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 "+"["+ChatColor.GOLD+"game"+ChatColor.WHITE+"/"+ChatColor.GOLD+"map"+ChatColor.WHITE+"/"+ChatColor.GOLD+"team"+ChatColor.WHITE+"/"+ChatColor.GOLD+"glow"+ChatColor.WHITE+"]");
			return true;
		}
	}
	public List<String> onTabComplete(CommandSender sender, Command command, String arg2, String[] args){
		if(args.length<1) {
			return null;
		}
		if(args.length>1) {
			if(args.length>2) {
				return null;
			}else {
				switch (args[0]) {
				case "game":
					return returnFollowCMD(args, gameCommands, 1);
				case "map":
					return returnFollowCMD(args, mapCommands, 1);
				case "team":
					return returnFollowCMD(args, teamCommands, 1);
				}
			}
		}else {
			return returnFollowCMD(args, baseCommands, 0);
		}
		return null;
	}
	public ArrayList<String> returnFollowCMD(String[] args, ArrayList<String> cmdList, int argsPos){
		ArrayList<String> afterCMD = new ArrayList<String>();
		if(!args[argsPos].equals("")) {
			for(String cmd : cmdList) {
				if (cmd.toLowerCase().startsWith(args[argsPos].toLowerCase())) {
					afterCMD.add(cmd);	
				}
			}
		}else {
			afterCMD = cmdList;
		}
		Collections.sort(afterCMD);
		return afterCMD;
	}
	public boolean gameCommand(CommandSender sender,String[] args) {
		if (args.length<2) {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game "+ChatColor.GOLD+"...");
			sender.sendMessage("["+ChatColor.GOLD+"create"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setMap"+ChatColor.WHITE+"/"+ChatColor.GOLD+"list"+ChatColor.WHITE+"/"+ChatColor.GOLD+"delete"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"join"+ChatColor.WHITE+"/"+ChatColor.GOLD+"playerList"+ChatColor.WHITE+"/"+ChatColor.GOLD+"ready"+ChatColor.WHITE+ChatColor.GOLD+"leave"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"start"+ChatColor.WHITE+"/"+ChatColor.GOLD+"fstart"+ChatColor.WHITE+"/"+ChatColor.GOLD+"restart"+ChatColor.WHITE+"/"+ChatColor.GOLD+"stop"+ChatColor.WHITE+"]");
			sender.sendMessage("");
			return true;
		}
		switch (args[1]) {
		case "playerList":
			if(args.length>2) {
				Game g = mainref.getGame(args[2]);
				sender.sendMessage(ChatColor.BLUE+"Player list for "+args[2]+ChatColor.BLUE+":");
				for(Player p : g.getPlayerList()) {
					String playerName=p.getName();
					if(g.is_Started()) {
						sender.sendMessage(playerName+ChatColor.BLUE+", Status: "+ChatColor.GOLD+"[IN_GAME]");
					}else {
						PlayerWrapper wrap = mainref.getPlayerWrapper(p);
						if(wrap.is_ready()) {
							sender.sendMessage(playerName+ChatColor.BLUE+", Status: "+ChatColor.GREEN+"[READY]");	
						}else {
							sender.sendMessage(playerName+ChatColor.BLUE+", Status: "+ChatColor.RED+"[NOT READY]");
						}
					}
				}
				return true;
			}else {
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game playerList "+ChatColor.GOLD+"<GameName>");
				return true;
			}
		case "create":
			if (args.length>2) {
				Game g = mainref.createGame(args[2]);
				sender.sendMessage(ChatColor.GREEN+"The game ("+args[2]+ChatColor.GREEN+") has been created!");
				if (sender instanceof Player) {
					Player p = (Player)sender;
					if (!mainref.isPlayerIngame(p)) {
						g.addPlayer((Player)sender);
						return true;
					}
					return true;
				}
				return true;
			}
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game create "+ChatColor.GOLD+"<GameName>");
			sender.sendMessage("");
			return true;
		case "ready":
			if(args.length>2) {
				Player p;
				if (args.length>3) {
					p = Bukkit.getServer().getPlayer(args[3]);
            		if (p.equals(null)) {
            			sender.sendMessage("");
                    	sender.sendMessage(ChatColor.RED+"This player does not exist!");
                    	sender.sendMessage("");
                        return true;
            		}
            	}else {
            		p = (Player)sender;		
				}
				Game g = mainref.getGame(args[2]);
				if (g.hasPlayer(p)){
					if(!g.is_playerReady(p)) {
						PlayerWrapper wrap = mainref.getPlayerWrapper(p);
						wrap.setReady(true);
						sender.sendMessage(ChatColor.GREEN+"Player ("+p+ChatColor.GREEN+") is now ready!");
					}else {
						sender.sendMessage("");
		    			sender.sendMessage(ChatColor.RED+"This player is already ready!");
		    			sender.sendMessage("");
					}
				}else {
					sender.sendMessage("");
	    			sender.sendMessage(ChatColor.RED+"This player is not in a game!");
	    			sender.sendMessage("");
				}
			}else {
            	sender.sendMessage("");
    			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game ready "+ChatColor.GOLD+"<GameName> (PlayerName)");
    			sender.sendMessage("");
                return true;
			}
		case "join":
            if(args.length>2) {
            	Player p;
            	if (args.length>3) {
            		p = Bukkit.getServer().getPlayer(args[3]);
            		if (p==null) {
            			sender.sendMessage("");
                    	sender.sendMessage(ChatColor.RED+"This player does not exist!");
                    	sender.sendMessage("");
                        return true;
            		}
            	}else {
            		p = (Player)sender;
            	}
                if (!mainref.isPlayerIngame(p)) {
                    Game g = mainref.getGame(args[2]);
                    g.addPlayer(p);
                    return true;
                }else {
                	sender.sendMessage("");
                	sender.sendMessage(ChatColor.RED+"This player is already in a game!");
                	sender.sendMessage("");
                    return true;
                }
            }else {
            	sender.sendMessage("");
    			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game join "+ChatColor.GOLD+"<GameName> (PlayerName)");
    			sender.sendMessage("");
                return true;
            }
		case "start":
			if (args.length>2) {
				Game g = mainref.getGame(args[2]);
				if (g!=null) {
					g.start(false,sender);
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This game does not exist!");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game start "+ChatColor.GOLD+"<GameName>");
				sender.sendMessage("");
				return true;
			}
		case "fstart":
			if (args.length>2) {
				Game g = mainref.getGame(args[2]);
				if (g!=null) {
					g.start(true,sender);
					return true;
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This game does not exist!");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game fstart "+ChatColor.GOLD+"<GameName>");
				sender.sendMessage("");
				return true;
			}
		case "setMap":
			if (args.length>3) {
				Game g = mainref.getGame(args[2]);
				if (g!=null) {
					if (mainref.getCfgmanager().getMapList().contains(args[3])) {
						g.setMapName(args[3]);
						sender.sendMessage(ChatColor.GREEN+"Map has been set!");
					}else {
						sender.sendMessage(ChatColor.RED+"This map doesnt exist!");
					}
					return true;
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This game does not exist!");
					sender.sendMessage("");
					return true;
				}
			}else {
				if (args.length>2) {
					Game g = mainref.getGame(args[2]);
					if (g!=null) {
						sender.sendMessage("");
						sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game setMap "+ChatColor.GOLD+"<GameName> <MapName>");
						sender.sendMessage(ChatColor.BLUE+"Current map for this game: "+ChatColor.GOLD+g.getMapName());
						sender.sendMessage("");
						return true;
					}else {
						sender.sendMessage("");
						sender.sendMessage(ChatColor.RED+"This game does not exist!");
						sender.sendMessage("");
						return true;
					}
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game setMap "+ChatColor.GOLD+"<GameName> <MapName>");
					sender.sendMessage("");
					return true;
				}
			}
		case "restart":
			if(args.length>2) {
				Game g = mainref.getGame(args[2]);
				if (g!=null) {
					g.start(true,sender);
					for(Player p:Bukkit.getOnlinePlayers()) {
						if (g.hasPlayer(p)) {
							p.sendMessage(ChatColor.RED+"Game has been restarted by: "+sender);
						}
					}
					return true;
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This game does not exist!");
					sender.sendMessage("");
					return true;
				}
			}else {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game restart "+ChatColor.GOLD+"<GameName>");
			sender.sendMessage("");
			return true;
			}
		case "stop":
			if(args.length>2) {
				Game game = mainref.getGame(args[2]);
				if (!game.is_Started()) {
					for(Player p:Bukkit.getOnlinePlayers()) {
						if (game.hasPlayer(p)) {
							p.sendMessage(ChatColor.RED+"Game has been forcefully stopped by: "+sender);
							PlayerWrapper wrap = mainref.getPlayerWrapper(p);
							wrap.resetItemStealed();
						}
					}
					game.endGame();
					return true;
				}
				sender.sendMessage("");
				sender.sendMessage(ChatColor.RED+"Game does not exist or haven't started!");
				sender.sendMessage("");
				return true;
			}
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game stop "+ChatColor.GOLD+"<GameName>");
			sender.sendMessage("");
			return true;
		case "leave":
            if(args.length>2) {
            	Player p;
            	if (args.length>3) {
            		p = Bukkit.getServer().getPlayer(args[3]);
            		if (p.equals(null)) {
            			sender.sendMessage("");
                    	sender.sendMessage(ChatColor.RED+"This player does not exist!");
                    	sender.sendMessage("");
                        return true;
            		}
            	}else {
            		p = (Player)sender;
            	}
                if (mainref.isPlayerIngame(p)) {
                    Game g = mainref.getGame(args[2]);
                    g.removePlayer(p);
                    sender.sendMessage(p.getName()+ChatColor.GREEN+") left this game!");
                    return true;
                }else {
                	sender.sendMessage("");
                	sender.sendMessage(ChatColor.RED+"This player is not in a game!");
                	sender.sendMessage("");
                    return true;
                }
            }else {
            	sender.sendMessage("");
    			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game leave "+ChatColor.GOLD+"<GameName> (PlayerName)");
    			sender.sendMessage("");
                return true;
            }
		case "delete":
			if(args.length>2) {
				Game game = mainref.getGame(args[2]);
				if(!(game==null)) {
					mainref.deleteGame(game);
					sender.sendMessage(ChatColor.GREEN+"This game has been succesfully deleted!");
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This game does not exist!");
					sender.sendMessage("");
				}
			}else {
            	sender.sendMessage("");
    			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game delete "+ChatColor.GOLD+"<GameName>");
    			sender.sendMessage("");
                return true;
			}
		case "list":
			sender.sendMessage(ChatColor.DARK_GREEN+"Games: ");
			for (Game g : mainref.getGamesList()) {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.AQUA+"- "+g.getName()+ChatColor.LIGHT_PURPLE+" ("+g.getMapName()+")");
				sender.sendMessage("");
			}
			return true;
		default:
			sender.sendMessage("");
			sender.sendMessage(ChatColor.RED+"Wrong Command! "+ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 game "+ChatColor.GOLD+"...");
			sender.sendMessage("["+ChatColor.GOLD+"create"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setMap"+ChatColor.WHITE+"/"+ChatColor.GOLD+"list"+ChatColor.WHITE+"/"+ChatColor.GOLD+"delete"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"join"+ChatColor.WHITE+"/"+ChatColor.GOLD+"leave"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"start"+ChatColor.WHITE+"/"+ChatColor.GOLD+"fstart"+ChatColor.WHITE+"/"+ChatColor.GOLD+"restart"+ChatColor.WHITE+"/"+ChatColor.GOLD+"stop"+ChatColor.WHITE+"]");
			sender.sendMessage("");
			return true;
		}
	}
	
	public boolean mapCommand(CommandSender sender,String[] args) {
		if (args.length<2) {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map "+ChatColor.GOLD+"...");
			sender.sendMessage("["+ChatColor.GOLD+"create"+ChatColor.WHITE+"/"+ChatColor.GOLD+"rename"+ChatColor.WHITE+"/"+ChatColor.GOLD+"list"+ChatColor.WHITE+"/"+ChatColor.GOLD+"delete"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"setGuardSpawn"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setThiefMinimapSpawn"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"Objects"+ChatColor.WHITE+"/"+ChatColor.GOLD+"Entrances"+ChatColor.WHITE+"/"+ChatColor.GOLD+"Escapes"+ChatColor.WHITE+"/"+ChatColor.GOLD+"Walls"+ChatColor.WHITE+"]");
			sender.sendMessage("");
			return true;
		}
		switch (args[1]) {
		case "Objects":
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map "+ChatColor.GOLD+"...");
			sender.sendMessage("["+ChatColor.GOLD+"addObject"+ChatColor.WHITE+"/"+ChatColor.GOLD+"objectList"+ChatColor.WHITE+"/"+ChatColor.GOLD+"removeObject"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"setObjBlock"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setObjLoc"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setObjSize"+ChatColor.WHITE+"]");
			sender.sendMessage("");
			return true;
		case "Entrances":
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map "+ChatColor.GOLD+"...");
			sender.sendMessage("["+ChatColor.GOLD+"addMapEntrance"+ChatColor.WHITE+"/"+ChatColor.GOLD+"entranceList"+ChatColor.WHITE+"/"+ChatColor.GOLD+"removeMapEntrance"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"setEntranceBlock"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setEntranceLoc"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setEntranceSize"+ChatColor.WHITE+"]");
			sender.sendMessage("");
			return true;
		case "Escapes":	
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map "+ChatColor.GOLD+"...");
			sender.sendMessage("["+ChatColor.GOLD+"addMapEscape"+ChatColor.WHITE+"/"+ChatColor.GOLD+"escapeList"+ChatColor.WHITE+"/"+ChatColor.GOLD+"removeMapEscape"+ChatColor.WHITE+"]");
			sender.sendMessage("["+ChatColor.GOLD+"setEscapeLoc"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setEscapeSize"+ChatColor.WHITE+"]");
			sender.sendMessage("");
			return true;
		case "Walls":
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map "+ChatColor.GOLD+"...");
			sender.sendMessage("["+ChatColor.GOLD+"addWall"+ChatColor.WHITE+"/"+ChatColor.WHITE+"/"+ChatColor.GOLD+"removeWall"+ChatColor.GOLD+"setWallCorner1"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setWallCorner2"+ChatColor.WHITE+"/"+ChatColor.GOLD+"setWallIndication"+ChatColor.WHITE+"]");
			sender.sendMessage(ChatColor.RED+"WARNING: Those are the walls used for guards' second ability "+ChatColor.LIGHT_PURPLE+ChatColor.UNDERLINE+"BUILDER"+ChatColor.RED+" which allow them to place additionnal walls on the map!");
			sender.sendMessage(ChatColor.ITALIC+""+ChatColor.BLUE+"By default, Purpur will be set under walls for the guards to activate them. If you want to change ALL those blocks: "+ChatColor.WHITE+"/vi5 map setWallIndication "+ChatColor.GOLD+"<MapName>");
			sender.sendMessage("");
			return true;
		case "addWall":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				List<String> wallsList = mainref.getCfgmanager().getMapWallsList(args[2]);
				if (wallsList.contains(args[3])) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This wall already exist for map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage("");
					return true;
				}else {
					wallsList.add(args[3]);
					mainref.getCfgmanager().setMapEscapesList(args[2], wallsList);
					sender.sendMessage(ChatColor.GREEN+"Map wall ("+ChatColor.GOLD+args[3]+ChatColor.GREEN+") has been created on map: "+ChatColor.GOLD+args[2]+ChatColor.GREEN+" !");
					return true;
				}
			} else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map addWall "+ChatColor.GOLD+"<MapName> <WallName>");
				sender.sendMessage("");
				return true;
			}
		case "removeWall":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				List<String> wallList = mainref.getCfgmanager().getMapWallsList(args[2]);
				if (wallList.contains(args[3])) {
					wallList.remove(args[3]);
					mainref.getCfgmanager().setMapWallsList(args[2], wallList);
					cfg.set("mapWalls."+args[3], null);
					sender.sendMessage(ChatColor.GREEN+"Wall removed!");
					return true;
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This Wall does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addWall "+ChatColor.GOLD+"<MapName> <WallName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map removeWall "+ChatColor.GOLD+"<MapName> <WallName>");
				sender.sendMessage("");
				return true;
			}
		case "setWallCorner1":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				if (mainref.getCfgmanager().getMapWallsList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						Block block = p.getTargetBlock(null, 10);
						cfg.set("mapWalls."+args[3]+".firstCorner", block.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Wall's first corner location set!");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
						return true;
					}
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This wall does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addWall "+ChatColor.GOLD+"<MapName> <WallName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage("usage: /vi5 map setWallCorner1 <MapName> <WallName>");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setWallCorner1 "+ChatColor.GOLD+"<MapName> <WallName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Location will be set on the block you are looking at "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "setWallCorner2":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				if (mainref.getCfgmanager().getMapWallsList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						Block block = p.getTargetBlock(null, 10);
						cfg.set("mapWalls."+args[3]+".secondCorner", block.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Wall's second corner location set!");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
						return true;
					}
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This wall does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addWall "+ChatColor.GOLD+"<MapName> <WallName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage("usage: /vi5 map setWallCorner2 <MapName> <WallName>");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setWallCorner2 "+ChatColor.GOLD+"<MapName> <WallName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Location will be set on the block you are looking at "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "setWallIndication":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				if (mainref.getCfgmanager().getMapWallsList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						Player p = (Player)sender;
						Block block= p.getTargetBlock(null, 10);
						cfg.set("mapWalls.blocksUnder", block.getType());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Blocks under ALL walls of this map set!");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
						return true;
					}
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This wall does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addWall "+ChatColor.GOLD+"<MapName> <WallName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage("usage: /vi5 map setWallIndication <MapName>");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setWallIndication "+ChatColor.GOLD+"<MapName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"The block you are looking at will be set under ALL of this map walls "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "create":
			if (args.length>2) {
				mainref.getCfgmanager().createMapConfig(args[2]);
				sender.sendMessage(ChatColor.GREEN+"Map created (except if it was already created)");
				return true;
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
				sender.sendMessage("");
				return true;
			}
		case "delete":
			if (args.length>2) {
				if(mainref.getCfgmanager().deleteMapConfig(args[2])) {
					sender.sendMessage(ChatColor.GREEN+"Map deleted!");
					return true;
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map delete "+ChatColor.GOLD+"<MapName>");
				sender.sendMessage("");
				return true;
			}
		case "setGuardSpawn":
			if (args.length>2) {
				if (sender instanceof Player) {
					Player p = (Player)sender;
					YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
					if (cfg==null) {
						sender.sendMessage("");
						sender.sendMessage(ChatColor.RED+"This map does not exist!");
						sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
						sender.sendMessage("");
						return true;
					}
					cfg.set("gardeSpawn",p.getLocation());
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
					sender.sendMessage(ChatColor.GREEN+"Guards spawn has been set!");
					return true;
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setGuardSpawn "+ChatColor.GOLD+"<MapName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Position will be set where you are standing "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "setThiefMinimapSpawn":
			if (args.length>2) {
				if (sender instanceof Player) {
					Player p = (Player)sender;
					YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
					if (cfg==null) {
						sender.sendMessage("");
						sender.sendMessage(ChatColor.RED+"This map does not exist!");
						sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
						sender.sendMessage("");
						return true;
					}
					cfg.set("voleurMinimapSpawn",p.getLocation());
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
					sender.sendMessage(ChatColor.GREEN+"Thief Minimap Spawn has been set!");
					return true;
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setThiefMinimapSpawn "+ChatColor.GOLD+"<MapName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Position will be set where you are standing "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "list":
			sender.sendMessage("");
			sender.sendMessage(ChatColor.DARK_GREEN+"Maps:");
			for (String s : mainref.getCfgmanager().getMapList()) {
				sender.sendMessage(ChatColor.AQUA+"- "+s);
			};
			sender.sendMessage("");
			return true;
		case "rename":
			if (args.length>3) {
				if (mainref.getCfgmanager().renameMapConfig(args[2], args[3])) {
					sender.sendMessage(ChatColor.GOLD+args[2]+ChatColor.GREEN+" has been renamed to "+ChatColor.GOLD+args[3]+ChatColor.GREEN+" !");
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"Encountered error during renaming!");
					sender.sendMessage(ChatColor.RED+"The map probably does not exist");
					sender.sendMessage("");
				}
				return true;
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map rename "+ChatColor.GOLD+"<MapName> <NewName>");
				sender.sendMessage("");
				return true;
			}
		case "addObject":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				List<String> l = mainref.getCfgmanager().getObjectNamesList(args[2]);
				if (l.contains(args[3])) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This object already exist for map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage("");
					return true;
				}else {
					l.add(args[3]);
					mainref.getCfgmanager().setObjectNamesList(args[2], l);
					sender.sendMessage(ChatColor.GREEN+"Map ("+ChatColor.GOLD+args[2]+ChatColor.GREEN+") has been created!");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map addObject "+ChatColor.GOLD+"<MapName> <ObjectName>");
				sender.sendMessage("");
				return true;
			}
		case "objectList":
			if(args.length>2) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				sender.sendMessage(ChatColor.DARK_GREEN+"Objects for map "+args[2]+" :");
				for (String s : mainref.getCfgmanager().getObjectNamesList(args[2])) {
					sender.sendMessage(ChatColor.AQUA+"- "+s);
				}
				return true;
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map objectList "+ChatColor.GOLD+"<MapName>");
				sender.sendMessage("");
				return true;
			}
		case "setObjLoc":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
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
						sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
						return true;
					}
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This object does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addObject "+ChatColor.GOLD+"<MapName> <ObjectName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage("usage: /vi5 map setObjLoc <MapName> <ObjectName>");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setObjLoc "+ChatColor.GOLD+"<MapName> <ObjectName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Location will be set on the block you are looking at "+ChatColor.RED+"!");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"This location is the center of the capture zone "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "setObjBlock":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
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
						sender.sendMessage("");
						sender.sendMessage(ChatColor.GREEN+"Object's block informations has been set!");
						sender.sendMessage("");
						return true;
					}else {
						sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
						return true;
					}
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This object does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addObject "+ChatColor.GOLD+"<MapName> <ObjectName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setObjLoc "+ChatColor.GOLD+"<MapName> <ObjectName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Location will be set on the block you are looking at "+ChatColor.RED+"!");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"This exact block will be shown during game and disappear on capture "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "setObjSize":
			if(args.length>6) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				if (mainref.getCfgmanager().getObjectNamesList(args[2]).contains(args[3])) {
					cfg.set("mapObjects."+args[3]+".sizex", StringToInt(args[4]));
					cfg.set("mapObjects."+args[3]+".sizey", StringToInt(args[5]));
					cfg.set("mapObjects."+args[3]+".sizez", StringToInt(args[6]));
					mainref.getCfgmanager().saveMapConfig(args[2], cfg);
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This object does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addObject "+ChatColor.GOLD+"<MapName> <ObjectName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setObjSize "+ChatColor.GOLD+"<MapName> <ObjectName> <x> <y> <z>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"This is the ray of the capture zone "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "removeObject":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				List<String> objectsList = mainref.getCfgmanager().getObjectNamesList(args[2]);
				if (objectsList.contains(args[3])) {
					objectsList.remove(args[3]);
					mainref.getCfgmanager().setObjectNamesList(args[2], objectsList);
					cfg.set("mapObjects."+args[3], null);
					return true;
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This object does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addObject "+ChatColor.GOLD+"<MapName> <ObjectName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map removeObject "+ChatColor.GOLD+"<MapName> <ObjectName>");
				sender.sendMessage("");
				return true;
			}	
		case "addEscape":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				List<String> escapesList = mainref.getCfgmanager().getMapEscapesList(args[2]);
				if (escapesList.contains(args[3])) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This escape already exist for map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage("");
					return true;
				}else {
					escapesList.add(args[3]);
					mainref.getCfgmanager().setMapEscapesList(args[2], escapesList);
					sender.sendMessage(ChatColor.GREEN+"Map escape ("+ChatColor.GOLD+args[3]+ChatColor.GREEN+") has been created on map: "+ChatColor.GOLD+args[2]+ChatColor.GREEN+" !");
					return true;
				}
			} else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map addMapEscape "+ChatColor.GOLD+"<MapName> <EscapeName>");
				sender.sendMessage("");
				return true;
			}
		case "setEscapeLoc":
			if(args.length>3) {
				if (mainref.getCfgmanager().getMapEscapesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
						if(cfg==null) {
							sender.sendMessage("");
							sender.sendMessage(ChatColor.RED+"This map does not exist!");
							sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
							sender.sendMessage("");
							return true;
						}
						Player player = (Player)sender;
						Block blockCenter = player.getTargetBlock(null, 10);
						cfg.set("mapEscapes."+args[3]+".centerLocation", blockCenter.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Escape's center location set!");
						return true;
					}else {
						sender.sendMessage("");
						sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
						sender.sendMessage("");
						return true;
					}
				} else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This escape does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addEscape "+ChatColor.GOLD+"<MapName> <EscapeName>");
					sender.sendMessage("");
					return true;
				}
			} else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setEscapeLoc "+ChatColor.GOLD+"<MapName> <EscapeName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Location will be set on the block you are looking at "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "setEscapeSize":
			if(args.length>6) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
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
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This escape does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addEscape "+ChatColor.GOLD+"<MapName> <EscapeName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setEscapeSize "+ChatColor.GOLD+"<MapName> <EscapeName> <x> <y> <z>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"This is the ray of the escape zone "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "addEntrance":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				List<String> entrancesList = mainref.getCfgmanager().getMapEntrancesList(args[2]);
				if (entrancesList.contains(args[3])) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This escape already exist for map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage("");
					return true;
				}else {
					entrancesList.add(args[3]);
					mainref.getCfgmanager().setMapEntrancesList(args[2], entrancesList);
					sender.sendMessage(ChatColor.GREEN+"Map entrance ("+ChatColor.GOLD+args[3]+ChatColor.GREEN+") has been created on map: "+ChatColor.GOLD+args[2]+ChatColor.GREEN+" !");
					return true;
				}
			} else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map addMapEntrance "+ChatColor.GOLD+"<MapName> <EntranceName>");
				sender.sendMessage("");
				return true;
			}
		case "setEntranceBlock":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
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
						sender.sendMessage("");
						sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command!");
						sender.sendMessage("");
						return true;
					}
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This entrance does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addEntrance "+ChatColor.GOLD+"<MapName> <EntranceName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setEntranceBlock "+ChatColor.GOLD+"<MapName> <EntranceName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Position will be set where you are standing "+ChatColor.RED+"!");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"This will be where you will be teleported when spawning "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}	
		case "setEntranceLoc":
			if(args.length>3) {
				if (mainref.getCfgmanager().getMapEntrancesList(args[2]).contains(args[3])) {
					if (sender instanceof Player) {
						YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
						if(cfg==null) {
							sender.sendMessage("");
							sender.sendMessage(ChatColor.RED+"This map does not exist!");
							sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
							sender.sendMessage("");
							return true;
						}
						Player player = (Player)sender;
						Block blockCenter = player.getTargetBlock(null, 10);
						cfg.set("mapEntrances."+args[3]+".centerLocation", blockCenter.getLocation());
						mainref.getCfgmanager().saveMapConfig(args[2], cfg);
						sender.sendMessage(ChatColor.GREEN+"Entrance's center location set!");
						return true;
					}else {
						sender.sendMessage("");
						sender.sendMessage(ChatColor.RED+"You need to be a player in order to use this command");
						sender.sendMessage("");
						return true;
					}
				} else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This entrance does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addEntrance "+ChatColor.GOLD+"<MapName> <EntranceName>");
					sender.sendMessage("");
					return true;
				}
			} else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setEntranceLoc "+ChatColor.GOLD+"<MapName> <EntranceName>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"Location will be set on the block you are looking at "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "setEntranceSize":
			if(args.length>6) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
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
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This entrance does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addEntrance "+ChatColor.GOLD+"<MapName> <EntranceName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map setEntranceSize "+ChatColor.GOLD+"<MapName> <EntranceName> <x> <y> <z>");
				sender.sendMessage(ChatColor.RED+"! "+ChatColor.BLUE+"This is the ray of the entrance zone "+ChatColor.RED+"!");
				sender.sendMessage("");
				return true;
			}
		case "removeEntrance":
			if(args.length>3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
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
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This entrance does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addEntrance "+ChatColor.GOLD+"<MapName> <EntranceName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map removeMapEntrance "+ChatColor.GOLD+"<MapName> <EntranceName>");
				sender.sendMessage("");
				return true;
			}
		case "removeEscape":
			if(args.length>=3) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if(cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
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
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This escape does not exist on map: "+ChatColor.GOLD+args[2]);
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map addEscape "+ChatColor.GOLD+"<MapName> <EscapeName>");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map removeMapEscape "+ChatColor.GOLD+"<MapName> <EscapeName>");
				sender.sendMessage("");
				return true;
			}
		case "entranceList":
			if(args.length>2) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				sender.sendMessage(ChatColor.DARK_GREEN+"Entrances for map "+args[2]+" :");
				for (String s : mainref.getCfgmanager().getMapEntrancesList(args[2])) {
					sender.sendMessage(ChatColor.AQUA+"- "+s);
				}
				return true;
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map entranceList "+ChatColor.GOLD+"<MapName>");
				sender.sendMessage("");
				return true;
			}
		case "escapeList":
			if(args.length>2) {
				YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(args[2]);
				if (cfg==null) {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"This map does not exist!");
					sender.sendMessage(ChatColor.GREEN+"Try: "+ChatColor.WHITE+"/vi5 map create "+ChatColor.GOLD+"<MapName>");
					sender.sendMessage("");
					return true;
				}
				sender.sendMessage(ChatColor.DARK_GREEN+"Escapes for map "+args[2]+" :");
				for (String s : mainref.getCfgmanager().getMapEscapesList(args[2])) {
					sender.sendMessage(ChatColor.AQUA+"- "+s);
				}
				return true;
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 map escapeList "+ChatColor.GOLD+"<MapName>");
				sender.sendMessage("");
				return true;
			}
		}
		return false;
	}
	
	public boolean teamCommand(CommandSender sender,String[] args) {
		if (args.length<2) {
			sender.sendMessage("");
			sender.sendMessage(ChatColor.BLUE+"Usage: "+ChatColor.WHITE+"/vi5 team "+ChatColor.GOLD+"...");
			sender.sendMessage(ChatColor.GOLD+"<guard/thief/spectator> [PlayerName]");
			sender.sendMessage("");
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
								sender.sendMessage("");
								sender.sendMessage(ChatColor.RED+"This player is not online!");
								sender.sendMessage("");
								return true;
							}else {
								if (mainref.isPlayerIngame(p)) {
									mainref.getPlayerWrapper(p).setTeam(Vi5Team.GARDE);
									sender.sendMessage(ChatColor.GREEN+"This player is now a guard");
									return true;
								}else {
									sender.sendMessage("");
									sender.sendMessage(ChatColor.RED+"This player is not in a game!");
									sender.sendMessage("");
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
								sender.sendMessage("");
								sender.sendMessage(ChatColor.RED+"This player is not online!");
								sender.sendMessage("");
								return true;
							}else {
								if (mainref.isPlayerIngame(p)) {
									mainref.getPlayerWrapper(p).setTeam(Vi5Team.VOLEUR);
									sender.sendMessage(ChatColor.GREEN+"This player is now a thief");
									return true;
								}else {
									sender.sendMessage("");
									sender.sendMessage(ChatColor.RED+"This player is not in a game!");
									sender.sendMessage("");
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
								sender.sendMessage("");
								sender.sendMessage(ChatColor.RED+"This player is not online!");
								sender.sendMessage("");
								return true;
							}else {
								if (mainref.isPlayerIngame(p)) {
									mainref.getPlayerWrapper(p).setTeam(Vi5Team.SPECTATEUR);
									sender.sendMessage(ChatColor.GREEN+"This player is now a spectator");
									return true;
								}else {
									sender.sendMessage("");
									sender.sendMessage(ChatColor.RED+"This player is not in a game!");
									sender.sendMessage("");
									return true;
								}
							}
						}else {
							mainref.getPlayerWrapper(player).setTeam(Vi5Team.SPECTATEUR);
							return true;
						}
					default:
						sender.sendMessage("");
						sender.sendMessage(ChatColor.RED+"Only thief/guard/spectator are valid teams!");
						sender.sendMessage("");
						return true;
					}
				}else {
					sender.sendMessage("");
					sender.sendMessage(ChatColor.RED+"You need to be in game to use this command!");
					sender.sendMessage("");
					return true;
				}
			}else {
				sender.sendMessage("");
				sender.sendMessage(ChatColor.RED+"You need to be a player to use this command!");
				sender.sendMessage("");
				return true;
			}
		}
	}
}