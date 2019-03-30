package com.rifledluffy.survival.domestication.animal.intelligence;

import com.rifledluffy.api.custom.tasks.CustomRunnable;
import com.rifledluffy.api.util.Util;
import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.animal.intelligence.tasks.BreedTask;
import com.rifledluffy.survival.domestication.animal.intelligence.tasks.SearchTask;
import com.rifledluffy.survival.domestication.animal.intelligence.tasks.SearchType;

public class Sentience implements Runnable {

	private DomesticAnimal animal;
	private int randomDelay = Util.getRandom().nextInt(100) + 5;
	private CustomRunnable sentience;
	
	public Sentience(DomesticAnimal animal) {
		this.animal = animal;
	}
	
	@Override
	public void run() {
		sentience = new CustomRunnable(RFSurvival.inst(), () -> {
			if (animal.getTimer().lookingForFood) {
				if (animal.occupied) return;
				animal.occupied = true;
				new SearchTask(animal, SearchType.FOOD).run();
				return;
			}

			if (animal.getTimer().lookingForWater) {
				if (animal.occupied) return;
				animal.occupied = true;
				new SearchTask(animal, SearchType.WATER).run();
				return;
			}

			if (animal.getHydration() >= animal.getThirstThreshold() && animal.getHunger() >= animal.getHungerThreshold()) {
				if (animal.occupied) return;
				animal.occupied = true;
				new BreedTask(animal).run();
			}

		}, () -> animal.getAnimal() == null || animal.getAnimal().isDead());
		sentience.runTimer(1, randomDelay);
	}
	
	public void stop() {
		sentience.stop();
	}



}