package fr.vi5team.vi5;


import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import fr.vi5team.vi5.enums.Vi5Team;

public class MapObject implements Listener {
	
	public enum CaptureState{
		STEALABLE,//l'objet peut etre volé
		CARRIED,//l'objet est porté par un voleur
		STEALED//un voleur c'est enfuit avec cet objet
	}
	
	private Game gameref;
	public CaptureState captureState=CaptureState.STEALABLE;
	public short captureLevel=0;//variable représentant le niveau de capture de l'objet (captureLevel/MAX_CAPTURE_LEVEL)*100 = pourcentage de capture
	public final short MAX_CAPTURE_LEVEL=1000;//constante représentant le niveau maximum de la jauge de capture
	public boolean captureCooldown=false;//dit si oui ou non l'objet est incapturable a cause du délais de capture
	private final String objectName;
	private final Location position;
	private final Location blockPosition;
	private final BlockData blockData;
	private final Material blockType;
	private final int xsize;
	private final int ysize;
	private final int zsize;
	private ArrayList<Player> playersOnObject=new ArrayList<Player>();
	
	
	public boolean isPlayerInZone(Location playerloc) {
		double playerx = playerloc.getX();
		double playery = playerloc.getY();
		double playerz = playerloc.getZ();
		boolean xx = ( position.getBlockX()+xsize > playerx && playerx > position.getBlockX()-xsize);
		boolean yy = ( position.getBlockY()+ysize > playery && playery > position.getBlockY()-ysize);
		boolean zz = ( position.getBlockZ()+zsize > playerz && playerz > position.getBlockZ()-zsize);
		return (xx && yy && zz);//la ligne de code de 80m me stressait
	}
	public boolean isGuardOnPoint() {
		for(Player p:playersOnObject) {
			PlayerWrapper wrap = gameref.getPlayerWrapper(p);
			if (wrap.getTeam()==Vi5Team.GARDE) {
				return true;
			}
		}
	return false;
	}
	@EventHandler
	public void onPlayerMove(PlayerMoveEvent event) {
		Location playerNewLocation = event.getTo();
		Player player = event.getPlayer();
		if(!playersOnObject.contains(player) && isPlayerInZone(playerNewLocation)) {
			playersOnObject.add(player);
		}
		if(playersOnObject.contains(player) && !isPlayerInZone(playerNewLocation)) {
			playersOnObject.remove(player);
		}
		return;
	}
	public void Tick() {
		if (captureState==CaptureState.STEALABLE && !captureCooldown) {
			for (Player p : playersOnObject) {
				PlayerWrapper wrap = gameref.getPlayerWrapper(p);
				if (wrap!=null) {
					if (wrap.getTeam()==Vi5Team.VOLEUR) {
						if (captureLevel<MAX_CAPTURE_LEVEL) {
							captureLevel++;
							int percent = (captureLevel/MAX_CAPTURE_LEVEL)*100;
							p.sendTitle("", ChatColor.GREEN+"Capturing "+ChatColor.RED+ChatColor.UNDERLINE+objectName+" :"+percent+"%", 0, 1, 0);
						}else {
							capture();
						}
					}
				}
			}
		}	
	}
	
	public void capture() {
		//called when this point is captured
		captureState=CaptureState.CARRIED;
		removeBlock();
	}
		
	public MapObject(Game game, String _objectName, Location _position, Location _blockPosition,BlockData _blockData,Material _blockType,int sizex,int sizey,int sizez) {
		blockData = _blockData;
		blockPosition = _blockPosition;
		blockType = _blockType;
		position = _position;
		xsize = sizex;
		ysize = sizey;
		zsize = sizez;
		objectName = _objectName;
		gameref = game;
		}
	public String getObjectName() {
		return objectName;
	}
	public Material getBlockType() {
		return blockType;
	}
	public BlockData getBlockData() {
		return blockData;
	}
	public Location getBlockPosition() {
		return blockPosition;
	}
	public void setBlock() {
		Block block = blockPosition.getWorld().getBlockAt(blockPosition);
		block.setType(getBlockType());
		block.setBlockData(getBlockData());
	}
	public void removeBlock() {
		Block block = blockPosition.getWorld().getBlockAt(blockPosition);
		block.setType(Material.AIR);
		block.setBlockData(null);
	}
}

