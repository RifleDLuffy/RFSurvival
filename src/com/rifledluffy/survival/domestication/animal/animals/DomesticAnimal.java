package com.rifledluffy.survival.domestication.animal.animals;

import com.rifledluffy.api.util.Util;
import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.domestication.animal.base.DomesticEntity;
import com.rifledluffy.survival.domestication.animal.base.DomesticSize;
import com.rifledluffy.survival.domestication.animal.intelligence.InternalTimer;
import com.rifledluffy.survival.domestication.animal.intelligence.Sentience;
import com.rifledluffy.survival.domestication.events.AnimalConsumeEvent;
import com.rifledluffy.survival.domestication.events.ConsumeType;
import com.rifledluffy.survival.domestication.food.FoodSource;
import com.rifledluffy.survival.domestication.food.edibles.EdibleBlock;
import com.rifledluffy.survival.domestication.food.edibles.EdibleItem;
import com.rifledluffy.survival.nms.handlers.NMSHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class DomesticAnimal implements DomesticEntity {
	
	private RFSurvival plugin = RFSurvival.inst();
	private NMSHandler navigation = plugin.getNavigation();
	private EdibleItem edibleItems = plugin.getAnimalManager().getEdibleItems();
	private EdibleBlock edibleBlocks = plugin.getAnimalManager().getEdibleBlocks();
	
	/*
	 * The living entity reference
	 */

	private List<Block> waterSources = new ArrayList<>();
	private List<FoodSource> foodSources = new ArrayList<>();

	public DomesticAnimal mother;
	
	public Animals animal;
	private DomesticSize size;
	
	private InternalTimer timer;
	private Sentience mind;
	
	public int hydration;
	public int hunger;
	
	private int thirstThreshold;
	private int hungerThreshold;

	public boolean occupied = false;
	
	public DomesticAnimal(Animals animal, int hydration, int hunger, int thirstThreshold, int hungerThreshold, DomesticSize size) {
		if (animal == null) return;
		this.animal = animal;
		this.hydration = 0;
		this.hunger = 0;
		this.thirstThreshold = thirstThreshold;
		this.hungerThreshold = hungerThreshold;
		this.size = size;
		plugin.getAnimalManager().animals.add(this);

		this.timer = new InternalTimer(this);
		this.timer.setup(size);
		this.mind = new Sentience(this);
		this.mind.run();
	}
	
	public DomesticAnimal(Animals animal, int thirstThreshold, int hungerThreshold, DomesticSize size) {
		this(animal, 100, 100, thirstThreshold, hungerThreshold, size);
	}
	
	public boolean isHungry() {
		return hunger <= hungerThreshold;
	}
	
	public boolean isThirsty() {
		return hydration <= thirstThreshold;
	}

	public EdibleItem getFoodManager() {
		return edibleItems;
	}

	public Animals getAnimal() {
		return animal;
	}

	public DomesticSize getSize() {
		return size;
	}

	public int getHydration() {
		return hydration;
	}

	public int getHunger() {
		return hunger;
	}

	public int getThirstThreshold() {
		return thirstThreshold;
	}

	public int getHungerThreshold() {
		return hungerThreshold;
	}

	public List<Block> getWaterSources() {
		return waterSources;
	}

	public List<FoodSource> getFoodSources() {
		return foodSources;
	}

	public InternalTimer getTimer() {
		return timer;
	}

	public Sentience getMind() {
		return mind;
	}

	public void setAnimal(Animals animal) {
		this.animal = animal;
	}

	@Override
	public void move(Location location) {
		if (animal == null || animal.isDead()) return;
		if (animal.isAdult()) navigation.pathToLoc(animal, location, 1);
		else navigation.pathToLoc(animal, location, 1.5F);
	}

	@Override
	public void move(Entity entity) {
		if (animal == null || animal.isDead()) return;
		if (animal.isAdult()) navigation.pathToEntity(animal, entity, 1);
		else navigation.pathToEntity(animal, entity, 1.5F);
	}
	
	@Override
	public void look(Location location) {
		animal.teleport(animal.getLocation().setDirection(Util.getVectorDir(animal.getLocation(), location)));
	}

	@Override
	public void look(Entity entity) {
		animal.teleport(animal.getLocation().setDirection(Util.getVectorDir(animal.getLocation(), entity.getLocation())));
	}
	
	@Override
	public void look(LivingEntity entity) {
		animal.teleport(animal.getLocation().setDirection(Util.getVectorDir(animal.getLocation(), entity.getEyeLocation())));
	}

	@Override
	public void breed(Animals target) {
		navigation.breed(getAnimal(), target);
	}

	@Override
	public boolean consume(AnimalConsumeEvent event) {
		if (event.isCancelled()) return false;
		if (event.getType() == ConsumeType.WATER) animal.getWorld().playSound(animal.getLocation(), Sound.ENTITY_GENERIC_DRINK, 1, (float) size.getScale());
		if (event.getType() == ConsumeType.ITEM) animal.getWorld().playSound(animal.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, (float) size.getScale());
		if (event.getType() == ConsumeType.FOODSOURCE) animal.getWorld().playSound(animal.getLocation(), Sound.ENTITY_GENERIC_EAT, 1, 0.1F);
		switch (event.getType()) {
			default:
				return false;
			case FOODSOURCE:
				return eat(event.getBlock());
			case WATER:
				return drink(event.getWater());
			case ITEM:
				return eat(event.getItem());
			case INVENTORY:
				return eat(event.getInventory(), event.getItemStack());
		}
	}

	@Override
	public boolean drink(Block block) {
		if (!(block.getBlockData() instanceof Levelled)) return false;
		Levelled water = (Levelled) block.getBlockData();
		if (water.getLevel() <= 0 && block.getType() == Material.CAULDRON) return false;
		if (water.getLevel() >= 8) return false;
		
		if (hydration > thirstThreshold) return false;
		if (water.getLevel() == 0) water.setLevel(7);
		water.setLevel(water.getLevel() - 1);
		hydration += 40 * size.getScale();
		if (hydration > 100) hydration = 100;
		block.getLocation().getBlock().setBlockData(water);
		if (water.getLevel() <= 0 && block.getType() != Material.CAULDRON) block.setType(Material.AIR);
		return true;
	}

	@Override
	public boolean eat(Item item) {
		double value = edibleItems.getValue(item.getItemStack().getType(), size);
		if (value <= 0) return false;
		if (hunger > hungerThreshold) return false;
		
		item.getItemStack().setAmount(item.getItemStack().getAmount() - 1);
		hunger += value;
		if (hunger > 100) hunger = 100;
		return true;
	}
	
	public boolean eat(FoodSource source) {
		int uses = source.getUses();
		if (uses <= 0) return false;

		double value = edibleBlocks.getValue(source.getMaterial());
		if (value <= 0) return false;

		if (hunger > hungerThreshold) return false;
		
		if (source.eat()) {
			hunger += value;
			animal.getWorld().spawnParticle(Particle.BLOCK_CRACK,	Util.centerAll(source.getLocation().getBlock()), 20, 0.2, 0.2, 0.2, source.getLocation().getBlock().getBlockData());
			if (hunger > 100) hunger = 100;
			if (source.getUses() == 0) source.destroy();
			return true;
		} else return false;
	}

	@Override
	public boolean eat(Inventory inventory, ItemStack item) {
		double value = edibleItems.getValue(item.getType(), size);
		
		if (value <= 0) return false;
		if (hunger > hungerThreshold) return false;

		item.setAmount(item.getAmount() - 1);
		hunger += value;
		if (hunger > 100) hunger = 100;
		return true;
	}

	@Override
	public void shelter(Location location) {
		move(location);
	}

	@Override
	public void steal(ItemStack item) {}
	
}
