package com.rifledluffy.survival.falling;

import com.rifledluffy.api.collections.CollectionUtil;
import com.rifledluffy.api.config.usage.ConfigUtil;
import com.rifledluffy.api.custom.CustomListener;
import com.rifledluffy.api.location.SoundConfig;
import com.rifledluffy.api.util.Util;
import com.rifledluffy.survival.RFSurvival;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.List;

public class FallingManager implements CustomListener {

    private boolean crippleEntities;
    private List<PotionEffect> crippleEffects;
    private boolean playSound;
    private int minFallHeight;

    private List<FallConfig> fallConfigs;
    private boolean crumblingEnabled;

    public FallingManager() {
        FileConfiguration config = RFSurvival.inst().getConfigManager().getConfig();

        this.crippleEntities = config.getBoolean("Falling.CrippleOnHighFalls.Enabled", true);
        this.minFallHeight = config.getInt("Falling.CrippleOnHighFalls.FallHeight", 10);

        ConfigurationSection effects = config.getConfigurationSection("Falling.CrippleOnHighFalls");
        this.crippleEffects = ConfigUtil.listFromKeys(effects,
                "Effects",
                effect -> {
                    PotionEffectType type;
                    ConfigurationSection inner = effects.getConfigurationSection("Effects");
                    try {
                        type = PotionEffectType.getByName(effect);
                        if (type == null) return null;
                    } catch (Exception e) {
                        return null;
                    }
                    int strength = inner.getInt(effect + ".Strength", 5);
                    int duration = inner.getInt(effect + ".Duration", 100);

                    return new PotionEffect(type, duration, strength);
                });

        this.crumblingEnabled = config.getBoolean("Falling.LandingDamagesBlocks.Enabled", true);
        this.playSound = config.getBoolean("Falling.LandingDamagesBlocks.PlaySound", true);

        ConfigurationSection blocks = config.getConfigurationSection("Falling.LandingDamagesBlocks");
        this.fallConfigs = ConfigUtil.listFromKeys(blocks,
                "Blocks",
                block -> new FallConfig(config.getConfigurationSection("Falling.LandingDamagesBlocks.Blocks." + block)));

        register(RFSurvival.inst());
    }

    private void crumbleBlock(Block block, Entity entity) {
        FallConfig config = fallConfigs.stream()
                .filter(c -> c.getMaterial() == block.getType())
                .findAny().orElse(null);

        if (config == null) return;

        if (config.getCrumbleMat() != null) {
            BlockBreakEvent test = new BlockBreakEvent(block, (entity instanceof Player) ? (Player) entity : null);
            Util.callEvent(test);

            if (test.isCancelled()) return;
            block.setType(config.getCrumbleMat());
        }
    }

    @EventHandler
    public void onFall(EntityDamageEvent event) {
        if (event.getCause() != EntityDamageEvent.DamageCause.FALL) return;
        if (!(event.getEntity() instanceof LivingEntity)) return;

        LivingEntity entity = (LivingEntity) event.getEntity();

        if (crippleEntities) if (entity.getFallDistance() >= minFallHeight) crippleEffects.forEach(entity::addPotionEffect);

        if (!crumblingEnabled) return;

        Block block = entity.getLocation().getBlock().getRelative(BlockFace.DOWN);
        if (block == null) return;

        FallConfig config = fallConfigs.stream()
                .filter(c -> c.getMaterial() == block.getType())
                .findAny().orElse(null);

        if (config == null) return;

        if (entity.getFallDistance() <= config.getBreakingHeight()) {
            if (entity.getFallDistance() >= config.getCrumbleHeight()) {
                int radius = (int) Math.ceil(config.getEffectRadius());
                List<Block> blocks = Util.getBlocksXZLocation(block, radius, radius);
                blocks.forEach(b -> crumbleBlock(b, entity));
            }
        } else {
            int radius = (int) Math.ceil(config.getEffectRadius() * config.getShatterFactor());
            List<Block> blocks = Util.getBlocksXZLocation(block, radius, radius);
            blocks = CollectionUtil.quickFilter(blocks, b -> b.getType() == block.getType());
            blocks.forEach(b -> {
                BlockBreakEvent test = new BlockBreakEvent(block, (entity instanceof Player) ? (Player) entity : null);
                Util.callEvent(test);

                if (test.isCancelled()) return;
                b.breakNaturally(new ItemStack(Material.AIR));
            });

            if (playSound) {
                SoundConfig breakBones = new SoundConfig(Sound.ENTITY_ZOMBIE_BREAK_WOODEN_DOOR);
                breakBones.pitch((float) 0.1);
                breakBones.volume(3);

                breakBones.play(block.getLocation());
            }
        }
    }
}
