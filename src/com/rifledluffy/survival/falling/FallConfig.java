package com.rifledluffy.survival.falling;

import com.rifledluffy.api.collections.CollectionUtil;
import com.rifledluffy.api.config.usage.ConfigUtil;
import com.rifledluffy.api.util.Util;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class FallConfig {

    private Material material;
    private Material crumbleMat;
    private int crumbleHeight;
    private int breakingHeight;
    private double effectRadius;
    private double shatterFactor;

    FallConfig(ConfigurationSection section) {
        this.material = CollectionUtil.parseEnum(Material.class, section.getName());
        this.crumbleMat = ConfigUtil.getEnum(section, "CrumbleTo", Material.class, Material.AIR);
        this.crumbleHeight = section.getInt("CrumbleHeight", 6);
        this.breakingHeight = section.getInt("BreakingHeight", 12);
        this.effectRadius = section.getDouble("EffectRadius", 2);
        this.shatterFactor = section.getDouble("ShatterFactor", 1);
    }

    Material getMaterial() {
        return material;
    }

    Material getCrumbleMat() {
        return crumbleMat;
    }

    int getCrumbleHeight() {
        return crumbleHeight;
    }

    int getBreakingHeight() {
        return breakingHeight;
    }

    double getEffectRadius() {
        return effectRadius;
    }

    double getShatterFactor() {
        return shatterFactor;
    }
}
