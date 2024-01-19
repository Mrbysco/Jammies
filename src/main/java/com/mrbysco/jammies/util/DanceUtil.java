package com.mrbysco.jammies.util;

import com.mrbysco.jammies.AttachmentHandler;
import com.mrbysco.jammies.JammiesMod;
import com.mrbysco.jammies.capability.DancingData;
import net.minecraft.world.entity.Entity;

public class DanceUtil {
	public static DancingData getDancingAttachment(Entity entity) {
		if (entity.getType().is(JammiesMod.CAN_DANCE)) {
			return entity.getData(AttachmentHandler.DANCING);
		}
		return null;
	}

	public static void saveDancing(Entity entity, DancingData dancingData) {
		entity.setData(AttachmentHandler.DANCING, dancingData);
	}
}
