package com.rifledluffy.survival.domestication.animal.group;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public interface Pack extends Group {

	@Override
	void move(Location location);

	@Override
	void move(Block block);

	@Override
	void avoid(LivingEntity entity);
	
	void attack(LivingEntity entity);
	
	void regroup(Location location);
	
	void regroup(Block block);
}
