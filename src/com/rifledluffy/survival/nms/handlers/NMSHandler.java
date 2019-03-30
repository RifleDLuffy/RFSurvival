package com.rifledluffy.survival.nms.handlers;

import org.bukkit.Location;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

public interface NMSHandler {

	void pathToLoc(Creature creature, Location location, float speed);
	
	void pathToEntity(Creature creature, Entity target, float speed);

	void breed(Animals breeder, Animals toBreed);
}
