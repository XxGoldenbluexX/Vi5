package fr.vi5team.vi5;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import fr.vi5team.vi5.enums.RunesList;

public abstract class BaseRune implements Listener {
	
	protected final Player player;
	protected final PlayerWrapper wraper;
	protected final Vi5Main mainref;
	protected final RunesList Rune;
	private int cooldown=0;
	private final BukkitRunnable cooldownTimer = new BukkitRunnable() {
		@Override
		public void run() {cooldownTick();}
		};
	
	public BaseRune(Vi5Main _mainref,PlayerWrapper _wraper, Player _player,RunesList _rune) {
		player=_player;
		wraper=_wraper;
		mainref=_mainref;
		Rune=_rune;
	}
	
	public abstract void cast();
	public abstract void tick();
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().equals(player)) {
			if (event.getItemDrop().getItemStack().getType()==Rune.getHotbarItem().getType()) {
				event.setCancelled(true);
				cast();
			}
		}
	}
	
	private void cooldownTick() {
		cooldown--;
		if (cooldown<=0){
			cooldown=0;
			setCurrentHotbarItem(Rune.getHotbarItem());
			player.sendMessage(ChatColor.DARK_PURPLE+Rune.getDisplayName()+ChatColor.LIGHT_PURPLE+" is now usable");
		}else {
			setCurrentHotbarItem(getCooldownItem(cooldown));
			cooldownTimer.cancel();
			cooldownTimer.runTaskLater(mainref, 20);
		}
	}
	protected void setCurrentHotbarItem(ItemStack itm) {
		switch (Rune.getTiers()) {
		case PRIMAIRE:
			player.getInventory().setItem(1, itm);
			break;
		case SECONDAIRE:
			player.getInventory().setItem(2, itm);
			break;
		case TERTIAIRE:
			player.getInventory().setItem(3, itm);
			break;
		}
	}
	
	public void setCooldown(int _cooldown) {
		cooldown = _cooldown;
		setCurrentHotbarItem(getCooldownItem(cooldown));
		cooldownTimer.cancel();
		cooldownTimer.runTaskLater(mainref, 20);
	}
	
	public boolean onCooldown() {
		if (cooldown<=0) {
			return true;
		}else {
			return false;
		}
	}
	
	public static ItemStack getCooldownItem(int cooldown) {
		return new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE,cooldown);
	}
	
	public void gameStart() {
		mainref.getPmanager().registerEvents(this, mainref);
		switch (Rune.getType()) {
		case PASSIF:
			ItemStack it = Rune.getHotbarItem().clone();
			it.addUnsafeEnchantment(Enchantment.MENDING, 10);
			ItemMeta meta =  it.getItemMeta();
			meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
			it.setItemMeta(meta);
			setCurrentHotbarItem(it);
			break;
		case SPELL:
			setCurrentHotbarItem(Rune.getHotbarItem());
			cooldown=0;
			break;
		}
	}
	
	
	public Player getPlayer() {
		return player;
	}

	public PlayerWrapper getWraper() {
		return wraper;
	}

	public Vi5Main getMainref() {
		return mainref;
	}

	public int getCooldown() {
		return cooldown;
	}

	public RunesList getRune() {
		return Rune;
	}
}