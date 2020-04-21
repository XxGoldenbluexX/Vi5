package fr.vi5team.vi5.enums;

public enum RunesTiers {
	PRIMAIRE(1),
	SECONDAIRE(2),
	TERTIAIRE(3);
	
	private final int InventorySlot;
	
	private RunesTiers(int _inventorySlot) {
		InventorySlot=_inventorySlot;
	}

	public int getInventorySlot() {
		return InventorySlot;
	}
	
}