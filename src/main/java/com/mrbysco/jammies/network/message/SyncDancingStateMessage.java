package com.mrbysco.jammies.network.message;

import com.mrbysco.jammies.CapabilityHandler;
import com.mrbysco.jammies.capability.DancingCapability;
import com.mrbysco.jammies.capability.IDancingMob;
import com.mrbysco.jammies.client.DanceHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.HumanoidMobRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.neoforged.neoforge.network.NetworkEvent.Context;

public class SyncDancingStateMessage {
	private final int entityID;
	private final CompoundTag data;

	public SyncDancingStateMessage(int entityID, CompoundTag tag) {
		this.entityID = entityID;
		this.data = tag;
	}

	private SyncDancingStateMessage(FriendlyByteBuf buf) {
		this.entityID = buf.readInt();
		this.data = buf.readNbt();
	}

	public void encode(FriendlyByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeNbt(data);
	}

	public static SyncDancingStateMessage decode(final FriendlyByteBuf packetBuffer) {
		return new SyncDancingStateMessage(packetBuffer.readInt(), packetBuffer.readNbt());
	}

	public void handle(Context ctx) {
		ctx.enqueueWork(() -> {
			if (ctx.getDirection().getReceptionSide().isClient()) {
				Minecraft mc = Minecraft.getInstance();
				if (mc.level == null)
					return;
				Entity entity = mc.level.getEntity(entityID);
				IDancingMob cap = entity.getCapability(CapabilityHandler.DANCING_CAPABILITY).orElse(null);
				if (cap != null) {
					DancingCapability.readNBT(cap, data);
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
				}
			}
		});
		ctx.setPacketHandled(true);
	}
}
