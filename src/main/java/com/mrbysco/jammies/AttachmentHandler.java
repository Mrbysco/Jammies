package com.mrbysco.jammies;

import com.mrbysco.jammies.capability.DancingData;
import com.mrbysco.jammies.network.message.SyncDancingStatePayload;
import com.mrbysco.jammies.util.DanceUtil;
import com.mrbysco.jammies.util.DetectionUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.living.LivingEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

public class AttachmentHandler {
	public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, JammiesMod.MOD_ID);

	public static final Supplier<AttachmentType<DancingData>> DANCING = ATTACHMENT_TYPES.register(
			"jammies_dancing", () -> AttachmentType.serializable(() -> new DancingData(false)).build());

//	public static final EntityCapability<IDancingMob, Void> DANCING_CAPABILITY =
//			EntityCapability.createVoid(
//					new ResourceLocation(JammiesMod.MOD_ID, "jammies_dancing"),
//					IDancingMob.class);
//
//	public static void registerCaps(RegisterCapabilitiesEvent event) {
//		List<EntityType<?>> list = List.of(EntityType.ZOMBIE, EntityType.HUSK, EntityType.DROWNED,
//				EntityType.ZOMBIE_VILLAGER, EntityType.SKELETON, EntityType.STRAY, EntityType.WITHER_SKELETON);
//
//		list.forEach(entityType -> {
//			event.registerEntity(DANCING_CAPABILITY, entityType, (entity, direction) -> new DancingCapability(false));
//		});
//	}

	@SubscribeEvent
	public void onLivingUpdate(LivingEvent.LivingTickEvent event) {
		LivingEntity livingEntity = event.getEntity();
		Level level = livingEntity.level();
		if (!level.isClientSide && livingEntity.tickCount % 20 == 0) {
			DancingData cap = DanceUtil.getDancingAttachment(livingEntity);
			if (cap != null) {
				if (!cap.isDancing() && DetectionUtil.closeToJukebox(livingEntity)) {
					cap.setDancing(true);
					//Sync dancing state to client
					PacketDistributor.ALL.noArg().send(new SyncDancingStatePayload(livingEntity.getId(),
							cap.isDancing(), cap.getAccumulatedTime(), cap.getLastTime()));
				}
			}
		}
	}
}
