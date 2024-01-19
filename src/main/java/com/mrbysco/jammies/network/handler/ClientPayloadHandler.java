package com.mrbysco.jammies.network.handler;

import com.mrbysco.jammies.JammiesMod;
import com.mrbysco.jammies.capability.DancingData;
import com.mrbysco.jammies.client.DanceHandler;
import com.mrbysco.jammies.network.message.SyncDancingStatePayload;
import com.mrbysco.jammies.util.DanceUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;

public class ClientPayloadHandler {
	private static final ClientPayloadHandler INSTANCE = new ClientPayloadHandler();

	public static ClientPayloadHandler getInstance() {
		return INSTANCE;
	}

	public void handleData(final SyncDancingStatePayload data, final PlayPayloadContext context) {
		context.workHandler().submitAsync(() -> {
					Minecraft mc = Minecraft.getInstance();
					if (mc.level == null)
						return;
					Entity entity = mc.level.getEntity(data.entityID());
					if (entity != null) {
						DancingData cap = DanceUtil.getDancingAttachment(entity);
						if (cap != null) {
							cap.setDancing(data.dancing());
							cap.setAccumulatedTime(data.accumulatedTime());
							cap.setLastTime(data.lastTime());

							if (cap.isDancing())
								cap.start(entity.tickCount);
							else {
								cap.stop();

								//noinspection rawtypes
								if (mc.getEntityRenderDispatcher().getRenderer(entity) instanceof HumanoidMobRenderer renderer &&
										renderer.getModel() instanceof HumanoidModel<?> model) {
									DanceHandler.resetModel(model);
								}
							}
							DanceUtil.saveDancing(entity, cap);
						}
					}
				})
				.exceptionally(e -> {
					// Handle exception
					context.packetHandler().disconnect(Component.translatable("jammies.networking.sync_dancing.failed", e.getMessage()));
					return null;
				});
	}
}
