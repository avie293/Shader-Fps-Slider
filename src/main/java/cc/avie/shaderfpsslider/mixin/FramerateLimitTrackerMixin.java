package cc.avie.shaderfpsslider.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.mojang.blaze3d.platform.FramerateLimitTracker;

import cc.avie.shaderfpsslider.ShaderFpsSlider;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(FramerateLimitTracker.class)
public class FramerateLimitTrackerMixin {
	@ModifyExpressionValue(
		method = "getFramerateLimit",
		at = @At(value = "FIELD", target = "Lcom/mojang/blaze3d/platform/FramerateLimitTracker;framerateLimit:I")
	)
	private int shader_fps_slider$useShaderLimit(int original) {
		return ShaderFpsSlider.apply(original);
	}
}
