package com.rifledluffy.survival.domestication.events;

import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import com.rifledluffy.survival.domestication.animal.base.DomesticEntity;
import com.rifledluffy.survival.domestication.food.FoodSource;

public class AnimalConsumeEvent extends Event implements Cancellable {
	
	DomesticEntity animal;
	Item item;
	FoodSource block;
	Block water;
	ItemStack itemStack;
	Inventory inventory;
	ConsumeType type;
	
	boolean cancelled;
	
	public AnimalConsumeEvent(DomesticEntity animal, Item item) {
		this.animal = animal;
		this.item = item;
		this.type = ConsumeType.ITEM;
	}
	
	public AnimalConsumeEvent(DomesticEntity animal, Block water) {
		this.animal = animal;
		this.water = water;
		this.type = ConsumeType.WATER;
	}
	
	public AnimalConsumeEvent(DomesticEntity animal, FoodSource block) {
		this.animal = animal;
		this.block = block;
		this.type = ConsumeType.FOODSOURCE;
	}
	
	public AnimalConsumeEvent(DomesticEntity animal, Inventory inventory, ItemStack item) {
		this.animal = animal;
		this.itemStack = item;
		this.inventory = inventory;
		this.type = ConsumeType.INVENTORY;
	}
	
	public DomesticEntity getAnimal() {
		return animal;
	}

	public Item getItem() {
		return item;
	}

	public FoodSource getBlock() {
		return block;
	}

	public Block getWater() {
		return water;
	}

	public ItemStack getItemStack() {
		return itemStack;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public ConsumeType getType() {
		return type;
	}

	private static final HandlerList handlers = new HandlerList();

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}
	
	static public HandlerList getHandlerList() {
		return handlers;
	}

	@Override
	public boolean isCancelled() {
		return this.cancelled;
	}

	@Override
	public void setCancelled(boolean flag) {
		this.cancelled = flag;
	}
}
