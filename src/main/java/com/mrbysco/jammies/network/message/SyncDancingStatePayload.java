package com.mrbysco.jammies.network.message;

import com.mrbysco.jammies.JammiesMod;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncDancingStatePayload(int entityID, boolean dancing, long accumulatedTime, long lastTime) implements CustomPacketPayload {
	public static final ResourceLocation ID = new ResourceLocation(JammiesMod.MOD_ID, "sync_dancing");
	public SyncDancingStatePayload(final FriendlyByteBuf packetBuffer) {
		this(packetBuffer.readInt(), packetBuffer.readBoolean(), packetBuffer.readLong(), packetBuffer.readLong());
	}

	public void write(FriendlyByteBuf buf) {
		buf.writeInt(entityID);
		buf.writeBoolean(dancing);
		buf.writeLong(accumulatedTime);
		buf.writeLong(lastTime);
	}

	@Override
	public ResourceLocation id() {
		return ID;
	}
}
