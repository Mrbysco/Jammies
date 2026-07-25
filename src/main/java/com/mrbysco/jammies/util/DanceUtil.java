package com.mrbysco.jammies.util;

import com.mrbysco.jammies.AttachmentHandler;
import com.mrbysco.jammies.JammiesMod;
import com.mrbysco.jammies.capability.DancingData;
import net.minecraft.client.renderer.entity.SkeletonRenderer;
import net.minecraft.client.renderer.entity.ZombieRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.context.ContextKey;
import net.minecraft.world.entity.Entity;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.renderstate.RegisterRenderStateModifiersEvent;
import org.jetbrains.annotations.Nullable;

@EventBusSubscriber(Dist.CLIENT)
public class DanceUtil {
	public static DancingData getDancingAttachment(Entity entity) {
		if (entity.is(JammiesMod.CAN_DANCE)) {
			return entity.getData(AttachmentHandler.DANCING);
		}
		return null;
	}

	@Nullable
	public static DancingData getDancingAttachment(LivingEntityRenderState renderState) {
		return renderState.getRenderData(DANCING_DATA);
	}

	public static void saveDancing(Entity entity, DancingData dancingData) {
		entity.setData(AttachmentHandler.DANCING, dancingData);
	}

	public static final ContextKey<DancingData> DANCING_DATA = new ContextKey<>(Identifier.fromNamespaceAndPath(JammiesMod.MOD_ID, "dancing_data"));

	@SubscribeEvent
	public static void registerCustomRenderData(RegisterRenderStateModifiersEvent event) {
		event.registerEntityModifier(ZombieRenderer.class, (zombie, renderState) -> {
			DancingData data = getDancingAttachment(zombie);
			if (data != null) {
				renderState.setRenderData(DANCING_DATA, data);
			}
		});
		event.registerEntityModifier(SkeletonRenderer.class, (skeleton, renderState) -> {
			DancingData data = getDancingAttachment(skeleton);
			if (data != null) {
				renderState.setRenderData(DANCING_DATA, data);
			}
		});
	}
}
