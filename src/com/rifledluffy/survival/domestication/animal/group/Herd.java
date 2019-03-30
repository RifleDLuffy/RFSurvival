package com.rifledluffy.survival.domestication.animal.group;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.LivingEntity;

public interface Herd extends Group {

	@Override
	void move(Location location);

	@Override
	void move(Block block);

	@Override
	void avoid(LivingEntity entity);

}
