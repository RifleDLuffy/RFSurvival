package com.rifledluffy.survival.domestication.animal.animals;

import com.rifledluffy.survival.domestication.animal.types.DomesticLarge;
import org.bukkit.entity.Horse;

public class DomesticHorse extends DomesticLarge {

    private Horse horse;

    public DomesticHorse(Horse horse, int thirstThreshold, int hungerThreshold) {
        super(horse, thirstThreshold, hungerThreshold);
        this.horse = horse;
    }

    public DomesticHorse(Horse horse, int hydration, int hunger, int thirstThreshold, int hungerThreshold) {
        super(horse, hydration, hunger, thirstThreshold, hungerThreshold);
        this.horse = horse;
    }
}
