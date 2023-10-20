package com.mrbysco.jammies;

import com.mrbysco.jammies.capability.DancingCapability;
import com.mrbysco.jammies.capability.IDancingMob;
import com.mrbysco.jammies.network.JammiesNetworking;
import com.mrbysco.jammies.network.message.SyncDancingStateMessage;
import com.mrbysco.jammies.util.DetectionUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.AbstractSkeleton;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;

public class CapabilityHandler {
	public static final Capability<IDancingMob> DANCING_CAPABILITY = CapabilityManager.get(new CapabilityToken<>() {
	});

	@SubscribeEvent
	public void onEntityConstructing(AttachCapabilitiesEvent<Entity> event) {
		if (event.getObject() instanceof Zombie || event.getObject() instanceof AbstractSkeleton) {
			event.addCapability(JammiesMod.DANCING_CAP, new DancingCapability(false));
		}
	}

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity livingEntity = event.getEntity();
		Level level = livingEntity.level();
		if (!level.isClientSide && level.getGameTime() % 20 == 0) {
			IDancingMob cap = livingEntity.getCapability(CapabilityHandler.DANCING_CAPABILITY).orElse(null);
			if (cap != null) {
				if (DetectionUtil.closeToJukebox(livingEntity) && !cap.isDancing()) {
					cap.setDancing(true);
					//Sync dancing state to client
					JammiesNetworking.CHANNEL.send(PacketDistributor.ALL.noArg(), new SyncDancingStateMessage(livingEntity.getId(), DancingCapability.writeNBT(cap)));
				}
			}
		}
	}
}
