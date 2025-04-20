package com.mrbysco.jammies.client;

import com.mrbysco.jammies.capability.DancingData;
import com.mrbysco.jammies.capability.IDancingMob;
import com.mrbysco.jammies.util.DanceUtil;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class DanceHandler {
	private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

	public static void doALittleDance(LivingEntityRenderState state, AnimationDefinition animationDefinition,
	                                  HumanoidModel<? extends HumanoidRenderState> humanoidModel, float speed) {
		DancingData cap = DanceUtil.getDancingAttachment(state);
		if (cap != null) {
//			System.out.println(cap.isDancing() + " "  + renderState);
			if (cap.isDancing())
				DanceHandler.animateHumanoidDancing(cap, animationDefinition, humanoidModel, state.ageInTicks, speed);
		}
	}

	public static void animateHumanoidDancing(IDancingMob cap, AnimationDefinition animationDefinition, HumanoidModel<? extends HumanoidRenderState> humanoidModel, float ageInTicks, float speed) {
		cap.updateTime(ageInTicks, speed);
		cap.ifStarted((animationState) -> {
			//Reset pos
			resetModel(humanoidModel);

			HumanoidKeyframeAnimations.animate(humanoidModel, animationDefinition, animationState.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
		});
	}

	public static void resetModel(HumanoidModel<? extends HumanoidRenderState> humanoidModel) {
		humanoidModel.head.resetPose();
		humanoidModel.hat.resetPose();
		humanoidModel.body.resetPose();
		humanoidModel.rightArm.resetPose();
		humanoidModel.leftArm.resetPose();
		humanoidModel.rightLeg.resetPose();
		humanoidModel.leftLeg.resetPose();
	}
}
