package com.rifledluffy.survival.domestication.animal.group;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public interface Group {

	void move(Location location);
	
	void move(Block block);
	
	void avoid(LivingEntity entity);

}
