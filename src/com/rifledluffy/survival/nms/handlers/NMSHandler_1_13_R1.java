package com.rifledluffy.survival.nms.handlers;

import net.minecraft.server.v1_13_R1.EntityCreature;
import net.minecraft.server.v1_13_R1.PathEntity;
import net.minecraft.server.v1_13_R1.EntityAnimal;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftCow;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftCreature;
import org.bukkit.craftbukkit.v1_13_R1.entity.CraftAnimals;
import org.bukkit.entity.Animals;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Entity;

public class NMSHandler_1_13_R1 implements NMSHandler {

	@Override
	public void pathToLoc(Creature creature, Location location, float speed) {
		EntityCreature entity = ((CraftCreature)creature).getHandle();
		PathEntity pathEntity = entity.getNavigation().a(location.getX(), location.getY(), location.getZ());
		entity.getNavigation().a(pathEntity, speed);
	}

	@Override
	public void pathToEntity(Creature creature, Entity target, float speed) {
		EntityCreature entity = ((CraftCreature)creature).getHandle();
		PathEntity pathEntity = entity.getNavigation().a((net.minecraft.server.v1_13_R1.Entity)target);
		entity.getNavigation().a(pathEntity, speed);
	}

	@Override
	public void breed(Animals breeder, Animals toBreed) {
		if (breeder.getType() != toBreed.getType()) return;
		if (!breeder.canBreed()) return;
		if (!toBreed.canBreed()) return;

		EntityAnimal nmsBreeder = ((CraftAnimals)breeder).getHandle();
		EntityAnimal nmsToBreed = ((CraftAnimals)toBreed).getHandle();
		if (nmsBreeder.isInLove() && nmsToBreed.isInLove()) return;

		nmsBreeder.e(400);
		nmsToBreed.e(400);

		breeder.setTarget(toBreed);

		breeder.getWorld().spawnParticle(Particle.HEART, breeder.getLocation(), 10, 0.5, 1, 0.5);
		toBreed.getWorld().spawnParticle(Particle.HEART, toBreed.getLocation(), 10, 0.5, 1, 0.5);
	}
}
