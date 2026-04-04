package com.mrbysco.jammies.network.message;

import com.mrbysco.jammies.JammiesMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SyncDancingStatePayload(int entityID, boolean dancing, long accumulatedTime,
                                      long lastTime) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, SyncDancingStatePayload> CODEC = CustomPacketPayload.codec(
			SyncDancingStatePayload::write,
			SyncDancingStatePayload::new);
	public static final Type<SyncDancingStatePayload> ID = new Type<>(Identifier.fromNamespaceAndPath(JammiesMod.MOD_ID, "sync_dancing"));

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
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
