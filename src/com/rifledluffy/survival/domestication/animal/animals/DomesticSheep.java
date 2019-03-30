package com.rifledluffy.survival.domestication.animal.animals;

import com.rifledluffy.survival.domestication.animal.types.DomesticMedium;
import org.bukkit.entity.Sheep;

public class DomesticSheep extends DomesticMedium {

    private Sheep sheep;

    public DomesticSheep(Sheep sheep, int thirstThreshold, int hungerThreshold) {
        super(sheep, thirstThreshold, hungerThreshold);
        this.sheep = sheep;
    }

    public DomesticSheep(Sheep sheep, int hydration, int hunger, int thirstThreshold, int hungerThreshold) {
        super(sheep, hydration, hunger, thirstThreshold, hungerThreshold);
        this.sheep = sheep;
    }
}
