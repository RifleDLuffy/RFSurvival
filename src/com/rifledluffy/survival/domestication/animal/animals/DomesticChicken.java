package com.rifledluffy.survival.domestication.animal.animals;

import org.bukkit.entity.Chicken;

import com.rifledluffy.survival.domestication.animal.types.DomesticSmall;

public class DomesticChicken extends DomesticSmall {

	private Chicken chicken;
	
	DomesticChicken(Chicken chicken) {
		super(chicken, hungerThreshold, thirstThreshold);
		this.chicken = chicken;
	}
	
	DomesticChicken(Chicken chicken, int hydration, int hunger) {
		super(chicken, hydration, hunger, hungerThreshold, thirstThreshold);
		this.chicken = chicken;
	}
}
