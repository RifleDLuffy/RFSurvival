package com.rifledluffy.survival.domestication.animal.types;

import com.rifledluffy.survival.RFSurvival;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;

import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.animal.base.DomesticEntity;
import com.rifledluffy.survival.domestication.animal.base.DomesticSize;

public class DomesticSmall extends DomesticAnimal implements DomesticEntity {
	
	protected static int thirstThreshold = 50;
	protected static int hungerThreshold = 50;
	
	public DomesticSmall(Animals animal, int thirstThreshold, int hungerThreshold) {
		super(animal, thirstThreshold, hungerThreshold, DomesticSize.SMALL);

		FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();
		ConfigurationSection thresholds = config.getConfigurationSection("Animals.Thresholds.Small");

		DomesticSmall.hungerThreshold = thresholds.getInt("HungerThreshold", 50);
		DomesticSmall.thirstThreshold = thresholds.getInt("ThirstThreshold", 50);
	}
	
	public DomesticSmall(Animals animal, int hydration, int hunger, int thirstThreshold, int hungerThreshold) {
		super(animal, hydration, hunger, thirstThreshold, hungerThreshold, DomesticSize.SMALL);

		FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();
		ConfigurationSection thresholds = config.getConfigurationSection("Animals.Thresholds.Small");

		DomesticSmall.hungerThreshold = thresholds.getInt("HungerThreshold", 50);
		DomesticSmall.thirstThreshold = thresholds.getInt("ThirstThreshold", 50);
	}
}
