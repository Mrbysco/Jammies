package com.mrbysco.jammies.mixin.client;

import com.mrbysco.jammies.capability.DancingData;
import com.mrbysco.jammies.client.DanceHandler;
import com.mrbysco.jammies.client.JamAnimations;
import com.mrbysco.jammies.util.DanceUtil;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.model.SkeletonModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.entity.state.SkeletonRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(SkeletonModel.class)
public abstract class SkeletonModelMixin<S extends SkeletonRenderState> extends HumanoidModel<S> {
	protected SkeletonModelMixin(ModelPart modelPart) {
		super(modelPart);
	}

	@Inject(method = "Lnet/minecraft/client/model/SkeletonModel;setupAnim(Lnet/minecraft/client/renderer/entity/state/SkeletonRenderState;)V",
			at = @At(value = "HEAD")
	)
	public void jammies$setupAnim(S state, CallbackInfo ci) {
		DancingData cap = DanceUtil.getDancingAttachment(state);
		if (cap != null && cap.isDancing()) {
			//Reset pos
			head.resetPose();
			hat.resetPose();
			body.resetPose();
			rightArm.resetPose();
			leftArm.resetPose();
			rightLeg.resetPose();
			leftLeg.resetPose();
		}
	}

	@Inject(method = "setupAnim(Lnet/minecraft/client/renderer/entity/state/SkeletonRenderState;)V",
			at = @At(value = "TAIL")
	)
	public void jammies$setupAnim2(S state, CallbackInfo ci) {
		DanceHandler.doALittleDance(state, JamAnimations.SPOOKY, (SkeletonModel) (Object) this, 1.0F);
	}
}
