package com.mrbysco.jammies.network;

import com.mrbysco.jammies.JammiesMod;
import com.mrbysco.jammies.network.handler.ClientPayloadHandler;
import com.mrbysco.jammies.network.message.SyncDancingStatePayload;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;

public class JammiesNetworking {
	public static void setupPackets(final RegisterPayloadHandlerEvent event) {
		final IPayloadRegistrar registrar = event.registrar(JammiesMod.MOD_ID);

		registrar.play(SyncDancingStatePayload.ID, SyncDancingStatePayload::new, handler -> handler
				.client(ClientPayloadHandler.getInstance()::handleData));
	}
}
