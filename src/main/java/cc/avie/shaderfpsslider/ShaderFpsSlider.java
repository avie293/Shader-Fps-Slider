package cc.avie.shaderfpsslider;

import net.fabricmc.api.ClientModInitializer;

import net.irisshaders.iris.api.v0.IrisApi;

import net.minecraft.resources.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ShaderFpsSlider implements ClientModInitializer {
	public static final String MOD_ID = "shader-fps-slider";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static boolean irisFailed = false;

	@Override
	public void onInitializeClient() {
		ShaderFpsConfig.load();
		LOGGER.info("Shader framerate limit: {}", ShaderFpsConfig.describe(ShaderFpsConfig.getLimit()));
	}

	public static int apply(int vanillaLimit) {
		int limit = ShaderFpsConfig.getLimit();
		if (limit == ShaderFpsConfig.OFF || !isShaderPackInUse()) {
			return vanillaLimit;
		}
		return limit;
	}

	private static boolean isShaderPackInUse() {
		if (irisFailed) {
			return false;
		}
		try {
			return IrisApi.getInstance().isShaderPackInUse();
		} catch (Throwable t) {
			irisFailed = true;
			LOGGER.error("Failed to query Iris, shader framerate limit disabled", t);
			return false;
		}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
