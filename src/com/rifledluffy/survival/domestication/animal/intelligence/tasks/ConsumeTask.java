package com.rifledluffy.survival.domestication.animal.intelligence.tasks;

import com.rifledluffy.api.custom.tasks.CustomRunnable;
import com.rifledluffy.api.util.Util;
import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.events.AnimalConsumeEvent;
import com.rifledluffy.survival.domestication.events.ConsumeType;
import com.rifledluffy.survival.domestication.food.FoodSource;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Item;
import org.bukkit.event.Event;

public class ConsumeTask {

	private DomesticAnimal animal;
	private Block block;
	
	private FoodSource food;
	private Levelled water;
	private Item item;
	private ConsumeType type;
	
	private Location target;
	private Location consumer;

	private boolean valid = true;
	private Event event;

	private CustomRunnable task;

	ConsumeTask(DomesticAnimal animal, FoodSource block) {
		this();
		this.animal = animal;
		this.block = block.getLocation().getBlock();
		this.food = block;
		this.type = ConsumeType.FOODSOURCE;
		this.event = new AnimalConsumeEvent(animal, block);
	}

	ConsumeTask(DomesticAnimal animal, Item item) {
		this();
		this.animal = animal;
		this.item = item;
		this.type = ConsumeType.ITEM;
		this.event = new AnimalConsumeEvent(animal, item);
	}

	ConsumeTask(DomesticAnimal animal, Block water) {
		this();
		this.animal = animal;
		this.block = water;
		this.water = (Levelled) water.getBlockData();
		this.type = ConsumeType.WATER;
		this.event = new AnimalConsumeEvent(animal, water);
	}

	private ConsumeTask() {
		task = new CustomRunnable(RFSurvival.inst(), () -> {
			switch(type) {
				case FOODSOURCE:
				case WATER:
					target = block.getLocation();
					animal.move(target);

					if (animal.mother != null && !animal.getAnimal().canBreed() && !animal.mother.occupied) animal.mother.move(target);
					else if (animal.mother != null) {
						stop();
						return;
					}

					consumer = animal.getAnimal().getLocation();
					if (consumer.distance(target) < 2) {
						CustomRunnable repeatConsume = new CustomRunnable(RFSurvival.inst(),
								() -> animal.move(target),
								() -> animal == null || animal.getAnimal() == null || animal.getAnimal().isDead() || !animal.consume((AnimalConsumeEvent) event));
						repeatConsume.runTimer(1,20);
						stop();
					}
					break;
				case ITEM:
					target = item.getLocation();
					if (item.isDead() || item.getItemStack().getType() == Material.AIR) {
						stop();
						return;
					}
					animal.move(target);

					if (animal.mother != null && !animal.getAnimal().canBreed() && !animal.mother.occupied) animal.mother.move(target);
					else {
						stop();
						return;
					}

					consumer = animal.getAnimal().getLocation();
					if (consumer.distance(target) < 1) {
						CustomRunnable repeatConsume = new CustomRunnable(RFSurvival.inst(),
								() -> animal.move(target),
								() -> animal == null || animal.getAnimal() == null || animal.getAnimal().isDead() || !animal.consume((AnimalConsumeEvent) event));
						repeatConsume.runTimer(1,20);
						stop();
					}
					break;
			}
		}, () -> animal == null || animal.getAnimal() == null || animal.getAnimal().isDead());
	}

	void run() {
		task.runTimer(1, 40);
	}

	private void stop() {
		task.stop();
		animal.occupied = false;
	}

}
