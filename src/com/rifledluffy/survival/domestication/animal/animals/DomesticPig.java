package com.rifledluffy.survival.domestication.animal.animals;

import org.bukkit.entity.Pig;

import com.rifledluffy.survival.domestication.animal.types.DomesticMedium;

public class DomesticPig extends DomesticMedium {

	private Pig pig;
	
	DomesticPig(Pig pig) {
		super(pig, hungerThreshold, thirstThreshold);
		this.pig = pig;
	}
	
	DomesticPig(Pig pig, int hydration, int hunger) {
		super(pig, hydration, hunger, hungerThreshold, thirstThreshold);
		this.pig = pig;
	}
}
