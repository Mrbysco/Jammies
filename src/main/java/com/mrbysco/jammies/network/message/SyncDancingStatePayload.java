package com.mrbysco.jammies.network.message;

import com.mrbysco.jammies.JammiesMod;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.Identifier;

public record SyncDancingStatePayload(int entityID, boolean dancing, long accumulatedTime,
                                      long lastTime) implements CustomPacketPayload {
	public static final StreamCodec<FriendlyByteBuf, SyncDancingStatePayload> CODEC = StreamCodec.composite(
			ByteBufCodecs.INT,
			SyncDancingStatePayload::entityID,
			ByteBufCodecs.BOOL,
			SyncDancingStatePayload::dancing,
			ByteBufCodecs.LONG,
			SyncDancingStatePayload::accumulatedTime,
			ByteBufCodecs.LONG,
			SyncDancingStatePayload::lastTime,
			SyncDancingStatePayload::new);
	public static final Type<SyncDancingStatePayload> ID = new Type<>(Identifier.fromNamespaceAndPath(JammiesMod.MOD_ID, "sync_dancing"));

	@Override
	public Type<? extends CustomPacketPayload> type() {
		return ID;
	}
}
