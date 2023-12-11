package com.mrbysco.jammies.network;

import com.mrbysco.jammies.JammiesMod;
import com.mrbysco.jammies.network.message.SyncDancingStateMessage;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.network.NetworkRegistry;
import net.neoforged.neoforge.network.simple.SimpleChannel;

public class JammiesNetworking {
	private static final String PROTOCOL_VERSION = "1";
	public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(JammiesMod.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals
	);

	private static int id = 0;

	public static void init() {
		CHANNEL.registerMessage(id++, SyncDancingStateMessage.class, SyncDancingStateMessage::encode, SyncDancingStateMessage::decode, SyncDancingStateMessage::handle);
	}
}
