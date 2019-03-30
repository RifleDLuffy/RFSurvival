package com.rifledluffy.survival.domestication;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.rifledluffy.api.custom.CustomListener;
import com.rifledluffy.api.util.Util;
import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.config.ConfigManager;
import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.animal.animals.DomesticType;
import com.rifledluffy.survival.domestication.events.AnimalConsumeEvent;
import com.rifledluffy.survival.domestication.events.ConsumeType;
import com.rifledluffy.survival.domestication.food.FoodSource;
import com.rifledluffy.survival.domestication.food.edibles.EdibleBlock;
import com.rifledluffy.survival.domestication.food.edibles.EdibleItem;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Animals;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityBreedEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntitySpawnEvent;

public class AnimalManager implements CustomListener {

	private boolean enabled;
	
	private RFSurvival plugin = RFSurvival.inst();
	private ConfigManager configManager = plugin.getConfigManager();
	private FileConfiguration config = configManager.getConfig();
	
	private EdibleItem edibleItems = new EdibleItem();
	private EdibleBlock edibleBlocks = new EdibleBlock();
	
	private List<FoodSource> foodSources = new ArrayList<>();
	
	public List<DomesticAnimal> animals = new ArrayList<>();

	public AnimalManager() {
		enabled = config.getBoolean("Animals.Enabled", true);
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void reload() {
		edibleItems.setup();
		edibleBlocks.setup();
	}
	
	public EdibleItem getEdibleItems() {
		return edibleItems;
	}
	public EdibleBlock getEdibleBlocks() {
		return edibleBlocks;
	}

	public List<FoodSource> getFoodSources() {
		return foodSources;
	}

	@EventHandler
	public void onBreed(EntityBreedEvent event) {
		if (event.isCancelled()) return;
		if (!(event.getMother() instanceof Animals)) return;
		if (!(event.getEntity() instanceof Animals)) return;
		if (isAnimal((Animals) event.getMother())) {
			DomesticAnimal newAnimal = DomesticType.getRespectiveAnimal((Animals) event.getEntity(), event.getEntity().getType());
			if (newAnimal.getAnimal() == null) return;
			animals.add(newAnimal);
			newAnimal.mother = getAnimal((Animals) event.getMother());
			plugin.getHologramManager().add(newAnimal);
		}
	}

	@EventHandler
	public void onSpawn(EntitySpawnEvent event) {
		if (event.isCancelled()) return;
		if (!(event.getEntity() instanceof Animals)) return;
		if (isAnimal((Animals) event.getEntity())) return;

		Animals animal = ((Animals) event.getEntity());

		DomesticAnimal newAnimal = DomesticType.getRespectiveAnimal(animal, animal.getType());
		if (newAnimal.getAnimal() == null) return;
		animals.add(newAnimal);
		plugin.getHologramManager().add(newAnimal);
	}
	
	@EventHandler
	public void onDeath(EntityDeathEvent event) {
		LivingEntity entity = event.getEntity();
		if (!(entity instanceof Animals)) return;
		Animals animal = (Animals) entity;
		if (!isAnimal(animal)) return;

		DomesticAnimal dom;
		List<DomesticAnimal> list = animals.stream().filter(e -> e.getAnimal() == animal).collect(Collectors.toList());
		if (list.size() == 0) return;
		dom = list.get(0);
		this.animals.remove(dom);
		dom.getTimer().stop();
		dom.getMind().stop();
		dom.setAnimal(null);
		plugin.getHologramManager().remove(dom);

		double goodDropMultiplier = config.getDouble("Animals.Thresholds.GoodDropMultiplier", 2);
		double badDropMultiplier = config.getDouble("Animals.Thresholds.BadDropMultiplier", 0.5);

		if (dom.getHunger() >= dom.getHungerThreshold()) {
			if (dom.getHydration() >= dom.getThirstThreshold()) {
				event.getDrops().forEach(item -> {
					int newAmount = (int) (item.getAmount() * goodDropMultiplier);
					if (newAmount > item.getType().getMaxStackSize()) newAmount = item.getType().getMaxStackSize();
					item.setAmount(newAmount);
				});
				event.setDroppedExp((int) (event.getDroppedExp() * goodDropMultiplier));
			}
		}

		if (dom.getHunger() < dom.getHungerThreshold()) {
			if (dom.getHydration() < dom.getThirstThreshold()) {
				event.getDrops().forEach(item -> {
					int newAmount = (int) (item.getAmount() * badDropMultiplier);
					if (newAmount < 0) newAmount = 0;
					item.setAmount(newAmount);
				});
				event.setDroppedExp((int) (event.getDroppedExp() * badDropMultiplier));
			}
		}
	}
	
	public void generateAnimals() {
		List<Animals> animals = new ArrayList<>();
		
		List<String> blacklistedWorlds = config.getStringList("blacklisted-worlds");
		List<World> blacklist = new ArrayList<>();
		
		blacklistedWorlds.stream()
			.filter(w -> Bukkit.getWorld(w) != null)
			.forEach(w -> blacklist.add(Bukkit.getWorld(w)));
		
		Bukkit.getWorlds().stream()
			.filter(world -> !blacklist.contains(world))
			.forEach(world -> world.getEntities().stream()
				.filter(e -> e instanceof Animals)
				.forEach(e -> animals.add((Animals) e)));
		
		List<DomesticAnimal> newAnimals = new ArrayList<>();
		
		animals.stream()
			.filter(animal -> animal.getCustomName() == null || animal.getCustomName().isEmpty())
			.filter(animal -> !animal.isInvulnerable())
			.filter(animal -> !animal.isSilent())
			.filter(LivingEntity::isCollidable)
			.filter(LivingEntity::hasAI)
			.forEach(animal -> {
				DomesticAnimal newAnimal = DomesticType.getRespectiveAnimal(animal, animal.getType());
				if (newAnimal.getAnimal() == null) return;
				newAnimals.add(newAnimal);
			});
	}

	public boolean isAnimal(Animals target) {
		for (DomesticAnimal animal : animals) {
			if (animal.getAnimal() == null) continue;
			if (animal.getAnimal().getUniqueId().equals(target.getUniqueId())) return true;
		}
		return false;
	}

	public DomesticAnimal getAnimal(Animals target) {
		return animals.stream()
				.filter(dom -> dom.getAnimal() != null)
				.filter(dom -> dom.getAnimal().equals(target))
				.findAny().orElse(null);
	}

	public boolean isFoodSource(Block block) {
		return foodSources.stream()
				.filter(food -> food.getLocation() != null)
				.anyMatch(food -> food.getLocation().equals(block.getLocation()));
	}

	public FoodSource getFoodSource(Block block) {
		return foodSources.stream()
				.filter(food -> food.getLocation() != null)
				.filter(food -> food.getLocation().equals(block.getLocation()))
				.findAny().orElse(null);
	}



}
