package com.rifledluffy.survival.domestication.animal.intelligence.tasks;

import com.rifledluffy.api.collections.CollectionUtil;
import com.rifledluffy.api.custom.tasks.CustomRunnable;
import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.food.FoodSource;
import com.rifledluffy.survival.utility.Util;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.Levelled;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftAnimals;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BreedTask {

    private CustomRunnable task;
    private DomesticAnimal animal;

    public void run() {
        task.run();
    }

    public BreedTask(DomesticAnimal animal) {
        task = new CustomRunnable(RFSurvival.inst(), () -> {
            this.animal = animal;
            DomesticAnimal potentialBreed = animal.getAnimal().getNearbyEntities(5, 5, 5).stream()
                    .filter(mate -> animal.getAnimal().getType() == mate.getType())
                    .map(Animals.class::cast)
                    .filter(mate -> RFSurvival.inst().getAnimalManager().isAnimal(mate))
                    .map(RFSurvival.inst().getAnimalManager()::getAnimal)
                    .filter(mate -> animal.getAnimal().isValid())
                    .min((o1, o2) -> {
                        double dist1 = o1.getAnimal().getLocation().distance(animal.getAnimal().getLocation());
                        double dist2 = o2.getAnimal().getLocation().distance(animal.getAnimal().getLocation());
                        return (int) (dist2 - dist1);
                    }).orElse(null);

            animal.occupied = false;
            if (potentialBreed == null) return;
            animal.breed(potentialBreed.getAnimal());
        }, () -> animal.getAnimal() == null || animal.getAnimal().isDead());
    }
}
