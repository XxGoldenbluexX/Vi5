package fr.vi5team.vi5.runes;

import java.util.ArrayList;
import java.util.WeakHashMap;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.Game;
import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class Rune_builder extends BaseRune{
	private static final byte MAX_WALLS=2;
	private byte NB_WALLS=0;
	private WeakHashMap<String,ArrayList<Location>> wallsInMapLocationsList = new WeakHashMap<String,ArrayList<Location>>();
	private WeakHashMap<String,ArrayList<Double>> wallsInMapCenterList = new WeakHashMap<String,ArrayList<Double>>();
	private ArrayList<String> wallsInGameList = new ArrayList<String>();
	private WeakHashMap<Player,String> playerOnWall = new WeakHashMap<Player,String>();
	public Rune_builder(Vi5Main _mainref, PlayerWrapper _wraper, Player _player, RunesList _rune) {
		super(_mainref, _wraper, _player, _rune);
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
		showCastItem();
	}
	public boolean isPlayerOnWall(Location ploc, String wallName) {
		ArrayList<Location> cornersLoc = wallsInMapLocationsList.get(wallName);
		Location loc1=cornersLoc.get(0);
		Location loc2=cornersLoc.get(1);
		boolean xCheck=false;
		boolean zCheck=false;
		if(loc1.getX()>loc2.getX()) {
			if(ploc.getX()<=loc1.getX()+1&&ploc.getX()>=loc2.getX()-1) {
				xCheck=true;
			}
		}else {
			if(ploc.getX()<=loc2.getX()+1&&ploc.getX()>=loc1.getX()-1) {
				xCheck=true;
			}
		}
		if(xCheck) {
			if(loc1.getZ()>loc2.getZ()) {
				if(ploc.getZ()<=loc1.getZ()+1&&ploc.getZ()>=loc2.getZ()-1) {
					zCheck=true;
				}
			}else {
				if(ploc.getZ()<=loc2.getZ()+1&&ploc.getZ()>=loc1.getZ()-1) {
					zCheck=true;
				}
			}
		}else {
			return false;
		}
		if(zCheck) {
			if(loc1.getY()>loc2.getY()) {
				if(ploc.getY()<=loc1.getY()+1&&ploc.getY()>=loc2.getY()-1) {
					return true;
				}
			}else {
				if(ploc.getZ()<=loc2.getZ()+1&&ploc.getZ()>=loc1.getZ()-1) {
					return true;
				}
			}
		}
		return false;
	}
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		PlayerWrapper wrap = mainref.getPlayerWrapper(p);
		if(wrap.getTeam()==Vi5Team.GARDE||wrap.getRuneSecondaire().Rune==RunesList.CROCHETEUR) {
			for(String wallName : wallsInGameList) {
				boolean playerInZone=isPlayerOnWall(p.getLocation(),wallName);
				if(!playerOnWall.keySet().contains(p)&&playerInZone) {
					if(!playerOnWall.values().contains(wallName)) {
						setBlocks(true,wallName);
					}
					playerOnWall.put(p, wallName);
				}else {
					if(playerOnWall.keySet().contains(p)&&!playerInZone) {
						playerOnWall.remove(p);
						if(!playerOnWall.values().contains(wallName)) {
							setBlocks(false,wallName);
						}
					}
				}
			}
		}
	}
	@Override
	public void tick() {
	}
	public void setBlocks(boolean remove, String wallName) {
		Location cornerLoc = wallsInMapLocationsList.get(wallName).get(0);
		Double xDiff=wallsInMapLocationsList.get(wallName).get(0).getX()-wallsInMapLocationsList.get(wallName).get(1).getX();
		boolean xInvert=false;
		boolean yInvert=false;
		boolean zInvert=false;
		double toAddX;
		double toAddY;
		double toAddZ;
		if(xDiff<0) {
			xInvert=true;
			xDiff=xDiff*-1;
		}
		Double yDiff=wallsInMapLocationsList.get(wallName).get(0).getY()-wallsInMapLocationsList.get(wallName).get(1).getY();
		if(yDiff<0) {
			yInvert=true;
			yDiff=yDiff*-1;
		}
		Double zDiff=wallsInMapLocationsList.get(wallName).get(0).getZ()-wallsInMapLocationsList.get(wallName).get(1).getZ();
		if(zDiff<0) {
			zInvert=true;
			zDiff=zDiff*-1;
		}
		for (Double x =0d; x<=xDiff; x++) {
			for (Double y =0d; y<=yDiff; y++) {
				for (Double z =0d; z<=zDiff; z++) {
					if(xInvert) {
						toAddX=-x;
					}else {
						toAddX=x;
					}
					if(yInvert) {
						toAddY=-y;
					}else {
						toAddY=y;
					}
					if(zInvert) {
						toAddZ=-z;
					}else {
						toAddZ=z;
					}
					Location loc = new Location(player.getWorld(),cornerLoc.getX()+toAddX,cornerLoc.getY()+toAddY,cornerLoc.getZ()+toAddZ);
					if(remove) {
						loc.getBlock().setType(Material.AIR);
					}else {
						loc.getBlock().setType(Material.BLACK_STAINED_GLASS);
					}	
				}
			}
		}
	}
	@Override
	public void gameEnd() {
		for(String wallName : wallsInGameList) {
			if(!playerOnWall.values().contains(wallName)) {
				setBlocks(true,wallName);
			}
		}
	}
//
	@Override
	public void gameStart() {
		Activate();
		showAdaptedHotbarItem();
		Game game = wraper.getGame();
		wallsInMapLocationsList = game.wallsInMapLocationsList;
		wallsInMapCenterList = game.wallsInMapCenterList;
	}

	@Override
	public void enterZone() {
		// TODO Auto-generated method stub
		
	}

}
