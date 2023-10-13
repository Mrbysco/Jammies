package com.mrbysco.jammies.client;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class HumanoidKeyframeAnimations {
	public static void animate(HumanoidModel<?> humanoidModel, AnimationDefinition animationDefinition, long accumulatedTime, float scale, Vector3f animationVecCache) {
		float f = getElapsedSeconds(animationDefinition, accumulatedTime);

		for (Map.Entry<String, List<AnimationChannel>> entry : animationDefinition.boneAnimations().entrySet()) {
			Optional<ModelPart> optional = switch (entry.getKey()) {
				case "head" -> Optional.of(humanoidModel.head);
				case "hat" -> Optional.of(humanoidModel.hat);
				case "body" -> Optional.of(humanoidModel.body);
				case "right_arm" -> Optional.of(humanoidModel.rightArm);
				case "left_arm" -> Optional.of(humanoidModel.leftArm);
				case "right_leg" -> Optional.of(humanoidModel.rightLeg);
				case "left_leg" -> Optional.of(humanoidModel.leftLeg);
				default -> Optional.empty();
			};
			List<AnimationChannel> list = entry.getValue();
			optional.ifPresent((p_232330_) -> {
				list.forEach((p_288241_) -> {
					Keyframe[] akeyframe = p_288241_.keyframes();
					int i = Math.max(0, Mth.binarySearch(0, akeyframe.length, (p_232315_) -> {
						return f <= akeyframe[p_232315_].timestamp();
					}) - 1);
					int j = Math.min(akeyframe.length - 1, i + 1);
					Keyframe keyframe = akeyframe[i];
					Keyframe keyframe1 = akeyframe[j];
					float f1 = f - keyframe.timestamp();
					float f2;
					if (j != i) {
						f2 = Mth.clamp(f1 / (keyframe1.timestamp() - keyframe.timestamp()), 0.0F, 1.0F);
					} else {
						f2 = 0.0F;
					}

					keyframe1.interpolation().apply(animationVecCache, f2, akeyframe, i, j, scale);
					p_288241_.target().apply(p_232330_, animationVecCache);
				});
			});
		}

	}

	private static float getElapsedSeconds(AnimationDefinition animationDefinition, long accumulatedTime) {
		float f = (float) accumulatedTime / 1000.0F;
		return animationDefinition.looping() ? f % animationDefinition.lengthInSeconds() : f;
	}

	public static Vector3f posVec(float x, float y, float z) {
		return new Vector3f(x, -y, z);
	}

	public static Vector3f degreeVec(float xDegrees, float yDegrees, float zDegrees) {
		return new Vector3f(xDegrees * ((float) Math.PI / 180F), yDegrees * ((float) Math.PI / 180F), zDegrees * ((float) Math.PI / 180F));
	}

	public static Vector3f scaleVec(double xScale, double yScale, double zScale) {
		return new Vector3f((float) (xScale - 1.0D), (float) (yScale - 1.0D), (float) (zScale - 1.0D));
	}
}
