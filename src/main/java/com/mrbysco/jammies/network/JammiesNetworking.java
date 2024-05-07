package com.mrbysco.jammies.network;

import com.mrbysco.jammies.JammiesMod;
import com.mrbysco.jammies.network.handler.ClientPayloadHandler;
import com.mrbysco.jammies.network.message.SyncDancingStatePayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class JammiesNetworking {
	public static void setupPackets(final RegisterPayloadHandlersEvent event) {
		final PayloadRegistrar registrar = event.registrar(JammiesMod.MOD_ID);

		registrar.playToClient(SyncDancingStatePayload.ID, SyncDancingStatePayload.CODEC, ClientPayloadHandler.getInstance()::handleData);
	}
}
