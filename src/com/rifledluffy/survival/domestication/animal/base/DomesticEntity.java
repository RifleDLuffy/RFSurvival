package com.rifledluffy.survival.domestication.animal.base;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.rifledluffy.survival.domestication.events.AnimalConsumeEvent;
import com.rifledluffy.survival.domestication.food.FoodSource;

public interface DomesticEntity {
	
	/*
	 * Simple Actions
	 */
	
	public void move(Location location);
	
	public void move(Entity entity);
	
	public void look(Location location);
	
	public void look(Entity entity);
	
	public void look(LivingEntity entity);

	public void breed(Animals target);

	/*
	 * Survival Features
	 */
	
	public boolean consume(AnimalConsumeEvent event);
	
	public boolean drink(Block water);
	
	public boolean eat(Item item);
	
	public boolean eat(FoodSource source);
	
	public boolean eat(Inventory inventory, ItemStack item);
	
	public void shelter(Location location);
	
	/*
	 * Other Features
	 */
	
	public void steal(ItemStack item);
}
