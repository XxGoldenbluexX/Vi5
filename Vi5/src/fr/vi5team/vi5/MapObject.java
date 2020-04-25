package fr.vi5team.vi5;


import java.util.ArrayList;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;

import fr.vi5team.vi5.enums.Vi5Team;
import fr.vi5team.vi5.enums.VoleurStatus;


public class MapObject implements Listener{
	
	public enum CaptureState{
		STEALABLE,//l'objet peut etre volé
		CARRIED,//l'objet est porté par un voleur
		STEALED//un voleur c'est enfuit avec cet objet
	}
	
	private Game gameref;
	public CaptureState captureState=CaptureState.STEALABLE;
	public short captureLevel=0;//variable représentant le niveau de capture de l'objet (captureLevel/MAX_CAPTURE_LEVEL)*100 = pourcentage de capture
	public final short MAX_CAPTURE_LEVEL=100;//constante représentant le niveau maximum de la jauge de capture
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
		boolean xx = ( position.getX()+xsize >= playerx && playerx >= position.getX()-xsize);
		boolean yy = ( position.getY()+ysize >= playery && playery >= position.getY()-ysize);
		boolean zz = ( position.getZ()+zsize >= playerz && playerz >= position.getZ()-zsize);
		return (xx && yy && zz);//la ligne de code de 80m me stressait
	}
	public boolean isGuardOnPoint() {
		for(Player p:playersOnObject) {
			PlayerWrapper wrap = gameref.getPlayerWrapper(p);
			if (wrap!=null) {
				if (wrap.getTeam()==Vi5Team.GARDE) {
					return true;
				}
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
		if (captureLevel>0 && captureState==CaptureState.STEALABLE) {
			captureLevel--;
		}
		for (Player p : playersOnObject) {
			if (captureState==CaptureState.STEALABLE && !captureCooldown) {
				PlayerWrapper wrap = gameref.getPlayerWrapper(p);
				if (wrap!=null) {
					if (wrap.getTeam()==Vi5Team.VOLEUR && wrap.getCurrentStatus()==VoleurStatus.INSIDE) {
						if (captureLevel<MAX_CAPTURE_LEVEL) {
							if (isGuardOnPoint()) {
								p.sendTitle("", ChatColor.RED+"Capture paused (a guard is near)", 0, 2, 0);
								return;
							}
							captureLevel+=2;
							int percent = Math.floorDiv(captureLevel*100,MAX_CAPTURE_LEVEL);
							p.sendTitle("", ChatColor.DARK_GREEN+"Capturing "+ChatColor.RED+ChatColor.UNDERLINE+objectName+" : "+percent+"%", 0, 2, 0);
						}else {
							capture(p,wrap);
						}
					}
				}
			}
		}
	}	
	
	public void capture(Player player,PlayerWrapper wrap) {
		//called when this point is captured
		captureState=CaptureState.CARRIED;
		wrap.addItemStealed();
		removeBlock();
		gameref.messageTeam(Vi5Team.GARDE, ChatColor.RED+"An object has been stolen");
		gameref.messageTeam(Vi5Team.VOLEUR, ChatColor.GOLD+player.getName()+ChatColor.AQUA+" stole "+ChatColor.RED+ChatColor.UNDERLINE+objectName);
		gameref.titleTeam(Vi5Team.GARDE, ChatColor.RED+"An object has been stolen", "", 5, 20, 20);
		gameref.titleTeam(Vi5Team.VOLEUR, ChatColor.RED+objectName+ChatColor.GOLD+" stole", ChatColor.GOLD+"by "+ChatColor.AQUA+player.getName(), 5, 20, 20);
		gameref.launchCaptureDelay();
		wrap.setLeaveCooldown(true);
		new BukkitRunnable() {
			@Override
			public void run() {
				player.sendMessage(ChatColor.DARK_GREEN+"You can leave the complex");
				wrap.setLeaveCooldown(false);
			}
		}.runTaskLater(gameref.getMainRef(), 600);
	}
	
	public void unregisterEvents() {
		HandlerList.unregisterAll(this);
	}
		
	public MapObject(Game game, String _objectName, Location _position, Location _blockPosition,BlockData bdata,Material _blockType,int sizex,int sizey,int sizez) {
		blockPosition = _blockPosition;
		blockData=bdata;
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
	public Location getBlockPosition() {
		return blockPosition;
	}
	public void setBlock() {
		Block block = blockPosition.getWorld().getBlockAt(blockPosition);
		block.setType(getBlockType());
		block.setBlockData(blockData);
	}
	public void removeBlock() {
		Block block = blockPosition.getWorld().getBlockAt(blockPosition);
		block.setType(Material.AIR);
	}
	public Location getPosition() {
		return position;
	}
	public int getXsize() {
		return xsize;
	}
	public int getYsize() {
		return ysize;
	}
	public int getZsize() {
		return zsize;
	}
	public BlockData getBlockData() {
		return blockData;
	}
	public boolean isCaptureCooldown() {
		return captureCooldown;
	}
	public void setCaptureCooldown(boolean captureCooldown) {
		this.captureCooldown = captureCooldown;
	}
}

