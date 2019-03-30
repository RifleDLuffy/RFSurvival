package com.rifledluffy.survival.domestication.animal.animals;

import org.bukkit.entity.Animals;
import org.bukkit.entity.Chicken;
import org.bukkit.entity.Cow;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Pig;

public enum DomesticType {

	COW(EntityType.COW),
	PIG(EntityType.PIG),
	CHICKEN(EntityType.CHICKEN);
	
	DomesticType(EntityType type) {
		this.type = type;
	}
	
	private EntityType type;
	
	public EntityType getType() {
		return this.type;
	}
	
	public static DomesticAnimal getRespectiveAnimal(Animals animal, EntityType type) {
		switch(type) {
			case COW:
				return new DomesticCow((Cow) animal);
			case PIG:
				return new DomesticPig((Pig) animal);
			case CHICKEN:
				return new DomesticChicken((Chicken) animal);
			default:
				return new DomesticAnimal(null, 0, 0, null);
		}
	}
}
