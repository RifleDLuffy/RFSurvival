package com.rifledluffy.survival.utility;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.rifledluffy.api.custom.CustomListener;
import com.rifledluffy.api.custom.tasks.CustomRunnable;
import com.rifledluffy.survival.RFSurvival;
import com.rifledluffy.survival.domestication.animal.animals.DomesticAnimal;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import java.util.HashMap;
import java.util.Map;

public class HologramManager implements CustomListener {

    private RFSurvival plugin = RFSurvival.inst();
    private boolean useHolographicDisplays;
    private Map<DomesticAnimal, Hologram> hologramMap = new HashMap<>();
    private CustomRunnable refreshAnimals;

    public HologramManager() {
        useHolographicDisplays = Bukkit.getPluginManager().isPluginEnabled("HolographicDisplays") && Bukkit.getPluginManager().isPluginEnabled("ProtocolLib");
    }

    public boolean useHolograms() {
        return useHolographicDisplays;
    }

    public void setup() {
        register(RFSurvival.inst());
        if (plugin.getAnimalManager().isEnabled()) {
            plugin.getAnimalManager().animals.forEach(this::add);
            refreshAnimals = new CustomRunnable(RFSurvival.inst(), () -> {
                hologramMap.forEach((animal, hologram) -> {
                    if (hologram.isDeleted() || animal == null || animal.getAnimal() == null || animal.getAnimal().isDead())
                        return;
                    hologram.clearLines();
                    Location newLoc = animal.getAnimal().getLocation().add(0, 2, 0);
                    hologram.teleport(newLoc);
                    hologram.appendTextLine("§7Hunger: §a" + animal.getHunger() + " §6/ 100");
                    hologram.appendTextLine("§7Thirst: §a" + animal.getHydration() + " §6/ 100");
                });
            });
            refreshAnimals.runTimer(1, 10);
        }
}

    @EventHandler
    public void onClickAnimal(PlayerInteractAtEntityEvent event) {
        if (!plugin.getAnimalManager().isEnabled()) return;
        Player player = event.getPlayer();
        if (player == null) return;
        if (!(event.getRightClicked() instanceof Animals)) return;
        if (!plugin.getAnimalManager().isAnimal((Animals) event.getRightClicked())) return;
        if (player.getInventory().getItemInMainHand().getType() != Material.AIR) return;

        DomesticAnimal animal = plugin.getAnimalManager().getAnimal((Animals) event.getRightClicked());
        show(animal,player);
        if (animal.mother != null && !animal.getAnimal().canBreed()) show(animal.mother, player);
    }

    private void show(DomesticAnimal animal, Player player) {
        Hologram hologram = hologramMap.get(animal);
        if (hologram == null) return;
        hologram.getVisibilityManager().showTo(player);

        CustomRunnable hide = new CustomRunnable(RFSurvival.inst(),
                () -> hologram.getVisibilityManager().hideTo(player),
                () -> hologram.isDeleted() || animal == null || animal.getAnimal() == null || animal.getAnimal().isDead());

        hide.runLater(100);
    }

    public void add(DomesticAnimal animal) {
        if (!useHolograms()) return;
        Location spawn = animal.getAnimal().getLocation().add(0, 2,0);
        Hologram hologram = HologramsAPI.createHologram(plugin,spawn);
        hologram.getVisibilityManager().setVisibleByDefault(false);
        hologram.appendTextLine("§7Hunger: §a" + animal.getHunger() + " §6/ 100");
        hologram.appendTextLine("§7Thirst: §a" + animal.getHydration() + " §6/ 100");
        hologramMap.put(animal,hologram);
    }

    public void remove(DomesticAnimal animal) {
        if (!useHolograms()) return;
        if (!hologramMap.containsKey(animal)) return;
        hologramMap.get(animal).delete();
    }

    public void reload() {
        if (plugin.getAnimalManager().isEnabled()) {
            refreshAnimals.stop();
            refreshAnimals.runTimer(1,10);
        }
    }

}
