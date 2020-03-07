package fr.vi5team.vi5.commands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
public class Vi5BaseCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		switch (arg3[0]) {
		case "game":
			gameCommand(arg0, arg1, arg2, arg3);
		case "map":
			mapCommand(arg0, arg1, arg2, arg3);
		}
		return false;
	}
	
	public void gameCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		switch (arg3[1]) {
		case "create":
		case "start":
		case "restart":
		case "stop":
		case "join":
		case "leave":
		case "list":
		}
	}
	
	public void mapCommand(CommandSender arg0, Command arg1, String arg2, String[] arg3) {
		switch (arg3[1]) {
		case "create":
		case "rename":
		case "list":
		}
	}
}