package com.rifledluffy.survival.domestication.animal.group;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.util.Vector;

import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.food.FoodSource;

public class Farm implements Herd {
	
	List<Location> shelters = new ArrayList<Location>();
	List<Location> waterSources = new ArrayList<Location>();
	List<FoodSource> foodSources = new ArrayList<FoodSource>();
	
	List<DomesticAnimal> animals = new ArrayList<DomesticAnimal>();

	@Override
	public void move(Location location) {
		for (DomesticAnimal animal : animals) animal.move(location);
	}

	@Override
	public void move(Block block) {
		for (DomesticAnimal animal : animals) animal.move(block.getLocation().add(0.5,0,0.5));
	}

	@Override
	public void avoid(LivingEntity entity) {}
	
	public void add(DomesticAnimal animal) {
		if (this.animals.contains(animal)) return;
		this.animals.add(animal);
	}
	
	public void add(List<DomesticAnimal> animals) {
		for (DomesticAnimal animal : animals) add(animal);
	}

	public List<Location> getShelters() {
		return shelters;
	}

	public List<Location> getWaterSources() {
		return waterSources;
	}

	public List<FoodSource> getFoodSources() {
		return foodSources;
	}
	
	public List<DomesticAnimal> getAnimals() {
		return animals;
	}
	
	public void addShelter(Location location) {
		if (!shelters.contains(location)) shelters.add(location);
	}
	
	public void addShelters(List<Location> locations) {
		locations.stream().filter(loc -> !shelters.contains(loc)).forEach(loc -> addShelter(loc));
	}
	
	public void addWaterSource(Location location) {
		if (!waterSources.contains(location)) waterSources.add(location);
	}
	
	public void addWaterSources(List<Location> locations) {
		locations.stream().filter(loc -> !waterSources.contains(loc)).forEach(loc -> addWaterSource(loc));
	}
	
	public void addFoodSource(FoodSource location) {
		if (!foodSources.contains(location)) foodSources.add(location);
	}
	
	public void addFoodSources(List<FoodSource> locations) {
		locations.stream().filter(loc -> !foodSources.contains(loc)).forEach(loc -> addFoodSource(loc));
	}

	public boolean canShelterAll() {
		return this.shelters.size() >= animals.size();
	}
	
	public void update() {
		for (Location location : shelters) if (location.getBlock().getLightFromSky() == (byte) 15) shelters.remove(location);
		for (Location location : waterSources) if (!(location.getBlock().getBlockData() instanceof Levelled)) waterSources.remove(location);
	}
	
	public Location getClosestShelter(Entity entity) {
		List<Block> locations = new ArrayList<Block>();
		shelters.stream().forEach(loc -> locations.add(loc.getBlock()));
		Collections.sort(locations, new SortBlocksByDistance(entity));
		return locations.get(0).getLocation();
	}
	
	public Location getClosestWaterSource(Entity entity) {
		List<Block> locations = new ArrayList<Block>();
		waterSources.stream().forEach(loc -> locations.add(loc.getBlock()));
		Collections.sort(locations, new SortBlocksByDistance(entity));
		return locations.get(0).getLocation();
	}
	
	public FoodSource getClosestFoodSource(Entity entity) {
		List<FoodSource> locations = foodSources;
		Collections.sort(locations, new SortFoodByDistance(entity));
		return locations.get(0);
	}
	
	class SortBlocksByDistance implements Comparator<Block> 
	{ 
		Entity entity;
		
		SortBlocksByDistance(Entity entity) {
			this.entity = entity;
		}

		@Override
		public int compare(Block a, Block b) {
			Location location = entity.getLocation();
			Location aLoc = a.getLocation().clone().add(new Vector(0.5,0.5,0.5));
			Location bLoc = b.getLocation().clone().add(new Vector(0.5,0.5,0.5));
			if (location.distance(aLoc) > location.distance(bLoc)) return 1;
			else if (location.distance(aLoc) > location.distance(bLoc)) return -1;
			else return 0;
		} 
	}
	
	class SortEntitiesByDistance implements Comparator<Entity> 
	{ 
		Entity entity;
		
		SortEntitiesByDistance(Entity entity) {
			this.entity = entity;
		}

		@Override
		public int compare(Entity a, Entity b) {
			Location location = entity.getLocation();
			Location aLoc = a.getLocation();
			Location bLoc = b.getLocation();
			if (location.distance(aLoc) > location.distance(bLoc)) return 1;
			else if (location.distance(aLoc) > location.distance(bLoc)) return -1;
			else return 0;
		} 
	}
	
	class SortFoodByDistance implements Comparator<FoodSource> 
	{ 
		Entity entity;
		
		SortFoodByDistance(Entity entity) {
			this.entity = entity;
		}

		@Override
		public int compare(FoodSource a, FoodSource b) {
			Location location = entity.getLocation();
			Location aLoc = a.getLocation().clone().add(new Vector(0.5,0.5,0.5));
			Location bLoc = b.getLocation().clone().add(new Vector(0.5,0.5,0.5));
			if (location.distance(aLoc) > location.distance(bLoc)) return 1;
			else if (location.distance(aLoc) > location.distance(bLoc)) return -1;
			else return 0;
		} 
	}

}
