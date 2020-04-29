package fr.vi5team.vi5;

import java.util.ArrayList;
import java.util.List;
import java.util.WeakHashMap;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.vi5team.vi5.enums.RunesList;
import fr.vi5team.vi5.enums.Vi5Team;

public class MapWall implements Listener{
	private Vi5Main mainref;
	private String mapName;
	private ArrayList<String> wallsInGame = new ArrayList<String>();
	private ArrayList<String> wallsInMap = new ArrayList<String>();
	private WeakHashMap<Player,String> playerOnWall = new WeakHashMap<Player,String>();
	private WeakHashMap<String,ArrayList<Location>> wallsInMapLocations = new WeakHashMap<String,ArrayList<Location>>();
	private WeakHashMap<String,ArrayList<Double>> wallsInMapCenter = new WeakHashMap<String,ArrayList<Double>>();
	private World world;
	
	public MapWall(Vi5Main _mainref,World _world,String _mapName) {
		mainref=_mainref;
		mapName=_mapName;
		world=_world;
		loadMapWalls();
	}
	public void loadMapWalls() {
		List<String> wallList = mainref.getCfgmanager().getMapWallsList(mapName);
		YamlConfiguration cfg = mainref.getCfgmanager().getMapConfig(mapName);
		for(String wallName : wallList) {
			ArrayList<Location> bothCorner = new ArrayList<Location>();
			Location loc1 = (Location) cfg.get("mapWalls."+wallName+".firstCorner");
			Location loc2 = (Location) cfg.get("mapWalls."+wallName+".secondCorner");
			if(loc1!=null&&loc2!=null) {
				wallsInMap.add(wallName);
				bothCorner.add(loc1);
				bothCorner.add(loc2);
				wallsInMapLocations.put(wallName, bothCorner);
				ArrayList<Double> centerLoc = new ArrayList<Double>();
				centerLoc.add(loc1.getX()+(loc2.getX()-loc1.getX())/2);
				centerLoc.add(loc1.getY()+(loc2.getY()-loc1.getY())/2);
				centerLoc.add(loc1.getZ()+(loc2.getZ()-loc1.getZ())/2);
				wallsInMapCenter.put(wallName, centerLoc);
			}
		}
	}
	public void addPlayerOnWall(Player p, String wallName) {
		if(!wallsInGame.contains(wallName)) {
			wallsInGame.add(wallName);
		}
		playerOnWall.put(p, wallName);
	}
	public void removeAllWalls() {
		for(String wallName : wallsInGame) {
			setBlocks(true,wallName);
		}
	}
	public void removePlayerOnWall(Player p) {
		String wall = playerOnWall.get(p);
		playerOnWall.remove(p);
		if(!playerOnWall.values().contains(wall)) {
			wallsInGame.remove(wall);
		}
	}
	public String isPlayerOnWall(Player p) {
		Location ploc = p.getLocation();
		for(String wallName : wallsInGame) {
			ArrayList<Location> cornersLoc = wallsInMapLocations.get(wallName);
			Location loc1=cornersLoc.get(0);
			Location loc2=cornersLoc.get(1);
			boolean xCheck=false;
			boolean yCheck=false;
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
				if(loc1.getY()>loc2.getY()) {
					if(ploc.getY()<=loc1.getY()+1&&ploc.getY()>=loc2.getY()-1) {
						yCheck=true;
					}
				}else {
					if(ploc.getY()<=loc2.getY()+1&&ploc.getY()>=loc1.getY()-1) {
						yCheck=true;
					}
				}
			}
			if(yCheck) {
				if(loc1.getZ()>loc2.getZ()) {
					if(ploc.getZ()<=loc1.getZ()+1&&ploc.getZ()>=loc2.getZ()-1) {
						return wallName;
					}
				}else {
					if(ploc.getZ()<=loc2.getZ()+1&&ploc.getZ()>=loc1.getZ()-1) {
						return wallName;
					}
				}
			}
			
		}
		return null;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Player p = event.getPlayer();
		PlayerWrapper wrap = mainref.getPlayerWrapper(p);
		if(wrap.getTeam()==Vi5Team.GARDE||wrap.getRuneSecondaire().getRune()==RunesList.CROCHETEUR) {
			String wallName=isPlayerOnWall(p);
			if(!playerOnWall.keySet().contains(p)&&wallName!=null) {
					//IL RENTRE
				if(!playerOnWall.values().contains(wallName)) {
					setBlocks(true,wallName);
				}
				addPlayerOnWall(p,wallName);
			}else {
				if(playerOnWall.keySet().contains(p)&&wallName==null) {
						//IL SORT
					removePlayerOnWall(p);
					if(!playerOnWall.values().contains(wallName)) {
						setBlocks(false,wallName);
					}
				}
			}
		}
	}
	public String getNearestWall(Player p) {
		Location ploc = p.getLocation();
		Double minDistance = Double.MAX_VALUE;
		String nearestWall=null;
		for(String wallName : wallsInMap) {
			if(!wallsInGame.contains(wallName)) {
				ArrayList<Double> loc = wallsInMapCenter.get(wallName);
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
		if(nearestWall!=null) {
			wallsInGame.add(nearestWall);
			setBlocks(false,nearestWall);
		}
		return nearestWall;
	}
	public void setBlocks(boolean remove, String wallName) {
		Location cornerLoc1 = wallsInMapLocations.get(wallName).get(0);
		Location cornerLoc2 = wallsInMapLocations.get(wallName).get(1);
		boolean xInvert=false;
		boolean yInvert=false;
		boolean zInvert=false;
		double toAddX;
		double toAddY;
		double toAddZ;
		Double xDiff=cornerLoc1.getX()-cornerLoc2.getX();
		if(xDiff<0) {
			xInvert=true;
			xDiff=xDiff*-1;
		}
		Double yDiff=cornerLoc1.getY()-cornerLoc2.getY();
		if(yDiff<0) {
			yInvert=true;
			yDiff=yDiff*-1;
		}
		Double zDiff=cornerLoc1.getZ()-cornerLoc2.getZ();
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
					}//
					Location loc = new Location(world,cornerLoc2.getX()+toAddX,cornerLoc2.getY()+toAddY,cornerLoc2.getZ()+toAddZ);
					if(remove) {
						loc.getBlock().setType(Material.AIR);
					}else {
						loc.getBlock().setType(Material.BLACK_STAINED_GLASS);
					}	
				}
			}
		}
	}
}
