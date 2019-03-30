package com.rifledluffy.survival.domestication.food.edibles;

import java.util.HashMap;
import java.util.Map;

import com.rifledluffy.api.collections.CollectionUtil;
import com.rifledluffy.api.config.usage.ConfigUtil;
import com.rifledluffy.survival.RFSurvival;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class EdibleBlock {

	private Map<Material, Integer> foodMap = new HashMap<>();
	
	public void setup() {
		FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();
		ConfigurationSection blocks = config.getConfigurationSection("Animals.Values.Blocks");

		blocks.getKeys(false).forEach(key -> {
			Material mat = CollectionUtil.parseEnum(Material.class, key);
			if (mat == null || !mat.isBlock()) return;

			int value = blocks.getInt(mat.name(), 0);
			if (value == 0) return;

			foodMap.put(mat, value);
		});
	}
	
	public Map<Material, Integer> getFoodMap() {
		return foodMap;
	}
	
	public double getValue(Material material) {
		return this.foodMap.get(material);
	}
}
