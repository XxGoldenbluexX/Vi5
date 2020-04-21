package fr.vi5team.vi5.runes;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Rune_builder extends BaseRune{
	private static final byte MAX_WALLS=2;
	private byte NB_WALLS=0;
	private WeakHashMap<String,ArrayList<Location>> wallsInMapLocationsList = new WeakHashMap<String,ArrayList<Location>>();
	private WeakHashMap<String,ArrayList<Double>> wallsInMapCenterList = new WeakHashMap<String,ArrayList<Double>>();
	private ArrayList<String> wallsInGameList = new ArrayList<String>();
	public Rune_builder(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void cast() {
		if(NB_WALLS<MAX_WALLS) {
			Location ploc = player.getLocation();
			Double minDistance = Double.MAX_VALUE;
			String nearestWall=null;
			for(String wallName : wallsInMapCenterList.keySet()) {
				if(!wallsInGameList.contains(wallName)) {
					ArrayList<Double> loc = wallsInMapCenterList.get(wallName);
					Double coordX = loc.get(0);
					Double coordY = loc.get(1);
					Double coordZ = loc.get(2);
					Double distance = Math.sqrt((Math.pow(coordX-ploc.getX(), 2))+(Math.pow(coordY-ploc.getY(), 2))+(Math.pow(coordZ-ploc.getZ(), 2)));
					if(distance<minDistance) {
						minDistance=distance;
						nearestWall=wallName;
					}
				}
			}
			if(nearestWall==null) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED+"There is no more possible walls on the map. Consider adding more later"));
			}else {
				wallsInGameList.add(nearestWall);
				NB_WALLS++;
			}
			if(NB_WALLS==MAX_WALLS) {
				player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN+"You have placed your last wall: "+ChatColor.GOLD+ChatColor.UNDERLINE+nearestWall));		
				return;
			}
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.GREEN+"You have placed the wall: "+ChatColor.GOLD+ChatColor.UNDERLINE+nearestWall));	
			showAdaptedHotbarItem();
		}else {
			player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(ChatColor.RED+"All your walls have already been placed!"));	
			
		}
		// TODO Auto-generated method stub
		
	}
	private void showAdaptedHotbarItem() {
		ItemStack item;
		ItemMeta meta;
		item=new ItemStack(Material.BRICK_WALL);
		meta=item.getItemMeta();
		meta.setDisplayName(ChatColor.GOLD+"You can place "+ChatColor.AQUA+(MAX_WALLS-NB_WALLS)+ChatColor.GOLD+"more walls");
		item.setItemMeta(meta);
		setCastItem(item);
		showHotbarItem();
	}
	@Override
	public void tick() {
		for(Player p : wraper.getGame().getPlayerList()) {
			//
		}
	}

	@Override
	public void gameEnd() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gameStart() {
		Activate();
		showAdaptedHotbarItem();
		String mapName = wraper.getGame().getMapName();
		List<String> wallList = mainref.getCfgmanager().getMapWallsList(mapName);
		YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(mapName);
		for(String wallName : wallList) {
			ArrayList<Location> bothCorner = new ArrayList<Location>();
			Location loc1 = (Location) cfg.get("mapWalls.firstCorner");
			Location loc2 = (Location) cfg.get("mapWalls.secondCorner");
			bothCorner.add(loc1);
			bothCorner.add(loc2);
			wallsInMapLocationsList.put(wallName, bothCorner);
			ArrayList<Double> centerLoc = new ArrayList<Double>();
			centerLoc.add(loc1.getX()+(loc2.getX()-loc1.getX())/2);
			centerLoc.add(loc1.getY()+(loc2.getY()-loc1.getY())/2);
			centerLoc.add(loc1.getZ()+(loc2.getZ()-loc1.getZ())/2);
			wallsInMapCenterList.put(wallName, centerLoc);
		}
			
	}

	@Override
	public void enterZone() {
		// TODO Auto-generated method stub
		
	}

}
