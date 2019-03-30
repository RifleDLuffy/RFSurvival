package com.rifledluffy.survival.utility;

import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import com.rifledluffy.survival.domestication.food.FoodSource;
import com.rifledluffy.survival.domestication.food.edibles.EdibleBlock;
import com.rifledluffy.survival.domestication.food.edibles.EdibleItem;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Map;

public class Util {

    public static Map<Material, Integer> getFoodMap() {
        EdibleBlock edibles = new EdibleBlock();
        edibles.setup();
        return edibles.getFoodMap();
    }

    public static Map<Material, Integer> getItemMap() {
        EdibleItem edibles = new EdibleItem();
        edibles.setup();
        return edibles.getFoodMap();
    }

    public static double getFoodValue(Material material) {
        EdibleBlock edibles = new EdibleBlock();
        edibles.setup();
        return edibles.getValue(material);
    }

    public static double getItemValue(Material material, DomesticAnimal animal) {
        EdibleItem edibles = new EdibleItem();
        edibles.setup();
        return edibles.getValue(material, animal.getSize());
    }

    private static boolean foodExists(Location location) {
        List<FoodSource> foodSources = RFSurvival.inst().getAnimalManager().getFoodSources();
        return foodSources.stream()
                .filter(source -> source.getLocation() != null)
                .anyMatch(source -> source.getLocation().equals(location));
    }

    public static boolean foodExists(Block block) {
        return foodExists(block.getLocation());
    }

    public static FoodSource getSource(Block block) {
        return getSource(block.getLocation());
    }

    public static FoodSource getSource(Location location) {
        List<FoodSource> foodSources = RFSurvival.inst().getAnimalManager().getFoodSources();
        return foodSources.stream()
                .filter(source -> source.getLocation().equals(location))
                .findFirst().orElse(null);
    }
}
