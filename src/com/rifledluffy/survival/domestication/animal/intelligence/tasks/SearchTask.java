package com.rifledluffy.survival.domestication.animal.intelligence.tasks;

import com.rifledluffy.api.collections.CollectionUtil;
import com.rifledluffy.api.custom.tasks.CustomRunnable;
import com.rifledluffy.api.util.Util;
import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.domestication.AnimalManager;
import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.food.FoodSource;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SearchTask {

	private DomesticAnimal animal;
	private SearchType type;
	private CustomRunnable task;

	private void stop() {
		task.stop();
		animal.occupied = false;
	}

	public void run() {
		task.run();
	}

	public SearchTask(DomesticAnimal animal, SearchType type) {
		this.animal = animal;
		this.type = type;
		task = new CustomRunnable(RFSurvival.inst(), () -> {
			if (type == SearchType.WATER) {
				List<Block> water = new ArrayList<>();
				animal.getWaterSources().stream()
						.filter(block -> block.getBlockData() instanceof Levelled)
						.filter(block -> block.getType() != Material.LAVA)
						.filter(block -> (!(((Levelled) block.getBlockData()).getLevel() == 0 && block.getType() == Material.CAULDRON)))
						.forEach(water::add);

				water.sort((o1, o2) -> {
					double dist1 = o1.getLocation().distance(animal.getAnimal().getLocation());
					double dist2 = o2.getLocation().distance(animal.getAnimal().getLocation());
					return Double.compare(dist1, dist2);
				});

				if (water.size() != 0) {
					new ConsumeTask(animal, water.get(0)).run();
					stop();
					return;
				}

				List<Block> possible = Util.getBlocksNearEntity(animal.getAnimal(), 10, 2, 10).stream()
						.filter(block -> block.getBlockData() instanceof Levelled)
						.filter(block -> block.getType() != Material.LAVA)
						.filter(block -> (!(((Levelled) block.getBlockData()).getLevel() == 0 && block.getType() == Material.CAULDRON)))
						.sorted((o1, o2) -> {
							double dist1 = o1.getLocation().distance(animal.getAnimal().getLocation());
							double dist2 = o2.getLocation().distance(animal.getAnimal().getLocation());
							return Double.compare(dist1, dist2);
						}).collect(Collectors.toList());

				List<Location> waterLocs = new ArrayList<>();
				possible.forEach(s -> waterLocs.add(s.getLocation()));

				animal.getWaterSources().addAll(CollectionUtil.quickMap(waterLocs, Location::getBlock));

				if (possible.size() > 0) {
					new ConsumeTask(animal, possible.get(0)).run();
				}
				stop();
			} else if (type == SearchType.FOOD) {
				List<FoodSource> foodSources = new ArrayList<>();
				animal.getFoodSources().stream()
						.filter(food -> !food.isEmpty())
						.forEach(foodSources::add);

				foodSources.sort((o1,o2) -> {
					double dist1 = o1.getLocation().distance(animal.getAnimal().getLocation());
					double dist2 = o2.getLocation().distance(animal.getAnimal().getLocation());
					return Double.compare(dist1, dist2);
				});
;
				if (foodSources.size() != 0) {
					new ConsumeTask(animal, foodSources.get(0)).run();
					return;
				}

				List<Item> edibleItems = new ArrayList<>();

				animal.getAnimal().getNearbyEntities(10, 10, 10).stream()
						.filter(e -> e instanceof Item)
						.filter(e -> !e.isDead())
						.filter(e -> com.rifledluffy.survival.utility.Util.getItemValue(((Item) e).getItemStack().getType(), animal) > 0)
						.forEach(e -> edibleItems.add((Item) e));

				edibleItems.sort((o1, o2) -> {
					double dist1 = o1.getLocation().distance(animal.getAnimal().getLocation());
					double dist2 = o2.getLocation().distance(animal.getAnimal().getLocation());
					return Double.compare(dist1, dist2);
				});

				if (edibleItems.size() != 0) {
					new ConsumeTask(animal, edibleItems.get(0)).run();
					return;
				}

				List<FoodSource> sources = new ArrayList<>();

				Map<Material, Integer> edibleBlocks = RFSurvival.inst().getAnimalManager().getEdibleBlocks().getFoodMap();

				List<Block> edibles = Util.getBlocksNearEntity(animal.getAnimal(), 10,10,10).stream()
						.filter(b -> edibleBlocks.containsKey(b.getType()))
						.collect(Collectors.toList());

				edibles.forEach(edible -> {
					AnimalManager manager = RFSurvival.inst().getAnimalManager();
					if (manager.isFoodSource(edible)) {
						sources.add(manager.getFoodSource(edible));
					} else sources.add(new FoodSource(edible));
				});

				animal.getFoodSources().addAll(sources);

				sources.sort((o1, o2) -> {
					double dist1 = o1.getLocation().distance(animal.getAnimal().getLocation());
					double dist2 = o2.getLocation().distance(animal.getAnimal().getLocation());
					return Double.compare(dist1, dist2);
				});

				if (sources.size() != 0) {
					new ConsumeTask(animal,sources.get(0)).run();
				} else stop();
			}
		}, () -> animal.getAnimal() == null || animal.getAnimal().isDead() || (!animal.getTimer().lookingForWater && !animal.getTimer().lookingForFood));
	}
}

