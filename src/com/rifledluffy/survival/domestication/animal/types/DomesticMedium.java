package com.rifledluffy.survival.domestication.animal.types;

import com.rifledluffy.survival.RFSurvival;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;

import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.animal.base.DomesticEntity;
import com.rifledluffy.survival.domestication.animal.base.DomesticSize;

public class DomesticMedium extends DomesticAnimal implements DomesticEntity {
	
	protected static int thirstThreshold = 60;
	protected static int hungerThreshold = 60;
	
	public DomesticMedium(Animals animal, int thirstThreshold, int hungerThreshold) {
		super(animal, thirstThreshold, hungerThreshold, DomesticSize.MEDIUM);

		FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();
		ConfigurationSection thresholds = config.getConfigurationSection("Animals.Thresholds.Medium");

		DomesticMedium.hungerThreshold = thresholds.getInt("HungerThreshold", 60);
		DomesticMedium.thirstThreshold = thresholds.getInt("ThirstThreshold", 60);
	}
	
	public DomesticMedium(Animals animal, int hydration, int hunger, int thirstThreshold, int hungerThreshold) {
		super(animal, hydration, hunger, thirstThreshold, hungerThreshold, DomesticSize.MEDIUM);

		FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();
		ConfigurationSection thresholds = config.getConfigurationSection("Animals.Thresholds.Medium");

		DomesticMedium.hungerThreshold = thresholds.getInt("HungerThreshold", 60);
		DomesticMedium.thirstThreshold = thresholds.getInt("ThirstThreshold", 60);
	}
}
