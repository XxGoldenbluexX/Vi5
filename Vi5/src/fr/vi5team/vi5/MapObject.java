package fr.vi5team.vi5;


import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class MapObject implements Listener {
	private Player player;
	private String object_name;
	private ConfigManager ConfigManager = new ConfigManager(null);
	private Game gameref;

	
	public void onPlayerMove(PlayerMoveEvent event, String _objectName, Location _position, int sizex,int sizey,int sizez) {
		Location loc = event.getTo();
		if(loc.getBlockX()>=_position.getBlockX()&&(loc.getBlockX()<=_position.getBlockX()+sizex)&&loc.getBlockY()>=_position.getBlockY()&&loc.getBlockY()<=_position.getBlockY()+sizey&&loc.getBlockZ()>=_position.getBlockZ()&&loc.getBlockZ()<=_position.getBlockZ()+sizez){
			//code here
		}
	}
	public MapObject(Game game, String _objectName, Location _position, Location _blockPosition,BlockData _blockData,Material _blocType,int sizex,int sizey,int sizez) {
		gameref = game;
		
		
		
	}
}
