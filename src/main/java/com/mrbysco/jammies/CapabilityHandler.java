package com.mrbysco.jammies;

import com.mrbysco.jammies.capability.DancingCapability;
import com.mrbysco.jammies.capability.IDancingMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class CapabilityHandler {
	public static final Capability<IDancingMob> DANCING_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	@SubscribeEvent
	public void onEntityConstructing(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Zombie || event.getObject() instanceof AbstractSkeleton) {
			event.addCapability(JammiesMod.DANCING_CAP, new DancingCapability(false));
		}
	}
}
