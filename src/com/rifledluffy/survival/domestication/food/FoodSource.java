package com.rifledluffy.survival.domestication.food;

import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.utility.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;

import java.util.Map;

public class FoodSource {
	
	private Material material;
	private Location location;
	private int uses;

	public FoodSource(Block block) {
		if (Util.foodExists(block)) return;
		Map<Material, Integer> edibleBlocks = RFSurvival.inst().getAnimalManager().getEdibleBlocks().getFoodMap();
		if (!edibleBlocks.containsKey(block.getType())) return;
		this.material = block.getType();
		this.location = block.getLocation();
		this.uses = edibleBlocks.get(this.material);
		RFSurvival.inst().getAnimalManager().getFoodSources().add(this);
	}
	
	public FoodSource(Location location) {
		this(location.getBlock());
	}
	
	public boolean isEmpty() {
		return this.uses == 0;
	}
	
	public void destroy() {
		location.getBlock().setType(Material.AIR);
		location.getWorld().playSound(location,Sound.BLOCK_GRASS_BREAK, 3,0.1F);
		this.material = null;
		this.location = null;
		this.uses = 0;
	}
	
	public boolean eat() {
		if (uses > 0) uses--;
		else return false;
		return true;
	}

	public Material getMaterial() {
		return material;
	}

	public Location getLocation() {
		return location;
	}

	public int getUses() {
		return uses;
	}

}
