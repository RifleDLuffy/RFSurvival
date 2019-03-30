package com.rifledluffy.survival.domestication.food.edibles;

import java.util.HashMap;
import java.util.Map;

import com.rifledluffy.api.collections.CollectionUtil;
import com.rifledluffy.api.util.Util;
import com.rifledluffy.survival.RFSurvival;
import org.bukkit.Material;

import com.rifledluffy.survival.domestication.animal.base.DomesticSize;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class EdibleItem {

	private Map<Material, Integer> foodMap = new HashMap<>();
	
	public void setup() {
		FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();
		ConfigurationSection blocks = config.getConfigurationSection("Animals.Values.Food");

		blocks.getKeys(false).forEach(key -> {
			Material mat = CollectionUtil.parseEnum(Material.class, key);
			if (mat == null) return;

			int value = blocks.getInt(mat.name(), 0);
			if (value == 0) return;

			foodMap.put(mat, value);
		});
	}

	public Map<Material, Integer> getFoodMap() {
		return foodMap;
	}

	public double getValue(Material material, DomesticSize size) {
		double scaling = 1;

		switch (size) {
			case SMALL:
				scaling = 1.5;
				break;
			default:
			case MEDIUM:
				break;
			case LARGE:
				scaling = 0.75;
				break;
		}

		try {
			return foodMap.get(material) * scaling;
		} catch (Exception e) {
			return -1;
		}
	}
}
