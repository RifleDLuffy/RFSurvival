package com.rifledluffy.survival.domestication.animal.intelligence;

import com.rifledluffy.api.custom.tasks.CustomRunnable;
import com.rifledluffy.api.util.Util;
import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.animal.base.DomesticSize;

public class InternalTimer implements Runnable {
	private int decayInterval = 400;
	
	public boolean lookingForFood = false;
	public boolean lookingForWater = false;
	
	private DomesticAnimal animal;
	
	private CustomRunnable decay;
	private CustomRunnable warn;
	
	public InternalTimer(DomesticAnimal animal) {
		this.animal = animal;
	}

	@Override
	public void run() {
		decay = new CustomRunnable(RFSurvival.inst(), () -> {
			if (animal.hydration > 0) animal.hydration--;
			if (animal.hunger > 0) animal.hunger--;
		}, () -> !animal.getAnimal().isValid());
		decay.runTimer(Util.getRandom().nextInt(200),  decayInterval);

		warn = new CustomRunnable(RFSurvival.inst(), () -> {
			lookingForWater = animal.isThirsty();
			lookingForFood = animal.isHungry();
		}, () -> !animal.getAnimal().isValid());
		warn.runTimer(1, 20);
	}
	
	public void setup(DomesticSize size) {
		double decayScale = 1;
		switch (size) {
			case SMALL:
				decayScale = 0.75;
				break;
			case MEDIUM:
				decayScale = 1;
				break;
			case LARGE:
				decayScale = 1.25;
				break;
		}
		decayInterval *= decayScale;
		this.run();
	}
	
	public void stop() {
		decay.stop();
		warn.stop();
	}
	
}
