package fr.vi5team.vi5.runes;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.attribute.AttributeModifier.Operation;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import fr.vi5team.vi5.PlayerWrapper;
import fr.vi5team.vi5.Vi5Main;
import fr.vi5team.vi5.enums.RunesList;

/**
 * 
 * @author XxGoldenbluexX
 *
 */

public abstract class BaseRune implements Listener {
	/**
	 * Réference au joueur a qui appartient cette rune
	 */
	protected final Player player;
	/**
	 * Réference au PlayerWrapper du joueur a qui appartient cette rune
	 */
	protected final PlayerWrapper wraper;
	/**
	 * Réference au plugin
	 */
	protected final Vi5Main mainref;
	/**
	 * Réference a la rune (RunesList) que cette classe représente
	 */
	protected final RunesList Rune;
	/**
	 * Représente le cooldown de la rune
	 */
	private float cooldown=0;
	/**
	 * Réference a l'item qui représente actuellement la rune dans la hotbar
	 */
	private ItemStack castItem;
	private ItemStack hotbarItem;
	private ItemStack cooldownItem;
	public BaseRune(Vi5Main _mainref,PlayerWrapper _wraper, Player _player,RunesList _rune) {
		player=_player;
		wraper=_wraper;
		mainref=_mainref;
		Rune=_rune;
		castItem=Rune.getHotbarItem();
		hotbarItem=castItem;
		cooldownItem = new ItemStack(Material.LIGHT_GRAY_STAINED_GLASS_PANE);
	}

	public abstract void cast();
	public abstract void tick();
	public abstract void gameEnd();
	public abstract void gameStart();
	public abstract void enterZone();
	
	@EventHandler
	public void onPlayerDrop(PlayerDropItemEvent event) {
		if (event.getPlayer().equals(player)) {
			if (event.getItemDrop().getItemStack().isSimilar(hotbarItem)) {
				switch (Rune.getType()) {
				case PASSIF:
					event.setCancelled(true);
					break;
				case SPELL:
					if (event.getItemDrop().getItemStack().isSimilar(castItem)) {
					event.getItemDrop().remove();
					event.setCancelled(false);
					cast();
					}else {
						event.setCancelled(true);
					}
					break;
				default:
					break;
				}
			}
		}
	}
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if (event.getItem()==null || event.getHand()==EquipmentSlot.OFF_HAND) {
			return;
		}
		if (event.getPlayer().equals(player)) {
			if (event.getItem().isSimilar(hotbarItem)) {
				switch (Rune.getType()) {
				case PASSIF:
					event.setCancelled(true);
					break;
				case SPELL:
					if (event.getItem().isSimilar(castItem)) {
					event.setCancelled(true);
					cast();
					}else {
						event.setCancelled(true);
					}
					break;
				default:
					break;
				}
			}
		}
	}
	public void showCastItem() {
		player.getInventory().setItem(Rune.getTiers().getInventorySlot(), castItem);
		hotbarItem=castItem;
		return;
	}
	/**
	 * Set the item used to cast the spell.
	 * THIS WILL NOT PUT IT IN THE HOTBAR (use showHotBarItem to do it)
	 * @param it
	 * The item you will set.
	 */
	public void setCastItem(ItemStack it) {
		castItem=it;
	}
	public void pretick() {
		cooldownTick();
		tick();
	}
	public void preGameEnd() {
		gameEnd();
		unregisterEvents();
	}
	private void setCooldownItem() {
		cooldownItem.setAmount(Math.max(Math.floorDiv(Math.round((cooldown+1)*10), 10),1));
		ItemMeta meta = cooldownItem.getItemMeta();
		meta.setDisplayName(ChatColor.RED+Rune.getDisplayName()+": "+(float)Math.round(cooldown*10)/10f);
		cooldownItem.setItemMeta(meta);
		hotbarItem=cooldownItem;
		player.getInventory().setItem(Rune.getTiers().getInventorySlot(), cooldownItem);
	}
	public void Activate() {
		mainref.getPmanager().registerEvents(this,mainref);
		showCastItem();
	}
	public void setCooldown(float _cooldown) {
		cooldown=_cooldown;
		cooldownTick();
	}
	private void cooldownTick() {
		if (cooldown>0) {
			cooldown-=0.05;
			if (cooldown<=0) {
				cooldown=0;
				showCastItem();
			}else {
				setCooldownItem();
			}
		}
	}
	private void unregisterEvents() {
		HandlerList.unregisterAll(this);
	}

	public ItemStack getHotbarItem() {
		return hotbarItem;
	}
	public RunesList getRune() {
		return Rune;
	}
	public void setHotbarItem(ItemStack hotbarItem) {
		this.hotbarItem = hotbarItem;
	}
	protected ItemStack makeSpamSword(Material mat, int damage) {
		ItemStack itm = new ItemStack(mat);
		ItemMeta meta = itm.getItemMeta();
		int realDamages = Math.max(0, damage-1);
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_SPEED,new AttributeModifier("pvp_1.8",1000,Operation.ADD_NUMBER));
		meta.addAttributeModifier(Attribute.GENERIC_ATTACK_DAMAGE,new AttributeModifier("diamondSwordDamages",realDamages,Operation.ADD_NUMBER));
		meta.setUnbreakable(true);
		itm.setItemMeta(meta);
		return itm;
	}
}