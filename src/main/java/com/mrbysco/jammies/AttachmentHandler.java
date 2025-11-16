package com.mrbysco.jammies;

import com.mrbysco.jammies.capability.DancingData;
import com.mrbysco.jammies.network.message.SyncDancingStatePayload;
import com.mrbysco.jammies.util.DanceUtil;
import com.mrbysco.jammies.util.DetectionUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentHandler {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, JammiesMod.MOD_ID);

	public static final Supplier<AttachmentType<DancingData>> DANCING = ATTACHMENT_TYPES.register(
			"jammies_dancing", () -> AttachmentType.builder(() -> new DancingData(false)).serialize(DancingData.CODEC).build());

	@SubscribeEvent
	public void onLivingUpdate(EntityTickEvent.Pre event) {
		Entity entity = event.getEntity();
		Level level = entity.level();
		if (!level.isClientSide() && entity.tickCount % 20 == 0 && entity instanceof LivingEntity livingEntity) {
			DancingData cap = DanceUtil.getDancingAttachment(livingEntity);
			if (cap != null) {
				if (!cap.isDancing() && DetectionUtil.closeToJukebox(livingEntity)) {
					cap.setDancing(true);
					//Sync dancing state to client
					PacketDistributor.sendToAllPlayers(new SyncDancingStatePayload(livingEntity.getId(),
							cap.isDancing(), cap.getAccumulatedTime(), cap.getLastTime()));
				}
			}
		}
	}
}
