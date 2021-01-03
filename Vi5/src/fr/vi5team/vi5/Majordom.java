package fr.vi5team.vi5;

import java.util.ArrayList;

import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.scheduler.BukkitRunnable;

public class Majordom{

	private static Vi5Main main;
	private static final ArrayList<Majordom> list = new ArrayList<Majordom>();
	private static final int CLOSE_DELAY=40;
	
	private final boolean state;
	private final Block block;
	private final Openable openable;
	private final BukkitRunnable runnable;
	
	private Majordom(Block b,Openable o) {
		block=b;
		openable=o;
		state=o.isOpen();
		o.setOpen(!state);
		block.setBlockData(openable);
		runnable = new BukkitRunnable() {
			@Override
			public void run() {
				openable.setOpen(state);
				block.setBlockData(openable);
				remove();
			}
		};
		runnable.runTaskLater(main, CLOSE_DELAY);
	}
	
	public void abort() {
		runnable.cancel();
		openable.setOpen(state);
		block.setBlockData(openable);
		remove();
	}
	
	private void remove() {
		list.remove(this);
	}
	
	public void CancelManagment(Block b) {
		for (Majordom m : list) {
			if (m.getBlock().equals(b)) {
				m.runnable.cancel();
				m.remove();
			}
		}
	}
	
	private Block getBlock() {
		return block;
	}
	
	public static void setMainRef(Vi5Main mainref) {
		main=mainref;
	}
	
	public static Majordom getManager(Block b) {
		for (Majordom m : list) {
			if (m.getBlock().equals(b)) {
				return m;
			}
		}
		return null;
	}
	
	public static void add(Block b,Openable o) {
		list.add(new Majordom(b,o));
	}
	
}
