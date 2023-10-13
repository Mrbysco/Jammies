package com.mrbysco.jammies.client;

import com.mrbysco.jammies.dancing.DancingMob;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.LivingEntity;
import org.joml.Vector3f;

public class DanceHandler {
	private static final Vector3f ANIMATION_VECTOR_CACHE = new Vector3f();

	public static void animateHumanoidDancing(DancingMob dancingMob, AnimationDefinition animationDefinition, HumanoidModel<? extends LivingEntity> humanoidModel, float ageInTicks, float speed) {
		AnimationState state = dancingMob.jammies$getDanceState();
		state.updateTime(ageInTicks, speed);
		state.ifStarted((animationState) -> {
			//Reset pos
			humanoidModel.head.resetPose();
			humanoidModel.hat.resetPose();
			humanoidModel.body.resetPose();
			humanoidModel.rightArm.resetPose();
			humanoidModel.leftArm.resetPose();
			humanoidModel.rightLeg.resetPose();
			humanoidModel.leftLeg.resetPose();

			HumanoidKeyframeAnimations.animate(humanoidModel, animationDefinition, animationState.getAccumulatedTime(), 1.0F, ANIMATION_VECTOR_CACHE);
		});
	}
}
