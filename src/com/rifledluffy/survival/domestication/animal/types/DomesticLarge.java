package com.rifledluffy.survival.domestication.animal.types;

import com.rifledluffy.survival.RFSurvival;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;

import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.animal.base.DomesticEntity;
import com.rifledluffy.survival.domestication.animal.base.DomesticSize;

public class DomesticLarge extends DomesticAnimal implements DomesticEntity {
	
	protected static int thirstThreshold = 80;
	protected static int hungerThreshold = 80;
	
	public DomesticLarge(Animals animal, int thirstThreshold, int hungerThreshold) {
		super(animal, thirstThreshold, hungerThreshold, DomesticSize.LARGE);

		FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();
		ConfigurationSection thresholds = config.getConfigurationSection("Animals.Thresholds.Large");

		DomesticLarge.hungerThreshold = thresholds.getInt("HungerThreshold", 80);
		DomesticLarge.thirstThreshold = thresholds.getInt("ThirstThreshold", 80);
	}

	public DomesticLarge(Animals animal, int hydration, int hunger, int thirstThreshold, int hungerThreshold) {
		super(animal, hydration, hunger, thirstThreshold, hungerThreshold, DomesticSize.LARGE);

		FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();
		ConfigurationSection thresholds = config.getConfigurationSection("Animals.Thresholds.Large");

		DomesticLarge.hungerThreshold = thresholds.getInt("HungerThreshold", 80);
		DomesticLarge.thirstThreshold = thresholds.getInt("ThirstThreshold", 80);
	}
	
}
