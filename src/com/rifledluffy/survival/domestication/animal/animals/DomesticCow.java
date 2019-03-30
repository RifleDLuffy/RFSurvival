package com.rifledluffy.survival.domestication.animal.animals;

import org.bukkit.entity.Cow;

import com.rifledluffy.survival.domestication.animal.types.DomesticLarge;

public class DomesticCow extends DomesticLarge {

	private Cow cow;
	
	DomesticCow(Cow cow) {
		super(cow, hungerThreshold, thirstThreshold);
		this.cow = cow;
	}
	
	DomesticCow(Cow cow, int hydration, int hunger) {
		super(cow, hydration, hunger, hungerThreshold, thirstThreshold);
		this.cow = cow;
	}
}
