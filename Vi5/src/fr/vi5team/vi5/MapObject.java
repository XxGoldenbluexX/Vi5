package fr.vi5team.vi5;


import java.util.ArrayList;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MapObject implements Listener {
	private Player player;
	private PlayerWrapper wrapper = new PlayerWrapper();
	private Location playerOldLocation;
	private Location playerNewLocation;
	private Game gameref;
	public byte captureState;
	private final String objectName;
	private final Location position;
	private final Location blockPosition;
	private final BlockData blockData;
	private final Material blockType;
	private final int xsize;
	private final int ysize;
	private final int zsize;
	private ArrayList<Player> playerOnObject=new ArrayList<Player>();
	
	public boolean isPlayerinZone(Location playerLocation, Location position, int xsize,int ysize,int zsize) {
		if(playerLocation.getBlockX()>=position.getBlockX()&&(playerLocation.getBlockX()<=position.getBlockX()+xsize)&&playerLocation.getBlockY()>=position.getBlockY()&&playerLocation.getBlockY()<=position.getBlockY()+ysize&&playerLocation.getBlockZ()>=position.getBlockZ()&&playerLocation.getBlockZ()<=position.getBlockZ()+zsize){
			return true;
		}
		return false;
		
	}	
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		playerOldLocation = event.getFrom();
		playerNewLocation = event.getTo();
		player = event.getPlayer();
		if(!isPlayerinZone(playerOldLocation, position, xsize, ysize, zsize)&&isPlayerinZone(playerNewLocation, position, xsize, ysize, zsize)) {
			playerOnObject.add(player);
		}
		if(isPlayerinZone(playerOldLocation, position, xsize, ysize, zsize)&&!isPlayerinZone(playerNewLocation, position, xsize, ysize, zsize)) {
			playerOnObject.remove(player);
		}
		return;
	}

	public void pointCapture() {
		
	}
	public void Tick() {
		if(playerOnObject != null) {
			//Tester si le gars est voleur
			pointCapture();
		}
	}
	
	public MapObject(Game game, String _objectName, Location _position, Location _blockPosition,BlockData _blockData,Material _blockType,int sizex,int sizey,int sizez) {
		this.blockData = _blockData;
		this.blockPosition = _blockPosition;
		this.blockType = _blockType;
		this.position = _position;
		this.xsize = sizex;
		this.ysize = sizey;
		this.zsize = sizez;
		this.objectName = _objectName;
		gameref = game;
		}
		
	}

